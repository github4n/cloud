package com.iot.device.vo.rsp.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "标准对象信息")
public class ParentVO {
    @ApiModelProperty(value = "标准对象id")
    private Long id;
    @ApiModelProperty(value = "标准对象编码")
    private String code;
    @ApiModelProperty(value = "标准对象名称")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
