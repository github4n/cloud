package com.iot.portal.web.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：租户文案导出入参（portal使用）
 * 创建人： yeshiyuan
 * 创建时间：2018/10/11 18:57
 * 修改人： yeshiyuan
 * 修改时间：2018/10/11 18:57
 * 修改描述：
 */
@ApiModel(description = "租户文案导出入参（portal使用）")
public class LangInfoExportReq {
    @ApiModelProperty(name = "objectId", value = "对象ID", dataType = "Long")
    private String objectId;

    @ApiModelProperty(name = "objectType", value = "对象类型", dataType = "String")
    private String objectType;

    @ApiModelProperty(name = "code", value = "验证码", dataType = "String")
    private String code;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
