package com.iot.shcs.ota.vo.req;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 10:08 2018/5/29
 * @Modify by:
 */
public class OtaUpdateVersionInfoListReq implements Serializable {

    private List<OtaUpdateVersionInfoReq> verInfo;

    public List<OtaUpdateVersionInfoReq> getVerInfo() {
        return verInfo;
    }

    public void setVerInfo(List<OtaUpdateVersionInfoReq> verInfo) {
        this.verInfo = verInfo;
    }
}
