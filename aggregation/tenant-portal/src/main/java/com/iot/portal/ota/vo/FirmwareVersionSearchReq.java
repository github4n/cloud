package com.iot.portal.ota.vo;

import com.iot.common.beans.SearchParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：IOT云平台
 * 模块名称：聚合层
 * 功能描述：OTA版本查询参数
 * 创建人： maochengyuan
 * 创建时间：2018/7/24 19:23
 * 修改人： maochengyuan
 * 修改时间：2018/7/24 19:23
 * 修改描述：
 */
@ApiModel(value="FirmwareVersionSearchReq", description="OTA版本查询参数")
public class FirmwareVersionSearchReq extends SearchParam {

    @ApiModelProperty(value="产品id",name="prodId")
    private String prodId;

    public FirmwareVersionSearchReq() {

    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

}
