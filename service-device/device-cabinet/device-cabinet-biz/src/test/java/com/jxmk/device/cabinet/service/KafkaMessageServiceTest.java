package com.jxmk.device.cabinet.service;

import com.jxmk.device.cabinet.api.entity.CabinetTask;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.time.LocalDateTime;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"cabinet-remote-control"})
class KafkaMessageServiceTest {

    @Resource
    private KafkaMessageService kafkaMessageService;

    @Test
    void testSendRemoteControlMessage() {
        CabinetTask task = new CabinetTask();
        task.setTaskId(1L);
        task.setTaskType(1);
        task.setTaskStatus(0);
        task.setCabinetSn("TEST001");
        task.setCreateTime(LocalDateTime.now());
        task.setUpdateTime(LocalDateTime.now());
        
        kafkaMessageService.sendRemoteControlMessage(task);
    }
} 