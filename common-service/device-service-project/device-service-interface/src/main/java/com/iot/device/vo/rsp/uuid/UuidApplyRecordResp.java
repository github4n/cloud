package com.iot.device.vo.rsp.uuid;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：uuid申请记录支付所需的信息
 * 创建人： yeshiyuan
 * 创建时间：2018/7/4 15:14
 * 修改人： yeshiyuan
 * 修改时间：2018/7/4 15:14
 * 修改描述：
 */
@ApiModel(value = "uuid申请记录信息", description = "uuid申请记录信息")
public class UuidApplyRecordResp {

    @ApiModelProperty(name = "id", value = "uuid申请记录id", dataType = "long")
    private Long id;

    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "long")
    private Long tenantId;

    @ApiModelProperty(name = "userId", value = "用户id", dataType = "long")
    private Long userId;

    @ApiModelProperty(name = "orderId", value = "订单id", dataType = "String")
    private String orderId;

    @ApiModelProperty(name = "payStatus", value = "支付状态", dataType = "Integer")
    private Integer payStatus;

    @ApiModelProperty(name = "createNum", value = "下载次数", dataType = "Integer")
    private Integer downNum;

    @ApiModelProperty(name = "createNum", value = "生成数量", dataType = "Integer")
    private Integer createNum;

    @ApiModelProperty(name = "goodsId", value = "商品id，对应goods_info主键（主要用于做方案条件搜索）", dataType = "Long")
    private Long goodsId;

    @ApiModelProperty(name = "uuidApplyStatus", value = "uuid申请状态（1:处理中;2:已完成;3:生成失败;4: P2PID不足）", dataType = "Integer")
    private Integer uuidApplyStatus;

    @ApiModelProperty(name = "fileId", value = "UUID列表zip文件ID", dataType = "String")
    private String fileId;

    @ApiModelProperty(name = "productId", value = "产品id", dataType = "Long")
    private Long productId;

    @ApiModelProperty(name = "uuidValidityYear", value = "有效时长", dataType = "Integer")
    private Integer uuidValidityYear;

    @ApiModelProperty(name = "createTime", value = "创建时间", dataType = "date")
    private Date createTime;

    @ApiModelProperty(name = "updateTime", value = "修改时间", dataType = "date")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getDownNum() {
        return downNum;
    }

    public void setDownNum(Integer downNum) {
        this.downNum = downNum;
    }

    public Integer getCreateNum() {
        return createNum;
    }

    public void setCreateNum(Integer createNum) {
        this.createNum = createNum;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getUuidApplyStatus() {
        return uuidApplyStatus;
    }

    public void setUuidApplyStatus(Integer uuidApplyStatus) {
        this.uuidApplyStatus = uuidApplyStatus;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getUuidValidityYear() {
        return uuidValidityYear;
    }

    public void setUuidValidityYear(Integer uuidValidityYear) {
        this.uuidValidityYear = uuidValidityYear;
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
