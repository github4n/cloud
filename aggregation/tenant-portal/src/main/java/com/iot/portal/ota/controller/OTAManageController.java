package com.iot.portal.ota.controller;

import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.device.vo.req.ota.StrategyConfigDto;
import com.iot.device.vo.req.ota.StrategyReportSearchReqDto;
import com.iot.portal.ota.service.OTAManageService;
import com.iot.portal.ota.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 项目名称：IOT云平台
 * 模块名称：聚合层
 * 功能描述：OTA控制层
 * 创建人： maochengyuan
 * 创建时间：2018/7/24 15:20
 * 修改人： maochengyuan
 * 修改时间：2018/7/24 15:20
 * 修改描述：
 */
@Api(description = "OTA管理")
@RestController
@RequestMapping("/OTAManageController")
public class OTAManageController {

    @Autowired
    private OTAManageService otaManageService;

    /**
     * 描述：查询升级日志
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradeLogSearchReq
     * @return
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查询升级日志", notes = "查询升级日志")
    @RequestMapping(value = "/getUpgradeLog", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getUpgradeLog(@RequestBody UpgradeLogSearchReq upgradeLogSearchReq) {
        return ResultMsg.SUCCESS.info(this.otaManageService.getUpgradeLog(upgradeLogSearchReq));
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "新增OTA升级版本信息", notes = "新增OTA升级版本信息")
    @RequestMapping(value = "/createFirmwareVersion", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse createFirmwareVersion(@RequestBody FirmwareVersionReq req) {
        this.otaManageService.createFirmwareVersion(req);
        return ResultMsg.SUCCESS.info();
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "新增OTA升级版本信息", notes = "新增OTA升级版本信息")
    @RequestMapping(value = "/initFirmwareVersion", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse initFirmwareVersion(@RequestBody FirmwareVersionReq req) {
        this.otaManageService.initFirmwareVersion(req);
        return ResultMsg.SUCCESS.info();
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation("初始固件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "产品id", required = true, paramType = "query", dataType = "String"),
    })
    @RequestMapping(value = "/getInitOTAVersionInfoListByProductId", method = RequestMethod.GET)
    public CommonResponse getInitOTAVersionInfoListByProductId( @RequestParam(value = "productId") Long productId){
        return ResultMsg.SUCCESS.info(this.otaManageService.getInitOTAVersionInfoListByProductId(productId));
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation("保存策略配置")
    @RequestMapping(value = "/saveStrategyConfig", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveStrategyConfig(@RequestBody StrategyConfigDto strategyConfigDto){
        return ResultMsg.SUCCESS.info(this.otaManageService.saveStrategyConfig(strategyConfigDto));
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation("获取策略配置")
    @RequestMapping(value = "/getStrategyConfig", method = RequestMethod.GET)
    public CommonResponse getStrategyConfig(@RequestParam(value = "productId") String productId){
        return ResultMsg.SUCCESS.info(this.otaManageService.getStrategyConfig(productId));
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation("获取容灾报告")
    @RequestMapping(value = "/getStrategyReport", method = RequestMethod.GET)
    public CommonResponse getStrategyReport(@RequestParam(value = "productId") String productId){
        return ResultMsg.SUCCESS.info(this.otaManageService.getStrategyReport(productId));
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation("分页获取容灾报告详情")
    @RequestMapping(value = "/selectStrategyReportByGroupAsPage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse selectStrategyReportByGroupAsPage(@RequestBody StrategyReportSearchReqDto dto){
        return ResultMsg.SUCCESS.info(this.otaManageService.selectStrategyReportByGroupAsPage(dto));
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "上传OTA升级包", notes = "上传OTA升级包")
    @RequestMapping(value = "/uploadFirmwareOtaFile", method = RequestMethod.POST)
    public CommonResponse uploadFirmwareOtaFile(MultipartHttpServletRequest file) {
        return ResultMsg.SUCCESS.info(this.otaManageService.uploadFirmwareOtaFile(file));
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "依据产品ID查询升级版本列表", notes = "依据产品ID查询升级版本列表")
    @RequestMapping(value = "/getFirmwareVersionListByProductId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getFirmwareVersionListByProductId(@RequestBody FirmwareVersionSearchReq req) {
        return ResultMsg.SUCCESS.info(this.otaManageService.getFirmwareVersionListByProductId(req));
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查询OTA版本合法性（唯一性）", notes = "查询OTA版本合法性（唯一性）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "产品id", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "otaVersion", value = "OTA版本", required = true, paramType = "query", dataType = "String"),
    })
    @RequestMapping(value = "/checkVersionLegality", method = RequestMethod.GET)
    public CommonResponse checkVersionLegality(@RequestParam("productId") String productId, @RequestParam("otaVersion") String otaVersion) {
        Boolean resp = this.otaManageService.checkVersionLegality(productId, otaVersion);
        return ResultMsg.SUCCESS.info(resp);
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查询OTA版本合法性（唯一性）", notes = "查询OTA版本合法性（唯一性）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "产品id", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "otaVersion", value = "OTA版本", required = true, paramType = "query", dataType = "String"),
    })
    @RequestMapping(value = "/checkVersionLegalityByProId", method = RequestMethod.GET)
    public CommonResponse checkVersionLegalityByProId(@RequestParam("productId") String productId, @RequestParam("otaVersion") String otaVersion) {
        Boolean resp = this.otaManageService.checkVersionLegalityByProId(productId, otaVersion);
        return ResultMsg.SUCCESS.info(resp);
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "计划操作记录分页查询", notes = "计划操作记录分页查询")
    @RequestMapping(value = "/getUpgradePlanLog", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getUpgradePlanLog(@RequestBody UpgradePlanSearchReq upgradePlanSearchReq) {
        return ResultMsg.SUCCESS.info(this.otaManageService.getUpgradePlanLog(upgradePlanSearchReq));
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "产品分页查询", notes = "计产品分页查询")
    @RequestMapping(value = "/getProduct", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getProduct(@RequestBody ProductOtaSearchReq productOtaSearchReq) {
        return ResultMsg.SUCCESS.info(this.otaManageService.getProduct(productOtaSearchReq));
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "升级计划查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "产品id", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/getUpgradePlan", method = RequestMethod.GET)
    public CommonResponse getUpgradePlan(@RequestParam("productId") String productId){
        return ResultMsg.SUCCESS.info(this.otaManageService.getUpgradePlan(productId));
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation("修改升级计划状态（启动 Start /暂停 Pause）")
    @RequestMapping(value = "/operatePlan", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse operatePlan(@RequestBody UpgradePlanUpdateReq upgradePlanUpdateReq){
        this.otaManageService.operatePlan(upgradePlanUpdateReq);
        return ResultMsg.SUCCESS.info();
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation("依据产品ID获取升级版本列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "产品id", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/getOTAVersionListByProductId", method = RequestMethod.GET)
    public CommonResponse getOTAVersionListByProductId(@RequestParam("productId") String productId){
        return ResultMsg.SUCCESS.info(this.otaManageService.getOTAVersionListByProductId(productId));
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation("依据产品ID获取版分页查询")
    @RequestMapping(value = "/getOTAVersionPageByProductId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getOTAVersionPageByProductId(@RequestBody FirmwareVersionSearchReq req){
        return ResultMsg.SUCCESS.info(this.otaManageService.getOTAVersionPageByProductId(req));
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation("升级计划修改")
    @RequestMapping(value = "/editUpgradePlan", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse editUpgradePlan(@RequestBody UpgradePlanVo upgradePlanVo){
        this.otaManageService.editUpgradePlan(upgradePlanVo);
        return ResultMsg.SUCCESS.info();
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation("获取版本使用百分比")
    @RequestMapping(value = "/getVersionPercent", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getVersionPercent(@RequestBody PercentReq percentReq){
        return ResultMsg.SUCCESS.info(this.otaManageService.getVersionPercent(percentReq));
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation("删除固件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "firmwareId", value = "固件id", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/deleteByFirmwareId", method = RequestMethod.GET)
    public CommonResponse deleteByFirmwareId(@RequestParam("firmwareId") String firmwareId){
        this.otaManageService.deleteByFirmwareId(firmwareId);
        return ResultMsg.SUCCESS.info();
    }
}
