package com.iot.portal.corpuser.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Set;

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
@ApiModel(value="RoleInitResp", description="角色信息")
public class RoleInitResp {

    @ApiModelProperty(value="allRoles",name="所有角色")
    private List<RoleResp> allRoles;

    @ApiModelProperty(value="roleids",name="当前用户拥有的角色id")
    private Set<Long> roleids;

    public RoleInitResp() {

    }

    public RoleInitResp(List<RoleResp> allRoles, Set<Long> roleids) {
        this.allRoles = allRoles;
        this.roleids = roleids;
    }

    public List<RoleResp> getAllRoles() {
        return allRoles;
    }

    public void setAllRoles(List<RoleResp> allRoles) {
        this.allRoles = allRoles;
    }

    public Set<Long> getRoleids() {
        return roleids;
    }

    public void setRoleids(Set<Long> roleids) {
        this.roleids = roleids;
    }

}