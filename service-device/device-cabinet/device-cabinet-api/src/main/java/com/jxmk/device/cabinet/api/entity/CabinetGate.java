package com.jxmk.device.cabinet.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("dev_cabinet_gate")
public class CabinetGate {
    
    @TableId(type = IdType.ASSIGN_ID)
    private Long cabinetGateId;
    
    private String cabinetSn;
    
    private Integer gateNo;
    
    private Integer gateStatus;
    
    private Integer gateProhibit;
    
    private LocalDateTime gateStatusTime;
    
    private LocalDateTime gateProhibitTime;
    
    private String prohibitReason;
    
    private String prohibitSource;
    
    private Integer batteryStatus;
    
    private Integer batteryLock;
    
    private String batterySn;
    
    private Integer putBatterySoc;
    
    private LocalDateTime putBatteryTime;
    
    private Integer putBatteryWay;
    
    private String createBy;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    private String updateBy;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private String delFlag;
} 