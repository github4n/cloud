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


### smarthome与设备相关的接口有：
* **device**
    + 绑定用户和设备请求 devBindReq
    + 解绑 devUnbindReq
    + 获取家庭设备列表 getDevListReq
    + 上报设备基本信息 updateDevBaseInfo
    + 设备状态通知（上报 子设备上线、离线 的消息） devStatusNotif
    + 设备状态通知 devStautsNotif
    + 上报设备详细信息 updateDevDetails
    + 设置设备属性（单控） setDevAttr
    + 设置设备属性通知 setDevAttrNotif
    + 设备方法调用请求 devActionReq
    + 设备方法调用响应 devActionResp
    + 设备事件通知  devEventNotif
    + 获取设备属性（app发消息给云，云转发给设备） getDevAttrReq
    + 获取设备属性信息响应（设备响应给云，云再转发到app） getDevAttrResp
    + 获取用户房间设备列表（直接从云端获取） getRoomDevListReq
    + 添加子设备通知（设备广播，云端接收到） addDevNotif
    + 删除子设备通知（设备广播，云端接收到） delDevNotif
    + 获取设备信息 getDevInfoReq
    + 设置设备信息响应（设备响应给云，云处理完业务再响应给app） setDevInfoNotif
    + 子设备上下线状态或名称改变上报 statusNotify
    + 设备上线 connect
    + 设备下线 disconnect
    + 修改家庭设备排序 updateDevOrder
    + 设置倒计时信息请求（app发消息给云，云转发给设备） setCountDownReq
    + 设置倒计时信息响应（设备响应给云，云再转发到app） setCountDownResp
    + 设置倒计时使能请求（app发消息给云，云转发给设备） setCountDownEnableReq
    + 设置倒计时使能响应（设备响应给云，云再转发到app） setCountDownEnableResp
    + 上报电量信息请求（设备发给云，云保存数据，回响应） updateEnergyReq
    + 上报运行时间请求（设备发给云，云保存数据，回响应） updateRuntimeReq
* **group**
    + 删除组请求 delGroupReq
    + 删除组响应 delGroupResp
    + ...
* **scene**
    + 删除场景请求2c delSceneReq
    + 删除场景响应2c delSceneResp
    + 添加场景项请求(组控)(scene_detail)2c addActionReq
    + 添加场景项响应(组控)(scene_detail)2c addActionResp
    + 删除场景项请求(组控)(scene_detail)2c delActionReq
    + 删除场景项响应(组控)(scene_detail)2c delActionResp
    + 修改场景项请求(组控)(scene_detail)2c editActionReq
    + 修改场景项响应(组控)(scene_detail)2c editActionResp
    + 查询 场景项列表 请求(scene_detail)2c getThenReq
    + 场景操作请求(执行场景请求，app --> cloud --> dev)2c excSceneReq
    + 场景操作响应(执行场景响应，dev --> cloud --> app)2c excSceneResp
* **IFTTT**
    + 删除Automation请求 delAutoReq
    + 删除Automation应答 delAutoResp
    + 获取Automation规则请求 getAutoRuleReq
    + 添加Automation规则请求 addAutoRuleReq
    + 添加Automation规则响应 addAutoRuleResp
    + 编辑Automation规则请求 editAutoRuleReq
    + 编辑Automation规则响应 editAutoRuleResp
    + 设置Automation使能（开关）请求 setAutoEnableReq
    + 设置Automation使能（开关）响应 setAutoEnableResp
    + 执行Automation通知 excAutoNotif
* **OTA**

### smartbuilding与设备相关的接口有：
* **device**
    + 子设备上下线状态或名称改变上报 statusNotify
    + 设备上线 connect
    + 设备下线 disconnect
    + 获取网关基本信息 getInfoResp
    + 子设备属性上报 propsNotify
    + 子设备实时属性上报 updatePropResp
    + 子设备事件上报 eventNotify
    + 获取子设备属性回调 getPropResp
    + 查询子设备列表回调 queryResp
    + 子设备列表变更上报回调 listNotify

* **group**
    + 添加分组成员 回调处理 addResp
    + 查询分组列表回调 listResp
    + operateResp
    + ???

* **scene**
    + 保存情景方式与2C不一样，没有使用MQTT
    + 场景操作响应(执行场景响应，dev --> cloud --> app) excSceneResp
    + 场景操作回调 operateResp
* **IFTTT**
    + 没有看到使用MQTT
* **OTA**
    + 上报升级状态 stateNotify
    + 上报子设备升级版本 otaVersionNotify
    + 获取设备的版本信息和状态 queryResp


control-service依赖vedio-service