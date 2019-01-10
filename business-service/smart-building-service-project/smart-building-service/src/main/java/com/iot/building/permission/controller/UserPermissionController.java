package com.iot.building.permission.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.iot.building.permission.api.UserPermissionApi;
import com.iot.building.permission.service.UserPermissionService;
import com.iot.building.permission.vo.UserDataPermissionAssignDto;
import com.iot.building.permission.vo.UserDataPermissionRelateDto;
import com.iot.common.beans.CommonResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: Xieby
 * @Date: 2018/10/25
 * @Description: *
 */
@RestController
public class UserPermissionController implements UserPermissionApi {

    @Autowired
    private UserPermissionService userPermissionService;

    @Override
    public List<UserDataPermissionRelateDto> getDataPermission(Long userId) {
        return userPermissionService.getDataPermission(userId);
    }
    
    @Override
    public Map<Long, List<UserDataPermissionRelateDto>> getDataPermissionByUserIds(@RequestBody List<Long> userIds) {
    	Map<Long, List<UserDataPermissionRelateDto>> userPermission=Maps.newHashMap();
    	if(CollectionUtils.isNotEmpty(userIds)) {
    		for(Long userId:userIds) {
    			List<UserDataPermissionRelateDto> list=userPermissionService.getDataPermission(userId);
    			userPermission.put(userId, list);
    		}
    	}
    	return userPermission;
    }

    @Override
    public CommonResponse assignSpacePermissionToUser(@RequestBody UserDataPermissionAssignDto dto) {
        return userPermissionService.assignSpacePermissionToUser(dto);
    }
}
