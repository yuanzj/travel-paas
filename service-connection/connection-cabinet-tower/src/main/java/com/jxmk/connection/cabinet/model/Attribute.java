package com.jxmk.connection.cabinet.model;

import lombok.Data;

@Data
public class Attribute {
    private String id;       // 信号量ID
    private String value;    // 属性值
    private String doorId;   // 柜门ID，可选
} 