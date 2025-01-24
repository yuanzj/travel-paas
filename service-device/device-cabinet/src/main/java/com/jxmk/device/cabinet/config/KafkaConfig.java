package com.jxmk.device.cabinet.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
public class KafkaConfig {
    public static final String TOPIC_CABINET_REMOTE_CONTROL = "cabinet-remote-control";
    public static final String TOPIC_CABINET_REMOTE_RESPONSE = "rc-response";
} 