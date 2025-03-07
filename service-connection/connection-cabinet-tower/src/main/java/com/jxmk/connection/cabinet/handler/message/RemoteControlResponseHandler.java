package com.jxmk.connection.cabinet.handler.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jxmk.connection.cabinet.enums.MessageTypeEnum;
import com.jxmk.connection.cabinet.model.Response;
import com.jxmk.connection.cabinet.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RemoteControlResponseHandler extends AbstractMessageHandler<Response> {

    public RemoteControlResponseHandler(
            ObjectMapper objectMapper,
            DeviceService deviceService,
            RedisTemplate<String, Object> redisTemplate,
            KafkaTemplate<String, Object> kafkaTemplate) {
        super(objectMapper, deviceService, redisTemplate, kafkaTemplate);
    }

    @Override
    protected Class<Response> getMessageClass() {
        return Response.class;
    }

    @Override
    protected void handleMessage(Response response) {
        log.info("收到设备 {} 的远程控制响应: result={}",
                response.getDevId(),
                response.getResult() == 1 ? "成功" : "失败");
        deviceService.handleControlResponse(response);
    }

    @Override
    protected Response buildResponse(Response request) {
        return null; // 远程控制响应不需要回复
    }

    @Override
    public int getMessageType() {
        return MessageTypeEnum.REMOTE_CONTROL_RESPONSE.getCode();
    }
}