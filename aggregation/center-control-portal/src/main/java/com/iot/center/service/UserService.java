package com.iot.center.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.building.permission.api.UserPermissionApi;
import com.iot.building.permission.vo.UserDataPermissionAssignDto;
import com.iot.building.permission.vo.UserDataPermissionRelateDto;
import com.iot.building.space.api.SpaceApi;
import com.iot.building.space.vo.QueryParamReq;
import com.iot.center.helper.BusinessExceptionEnum;
import com.iot.center.helper.Constants;
import com.iot.center.utils.ExcelUtils;
import com.iot.center.vo.UserInfoVo;
import com.iot.common.beans.BeanUtil;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.common.helper.Page;
import com.iot.common.util.MD5SaltUtil;
import com.iot.control.space.vo.SpaceAndSpaceDeviceVo;
import com.iot.control.space.vo.SpaceResp;
import com.iot.permission.api.PermissionApi;
import com.iot.permission.enums.DataTypeEnum;
import com.iot.permission.vo.*;
import com.iot.user.api.UserApi;
import com.iot.user.enums.AdminStatusEnum;
import com.iot.user.enums.UserLevelEnum;
import com.iot.user.enums.UserStatusEnum;
import com.iot.user.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserApi userApi;
    @Autowired
    private SpaceApi spaceApi;
    @Autowired
    private Environment dnvironment;
    @Autowired
    private PermissionApi permissionApi;
    @Autowired
    private UserPermissionApi userPermissionApi;
    @Autowired
    private com.iot.control.space.api.SpaceApi commonSpaceApi;

    public CommonResponse<Page<UserResp>> getUserList(UserSearchReq req) {
        Page<UserResp> page = userApi.getUserList(req);
        List<Long> userIds=Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(page.getResult())) {
        	for (UserResp user : page.getResult()) {
                userIds.add(user.getId());
            }
            Map<Long,List<RoleDto>> dtos = permissionApi.getBathUserRoles(userIds);//
            Map<Long,List<UserDataPermissionRelateDto>> permissionMap=userPermissionApi.getDataPermissionByUserIds(userIds);
            page.getResult().forEach(user->{
            	user.setPassword(null);
            	user.setMqttPassword(null);
            	//1 权限关系
            	getPermissionScope(permissionMap, user);
            	//2 角色关系
            	getRole(dtos, user);
            });
        }
        return CommonResponse.success(page);
    }

	private void getRole(Map<Long, List<RoleDto>> dtos, UserResp user) {
		List<RoleDto> dto=dtos.get(user.getId());
		// user relate role is 1:1
		user.setRoleName(CollectionUtils.isNotEmpty(dto) ? dto.get(0).getRoleName() : null);
	}

	private void getPermissionScope(Map<Long, List<UserDataPermissionRelateDto>> permissionMap, UserResp user) {
		List<UserDataPermissionRelateDto> dtoList=permissionMap.get(user.getId());
		StringBuffer sb = new StringBuffer();
		if(CollectionUtils.isNotEmpty(dtoList)) {
             for (UserDataPermissionRelateDto relateDto : dtoList) {
                 if (relateDto.getDataType() == null || relateDto.getDataType().intValue() == 1) {
                     continue;
                 }
                 if (Strings.isNullOrEmpty(sb.toString())) {
                     sb.append(relateDto.getDataName());
                 } else {
                     sb.append(",").append(relateDto.getDataName());
                 }
             }
		}
		user.setDataScope(sb.toString());
	}

    public CommonResponse addUser(UserReq req) {
        dnvironment= ApplicationContextHelper.getBean(Environment.class);
        String headImg = dnvironment.getProperty(Constants.headImg);
        LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
        req.setUserLevel(UserLevelEnum.BUSINESS.getCode());
        req.setTenantId(user.getTenantId());
        // todo check user name is alone
        int count = userApi.checkUserName2B(req.getUserName());
        if (count == 0) {
            return new CommonResponse(ResultMsg.FAIL, "用户名重复,请重新提交！");
        }
        req.setPassword(req.getPassword());//默认密码 配合登录一定要md5
        req.setLocationId(user.getLocationId());
        req.setUserLevel(UserLevelEnum.BUSINESS.getCode());
        req.setUserStatus(UserStatusEnum.NORMAL.getCode());
        req.setAdminStatus(AdminStatusEnum.NORMAL.getCode());
        req.setCreateTime(new Date());
        req.setUpdateTime(new Date());
        if(req.getId() ==null){
            //新增，设置默认头像
            req.setHeadImg(headImg);
        }
        Long userId = userApi.addUser(req);
        return CommonResponse.success(userId);
    }


    public CommonResponse editUser(UserReq req) {
    	judgeSuperAdmin(req.getId());
        userApi.editUser(req);
        return CommonResponse.success();
    }

    private void judgeSuperAdminRole(Long id,Long roleId) {
		FetchUserResp userResp=userApi.getUser(id);
		List<RoleDto> userRoles=permissionApi.getUserRoles(id);
		int i=0;
		if(CollectionUtils.isNotEmpty(userRoles)) {
			for(RoleDto dto:userRoles) {
				if(roleId.compareTo(dto.getId())==0) {
					i++;
					break;
				}
			}
		}
    	if(userResp.getAdminStatus()==-1 && i==0) {
    		throw new BusinessException(BusinessExceptionEnum.SUPER_ADMIN_EXCEPITON);
    	}
	}
    
	private void judgeSuperAdmin(Long id) {
		FetchUserResp userResp=userApi.getUser(id);
    	if(userResp.getAdminStatus()==-1) {
    		throw new BusinessException(BusinessExceptionEnum.SUPER_ADMIN_EXCEPITON);
    	}
	}

    public CommonResponse deleteUser(Long id) {
    	judgeSuperAdmin(id);
        userApi.deleteUser(id);
        return CommonResponse.success();
    }

    public CommonResponse<List<RoleDto>> getRoleList(RoleReqDto req) {
        List<RoleDto> dtos = permissionApi.getRole(req);
        return CommonResponse.success(dtos);
    }

    public CommonResponse addRole(RoleDto role,Long userId) {
        LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
        //校验重名
        RoleReqDto roleReqVo = new RoleReqDto();
        roleReqVo.setRoleName(role.getRoleName());
        int count = permissionApi.getRole(roleReqVo).size();
        if (count != 0) {
            return new CommonResponse(ResultMsg.FAIL, "用户名重复,请重新提交！");
        }
        role.setRoleType("2B");
        role.setRoleCode("Manager");
        role.setTenantId(user.getTenantId());
        role.setOrgId(user.getOrgId());
        role.setCreateTime(new Date());
        role.setUpdateTime(new Date());
        role.setCreateBy(userId);
        role.setUpdateBy(userId);
        return permissionApi.addRole(role);
    }

    public CommonResponse editRole(RoleDto role) {
        return permissionApi.editRole(role);
    }

    public CommonResponse deleteRole(Long id) {
        return permissionApi.deleteRole(id);
    }

    public CommonResponse assignPermissionToRole(Long roleId, Long[] permissionIds) {
        return permissionApi.assignPermissionToRole(roleId, permissionIds);
    }

    public CommonResponse assignPermissionToUser(Long tenantId,Long userId, Long roleId, Long[] spaceIds) {
        judgeSuperAdminRole(userId,roleId);
        List<Long> ids = Lists.newArrayList(spaceIds);
        QueryParamReq req =new QueryParamReq();
        req.setTenantId(tenantId);
    	req.setSpaceIds(ids);
        List<SpaceResp> list = spaceApi.findBySpaceIds(req);
        List objects = Lists.newArrayList();
        for (SpaceResp space : list) {
            JSONObject o = JSON.parseObject(JSON.toJSONString(space), JSONObject.class);
            objects.add(o);
        }
        UserDataPermissionAssignDto dto = new UserDataPermissionAssignDto(userId, roleId, objects);

        return userPermissionApi.assignSpacePermissionToUser(dto);
    }

    public CommonResponse<UserInfoVo> getUserPermission(Long userId) {

        UserInfoVo info = new UserInfoVo();

        FetchUserResp user = userApi.getUser(userId);
        if (user != null) {
            BeanUtil.copyProperties(user, info);
        }

        List<RoleDto> roleList = permissionApi.getUserRoles(userId);
        info.setRoles(roleList);

        List<PermissionDto> functions = permissionApi.getFunctionPermission(userId);
        info.setFunctionPermissions(functions);

        // data permission contains project data and space data
        List<UserDataPermissionRelateDto> data = userPermissionApi.getDataPermission(userId);
        List<UserDataPermissionRelateDto> dataList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(data)) {
            List<Long> spaceIdList = Lists.newArrayList();
            Map<Long, Long> idMap = Maps.newHashMap();
            for (UserDataPermissionRelateDto dto : data) {
                // check data is project data
                if (dto.getDataType() != null && dto.getDataType().intValue() == DataTypeEnum.PROJECT.getCode()) {
                    dataList.add(dto);
                    continue;
                }
                spaceIdList.add(dto.getDataId());
                idMap.put(dto.getDataId(), dto.getId());
            }
            // find space contains all child space
            if (CollectionUtils.isNotEmpty(spaceIdList)) {
            	SpaceAndSpaceDeviceVo req = new SpaceAndSpaceDeviceVo();
            	req.setTenantId(user.getTenantId());
            	req.setSpaceIds(spaceIdList);
                List<SpaceResp> spaceList = commonSpaceApi.findSpaceInfoBySpaceIds(req);
                for (SpaceResp space : spaceList) {
                    UserDataPermissionRelateDto relate = new UserDataPermissionRelateDto();
                    relate.setDataId(space.getId());
                    relate.setDataName(space.getName());
                    relate.setDataType(getDateType(space.getType()));
                    relate.setUserId(userId);
                    relate.setId(idMap.get(space.getId()));
                    dataList.add(relate);
                }
            }
            info.setDataPermissions(dataList);
        }
        Constants.userPermission.put(userId, info.getFunctionPermissions());
        Constants.dataPermission.put(userId, info.getDataPermissions());
        return CommonResponse.success(info);
    }

    public CommonResponse<List<PermissionDto>> getPermissionList() {
        LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
        PermissionVo vo = new PermissionVo();
        vo.setOrgId(user.getOrgId());
        vo.setTenantId(user.getTenantId());
        vo.setSystemType(Constants.SYSTEM_TYPE);
        List<PermissionDto> list = permissionApi.getPermissionList(vo);
        return CommonResponse.success(list);
    }

    public CommonResponse<List<PermissionDto>> getPermissionByRoleId(Long roleId) {
        List<PermissionDto> list = permissionApi.getPermissionByRoleId(roleId);
        return CommonResponse.success(list);
    }

    public CommonResponse batchImportUser(MultipartFile file) {

        LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
        List successSb = Lists.newArrayList();
        List failSb = Lists.newArrayList();
        try {
            List<String[]> lines = ExcelUtils.resolveExcel(file);
            if (CollectionUtils.isEmpty(lines)) {
                return new CommonResponse(ResultMsg.FAIL, "未找到有效数据！");
            }
            // first line is title
            for (int i=0; i<lines.size(); i++) {
                try {
                    String[] line = lines.get(i);
                    if (ArrayUtils.isEmpty(line) || line.length < 2) {
                        continue;
                    }

                    String nickname = line[0];
                    String userName = line[1];
                    String roleName = line.length == 3 ? line[2] : null;

                    UserReq req = new UserReq();
                    req.setUserName(userName);
                    req.setNickname(nickname);
                    req.setPassword(MD5SaltUtil.MD5("abc123"));
                    req.setUserLevel(UserLevelEnum.BUSINESS.getCode());
                    req.setTenantId(user.getTenantId());
                    req.setCreateTime(new Date());
                    req.setUpdateTime(new Date());
                    CommonResponse response = addUser(req);

                    if (response.isSuccess()) {
                        successSb.add(userName);
                        // add user role
                        if (!Strings.isNullOrEmpty(roleName)) {
                            Long userId = (Long) response.getData();
                            addUserRole(userId, roleName, user.getTenantId(), user.getUserId());
                        }
                    } else {
                        failSb.add(userName+" "+response.getData());
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    continue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String,Object> backMap=Maps.newHashMap();
        backMap.put("success", successSb);
        backMap.put("fail", failSb);

        return CommonResponse.success(backMap);
    }

    private void addUserRole(Long userId, String roleName, Long tenantId, Long createId) {
        try {
            RoleReqDto dto = new RoleReqDto();
            dto.setRoleName(roleName);
            dto.setRoleType("2B");
            dto.setTenantId(tenantId);
            List<RoleDto> roles = permissionApi.getRole(dto);
            if (CollectionUtils.isEmpty(roles)) {
                return;
            }
            Long roleId = roles.get(0).getId();
            UserToRolesDto userToRolesDto = new UserToRolesDto(createId, userId, Lists.newArrayList(roleId));
            permissionApi.addUserToRoles(userToRolesDto);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    
    /**
     * excel 导入用户
     * @param user
     * @param list
     */
    public Map<String,Object> saveUserAndRoleByExcel(LoginResp user, List<String[]> list) {
    	Map<String,Object> map=Maps.newHashMap();
    	List<String> error = Lists.newArrayList();
    	List<String> success = Lists.newArrayList();
		if (list != null) {
			log.info("list.size:"+list.size());
		    for (int i= 0;i<list.size();i++) {
		    	try {
		    		// 判断该设备是否存在
		            String username = list.get(i)[0];//用户名
		            if(StringUtils.isBlank(username)){
		            	error.add(i+1+"行用户名为空");
		        		continue;
		        	}
		        	Integer flag = userApi.checkUserName2B(username);
		        	if (flag==0) {//代表用户存在
		        		error.add(username+"用户已经存在");
		        		continue;
		        	}
		            String nickName = list.get(i)[1];//昵称
		            Long userId=saveUserExcel(user, username,nickName);
		            //角色
		            String roleName = list.get(i)[2];
		            saveRole(user, userId, roleName);
		            success.add(username);
				} catch (Exception e) {
					e.printStackTrace();
					error.add(i+1+"行执行异常");
					continue;
				}
		    }
		}
		map.put("success", success);
		map.put("error", error);
		return map;
	}
    
    private Long saveUserExcel(LoginResp user, String username,String nickName) {
    	UserReq req=new UserReq();
    	log.info("获取解析的uuid"+username);
    	req.setUserName(username);
    	req.setNickname(nickName);
    	req.setPassword(MD5SaltUtil.MD5("000000"));
    	req.setUserLevel(UserLevelEnum.BUSINESS.getCode());
    	req.setTenantId(user.getTenantId());
    	req.setLocationId(user.getLocationId());
    	req.setUserLevel(UserLevelEnum.BUSINESS.getCode());
    	req.setUserStatus(UserStatusEnum.NORMAL.getCode());
    	req.setAdminStatus(AdminStatusEnum.NORMAL.getCode());
    	req.setCreateTime(new Date());
    	req.setUpdateTime(new Date());
    	Long userId = userApi.addUser(req);
        return userId;
    }

	private void saveRole(LoginResp user, Long userId, String roleName) {
		if(StringUtils.isNotBlank(roleName)) {
		    RoleReqDto dot=new RoleReqDto();
		    dot.setTenantId(user.getTenantId());
		    dot.setRoleName(roleName);
		    List<RoleDto> roleList=permissionApi.getRole(dot);
		    if(CollectionUtils.isNotEmpty(roleList)) {
		    	RoleDto role=roleList.get(0);
		    	List objects = Lists.newArrayList();
		        UserDataPermissionAssignDto dto = new UserDataPermissionAssignDto(userId, role.getId(), objects);
		        userPermissionApi.assignSpacePermissionToUser(dto);
		    }
		}
	}
	
	public List<UserDataPermissionRelateDto> getDataPermission(Long tenantId,List<UserDataPermissionRelateDto> dataPermission){
		List<SpaceResp> spaceList=Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(dataPermission)) {
			for(UserDataPermissionRelateDto dto:dataPermission) {
				if(dto.getDataType()==DataTypeEnum.BUILD.getCode() 
						|| dto.getDataType()==DataTypeEnum.FLOOR.getCode()) {
					spaceList.addAll(commonSpaceApi.findChild(tenantId, dto.getDataId()));
				}
			}
		}
		if(CollectionUtils.isNotEmpty(spaceList)) {
			spaceList.forEach(resp->{
				UserDataPermissionRelateDto vo=new UserDataPermissionRelateDto();
				vo.setDataId(resp.getId());
				vo.setDataName(resp.getName());
				vo.setDataType(getDateType(resp.getType()));
				dataPermission.add(vo);
			});
		}
		return dataPermission;
	}
	
	private Integer getDateType(String type) {
		 switch (type) {
	         case "BUILD":
	         	return DataTypeEnum.BUILD.getCode();
	         case "FLOOR": 
	        	 return DataTypeEnum.FLOOR.getCode();
	         case "ROOM":
	        	 return DataTypeEnum.ROOM.getCode();
		 }
		 return 4;
	}
}
