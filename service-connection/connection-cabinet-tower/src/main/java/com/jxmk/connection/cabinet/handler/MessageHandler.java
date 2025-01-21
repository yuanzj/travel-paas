package com.jxmk.connection.cabinet.handler;

import io.netty.channel.ChannelHandlerContext;

public interface MessageHandler {
    void handle(ChannelHandlerContext ctx, String message) throws Exception;

    int getMessageType();
}