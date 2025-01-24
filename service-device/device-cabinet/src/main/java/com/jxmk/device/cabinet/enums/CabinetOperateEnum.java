package com.jxmk.device.cabinet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 电柜操作枚举
 */
@Getter
@AllArgsConstructor
public enum CabinetOperateEnum {

    CABINET_ENABLE(1, "电柜启用"),
    CABINET_DISABLE(2, "电柜禁用"),
    CABINET_OPEN(3, "电柜开门"),
    GATE_ENABLE(4, "电柜仓门启用"),
    GATE_DISABLE(5, "电柜仓门禁用"),
    GET_BATTERY(6, "取出电池"),
    EXCHANGE_BATTERY(7, "更换电池"),
    RETURN_BATTERY(8, "归还电池");

    /**
     * 操作代码
     */
    private final Integer code;

    /**
     * 操作描述
     */
    private final String desc;

    /**
     * 根据代码获取枚举
     */
    public static CabinetOperateEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (CabinetOperateEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
} 