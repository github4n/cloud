package com.iot.permission.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.iot.permission.constants.ModuleConstants;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.iot.common.beans.BeanUtil;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.SecurityUtil;
import com.iot.common.util.StringUtil;
import com.iot.permission.entity.Permission;
import com.iot.permission.entity.Role;
import com.iot.permission.entity.RolePermissionRelate;
import com.iot.permission.entity.UserRoleRelate;
import com.iot.permission.exception.BusinessExceptionEnum;
import com.iot.permission.mapper.RoleMapper;
import com.iot.permission.service.PermissionService;
import com.iot.permission.vo.AssignPermissionDto;
import com.iot.permission.vo.PermissionDto;
import com.iot.permission.vo.PermissionVo;
import com.iot.permission.vo.RoleDto;
import com.iot.permission.vo.RoleReqDto;
import com.iot.permission.vo.UserToPermissionDto;
import com.iot.permission.vo.UserToRolesDto;
import com.iot.permission.vo.UsersToRoleDto;
import com.iot.redis.RedisCacheUtil;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：权限接口
 * 功能描述：权限接口
 * 创建人： 李帅
 * 创建时间：2018年7月3日 下午5:07:43
 * 修改人：李帅
 * 修改时间：2018年7月3日 下午5:07:43
 */
@Service
public class PermissionManager {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(PermissionManager.class);

	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private PermissionService permissionService;

	/**
	 * 
	 * 描述：获取角色信息
	 * @author 李帅
	 * @created 2018年7月12日 下午2:17:15
	 * @since 
	 * @param roleReqVo
	 */
	public List<RoleDto> getRole(RoleReqDto roleReqVo){
		
		List<Role> roles = permissionService.getRole(roleReqVo);
		List<RoleDto> roleDtos = new ArrayList<RoleDto>();
		RoleDto roleDto = null;
		for(Role role : roles) {
			roleDto = new RoleDto();
			BeanUtils.copyProperties(role, roleDto);
			roleDtos.add(roleDto);
		}
		return roleDtos;
	}

	/**
	 * 描述：依据角色类别获取角色信息
	 * @author maochengyuan
	 * @created 2018/7/14 11:42
	 * @param roleType 角色类型
	 * @return java.util.List<com.iot.permission.vo.RoleDto>
	 */
	public List<RoleDto> getRoleByRoleType(String roleType) {
		if (StringUtil.isEmpty(roleType)) {
			throw new BusinessException(BusinessExceptionEnum.ROLE_TYPE_ISNULL);
		}
		List<RoleDto> roleDtos = RedisCacheUtil.listGetAll(ModuleConstants.ROLE_INFO_ROLETYPE + roleType, RoleDto.class);
		if(roleDtos == null || roleDtos.size() == 0) {
			List<Role> roles = this.permissionService.getRole(new RoleReqDto(roleType));
			roleDtos = new ArrayList<RoleDto>();
			RoleDto roleDto = null;
			for(Role role : roles) {
				roleDto = new RoleDto();
				BeanUtils.copyProperties(role, roleDto);
				roleDtos.add(roleDto);
			}
			RedisCacheUtil.listSet(ModuleConstants.ROLE_INFO_ROLETYPE + roleType, roleDtos, null, true);
		}
		return roleDtos;
	}
	
	/**
	 * 
	 * 描述：同一用户绑定多名角色
	 * @author 李帅
	 * @created 2018年7月12日 下午2:18:07
	 * @since 
	 * @param userToRoleVo
	 */
	public void addUserToRoles(UserToRolesDto userToRoleVo) {
		if (userToRoleVo.getRoleIds() == null || userToRoleVo.getRoleIds().size() == 0) {
			throw new BusinessException(BusinessExceptionEnum.ROLEID_ISNULL);
		}
		if(userToRoleVo.getUserId() == null || userToRoleVo.getUserId() == 0) {
			throw new BusinessException(BusinessExceptionEnum.USERIDS_ISNULL);
		}
		UserRoleRelate userRoleRelate = null;
		for(Long roleId : userToRoleVo.getRoleIds()) {
			userRoleRelate = new UserRoleRelate();
//			Long userRoleRelateId = RedisCacheUtil.incr(ModuleConstants.DB_TABLE_USER_ROLE_RELATE, 0L);
//			userRoleRelate.setId(userRoleRelateId);
			userRoleRelate.setRoleId(roleId);
			userRoleRelate.setUserId(userToRoleVo.getUserId());
			userRoleRelate.setCreateBy(userToRoleVo.getCreateId());
			permissionService.saveUserRoleRelate(userRoleRelate);
		}
	}

	/**
	 * 
	 * 描述：多名用户绑定同一角色
	 * @author 李帅
	 * @created 2018年7月12日 下午2:18:07
	 * @since 
	 * @param userToRoleVo
	 */
	public void addUsersToRole(UsersToRoleDto userToRoleVo) {
		if (userToRoleVo.getRoleId() == null || userToRoleVo.getRoleId() == 0) {
			throw new BusinessException(BusinessExceptionEnum.ROLEID_ISNULL);
		}
		if(userToRoleVo.getUserIds() == null || userToRoleVo.getUserIds().size() == 0) {
			throw new BusinessException(BusinessExceptionEnum.USERIDS_ISNULL);
		}
		UserRoleRelate userRoleRelate = null;
		for(Long userId : userToRoleVo.getUserIds()) {
			userRoleRelate = new UserRoleRelate();
//			Long userRoleRelateId = RedisCacheUtil.incr(ModuleConstants.DB_TABLE_USER_ROLE_RELATE, 0L);
//			userRoleRelate.setId(userRoleRelateId);
			userRoleRelate.setRoleId(userToRoleVo.getRoleId());
			userRoleRelate.setUserId(userId);
			userRoleRelate.setCreateBy(userToRoleVo.getCreateId());
			permissionService.saveUserRoleRelate(userRoleRelate);
		}
	}
	
	/**
	 * 
	 * 描述：获取用户权限
	 * @author 李帅
	 * @created 2018年7月12日 下午7:46:27
	 * @since 
	 * @param userId
	 * @return
	 */
	public List<PermissionDto> getUserPermissionTree(Long userId) {
		if(userId == null || userId == 0) {
			throw new BusinessException(BusinessExceptionEnum.USERIDS_ISNULL);
		}
		List<PermissionDto> permissionVos = RedisCacheUtil.listGetAll(ModuleConstants.PERMISSION_INFO + userId, PermissionDto.class);
		List<Permission> permissions = null;

		Map<String, PermissionDto> permissionVoMap = new HashMap<String, PermissionDto>();
		PermissionDto permissionVo = null;
		PermissionDto permissionVoP = null;
		List<PermissionDto> childs = null;
		if (permissionVos == null || permissionVos.size() == 0) {
			permissions = permissionService.getUserPermission(userId);
			for (Permission permission : permissions) {
				permissionVo = new PermissionDto();
				BeanUtils.copyProperties(permission, permissionVo);
//				CommonUtil.encryptId(videoTaskVo.getTaskId())
				permissionVo.setId(SecurityUtil.EncryptByAES(new StringBuilder().append(permission.getId()).toString(), ModuleConstants.AES_KEY));
				if(permission.getParentId() != null) {
					permissionVo.setParentId(SecurityUtil.EncryptByAES(new StringBuilder().append(permission.getParentId()).toString(), ModuleConstants.AES_KEY));
				}
				permissionVoMap.put(permissionVo.getId(), permissionVo);
			}
			for (Permission permission : permissions) {
				if (permission.getParentId() == null) {
					permissionVos.add(permissionVoMap.get(SecurityUtil.EncryptByAES(new StringBuilder().append(permission.getId()).toString(), ModuleConstants.AES_KEY)));
				} else {
					permissionVoP = permissionVoMap.get(SecurityUtil.EncryptByAES(new StringBuilder().append(permission.getParentId()).toString(), ModuleConstants.AES_KEY));
					if(permissionVoP != null) {
						if(permissionVoP.getChilds() == null || permissionVoP.getChilds().size() == 0) {
							childs = new ArrayList<PermissionDto>();
							childs.add(permissionVoMap.get(SecurityUtil.EncryptByAES(new StringBuilder().append(permission.getId()).toString(), ModuleConstants.AES_KEY)));
							permissionVoP.setChilds(childs);
							permissionVoMap.put(permissionVoP.getId(), permissionVoP);
							permissionVoMap.remove(SecurityUtil.EncryptByAES(new StringBuilder().append(permission.getId()).toString(), ModuleConstants.AES_KEY));
						}else {
							childs = permissionVoP.getChilds();
							childs.add(permissionVoMap.get(SecurityUtil.EncryptByAES(new StringBuilder().append(permission.getId()).toString(), ModuleConstants.AES_KEY)));
							permissionVoP.setChilds(childs);
							permissionVoMap.put(permissionVoP.getId(), permissionVoP);
							permissionVoMap.remove(SecurityUtil.EncryptByAES(new StringBuilder().append(permission.getId()).toString(), ModuleConstants.AES_KEY));
						}
					}
				}
			}
			RedisCacheUtil.listSet(ModuleConstants.PERMISSION_INFO + userId, permissionVos, null, true);
		}
		return permissionVos;
	}
	
	/**
	 * 
	 * 描述：获取角色权限
	 * @author 李帅
	 * @created 2018年7月12日 下午7:46:27
	 * @since 
	 * @param userId
	 * @return
	 */
	public List<PermissionDto> getRolePermissionTree(Long roleId) {
		if(roleId == null || roleId == 0) {
			throw new BusinessException(BusinessExceptionEnum.ROLEID_ISNULL);
		}
		List<PermissionDto> permissionVos = new ArrayList<PermissionDto>();;
		List<Permission> permissions = null;

		Map<String, PermissionDto> permissionVoMap = new HashMap<String, PermissionDto>();
		PermissionDto permissionVo = null;
		PermissionDto permissionVoP = null;
		List<PermissionDto> childs = null;
		permissions = permissionService.getRolePermission(roleId);
		for (Permission permission : permissions) {
			permissionVo = new PermissionDto();
			BeanUtils.copyProperties(permission, permissionVo);
//				CommonUtil.encryptId(videoTaskVo.getTaskId())
			permissionVo.setId(SecurityUtil.EncryptByAES(new StringBuilder().append(permission.getId()).toString(), ModuleConstants.AES_KEY));
			if(permission.getParentId() != null) {
				permissionVo.setParentId(SecurityUtil.EncryptByAES(new StringBuilder().append(permission.getParentId()).toString(), ModuleConstants.AES_KEY));
			}
			permissionVoMap.put(permissionVo.getId(), permissionVo);
		}
		for (Permission permission : permissions) {
			if (permission.getParentId() == null) {
				permissionVos.add(permissionVoMap.get(SecurityUtil.EncryptByAES(new StringBuilder().append(permission.getId()).toString(), ModuleConstants.AES_KEY)));
			} else {
				permissionVoP = permissionVoMap.get(SecurityUtil.EncryptByAES(new StringBuilder().append(permission.getParentId()).toString(), ModuleConstants.AES_KEY));
				if(permissionVoP != null) {
					if(permissionVoP.getChilds() == null || permissionVoP.getChilds().size() == 0) {
						childs = new ArrayList<PermissionDto>();
						childs.add(permissionVoMap.get(SecurityUtil.EncryptByAES(new StringBuilder().append(permission.getId()).toString(), ModuleConstants.AES_KEY)));
						permissionVoP.setChilds(childs);
						permissionVoMap.put(permissionVoP.getId(), permissionVoP);
						permissionVoMap.remove(SecurityUtil.EncryptByAES(new StringBuilder().append(permission.getId()).toString(), ModuleConstants.AES_KEY));
					}else {
						childs = permissionVoP.getChilds();
						childs.add(permissionVoMap.get(SecurityUtil.EncryptByAES(new StringBuilder().append(permission.getId()).toString(), ModuleConstants.AES_KEY)));
						permissionVoP.setChilds(childs);
						permissionVoMap.put(permissionVoP.getId(), permissionVoP);
						permissionVoMap.remove(SecurityUtil.EncryptByAES(new StringBuilder().append(permission.getId()).toString(), ModuleConstants.AES_KEY));
					}
				}

			}
		}
		return permissionVos;
	}
	
	/**
	 * 
	 * 描述：获取所有权限
	 * @author 李帅
	 * @created 2018年7月12日 下午7:46:27
	 * @since 
	 * @param userId
	 * @return
	 */
	public List<PermissionDto> getAllPermissionTree() {
		List<PermissionDto> permissionVos = new ArrayList<PermissionDto>();
		List<Permission> permissions = null;

		Map<String, PermissionDto> permissionVoMap = new HashMap<String, PermissionDto>();
		PermissionDto permissionVo = null;
		PermissionDto permissionVoP = null;
		List<PermissionDto> childs = null;
		permissions = permissionService.getAllPermission();
		for (Permission permission : permissions) {
			permissionVo = new PermissionDto();
			BeanUtils.copyProperties(permission, permissionVo);
//				CommonUtil.encryptId(videoTaskVo.getTaskId())
			permissionVo.setId(SecurityUtil.EncryptByAES(new StringBuilder().append(permission.getId()).toString(), ModuleConstants.AES_KEY));
			if(permission.getParentId() != null) {
				permissionVo.setParentId(SecurityUtil.EncryptByAES(new StringBuilder().append(permission.getParentId()).toString(), ModuleConstants.AES_KEY));
			}
			permissionVoMap.put(permissionVo.getId(), permissionVo);
		}
		for (Permission permission : permissions) {
			if (permission.getParentId() == null) {
				permissionVos.add(permissionVoMap.get(SecurityUtil.EncryptByAES(new StringBuilder().append(permission.getId()).toString(), ModuleConstants.AES_KEY)));
			} else {
				permissionVoP = permissionVoMap.get(SecurityUtil.EncryptByAES(new StringBuilder().append(permission.getParentId()).toString(), ModuleConstants.AES_KEY));
				if(permissionVoP != null) {
					if(permissionVoP.getChilds() == null || permissionVoP.getChilds().size() == 0) {
						childs = new ArrayList<PermissionDto>();
						childs.add(permissionVoMap.get(SecurityUtil.EncryptByAES(new StringBuilder().append(permission.getId()).toString(), ModuleConstants.AES_KEY)));
						permissionVoP.setChilds(childs);
						permissionVoMap.put(permissionVoP.getId(), permissionVoP);
						permissionVoMap.remove(SecurityUtil.EncryptByAES(new StringBuilder().append(permission.getId()).toString(), ModuleConstants.AES_KEY));
					}else {
						childs = permissionVoP.getChilds();
						childs.add(permissionVoMap.get(SecurityUtil.EncryptByAES(new StringBuilder().append(permission.getId()).toString(), ModuleConstants.AES_KEY)));
						permissionVoP.setChilds(childs);
						permissionVoMap.put(permissionVoP.getId(), permissionVoP);
						permissionVoMap.remove(SecurityUtil.EncryptByAES(new StringBuilder().append(permission.getId()).toString(), ModuleConstants.AES_KEY));
					}
				}

			}
		}
		return permissionVos;
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
	public List<RoleDto> getUserRoles(Long userId){
		if(userId == null || userId == 0) {
			throw new BusinessException(BusinessExceptionEnum.USERIDS_ISNULL);
		}
		List<RoleDto> roleDtos = RedisCacheUtil.listGetAll(ModuleConstants.ROLE_INFO_USERID + userId, RoleDto.class);
		logger.info("roleDtos size =" + JSON.toJSONString(roleDtos));
		if(CollectionUtils.isEmpty(roleDtos)) {
			List<Role> roles = permissionService.getUserRoles(userId);
			roleDtos = new ArrayList<RoleDto>();
			RoleDto roleDto = null;
			for(Role role : roles) {
				roleDto = new RoleDto();
				BeanUtils.copyProperties(role, roleDto);
				roleDtos.add(roleDto);
			}
			RedisCacheUtil.listSet(ModuleConstants.ROLE_INFO_USERID + userId, roleDtos, null, true);
		}
		return roleDtos;
	}
	
	/**
	 * 
	 * 描述：校验用户权限
	 * @author 李帅
	 * @created 2018年7月12日 下午7:54:58
	 * @since 
	 * @param userToPermissionVo
	 * @return
	 */
	public Map<String, Boolean> verifyUserPermission(UserToPermissionDto userToPermissionVo) {
		if(userToPermissionVo.getUserId() == null || userToPermissionVo.getUserId() == 0) {
			throw new BusinessException(BusinessExceptionEnum.USERIDS_ISNULL);
		}
		if (userToPermissionVo.getPermissionUrls() == null || userToPermissionVo.getPermissionUrls().size() == 0) {
			throw new BusinessException(BusinessExceptionEnum.PERMISSIONURL_ISNULL);
		}
		Map<String, Boolean> permissionMap = new HashMap<String, Boolean>();
		Map<String, String> permissionUrls = RedisCacheUtil.hashMultiGet(ModuleConstants.PERMISSION_URL + userToPermissionVo.getUserId(), userToPermissionVo.getPermissionUrls());
		if(permissionUrls == null || permissionUrls.size() == 0) {
			List<String> permissionUrlList = permissionService.getUserPermissionUrl(userToPermissionVo.getUserId());
			Map<String, String> map = new HashMap<String, String>();
			for(String str : permissionUrlList) {
				map.put(str, str);
			}
			Map<String, Map<String, String>> redisMap = new HashMap<String, Map<String, String>>();
			redisMap.put(ModuleConstants.PERMISSION_URL + userToPermissionVo.getUserId(), map);
			RedisCacheUtil.hashPutBatch(redisMap);
		}
		for(String permissionUrl : userToPermissionVo.getPermissionUrls()) {
			if(permissionUrls.get(permissionUrl) != null) {
				permissionMap.put(permissionUrl, true);
			}else {
				permissionMap.put(permissionUrl, false);
			}
		}
		return permissionMap;
	}

	public CommonResponse addRole(RoleDto dto) {
		Role role = new Role();
		BeanUtil.copyProperties(dto, role);
		role.setIsDeleted("valid");
		roleMapper.insert(role);
		return CommonResponse.success();
	}

	public CommonResponse editRole(RoleDto dto) {
		Role role = roleMapper.selectByPrimaryKey(dto.getId());
//		role.setRoleCode(dto.getRoleCode());
		role.setRoleName(dto.getRoleName());
//		role.setRoleType(dto.getRoleType());
//		role.setRoleDesc(dto.getRoleDesc());
		role.setUpdateBy(dto.getUpdateBy());
		role.setUpdateTime(dto.getUpdateTime());
		roleMapper.updateByPrimaryKey(role);
		return CommonResponse.success();
	}

	public CommonResponse deleteRole(Long id) {
		roleMapper.deleteByPrimaryKey(id);
		return CommonResponse.success();
	}

	public CommonResponse assignPermissionToRole(Long roleId, Long[] permissionIds) {
		// delete old permission
		permissionService.deletePermissionByRoleId(roleId);

		// add old permission
		for (Long permissionId : permissionIds) {
			RolePermissionRelate r = new RolePermissionRelate();
			r.setRoleId(roleId);
			r.setPermissionId(permissionId);
			permissionService.saveRolePermissionRelate(r);
		}

		return CommonResponse.success();
	}


	public List<PermissionDto> getPermissionList(PermissionVo vo) {
		List<Permission> permissions = permissionService.getPermissionList(vo);
		List<PermissionDto> list = Lists.newArrayList();
		for (Permission p : permissions) {
			PermissionDto dto = new PermissionDto();
			BeanUtils.copyProperties(p, dto);
			dto.setId(String.valueOf(p.getId()));
			dto.setParentId(p.getParentId() == null ? null : String.valueOf(p.getParentId()));
			list.add(dto);
		}
		return list;
	}

	public List<PermissionDto> getFunctionPermission(Long userId) {
		List<Permission> list = permissionService.getUserPermission(userId);
		List<PermissionDto> dtos = Lists.newArrayList();
		for (Permission p : list) {
			PermissionDto dto = new PermissionDto();
			BeanUtils.copyProperties(p, dto);
			dto.setParentId(p.getParentId() == null ? null : String.valueOf(p.getParentId()));
			dto.setId(String.valueOf(p.getId()));
			dtos.add(dto);
		}
		return dtos;
	}

	public List<PermissionDto> getPermissionByRoleId(Long roleId) {
		return permissionService.getPermissionByRoleId(roleId);
	}
	
	/**
	 * 
	 * 描述：新增权限
	 * @author 李帅
	 * @created 2018年10月16日 上午11:16:07
	 * @since 
	 * @param permissionVo
	 */
	public void savePermission(PermissionVo permissionVo){
		if(permissionVo.getCreateBy() == null) {
			throw new BusinessException(BusinessExceptionEnum.USERID_ISNULL);
		}
		List<RoleDto> roles = getUserRoles(permissionVo.getCreateBy());
		Boolean bossOwner = false;
		for(RoleDto role : roles) {
			if("Owner".equals(role.getRoleCode()) && "Boss".equals(role.getRoleType())) {
				bossOwner = true;
			}
		}
		if(!bossOwner) {
			throw new BusinessException(BusinessExceptionEnum.USE_NO_MODIFY_PERMISSION);
		}else {
			Permission permission = new Permission();
			BeanUtil.copyProperties(permissionVo, permission);
			if(permissionVo.getParentId() != null) {
				permission.setParentId(Long.parseLong(SecurityUtil.DecryptAES(permissionVo.getParentId(), ModuleConstants.AES_KEY)));
			}
			permissionService.savePermission(permission);
			RedisCacheUtil.deleteBlurry(ModuleConstants.PERMISSION_INFO);
			RedisCacheUtil.deleteBlurry(ModuleConstants.PERMISSION_URL);
		}
		
	}

	/**
	 * 
	 * 描述：修改权限
	 * @author 李帅
	 * @created 2018年10月16日 上午11:16:28
	 * @since 
	 * @param permissionVo
	 */
	public void editPermission(PermissionVo permissionVo){
		if(StringUtil.isEmpty(permissionVo.getId())) {
			throw new BusinessException(BusinessExceptionEnum.PERMISSIONID_ISNULL);
		}
		if(permissionVo.getCreateBy() == null) {
			throw new BusinessException(BusinessExceptionEnum.USERID_ISNULL);
		}
		List<RoleDto> roles = getUserRoles(permissionVo.getCreateBy());
		Boolean bossOwner = false;
		for(RoleDto role : roles) {
			if("Owner".equals(role.getRoleCode()) && "Boss".equals(role.getRoleType())) {
				bossOwner = true;
			}
		}
		if(!bossOwner) {
			throw new BusinessException(BusinessExceptionEnum.USE_NO_MODIFY_PERMISSION);
		}else {
			Permission permission = new Permission();
			BeanUtil.copyProperties(permissionVo, permission);
			if(permissionVo.getId() != null) {
				permission.setId(Long.parseLong(SecurityUtil.DecryptAES(permissionVo.getId(), ModuleConstants.AES_KEY)));
			}
			if(permissionVo.getParentId() != null) {
				permission.setParentId(Long.parseLong(SecurityUtil.DecryptAES(permissionVo.getParentId(), ModuleConstants.AES_KEY)));
			}
			permissionService.editPermission(permission);
			RedisCacheUtil.deleteBlurry(ModuleConstants.PERMISSION_INFO);
			RedisCacheUtil.deleteBlurry(ModuleConstants.PERMISSION_URL);
		}
		
	}

	/**
	 * 
	 * 描述：删除权限
	 * @author 李帅
	 * @created 2018年10月16日 上午11:16:51
	 * @since 
	 * @param permissionId
	 * @param userId
	 */
	public void deletePermission(String permissionId, Long userId){
		if(StringUtil.isEmpty(permissionId)) {
			throw new BusinessException(BusinessExceptionEnum.PERMISSIONID_ISNULL);
		}
		if(userId == null) {
			throw new BusinessException(BusinessExceptionEnum.USERID_ISNULL);
		}
		List<RoleDto> roles = getUserRoles(userId);
		Boolean bossOwner = false;
		for(RoleDto role : roles) {
			if("Owner".equals(role.getRoleCode()) && "Boss".equals(role.getRoleType())) {
				bossOwner = true;
			}
		}
		if(!bossOwner) {
			throw new BusinessException(BusinessExceptionEnum.USE_NO_MODIFY_PERMISSION);
		}else {
			List<PermissionDto> permissionDtos = getAllPermissionTree();
			List<Long> permissionIds = new ArrayList<Long>();
			for(PermissionDto permissionDto : permissionDtos) {
				permissionIds = getPermissionById(permissionId, permissionDto, permissionIds);
			}
			
			permissionService.deletePermission(permissionIds);
			permissionService.deleteRolePermissionRelate(permissionIds);
			RedisCacheUtil.deleteBlurry(ModuleConstants.PERMISSION_INFO);
			RedisCacheUtil.deleteBlurry(ModuleConstants.PERMISSION_URL);
		}
		
	}
	
	/**
	 * 
	 * 描述：定位该权限在权限树的位置，并返回其下所有枝叶的权限ID
	 * @author 李帅
	 * @created 2018年10月16日 上午11:17:27
	 * @since 
	 * @param permissionId
	 * @param permissionDto
	 * @param permissionIds
	 * @return
	 */
	public List<Long> getPermissionById(String permissionId, PermissionDto permissionDto, List<Long> permissionIds){
		if(permissionDto.getId().equals(permissionId)) {
			permissionIds = getChildPermissionId(permissionDto, permissionIds);
		}else if(permissionDto.getChilds() != null && permissionDto.getChilds().size() > 0) {
			for(PermissionDto permission : permissionDto.getChilds()) {
				permissionIds = getPermissionById(permissionId, permission, permissionIds);
			}
		}
		return permissionIds;
	}
	
	/**
	 * 
	 * 描述：获取其下所有枝叶的权限ID
	 * @author 李帅
	 * @created 2018年10月16日 上午11:19:15
	 * @since 
	 * @param permissionDto
	 * @param permissionIds
	 * @return
	 */
	public List<Long> getChildPermissionId(PermissionDto permissionDto, List<Long> permissionIds){
		permissionIds.add(Long.parseLong(SecurityUtil.DecryptAES(permissionDto.getId(), ModuleConstants.AES_KEY)));
		if(permissionDto.getChilds() != null && permissionDto.getChilds().size() > 0) {
			for(PermissionDto permission : permissionDto.getChilds()) {
				permissionIds = getChildPermissionId(permission, permissionIds);
			}
			
		}
		return permissionIds;
	}
	
	/**
	 * 
	 * 描述：BOSS拥有者分配其他角色功能权限
	 * @author 李帅
	 * @created 2018年10月16日 上午11:15:08
	 * @since 
	 */
	public void bossOwnerAssignPermissionToOtherRole(AssignPermissionDto assignPermissionDto) {
		if(assignPermissionDto.getUserId() == null) {
			throw new BusinessException(BusinessExceptionEnum.USERID_ISNULL);
		}
		if(assignPermissionDto.getRoleId() == null || assignPermissionDto.getRoleId() == 0) {
			throw new BusinessException(BusinessExceptionEnum.ROLEID_ISNULL);
		}
		if(assignPermissionDto.getPermissionIds() == null || assignPermissionDto.getPermissionIds().length == 0) {
			throw new BusinessException(BusinessExceptionEnum.PERMISSIONID_ISNULL);
		}
		List<RoleDto> roles = getUserRoles(assignPermissionDto.getUserId());
		Boolean bossOwner = false;
		for(RoleDto role : roles) {
			if("Owner".equals(role.getRoleCode()) && "Boss".equals(role.getRoleType())) {
				bossOwner = true;
			}
		}
		if(!bossOwner) {
			throw new BusinessException(BusinessExceptionEnum.USE_NO_MODIFY_PERMISSION);
		}else {
			permissionService.deletePermissionByRoleId(assignPermissionDto.getRoleId());

			for (String permissionId : assignPermissionDto.getPermissionIds()) {
				RolePermissionRelate r = new RolePermissionRelate();
				r.setRoleId(assignPermissionDto.getRoleId());
				r.setPermissionId(Long.parseLong(SecurityUtil.DecryptAES(permissionId, ModuleConstants.AES_KEY)));
				permissionService.saveRolePermissionRelate(r);
			}
		}
	}

	/**
	 * 保存用户角色关系
	 */
	public void saveUserRoleRelate(UserRoleRelate userRoleRelate) {
		permissionService.saveUserRoleRelate(userRoleRelate);
	}

	/**
	 * 删除某个用户的角色关系
	 */
	public void deleteUserRoleRelateByUserId(Long userId) {
		permissionService.deleteUserRoleRelateByUserId(userId);
	}

	/**
	 * 描述：删除子账户时，删除用户关联的角色
	 * @author wucheng
	 * @date 2018-11-06 11:15:13
	 * @param roleIds
	 * @return
	 */
	public int deleteUserRoleRelateByRoleId(List<Long> roleIds) {
		return permissionService.deleteUserRoleRelateById(roleIds);
	}

	/**
	 * 描述：根据用户id获取当前子账户关联的角色信息
	 * @author wucheng
	 * @date 2018-11-06 11:15:13
	 * @param userId 用户id
	 * @return
	 */
	public List<UserRoleRelate> getUserRoleRelateByUserId(Long userId){
		return permissionService.getUserRoleRelateByUserId(userId);
	}
}
