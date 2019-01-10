package com.iot.tenant.api;

import com.iot.tenant.api.fallback.UserVirtualOrgApiFallbackFactory;
import com.iot.tenant.vo.req.AddUserVirtualOrgReq;
import com.iot.tenant.vo.resp.UserDefaultOrgInfoResp;
import com.iot.tenant.vo.resp.UserOrgInfoResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Api(tags = "用户-组织服务接口")
@FeignClient(value = "tenant-service", fallbackFactory = UserVirtualOrgApiFallbackFactory.class)
@RequestMapping("/userVirtualOrgApi")
public interface UserVirtualOrgApi {

    @ApiOperation("添加用户-组织信息")
    @RequestMapping(value = "/addUserVirtualOrg", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long addUserVirtualOrg(@RequestBody AddUserVirtualOrgReq req);


    @ApiOperation("根据用户获取组织信息")
    @RequestMapping(value = "/getOrgInfoByUserId", method = RequestMethod.GET)
    UserOrgInfoResp getOrgInfoByUserId(@RequestParam("userId") Long userId);

    @ApiOperation("根据用户获取所属创建组织信息[list 集合只有一个]")
    @RequestMapping(value = "/getDefaultUsedOrgInfoByUserId", method = RequestMethod.GET)
    UserDefaultOrgInfoResp getDefaultUsedOrgInfoByUserId(@RequestParam("userId") Long userId);

    @ApiOperation("删除用户与组织的关系")
    @RequestMapping(value = "/deleteOrgByUserIdAndOrgId", method = RequestMethod.GET)
    void deleteOrgByUserIdAndOrgId(@RequestParam("userId") Long userId, @RequestParam("orgId") Long orgId);
}
