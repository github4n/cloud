package com.iot.shcs.device.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @description:
 * @program: cloud
 * @return:
 * @author: chq
 * @date: 2018/11/12 16:35
 **/
@Data
@ToString
public class SubDevReq implements Serializable {

    private String devId;

    private String devName; //设备名称

    private String productId;

    private Long address; //设备操作地址

    private Integer isAppDev;

    private String comMode; //协议类型

    private String mac; //设备mac

    private String icon;

    private String version;

    private Long tenantId;

    private String userUuid;

    private String homeId; //家ID

    private String roomId; //房间ID

    private List<Long> remoteGroudId; //遥控器分组id，当设备是遥控器时，为0-255的值，其他设备为0

}
