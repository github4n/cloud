package com.iot.payment.entity.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：订单记录
 * 创建人： yeshiyuan
 * 创建时间：2018/7/3 11:39
 * 修改人： yeshiyuan
 * 修改时间：2018/7/3 11:39
 * 修改描述：
 */
@ApiModel(value = "订单记录",description = "订单记录")
public class OrderRecord {

    @ApiModelProperty(name = "id", value = "订单id", dataType = "string")
    private String id;

    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "Long")
    private Long tenantId;

    @ApiModelProperty(name = "userId", value = "用户id", dataType = "Long")
    private Long userId;

    @ApiModelProperty(name = "totalPrice", value = "订单总价", dataType = "BigDecimal")
    private BigDecimal totalPrice;

    @ApiModelProperty(name = "currency", value = "货币单位", dataType = "string")
    private String currency;

    @ApiModelProperty(name = "orderType", value = "订单类型，对应字典表sys_dict_item的type_id=2的记录", dataType = "int")
    private Integer orderType;

    @ApiModelProperty(name = "orderStatus", value = "订单状态,对应字典表sys_dict_item的type_id=3的记录（0:已关闭；1:待付款；2:付款失败；3:付款成功；4：退款中；5：退款成功；6：退款失败）", dataType = "int")
    private Integer orderStatus;

    @ApiModelProperty(name = "createTime", value = "创建时间", dataType = "date")
    private Date createTime;

    @ApiModelProperty(name = "updateTime", value = "修改时间", dataType = "date")
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
