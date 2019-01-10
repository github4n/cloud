package com.iot.device.vo.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 15:01 2018/6/28
 * @Modify by:
 */
@Data
@ToString
@ApiModel("设备类型信息")
public class DeviceTypeListResp implements Serializable {
    //id
    @ApiModelProperty(value = "设备类型id")
    private Long id;
    //类别名称
    @ApiModelProperty(value = "类别名称")
    private String name;
    //唯一标识
    @ApiModelProperty(value = "唯一标识")
    private String code;

    private String img;

    /**
     * 是否为免开发产品 0否 1是soc
     */
    @ApiModelProperty(value = "0非免开发产品，1免开发产品")
    private Integer whetherSoc;

    @ApiModelProperty(value = "ifttt类型(0:不支持 1：支持if 2:支持then 3:支持if支持then)")
    private Integer iftttType;

    private List<SmartDeviceTypeResp> smartDeviceTypeResps;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getWhetherSoc() {
        return whetherSoc;
    }

    public void setWhetherSoc(Integer whetherSoc) {
        this.whetherSoc = whetherSoc;
    }

    public List<SmartDeviceTypeResp> getSmartDeviceTypeResps() {
        return smartDeviceTypeResps;
    }

    public void setSmartDeviceTypeResps(List<SmartDeviceTypeResp> smartDeviceTypeResps) {
        this.smartDeviceTypeResps = smartDeviceTypeResps;
    }
}
