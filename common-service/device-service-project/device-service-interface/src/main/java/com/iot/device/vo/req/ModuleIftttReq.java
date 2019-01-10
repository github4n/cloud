package com.iot.device.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称：cloud
 * 功能描述：模组联动入参
 * 创建人： yeshiyuan
 * 创建时间：2018/10/23 17:13
 * 修改人： yeshiyuan
 * 修改时间：2018/10/23 17:13
 * 修改描述：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "模组联动入参")
public class ModuleIftttReq {

    @ApiModelProperty(name = "id", value = "id", dataType = "Long")
    private Long id;
    @ApiModelProperty(name = "ifData", value = "if数据（1：支持，0：不支持）", dataType = "Integer")
    private Integer ifData;
    @ApiModelProperty(name = "thenData", value = "then数据（1：支持，0：不支持）", dataType = "Integer")
    private Integer thenData;


}
