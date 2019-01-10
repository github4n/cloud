package com.iot.portal.web.vo.req;

import com.iot.common.exception.BusinessException;
import com.iot.common.util.StringUtil;
import com.iot.portal.exception.BusinessExceptionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 项目名称：cloud
 * 功能描述：服务购买提交入参
 * 创建人： yeshiyuan
 * 创建时间：2018/9/13 17:52
 * 修改人： yeshiyuan
 * 修改时间：2018/9/13 17:52
 * 修改描述：
 */
@ApiModel(description = "服务购买提交入参")
public class ServiceBuySubmitReq {

    @ApiModelProperty(name = "totalPrice", value = "订单总价", dataType = "BigDecimal")
    private BigDecimal totalPrice;

    @ApiModelProperty(name = "currency", value = "货币单位", dataType = "string")
    private String currency;

    @ApiModelProperty(name = "serviceId", value = "绑定服务id", dataType = "String")
    private Long serviceId;

    @ApiModelProperty(name = "goodsId", value = "商品id(AES加密)", dataType = "String")
    private String goodsId;

    @ApiModelProperty(name = "goodsTypeId", value = "商品类型（AES加密，3:app打包；4：第三方服务）", dataType = "String")
    private String goodsTypeId;

    @ApiModelProperty(name = "addDemandDesc", value = "附加需求描述", dataType = "String")
    private String addDemandDesc;

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

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(String goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
    }

    public String getAddDemandDesc() {
        return addDemandDesc;
    }

    public void setAddDemandDesc(String addDemandDesc) {
        this.addDemandDesc = addDemandDesc;
    }

    public static void checkParam(ServiceBuySubmitReq req) {
        if (req == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR);
        }
        if (req.getTotalPrice() == null || req.getTotalPrice().compareTo(new BigDecimal(0)) == -1){
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "totalPirce is error");
        }
        if (StringUtil.isBlank(req.getGoodsId())) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "goodsId is null");
        }
        if (StringUtil.isBlank(req.getCurrency())) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "currency is null");
        }
        if (req.getServiceId()==null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "serviceId is null");
        }
        if (StringUtil.isBlank(req.getGoodsTypeId())) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "goodsTypeId is null");
        }
    }
}
