package com.iot.building.permission.api;

import com.iot.building.permission.api.fallback.UserPermissionApiFallbackFactory;
import com.iot.building.permission.vo.UserDataPermissionAssignDto;
import com.iot.building.permission.vo.UserDataPermissionRelateDto;
import com.iot.common.beans.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author: Xieby
 * @Date: 2018/10/25
 * @Description: *
 */
@Api("数据权限接口")
@FeignClient(value = "building-control-service", fallbackFactory = UserPermissionApiFallbackFactory.class)
@RequestMapping("/user/permission")
public interface UserPermissionApi {

    @ApiOperation(value = "获取用户数据权限", notes = "获取用户数据权限")
    @RequestMapping(value = "/getDataPermission", method= RequestMethod.POST)
    List<UserDataPermissionRelateDto> getDataPermission(@RequestParam("userId") Long userId);

    @ApiOperation(value = "分配数据权限到用户", notes = "分配数据权限到用户")
    @RequestMapping(value = "/assignSpacePermissionToUser", method=RequestMethod.POST,consumes= MediaType.APPLICATION_JSON_VALUE)
    CommonResponse assignSpacePermissionToUser(@RequestBody UserDataPermissionAssignDto dto);

    @ApiOperation(value = "获取批量用户数据权限", notes = "获取用户数据权限")
    @RequestMapping(value = "/getDataPermissionByUserIds", method= RequestMethod.POST,consumes= MediaType.APPLICATION_JSON_VALUE)
    Map<Long,List<UserDataPermissionRelateDto>> getDataPermissionByUserIds(@RequestBody List<Long> userIds);

}
