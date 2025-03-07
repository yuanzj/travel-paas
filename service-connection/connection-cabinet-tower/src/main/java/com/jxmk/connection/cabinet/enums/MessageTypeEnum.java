package com.jxmk.connection.cabinet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 报文类型枚举
 */
@Getter
@AllArgsConstructor
public enum MessageTypeEnum {

    /**
     * 登录请求
     */
    LOGIN_REQUEST(110, "登录请求", "设备->平台"),

    /**
     * 登录响应
     */
    LOGIN_RESPONSE(111, "登录响应", "平台->设备"),

    /**
     * 配置查询请求
     */
    CONFIG_QUERY_REQUEST(210, "配置查询请求", "平台->设备"),

    /**
     * 配置查询响应
     */
    CONFIG_QUERY_RESPONSE(211, "配置查询响应", "设备->平台"),

    /**
     * 属性上报请求
     */
    ATTRIBUTE_REPORT_REQUEST(310, "属性上报请求", "设备->平台"),

    /**
     * 属性上报响应
     */
    ATTRIBUTE_REPORT_RESPONSE(311, "属性上报响应", "平台->设备"),

    /**
     * 告警上报请求
     */
    ALARM_REPORT_REQUEST(410, "告警上报请求", "设备->平台"),

    /**
     * 告警上报响应
     */
    ALARM_REPORT_RESPONSE(411, "告警上报响应", "平台->设备"),

    /**
     * 远程控制请求
     */
    REMOTE_CONTROL_REQUEST(500, "远程控制请求", "平台->设备"),

    /**
     * 远程控制响应
     */
    REMOTE_CONTROL_RESPONSE(501, "远程控制响应", "设备->平台");

    /**
     * 类型编码
     */
    private final int code;

    /**
     * 报文动作
     */
    private final String action;

    /**
     * 数据流方向
     */
    private final String direction;

    /**
     * 根据编码获取枚举
     */
    public static MessageTypeEnum getByCode(int code) {
        for (MessageTypeEnum type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }
}