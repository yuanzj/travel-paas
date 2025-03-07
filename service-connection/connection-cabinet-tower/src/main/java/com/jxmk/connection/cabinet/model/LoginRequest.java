package com.jxmk.connection.cabinet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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