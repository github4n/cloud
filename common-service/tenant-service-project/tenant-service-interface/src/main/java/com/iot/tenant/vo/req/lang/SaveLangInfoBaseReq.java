package com.iot.tenant.vo.req.lang;

import com.iot.common.exception.BusinessException;
import com.iot.common.util.StringUtil;
import com.iot.tenant.enums.LangInfoObjectTypeEnum;
import com.iot.tenant.exception.TenantExceptionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称：cloud
 * 功能描述：保存文案基础信息入参
 * 创建人： yeshiyuan
 * 创建时间：2018/9/29 16:04
 * 修改人： yeshiyuan
 * 修改时间：2018/9/29 16:04
 * 修改描述：
 */
@ApiModel(description = "保存文案基础信息入参")
public class SaveLangInfoBaseReq {

    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "Long")
    private Long tenantId;

    @ApiModelProperty(name = "objectId", value = "对象ID", dataType = "Long")
    private Long objectId;

    @ApiModelProperty(name = "objectType", value = "对象类型", dataType = "String")
    private String objectType;

    @ApiModelProperty(name = "userId", value = "用户id", dataType = "Long")
    private Long userId;

    @ApiModelProperty(name = "belongModule", value = "所属模块", dataType = "String")
    private String belongModule;

    @ApiModelProperty(name = "langInfos", value = "语言信息", dataType = "List")
    private List<LangInfoReq> langInfos;

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

    public void setBelongModule(String belongModule) {
        this.belongModule = belongModule;
    }

    public static void checkParam(SaveLangInfoBaseReq req){
        if (!LangInfoObjectTypeEnum.checkObjectType(req.getObjectType())) {
            throw new BusinessException(TenantExceptionEnum.LANGINFO_OBJECTTYPE_ERROR);
        }
        if ( req.getObjectType().equals(LangInfoObjectTypeEnum.appConfig.name()) && req.getObjectId().intValue()!=-1 ) {
            throw new BusinessException(TenantExceptionEnum.LANGINFO_OBJECTID_ERROR);
        }
        if (req.getLangInfos() == null || req.getLangInfos().isEmpty() ) {
            throw new BusinessException(TenantExceptionEnum.LANGINFO_CONTENT_NULL);
        } else {
            Map<String, Integer> keyMap = new HashMap<>();
            req.getLangInfos().forEach(langInfo -> {
                if (keyMap.containsKey(langInfo.getKey())) {
                    throw new BusinessException(TenantExceptionEnum.LANGINFO_LANGKEY_EXISTS, langInfo.getKey());
                }
                keyMap.put(langInfo.getKey(),1);
            });
        }
    }
}
