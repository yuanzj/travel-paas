package com.jxmk.connection.cabinet.handler.task;

import com.jxmk.device.cabinet.api.entity.CabinetTask;
import com.jxmk.device.cabinet.api.enums.CabinetOperateEnum;

/**
 * 任务处理器接口
 */
public interface TaskHandler {

    /**
     * 处理任务
     */
    void handle(CabinetTask task);

    /**
     * 获取支持的操作类型
     */
    CabinetOperateEnum getOperateType();
}