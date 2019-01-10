package com.iot.device.vo.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称：cloud
 * 功能描述：模组支持iftttt数据
 * 创建人： yeshiyuan
 * 创建时间：2018/10/23 13:46
 * 修改人： yeshiyuan
 * 修改时间：2018/10/23 13:46
 * 修改描述：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "模组支持iftttt数据")
public class ModuleIftttDataResp {

    @ApiModelProperty(name = "id", value = "id", dataType = "Long")
    private Long id;
    @ApiModelProperty(name = "name", value = "名字", dataType = "String")
    private String name;
    @ApiModelProperty(name = "flag", value = "选择标志（1：已选；0：未选）", dataType = "Integer")
    private Integer flag;
}
