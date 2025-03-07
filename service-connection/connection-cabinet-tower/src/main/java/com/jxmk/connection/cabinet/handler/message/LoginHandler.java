package com.jxmk.connection.cabinet.handler.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jxmk.connection.cabinet.enums.MessageTypeEnum;
import com.jxmk.connection.cabinet.model.LoginRequest;
import com.jxmk.connection.cabinet.model.Response;
import com.jxmk.connection.cabinet.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoginHandler extends AbstractMessageHandler<LoginRequest> {

    public LoginHandler(
            ObjectMapper objectMapper,
            DeviceService deviceService,
            RedisTemplate<String, Object> redisTemplate,
            KafkaTemplate<String, Object> kafkaTemplate) {
        super(objectMapper, deviceService, redisTemplate, kafkaTemplate);
    }

    @Override
    protected Class<LoginRequest> getMessageClass() {
        return LoginRequest.class;
    }

    @Override
    protected void handleMessage(LoginRequest request) {
        log.info("收到设备 {} 的登录请求", request.getDevId());
    }

    @Override
    protected Response buildResponse(LoginRequest request) {
        return Response.builder()
                .msgType(MessageTypeEnum.LOGIN_RESPONSE.getCode())
                .devId(request.getDevId())
                .result(1)
                .txnNo(request.getTxnNo())
                .build();
    }

    @Override
    public int getMessageType() {
        return MessageTypeEnum.LOGIN_REQUEST.getCode();
    }
}