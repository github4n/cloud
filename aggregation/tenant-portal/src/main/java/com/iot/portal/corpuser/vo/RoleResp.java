package com.iot.portal.corpuser.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：2b聚合层
 * 功能描述：角色VO
 * 创建人： maochengyuan
 * 创建时间：2018/7/14 14:23
 * 修改人： maochengyuan
 * 修改时间：2018/7/14 14:23
 * 修改描述：
 */
@ApiModel(value="RoleResp", description="角色信息")
public class RoleResp {

    @ApiModelProperty(value="id",name="角色id")
    private Long id;

    @ApiModelProperty(value="roleName",name="角色名称")
    private String roleName;

    public RoleResp(Long id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

}