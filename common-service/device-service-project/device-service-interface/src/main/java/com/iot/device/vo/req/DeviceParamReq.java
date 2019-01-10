package com.iot.device.vo.req;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 8:58 2018/5/11
 * @Modify by:
 */
public class DeviceParamReq implements Serializable {

    private boolean checkUserNotNull = true;

    private List<String> deviceIdList;

    private boolean isCount = false;

    //checkUserNotNull ==false 且 userId 传入条件则进行查询
    private Long userId;

    public List<String> getDeviceIdList() {
        return deviceIdList;
    }

    public boolean isCheckUserNotNull() {
        return checkUserNotNull;
    }

    public void setCheckUserNotNull(boolean checkUserNotNull) {
        this.checkUserNotNull = checkUserNotNull;
    }

    public void setDeviceIdList(List<String> deviceIdList) {
        this.deviceIdList = deviceIdList;
    }

    public boolean isCount() {
        return isCount;
    }

    public void setCount(boolean count) {
        isCount = count;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
