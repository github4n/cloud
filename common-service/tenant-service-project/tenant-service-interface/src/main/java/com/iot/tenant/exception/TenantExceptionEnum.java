package com.iot.tenant.exception;


import com.iot.common.exception.IBusinessException;

public enum TenantExceptionEnum implements IBusinessException {
    TENANT_NOT_EXIST(400, "tenant.not.exist"),
    TENANT_CODE_EXIST(400, "tenant.code.exist"),
    USER_VIRTUAL_ORG_EXIST(400, "user.org.exist"),
    USER_VIRTUAL_ORG_NOT_EXIST(400, "user.org.not.exist"),
    USER_VIRTUAL_DEFAULT_USED_NOT_DELETE(400, "user.org.default.used.not.delete"),
    USER_VIRTUAL_DELETE_ERROR(400, "user.org.delete.error"),
	
    ADDRESID_IS_NULL(400, "addresId.is.null"),
	TENANTID_IS_NULL(400, "tenantId.is.null"),
	COUNTRY_IS_NULL(400, "country.is.null"),
	STATE_IS_NULL(400, "state.is.null"),
	CITY_IS_NULL(400, "city.is.null"),
	ADDRES_IS_NULL(400, "addres.is.null"),
	CONTACTERNAME_IS_NULL(400, "contacterName.is.null"),
	CONTACTERTEL_IS_NULL(400, "contacterTel.is.null"),
	ZIPCODE_IS_NULL(400, "zipCode.is.null"),
	CREATEBY_IS_NULL(400, "createBy.is.null"),
	UPDATEBY_IS_NULL(400, "updateBy.is.null"),
	
	PARAM_IS_NULL(400, "param.is.null"),
	OBJECTID_IS_NULL(400, "objectId.is.null"),
	OBJECTTYPE_IS_NULL(400, "objectType.is.null"),
	COPYWRITE_IS_NULL(400, "copywrite.is.null"),
	LANGTYPES_IS_NULL(400, "langTypes.is.null"),
	LANGKEY_IS_NULL(400, "langKey.is.null"),
	
	LANGINFO_OBJECTTYPE_ERROR(400,"langInfo.objectType.error"),
    LANGINFO_OBJECTID_ERROR(400,"langInfo.objectId.error"),
    LANGINFO_CONTENT_NULL(400,"langInfo.content.null"),
    LANGINFO_LANGTYPE_ERROR(400,"langInfo.langType.error"),
    LANGINFO_LANGKEY_EXISTS(400, "langInfo.langKey.exists"),
    PARAM_ERROR(400, "param.error"),

    /**App原始审核状态不为待审核*/
    APP_REVIEW_STATUS_ERROR(400, "app.review.status.error"),

    /**租户不一致*/
    APP_REVIEW_TENANT_INCONSISTENCY(400, "app.review.tenant.inconsistency"),

    /**APP信息为空*/
    APP_INFO_EMPTY_ERROR(400, "app.info.empty.error"),

    /**APP审核目标状态不对*/
    APP_REVIEW_TARGET_STATUS_ERROR(400, "app.review.target.status.error"),

    AUDITSTATUS_NO_PASS(400, "auditStatus.no.pass"),

    AUDITSTATUS_IS_NULL(400, "auditStatus.is.null");
	
    private int code;

    private String messageKey;

    TenantExceptionEnum(String messageKey) {
        code = 0;
        this.messageKey = messageKey;
    }

    TenantExceptionEnum(int code, String messageKey) {
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
