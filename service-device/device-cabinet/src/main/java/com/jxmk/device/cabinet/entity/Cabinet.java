package com.jxmk.device.cabinet.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("dev_cabinet")
public class Cabinet {

    @TableId(type = IdType.AUTO)
    private Long cabinetId;

    private String cabinetSn;

    private String cabinetModel;

    private String cabinetName;

    private Integer cabinetGateCount;

    private Integer cabinetStatus;

    private Integer cabinetProtocol;

    private LocalDateTime firstReportTime;

    private LocalDateTime lastReportTime;

    private String manufacturer;

    private String firmwareVersion;

    private LocalDateTime installedDate;

    private String coordinates;

    private String province;

    private String city;

    private String district;

    private String address;

    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private String delFlag;
}