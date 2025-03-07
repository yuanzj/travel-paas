package com.jxmk.connection.cabinet.handler.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jxmk.connection.cabinet.enums.MessageTypeEnum;
import com.jxmk.connection.cabinet.model.AttributeReport;
import com.jxmk.connection.cabinet.model.Response;
import com.jxmk.connection.cabinet.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AttributeReportHandler extends AbstractMessageHandler<AttributeReport> {

    public AttributeReportHandler(
            ObjectMapper objectMapper,
            DeviceService deviceService,
            RedisTemplate<String, Object> redisTemplate,
            KafkaTemplate<String, Object> kafkaTemplate) {
        super(objectMapper, deviceService, redisTemplate, kafkaTemplate);
    }

    @Override
    protected Class<AttributeReport> getMessageClass() {
        return AttributeReport.class;
    }

    @Override
    protected void handleMessage(AttributeReport report) {
        log.info("收到设备 {} 的属性上报，是否全量：{}", report.getDevId(), report.getIsFull());
        report.getAttrList().forEach(attr -> log.info("信号量ID: {}, 值: {}, 柜门ID: {}",
                attr.getId(), attr.getValue(), attr.getDoorId()));
    }

    @Override
    protected Response buildResponse(AttributeReport request) {
        return Response.builder()
                .msgType(MessageTypeEnum.ATTRIBUTE_REPORT_RESPONSE.getCode())
                .devId(request.getDevId())
                .result(1)
                .txnNo(request.getTxnNo())
                .build();
    }

    @Override
    public int getMessageType() {
        return MessageTypeEnum.ATTRIBUTE_REPORT_REQUEST.getCode();
    }
}