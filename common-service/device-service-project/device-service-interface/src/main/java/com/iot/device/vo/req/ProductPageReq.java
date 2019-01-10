package com.iot.device.vo.req;

import com.iot.device.util.ReqBasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 16:15 2018/6/27
 * @Modify by:
 */
@ApiModel("产品分页请求参数")
public class ProductPageReq extends ReqBasePage implements Serializable {

    @ApiModelProperty(value = "设备类型id")
    private Long deviceTypeId;

    /**
     * 企业开发组id
     */
    @ApiModelProperty(value = "企业开发组id")
    private Long enterpriseDevelopId;

    public Long getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Long deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public Long getEnterpriseDevelopId() {
        return enterpriseDevelopId;
    }

    public void setEnterpriseDevelopId(Long enterpriseDevelopId) {
        this.enterpriseDevelopId = enterpriseDevelopId;
    }
}
