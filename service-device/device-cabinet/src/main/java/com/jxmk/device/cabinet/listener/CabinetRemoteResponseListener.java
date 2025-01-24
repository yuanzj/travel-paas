package com.jxmk.device.cabinet.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jxmk.device.cabinet.config.KafkaConfig;
import com.jxmk.device.cabinet.dto.CabinetCallbackRequest;
import com.jxmk.device.cabinet.entity.CabinetTask;
import com.jxmk.device.cabinet.enums.TaskStatusEnum;
import com.jxmk.device.cabinet.service.CabinetTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class CabinetRemoteResponseListener {

    private final CabinetTaskService cabinetTaskService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ThreadPoolTaskExecutor callbackExecutor;

    @KafkaListener(topics = KafkaConfig.TOPIC_CABINET_REMOTE_RESPONSE)
    @Transactional(rollbackFor = Exception.class)
    public void onMessage(String message) {
        try {
            log.info("收到远程控制响应：{}", message);
            CabinetTask task = objectMapper.readValue(message, CabinetTask.class);
            
            // 更新任务状态
            cabinetTaskService.updateById(task);

            // 判断是否需要回调通知
            if (task.getNotifyUrl() != null && 
                (task.getTaskStatus().equals(TaskStatusEnum.SUCCESS.getCode()) || 
                 task.getTaskStatus().equals(TaskStatusEnum.FAILED.getCode()))) {
                callbackExecutor.execute(() -> sendCallback(task));
            }
        } catch (Exception e) {
            log.error("处理远程控制响应失败：{}", message, e);
        }
    }

    private void sendCallback(CabinetTask task) {
        try {
            CabinetCallbackRequest request = new CabinetCallbackRequest();
            request.setOpenId(task.getRequestUserId());
            request.setRequestId(task.getRequestId());
            
            if (task.getTaskStatus().equals(TaskStatusEnum.SUCCESS.getCode())) { // 成功
                request.setResult("success");
                
                // 如果是取电或换电，设置获取的电池信息
                if (task.getGetBatterySn() != null) {
                    CabinetCallbackRequest.BatteryInfo getBattery = new CabinetCallbackRequest.BatteryInfo();
                    getBattery.setCabinetSn(task.getCabinetSn());
                    getBattery.setGateNo(task.getGetGateNo());
                    getBattery.setBatterySn(task.getGetBatterySn());
                    getBattery.setBatterySoc(task.getGetBatterySoc());
                    request.setGetBattery(getBattery);
                }
                
                // 如果是换电或归还，设置归还的电池信息
                if (task.getPutBatterySn() != null) {
                    CabinetCallbackRequest.BatteryInfo putBattery = new CabinetCallbackRequest.BatteryInfo();
                    putBattery.setCabinetSn(task.getCabinetSn());
                    putBattery.setGateNo(task.getPutGateNo());
                    putBattery.setBatterySn(task.getPutBatterySn());
                    putBattery.setBatterySoc(task.getPutBatterySoc());
                    request.setPutBattery(putBattery);
                }
            } else { // 失败
                request.setResult("fail");
                request.setErrMsg("任务执行失败，错误码：" + task.getFailedCode());
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<CabinetCallbackRequest> entity = new HttpEntity<>(request, headers);

            restTemplate.postForEntity(task.getNotifyUrl(), entity, String.class);
            log.info("回调通知成功：taskId={}, url={}", task.getTaskId(), task.getNotifyUrl());
        } catch (Exception e) {
            log.error("回调通知失败：taskId={}, url={}", task.getTaskId(), task.getNotifyUrl(), e);
        }
    }
} 