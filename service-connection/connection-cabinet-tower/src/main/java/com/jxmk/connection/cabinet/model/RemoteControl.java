package com.jxmk.connection.cabinet.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RemoteControl {
    private Integer msgType;  // 500
    private String devId;
    private List<ControlParam> paramList;
    private String txnNo;
} 