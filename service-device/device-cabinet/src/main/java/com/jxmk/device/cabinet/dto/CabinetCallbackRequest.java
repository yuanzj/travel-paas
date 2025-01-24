package com.jxmk.device.cabinet.dto;

import lombok.Data;

@Data
public class CabinetCallbackRequest {
    private String openId;
    private String requestId;
    private String result;
    private BatteryInfo putBattery;
    private BatteryInfo getBattery;
    private String errMsg;

    @Data
    public static class BatteryInfo {
        private String cabinetSn;
        private Integer gateNo;
        private String batterySn;
        private Integer batterySoc;
    }
} 