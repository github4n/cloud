package com.iot.device.vo.req;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 10:31 2018/5/7
 * @Modify by:
 */
public class CommDeviceFwTypeVersionReq implements Serializable {

    /**
     * 分位的类型  固件类型
     * fw_type
     * 0:所有的模块在一个分位里面  1:wifi 模块的分位  2: zigbee模块的分位 3: z-wave模块的分位 4：ble模块的分位 不填默认值为0.
     */
    private Integer fwType;

    /**
     * 设备版本号 对应固件类型 版本
     * version
     */
    private String version;

    public Integer getFwType() {
        return fwType;
    }

    public void setFwType(Integer fwType) {
        this.fwType = fwType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
