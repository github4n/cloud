package com.iot.shcs.space.vo;

/**
 * @description:
 * @program: cloud
 * @return:
 * @author: chq
 * @date: 2018/10/11 15:16
 **/
public class SpaceReqVo {
    //需要修改的对象
    private SpaceReq setValueParam;
    //where查找条件
    private SpaceReq requstParam;

    public SpaceReq getSetValueParam() {
        return setValueParam;
    }

    public void setSetValueParam(SpaceReq setValueParam) {
        this.setValueParam = setValueParam;
    }

    public SpaceReq getRequstParam() {
        return requstParam;
    }

    public void setRequstParam(SpaceReq requstParam) {
        this.requstParam = requstParam;
    }
}
