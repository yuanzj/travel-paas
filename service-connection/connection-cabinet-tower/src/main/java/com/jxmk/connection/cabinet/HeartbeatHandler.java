package com.jxmk.connection.cabinet;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {
    private static final int READ_IDLE_TIME = 10;  // 读超时时间
    private static final int WRITE_IDLE_TIME = 5; // 写超时时间

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 添加IdleStateHandler
        ctx.pipeline().addFirst(new IdleStateHandler(
                READ_IDLE_TIME,
                WRITE_IDLE_TIME,
                0,
                TimeUnit.MINUTES));
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent event) {
            switch (event.state()) {
                case READER_IDLE:
                    handleReaderIdle(ctx);
                    break;
                case WRITER_IDLE:
                    handleWriterIdle(ctx);
                    break;
                case ALL_IDLE:
                    break;
            }
        }
    }

    private void handleReaderIdle(ChannelHandlerContext ctx) {
        log.warn("读空闲超时，关闭连接：{}", ctx.channel().remoteAddress());
        ctx.close();
    }

    private void handleWriterIdle(ChannelHandlerContext ctx) {
        // todo 发送参数查询用于维持长连接
        // ctx.writeAndFlush("");
    }
}