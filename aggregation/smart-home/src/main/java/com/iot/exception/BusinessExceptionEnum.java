package com.iot.exception;

import com.iot.common.exception.IBusinessException;

/**
 * 项目名称：立达信IOT视频云
 * 模块名称：聚合层
 * 功能描述：视频云聚合层异常枚举
 * 创建人： mao2080@sina.com
 * 创建时间：2018/3/22 15:16
 * 修改人： mao2080@sina.com
 * 修改时间：2018/3/22 15:16
 * 修改描述：
 */
public enum BusinessExceptionEnum implements IBusinessException {

    UNKNOWN_EXCEPTION("videoCloud.unknow.exception"),

    CONVERT_TO_LIST_EXCEPTION("videoCloud.convertToList.exception"),

    CONVERT_TO_DTO_EXCEPTION("videoCloud.convertToDto.exception"),

    EMPTY_OF_PARAM_EXCEPTION("videoCloud.param.empty.exception"),

    APP_VERIFY_FAIL("videoCloud.app.verify.fail.exception"),

    PARAM_ERROR("videoCloud.param.error"),

    EMPTY_OF_WEBPLANVO("videoCloud.webPlanVo.empty"),

    EMPTY_OF_TENANTID("videoCloud.tenantId.empty"),

    EMPTY_OF_USERID("videoCloud.userId.empty"),

    EMPTY_OF_PACKAGEID("videoCloud.packageId.empty"),

    EMPTY_OF_COUNTS("videoCloud.counts.empty"),

    EMPTY_OF_PAYPRICE("videoCloud.payPrice.empty"),

    EMPTY_OF_CURRENCY("videoCloud.currency.empty"),

    EMPTY_OF_PLANID("videoCloud.planId.empty"),

    EMPTY_OF_PACKAGENAME("videoCloud.packageName.empty"),

    /**
     * 交易记录不存在
     */
    PAY_TRANSATION_IS_NOT_EXIST("videoCloud.PayTransation.not.exist"),

    /**
     * 金额不一致
     */
    PAYPRICE_NOT_SAME("videoCloud.payPrice.not.same"),

    EMPTY_OF_APPPLAN("videoCloud.appPlan.empty"),

    EMPTY_OF_PAYID("videoCloud.payId.empty"),

    /**
     * 用户头像为空
     */
    USER_PHOTO_IS_EMPTY("videoCloud.user.photo.empty"),

    /**
     * 用户头像上传失败
     */
    USER_PHOTO_UPLOAD_FAILED("videoCloud.user.photo.upload.failed"),

    PLAN_BANDING_DEVICE_FAILED("videoCloud.plan.banding.device.failed"),

    GET_DEVICELISET_FAILED("videoCloud.get.device.list.failed"),

    GET_UNBIND_PLAN_DEVICELIST_FAILED("videoCloud.get.unbind.plan.device.list.failed"),

    PLAN_IS_NOT_EXIST("videoCloud.plan.not.exist"),

    DEVICE_IS_NOT_EXIST("videoCloud.device.not.exist"),

    VIDEO_PACKAGE_ERROR("videoCloud.videoPackage.error"),

    /**
     * 校验不通过
     */
    CHECK_NO_PASS("smartHome.check.noPass"),

    REQ_TIMEOUT_FORBIDDEN(401, "request.forbidden"),

    /**
     * 错误描述
     */
    ERROE("smartHome.error"),

    SHARE_HEAD_ERROR(74273, "share.head.error"),
    ARNOO_ACCOUNT_ERROR(13001, "the.arnoo.account.or.name.is.incorrect"),
    ARNOO_SHARE_NOT_INVITE_MYSELF_ERROR(13002, "the.arnoo.not.invite.myself"),
    ARNOO_SHARE_NOT_REPEAT_INVITE_ERROR(13003, "the.arnoo.not.repeat.invite"),
    ARNOO_SHARE_EXPIRE_INVITE_ERROR(13004, "the.arnoo.expire.invite"),
    ARNOO_SHARE_UPPER_INVITE_MAX_SIZE_ERROR(13005, "the.arnoo.upper.invite.max.size"),
    ARNOO_SHARE_NOT_EXIST(13006, "the.arnoo.share.home.not.exist"),
    ARNOO_SHARE_NOT_PERMISSION(13007, "the.arnoo.share.not.permission"),
    ARNOO_SHARE_BUSINESS_ERROR(13008, "the.arnoo.share.business.error"),
    ARNOO_SHARE_COMMIT_TIMEOUT_ERROR(13009, "the.arnoo.share.commit.timeout"),
    ;

    private int code;

    private String messageKey;


    BusinessExceptionEnum(String messageKey) {
        this.code = 0;
        this.messageKey = messageKey;
    }

    BusinessExceptionEnum(int code, String messageKey) {
        this.code = code;
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