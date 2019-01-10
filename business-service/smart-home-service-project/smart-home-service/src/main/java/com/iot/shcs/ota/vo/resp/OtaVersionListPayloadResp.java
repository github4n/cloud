package com.iot.shcs.ota.vo.resp;


import com.iot.shcs.ota.vo.BasePayload;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 9:55 2018/5/29
 * @Modify by:
 */
public class OtaVersionListPayloadResp extends BasePayload implements Serializable {

    private List<OtaVersionResp> verList;

    public OtaVersionListPayloadResp(List<OtaVersionResp> verList) {
        this.verList = verList;
    }

    public List<OtaVersionResp> getVerList() {
        return verList;
    }

    public void setVerList(List<OtaVersionResp> verList) {
        this.verList = verList;
    }
}
