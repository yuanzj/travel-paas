package com.jxmk.connection.cabinet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 电柜操作失败枚举
 */
@Getter
@AllArgsConstructor
public enum CabinetFailureEnum {

    /**
     * 超时失败
     */
    TIMEOUT(-1001, "操作超时"),

    /**
     * 离线失败
     */
    OFFLINE(-1002, "设备离线"),

    /**
     * 未知原因失败
     */
    UNKNOWN(-9999, "未知错误");

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误描述
     */
    private final String desc;

    /**
     * 根据错误码获取枚举
     */
    public static CabinetFailureEnum getByCode(Integer code) {
        for (CabinetFailureEnum failure : values()) {
            if (failure.getCode().equals(code)) {
                return failure;
            }
        }
        return UNKNOWN;
    }
}