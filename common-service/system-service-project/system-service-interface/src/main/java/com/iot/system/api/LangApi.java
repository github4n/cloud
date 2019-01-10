package com.iot.system.api;

import java.util.Collection;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iot.system.api.fallback.LangApiFallbackFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("国际化接口")
@FeignClient(value = "system-service", fallbackFactory = LangApiFallbackFactory.class)
@RequestMapping("/api/lang")
public interface LangApi {

    /**
     * 
     * 描述：通过key获取对应语言信息（如果没有value，返回key值）
     * @author 李帅
     * @created 2018年7月11日 下午4:16:10
     * @since 
     * @param keys
     * @param lang
     * @return
     */
    @ApiOperation("通过key获取对应语言信息")
    @RequestMapping(value = "/getLangValueByKey", method = RequestMethod.POST)
    Map<String, String> getLangValueByKey(@RequestParam("keys") Collection<String> keys, @RequestParam("lang") String lang );

}
