package com.iot.device.vo.req.device;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


@Data
@ToString
public class AddCommDeviceStateInfoReq implements Serializable {


    /**
     * 属性描述
     */
    private String propertyDesc;

    /**
     * 设备属性名称
     */
    private String propertyName;

    /**
     * 设备属性值
     */
    private String propertyValue;


    private Long groupId;

}
