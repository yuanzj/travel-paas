package com.jxmk.connection.cabinet.handler.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jxmk.connection.cabinet.enums.CabinetSignalEnum;
import com.jxmk.connection.cabinet.enums.MessageTypeEnum;
import com.jxmk.connection.cabinet.model.AlarmReport;
import com.jxmk.connection.cabinet.model.Response;
import com.jxmk.connection.cabinet.service.DeviceService;
import com.jxmk.device.cabinet.api.constant.KafkaConstants;
import com.jxmk.device.cabinet.api.constant.RedisConstants;
import com.jxmk.device.cabinet.api.entity.CabinetTask;
import com.jxmk.device.cabinet.api.enums.CabinetOperateEnum;
import com.jxmk.device.cabinet.api.enums.TaskStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Component
public class AlarmReportHandler extends AbstractMessageHandler<AlarmReport> {

    public AlarmReportHandler(
            ObjectMapper objectMapper,
            DeviceService deviceService,
            RedisTemplate<String, Object> redisTemplate,
            KafkaTemplate<String, Object> kafkaTemplate) {
        super(objectMapper, deviceService, redisTemplate, kafkaTemplate);
    }

    @Override
    protected Class<AlarmReport> getMessageClass() {
        return AlarmReport.class;
    }

    @Override
    protected void handleMessage(AlarmReport report) {
        report.getAlarmList().forEach(alarm -> {
            // 检查是否是取出电池的告警
            if (CabinetSignalEnum.TAKE_BATTERY.getSignalId().equals(alarm.getId()) &&
                    CabinetSignalEnum.TAKE_BATTERY.getExtension().equals(alarm.getAlarmDesc()) &&
                    Objects.nonNull(alarm.getUserId())) {
                log.info("用户 {} 成功从设备 {} 取出电池: 柜门={}, 电池={}, 告警时间={}",
                        alarm.getUserId(),
                        report.getDevId(),
                        alarm.getDoorId(),
                        alarm.getBatteryId(),
                        alarm.getAlarmTime());

                // 构建Redis key
                String redisKey = RedisConstants.CABINET_TASK_KEY_PREFIX + CabinetOperateEnum.GET_BATTERY.getCode()
                        + ":" + alarm.getUserId();

                // 获取并更新任务状态
                CabinetTask task = (CabinetTask) redisTemplate.opsForValue().get(redisKey);
                if (task != null) {
                    // 更新任务状态为完成
                    task.setTaskStatus(TaskStatusEnum.SUCCESS.getCode());
                    task.setGetBatterySn(alarm.getBatteryId());
                    task.setGetGateNo(Integer.valueOf(alarm.getDoorId()));
                    task.setGetBatteryTime(LocalDateTime.now());
                    // 发送任务完成消息
                    kafkaTemplate.send(KafkaConstants.TOPIC_RC_RESPONSE, task);
                    log.info("已发送任务完成消息：{}", task);

                    // 删除Redis中的任务
                    redisTemplate.delete(redisKey);
                }
            } else {
                // 其他类型的告警
                log.info("收到设备 {} 的告警: 信号量ID={}, 描述={}, 状态={}, 柜门={}, 电池={}, 用户={}, 告警时间={}",
                        report.getDevId(),
                        alarm.getId(),
                        alarm.getAlarmDesc(),
                        alarm.getAlarmFlag() == 1 ? "开始" : "结束",
                        alarm.getDoorId(),
                        alarm.getBatteryId(),
                        alarm.getUserId(),
                        alarm.getAlarmTime());
            }
        });
    }

    @Override
    protected Response buildResponse(AlarmReport request) {
        return Response.builder()
                .msgType(MessageTypeEnum.ALARM_REPORT_RESPONSE.getCode())
                .devId(request.getDevId())
                .result(1)
                .txnNo(request.getTxnNo())
                .build();
    }

    @Override
    public int getMessageType() {
        return MessageTypeEnum.ALARM_REPORT_REQUEST.getCode();
    }
}