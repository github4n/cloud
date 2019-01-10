package com.iot.tenant.vo.req.lang;

import com.iot.common.exception.BusinessException;
import com.iot.tenant.enums.LangInfoObjectTypeEnum;
import com.iot.tenant.exception.TenantExceptionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：删除文案基础信息（BOSS使用）
 * 创建人： yeshiyuan
 * 创建时间：2018/10/10 15:26
 * 修改人： yeshiyuan
 * 修改时间：2018/10/10 15:26
 * 修改描述：
 */
@ApiModel(description = "删除文案基础信息（BOSS使用）")
public class DelLangInfoBaseReq {

    @ApiModelProperty(name = "objectId", value = "对象ID", dataType = "Long")
    private Long objectId;

    @ApiModelProperty(name = "objectType", value = "对象类型", dataType = "String")
    private String objectType;

    @ApiModelProperty(name = "keys", value = "文案key", dataType = "List")
    private List<String> keys;

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

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public static void checkParam(DelLangInfoBaseReq req) {
        if (!LangInfoObjectTypeEnum.checkObjectType(req.getObjectType())) {
            throw new BusinessException(TenantExceptionEnum.LANGINFO_OBJECTTYPE_ERROR);
        }
        if ( req.getObjectType().equals(LangInfoObjectTypeEnum.appConfig.name()) && req.getObjectId().intValue()!=-1 ) {
            throw new BusinessException(TenantExceptionEnum.LANGINFO_OBJECTID_ERROR);
        }
        if (req.getKeys()== null || req.getKeys().isEmpty()) {
            throw new BusinessException(TenantExceptionEnum.LANGKEY_IS_NULL);
        }
    }
}
