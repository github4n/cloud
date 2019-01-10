package com.iot.tenant.api;

import com.iot.tenant.api.fallback.VoiceBoxConfigApiFallbackFactory;
import com.iot.tenant.vo.req.VoiceBoxConfigReq;
import com.iot.tenant.vo.resp.VoiceBoxConfigResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Api(tags = "租户智能音箱配置信息-服务接口")
@FeignClient(value = "tenant-service", fallbackFactory = VoiceBoxConfigApiFallbackFactory.class)
@RequestMapping("/voiceBoxConfig")
public interface VoiceBoxConfigApi {

    @ApiOperation("根据oauthClientId获取voiceBoxConfig信息")
    @RequestMapping(value = "/getByOauthClientId", method = RequestMethod.GET)
    VoiceBoxConfigResp getByOauthClientId(@RequestParam("oauthClientId") String oauthClientId);

    @ApiOperation("根据voiceBoxConfigReq获取voiceBoxConfig信息")
    @RequestMapping(value = "/getByVoiceBoxConfigReq", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    VoiceBoxConfigResp getByVoiceBoxConfigReq(@RequestBody VoiceBoxConfigReq voiceBoxConfigReq);
}
