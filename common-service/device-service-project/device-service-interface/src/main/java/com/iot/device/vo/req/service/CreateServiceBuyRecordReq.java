package com.iot.device.vo.req.service;

import com.iot.common.exception.BusinessException;
import com.iot.device.exception.ServiceBuyRecordExceptionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：创建虚拟服务购买记录入参
 * 创建人： yeshiyuan
 * 创建时间：2018/9/13 15:20
 * 修改人： yeshiyuan
 * 修改时间：2018/9/13 15:20
 * 修改描述：
 */
@ApiModel(description = "创建虚拟服务购买记录入参")
public class CreateServiceBuyRecordReq {

    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "Long")
    private Long tenantId;

    @ApiModelProperty(name = "userId", value = "用户id", dataType = "Long")
    private Long userId;

    @ApiModelProperty(name = "orderId", value = "订单id，对应order_record表主键", dataType = "String")
    private String orderId;

    @ApiModelProperty(name = "serviceId", value = "虚拟服务id", dataType = "Long")
    private Long serviceId;

    @ApiModelProperty(name = "goodsId", value = "商品id，对应goods_info主键", dataType = "Long")
    private Long goodsId;

    @ApiModelProperty(name = "goodsTypeId", value = "商品类别id，字典表：type_id=4", dataType = "Integer")
    private Integer goodsTypeId;

    @ApiModelProperty(name = "goodsNum", value = "购买数量", dataType = "Integer")
    private Integer goodsNum;

    @ApiModelProperty(name = "addDemandDesc", value = "附加需求描述", dataType = "String")
    private String addDemandDesc;

    @ApiModelProperty(name = "createTime", value = "创建时间", dataType = "Date")
    private Date createTime;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(Integer goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getAddDemandDesc() {
        return addDemandDesc;
    }

    public void setAddDemandDesc(String addDemandDesc) {
        this.addDemandDesc = addDemandDesc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public static boolean check(CreateServiceBuyRecordReq recordReq) {
        if (recordReq == null) {
            throw new BusinessException(ServiceBuyRecordExceptionEnum.PARAM_ERROR, "CreateServiceBuyRecordReq is null");
        }
        if (recordReq.getGoodsId() == null) {
            throw new BusinessException(ServiceBuyRecordExceptionEnum.PARAM_ERROR, "goodsId is null");
        }
        if (recordReq.getServiceId()==null) {
            throw new BusinessException(ServiceBuyRecordExceptionEnum.PARAM_ERROR, "serviceId is null");
        }
        if (recordReq.getGoodsNum() == null || recordReq.getGoodsNum().compareTo(1) == -1) {
            throw new BusinessException(ServiceBuyRecordExceptionEnum.PARAM_ERROR, "goodsNum must be greater than 0");
        }
        if (recordReq.getTenantId() == null) {
            throw new BusinessException(ServiceBuyRecordExceptionEnum.PARAM_ERROR, "tenantId is null");
        }
        if (recordReq.getUserId() == null) {
            throw new BusinessException(ServiceBuyRecordExceptionEnum.PARAM_ERROR, "userId is null");
        }
        if (recordReq.getOrderId() == null) {
            throw new BusinessException(ServiceBuyRecordExceptionEnum.PARAM_ERROR, "orderId is null");
        }
        if (recordReq.getCreateTime() == null) {
            throw new BusinessException(ServiceBuyRecordExceptionEnum.PARAM_ERROR, "createTime is null");
        }
        return true;
    }

    public CreateServiceBuyRecordReq() {
    }

    public CreateServiceBuyRecordReq(Long tenantId, Long userId, String orderId, Long serviceId, Long goodsId, Integer goodsTypeId, Integer goodsNum, String addDemandDesc, Date createTime) {
        this.tenantId = tenantId;
        this.userId = userId;
        this.orderId = orderId;
        this.serviceId = serviceId;
        this.goodsId = goodsId;
        this.goodsTypeId = goodsTypeId;
        this.goodsNum = goodsNum;
        this.addDemandDesc = addDemandDesc;
        this.createTime = createTime;
    }
}
