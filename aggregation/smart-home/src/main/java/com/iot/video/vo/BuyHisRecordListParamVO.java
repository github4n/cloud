package com.iot.video.vo;

import com.iot.vo.AggBaseVO;
import io.swagger.annotations.ApiModel;

/**
 * 项目名称：IOT视频云
 * 模块名称：聚合层
 * 功能描述：购买历史查询参数VO
 * 创建人： mao2080@sina.com
 * 创建时间：2018/3/26 16:56
 * 修改人： mao2080@sina.com
 * 修改时间：2018/3/26 16:56
 * 修改描述：
 */
@ApiModel
public class BuyHisRecordListParamVO extends AggBaseVO {

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 计划id
     */
    private String planId;

    /**
     * 当前页码
     */
    private int pageNum;

    /**
     * 每页条数
     */
    private int pageSize;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
