package com.jxmk.device.cabinet.api.constant;

/**
 * Kafka 常量
 */
public interface KafkaConstants {

    /**
     * 远程控制任务的Kafka主题
     */
    String TOPIC_CABINET_REMOTE_CONTROL = "cabinet-remote-control";

    /**
     * 任务完成响应的Kafka主题
     */
    String TOPIC_RC_RESPONSE = "rc-response";
} 