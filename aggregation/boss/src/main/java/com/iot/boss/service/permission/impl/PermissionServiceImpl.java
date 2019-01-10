package com.iot.boss.service.permission.impl;

import com.iot.boss.service.permission.PermissionService;
import com.iot.boss.vo.permission.AssignPermissionVo;
import com.iot.permission.api.PermissionApi;
import com.iot.permission.vo.*;
import com.iot.saas.SaaSContextHolder;
import com.iot.system.api.LangApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：权限相关功能
 * 功能描述：权限相关功能
 * 创建人： 李帅
 * 创建时间：2018年10月15日 上午10:02:15
 * 修改人：李帅
 * 修改时间：2018年10月15日 上午10:02:15
 */
@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionApi permissionApi;
    @Autowired
	private LangApi langApi;
    
    /**
     * 
     * 描述：获取角色信息
     * @author 李帅
     * @created 2018年10月15日 下午8:58:46
     * @since 
     * @param roleReqVo
     * @return
     */
    @Override
    public List<RoleDto> getRole(RoleReqDto roleReqVo) {
    	return permissionApi.getRole(roleReqVo);
    }
    
    /**
     * 
     * 描述：BOSS拥有者分配其他角色功能权限
     * @author 李帅
     * @created 2018年10月16日 上午11:23:26
     * @since 
     * @param userId
     * @param permissionIds
     */
    @Override
	public void bossOwnerAssignPermissionToOtherRole(AssignPermissionVo assignPermissionVo) {
		// 在Header中获取数据
		Long userId = SaaSContextHolder.getCurrentUserId();
		// Long tenantId = SaaSContextHolder.currentTenantId();
		AssignPermissionDto assignPermissionDto = new AssignPermissionDto();
		assignPermissionDto.setRoleId(assignPermissionVo.getRoleId());
		assignPermissionDto.setPermissionIds(assignPermissionVo.getPermissionIds());
		assignPermissionDto.setUserId(userId);
		permissionApi.bossOwnerAssignPermissionToOtherRole(assignPermissionDto);
	}

	/**
	 *@description 递归获取所有的permissionName
	 *@author wucheng
	 *@params [dtos, permissionNameTrees]
	 *@create 2018/12/20 16:48
	 *@return java.util.Set<java.lang.String>
	 */
	private  Set<String> getPermissionNameTrees(List<PermissionDto> dtos, Set<String> permissionNameTrees) {
    	if (permissionNameTrees == null) {
			permissionNameTrees = new TreeSet<>();
		}
		List<PermissionDto> childrens = new ArrayList<>();
		for (PermissionDto p1 : dtos) {
			permissionNameTrees.add(p1.getPermissionName());
			List<PermissionDto> nextChildrens = p1.getChilds();
			if (nextChildrens != null) {
				childrens.addAll(nextChildrens);
			}
		}
		if (childrens != null && childrens.size() > 0) {
			getPermissionNameTrees(childrens, permissionNameTrees);
		}
        return permissionNameTrees;
	}
	/**
	 *@description 递归翻译每一个PermissionName
	 *@author wucheng
	 *@params [dtos, langMap]
	 *@create 2018/12/20 16:49
	 *@return void
	 */
	private  void setPermissionNameTreess(List<PermissionDto> dtos, Map<String, String> langMap) {
		List<PermissionDto> saveChildrens = new ArrayList<>();
		for (PermissionDto p1 : dtos) {
			p1.setPermissionName(langMap.get(p1.getPermissionName()));
			List<PermissionDto> nextChildrens = p1.getChilds();
			if (nextChildrens != null && nextChildrens.size() > 0) {
				saveChildrens.addAll(nextChildrens);
			}
		}
		if (saveChildrens != null && saveChildrens.size() > 0) {
			setPermissionNameTreess(saveChildrens, langMap);
		}
	}
    /**
     * 
     * 描述：获取所有权限
     * @author 李帅
     * @created 2018年10月16日 上午11:54:30
     * @since 
     * @return
     */
    @Override
    public List<PermissionDto> getAllPermissionTree() {
    	Set<String> permissionNameTrees = null;
		List<PermissionDto>  results = permissionApi.getAllPermissionTree();
		if (results != null && results.size() > 0) {
			permissionNameTrees = getPermissionNameTrees(results, permissionNameTrees);
			if (permissionNameTrees != null && permissionNameTrees.size() > 0) {
				Map<String, String> map = langApi.getLangValueByKey(permissionNameTrees, LocaleContextHolder.getLocale().toString());
				if(map != null && !map.isEmpty()) {
					setPermissionNameTreess(results, map);
				}
			}
		}
    	return results;
    }
    
    /**
	 * 
	 * 描述：获取角色权限
	 * @author 李帅
	 * @created 2018年10月17日 下午4:33:42
	 * @since 
	 * @param roleId
	 * @return
	 */
    @Override
    public List<PermissionDto> getRolePermissionTree(Long roleId){
		Set<String> permissionNameTrees = null;
		List<PermissionDto>  results = permissionApi.getRolePermissionTree(roleId);
		if (results != null && results.size() > 0) {
			permissionNameTrees = getPermissionNameTrees(results, permissionNameTrees);
			if (permissionNameTrees != null && permissionNameTrees.size() > 0) {
				Map<String, String> map = langApi.getLangValueByKey(permissionNameTrees, LocaleContextHolder.getLocale().toString());
				if(map != null && !map.isEmpty()) {
					setPermissionNameTreess(results, map);
				}
			}
		}
		return results;
    }
	
    /**
     * 
     * 描述：新增权限
     * @author 李帅
     * @created 2018年10月16日 下午1:46:30
     * @since 
     * @param permissionVo
     */
    @Override
	public void savePermission(PermissionVo permissionVo) {
		// 在Header中获取数据
		Long userId = SaaSContextHolder.getCurrentUserId();
		// Long tenantId = SaaSContextHolder.currentTenantId();
		permissionVo.setCreateBy(userId);
		permissionApi.savePermission(permissionVo);
	}
	
    /**
     * 
     * 描述：修改权限
     * @author 李帅
     * @created 2018年10月16日 下午1:46:50
     * @since 
     * @param permissionVo
     */
    @Override
	public void editPermission(PermissionVo permissionVo) {
		// 在Header中获取数据
		Long userId = SaaSContextHolder.getCurrentUserId();
		// Long tenantId = SaaSContextHolder.currentTenantId();
		permissionVo.setCreateBy(userId);
		permissionApi.editPermission(permissionVo);
	}
	
    /**
     * 
     * 描述：删除权限
     * @author 李帅
     * @created 2018年10月16日 下午1:47:12
     * @since 
     * @param permissionId
     */
    @Override
	public void deletePermission(String permissionId) {
		// 在Header中获取数据
		Long userId = SaaSContextHolder.getCurrentUserId();
		// Long tenantId = SaaSContextHolder.currentTenantId();
		permissionApi.deletePermission(permissionId, userId);
	}
}
