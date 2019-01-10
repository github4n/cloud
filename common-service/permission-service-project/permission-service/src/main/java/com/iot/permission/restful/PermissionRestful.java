package com.iot.permission.restful;

import com.iot.common.beans.CommonResponse;
import com.iot.permission.api.PermissionApi;
import com.iot.permission.entity.UserRoleRelate;
import com.iot.permission.manager.PermissionManager;
import com.iot.permission.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：权限接口
 * 功能描述：权限接口
 * 创建人： 李帅
 * 创建时间：2018年7月3日 上午11:19:34
 * 修改人：李帅
 * 修改时间：2018年7月3日 上午11:19:34
 */
@RestController
public class PermissionRestful implements PermissionApi {

	@Autowired
	private PermissionManager permissionManager;

	/**
	 * 
	 * 描述：获取角色信息
	 * @author 李帅
	 * @created 2018年7月12日 下午2:17:15
	 * @since 
	 * @param roleReqVo
	 */
	public List<RoleDto> getRole(@RequestBody RoleReqDto roleReqVo){
		return permissionManager.getRole(roleReqVo);
	}

	/**
	 * 描述：依据角色类别获取角色信息
	 * @author maochengyuan
	 * @created 2018/7/14 11:42
	 * @param roleType 角色类型
	 * @return java.util.List<com.iot.permission.vo.RoleDto>
	 */
	@Override
	public List<RoleDto> getRoleByRoleType(@RequestParam("roleType") String roleType) {
		return this.permissionManager.getRoleByRoleType(roleType);
	}

	/**
	 * 
	 * 描述：同一用户绑定多名角色
	 * @author 李帅
	 * @created 2018年7月12日 下午2:18:00
	 * @since 
	 * @param userToRoleVo
	 */
	public void addUserToRoles(@RequestBody UserToRolesDto userToRoleVo) {
		permissionManager.addUserToRoles(userToRoleVo);
	}
	
	/**
	 * 
	 * 描述：多名用户绑定同一角色
	 * @author 李帅
	 * @created 2018年7月12日 下午2:18:00
	 * @since 
	 * @param userToRoleVo
	 */
	public void addUsersToRole(@RequestBody UsersToRoleDto userToRoleVo) {
		permissionManager.addUsersToRole(userToRoleVo);
	}
	
	/**
	 * 
	 * 描述：获取用户权限
	 * @author 李帅
	 * @created 2018年7月12日 下午7:44:27
	 * @since 
	 * @param userId
	 * @return
	 */
	public List<PermissionDto> getUserPermissionTree(@RequestParam("userId") Long userId) {
		return permissionManager.getUserPermissionTree(userId);
	}
	
	/**
	 * 
	 * 描述：获取角色权限
	 * @author 李帅
	 * @created 2018年7月12日 下午7:44:27
	 * @since 
	 * @param roleId
	 * @return
	 */
	public List<PermissionDto> getRolePermissionTree(@RequestParam("roleId") Long roleId) {
		return permissionManager.getRolePermissionTree(roleId);
	}
	
	/**
	 * 
	 * 描述：获取所有权限
	 * @author 李帅
	 * @created 2018年7月12日 下午7:44:27
	 * @since 
	 * @param userId
	 * @return
	 */
	public List<PermissionDto> getAllPermissionTree() {
		return permissionManager.getAllPermissionTree();
	}
	
	/**
	 * 
	 * 描述：获取用户角色信息
	 * @author 李帅
	 * @created 2018年7月12日 下午7:44:16
	 * @since 
	 * @param userId
	 * @return
	 */
	public List<RoleDto> getUserRoles(@RequestParam("userId") Long userId){
		return permissionManager.getUserRoles(userId);
	}

	/**
	 * 描述：获取用户角色信息
	 * @author nongchongwei
	 * @date 2018/11/6 14:22
	 * @param
	 * @return
	 */
	@Override
	public Map<Long, List<RoleDto>> getBathUserRoles(@RequestParam("userIdList")List<Long> userIdList) {
		Map<Long, List<RoleDto>> retMap = new HashMap<>();
		for(Long userId : userIdList){
			List<RoleDto> temp = permissionManager.getUserRoles(userId);
			retMap.put(userId,temp);
		}
		return retMap;
	}

	/**
	 * 
	 * 描述：校验用户权限
	 * @author 李帅
	 * @created 2018年7月12日 下午7:54:46
	 * @since 
	 * @param userToPermissionVo
	 * @return
	 */
	public Map<String, Boolean> verifyUserPermission(@RequestBody UserToPermissionDto userToPermissionVo) {
		return permissionManager.verifyUserPermission(userToPermissionVo);
	}

	@Override
	public CommonResponse addRole(@RequestBody RoleDto dto) {
		return permissionManager.addRole(dto);
	}

	@Override
	public CommonResponse editRole(@RequestBody RoleDto dto) {
		return permissionManager.editRole(dto);
	}

	@Override
	public CommonResponse deleteRole(@RequestParam("id") Long id) {
		return permissionManager.deleteRole(id);
	}

	@Override
	public CommonResponse assignPermissionToRole(@RequestParam("roleId") Long roleId,
												 @RequestParam("permissionIds") Long[] permissionIds) {
		return permissionManager.assignPermissionToRole(roleId, permissionIds);
	}

	@Override
	public List<PermissionDto> getPermissionByRoleId(@RequestParam("roleId") Long roleId) {
		return permissionManager.getPermissionByRoleId(roleId);
	}

	@Override
	public List<PermissionDto> getPermissionList(@RequestBody PermissionVo permissionVo) {
		return permissionManager.getPermissionList(permissionVo);
	}

	@Override
	public List<PermissionDto> getFunctionPermission(@RequestParam("userId") Long userId) {
		return permissionManager.getFunctionPermission(userId);
	}

	public PermissionManager getPermissionManager() {
		return permissionManager;
	}

	public void setPermissionManager(PermissionManager permissionManager) {
		this.permissionManager = permissionManager;
	}
	
	/**
	 * 
	 * 描述：新增权限
	 * @author 李帅
	 * @created 2018年10月16日 上午11:15:48
	 * @since 
	 * @param permissionVo
	 */
	public void savePermission(@RequestBody PermissionVo permissionVo) {
		permissionManager.savePermission(permissionVo);
	}
	
	/**
	 * 
	 * 描述：修改权限
	 * @author 李帅
	 * @created 2018年10月16日 上午11:16:21
	 * @since 
	 * @param permissionVo
	 */
	public void editPermission(@RequestBody  PermissionVo permissionVo) {
		permissionManager.editPermission(permissionVo);
	}
	
	/**
	 * 
	 * 描述：删除权限
	 * @author 李帅
	 * @created 2018年10月16日 上午11:16:43
	 * @since 
	 * @param permissionId
	 * @param userId
	 */
	public void deletePermission(@RequestParam("permissionId") String permissionId, @RequestParam("userId") Long userId) {
		permissionManager.deletePermission(permissionId, userId);
	}
	
	/**
	 * 
	 * 描述：BOSS拥有者分配其他角色功能权限
	 * @author 李帅
	 * @created 2018年10月16日 上午11:15:00
	 */
	@Override
	public void bossOwnerAssignPermissionToOtherRole(@RequestBody AssignPermissionDto assignPermissionDto) {
		permissionManager.bossOwnerAssignPermissionToOtherRole(assignPermissionDto);
	}

	@Override
	public void deleteUserRoleRelateByUserId(@RequestParam("userId") Long userId) {
		permissionManager.deleteUserRoleRelateByUserId(userId);
	}

	@Override
	public void saveUserRoleRelate(@RequestBody UserRoleRelate userRoleRelate) {
		permissionManager.saveUserRoleRelate(userRoleRelate);
	}

	/**
	 * 描述：删除子账户时，删除用户关联的角色
	 * @author wucheng
	 * @date 2018-11-06 11:15:13
	 * @param ids 用户绑定的角色id集合List<Long> ids
	 * @return
	 */
	@Override
	public int deleteUserRoleRelateById(Long[] ids) {
		List<Long> idsList = Arrays.asList(ids);
		return permissionManager.deleteUserRoleRelateByRoleId(idsList);
	}
	/**
	 * 描述：根据用户id获取当前子账户关联的角色信息
	 * @author wucheng
	 * @date 2018-11-06 11:15:13
	 * @param userId 用户id
	 * @return
	 */
	@Override
	public List<UserRoleRelate> getUserRoleRelateByUserId(Long userId) {
		return permissionManager.getUserRoleRelateByUserId(userId);
	}
}