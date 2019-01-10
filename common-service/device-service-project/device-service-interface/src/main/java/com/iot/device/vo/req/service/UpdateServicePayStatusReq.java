package com.iot.device.vo.req.service;

import com.iot.common.exception.BusinessException;
import com.iot.common.util.StringUtil;
import com.iot.device.enums.ServicePayStatusEnum;
import com.iot.device.exception.ServiceBuyRecordExceptionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：修改虚拟服务订单支付状态入参
 * 创建人： yeshiyuan
 * 创建时间：2018/9/13 16:50
 * 修改人： yeshiyuan
 * 修改时间：2018/9/13 16:50
 * 修改描述：
 */
@ApiModel(description = "修改虚拟服务订单支付状态入参")
public class UpdateServicePayStatusReq {

    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "Long")
    private Long tenantId;

    @ApiModelProperty(name = "orderId", value = "订单id，对应order_record表主键", dataType = "String")
    private String orderId;

    @ApiModelProperty(name = "oldPayStatus", value = "旧支付状态", dataType = "Integer")
    private Integer oldPayStatus;

    @ApiModelProperty(name = "newPayStatus", value = "新支付状态", dataType = "Integer")
    private Integer newPayStatus;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getOldPayStatus() {
        return oldPayStatus;
    }

    public void setOldPayStatus(Integer oldPayStatus) {
        this.oldPayStatus = oldPayStatus;
    }

    public Integer getNewPayStatus() {
        return newPayStatus;
    }

    public void setNewPayStatus(Integer newPayStatus) {
        this.newPayStatus = newPayStatus;
    }

    public UpdateServicePayStatusReq() {
    }

    public UpdateServicePayStatusReq(Long tenantId, String orderId, Integer oldPayStatus, Integer newPayStatus) {
        this.tenantId = tenantId;
        this.orderId = orderId;
        this.oldPayStatus = oldPayStatus;
        this.newPayStatus = newPayStatus;
    }

    public static void check(UpdateServicePayStatusReq req){
        if (req == null) {
            throw new BusinessException(ServiceBuyRecordExceptionEnum.PARAM_ERROR, "param is null");
        }
        if (StringUtil.isBlank(req.getOrderId())) {
            throw new BusinessException(ServiceBuyRecordExceptionEnum.PARAM_ERROR, "orderId is null");
        }
        if (!ServicePayStatusEnum.checkIsValid(req.getNewPayStatus())) {
            throw new BusinessException(ServiceBuyRecordExceptionEnum.PARAM_ERROR, "payStatus error");
        }
    }
}
