package com.jxmk.connection.cabinet.handler.task;

import com.jxmk.common.core.constant.CommonConstants;
import com.jxmk.common.core.util.R;
import com.jxmk.connection.cabinet.enums.CabinetFailureEnum;
import com.jxmk.connection.cabinet.enums.CabinetSignalEnum;
import com.jxmk.connection.cabinet.enums.MessageTypeEnum;
import com.jxmk.connection.cabinet.exception.TravelOfflineException;
import com.jxmk.connection.cabinet.exception.TravelTimeoutException;
import com.jxmk.connection.cabinet.model.ControlParam;
import com.jxmk.connection.cabinet.model.RemoteControl;
import com.jxmk.connection.cabinet.service.DeviceService;
import com.jxmk.device.cabinet.api.constant.KafkaConstants;
import com.jxmk.device.cabinet.api.constant.RedisConstants;
import com.jxmk.device.cabinet.api.entity.CabinetTask;
import com.jxmk.device.cabinet.api.enums.CabinetOperateEnum;
import com.jxmk.device.cabinet.api.enums.TaskStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetBatteryTaskHandler implements TaskHandler {

    private final DeviceService deviceService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final long REDIS_TASK_EXPIRE = 15; // 15分钟过期

    @Override
    public void handle(CabinetTask task) {
        RemoteControl control = RemoteControl.builder()
                .msgType(MessageTypeEnum.REMOTE_CONTROL_REQUEST.getCode())
                .devId(task.getCabinetSn())
                .txnNo(task.getTxnNo())
                .paramList(List.of(
                        ControlParam.builder()
                                .id(CabinetSignalEnum.TAKE_BATTERY.getSignalId())
                                .value(CabinetSignalEnum.TAKE_BATTERY.getExtension())
                                .voltage(task.getRequestBatteryVoltage().toString())
                                .userId(task.getRequestUserId())
                                .build()))
                .build();

        // 发送控制命令
        try {
            R<String> result = deviceService.sendControl(control);
            log.info("取电控制命令发送结果：{}", result);

            if (result.getCode() == CommonConstants.SUCCESS) {
                // 设置任务状态为处理中
                task.setTaskStatus(TaskStatusEnum.PROCESSING.getCode());

                // 构建Redis键
                String redisKey = RedisConstants.CABINET_TASK_KEY_PREFIX +
                        task.getTaskType() +
                        ":" +
                        task.getRequestUserId();

                // 将任务缓存到Redis，设置5分钟过期
                redisTemplate.opsForValue().set(
                        redisKey,
                        task,
                        REDIS_TASK_EXPIRE,
                        TimeUnit.MINUTES);

                log.info("任务已缓存到Redis，key={}", redisKey);
            } else {
                // 设置任务状态为失败
                task.setTaskStatus(TaskStatusEnum.FAILED.getCode());
                task.setFailedCode(CabinetFailureEnum.UNKNOWN.getCode());
                task.setFailedTime(LocalDateTime.now());
                log.error("取电控制命令发送失败：{}", result);
            }
        } catch (TravelOfflineException e) {
            log.error("设备不在线");
            task.setTaskStatus(TaskStatusEnum.FAILED.getCode());
            task.setFailedCode(CabinetFailureEnum.OFFLINE.getCode());
        } catch (TravelTimeoutException e) {
            log.error("等待设备响应超时");
            task.setTaskStatus(TaskStatusEnum.FAILED.getCode());
            task.setFailedCode(CabinetFailureEnum.TIMEOUT.getCode());
        } catch (Exception e) {
            log.error("发送控制命令失败", e);
            task.setTaskStatus(TaskStatusEnum.FAILED.getCode());
            task.setFailedCode(CabinetFailureEnum.UNKNOWN.getCode());
        } finally {
            kafkaTemplate.send(KafkaConstants.TOPIC_RC_RESPONSE, task);
            log.info("已发送任务完成消息：{}", task);
        }

    }

    @Override
    public CabinetOperateEnum getOperateType() {
        return CabinetOperateEnum.GET_BATTERY;
    }
}