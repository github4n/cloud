package com.iot.building.ota.api;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iot.building.ota.api.fallback.OtaControlApiFallbackFactory;
import com.iot.common.helper.Page;
import com.iot.device.vo.req.OtaPageReq;
import com.iot.device.vo.req.ota.OtaFileInfoReq;
import com.iot.device.vo.rsp.ota.OtaFileInfoResp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("OTA接口")
@FeignClient(value = "building-control-service", fallbackFactory = OtaControlApiFallbackFactory.class)
@RequestMapping("/ota")
public interface OtaControlApi {

	@ApiOperation(value = "更新ota包")
    @RequestMapping(value = "/updateOtaVersion", method = RequestMethod.GET)
    void updateOtaVersion(@RequestParam("orgId") Long orgId, @RequestParam("deviceId") String deviceId, @RequestParam("tenantId") Long tenantId, @RequestParam("locationId") Long locationId);

	@ApiOperation("下载云端OTA文件")
    @RequestMapping(value = "/downLoadOtaFile", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void downLoadOtaFile(@RequestBody OtaFileInfoReq otaFileInfoReq);

	@ApiOperation(value = "获取OTA文件列表")
    @RequestMapping(value = "/getOtaFileList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<OtaFileInfoResp> getOtaFileList(@RequestBody OtaPageReq pageReq);
    
    @ApiOperation(value = "新增OTA文件信息")
    @RequestMapping(value = "/saveOtaFileInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int saveOtaFileInfo(@RequestBody OtaFileInfoReq otaFileInfoReq);
    
    @ApiOperation(value = "更新OTA文件信息")
    @RequestMapping(value = "/updateOtaFileInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int updateOtaFileInfo(@RequestBody OtaFileInfoReq otaFileInfoReq);

    @ApiOperation(value = "根据productId获取OTA文件信息")
    @RequestMapping(value = "/findOtaFileInfoByProductId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    OtaFileInfoResp findOtaFileInfoByProductId(@RequestBody OtaFileInfoReq otaFileInfoReq);
}
