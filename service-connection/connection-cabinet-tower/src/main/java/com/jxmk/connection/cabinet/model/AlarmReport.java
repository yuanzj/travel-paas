package com.jxmk.connection.cabinet.model;

import lombok.Data;

import java.util.List;

@Data
public class AlarmReport {
    private Integer msgType;
    private List<Alarm> alarmList;
    private String devId;
    private String txnNo;
} 