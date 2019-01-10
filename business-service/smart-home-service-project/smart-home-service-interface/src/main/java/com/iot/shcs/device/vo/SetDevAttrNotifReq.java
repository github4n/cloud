package com.iot.shcs.device.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 11:08 2018/8/6
 * @Modify by:
 */
@Data
@ToString
public class SetDevAttrNotifReq implements Serializable {

    private String parentDeviceId;

    private String devId;

    //{"OnOff": "1","online":1}
    private Map<String, Object> attr;

}
