package com.iot.device.vo.rsp;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 17:25 2018/5/7
 * @Modify by:
 */
public class IftttDeviceResp extends DeviceResp implements Serializable {

    private String iftttType;

    public String getIftttType() {
        return iftttType;
    }

    public void setIftttType(String iftttType) {
        this.iftttType = iftttType;
    }
}
