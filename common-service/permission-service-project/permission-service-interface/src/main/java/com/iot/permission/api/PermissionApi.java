package com.iot.permission.api;

import java.util.List;
import java.util.Map;

import com.iot.permission.entity.UserRoleRelate;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iot.common.beans.CommonResponse;
import com.iot.permission.vo.AssignPermissionDto;
import com.iot.permission.vo.PermissionDto;
import com.iot.permission.vo.PermissionVo;
import com.iot.permission.vo.RoleDto;
import com.iot.permission.vo.RoleReqDto;
import com.iot.permission.vo.UserToPermissionDto;
import com.iot.permission.vo.UserToRolesDto;
import com.iot.permission.vo.UsersToRoleDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "权限接口")
@FeignClient(value = "permission-service")
@RequestMapping("/api/permission")
public interface PermissionApi {

	/**
	 * 描述：获取角色信息
	 *
	 * @param roleReqVo
	 * @author 李帅
	 * @created 2018年7月12日 下午2:17:15
	 * @since
	 */
	@ApiOperation(value = "获取角色信息", notes = "获取角色信息", response = RoleDto.class)
	@RequestMapping(value = "/getRole", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	List<RoleDto> getRole(@RequestBody RoleReqDto roleReqVo);

	/**
	 * 描述：依据角色类别获取角色信息
	 *
	 * @param roleType
	 * @return java.util.List<com.iot.permission.vo.RoleDto>
	 * @author maochengyuan
	 * @created 2018/7/14 11:41
	 */
	@ApiOperation(value = "依据角色类别获取角色信息", notes = "依据角色类别获取角色信息", response = RoleDto.class)
	@ApiImplicitParam(name = "roleType", value = "用户类型", required = true, paramType = "query", dataType = "String")
	@RequestMapping(value = "/getRoleByRoleType", method = RequestMethod.GET)
	List<RoleDto> getRoleByRoleType(@RequestParam("roleType") String roleType);

	/**
	 * 描述：同一用户绑定多名角色
	 *
	 * @param userToRoleVo
	 * @author 李帅
	 * @created 2018年7月12日 下午2:17:15
	 * @since
	 */
	@ApiOperation(value = "同一用户绑定多名角色", notes = "同一用户绑定多名角色", response = UserToRolesDto.class)
	@RequestMapping(value = "/addUserToRoles", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	void addUserToRoles(@RequestBody UserToRolesDto userToRoleVo);

	/**
	 * 描述：多名用户绑定同一角色
	 *
	 * @param userToRoleVo
	 * @author 李帅
	 * @created 2018年7月12日 下午2:17:15
	 * @since
	 */
	@ApiOperation(value = "多名用户绑定同一角色", notes = "多名用户绑定同一角色", response = UsersToRoleDto.class)
	@RequestMapping(value = "/addUsersToRole", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	void addUsersToRole(@RequestBody UsersToRoleDto userToRoleVo);

	/**
	 * 描述：获取用户权限
	 *
	 * @param userId
	 * @return
	 * @author 李帅
	 * @created 2018年7月12日 下午7:44:16
	 * @since
	 */
	@ApiOperation(value = "获取用户权限", notes = "获取用户权限", response = PermissionDto.class)
	@ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "query", dataType = "Integer")
	@RequestMapping(value = "/getUserPermissionTree", method = RequestMethod.GET)
	List<PermissionDto> getUserPermissionTree(@RequestParam("userId") Long userId);

	/**
	 * 描述：获取角色权限
	 *
	 * @param roleId
	 * @return
	 * @author 李帅
	 * @created 2018年7月12日 下午7:44:16
	 * @since
	 */
	@ApiOperation(value = "获取角色权限", notes = "获取角色权限", response = PermissionDto.class)
	@ApiImplicitParam(name = "roleId", value = "用户ID", required = true, paramType = "query", dataType = "Integer")
	@RequestMapping(value = "/getRolePermissionTree", method = RequestMethod.GET)
	List<PermissionDto> getRolePermissionTree(@RequestParam("roleId") Long roleId);

	/**
	 * 描述：获取所有权限
	 *
	 * @return
	 * @author 李帅
	 * @created 2018年7月12日 下午7:44:16
	 * @since
	 */
	@ApiOperation(value = "获取所有权限", notes = "获取所有权限", response = PermissionDto.class)
//	@ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "query", dataType = "Integer")
	@RequestMapping(value = "/getAllPermissionTree", method = RequestMethod.GET)
	List<PermissionDto> getAllPermissionTree();

	/**
	 * 描述：获取用户角色信息
	 *
	 * @param userId
	 * @return
	 * @author 李帅
	 * @created 2018年7月12日 下午7:44:16
	 * @since
	 */
	@ApiOperation(value = "获取用户角色信息", notes = "获取用户角色信息", response = RoleDto.class)
	@ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "query", dataType = "Integer")
	@RequestMapping(value = "/getUserRoles", method = RequestMethod.GET)
	List<RoleDto> getUserRoles(@RequestParam("userId") Long userId);

	/**
	 * 描述：获取用户角色信息
	 *
	 * @param
	 * @return
	 * @author nongchongwei
	 * @date 2018/11/6 14:17
	 */
	@ApiOperation(value = "获取用户角色信息", notes = "获取用户角色信息", response = RoleDto.class)
	@ApiImplicitParam(name = "userIdList", value = "用户ID", required = true, paramType = "query", dataType = "Long")
	@RequestMapping(value = "/getBathUserRoles", method = RequestMethod.GET)
	Map<Long, List<RoleDto>> getBathUserRoles(@RequestParam("userIdList") List<Long> userIdList);

	/**
	 * 描述：校验用户权限
	 *
	 * @param userToPermissionVo
	 * @return
	 * @author 李帅
	 * @created 2018年7月12日 下午7:54:38
	 * @since
	 */
	@ApiOperation(value = "校验用户权限", notes = "校验用户权限", response = UserToPermissionDto.class)
	@RequestMapping(value = "/verifyUserPermission", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	Map<String, Boolean> verifyUserPermission(@RequestBody UserToPermissionDto userToPermissionVo);

	@ApiOperation(value = "新增角色", notes = "新增角色", response = RoleDto.class)
	@RequestMapping(value = "/addRole", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	CommonResponse addRole(@RequestBody RoleDto role);

	@ApiOperation(value = "修改角色", notes = "修改角色", response = RoleDto.class)
	@RequestMapping(value = "/editRole", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	CommonResponse editRole(@RequestBody RoleDto role);

	@ApiOperation(value = "删除自定义角色", notes = "删除自定义角色")
	@RequestMapping(value = "/deleteRole", method = RequestMethod.POST)
	CommonResponse deleteRole(@RequestParam("id") Long id);

	@ApiOperation(value = "分配角色功能权限", notes = "分配角色功能权限")
	@RequestMapping(value = "/assignPermissionToRole", method = RequestMethod.POST)
	CommonResponse assignPermissionToRole(@RequestParam("roleId") Long roleId, @RequestParam("permissionIds") Long[] permissionIds);

	@ApiOperation(value = "获取权限数据列表", notes = "获取权限数据列表")
	@RequestMapping(value = "/getPermissionList", method = RequestMethod.POST)
	List<PermissionDto> getPermissionList(@RequestBody PermissionVo permissionVo);

	@ApiOperation(value = "获取功能权限列表", notes = "获取功能权限列表")
	@RequestMapping(value = "/getFunctionPermission", method = RequestMethod.POST)
	List<PermissionDto> getFunctionPermission(@RequestParam("userId") Long userId);

	@ApiOperation(value = "根据角色ID获取功能权限", notes = "根据角色ID获取功能权限")
	@RequestMapping(value = "/getPermissionByRoleId", method = RequestMethod.POST)
	List<PermissionDto> getPermissionByRoleId(@RequestParam("roleId") Long roleId);

	@ApiOperation(value = "新增权限", notes = "新增权限", response = PermissionVo.class)
	@RequestMapping(value = "/savePermission", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	void savePermission(@RequestBody PermissionVo permissionVo);

	@ApiOperation(value = "修改权限", notes = "修改权限", response = PermissionVo.class)
	@RequestMapping(value = "/editPermission", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	void editPermission(@RequestBody PermissionVo permissionVo);

	@ApiOperation(value = "删除权限", notes = "删除权限")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "permissionId", value = "权限ID", required = true, paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "query", dataType = "Integer")})
	@RequestMapping(value = "/deletePermission", method = RequestMethod.GET)
	void deletePermission(@RequestParam("permissionId") String permissionId, @RequestParam("userId") Long userId);

	@ApiOperation(value = "BOSS拥有者分配其他角色功能权限", notes = "BOSS拥有者分配其他角色功能权限")
	@RequestMapping(value = "/bossOwnerAssignPermissionToOtherRole", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	void bossOwnerAssignPermissionToOtherRole(@RequestBody AssignPermissionDto assignPermissionDto);

	@RequestMapping(value = "/deleteUserRoleRelateByUserId", method = RequestMethod.POST)
	void deleteUserRoleRelateByUserId(@RequestParam("userId") Long userId);

	@RequestMapping(value = "/saveUserRoleRelate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	void saveUserRoleRelate(@RequestBody UserRoleRelate userRoleRelate);

	@ApiOperation(value = "删除用户子账户时，删除用户绑定的角色", notes = "删除用户子账户时，删除用户绑定的角色")
	@RequestMapping(value = "/deleteUserRoleRelateById", method = RequestMethod.POST)
	int deleteUserRoleRelateById(@RequestParam("ids") Long[] ids);

	@ApiOperation(value = "根据userId获取当前用户关联的角色", notes = "根据userId获取当前用户关联的角色")
	@RequestMapping(value = "/getUserRoleRelateByUserId", method = RequestMethod.POST)
	List<UserRoleRelate> getUserRoleRelateByUserId(@RequestParam("userId") Long userId);
}
