package com.jxmk.connection.cabinet.model;

import lombok.Data;

@Data
public class LoginRequest {
    private Integer msgType;
    private String imsi;
    private String switchCabStatus;
    private String hardVersion;
    private String softVersion;
    private String devId;
    private String protocolVersion;
    private Long time;
    private Integer devType;
    private String txnNo;
} 