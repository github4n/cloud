package com.iot.control.space.vo;

/**
 * @description:
 * @program: cloud
 * @return:
 * @author: chq
 * @date: 2018/10/11 11:37
 **/
public class SpaceDeviceReqVo {
    //需要修改的对象
    private SpaceDeviceReq setValueParam;
    //where查找条件
    private SpaceDeviceReq requstParam;

    public SpaceDeviceReq getSetValueParam() {
        return setValueParam;
    }

    public void setSetValueParam(SpaceDeviceReq setValueParam) {
        this.setValueParam = setValueParam;
    }

    public SpaceDeviceReq getRequstParam() {
        return requstParam;
    }

    public void setRequstParam(SpaceDeviceReq requstParam) {
        this.requstParam = requstParam;
    }
}
