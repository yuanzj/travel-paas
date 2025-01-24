/*
 Navicat Premium Dump SQL

 Source Server         : 开发环境
 Source Server Type    : MySQL
 Source Server Version : 80032 (8.0.32)
 Source Host           : localhost:3306
 Source Schema         : travel

 Target Server Type    : MySQL
 Target Server Version : 80032 (8.0.32)
 File Encoding         : 65001

 Date: 24/01/2025 17:41:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for data_cabinet_msg
-- ----------------------------
DROP TABLE IF EXISTS `data_cabinet_msg`;
CREATE TABLE `data_cabinet_msg` (
  `id` bigint NOT NULL COMMENT '主键',
  `cabinet_sn` varchar(64) NOT NULL COMMENT '电柜序列号',
  `msg_type` varchar(32) DEFAULT NULL COMMENT '消息类型 如：TX:110、RX:111',
  `msg_txn_no` varchar(64) DEFAULT NULL COMMENT '消息流水号',
  `raw_data` text COMMENT '原始报文',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_cabinet_sn` (`cabinet_sn`),
  KEY `idx_msg_txn_no` (`msg_txn_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='电柜通信记录表';

-- ----------------------------
-- Table structure for dev_cabinet
-- ----------------------------
DROP TABLE IF EXISTS `dev_cabinet`;
CREATE TABLE `dev_cabinet` (
  `cabinet_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `cabinet_sn` varchar(64) NOT NULL COMMENT '电柜序列号',
  `cabinet_model` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '电柜型号',
  `cabinet_name` varchar(100) DEFAULT NULL COMMENT '电柜名称',
  `cabinet_gate_count` int DEFAULT NULL COMMENT '仓门数量',
  `cabinet_status` tinyint NOT NULL DEFAULT '0' COMMENT '状态 0：未激活 1：已激活 2：维护中 3：已退库',
  `cabinet_protocol` tinyint NOT NULL DEFAULT '1' COMMENT '协议类型 1：铁塔协议',
  `online_status` tinyint DEFAULT '0' COMMENT '在线状态：0-离线，1-在线',
  `first_report_time` datetime DEFAULT NULL COMMENT '首次上报时间',
  `last_report_time` datetime DEFAULT NULL COMMENT '最后上报时间',
  `manufacturer` varchar(100) DEFAULT NULL COMMENT '制造商',
  `firmware_version` varchar(32) DEFAULT NULL COMMENT '固件版本',
  `installed_date` datetime DEFAULT NULL COMMENT '安装时间',
  `coordinates` varchar(100) DEFAULT NULL COMMENT '安装位置经纬度',
  `province` varchar(32) DEFAULT NULL COMMENT '省',
  `city` varchar(32) DEFAULT NULL COMMENT '市',
  `district` varchar(32) DEFAULT NULL COMMENT '区',
  `address` varchar(255) DEFAULT NULL COMMENT '详细地址',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0：未删除 1：已删除',
  PRIMARY KEY (`cabinet_id`),
  UNIQUE KEY `uk_cabinet_sn` (`cabinet_sn`)
) ENGINE=InnoDB AUTO_INCREMENT=1882601226408812546 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='电柜信息表';

-- ----------------------------
-- Table structure for dev_cabinet_gate
-- ----------------------------
DROP TABLE IF EXISTS `dev_cabinet_gate`;
CREATE TABLE `dev_cabinet_gate` (
  `cabinet_gate_id` bigint NOT NULL COMMENT '主键ID',
  `cabinet_sn` varchar(64) NOT NULL COMMENT '电柜序列号',
  `gate_no` int NOT NULL COMMENT '仓门编号',
  `gate_status` tinyint DEFAULT NULL COMMENT '仓门状态：-1：停用；0：关；1：开',
  `gate_prohibit` tinyint DEFAULT NULL COMMENT '仓门禁用状态：0：正常；1：禁用',
  `gate_status_time` datetime DEFAULT NULL COMMENT '仓门状态变更时间',
  `gate_prohibit_time` datetime DEFAULT NULL COMMENT '仓门禁用时间',
  `prohibit_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '仓门禁用原因',
  `prohibit_source` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '仓门禁用来源：0：电柜自动禁用；非 0：禁用人用户ID',
  `battery_status` tinyint DEFAULT NULL COMMENT '电池状态',
  `battery_lock` tinyint DEFAULT NULL COMMENT '电池锁状态',
  `battery_sn` varchar(64) DEFAULT NULL COMMENT '电池序列号',
  `put_battery_soc` int DEFAULT NULL COMMENT '放入电池电量',
  `put_battery_time` datetime DEFAULT NULL COMMENT '放入电池时间',
  `put_battery_way` tinyint DEFAULT NULL COMMENT '放入电池方式',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0：未删除 1：已删除',
  PRIMARY KEY (`cabinet_gate_id`),
  UNIQUE KEY `uk_cabinet_sn_gate_no` (`cabinet_sn`,`gate_no`),
  KEY `idx_battery_sn` (`battery_sn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='电柜仓门表';

-- ----------------------------
-- Table structure for dev_cabinet_task
-- ----------------------------
DROP TABLE IF EXISTS `dev_cabinet_task`;
CREATE TABLE `dev_cabinet_task` (
  `task_id` bigint NOT NULL COMMENT '主键',
  `task_type` tinyint DEFAULT NULL COMMENT '任务类型',
  `task_status` tinyint DEFAULT NULL COMMENT '任务状态',
  `txn_no` varchar(64) DEFAULT NULL COMMENT '交易流水号',
  `cabinet_sn` varchar(64) NOT NULL COMMENT '电柜序列号',
  `put_gate_no` int DEFAULT NULL COMMENT '放入仓门号',
  `put_battery_sn` varchar(64) DEFAULT NULL COMMENT '放入电池序列号',
  `put_battery_soc` int DEFAULT NULL COMMENT '归还电池电量',
  `put_battery_time` datetime DEFAULT NULL COMMENT '放入电池时间',
  `get_gate_no` int DEFAULT NULL COMMENT '取出仓门号',
  `get_battery_sn` varchar(64) DEFAULT NULL COMMENT '取出电池序列号',
  `get_battery_soc` int DEFAULT NULL COMMENT '获取电池电量',
  `get_battery_time` datetime DEFAULT NULL COMMENT '取出电池时间',
  `failed_code` int DEFAULT NULL COMMENT '失败代码',
  `failed_time` datetime DEFAULT NULL COMMENT '失败时间',
  `request_id` varchar(64) DEFAULT NULL COMMENT '请求ID',
  `request_card_sn` varchar(64) DEFAULT NULL COMMENT '请求卡号',
  `request_user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '请求用户ID',
  `request_put_battery_sn` varchar(64) DEFAULT NULL COMMENT '请求放入电池序列号',
  `request_gate_no` int DEFAULT NULL COMMENT '请求仓门号',
  `request_battery_voltage` decimal(10,2) DEFAULT NULL COMMENT '请求电池电压',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`task_id`),
  KEY `idx_cabinet_sn` (`cabinet_sn`),
  KEY `idx_txn_no` (`txn_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='电柜任务表';

SET FOREIGN_KEY_CHECKS = 1;
