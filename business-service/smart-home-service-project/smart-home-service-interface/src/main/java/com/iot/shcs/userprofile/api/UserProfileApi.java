package com.iot.shcs.userprofile.api;

import com.iot.shcs.userprofile.vo.req.UserProfileReq;
import com.iot.shcs.userprofile.vo.resp.UserProfileResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2019/1/3 22:20
 * 修改人:
 * 修改时间：
 */

@Api(tags = "用户配置信息接口")
@FeignClient(value = "smart-home-service")
@RequestMapping("/userprofile")
public interface UserProfileApi {
    @ApiOperation("获取用户配置信息")
    @RequestMapping(value = "/getByUserIdAndType", method = RequestMethod.GET)
    UserProfileResp getByUserIdAndType(@RequestParam(value = "userId", required = true) Long userId,
                                       @RequestParam(value = "profileType", required = true) String profileType);

    @ApiOperation("保存/更新用户配置")
    @RequestMapping(value = "/saveOrUpdateUserProfile", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveOrUpdateUserProfile(@RequestBody UserProfileReq userProfileReq);
}
