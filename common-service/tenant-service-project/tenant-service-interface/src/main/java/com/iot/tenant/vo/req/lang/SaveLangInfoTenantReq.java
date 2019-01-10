package com.iot.tenant.vo.req.lang;

import com.iot.common.exception.BusinessException;
import com.iot.common.util.StringUtil;
import com.iot.tenant.enums.LangInfoObjectTypeEnum;
import com.iot.tenant.enums.LangTypeEnum;
import com.iot.tenant.exception.TenantExceptionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：保存租户文案信息入参
 * 创建人： yeshiyuan
 * 创建时间：2018/9/29 16:04
 * 修改人： yeshiyuan
 * 修改时间：2018/9/29 16:04
 * 修改描述：
 */
@ApiModel(description = "保存租户文案信息入参")
public class SaveLangInfoTenantReq {

    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "Long")
    private Long tenantId;

    @ApiModelProperty(name = "objectId", value = "对象ID", dataType = "String")
    private String objectId;

    @ApiModelProperty(name = "objectType", value = "对象类型", dataType = "String")
    private String objectType;

    @ApiModelProperty(name = "userId", value = "用户id", dataType = "Long")
    private Long userId;

    @ApiModelProperty(name = "belongModule", value = "所属模块", dataType = "String")
    private String belongModule;

    @ApiModelProperty(name = "addLangType", value = "新增语言类型", dataType = "List")
    private List<String> addLangType;

    @ApiModelProperty(name = "delLangType", value = "删除语言类型", dataType = "List")
    private List<String> delLangType;

    @ApiModelProperty(name = "langInfos", value = "语言信息", dataType = "List")
    private List<LangInfoReq> langInfos;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public List<LangInfoReq> getLangInfos() {
        return langInfos;
    }

    public void setLangInfos(List<LangInfoReq> langInfos) {
        this.langInfos = langInfos;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBelongModule() {
        if (StringUtil.isBlank(belongModule)) {
            return null;
        }
        return belongModule;
    }

    public List<String> getAddLangType() {
        return addLangType;
    }

    public void setAddLangType(List<String> addLangType) {
        this.addLangType = addLangType;
    }

    public List<String> getDelLangType() {
        return delLangType;
    }

    public void setDelLangType(List<String> delLangType) {
        this.delLangType = delLangType;
    }

    public void setBelongModule(String belongModule) {
        this.belongModule = belongModule;
    }

    public static void checkParam(SaveLangInfoTenantReq req) {
        if (req == null) {
            throw new BusinessException(TenantExceptionEnum.PARAM_IS_NULL);
        }
        if (!LangInfoObjectTypeEnum.checkObjectType(req.getObjectType())) {
            throw new BusinessException(TenantExceptionEnum.LANGINFO_OBJECTTYPE_ERROR);
        }
        if (req.getTenantId()==null) {
            throw new BusinessException(TenantExceptionEnum.TENANTID_IS_NULL);
        }
        if (StringUtil.isBlank(req.getObjectId())) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "objectId is null");
        }
        if (req.getUserId() == null) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "userId is null");
        }
        if (req.getObjectType().equals(LangInfoObjectTypeEnum.appConfig.toString()) && StringUtil.isBlank(req.getBelongModule())) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "belongModule is null");
        }
        if ( req.addLangType != null) {
            req.addLangType.forEach(langType -> {
                if(!LangTypeEnum.checkLangType(langType)) {
                    throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "add lang type error");
                }
            });
        }
    }




}
