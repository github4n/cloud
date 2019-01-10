package com.iot.device.vo.rsp.servicemodule;

import com.iot.device.vo.rsp.product.ParentVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
  * @despriction：功能模组事件信息
  * @author  yeshiyuan
  * @created 2018/11/22 17:38
  */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@ApiModel("功能模组事件信息")
public class EventResp implements Serializable {
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Long id;

    /**
     * 事件名称
     */
    @ApiModelProperty(value = "事件名称")
    private String name;
    /**
     * 唯一标识code
     */
    @ApiModelProperty(value = "唯一标识code")
    private String code;

    /**
     * json参数集
     */
    @ApiModelProperty(value = "json参数集")
    private String params;


    @ApiModelProperty(value = "事件属性")
    private List<PropertyResp> properties;

    @ApiModelProperty(value = "父id")
    private Long parentId;
}
