package com.iot.portal.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 11:31 2018/7/3
 * @Modify by:
 */
@ApiModel("功能模组属性信息")
public class PortalPropertyListResp extends PortalPropertyInfoResp implements Serializable {

    //是否被选中 默认未选中
    @ApiModelProperty(value = "设备类型id", allowableValues = "true 选中 ,false 未选中")
    private boolean whetherCheck = false;

    public boolean isWhetherCheck() {
        return whetherCheck;
    }

    public void setWhetherCheck(boolean whetherCheck) {
        this.whetherCheck = whetherCheck;
    }
}
