package com.jxmk.connection.cabinet.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest extends BaseMessage {
    private String imsi;
    private String switchCabStatus;
    private String hardVersion;
    private String softVersion;
    private String protocolVersion;
    private Long time;
    private Integer devType;
}