package com.jxmk.connection.cabinet.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jxmk.connection.cabinet.model.AttributeReport;
import com.jxmk.connection.cabinet.model.Response;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AttributeReportHandler implements MessageHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(ChannelHandlerContext ctx, String message) throws Exception {
        AttributeReport report = objectMapper.readValue(message, AttributeReport.class);

        log.info("收到设备 {} 的属性上报，是否全量：{}", report.getDevId(), report.getIsFull());
        report.getAttrList().forEach(attr ->
                log.info("信号量ID: {}, 值: {}, 柜门ID: {}",
                        attr.getId(), attr.getValue(), attr.getDoorId())
        );

        Response response = Response.builder()
                .msgType(311)
                .devId(report.getDevId())
                .result(1)
                .txnNo(report.getTxnNo())
                .build();

        ctx.writeAndFlush(objectMapper.writeValueAsString(response));
    }

    @Override
    public int getMessageType() {
        return 310;
    }
} 