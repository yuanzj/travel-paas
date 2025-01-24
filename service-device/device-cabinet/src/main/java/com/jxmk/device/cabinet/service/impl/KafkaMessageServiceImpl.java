package com.jxmk.device.cabinet.service.impl;

import com.jxmk.device.cabinet.config.KafkaConfig;
import com.jxmk.device.cabinet.entity.CabinetTask;
import com.jxmk.device.cabinet.service.KafkaMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessageServiceImpl implements KafkaMessageService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendRemoteControlMessage(CabinetTask task) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(
            KafkaConfig.TOPIC_CABINET_REMOTE_CONTROL,
            task.getCabinetSn(),
            task
        );
        
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("发送远程控制消息成功：cabinetSn={}, taskId={}", task.getCabinetSn(), task.getTaskId());
            } else {
                log.error("发送远程控制消息失败：cabinetSn={}, taskId={}", task.getCabinetSn(), task.getTaskId(), ex);
            }
        });
    }
} 