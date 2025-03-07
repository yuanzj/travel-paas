package com.jxmk.connection.cabinet.handler.message;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class MessageHandlerFactory {

    private final Map<Integer, MessageHandler> handlerMap;

    /**
     * 构造方法，Spring会自动注入所有MessageHandler实现类
     */
    public MessageHandlerFactory(List<MessageHandler> handlers) {
        this.handlerMap = handlers.stream()
                .collect(Collectors.toMap(
                        MessageHandler::getMessageType,
                        Function.identity()));
    }

    /**
     * 根据消息类型获取对应的处理器
     */
    public MessageHandler getHandler(Integer messageType) {
        return handlerMap.get(messageType);
    }
}