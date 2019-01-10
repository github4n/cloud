package com.iot.device.vo.rsp;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 9:02 2018/5/11
 * @Modify by:
 */
public class DeviceParamResp implements Serializable {

    private List<DeviceResp> deviceResps;

    private int count = 0;

    public List<DeviceResp> getDeviceResps() {
        return deviceResps;
    }

    public void setDeviceResps(List<DeviceResp> deviceResps) {
        this.deviceResps = deviceResps;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
