package com.iot.device.vo.rsp.servicemodule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
  * @despriction：属性出参
  * @author  yeshiyuan
  * @created 2018/11/22 15:12
  */
@Data
@NoArgsConstructor
@ApiModel(description = "属性出参")
public class PropertyResp {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "属性名称")
    private String name;

    @ApiModelProperty(value = "唯一标识code")
    private String code;

    @ApiModelProperty(value = "请求参数格式：0 array ,1 object")
    private Integer reqParamType;

    @ApiModelProperty(value = "返回参数格式：0 array ,1 object")
    private Integer returnType;


    @ApiModelProperty(value = "参数类型，0:int,1:float,2:bool,3:enum,4:string")
    private Integer paramType;
    /**
     * 最小值
     */
    @ApiModelProperty(value = "最小值")
    private String minVal;
    /**
     * 最大值
     */
    @ApiModelProperty(value = "最大值")
    private String maxVal;
    /**
     * 允许值(enum 可以多个逗号隔开)
     */
    @ApiModelProperty(value = "允许值(enum 可以多个逗号隔开)")
    private String allowedValues;

    @ApiModelProperty(value = "父id")
    private Long parentId;


}
