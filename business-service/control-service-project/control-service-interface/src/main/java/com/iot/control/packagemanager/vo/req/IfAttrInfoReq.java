package com.iot.control.packagemanager.vo.req;

import com.iot.device.api.ServiceModuleApi;
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
public class IfAttrInfoReq extends AttrInfoBaseVo {

    @ApiModelProperty(name = "eventProperties", value = "事件中的属性详情", dataType = "List")
    private List<PropertyInfoVo> eventProperties;

    public IfAttrInfoReq() {
    }

    public IfAttrInfoReq(Long attrId, String attrType, String value, String compOp,List<PropertyInfoVo> eventProperties ) {
        super(attrId, attrType, value, compOp);
        this.eventProperties = eventProperties;
    }
}
