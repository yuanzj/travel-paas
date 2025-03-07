package com.jxmk.connection.cabinet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jxmk.connection.cabinet.handler.message.MessageHandler;
import com.jxmk.connection.cabinet.handler.message.MessageHandlerFactory;
import com.jxmk.connection.cabinet.service.DeviceService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executor;

@Slf4j
@RequiredArgsConstructor
public class TcpServerHandler extends ChannelInboundHandlerAdapter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DeviceService deviceService;
    private final MessageHandlerFactory messageHandlerFactory;
    private final Executor messageHandlerExecutor;
    @Setter
    private String deviceId;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            String message = (String) msg;
            log.info("收到消息: {}", message);

            JsonNode jsonNode = objectMapper.readTree(message);
            int msgType = jsonNode.get("msgType").asInt();

            MessageHandler handler = messageHandlerFactory.getHandler(msgType);
            if (handler != null) {
                messageHandlerExecutor.execute(() -> {
                    try {
                        handler.handle(ctx, message);
                    } catch (Exception e) {
                        log.error("消息处理失败", e);
                        ctx.close();
                    }
                });
            } else {
                log.warn("未知的消息类型: {}", msgType);
            }

        } catch (Exception e) {
            log.error("消息处理失败", e);
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("处理过程中发生异常", cause);
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("客户端连接成功: {}", ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (deviceId != null) {
            deviceService.removeChannel(deviceId);
        }
        log.info("客户端断开连接: {}", ctx.channel().remoteAddress());
    }

}