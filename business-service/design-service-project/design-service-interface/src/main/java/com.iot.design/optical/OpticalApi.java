package com.iot.design.optical;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Api("光学业务接口")
@FeignClient(value = "design-service")
@RequestMapping("/optical")
public interface OpticalApi {

    /**
     * 读取文件
     *
     * @param fileId
     * @return
     */
    @ApiOperation("读取文件")
    @RequestMapping(value = "/readFile", method = RequestMethod.GET)
    public void readFile(@RequestParam("fileId") String fileId);

    @ApiOperation("添加位置指数文件到redis")
    @RequestMapping(value = "/addWZZS2Redis", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void addWZZS2Redis(@RequestBody String wzzsJSON);
}
