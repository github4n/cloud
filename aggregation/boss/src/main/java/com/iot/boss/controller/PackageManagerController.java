package com.iot.boss.controller;

import com.iot.boss.service.packagemanager.IPackageInfoService;
import com.iot.boss.service.packagemanager.IPackageRuleService;
import com.iot.boss.service.packagemanager.IPackageSceneService;
import com.iot.boss.vo.packagemanager.req.BossSavePackageReq;
import com.iot.boss.vo.packagemanager.req.BossSearchPackageReq;
import com.iot.boss.vo.packagemanager.req.BossUpdatePackageReq;
import com.iot.common.beans.CommonResponse;
import com.iot.control.packagemanager.vo.req.SavePackageDeviceTypeReq;
import com.iot.control.packagemanager.vo.req.rule.SaveRuleInfoReq;
import com.iot.control.packagemanager.vo.req.scene.SaveSceneInfoReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
  * @despriction：套包管理ctrl
  * @author  yeshiyuan
  * @created 2018/11/21 8:53
  */
@Api(tags = "套包管理")
@RestController
@RequestMapping(value = "/api/packageManager")
public class PackageManagerController {

    @Autowired
    private IPackageInfoService packageInfoService;
    @Autowired
    private IPackageRuleService packageRuleService;
    @Autowired
    private IPackageSceneService packageSceneService;

    /**
     *@description 保存套包
     *@author wucheng
     *@params [req]
     *@create 2018/11/23 15:42
     *@return com.iot.common.beans.CommonResponse
     */
    @ApiOperation(value = "保存套包基本信息", notes = "保存套包基本信息")
    @RequestMapping(value = "/savePackageInfo", method = RequestMethod.POST)
    public CommonResponse savePackageInfo(@RequestBody BossSavePackageReq req) {
        return CommonResponse.success(packageInfoService.addPackage(req));
    }
    /**
     *@description 按packageType、tenantId分页获取套包数据列表
     *@author wucheng
     *@params [req]
     *@create 2018/11/23 15:42
     *@return com.iot.common.beans.CommonResponse
     */
    @ApiOperation(value = "分页获取套包数据列表", notes = "分页获取套包数据列表")
    @RequestMapping(value = "/getPagePackage", method = RequestMethod.POST)
    public CommonResponse getPagePackage(@RequestBody BossSearchPackageReq req) {
        return CommonResponse.success(packageInfoService.getPagePackage(req));
    }
    /**
     *@description 根据ids获取套包信息
     *@author wucheng
     *@params [ids]
     *@create 2018/11/23 15:43
     *@return com.iot.common.beans.CommonResponse
     */
    @ApiOperation(value = "根据id删除数据套包数据，多个id, 使用英文逗号隔开", notes = "根据id删除数据，多个id, 使用英文逗号隔开")
    @RequestMapping(value = "/deletePackageByIds", method = RequestMethod.POST)
    public CommonResponse deletePackageByIds(@RequestParam(value = "ids") String ids) {
        packageInfoService.deleteByIds(ids);
        return CommonResponse.success();
    }

    /**
     *@description 根据id 修改套包基本信息
     *@author wucheng
     *@params [req]
     *@create 2018/11/23 15:43
     *@return com.iot.common.beans.CommonResponse
     */
    @ApiOperation(value = "根据id修改套包信息", notes = "根据id修改套包信息")
    @RequestMapping(value = "/updatePackageById", method = RequestMethod.POST)
    public CommonResponse updatePackageById(@RequestBody BossUpdatePackageReq req) {
        return CommonResponse.success(packageInfoService.updatePackageById(req));
    }
    /**
     *@description 根据id获取套包信息
     *@author wucheng
     *@params [id]
     *@create 2018/11/23 15:44
     *@return com.iot.common.beans.CommonResponse
     */
    @ApiOperation(value = "根据id获取套包信息", notes = "根据id获取套包信息")
    @RequestMapping(value = "/getPackageById", method = RequestMethod.GET)
    public CommonResponse getPackageById(@RequestParam("id") Long id) {
        return CommonResponse.success(packageInfoService.getPackageById(id));
    }

    /**
     *@description 批量保存套包策略
     *@author wucheng
     *@params [req]
     *@create 2018/11/23 15:47
     *@return com.iot.common.beans.CommonResponse
     */
    @ApiOperation(value = "批量保存套包策略", notes = "批量保存套包策略")
    @RequestMapping(value = "/batchSaveTemplateRule", method = RequestMethod.POST)
    public CommonResponse batchSaveTemplateRule(@RequestBody List<SaveRuleInfoReq> req) {
        return CommonResponse.success(packageRuleService.batchSaveTemplateRule(req));
    }

    /**
     *@description 保存套包策略
     *@author wucheng
     *@params [req]
     *@create 2018/12/13 9:32
     *@return com.iot.common.beans.CommonResponse
     */
    @ApiOperation(value = "保存套包策略", notes = "保存套包策略")
    @RequestMapping(value = "/saveTemplateRule", method = RequestMethod.POST)
    public CommonResponse saveTemplateRule(@RequestBody SaveRuleInfoReq req) {
        return CommonResponse.success(packageRuleService.saveTemplateRule(req));
    }

    /**
      * @despriction：保存套包关联设备类型
      * @author  yeshiyuan
      * @created 2018/11/21 11:30
      */
    @ApiOperation(value = "保存套包关联设备类型", notes = "保存套包关联设备类型")
    @RequestMapping(value = "/savePackageDeviceType", method = RequestMethod.POST)
    public CommonResponse savePackageDeviceType(@RequestBody SavePackageDeviceTypeReq saveReq) {
        packageInfoService.savePackageDeviceType(saveReq);
        return CommonResponse.success();
    }

    /**
      * @despriction：查询套包支持的设备类型
      * @author  yeshiyuan
      * @created 2018/11/21 11:30
      */
    @ApiOperation(value = "查询套包支持的设备类型", notes = "查询套包支持的设备类型")
    @RequestMapping(value = "/getDeviceTypes", method = RequestMethod.GET)
    public CommonResponse getDeviceTypes(Long packageId){
        return CommonResponse.success(packageInfoService.getDeviceTypesByPackageId(packageId));
    }

    /**
      * @despriction：根据ifttt属性查找设备的功能模组信息
      * @author  yeshiyuan
      * @created 2018/11/22 21:07
      */
    @ApiOperation(value = "根据ifttt属性查找设备的功能模组信息", notes = "根据ifttt属性查找设备的功能模组信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceTypeId", value = "设备类型id", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "iftttType", value = "ifttt类型（if：支持if；then：支持then）", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/getDeviceModuleDetail", method = RequestMethod.GET)
    public CommonResponse getDeviceModuleDetail(Long deviceTypeId, String iftttType) {
        return CommonResponse.success(packageInfoService.getDeviceModuleDetail(deviceTypeId, iftttType));
    }

    /**
     * @despriction：套包下的场景查询
     * @author  nongchongwei
     * @created 2018/11/21 11:30
     */
    @ApiOperation(value = "根据套包id查询套包下的场景列表", notes = "套包下的场景查询")
    @ApiImplicitParam(name = "packageId", value = "套包id", dataType = "Long", paramType = "query")
    @RequestMapping(value = "/getSceneListByPackageId", method = RequestMethod.GET)
    public CommonResponse getSceneListByPackageId(Long packageId){
        return CommonResponse.success(packageSceneService.getSceneList(packageId));
    }

    /**
     * @despriction：根据ifttt类型过滤加载套包下的设备类型
     * @author  yeshiyuan
     * @created 2018/11/23 17:32
     */
    @ApiOperation(value = "根据ifttt类型过滤加载套包下的设备类型", notes = "根据ifttt类型过滤加载套包下的设备类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "packageId", value = "套包id", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "iftttType", value = "ifttt类型（if：支持if/ifThen；then：支持then/ifthen）", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/getDeviecTypeByIftttType", method = RequestMethod.GET)
    public CommonResponse getDeviecTypeByIftttType(Long packageId, String iftttType) {
        return CommonResponse.success(packageInfoService.getDeviecTypeByIftttType(packageId, iftttType));
    }

    /**
     *@description 批量保存套包场景
     *@author wucheng
     *@params [req]
     *@create 2018/11/23 15:47
     *@return com.iot.common.beans.CommonResponse
     */
    @ApiOperation(value = "批量保存套包场景", notes = "批量保存套包场景")
    @RequestMapping(value = "/batchSaveScene", method = RequestMethod.POST)
    public CommonResponse batchSaveScene(@RequestBody List<SaveSceneInfoReq> req) {
        return CommonResponse.success(packageSceneService.batchInsertSceneInfo(req));
    }

    /**
     *@description 保存套包场景
     *@author wucheng
     *@params [req]
     *@create 2018/12/13 9:55
     *@return com.iot.common.beans.CommonResponse
     */
    @ApiOperation(value = "保存套包场景", notes = "保存套包场景")
    @RequestMapping(value = "/saveSceneInfo", method = RequestMethod.POST)
    public CommonResponse saveSceneInfo(@RequestBody SaveSceneInfoReq req) {
        return CommonResponse.success(packageSceneService.saveSceneInfo(req));
    }

    /**
     *@description 根据场景id修改场景
     *@author wucheng
     *@params [req]
     *@create 2018/11/23 15:47
     *@return com.iot.common.beans.CommonResponse
     */
    @ApiOperation(value = "根据场景id修改场景", notes = "根据场景id修改场景")
    @RequestMapping(value = "/updateSceneById", method = RequestMethod.POST)
    public CommonResponse updateSceneById(@RequestBody SaveSceneInfoReq req) {
        return CommonResponse.success(packageSceneService.updateByPrimaryKey(req));
    }

    /**
     *@description 根据策id修改策略
     *@author wucheng
     *@params [req]
     *@create 2018/11/23 15:47
     *@return com.iot.common.beans.CommonResponse
     */
    @ApiOperation(value = "根据场景id修改场景", notes = "根据场景id修改场景")
    @RequestMapping(value = "/updateTemplateRuleById", method = RequestMethod.POST)
    public CommonResponse updateTemplateRuleById(@RequestBody SaveRuleInfoReq req) {
        return CommonResponse.success(packageRuleService.updateTemplateRuleById(req));
    }

    /**
     *@description 根据套包id,获取其绑定的策略列表信息
     *@author wucheng
     *@params [req]
     *@create 2018/11/23 15:47
     *@return com.iot.common.beans.CommonResponse
     */
    @ApiOperation(value = "根据套包id,查询策略列表信息", notes = "根据套包id,查询策略列表信息")
    @RequestMapping(value = "/getTemplateRuleListByPackageId", method = RequestMethod.POST)
    public CommonResponse getTemplateRuleListByPackageId(@RequestParam("packageId") Long packageId) {
        return CommonResponse.success(packageRuleService.getTemplateRuleListByPackageId(packageId));
    }

    /**
     *@description 根据策略id,删除策略
     *@author wucheng
     *@params [req]
     *@create 2018/11/23 15:47
     *@return com.iot.common.beans.CommonResponse
     */
    @ApiOperation(value = "根据策略id,删除策略", notes = "根据策略id,删除策略")
    @RequestMapping(value = "/deleteTemplateRuleById", method = RequestMethod.POST)
    public CommonResponse deleteTemplateRuleById(@RequestParam("id") Long id) {
        return CommonResponse.success(packageRuleService.deleteTemplateRuleById(id));
    }

    /**
     *@description 根据策略id,获取策略详情
     *@author wucheng
     *@params [req]
     *@create 2018/11/23 15:47
     *@return com.iot.common.beans.CommonResponse
     */
    @ApiOperation(value = "根据策略id,获取策略详情", notes = "根据策略id,获取策略详情")
    @RequestMapping(value = "/getTemplateRuleDetailInfoById", method = RequestMethod.POST)
    public CommonResponse getTemplateRuleDetailInfoById(@RequestParam("id") Long id) {
        return CommonResponse.success(packageRuleService.getTemplateRuleDetailInfoById(id));
    }


    /**
     *@description 根据场景id,删除场景
     *@author wucheng
     *@params [req]
     *@create 2018/11/23 15:47
     *@return com.iot.common.beans.CommonResponse
     */
    @ApiOperation(value = "根据场景id,删除场景", notes = "根据场景id,删除场景")
    @RequestMapping(value = "/deleteSceneInfoById", method = RequestMethod.POST)
    public CommonResponse deleteSceneInfoById(@RequestParam("id") Long id) {
        return CommonResponse.success(packageSceneService.deleteSceneInfoById(id));
    }


    /**
     *@description 根据场景id,获取场景详细
     *@author wucheng
     *@params [req]
     *@create 2018/11/23 15:47
     *@return com.iot.common.beans.CommonResponse
     */
    @ApiOperation(value = "根据场景id,获取场景详细", notes = "根据场景id,获取场景详细")
    @RequestMapping(value = "/getSceneInfoDetailById", method = RequestMethod.POST)
    public CommonResponse getSceneInfoDetailById(@RequestParam("id") Long id) {
        return CommonResponse.success(packageSceneService.getSceneInfoDetailById(id));
    }
}
