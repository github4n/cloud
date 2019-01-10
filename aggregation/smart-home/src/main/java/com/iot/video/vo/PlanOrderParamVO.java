package com.iot.video.vo;

import com.iot.vo.AggBaseVO;
import io.swagger.annotations.ApiModel;

import java.util.List;

/**
 * 项目名称：立达信IOT视频云
 * 模块名称：聚合层
 * 功能描述：排序VO类
 * 创建人： mao2080@sina.com
 * 创建时间：2018/3/23 14:43
 * 修改人： mao2080@sina.com
 * 修改时间：2018/3/23 14:43
 * 修改描述：
 */
@ApiModel
public class PlanOrderParamVO extends AggBaseVO {

    /**
     * 计划id集合
     */
    private List<String> planIds;

    /**
     * 排序位置id集合
     */
    private List<String> orderIds;
    
    public List<String> getPlanIds() {
        return planIds;
    }

    public void setPlanIds(List<String> planIds) {
        this.planIds = planIds;
    }

    public List<String> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<String> orderIds) {
        this.orderIds = orderIds;
    }

}
