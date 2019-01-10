package com.iot.control.packagemanager.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
  * @despriction：属性信息
  * @author  yeshiyuan
  * @created 2018/11/23 10:58
  */
@ApiModel(description = "then参数信息")
@Data
public class ThenAttrInfoReq extends AttrInfoBaseVo {

    @ApiModelProperty(name = "actionProperties", value = "方法中的属性详情", dataType = "List")
    private List<PropertyInfoVo> actionProperties;

    public ThenAttrInfoReq() {

    }

    public ThenAttrInfoReq(Long attrId, String attrType, String value, String compOp, List<PropertyInfoVo> actionProperties) {
        super(attrId, attrType, value, compOp);
        this.actionProperties = actionProperties;
    }
}
