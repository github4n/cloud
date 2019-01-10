package com.iot.device.vo.rsp.ota;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：IOT云平台
 * 模块名称：ota
 * 功能描述：ota
 * 创建人： 490485964@qq.com
 * 创建时间：2018年08月01日 11:06
 * 修改人： 490485964@qq.com
 * 修改时间：2018年08月01日 11:06
 */
public class PushOta {
    /**
     * 用户Id
     */
    private Long userId;
    /**
     * 设备信息
     */
    private List<PushOtaDevInfo> devList = new ArrayList<>();

    public PushOta() {

    }

    public PushOta(Long userId, List<PushOtaDevInfo> devList) {
        this.userId = userId;
        this.devList = devList;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<PushOtaDevInfo> getDevList() {
        return devList;
    }

    public void setDevList(List<PushOtaDevInfo> devList) {
        this.devList = devList;
    }
}
