package com.jxmk.connection.cabinet.config;

import com.jxmk.connection.cabinet.handler.MessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class HandlerConfig {

    @Bean
    public Map<Integer, MessageHandler> messageHandlers(List<MessageHandler> handlers) {
        return handlers.stream()
                .collect(Collectors.toMap(
                        MessageHandler::getMessageType,
                        Function.identity()
                ));
    }
} 