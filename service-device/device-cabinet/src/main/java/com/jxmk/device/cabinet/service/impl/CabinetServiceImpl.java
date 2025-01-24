package com.jxmk.device.cabinet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxmk.device.cabinet.dto.CabinetOperateRequest;
import com.jxmk.device.cabinet.dto.CabinetOperateResponse;
import com.jxmk.device.cabinet.entity.Cabinet;
import com.jxmk.device.cabinet.entity.CabinetTask;
import com.jxmk.device.cabinet.entity.CabinetGate;
import com.jxmk.device.cabinet.dto.CabinetImport;
import com.jxmk.device.cabinet.mapper.CabinetMapper;
import com.jxmk.device.cabinet.service.CabinetService;
import com.jxmk.device.cabinet.service.CabinetTaskService;
import com.jxmk.device.cabinet.service.KafkaMessageService;
import com.jxmk.device.cabinet.service.CabinetGateService;
import com.jxmk.device.cabinet.enums.CabinetOperateEnum;
import com.jxmk.device.cabinet.enums.TaskStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CabinetServiceImpl extends ServiceImpl<CabinetMapper, Cabinet> implements CabinetService {

    private final CabinetTaskService cabinetTaskService;
    private final KafkaMessageService kafkaMessageService;
    private final CabinetGateService cabinetGateService;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    private static final String CABINET_KEY_PREFIX = "cabinet:info:";
    private static final String GATE_KEY_PREFIX = "cabinet:gate:";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CabinetOperateResponse getBattery(CabinetOperateRequest request) {
        // 1. 创建任务
        CabinetTask task = new CabinetTask();
        task.setTaskType(CabinetOperateEnum.GET_BATTERY.getCode());
        task.setTaskStatus(TaskStatusEnum.PENDING.getCode());
        task.setCabinetSn(request.getCabinetNo());
        task.setRequestGateNo(request.getGateNo());
        task.setRequestId(request.getRequestId());
        task.setRequestUserId(request.getOpenId());
        task.setNotifyUrl(request.getNotifyUrl());
        cabinetTaskService.save(task);
        
        // 2. 发送远程控制消息
        kafkaMessageService.sendRemoteControlMessage(task);
        
        // 3. 返回响应
        CabinetOperateResponse response = new CabinetOperateResponse();
        response.setResult("success");
        response.setRequestId(request.getRequestId());
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CabinetOperateResponse exchangeBattery(CabinetOperateRequest request) {
        // 1. 创建任务
        CabinetTask task = new CabinetTask();
        task.setTaskType(CabinetOperateEnum.EXCHANGE_BATTERY.getCode());
        task.setTaskStatus(TaskStatusEnum.PENDING.getCode());
        task.setCabinetSn(request.getCabinetNo());
        task.setRequestId(request.getRequestId());
        task.setRequestUserId(request.getOpenId());
        task.setNotifyUrl(request.getNotifyUrl());
        cabinetTaskService.save(task);
        
        // 2. 发送远程控制消息
        kafkaMessageService.sendRemoteControlMessage(task);
        
        // 3. 返回响应
        CabinetOperateResponse response = new CabinetOperateResponse();
        response.setResult("success");
        response.setRequestId(request.getRequestId());
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CabinetOperateResponse returnBattery(CabinetOperateRequest request) {
        // 1. 创建任务
        CabinetTask task = new CabinetTask();
        task.setTaskType(CabinetOperateEnum.RETURN_BATTERY.getCode());
        task.setTaskStatus(TaskStatusEnum.PENDING.getCode());
        task.setCabinetSn(request.getCabinetNo());
        task.setRequestId(request.getRequestId());
        task.setRequestUserId(request.getOpenId());
        task.setNotifyUrl(request.getNotifyUrl());
        cabinetTaskService.save(task);
        
        // 2. 发送远程控制消息
        kafkaMessageService.sendRemoteControlMessage(task);
        
        // 3. 返回响应
        CabinetOperateResponse response = new CabinetOperateResponse();
        response.setResult("success");
        response.setRequestId(request.getRequestId());
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CabinetOperateResponse enableCabinet(CabinetOperateRequest request) {
        // 1. 创建任务
        CabinetTask task = new CabinetTask();
        task.setTaskType(CabinetOperateEnum.CABINET_ENABLE.getCode());
        task.setTaskStatus(TaskStatusEnum.PENDING.getCode());
        task.setCabinetSn(request.getCabinetNo());
        task.setRequestId(request.getRequestId());
        task.setRequestUserId(request.getOpenId());
        task.setNotifyUrl(request.getNotifyUrl());
        cabinetTaskService.save(task);
        
        // 2. 发送远程控制消息
        kafkaMessageService.sendRemoteControlMessage(task);
        
        // 3. 返回响应
        CabinetOperateResponse response = new CabinetOperateResponse();
        response.setResult("success");
        response.setRequestId(request.getRequestId());
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CabinetOperateResponse disableCabinet(CabinetOperateRequest request) {
        // 1. 创建任务
        CabinetTask task = new CabinetTask();
        task.setTaskType(CabinetOperateEnum.CABINET_DISABLE.getCode());
        task.setTaskStatus(TaskStatusEnum.PENDING.getCode());
        task.setCabinetSn(request.getCabinetNo());
        task.setRequestId(request.getRequestId());
        task.setRequestUserId(request.getOpenId());
        task.setNotifyUrl(request.getNotifyUrl());
        cabinetTaskService.save(task);
        
        // 2. 发送远程控制消息
        kafkaMessageService.sendRemoteControlMessage(task);
        
        // 3. 返回响应
        CabinetOperateResponse response = new CabinetOperateResponse();
        response.setResult("success");
        response.setRequestId(request.getRequestId());
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CabinetOperateResponse openCabinet(CabinetOperateRequest request) {
        // 1. 创建任务
        CabinetTask task = new CabinetTask();
        task.setTaskType(CabinetOperateEnum.CABINET_OPEN.getCode());
        task.setTaskStatus(TaskStatusEnum.PENDING.getCode());
        task.setCabinetSn(request.getCabinetNo());
        task.setRequestId(request.getRequestId());
        task.setRequestUserId(request.getOpenId());
        task.setNotifyUrl(request.getNotifyUrl());
        cabinetTaskService.save(task);
        
        // 2. 发送远程控制消息
        kafkaMessageService.sendRemoteControlMessage(task);
        
        // 3. 返回响应
        CabinetOperateResponse response = new CabinetOperateResponse();
        response.setResult("success");
        response.setRequestId(request.getRequestId());
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CabinetOperateResponse enableGate(CabinetOperateRequest request) {
        // 1. 创建任务
        CabinetTask task = new CabinetTask();
        task.setTaskType(CabinetOperateEnum.GATE_ENABLE.getCode());
        task.setTaskStatus(TaskStatusEnum.PENDING.getCode());
        task.setCabinetSn(request.getCabinetNo());
        task.setRequestGateNo(request.getGateNo());
        task.setRequestId(request.getRequestId());
        task.setRequestUserId(request.getOpenId());
        task.setNotifyUrl(request.getNotifyUrl());
        cabinetTaskService.save(task);
        
        // 2. 发送远程控制消息
        kafkaMessageService.sendRemoteControlMessage(task);
        
        // 3. 返回响应
        CabinetOperateResponse response = new CabinetOperateResponse();
        response.setResult("success");
        response.setRequestId(request.getRequestId());
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CabinetOperateResponse disableGate(CabinetOperateRequest request) {
        // 1. 创建任务
        CabinetTask task = new CabinetTask();
        task.setTaskType(CabinetOperateEnum.GATE_DISABLE.getCode());
        task.setTaskStatus(TaskStatusEnum.PENDING.getCode());
        task.setCabinetSn(request.getCabinetNo());
        task.setRequestGateNo(request.getGateNo());
        task.setRequestId(request.getRequestId());
        task.setRequestUserId(request.getOpenId());
        task.setNotifyUrl(request.getNotifyUrl());
        cabinetTaskService.save(task);
        
        // 2. 发送远程控制消息
        kafkaMessageService.sendRemoteControlMessage(task);
        
        // 3. 返回响应
        CabinetOperateResponse response = new CabinetOperateResponse();
        response.setResult("success");
        response.setRequestId(request.getRequestId());
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public void importCabinet(List<CabinetImport> list) {
        // 1. 校验必填字段
        list.forEach(dto -> {
            if (dto.getManufacturer() == null || dto.getModel() == null || 
                dto.getCabinetSn() == null || dto.getGateCount() == null || 
                dto.getProtocol() == null) {
                throw new IllegalArgumentException("存在必填字段为空");
            }
        });

        // 2. 检查设备号是否已存在
        List<String> cabinetSns = list.stream()
                .map(CabinetImport::getCabinetSn)
                .collect(Collectors.toList());
        
        LambdaQueryWrapper<Cabinet> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Cabinet::getCabinetSn, cabinetSns);
        List<Cabinet> existCabinets = this.list(queryWrapper);
        
        if (!existCabinets.isEmpty()) {
            throw new IllegalArgumentException("设备号已存在：" + 
                existCabinets.stream()
                    .map(Cabinet::getCabinetSn)
                    .collect(Collectors.joining(",")));
        }

        // 3. 批量保存电柜和仓门信息
        for (CabinetImport dto : list) {
            // 保存电柜信息
            Cabinet cabinet = new Cabinet();
            BeanUtils.copyProperties(dto, cabinet);
            this.save(cabinet);

            try {
                // 存储电柜信息到Redis
                String cabinetJson = objectMapper.writeValueAsString(cabinet);
                stringRedisTemplate.opsForValue().set(
                    CABINET_KEY_PREFIX + cabinet.getCabinetSn(), 
                    cabinetJson
                );

                // 初始化仓门信息
                List<CabinetGate> gates = new ArrayList<>();
                for (int i = 1; i <= dto.getGateCount(); i++) {
                    CabinetGate gate = new CabinetGate();
                    gate.setCabinetSn(dto.getCabinetSn());
                    gate.setGateNo(i);
                    gates.add(gate);
                }
                
                // 批量保存仓门信息到数据库
                cabinetGateService.saveBatch(gates);

                // 将所有仓门信息一次性存储到Redis
                String gatesJson = objectMapper.writeValueAsString(gates);
                stringRedisTemplate.opsForValue().set(
                    GATE_KEY_PREFIX + cabinet.getCabinetSn(),
                    gatesJson
                );
                
            } catch (Exception e) {
                log.error("JSON序列化或Redis存储失败", e);
                throw new RuntimeException("JSON序列化或Redis存储失败", e);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(Cabinet cabinet) {
        boolean result = super.save(cabinet);
        if (result) {
            try {
                // 保存到Redis
                String cabinetJson = objectMapper.writeValueAsString(cabinet);
                stringRedisTemplate.opsForValue().set(
                    CABINET_KEY_PREFIX + cabinet.getCabinetSn(), 
                    cabinetJson
                );
            } catch (Exception e) {
                log.error("Cabinet保存到Redis失败: {}", cabinet.getCabinetSn(), e);
                throw new RuntimeException("Cabinet保存到Redis失败", e);
            }
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(Cabinet cabinet) {
        boolean result = super.updateById(cabinet);
        if (result) {
            try {
                // 更新Redis
                String cabinetJson = objectMapper.writeValueAsString(cabinet);
                stringRedisTemplate.opsForValue().set(
                    CABINET_KEY_PREFIX + cabinet.getCabinetSn(), 
                    cabinetJson
                );
            } catch (Exception e) {
                log.error("Cabinet更新Redis失败: {}", cabinet.getCabinetSn(), e);
                throw new RuntimeException("Cabinet更新Redis失败", e);
            }
        }
        return result;
    }
} 