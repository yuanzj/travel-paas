## 系统说明

- 基于 Spring Boot、 Netty、Mybatis Plus、ShardingSphere 构建两轮出行系统 PaaS层服务

## 快速开始
### 核心依赖

| 依赖             | 版本             |
|----------------|----------------|
| Spring Boot    | 3.4.1          |
| Netty          | 4.1.116.Final  |
| Mybatis Plus   | 3.5.10         |
| ShardingSphere | 5.4.1          |

### 模块说明
```
travel-paas
├── README.md
├── doc -- 接口文档
├── db -- 数据库脚本
├── service-connection -- 连接管理
│         ├── connection-cabinet-tower -- 铁塔协议连接管理
├── service-data -- 数据管理
│         ├── data-cabinet -- 电柜数据管理
└── service-device -- 设备管理
    ├── device-cabinet -- 电柜设备管理
```