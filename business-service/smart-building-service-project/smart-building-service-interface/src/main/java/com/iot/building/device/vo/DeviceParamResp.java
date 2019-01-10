package com.iot.building.device.vo;

import com.iot.device.vo.rsp.DeviceResp;

import java.io.Serializable;
import java.util.List;

public class DeviceParamResp implements Serializable {

    private List<DeviceResp> deviceResps;
    //private List<ListDeviceInfoRespVo> deviceResps;

    private int count = 0;

   /* public List<ListDeviceInfoRespVo> getDeviceResps() {
        return deviceResps;
    }

    public void setDeviceResps(List<ListDeviceInfoRespVo> deviceResps) {
        this.deviceResps = deviceResps;
    }*/
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
