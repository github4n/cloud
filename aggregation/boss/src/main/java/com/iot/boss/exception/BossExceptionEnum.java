package com.iot.boss.exception;

import com.iot.common.exception.IBusinessException;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：异常枚举
 * 功能描述：异常枚举
 * 创建人： 李帅
 * 创建时间：2018年10月24日 下午5:09:20
 * 修改人：李帅
 * 修改时间：2018年10月24日 下午5:09:20
 */
public enum BossExceptionEnum implements IBusinessException {

    /**参数错误*/
    PARAM_IS_ERROR("param.is.error"),
	GOODSINFO_NOT_EXIST("goodsInfo.not.exist"),
	ORDERID_IS_NULL("orderId.is.null"),
	ORDERID_NOT_EXIST("orderId.not.exist"),
	UUIDREFUNDREQ_IS_NULL("uuidRefundReq.is.null."),
	TENANTID_IS_NULL("tenantId.is.null."),
	REFUND_EXCEED_ORDER("The.refund.amount.exceeds.the.order.amount"),
	TENANT_CODE_ERROR("Tenant.Code.must.be.a.3.to.6.bit.string"),
	ORDERDETAILINFO_NOT_EXIST("orderDetailInfo.not.exist"),
    PACKAGE_PARAMS_NOT_EXIST("package.params.not.exits"),
    PACKAGE_IMAGE_IS_NULL("package.image.is.null"),
    PACKAGE_NAME_IS_NULL("package.name.is.null"),
    PACKAGE_ID_IS_NULL("package.id.is.null"),
    PACKAGE_TYPE_IS_NULL("package.type.is.null"),
    PACKAGE_JSON_IS_ERROR("json.is.error"),
    PACKAGE_DEVICETYPE_ERROR("there must be a device type of technology type gateway"),
    PACKAGE_SCENE_NUMBER_ERROR("the number of scenes should not exceed 30"),
    PACKAGE_TEMPRULE_NUMBER_ERROR("the number of tempRule should not exceed 30"),
    SCENE_CONFIG_IS_ERROR("scene.config.is.error"),
    TEMPLATE_PARAM_IS_NULL("param.is.null");
    private int code;

    private String messageKey;

    BossExceptionEnum(String messageKey) {
        code = 0;
        this.messageKey = messageKey;
    }

    BossExceptionEnum(int code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }
}
