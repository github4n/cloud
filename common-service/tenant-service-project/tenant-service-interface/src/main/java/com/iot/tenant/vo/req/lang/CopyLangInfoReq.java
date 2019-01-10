package com.iot.tenant.vo.req.lang;

import com.iot.common.exception.BusinessException;
import com.iot.tenant.enums.LangInfoObjectTypeEnum;
import com.iot.tenant.exception.TenantExceptionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：拷贝基础文案入参
 * 创建人： yeshiyuan
 * 创建时间：2018/9/30 14:23
 * 修改人： yeshiyuan
 * 修改时间：2018/9/30 14:23
 * 修改描述：
 */
@ApiModel(description = "拷贝基础文案入参")
public class CopyLangInfoReq {

    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "Long")
    private Long tenantId;

    @ApiModelProperty(name = "objectId", value = "对象ID", dataType = "Long")
    private Long objectId;

    @ApiModelProperty(name = "copyObjectId", value = "拷贝对象id", dataType = "Long")
    private Long copyObjectId;

    @ApiModelProperty(name = "objectType", value = "对象类型", dataType = "String")
    private String objectType;

    @ApiModelProperty(name = "userId", value = "用户id", dataType = "Long")
    private Long userId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCopyObjectId() {
        return copyObjectId;
    }

    public void setCopyObjectId(Long copyObjectId) {
        this.copyObjectId = copyObjectId;
    }

    public static void checkParam(CopyLangInfoReq req) {
        if (!LangInfoObjectTypeEnum.checkObjectType(req.getObjectType())) {
            throw new BusinessException(TenantExceptionEnum.LANGINFO_OBJECTTYPE_ERROR);
        }
        if (req.getTenantId()==null) {
            throw new BusinessException(TenantExceptionEnum.TENANTID_IS_NULL);
        }
        if (req.getObjectId() == null) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "objectId is null");
        }
        if (req.getUserId() == null) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "userId is null");
        }
        if (req.getCopyObjectId() == null) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "copyObjectId is null");
        }
    }

    public CopyLangInfoReq() {
    }

    public CopyLangInfoReq(Long tenantId, Long objectId, Long copyObjectId, String objectType, Long userId) {
        this.tenantId = tenantId;
        this.objectId = objectId;
        this.copyObjectId = copyObjectId;
        this.objectType = objectType;
        this.userId = userId;
    }
}
