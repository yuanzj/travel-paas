package com.jxmk.device.cabinet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jxmk.device.cabinet.dto.CabinetCallbackRequest;
import com.jxmk.device.cabinet.entity.CabinetTask;
import com.jxmk.device.cabinet.enums.TaskStatusEnum;
import com.jxmk.device.cabinet.service.CabinetTaskService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskStatusCheckServiceImpl {

    private final CabinetTaskService cabinetTaskService;
    private final RedissonClient redissonClient;
    private final ScheduledExecutorService scheduledExecutorService;
    private final RestTemplate restTemplate;
    private final ThreadPoolTaskExecutor callbackExecutor;
    
    private static final String LOCK_KEY = "cabinet:task:status:check:lock";
    
    @PostConstruct
    public void init() {
        // 延迟1分钟后开始执行，每1分钟执行一次
        scheduledExecutorService.scheduleWithFixedDelay(
            this::checkTaskStatus,
            1,
            1,
            TimeUnit.MINUTES
        );
        log.info("任务状态检查定时任务已启动");
    }
    
    @PreDestroy
    public void destroy() {
        scheduledExecutorService.shutdown();
        try {
            if (!scheduledExecutorService.awaitTermination(1, TimeUnit.SECONDS)) {
                scheduledExecutorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public void checkTaskStatus() {
        RLock lock = redissonClient.getLock(LOCK_KEY);
        try {
            // 尝试获取锁，等待5秒，持有锁60秒
            if (lock.tryLock(5, 60, TimeUnit.SECONDS)) {
                try {
                    doCheck();
                } finally {
                    if (lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                }
            } else {
                log.debug("未获取到分布式锁，跳过本次任务状态检查");
            }
        } catch (InterruptedException e) {
            log.error("获取分布式锁异常", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("任务状态检查异常", e);
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void doCheck() {
        LocalDateTime timeoutPoint = LocalDateTime.now().minusMinutes(3);
        
        // 先查询需要更新的任务
        LambdaQueryWrapper<CabinetTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CabinetTask::getTaskStatus, TaskStatusEnum.PENDING.getCode())
                .le(CabinetTask::getCreateTime, timeoutPoint)
                .isNotNull(CabinetTask::getNotifyUrl);
        List<CabinetTask> timeoutTasks = cabinetTaskService.list(queryWrapper);
        
        if (!timeoutTasks.isEmpty()) {
            // 批量更新状态
            LambdaUpdateWrapper<CabinetTask> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(CabinetTask::getTaskStatus, TaskStatusEnum.PENDING.getCode())
                    .le(CabinetTask::getCreateTime, timeoutPoint)
                    .set(CabinetTask::getTaskStatus, TaskStatusEnum.FAILED.getCode())
                    .set(CabinetTask::getFailedCode, -1)
                    .set(CabinetTask::getFailedTime, LocalDateTime.now());
            
            boolean updated = cabinetTaskService.update(updateWrapper);
            
            if (updated) {
                log.info("已将超过3分钟未开始的任务更新为失败状态，更新时间：{}", timeoutPoint);
                // 异步发送回调通知
                timeoutTasks.forEach(task -> 
                    callbackExecutor.execute(() -> sendTimeoutCallback(task)));
            }
        }
    }
    
    private void sendTimeoutCallback(CabinetTask task) {
        try {
            CabinetCallbackRequest request = new CabinetCallbackRequest();
            request.setOpenId(task.getRequestUserId());
            request.setRequestId(task.getRequestId());
            request.setResult("fail");
            request.setErrMsg("任务执行超时");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<CabinetCallbackRequest> entity = new HttpEntity<>(request, headers);

            restTemplate.postForEntity(task.getNotifyUrl(), entity, String.class);
            log.info("超时任务回调通知成功：taskId={}, url={}", task.getTaskId(), task.getNotifyUrl());
        } catch (Exception e) {
            log.error("超时任务回调通知失败：taskId={}, url={}", task.getTaskId(), task.getNotifyUrl(), e);
        }
    }
} 