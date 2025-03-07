package com.jxmk.connection.cabinet.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseMessage {
    /**
     * 消息类型
     */
    private Integer msgType;

    /**
     * 设备ID
     */
    private String devId;

    /**
     * 交易流水号
     */
    private String txnNo;
}