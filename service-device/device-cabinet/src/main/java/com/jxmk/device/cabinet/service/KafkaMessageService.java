package com.jxmk.device.cabinet.service;

import com.jxmk.device.cabinet.entity.CabinetTask;

public interface KafkaMessageService {
    
    /**
     * 发送远程控制消息
     */
    void sendRemoteControlMessage(CabinetTask task);
} 