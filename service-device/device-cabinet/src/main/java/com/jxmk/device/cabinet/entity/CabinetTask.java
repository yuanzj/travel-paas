package com.jxmk.device.cabinet.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.jxmk.device.cabinet.enums.CabinetOperateEnum;
import com.jxmk.device.cabinet.enums.TaskStatusEnum;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("dev_cabinet_task")
public class CabinetTask {
    
    @TableId(type = IdType.ASSIGN_ID)
    private Long taskId;
    
    /**
     * 任务类型
     * @see CabinetOperateEnum
     */
    private Integer taskType;
    
    /**
     * 任务状态
     * @see TaskStatusEnum
     */
    private Integer taskStatus;
    
    private String txnNo;
    
    private String cabinetSn;
    
    private Integer putGateNo;
    
    private String putBatterySn;
    
    private LocalDateTime putBatteryTime;
    
    /**
     * 归还电池电量
     */
    private Integer putBatterySoc;
    
    private Integer getGateNo;
    
    private String getBatterySn;
    
    private LocalDateTime getBatteryTime;
    
    /**
     * 获取电池电量
     */
    private Integer getBatterySoc;
    
    private Integer failedCode;
    
    private LocalDateTime failedTime;
    
    private String requestId;
    
    private String requestCardSn;
    
    /**
     * 请求用户ID
     */
    private String requestUserId;
    
    private String requestPutBatterySn;
    
    private Integer requestGateNo;
    
    private BigDecimal requestBatteryVoltage;
    
    /**
     * 回调通知地址
     */
    private String notifyUrl;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
} 