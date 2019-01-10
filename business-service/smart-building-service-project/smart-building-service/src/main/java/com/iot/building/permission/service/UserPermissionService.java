package com.iot.building.permission.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iot.building.permission.entity.UserDataPermissionRelate;
import com.iot.building.permission.mapper.UserDataPermissionRelateMapper;
import com.iot.building.permission.vo.UserDataPermissionAssignDto;
import com.iot.building.permission.vo.UserDataPermissionRelateDto;
import com.iot.common.beans.CommonResponse;
import com.iot.permission.api.PermissionApi;
import com.iot.permission.constants.ModuleConstants;
import com.iot.permission.entity.UserRoleRelate;
import com.iot.permission.enums.DataTypeEnum;
import com.iot.permission.vo.RoleDto;
import com.iot.redis.RedisCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Xieby
 * @Date: 2018/10/25
 * @Description: *
 */
@Service
public class UserPermissionService {

    private Logger log = LoggerFactory.getLogger(UserPermissionService.class);

    @Autowired
    private PermissionApi permissionApi;
    @Autowired
    private UserDataPermissionRelateMapper userDataPermissionRelateMapper;

    public List<UserDataPermissionRelateDto> getDataPermission(Long userId) {
        return userDataPermissionRelateMapper.getDataPermissionByUserId(userId);
    }

    public CommonResponse assignSpacePermissionToUser(UserDataPermissionAssignDto dto) {
        Long userId = dto.getUserId();
        Long roleId = dto.getRoleId();

        // delete old data permission
        userDataPermissionRelateMapper.deleteSpacePermissionByUserId(userId);

        // delete old user role relate
        permissionApi.deleteUserRoleRelateByUserId(userId);

        // add user role relate
        UserRoleRelate r = new UserRoleRelate();
        r.setRoleId(roleId);
        r.setUserId(userId);
        permissionApi.saveUserRoleRelate(r);

        refreshRoleInfo(userId);

        // add user data relate
        for (JSONObject space : dto.getList()) {
            UserDataPermissionRelate dataRelate = new UserDataPermissionRelate();
            Long spaceId = space.getLong("id");
            String type = space.getString("type");
            String spaceName = space.getString("name");
            int spaceType = 0;
            switch (type) {
                case "BUILD" : spaceType = DataTypeEnum.BUILD.getCode(); break;
                case "FLOOR" : spaceType = DataTypeEnum.FLOOR.getCode() ; break;
                default :      spaceType = DataTypeEnum.ROOM.getCode() ; break;
            }
            dataRelate.setUserId(userId);
            dataRelate.setDataId(spaceId);
            dataRelate.setDataType(spaceType);
            dataRelate.setDataName(spaceName);
            saveUserDataPermissionRelate(dataRelate);
        }

        return CommonResponse.success();
    }

    private void saveUserDataPermissionRelate(UserDataPermissionRelate dataRelate) {
        userDataPermissionRelateMapper.insert(dataRelate);
    }

    private void refreshRoleInfo(Long userId) {
        log.info("before delete, is contain key {} = {} . ", ModuleConstants.ROLE_INFO_USERID + userId, RedisCacheUtil.hasKey(ModuleConstants.ROLE_INFO_USERID + userId));
        RedisCacheUtil.delete(ModuleConstants.ROLE_INFO_USERID + userId);
        log.info("after delete, is contain key {} = {} . ", ModuleConstants.ROLE_INFO_USERID + userId, RedisCacheUtil.hasKey(ModuleConstants.ROLE_INFO_USERID + userId));
        List<RoleDto> roleList = permissionApi.getUserRoles(userId);
        log.info("role list = " + JSON.toJSONString(roleList));
//        List<RoleDto> dtos = Lists.newArrayList();
//        for (Role role : roleList) {
//            RoleDto dto = new RoleDto();
//            BeanUtil.copyProperties(role, dto);
//            dtos.add(dto);
//        }
        RedisCacheUtil.listSet(ModuleConstants.ROLE_INFO_USERID + userId, roleList, null, true);
    }
}
