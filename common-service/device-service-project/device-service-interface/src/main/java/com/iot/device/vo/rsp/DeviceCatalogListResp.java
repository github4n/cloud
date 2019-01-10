package com.iot.device.vo.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:55 2018/6/28
 * @Modify by:
 */
@ApiModel("设备目录信息")
public class DeviceCatalogListResp implements Serializable {
    //id
    @ApiModelProperty(value = "id")
    private Long id;
    //名称
    @ApiModelProperty(value = "名称")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
