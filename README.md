# 云服务项目

### 结构说明
- ***aggregation*** 聚合层
- ***business-service*** 业务层
- ***common-service*** 通用层
- ***core*** 框架核心
- ***gateway*** 路由网关
- ***database*** 项目数据库初始化脚本

### 架构组织

* **cloud**
    + **aggregation**   
        - **center-control-portal** 中控项目
        - **smart-home**  2C手机应用项目
    + **business-siverce**
        - **common-business**   通用业务服务项目
        - **ifttt-service** 联动服务
        - **scene-service** 情景服务
        - **space-service** 空间服务
        - **video-service** 视频业务服务
        - **video-timer**   视频定时任务服务
    + **common-service**
        - **admin-service** 服务健康检测服务
        - **config-service**  配置服务
        - **device-service**  设备服务
        - **eureka-service**  注册中心
        - **file-service**  文件服务
        - **hystrix-dashboard** 断路器监控服务
        - **message-service** 消息服务
        - **mqttploy-service** 策略服务
        - **payment-service** 支付服务
        - **tenant-service**  租户服务
        - **user-service**  用户服务 
    + **core**
    + **database**
    + **gateway**
        - **mobile-getway**  手机应用路由网关


