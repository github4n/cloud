package com.iot.device.vo.rsp.servicemodule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
  * @despriction：功能组方法基础信息
  * @author  yeshiyuan
  * @created 2018/11/22 17:02
  */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@ApiModel("功能组方法基础信息")
public class ActionResp implements Serializable {

    @ApiModelProperty(value = "主键id")
    private Long id;
    /**
     * 方法名称
     */
    @ApiModelProperty(value = "方法名称")
    private String name;
    /**
     * 唯一标识code
     */
    @ApiModelProperty(value = "唯一标识code")
    private String code;

    @ApiModelProperty(value = "请求参数格式：0 array ,1 object")
    private Integer reqParamType;

    @ApiModelProperty(value = "返回参数格式：0 array ,1 object")
    private Integer returnType;

    @ApiModelProperty(value = "返回结果集")
    private String returns;

    @ApiModelProperty(value = "方法属性")
    private List<PropertyResp> properties;

    @ApiModelProperty(value = "父id")
    private Long parentId;

}
