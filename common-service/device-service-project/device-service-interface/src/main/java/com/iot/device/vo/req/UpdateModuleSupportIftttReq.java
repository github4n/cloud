package com.iot.device.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：管理模组联动入参
 * 创建人： yeshiyuan
 * 创建时间：2018/10/23 16:46
 * 修改人： yeshiyuan
 * 修改时间：2018/10/23 16:46
 * 修改描述：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "管理模组联动入参")
public class UpdateModuleSupportIftttReq {

    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "Long")
    private Long tenantId;
    @ApiModelProperty(name = "userId", value = "用户id", dataType = "Long")
    private Long userId;
    @ApiModelProperty(name = "serviceModuleIds", value = "模组ids", dataType = "List")
    private List<Long> serviceModuleIds;
    @ApiModelProperty(name = "list", value = "联动列表", dataType = "List")
    private List<ModuleIftttReq> list;

}
