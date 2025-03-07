package com.jxmk.connection.cabinet.handler.task;

import com.jxmk.device.cabinet.api.enums.CabinetOperateEnum;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TaskHandlerFactory {

    private final Map<CabinetOperateEnum, TaskHandler> handlerMap;

    public TaskHandlerFactory(List<TaskHandler> handlers) {
        this.handlerMap = handlers.stream()
                .collect(Collectors.toMap(
                        TaskHandler::getOperateType,
                        Function.identity()));
    }

    public TaskHandler getHandler(CabinetOperateEnum operateType) {
        return handlerMap.get(operateType);
    }
}