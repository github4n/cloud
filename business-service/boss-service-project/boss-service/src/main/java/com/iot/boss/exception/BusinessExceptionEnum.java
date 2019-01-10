package com.iot.boss.exception;

import com.iot.common.exception.IBusinessException;

/** 
 * 
 * 项目名称：IOT视频云
 * 模块名称：聚合层
 * 功能描述：视频云聚合层异常枚举
 * 创建人： mao2080@sina.com 
 * 创建时间：2018/3/22 15:16
 * 修改人： mao2080@sina.com 
 * 修改时间：2018/3/22 15:16
 * 修改描述：
 */
public enum BusinessExceptionEnum implements IBusinessException {

    /**报障单不存在*/
    MALF_RECORD_NOT_EXISTS("bossservice.malfrecord.notExists"),

    /**管理员不存在*/
	ADMIN_NOT_EXISTS("bossService.admin.notExists"),

    /**没有足够的权限*/
    ADMIN_AUTH_NOT_ENOUGH("bossService.admin.notEnoughAuth"),

    /**校验不通过*/
    MALF_RECORD_CHECK_NO_PASS("bossService.malf.checkNoPass"),

    /**用户id为空*/
    MALF_RECORD_CHECK_NO_USERID("bossService.malf.userIdIsNull"),

    /**报障描述为空*/
    MALF_RECORD_CHECK_NO_DESC("bossService.malf.malfDescIsNull"),

    /**租户ID不存在*/
    MALF_RECORD_CHECK_NO_TENANTID("bossService.malf.tenantIdIsNull"),

    /**超级管理员不存在*/
    SUPERADMIN_NOT_EXISTS("bossService.superAdmin.notExists"),

    /**报障单等级为空*/
    MALF_RECORD_CHECK_NO_MALFRANK("bossService.malf.malfRankIsNull"),

    /**处理人员ID为空*/
    MALF_RECORD_CHECK_NO_HANDLEADMINID("bossService.malf.handleAdminIdIsNull"),

    /**处理状态不匹配*/
    PROCESSING_STATE_NOT_MATCH("bossService.processing.state.not.match"),

    PEOPLE_NOT_OPERATMAN("bossService.people.not.operatMan"),

    /**参数错误*/
    PARAM_ERROR("bossService.param.error"),

    OBJECT_IS_NULL("bossService.objectIsNull"),

    HANDLE_STATUS_ERROR("bossService.handleStatusError"),

    MALF_STATUS_ERROR("bossService.handleStatusError"),

    MALF_RANK_ERROR("bossService.handleRankError"),

    MALF_DATE_TYPE_ERROR("bossService.dateTypeError"),

    /**订单重复申请退款*/
    VIDEO_REFUND_ORDER_REPEAT("bossService.refund.order.repeat"),
    /**不是最新的订单*/
    VIDEO_REFUND_NOT_LATEST_ORDER("bossService.refund.not.latest.order"),
    /**申请退款超出*/
    VIDEO_REFUND_PRICE_BEYOND("bossService.refund.price.beyond"),
    /**租户id为空*/
    VIDEO_REFUND_CHECK_NO_TENANTID("bossService.refund.tenantIdIsNull"),
    /**计划id为空*/
    VIDEO_REFUND_CHECK_NO_PLANID("bossService.refund.planIdIsNull"),
    /**订单id为空*/
    VIDEO_REFUND_CHECK_NO_ORDERID("bossService.refund.orderIdIsNull"),
    /**退款金额为空*/
    VIDEO_REFUND_CHECK_NO_PRICE("bossService.refund.priceIsNull"),
    /**退款理由为空*/
    VIDEO_REFUND_CHECK_NO_REASON("bossService.refund.reasonIsNull"),
    /**操作人Id为空*/
    VIDEO_REFUND_CHECK_NO_APPLYID("bossService.refund.applyIdIsNull"),

    INTERNAL_ERROR("bossService.internalError"),

    /**
     * 退款记录不存在
     */
    REFUND_RECORD_NOT_EXISTS("bossService.refundRecord.notExists"),
    /**退款记录审核状态错误*/
    REFUND_RECORD_AUDIT_STATUS_ERROR("bossService.refundRecord.auditStatus.error"),
    /**退款记录退款状态错误*/
    REFUND_RECORD_REFUND_STATUS_ERROR("bossService.refundRecord.refundStatus.error"),
    /**退款支付失败*/
    REFUND_PAY_ERROR("bossService.refundRecord.refundPay.error");

    private String messageKey;

    BusinessExceptionEnum(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public int getCode() {
        return 0;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }

}