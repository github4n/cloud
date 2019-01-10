package com.iot.shcs.ota;

import com.iot.common.helper.Page;
import com.iot.device.vo.req.ota.*;
import com.iot.device.vo.rsp.ota.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
 * @Author: nongchongwei
 * @Descrpiton:
 * @Date: 8:54 2018/10/24
 * @Modify by:
 */
@Api(tags = "ota接口")
@FeignClient(value = "smart-home-service")
@RequestMapping("/ota")
public interface OtaSmartHomeApi {

    @ApiOperation(value="启动计划，通知设备升级", notes="启动计划，通知设备升级")
    @ApiImplicitParams({ @ApiImplicitParam(name = "productId", value = "产品Id",paramType = "query", required = true,dataType = "Long")})
    @RequestMapping(value = "/noticeDevice", method = {RequestMethod.POST})
    void noticeDevice(@RequestParam("productId") Long productId, @RequestParam("tenantId") Long tenantId, @RequestParam("userId") Long userId);

    @ApiOperation(value = "通过产品id查询升级日志")
    @RequestMapping(value = "/getUpgradeLog", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<UpgradeLogResp> getUpgradeLog(@RequestBody UpgradeLogReq upgradeLogReq);

    @ApiOperation(value = "计划操作记录分页查询")
    @RequestMapping(value = "/getUpgradePlanLog", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<UpgradePlanLogResp> getUpgradePlanLog(@RequestBody UpgradePlanReq upgradePlanReq);

    @ApiOperation("查询OTA版本合法性（唯一性）")
    @RequestMapping(value = "/checkVersionLegality", method = RequestMethod.GET)
    Boolean checkVersionLegality(@RequestParam(value = "tenantId") Long tenantId, @RequestParam(value = "prodId") Long prodId, @RequestParam(value = "otaVersion") String otaVersion);

    @ApiOperation("依据产品ID查询升级版本列表")
    @RequestMapping(value = "/getFirmwareVersionListByProductId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<FirmwareVersionResp> getFirmwareVersionListByProductId(@RequestBody FirmwareVersionSearchReqDto dto);

    @ApiOperation(value = "新增OTA升级版本信息")
    @RequestMapping(value = "/createFirmwareVersion", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int createFirmwareVersion(@RequestBody FirmwareVersionReqDto dto);

    @ApiOperation(value = "新增OTA升级版本信息,一创建便上线，仅用于发布产品时使用")
    @RequestMapping(value = "/initFirmwareVersion", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void initFirmwareVersion(@RequestBody FirmwareVersionDto dto);

    @ApiOperation("初始固件查询")
    @RequestMapping(value = "/getInitOTAVersionInfoListByProductId", method = RequestMethod.GET)
    FirmwareVersionQueryResp getInitOTAVersionInfoListByProductId(@RequestParam(value = "tenantId") Long tenantId, @RequestParam(value = "productId") Long productId);

    @ApiOperation(value = "升级计划查询")
    @RequestMapping(value = "/getUpgradePlan", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    UpgradePlanResp getUpgradePlan(@RequestBody UpgradePlanReq upgradePlanReq);

    @ApiOperation("修改升级计划状态（启动/暂停）")
    @RequestMapping(value = "/operatePlan", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void operatePlan(@RequestBody UpgradePlanOperateReq upgradePlanOperateReq);

    @ApiOperation("升级计划修改")
    @RequestMapping(value = "/editUpgradePlan", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int editUpgradePlan(@RequestBody UpgradePlanEditReq upgradePlanEditReq);

    @ApiOperation("依据产品ID获取升级版本列表")
    @RequestMapping(value = "/getOTAVersionListByProductId", method = RequestMethod.GET)
    List<String> getOTAVersionListByProductId(@RequestParam(value = "tenantId") Long tenantId, @RequestParam(value = "productId") Long productId);

    @ApiOperation("依据产品ID获取版分页查询")
    @RequestMapping(value = "/getOTAVersionPageByProductId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<FirmwareVersionPageResp> getOTAVersionPageByProductId(@RequestBody FirmwareVersionSearchReqDto req);

    @ApiOperation("获取版本使用百分比")
    @RequestMapping(value = "/getVersionPercent", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Map<String,String> getVersionPercent(@RequestBody VersionPercentReq versionPercentReq);

    @ApiOperation("根据产品model查升级计划")
    @RequestMapping(value = "/getUpgradePlanByProductModel", method = RequestMethod.GET)
    UpgradePlanResp getUpgradePlanByProductModel(@RequestParam(value = "model") String model);

    @ApiOperation("根据产品model获取固件下载url")
    @RequestMapping(value = "/getFirmwareUrlByModel", method = RequestMethod.GET)
    Map<String,String>  getFirmwareUrlByModel(@RequestParam(value = "model") String model, @RequestParam(value = "version") String version);

    @ApiOperation("删除固件")
    @RequestMapping(value = "/deleteByFirmwareId", method = RequestMethod.GET)
    int deleteByFirmwareId(@RequestParam(value = "id") Long id);

}
