package com.iot.device.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DeviceCatalogReq {

    private Integer pageNum = 1;

    private Integer pageSize = 10;

    @ApiModelProperty(value = "一级分类id，新增不需要填写")
    private Long id;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;

    private Long tenantId;

    /**
     * 可以检索 分类名称、description
     */
    private String searchValues;

    private Long updateBy;
}
