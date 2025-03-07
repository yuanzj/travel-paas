package com.jxmk.connection.cabinet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 电柜信号量枚举
 */
@Getter
@AllArgsConstructor
public enum CabinetSignalEnum {

    /**
     * 电柜操作-取电
     */
    TAKE_BATTERY("02301001", "11", "取电操作"),

    /**
     * 电柜操作-退电
     */
    RETURN_BATTERY("02301001", "12", "退电操作"),

    /**
     * 电柜操作-换电
     */
    EXCHANGE_BATTERY("02301001", "01", "换电操作");

    /**
     * 信号量ID
     */
    private final String signalId;

    /**
     * 信号量扩展
     */
    private final String extension;

    /**
     * 信号量描述
     */
    private final String desc;
} 