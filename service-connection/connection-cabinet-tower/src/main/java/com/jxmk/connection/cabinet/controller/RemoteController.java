package com.jxmk.connection.cabinet.controller;

import com.jxmk.connection.cabinet.model.R;
import com.jxmk.connection.cabinet.model.RemoteControl;
import com.jxmk.connection.cabinet.service.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/device")
@RequiredArgsConstructor
public class RemoteController {

    private final DeviceService deviceService;

    @PostMapping("/control")
    public R<String> control(@RequestBody RemoteControl control) {
        log.info("收到远程控制请求: {}", control);
        return deviceService.sendControl(control);
    }

    /**
     * 查询设备在线状态
     */
    @GetMapping("/online/{deviceId}")
    public R<Boolean> isOnline(@PathVariable String deviceId) {
        return R.ok(deviceService.isOnline(deviceId));
    }

    /**
     * 获取所有在线设备
     */
    @GetMapping("/online/list")
    public R<List<String>> getOnlineDevices() {
        return R.ok(deviceService.getOnlineDevices());
    }
} 