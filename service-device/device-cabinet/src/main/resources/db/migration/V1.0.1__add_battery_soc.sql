ALTER TABLE dev_cabinet_task
    ADD COLUMN put_battery_soc int DEFAULT NULL COMMENT '归还电池电量',
    ADD COLUMN get_battery_soc int DEFAULT NULL COMMENT '获取电池电量'; 