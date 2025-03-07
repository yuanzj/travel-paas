package com.jxmk.device.cabinet.api.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class CabinetImport {
    
    @ExcelProperty("生产厂商")
    private String manufacturer;
    
    @ExcelProperty("电柜型号")
    private String cabinetModel;
    
    @ExcelProperty("电柜编号")
    private String cabinetSn;
    
    @ExcelProperty("仓门数量")
    private Integer gateCount;
    
    @ExcelProperty("电柜协议")
    private String protocol;
} 