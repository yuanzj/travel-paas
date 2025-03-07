package com.jxmk.connection.cabinet.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jxmk.connection.cabinet.handler.task.TaskHandler;
import com.jxmk.connection.cabinet.handler.task.TaskHandlerFactory;
import com.jxmk.device.cabinet.api.constant.KafkaConstants;
import com.jxmk.device.cabinet.api.entity.CabinetTask;
import com.jxmk.device.cabinet.api.enums.CabinetOperateEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CabinetTaskListener {

    private final ObjectMapper objectMapper;
    private final TaskHandlerFactory taskHandlerFactory;

    @KafkaListener(topics = KafkaConstants.TOPIC_CABINET_REMOTE_CONTROL)
    public void onMessage(String message) {
        try {
            log.info("收到远程控制任务：{}", message);
            CabinetTask task = objectMapper.readValue(message, CabinetTask.class);

            CabinetOperateEnum operate = CabinetOperateEnum.getByCode(task.getTaskType());
            TaskHandler handler = taskHandlerFactory.getHandler(operate);

            if (handler != null) {
                handler.handle(task);
            } else {
                log.warn("未找到任务处理器：taskType={}", task.getTaskType());
            }
        } catch (Exception e) {
            log.error("处理远程控制任务失败：{}", message, e);
        }
    }
}