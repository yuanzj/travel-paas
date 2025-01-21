package com.jxmk.connection.cabinet.model;

import lombok.Data;

import java.util.List;

@Data
public class AttributeReport {
    private Integer msgType;
    private List<Attribute> attrList;
    private String devId;
    private String txnNo;
    private Integer isFull;  // 0：增量，1：全量
} 