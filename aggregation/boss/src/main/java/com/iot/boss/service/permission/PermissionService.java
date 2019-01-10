package com.iot.boss.service.permission;

import java.util.List;

import com.iot.boss.vo.permission.AssignPermissionVo;
import com.iot.permission.vo.PermissionDto;
import com.iot.permission.vo.PermissionVo;
import com.iot.permission.vo.RoleDto;
import com.iot.permission.vo.RoleReqDto;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：权限相关功能
 * 功能描述：权限相关功能
 * 创建人： 李帅
 * 创建时间：2018年10月15日 上午10:02:08
 * 修改人：李帅
 * 修改时间：2018年10月15日 上午10:02:08
 */
public interface PermissionService {

	/**
	 * 
	 * 描述：获取角色信息
	 * @author 李帅
	 * @created 2018年10月15日 下午8:58:39
	 * @since 
	 * @param roleReqDto
	 * @return
	 */
	List<RoleDto> getRole(RoleReqDto roleReqDto);
	
	/**
	 * 
	 * 描述：BOSS拥有者分配其他角色功能权限
	 * @author 李帅
	 * @created 2018年10月16日 上午11:23:32
	 * @since 
	 * @param userId
	 * @param permissionIds
	 */
	void bossOwnerAssignPermissionToOtherRole(AssignPermissionVo assignPermissionVo);
	
	/**
	 * 
	 * 描述：获取所有权限
	 * @author 李帅
	 * @created 2018年10月16日 上午11:54:23
	 * @since 
	 * @return
	 */
	List<PermissionDto> getAllPermissionTree();
	
	/**
	 * 
	 * 描述：获取角色权限
	 * @author 李帅
	 * @created 2018年10月17日 下午4:33:42
	 * @since 
	 * @param roleId
	 * @return
	 */
	List<PermissionDto> getRolePermissionTree(Long roleId);
	
	/**
	 * 
	 * 描述：新增权限
	 * @author 李帅
	 * @created 2018年10月16日 下午1:46:21
	 * @since 
	 * @param permissionVo
	 */
	void savePermission(PermissionVo permissionVo);
	
	/**
	 * 
	 * 描述：修改权限
	 * @author 李帅
	 * @created 2018年10月16日 下午1:46:42
	 * @since 
	 * @param permissionVo
	 */
	void editPermission(PermissionVo permissionVo);
	
	/**
	 * 
	 * 描述：删除权限
	 * @author 李帅
	 * @created 2018年10月16日 下午1:47:04
	 * @since 
	 * @param permissionId
	 */
	void deletePermission(String permissionId);
}
