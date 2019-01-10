package com.iot.video.dto;

import io.swagger.annotations.ApiModel;

import java.util.List;

/**
 * 项目名称：IOT视频云
 * 模块名称：服务层
 * 功能描述：视频最后一帧查询参数VO
 * 创建人： mao2080@sina.com
 * 创建时间：2018/3/23 15:18
 * 修改人： mao2080@sina.com
 * 修改时间：2018/3/23 15:18
 * 修改描述：
 */
@ApiModel
public class LastPicParamDto extends BaseDto {

    /**计划id集合*/
    private List<String> deviceIdList;



    public LastPicParamDto() {

    }

    public List<String> getDeviceIdList() {
        return deviceIdList;
    }

    public void setDeviceIdList(List<String> deviceIdList) {
        this.deviceIdList = deviceIdList;
    }
}
