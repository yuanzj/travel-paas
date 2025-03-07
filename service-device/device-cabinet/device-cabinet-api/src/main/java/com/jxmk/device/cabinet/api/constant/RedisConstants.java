package com.jxmk.device.cabinet.api.constant;

public interface RedisConstants {

    /**
     * Redis中任务缓存的key前缀
     */
    String CABINET_TASK_KEY_PREFIX = "cabinet:task:";

    /**
     * 设备信息缓存的key前缀
     */
    String CABINET_KEY_PREFIX = "cabinet:info:";

    /**
     * 柜门信息缓存的key前缀
     */
    String GATE_KEY_PREFIX = "cabinet:gate:";
}