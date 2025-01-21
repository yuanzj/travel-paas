package com.jxmk.connection.cabinet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private Integer msgType;
    private String devId;
    private Integer result;  // 1：成功 0：失败
    private String txnNo;
} 