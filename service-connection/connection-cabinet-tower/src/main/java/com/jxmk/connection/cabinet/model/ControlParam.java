package com.jxmk.connection.cabinet.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ControlParam {
    private String id;           // 信号量ID
    private String value;        // 参数值
    private String doorId;       // 柜门ID，可选
    private String batteryId;    // 电池设备ID，可选
    private String userId;       // App用户id，首放、换电、退电流程必填
    private String voltage;      // 电压等级，可选
    private String scanBattery;  // 电池是否支持通讯，换电流程必填
} 