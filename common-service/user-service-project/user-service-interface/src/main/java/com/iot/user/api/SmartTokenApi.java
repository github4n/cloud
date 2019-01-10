package com.iot.user.api;

import com.iot.user.vo.SmartTokenResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/19 8:39
 * @Modify by:
 */

@Api("smartToken接口")
@FeignClient(value = "user-service", fallbackFactory = UserApiFallbackFactory.class)
@RequestMapping("/smartToken")
public interface SmartTokenApi {
    @ApiOperation("根据 userId、smart 获取一个 SmartTokenResp")
    @RequestMapping(value = "/getSmartTokenByUserIdAndSmart", method = RequestMethod.GET)
    SmartTokenResp getSmartTokenByUserIdAndSmart(@RequestParam("userId") Long userId, @RequestParam("smart") Integer smart);

    @ApiOperation("根据 userId、smartType 删除数据")
    @RequestMapping(value = "/deleteSmartTokenByUserIdAndSmart", method = RequestMethod.GET)
    void deleteSmartTokenByUserIdAndSmart(@RequestParam("userId") Long userId, @RequestParam("smart") Integer smart);

    @ApiOperation("根据 userId、localAccessToken 删除数据")
    @RequestMapping(value = "/deleteByUserIdAndLocalAccessToken", method = RequestMethod.GET)
    void deleteByUserIdAndLocalAccessToken(@RequestParam("userId") Long userId, @RequestParam("localAccessToken") String localAccessToken);

    @ApiOperation("获取用户所有的 smartToken")
    @RequestMapping(value = "/findSmartTokenListByUserId", method = RequestMethod.GET)
    List<SmartTokenResp> findSmartTokenListByUserId(@RequestParam("userId") Long userId);

    @ApiOperation("根据 userId 获取 third_party_info_id 不为空的记录")
    @RequestMapping(value = "/findThirdPartyInfoIdNotNull", method = RequestMethod.GET)
    List<SmartTokenResp> findThirdPartyInfoIdNotNull(@RequestParam("userId") Long userId);

    @ApiOperation("获取用户对应Alexa的toekn")
    @RequestMapping(value = "/getAlexaSmartTokenByUserId", method = RequestMethod.GET)
    SmartTokenResp getAlexaSmartTokenByUserId(@RequestParam("userId") Long userId);

    @ApiOperation("存储用户alexa令牌")
    @RequestMapping(value = "/saveTokenForAlexa", method = RequestMethod.GET)
    boolean saveTokenForAlexa(@RequestParam("code") String code, @RequestParam("userId") Long userId);

    @ApiOperation("根据 本地生成的access_token 获取记录")
    @RequestMapping(value = "/getByLocalAccessToken", method = RequestMethod.GET)
    public SmartTokenResp getByLocalAccessToken(@RequestParam("localAccessToken") String localAccessToken);
}
