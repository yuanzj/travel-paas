package com.jxmk.device.cabinet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jxmk.device.cabinet.dto.CabinetOperateRequest;
import com.jxmk.device.cabinet.dto.CabinetOperateResponse;
import com.jxmk.device.cabinet.entity.Cabinet;
import com.jxmk.device.cabinet.dto.CabinetImport;
import java.util.List;

public interface CabinetService extends IService<Cabinet> {
    
    /**
     * 取电池操作
     */
    CabinetOperateResponse getBattery(CabinetOperateRequest request);
    
    /**
     * 换电操作
     */
    CabinetOperateResponse exchangeBattery(CabinetOperateRequest request);
    
    /**
     * 归还电池操作
     */
    CabinetOperateResponse returnBattery(CabinetOperateRequest request);
    
    /**
     * 电柜启用
     */
    CabinetOperateResponse enableCabinet(CabinetOperateRequest request);
    
    /**
     * 电柜禁用
     */
    CabinetOperateResponse disableCabinet(CabinetOperateRequest request);
    
    /**
     * 电柜开门
     */
    CabinetOperateResponse openCabinet(CabinetOperateRequest request);
    
    /**
     * 电柜仓门启用
     */
    CabinetOperateResponse enableGate(CabinetOperateRequest request);
    
    /**
     * 电柜仓门禁用
     */
    CabinetOperateResponse disableGate(CabinetOperateRequest request);
    
    /**
     * 批量导入电柜
     */
    void importCabinet(List<CabinetImport> list);
} 