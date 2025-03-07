package com.jxmk.connection.cabinet.handler.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jxmk.connection.cabinet.model.BaseMessage;
import com.jxmk.connection.cabinet.model.Response;
import com.jxmk.connection.cabinet.service.DeviceService;
import com.jxmk.device.cabinet.api.constant.RedisConstants;
import com.jxmk.device.cabinet.api.entity.Cabinet;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * 消息处理器抽象基类
 * 
 * @param <T> 消息类型
 */
@Slf4j
public abstract class AbstractMessageHandler<T extends BaseMessage> implements MessageHandler {

    protected final ObjectMapper objectMapper;
    protected final DeviceService deviceService;
    protected final RedisTemplate<String, Object> redisTemplate;
    protected final KafkaTemplate<String, Object> kafkaTemplate;

    public AbstractMessageHandler(ObjectMapper objectMapper,
            DeviceService deviceService,
            RedisTemplate<String, Object> redisTemplate,
            KafkaTemplate<String, Object> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.deviceService = deviceService;
        this.redisTemplate = redisTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void handle(ChannelHandlerContext ctx, String message) throws Exception {
        // 1. 报文解析
        T request = parseMessage(message);

        // 2. 校验设备号
        if (!validateDeviceId(request)) {
            log.error("设备号不合法：{}", request.getDevId());
            ctx.close();
            return;
        }

        // 3. 注册设备通道
        registerDevice(ctx, request);

        // 4. 业务逻辑处理
        handleMessage(request);

        // 5. 回复终端消息
        Response response = buildResponse(request);
        if (response != null) {
            ctx.writeAndFlush(objectMapper.writeValueAsString(response));
        }
    }

    /**
     * 校验设备号
     */
    protected boolean validateDeviceId(T request) {
        Cabinet cabinet = (Cabinet) redisTemplate.opsForValue()
                .get(RedisConstants.CABINET_KEY_PREFIX + request.getDevId());
        return cabinet != null;
    }

    /**
     * 注册设备通道
     */
    protected void registerDevice(ChannelHandlerContext ctx, T request) {
        deviceService.addChannel(request.getDevId(), ctx.channel());
        log.info("设备 {} 通道已注册", request.getDevId());
    }

    /**
     * 解析消息
     */
    protected T parseMessage(String message) throws Exception {
        return objectMapper.readValue(message, getMessageClass());
    }

    /**
     * 获取消息类型Class
     */
    protected abstract Class<T> getMessageClass();

    /**
     * 处理业务逻辑
     */
    protected abstract void handleMessage(T message);

    /**
     * 构建响应消息
     */
    protected abstract Response buildResponse(T request);
}