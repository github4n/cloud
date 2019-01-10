package com.iot.permission.service;

import java.util.List;

import com.iot.permission.entity.Permission;
import com.iot.permission.entity.Role;
import com.iot.permission.entity.RolePermissionRelate;
import com.iot.permission.entity.UserRoleRelate;
import com.iot.permission.vo.PermissionDto;
import com.iot.permission.vo.PermissionVo;
import com.iot.permission.vo.RoleReqDto;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：权限接口
 * 功能描述：权限接口
 * 创建人： 李帅
 * 创建时间：2018年7月3日 上午11:19:19
 * 修改人：李帅
 * 修改时间：2018年7月3日 上午11:19:19
 */
public interface PermissionService {

	/**
	 * 
	 * 描述：获取角色信息
	 * @author 李帅
	 * @created 2018年7月12日 下午2:17:15
	 * @since 
	 * @param roleReqVo
	 */
	public List<Role> getRole(RoleReqDto roleReqVo);
	
	/**
	 * 
	 * 描述：查询默认角色
	 * @author 李帅
	 * @created 2018年7月12日 下午2:35:39
	 * @since 
	 * @param roleCode
	 * @param roleType
	 * @return
	 */
	public Role getDefaultRole(String roleCode, String roleType);
	
	/**
	 * 
	 * 描述：保存用户角色关联关系
	 * @author 李帅
	 * @created 2018年7月12日 下午7:20:07
	 * @since 
	 * @param userRoleRelate
	 */
	public void saveUserRoleRelate(UserRoleRelate userRoleRelate);
	
	/**
	 * 
	 * 描述：获取用户权限
	 * @author 李帅
	 * @created 2018年7月12日 下午2:35:39
	 * @since 
	 * @param userId
	 * @return
	 */
	public List<Permission> getUserPermission(Long userId);
	
	/**
	 * 
	 * 描述：获取角色权限
	 * @author 李帅
	 * @created 2018年7月12日 下午2:35:39
	 * @since 
	 * @param roleId
	 * @return
	 */
	public List<Permission> getRolePermission(Long roleId);
	
	/**
	 * 
	 * 描述：获取所有权限
	 * @author 李帅
	 * @created 2018年7月12日 下午2:35:39
	 * @since 
	 * @return
	 */
	public List<Permission> getAllPermission();
	
	/**
	 * 
	 * 描述：获取用户角色信息
	 * @author 李帅
	 * @created 2018年7月12日 下午7:44:16
	 * @since 
	 * @param userId
	 * @return
	 */
	public List<Role> getUserRoles(Long userId);
	
	/**
	 * 
	 * 描述：获取用户权限URL
	 * @author 李帅
	 * @created 2018年7月12日 下午2:35:39
	 * @since 
	 * @param userId
	 * @return
	 */
	public List<String> getUserPermissionUrl(Long userId);

	void deletePermissionByRoleId(Long roleId);

	void saveRolePermissionRelate(RolePermissionRelate r);

	void deleteUserRoleRelateByUserId(Long userId);

    List<Permission> getPermissionList(PermissionVo vo);

	List<PermissionDto> getPermissionByRoleId(Long roleId);
	
	void savePermission(Permission permission);
	
	void editPermission(Permission permission);
	
	void deletePermission(List<Long> permissionIds);
	
	void deleteRolePermissionRelate(List<Long> permissionIds);

	/**
	 * 描述：删除子账户时，删除用户关联的角色
	 * @author wucheng
	 * @date 2018-11-06 11:15:13
	 * @param ids 用户绑定的角色id集合List<Long> ids
	 * @return
	 */
	int deleteUserRoleRelateById(List<Long> ids);

	/**
	 * 描述：根据用户id获取当前用户关联的角色信息
	 * @author wucheng
	 * @date 2018-11-06 11:15:13
	 * @param userId 用户id
	 * @return
	 */
	List<UserRoleRelate> getUserRoleRelateByUserId(Long userId);
}