package com.iot.shcs.ota.vo.resp;

import com.iot.shcs.ota.vo.BasePayload;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 9:47 2018/5/29
 * @Modify by:
 */
public class OtaNotifPayloadResp extends BasePayload implements Serializable {


    private List<OtaNotifResp> ota;

    public OtaNotifPayloadResp(List<OtaNotifResp> ota) {
        this.ota = ota;
    }


    public List<OtaNotifResp> getOta() {
        return ota;
    }

    public void setOta(List<OtaNotifResp> ota) {
        this.ota = ota;
    }
}
