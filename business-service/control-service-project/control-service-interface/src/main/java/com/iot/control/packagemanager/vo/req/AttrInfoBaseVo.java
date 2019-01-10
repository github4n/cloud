package com.iot.control.packagemanager.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
  * @despriction：attrInfo共用字段
  * @author  yeshiyuan
  * @created 2018/11/23 11:01
  */
@Data
@ApiModel(description = "attrInfo共用字段")
public class AttrInfoBaseVo {

    @ApiModelProperty(name = "attrId", value = "方法/属性/事件id,通过attrType找到对应的属性详情（service_module_action/event/property）", dataType = "Long")
    private Long attrId;
    @ApiModelProperty(name = "attrType", value = "类型：action:方法,property:属性,event:事件 ", dataType = "String")
    private String attrType;
    @ApiModelProperty(name = "value", value = "值", dataType = "String")
    private String value;
    @ApiModelProperty(name = "compOp", value = "条件判断符", dataType = "String")
    private String compOp;

    public AttrInfoBaseVo() {
    }

    public AttrInfoBaseVo(Long attrId, String attrType, String value, String compOp) {
        this.attrId = attrId;
        this.attrType = attrType;
        this.value = value;
        this.compOp = compOp;
    }
}
