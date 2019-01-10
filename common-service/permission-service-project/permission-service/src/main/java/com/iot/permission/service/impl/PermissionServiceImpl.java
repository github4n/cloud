package com.iot.permission.service.impl;

import com.iot.permission.dao.PermissionMapper;
import com.iot.permission.entity.Permission;
import com.iot.permission.entity.Role;
import com.iot.permission.entity.RolePermissionRelate;
import com.iot.permission.entity.UserRoleRelate;
import com.iot.permission.mapper.UserRoleRelateMapper;
import com.iot.permission.service.PermissionService;
import com.iot.permission.vo.PermissionDto;
import com.iot.permission.vo.PermissionVo;
import com.iot.permission.vo.RoleReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：权限接口
 * 功能描述：权限接口
 * 创建人： 李帅
 * 创建时间：2018年7月3日 下午5:07:56
 * 修改人：李帅
 * 修改时间：2018年7月3日 下午5:07:56
 */
@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PermissionMapper permissionMapper;

	@Autowired
	private UserRoleRelateMapper userRoleRelateMapper;

	/**
	 * 
	 * 描述：获取角色信息
	 * @author 李帅
	 * @created 2018年7月12日 下午2:17:15
	 * @since 
	 * @param roleReqVo
	 */
	public List<Role> getRole(RoleReqDto roleReqVo){
		return permissionMapper.getRole(roleReqVo);
	}

	/**
	 * 
	 * 描述：查询默认角色
	 * @author 李帅
	 * @created 2018年7月12日 下午2:35:46
	 * @since 
	 * @param roleCode
	 * @param roleType
	 * @return
	 */
	public Role getDefaultRole(String roleCode, String roleType) {
		return permissionMapper.getDefaultRole(roleCode, roleType);
	}
	
	/**
	 * 
	 * 描述：保存用户角色关联关系
	 * @author 李帅
	 * @created 2018年7月12日 下午7:20:07
	 * @since 
	 * @param userRoleRelate
	 */
	public void saveUserRoleRelate(UserRoleRelate userRoleRelate) {
		permissionMapper.saveUserRoleRelate(userRoleRelate);
	}
	
	/**
	 * 
	 * 描述：获取用户权限
	 * @author 李帅
	 * @created 2018年7月12日 下午2:35:39
	 * @since 
	 * @param userId
	 * @return
	 */
	public List<Permission> getUserPermission(Long userId){
		return permissionMapper.getUserPermission(userId);
	}
	
	/**
	 * 
	 * 描述：获取角色权限
	 * @author 李帅
	 * @created 2018年7月12日 下午2:35:39
	 * @since 
	 * @param roleId
	 * @return
	 */
	public List<Permission> getRolePermission(Long roleId){
		return permissionMapper.getRolePermission(roleId);
	}
	
	/**
	 * 
	 * 描述：获取所有权限
	 * @author 李帅
	 * @created 2018年7月12日 下午2:35:39
	 * @since 
	 * @return
	 */
	public List<Permission> getAllPermission(){
		return permissionMapper.getAllPermission();
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
	public List<Role> getUserRoles(Long userId){
		return permissionMapper.getUserRoles(userId);
	}
	
	/**
	 * 
	 * 描述：获取用户权限URL
	 * @author 李帅
	 * @created 2018年7月12日 下午2:35:39
	 * @since 
	 * @param userId
	 * @return
	 */
	public List<String> getUserPermissionUrl(Long userId){
		return permissionMapper.getUserPermissionUrl(userId);
	}

	@Override
	public void deletePermissionByRoleId(Long roleId) {
		permissionMapper.deletePermissionByRoleId(roleId);
	}

	@Override
	public void saveRolePermissionRelate(RolePermissionRelate r) {
		permissionMapper.saveRolePermissionRelate(r);
	}

	@Override
	public void deleteUserRoleRelateByUserId(Long userId) {
		permissionMapper.deleteUserRoleRelateByUserId(userId);
	}

	@Override
	public List<Permission> getPermissionList(PermissionVo vo) {
		return permissionMapper.queryList(vo);
	}

	@Override
	public List<PermissionDto> getPermissionByRoleId(Long roleId) {
		return permissionMapper.getPermissionByRoleId(roleId);
	}
	
	@Override
	public void savePermission(Permission permission) {
		permissionMapper.savePermission(permission);
	}
	
	@Override
	public void editPermission(Permission permission) {
		permissionMapper.editPermission(permission);
	}
	
	@Override
	public void deletePermission(List<Long> permissionIds) {
		permissionMapper.deletePermission(permissionIds);
	}
	
	@Override
	public void deleteRolePermissionRelate(List<Long> permissionIds) {
		permissionMapper.deleteRolePermissionRelate(permissionIds);
	}

	@Override
	public int deleteUserRoleRelateById(List<Long> ids) {
		return userRoleRelateMapper.deleteUserRoleRelateById(ids);
	}

	@Override
	public List<UserRoleRelate> getUserRoleRelateByUserId(Long userId) {
		return userRoleRelateMapper.getUserRoleRelateByUserId(userId);
	}
}
