package com.iot.device.api;

import com.iot.common.helper.Page;
import com.iot.device.api.fallback.OTAServiceApiFallbackFactory;
import com.iot.device.vo.req.OtaPageReq;
import com.iot.device.vo.req.ota.*;
import com.iot.device.vo.req.ota.FirmwareVersionDto;
import com.iot.device.vo.rsp.ota.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: nongchongwei
 * @Descrpiton:
 * @Date: 14:17 2018/10/23
 * @Modify by:
 */
@Api(tags = "ota接口")
@FeignClient(value = "device-service", fallbackFactory = OTAServiceApiFallbackFactory.class)
@RequestMapping(value = "/ota")
public interface OTAServiceApi {
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

    @ApiOperation(value = "升级计划查询")
    @RequestMapping(value = "/getUpgradePlan", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    UpgradePlanResp getUpgradePlan(@RequestBody UpgradePlanReq upgradePlanReq);

    @ApiOperation(value = "升级计划查询")
    @RequestMapping(value = "/getUpgradePlanByProductId", method = RequestMethod.GET)
    UpgradePlanResp getUpgradePlanByProductId(@RequestParam(value = "productId") Long productId);

    @ApiOperation(value = "升级计划批量查询")
    @RequestMapping(value = "/getUpgradePlanByBathProductId", method = RequestMethod.GET)
    Map<Long,UpgradePlanResp> getUpgradePlanByBathProductId(@RequestParam(value = "productIdList") List<Long> productIdList);

    @ApiOperation("更新计划状态")
    @RequestMapping(value = "/updatePlanStatus", method = RequestMethod.GET)
    int updatePlanStatus(@RequestParam(value = "id") Long id, @RequestParam(value = "planStatus") String planStatus);

    @ApiOperation("更新启动时间")
    @RequestMapping(value = "/updatePlanStartTime", method = RequestMethod.GET)
    int updatePlanStartTime(@RequestParam(value = "id") Long id, @RequestParam(value = "startTime") Date startTime);

    @ApiOperation("查询子设备信息")
    @RequestMapping(value = "/getSubOtaDeviceInfo", method = RequestMethod.GET)
    List<SubOtaDeviceInfo> getSubOtaDeviceInfo(@RequestParam(value = "productId") Long productId, @RequestParam(value = "versionList") List<String> versionList);

    @ApiOperation("查询直连设备信息")
    @RequestMapping(value = "/getDirectForceOta", method = RequestMethod.GET)
    List<ForceOtaDevInfo> getDirectForceOta(@RequestParam(value = "productId") Long productId, @RequestParam(value = "versionList") List<String> versionList );

    @ApiOperation("根据产品id和版本列表查询设备信息")
    @RequestMapping(value = "/getOtaDeviceInfo", method = RequestMethod.GET)
    List<OtaDeviceInfo> getOtaDeviceInfo(@RequestParam(value = "productId") Long productId, @RequestParam(value = "versionList") List<String> versionList);

    @ApiOperation("更新固件版本上线时间")
    @RequestMapping(value = "/updateVersionOnlineTime", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int updateVersionOnlineTime(@RequestBody FirmwareVersionUpdateVersionDto dto);

    @ApiOperation("更新固件版本上线时间,用于产品审核通过时")
    @RequestMapping(value = "/updateVersionOnlineTimeNoVersion", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int updateVersionOnlineTimeNoVersion(@RequestBody FirmwareVersionUpdateVersionDto dto);

    @ApiOperation("更新固件版本上线时间，无租户")
    @RequestMapping(value = "/updateVersionOnlineTimeByProductId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int updateVersionOnlineTimeByProductId(@RequestBody FirmwareVersionUpdateVersionDto dto);

    @ApiOperation("固件查询")
    @RequestMapping(value = "/getAllOTAVersionInfoListByProductId", method = RequestMethod.GET)
    List<FirmwareVersionQueryResp> getAllOTAVersionInfoListByProductId(@RequestParam(value = "tenantId") Long tenantId, @RequestParam(value = "productId") Long productId);

    @ApiOperation("初始固件查询")
    @RequestMapping(value = "/getInitOTAVersionInfoListByProductId", method = RequestMethod.GET)
    List<FirmwareVersionQueryResp> getInitOTAVersionInfoListByProductId(@RequestParam(value = "tenantId") Long tenantId, @RequestParam(value = "productId") Long productId);

    @ApiOperation("记录操作日志")
    @RequestMapping(value = "/recordUpgradePlanLog", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void recordUpgradePlanLog(@RequestBody UpgradePlanOperateReq upgradePlanOperateReq);

    @ApiOperation("升级计划修改")
    @RequestMapping(value = "/editUpgradePlan", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int editUpgradePlan(@RequestBody UpgradePlanEditReq upgradePlanEditReq);

    @ApiOperation("依据产品ID获取升级版本列表")
    @RequestMapping(value = "/getOTAVersionListByProductId", method = RequestMethod.GET)
    List<String> getOTAVersionListByProductId(@RequestParam(value = "tenantId") Long tenantId, @RequestParam(value = "productId") Long productId);

    @ApiOperation("依据产品ID获取版分页查询")
    @RequestMapping(value = "/getOTAVersionPageByProductId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<FirmwareVersionPageResp> getOTAVersionPageByProductId(@RequestBody FirmwareVersionSearchReqDto req);

    @ApiOperation("查询下一个版本信息")
    @RequestMapping(value = "/getNextVersion", method = RequestMethod.GET)
    UpgradeInfoResp getNextVersion(@RequestParam(value = "productId") Long productId, @RequestParam(value = "currentVersion") String currentVersion);

    @ApiOperation("查询下一个版本信息map")
    @RequestMapping(value = "/getNextVersionMap", method = RequestMethod.GET)
    Map<String,UpgradeInfoResp> getNextVersionMap(@RequestParam(value = "productIdVersionList") List<String> productIdVersionList);

    @ApiOperation("批量查询升级版本信息map")
    @RequestMapping(value = "/getUpgradeInfoRespMap", method = RequestMethod.GET)
    Map<String,UpgradeInfoResp> getUpgradeInfoRespMap(@RequestParam(value = "productId") Long productId, @RequestParam(value = "versionList") List<String> versionList);

    @ApiOperation("批量插入设备版本信息")
    @RequestMapping(value = "/batchInsertUpgradeDeviceVersion", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void batchInsertUpgradeDeviceVersion(@RequestBody BatchIUpgradeDeviceVersion batchIUpgradeDeviceVersion);

    @ApiOperation("获取一级设备强制升级设备列表")
    @RequestMapping(value = "/getDirForceOtaList", method = RequestMethod.GET)
    List<ForceOtaDevInfo> getDirForceOtaList(@RequestParam(value = "productId") Long productId, @RequestParam(value = "sta") int sta);

    @ApiOperation("获取二级设备强制升级设备列表")
    @RequestMapping(value = "/getSubForceOtaList", method = RequestMethod.GET)
    List<SubForceOta> getSubForceOtaList(@RequestParam(value = "productId") Long productId, @RequestParam(value = "sta") int sta);

    @ApiOperation("获取推送升级设备列表")
    @RequestMapping(value = "/getPushOtaList", method = RequestMethod.GET)
    List<PushOta> getPushOtaList(@RequestParam(value = "productId") Long productId, @RequestParam(value = "sta") int sta);

    @ApiOperation("获取一级设备策略升级设备列表")
    @RequestMapping(value = "/getDirForceStrategyOtaList", method = RequestMethod.GET)
    List<ForceOtaDevInfo> getDirForceStrategyOtaList(@RequestParam(value = "planId") Long planId, @RequestParam(value = "group") Integer group, @RequestParam(value = "sta") int sta);

    @ApiOperation("获取二级设备策略升级设备列表")
    @RequestMapping(value = "/getSubForceStrategyOtaList", method = RequestMethod.GET)
    List<SubForceOta> getSubForceStrategyOtaList(@RequestParam(value = "planId") Long planId, @RequestParam(value = "group") Integer group, @RequestParam(value = "sta") int sta);

    @ApiOperation("获取推送策略升级设备列表")
    @RequestMapping(value = "/getPushStrategyOtaList", method = RequestMethod.GET)
    List<PushOta> getPushStrategyOtaList(@RequestParam(value = "planId") Long planId, @RequestParam(value = "group") Integer group, @RequestParam(value = "sta") int sta);

    @ApiOperation("获取计划版本列表")
    @RequestMapping(value = "/getVersionByProductId", method = RequestMethod.GET)
    List<String> getVersionByProductId(@RequestParam(value = "productId") Long productId);

    @ApiOperation("缓存升级结果")
    @RequestMapping(value = "/cacheUpgradeLog", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void cacheUpgradeLog(@RequestBody UpgradeLogAddReq req);

    @ApiOperation("获取升级缓存")
    @RequestMapping(value = "/getCacheUpgradeLog", method = RequestMethod.GET)
    UpgradeLogAddReq getCacheUpgradeLog(@RequestParam(value = "deviceId") String deviceId);

    @ApiOperation("缓存升级结果")
    @RequestMapping(value = "/cacheBatchUpgradeLog", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void cacheBatchUpgradeLog(@RequestBody UpgradeLogAddBatchReq req);

    @ApiOperation("更新升级结果")
    @RequestMapping(value = "/updateUpgradeLog", method = RequestMethod.GET)
    void updateUpgradeLog(@RequestParam(value = "deviceId") String deviceId, @RequestParam(value = "upgradeResult") String upgradeResult,@RequestParam(value = "upgradeMsg")String upgradeMsg);

    @ApiOperation("更新升级结果")
    @RequestMapping(value = "/updateStrategyReport", method = RequestMethod.GET)
    void updateStrategyReport(@RequestParam(value = "deviceId") String deviceId, @RequestParam(value = "upgradeResult") String upgradeResult, @RequestParam(value = "reason") String reason);

    @ApiOperation("查询策略升级 升级失败版本")
    @RequestMapping(value = "/selectFailUpgradeVersion", method = RequestMethod.GET)
    List<String> selectFailUpgradeVersion(@RequestParam(value = "planId") Long planId);

    @ApiOperation("删除容灾报告")
    @RequestMapping(value = "/deleteStrategyReportByPlanId", method = RequestMethod.GET)
    int deleteStrategyReportByPlanId(@RequestParam(value = "planId") Long planId);

    @ApiOperation("获取容灾报告")
    @RequestMapping(value = "/getStrategyReport", method = RequestMethod.GET)
    List<StrategyReportGroupVo> getStrategyReport( @RequestParam(value = "productId") Long productId);

    @ApiOperation(value = "分页查询容灾报告详情")
    @RequestMapping(value = "/selectStrategyReportByGroupAsPage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<StrategyReportResp> selectStrategyReportByGroupAsPage(@RequestBody StrategyReportSearchReqDto dto);

    @ApiOperation("查询设备版本")
    @RequestMapping(value = "/getOtaDeviceVersion", method = RequestMethod.GET)
    String getOtaDeviceVersion(@RequestParam(value = "deviceId") String deviceId);

    @ApiOperation("获取固件下载url,供中控微服务间内部调用")
    @RequestMapping(value = "/getFirmwareUrl", method = RequestMethod.GET)
    Map<String,String>  getFirmwareUrl(@RequestParam(value = "productId") Long productId, @RequestParam(value = "version") String version);

    @ApiOperation("获取版本使用百分比")
    @RequestMapping(value = "/getVersionPercent", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Map<String,String> getVersionPercent(@RequestBody VersionPercentReq versionPercentReq);

    @ApiOperation("根据产品model查升级计划")
    @RequestMapping(value = "/getUpgradePlanByProductModel", method = RequestMethod.GET)
    UpgradePlanResp getUpgradePlanByProductModel(@RequestParam(value = "model") String model);

    @ApiOperation("根据产品model获取固件下载url")
    @RequestMapping(value = "/getFirmwareUrlByModel", method = RequestMethod.GET)
    Map<String,String>  getFirmwareUrlByModel(@RequestParam(value = "model") String model, @RequestParam(value = "version") String version);

    @ApiOperation("保存策略配置")
    @RequestMapping(value = "/saveStrategyConfig", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int saveStrategyConfig(@RequestBody StrategyConfigDto strategyConfigDto);

    @ApiOperation("获取策略配置")
    @RequestMapping(value = "/getStrategyConfig", method = RequestMethod.GET)
    List<StrategyConfigResp> getStrategyConfig(@RequestParam(value = "planId") Long planId, @RequestParam(value = "tenantId") Long tenantId);

    @ApiOperation("查询策略明细 指定uuid升级")
    @RequestMapping(value = "/getStrategyDetailUuid", method = RequestMethod.GET)
    Set<String> getStrategyDetailUuid(@RequestParam(value = "planId") Long planId);

    @ApiOperation("查询计划编辑的批次号 指定批次升级")
    @RequestMapping(value = "/selectBatchByPlanId", method = RequestMethod.GET)
    List<Long> selectBatchByPlanId(@RequestParam(value = "planId") Long planId);

    @ApiOperation("查询策略明细 批次升级")
    @RequestMapping(value = "/getStrategyDetailWithBatchNum", method = RequestMethod.GET)
    Set<String>  getStrategyDetailWithBatchNum(@RequestParam(value = "planId") Long planId);

    @ApiOperation("查询策略明细 策略升级")
    @RequestMapping(value = "/getStrategyDetailWithGroup", method = RequestMethod.GET)
    List<String> getStrategyDetailWithGroup(@RequestParam(value = "planId") Long planId,@RequestParam(value = "group") Integer group);

    @ApiOperation("查询策略明细 策略升级 查计划下的全部")
    @RequestMapping(value = "/getStrategyDetailWithPlanId", method = RequestMethod.GET)
    List<String> getStrategyDetailWithPlanId(@RequestParam(value = "planId") Long planId);

    @ApiOperation("删除策略明细")
    @RequestMapping(value = "/delStrategyDetail", method = RequestMethod.GET)
    int delStrategyDetail(@RequestParam(value = "planId") Long planId);

    @ApiOperation("保存策略明细")
    @RequestMapping(value = "/saveStrategyDetail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveStrategyDetail(@RequestBody StrategyDetailDto strategyDetailDto);

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

    @ApiOperation("删除固件")
    @RequestMapping(value = "/deleteByFirmwareId", method = RequestMethod.GET)
    int deleteByFirmwareId(@RequestParam(value = "id") Long id);
}
