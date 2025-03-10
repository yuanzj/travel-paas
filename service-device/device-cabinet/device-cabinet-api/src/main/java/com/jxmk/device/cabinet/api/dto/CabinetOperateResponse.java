package com.jxmk.device.cabinet.api.dto;

import lombok.Data;

@Data
public class CabinetOperateResponse {
    
    /**
     * 操作结果
     */
    private String result;
    
    /**
     * 请求ID
     */
    private String requestId;
} 