package com.jxmk.connection.cabinet.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jxmk.connection.cabinet.model.AlarmReport;
import com.jxmk.connection.cabinet.model.Response;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmReportHandler implements MessageHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(ChannelHandlerContext ctx, String message) throws Exception {
        AlarmReport report = objectMapper.readValue(message, AlarmReport.class);

        report.getAlarmList().forEach(alarm ->
                log.info("收到设备 {} 的告警: 信号量ID={}, 描述={}, 状态={}, 柜门={}, 电池={}",
                        report.getDevId(),
                        alarm.getId(),
                        alarm.getAlarmDesc(),
                        alarm.getAlarmFlag() == 1 ? "开始" : "结束",
                        alarm.getDoorId(),
                        alarm.getBatteryId()
                )
        );

        Response response = Response.builder()
                .msgType(411)
                .devId(report.getDevId())
                .result(1)
                .txnNo(report.getTxnNo())
                .build();

        ctx.writeAndFlush(objectMapper.writeValueAsString(response));
    }

    @Override
    public int getMessageType() {
        return 410;
    }
} 