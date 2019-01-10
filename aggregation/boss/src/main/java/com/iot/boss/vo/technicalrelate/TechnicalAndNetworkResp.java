package com.iot.boss.vo.technicalrelate;

import com.iot.device.vo.rsp.NetworkTypeResp;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：技术方案和配网模式
 * 创建人： yeshiyuan
 * 创建时间：2018/10/16 20:19
 * 修改人： yeshiyuan
 * 修改时间：2018/10/16 20:19
 * 修改描述：
 */
public class TechnicalAndNetworkResp {

    private List<TechnicalListResp> technicals;

    private List<NetworkTypeResp> networkTypes;

    public List<TechnicalListResp> getTechnicals() {
        return technicals;
    }

    public void setTechnicals(List<TechnicalListResp> technicals) {
        this.technicals = technicals;
    }

    public List<NetworkTypeResp> getNetworkTypes() {
        return networkTypes;
    }

    public void setNetworkTypes(List<NetworkTypeResp> networkTypes) {
        this.networkTypes = networkTypes;
    }
}
