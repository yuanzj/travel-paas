package com.jxmk.connection.cabinet.model;

import lombok.Data;

@Data
public class Alarm {
    private String id;           // 信号量ID
    private Long alarmTime;      // 告警时间
    private String alarmDesc;    // 告警事件描述
    private Integer alarmFlag;   // 告警标识 1：开始 0：结束
    private String doorId;       // 柜门ID，可选
    private String batteryId;    // 电池设备ID，可选
} 