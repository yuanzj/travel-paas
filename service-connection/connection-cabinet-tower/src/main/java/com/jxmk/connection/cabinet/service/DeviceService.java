package com.jxmk.connection.cabinet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jxmk.common.core.util.R;
import com.jxmk.connection.cabinet.exception.TravelOfflineException;
import com.jxmk.connection.cabinet.exception.TravelTimeoutException;
import com.jxmk.connection.cabinet.model.RemoteControl;
import com.jxmk.connection.cabinet.model.Response;
import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceService {

    private final Map<String, Channel> deviceChannels = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, CompletableFuture<Response>> pendingControls = new ConcurrentHashMap<>();

    /**
     * 添加设备连接
     *
     * @param deviceId 设备ID
     * @param channel  设备连接
     */
    public void addChannel(String deviceId, Channel channel) {
        deviceChannels.put(deviceId, channel);
        log.info("设备 {} 已连接", deviceId);
    }

    /**
     * 移除设备连接
     *
     * @param deviceId 设备ID
     */
    public void removeChannel(String deviceId) {
        deviceChannels.remove(deviceId);
        log.info("设备 {} 已断开", deviceId);
    }

    /**
     * 同步发送控制命令
     *
     * @param control 控制命令
     * @return 包含发送结果的R对象
     */
    public R<String> sendControl(RemoteControl control) {
        Channel channel = deviceChannels.get(control.getDevId());
        if (channel == null || !channel.isActive()) {
            throw new TravelOfflineException("设备不在线");
        }

        CompletableFuture<Response> future = new CompletableFuture<>();
        try {
            String message = objectMapper.writeValueAsString(control);
            pendingControls.put(control.getTxnNo(), future);
            channel.writeAndFlush(message);

            // 等待设备响应，30秒超时
            Response response = future.get(30, TimeUnit.SECONDS);
            return response.getResult() == 1 ? R.ok(response.getTxnNo()) : R.failed("设备执行失败");
        } catch (TimeoutException e) {
            log.error("等待设备响应超时");
            throw new TravelTimeoutException("等待设备响应超时");
        } catch (Exception e) {
            log.error("发送控制命令失败", e);
            throw new RuntimeException("发送控制命令失败");
        } finally {
            pendingControls.remove(control.getTxnNo());
        }
    }

    /**
     * 处理设备控制响应
     *
     * @param response 设备响应
     */
    public void handleControlResponse(Response response) {
        CompletableFuture<Response> future = pendingControls.get(response.getTxnNo());
        if (future != null) {
            future.complete(response);
        } else {
            log.warn("收到未知的控制响应: {}", response);
        }
    }

    /**
     * 检查设备是否在线
     *
     * @param deviceId 设备ID
     * @return true: 在线, false: 离线
     */
    public boolean isOnline(String deviceId) {
        Channel channel = deviceChannels.get(deviceId);
        return channel != null && channel.isActive();
    }

    /**
     * 获取所有在线设备ID
     *
     * @return 在线设备ID列表
     */
    public List<String> getOnlineDevices() {
        return deviceChannels.entrySet().stream()
                .filter(entry -> entry.getValue().isActive())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}