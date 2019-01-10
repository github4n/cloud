package com.iot.portal.exception;

import com.iot.common.exception.IBusinessException;

public enum BusinessExceptionEnum implements IBusinessException {
    /**
     * 用户头像为空
     */
    USER_PHOTO_IS_EMPTY("videoCloud.user.photo.empty"),
    /**
     * 用户头像上传失败
     */
    USER_PHOTO_UPLOAD_FAILED("videoCloud.user.photo.upload.failed"),

    USER_NEED_LOGIN("user.need.login"),

    PARAM_NOT_NULL("param.not.null"),

    USER_NOT_EXIST("user.not.exist"),

    USER_DOES_NOT_BELONG_THIS_TENANT("user.does.not.belong.this.tenant"),

    TENANT_INFO_IS_NULL("tenant.info.is.null"),

    CHOOSE_AT_LEAST_ONE_ROLE("choose.at.least.one.role"),

    PASSWORD_IS_NOT_THE_SAME("oldPassword.is.not.correct"),

    /**
     * 参数错误
     */
    PARAM_IS_ERROR("Parameter error."),


    TENANT_ID_ERROR("Tenant ID error."),

    /**
     * 文件为空
     */
    UPLOAD_FILE_IS_NULL("upload.file.is.null"),

    /**
     * 上传文件失败
     */
    UPLOAD_FILE_UPLOAD_FAILED("upload.file.upload.failed"),

    /**
     * 文件格式不支持
     */
    UPLOAD_FILE_TYPE_N0T_SUPPORT("upload.file.type.notsupport"),

    /**
     * 文件超过最大限制大小
     */
    EXCEED_MAX_FILE_SIZE("exceed.max.file.size"),

    ACCESS_DENIED("access.denied"),

    SAVE_PACK_FILE_ERROE("Unable to save the pack file."),

    /**
     * 支付回调出错
     */
    PAY_CALLBACK_ERROR("pay.callBack.error"),

    APP_INFO_NOT_FOUND("Cannot find the APP information."),

    PACKNAME_IS_NULL("The package name is empty."),
    
    TEST_GOOGLE_JSON_CONFIG_FAILED("Configuration failed. Please check your FCM information."),
    TEST_P12CERT_CONFIG_FAILED("P12 certificate configuration failed. Please check and try again."),
    TEST_MOBILEPROVISION_CONFIG_FAILED("test.mobileprovision.config.failed"),
    
    GOODS_INFO_NOT_FOUND("goods.info.not.found"),

    SEND_PACK_REQ_ERROE("send.pack.req.error"),

    PAY_GET_URL_ERROR("pay.getUrl.error"),

    FILE_DOWNLOAD_ERROR("Unable to download the file.\n");

    /**
     * 异常代码
     */
    private int code;

    /**
     * 异常描述
     */
    private String messageKey;

    BusinessExceptionEnum(String messageKey) {
        this.code = 0;
        this.messageKey = messageKey;
    }

    /**
     * 描述：构建异常
     *
     * @param code       错误代码
     * @param messageKey 错误描述
     * @return
     * @author mao2080@sina.com
     * @created 2017年3月21日 上午10:50:58
     * @since
     */
    BusinessExceptionEnum(Integer code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

    public int getCode() {
        return code;
    }

    public String getMessageKey() {
        return messageKey;
    }

}
