package com.jxmk.connection.cabinet.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jxmk.connection.cabinet.model.Response;
import com.jxmk.connection.cabinet.service.DeviceService;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RemoteControlResponseHandler implements MessageHandler {

    private final ObjectMapper objectMapper;
    private final DeviceService deviceService;

    @Override
    public void handle(ChannelHandlerContext ctx, String message) throws Exception {
        Response response = objectMapper.readValue(message, Response.class);
        log.info("收到设备 {} 的远程控制响应: result={}",
                response.getDevId(),
                response.getResult() == 1 ? "成功" : "失败"
        );

        deviceService.handleControlResponse(response);
    }

    @Override
    public int getMessageType() {
        return 501;
    }
} 