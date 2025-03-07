package com.jxmk.device.cabinet.api.dto;

import lombok.Data;

@Data
public class CabinetOperateRequest {
    
    /**
     * 用户标识
     */
    private String openId;
    
    /**
     * 电柜编号
     */
    private String cabinetNo;
    
    /**
     * 仓门号
     */
    private Integer gateNo;

    /**
     * 电池电压
     */
    private String voltage;
    
    /**
     * 回调通知地址
     */
    private String notifyUrl;
    
    /**
     * 请求ID
     */
    private String requestId;
} 