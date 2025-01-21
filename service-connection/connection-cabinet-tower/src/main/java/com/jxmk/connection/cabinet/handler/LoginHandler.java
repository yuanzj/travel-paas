package com.jxmk.connection.cabinet.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jxmk.connection.cabinet.TcpServerHandler;
import com.jxmk.connection.cabinet.model.LoginRequest;
import com.jxmk.connection.cabinet.model.Response;
import com.jxmk.connection.cabinet.service.DeviceService;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginHandler implements MessageHandler {

    private final ObjectMapper objectMapper;
    private final DeviceService deviceService;

    @Override
    public void handle(ChannelHandlerContext ctx, String message) throws Exception {
        LoginRequest request = objectMapper.readValue(message, LoginRequest.class);

        if (ctx.handler() instanceof TcpServerHandler) {
            ((TcpServerHandler) ctx.handler()).setDeviceId(request.getDevId());
        }

        deviceService.addChannel(request.getDevId(), ctx.channel());

        Response response = Response.builder()
                .msgType(111)
                .devId(request.getDevId())
                .result(1)
                .txnNo(request.getTxnNo())
                .build();

        String jsonResponse = objectMapper.writeValueAsString(response);
        ctx.writeAndFlush(jsonResponse);
        log.info("设备 {} 登录成功", request.getDevId());
    }

    @Override
    public int getMessageType() {
        return 110;
    }
} 