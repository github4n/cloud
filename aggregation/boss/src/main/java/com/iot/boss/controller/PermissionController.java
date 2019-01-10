package com.iot.boss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.boss.service.permission.PermissionService;
import com.iot.boss.vo.permission.AssignPermissionVo;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.permission.vo.PermissionVo;
import com.iot.permission.vo.RoleReqDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(description = "权限相关功能",value = "权限相关功能")
@RequestMapping("api/permission")
public class PermissionController {

	@Autowired
	private PermissionService permissionService;

    @ApiOperation(value = "获取角色信息", notes = "获取角色信息")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "getRole", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getRole(@RequestBody RoleReqDto roleReqDto) {
        return ResultMsg.SUCCESS.info(this.permissionService.getRole(roleReqDto));
    }
    
    @ApiOperation(value = "BOSS拥有者分配其他角色功能权限", notes = "BOSS拥有者分配其他角色功能权限")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "bossOwnerAssignPermissionToOtherRole", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse bossOwnerAssignPermissionToOtherRole(@RequestBody AssignPermissionVo assignPermissionVo) {
    	this.permissionService.bossOwnerAssignPermissionToOtherRole(assignPermissionVo);
        return ResultMsg.SUCCESS.info();
    }
    
    /**
	 * 
	 * 描述：获取所有权限
	 * @author 李帅
	 * @created 2018年7月12日 下午7:44:16
	 * @since 
	 * @return
	 */
	@ApiOperation(value = "获取所有权限", notes = "获取所有权限")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "/getAllPermissionTree", method = RequestMethod.GET)
	public CommonResponse getAllPermissionTree() {
		return ResultMsg.SUCCESS.info(this.permissionService.getAllPermissionTree());
	}
	
	@ApiOperation(value = "获取角色权限", notes = "获取角色权限")
	@LoginRequired(value = Action.Normal)
	@ApiImplicitParam(name = "roleId", value = "用户ID", required = true, paramType = "query", dataType = "Integer")
	@RequestMapping(value = "/getRolePermissionTree", method = RequestMethod.GET)
	CommonResponse getRolePermissionTree(@RequestParam("roleId") Long roleId) {
		return ResultMsg.SUCCESS.info(this.permissionService.getRolePermissionTree(roleId));
	}
	
	@ApiOperation(value = "新增权限", notes = "新增权限")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "/savePermission", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public CommonResponse savePermission(@RequestBody PermissionVo permissionVo) {
		this.permissionService.savePermission(permissionVo);
        return ResultMsg.SUCCESS.info();
	}
	
	@ApiOperation(value = "修改权限", notes = "修改权限")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "/editPermission", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public CommonResponse editPermission(@RequestBody  PermissionVo permissionVo) {
		this.permissionService.editPermission(permissionVo);
        return ResultMsg.SUCCESS.info();
	}
	
	@ApiOperation(value = "删除权限", notes = "删除权限")
	@LoginRequired(value = Action.Normal)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "permissionId", value = "权限ID", required = true, paramType = "query", dataType = "String")})
	@RequestMapping(value = "/deletePermission", method=RequestMethod.GET)
	public CommonResponse deletePermission(@RequestParam("permissionId") String permissionId) {
		this.permissionService.deletePermission(permissionId);
        return ResultMsg.SUCCESS.info();
	}
}
