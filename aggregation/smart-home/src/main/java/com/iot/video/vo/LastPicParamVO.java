package com.iot.video.vo;

import com.iot.vo.AggBaseVO;
import io.swagger.annotations.ApiModel;

import java.util.List;

/**
 * 项目名称：立达信IOT视频云
 * 模块名称：聚合层
 * 功能描述：视频最后一帧查询参数VO
 * 创建人： mao2080@sina.com
 * 创建时间：2018/3/23 15:18
 * 修改人： mao2080@sina.com
 * 修改时间：2018/3/23 15:18
 * 修改描述：
 */
@ApiModel
public class LastPicParamVO extends AggBaseVO {

    /**
     * 计划id集合
     */
    private List<String> deviceIdList;

    public List<String> getDeviceIdList() {
        return deviceIdList;
    }

    public void setDeviceIdList(List<String> deviceIdList) {
        this.deviceIdList = deviceIdList;
    }
}
