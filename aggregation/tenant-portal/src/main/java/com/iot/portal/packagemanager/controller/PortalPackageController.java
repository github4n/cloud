package com.iot.portal.packagemanager.controller;

import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.control.packagemanager.vo.req.rule.SaveRuleInfoReq;
import com.iot.control.packagemanager.vo.req.scene.SaveSceneInfoReq;
import com.iot.portal.packagemanager.service.IPackageCreateService;
import com.iot.portal.packagemanager.service.IPackageRuleService;
import com.iot.portal.packagemanager.service.IPackageSceneService;
import com.iot.portal.packagemanager.service.PackageInfoService;
import com.iot.portal.packagemanager.vo.req.CreatePackageReq;
import com.iot.portal.packagemanager.vo.req.SavePackageBaseInfoReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
  * @despriction：portal套包管理
  * @author  yeshiyuan
  * @created 2018/11/24 9:46
  */
@Api(tags = "portal套包管理", description = "portal套包管理")
@RestController
@RequestMapping(value = "/portal/package")
public class PortalPackageController {

    @Autowired
    private PackageInfoService packageInfoService;

    @Autowired
    private IPackageRuleService packageRuleService;

    @Autowired
    private IPackageSceneService packageSceneService;

    @Autowired
    private IPackageCreateService packageCreateService;

    /**
      * @despriction：创建套包
      * @author  yeshiyuan
      * @created 2018/11/24 9:53
      */
    @LoginRequired
    @ApiOperation(value = "创建套包", notes = "创建套包")
    @RequestMapping(value = "/createPackage", method = RequestMethod.POST)
    public CommonResponse createPackage(@RequestBody CreatePackageReq packageReq) {
        return CommonResponse.success(packageCreateService.createPackage(packageReq));
    }

    /**
      * @despriction：加载套包类型
      * @author  yeshiyuan
      * @created 2018/12/7 10:39
      */
    @LoginRequired
    @ApiOperation(value = "加载套包类型", notes = "加载套包类型")
    @RequestMapping(value = "/loadPackageType", method = RequestMethod.GET)
    public CommonResponse loadPackageType() {
        return CommonResponse.success(packageInfoService.loadPackageType());
    }

    /**
     * @despriction：根据ifttt属性查找产品的功能模组信息
     * @author  yeshiyuan
     * @created 2018/11/22 21:07
     */
    @ApiOperation(value = "根据ifttt属性查找产品的功能模组信息", notes = "根据ifttt属性查找产品的功能模组信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "产品id", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "iftttType", value = "ifttt类型（if：支持if；then：支持then）", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/getProductModuleDetail", method = RequestMethod.GET)
    public CommonResponse getProductModuleDetail(Long productId, String iftttType) {
        return CommonResponse.success(packageInfoService.getProductModuleDetail(productId, iftttType));
    }

    /**
      * @despriction：保存场景配置
      * @author  yeshiyuan
      * @created 2018/12/10 14:00
      */
    @LoginRequired
    @ApiOperation(value = "保存场景配置", notes = "保存场景配置")
    @RequestMapping(value = "/saveSceneConfig", method = RequestMethod.POST)
    public CommonResponse saveSceneConfig(@RequestBody SaveSceneInfoReq saveReq) {
        packageSceneService.saveSceneConfig(saveReq);
        return CommonResponse.success();
    }

    /**
      * @despriction：删除场景
      * @author  yeshiyuan
      * @created 2018/12/10 19:00
      */
    @LoginRequired
    @ApiOperation(value = "删除场景", notes = "删除场景")
    @RequestMapping(value = "/deleteScene", method = RequestMethod.POST)
    public CommonResponse deleteScene(Long sceneId) {
        packageSceneService.deleteScene(sceneId);
        return CommonResponse.success();
    }

    /**
      * @despriction：保存策略配置
      * @author  yeshiyuan
      * @created 2018/12/10 14:34
      */
    @ApiOperation(value = "保存策略配置", notes = "保存策略配置")
    @RequestMapping(value = "/saveRuleConfig", method = RequestMethod.POST)
    public CommonResponse saveRuleConfig(@RequestBody SaveRuleInfoReq saveRuleInfoReq) {
        packageRuleService.saveRule(saveRuleInfoReq);
        return CommonResponse.success();
    }
    /**
      * @despriction：删除策略
      * @author  yeshiyuan
      * @created 2018/12/11 15:06
      */
    @ApiOperation(value = "删除策略", notes = "删除策略")
    @RequestMapping(value = "/deleteRule", method = RequestMethod.POST)
    public CommonResponse deleteRule(Long ruleId) {
        packageRuleService.delete(ruleId);
        return CommonResponse.success();
    }

    /**
      * @despriction：删除套包
      * @author  yeshiyuan
      * @created 2018/12/10 19:26
      */
    @ApiOperation(value = "删除套包")
    @RequestMapping(value = "/deletePackage", method = RequestMethod.POST)
    public CommonResponse deletePackage(@RequestParam("packageId") Long packageId) {
        packageInfoService.deletePackage(packageId);
        return CommonResponse.success();
    }
    
    /**
     * 
     * 描述：查询套包产品
     * @author 李帅
     * @created 2018年12月10日 下午8:55:11
     * @since 
     * @return
     */
    @ApiOperation(value = "查询套包产品")
    @RequestMapping(value = "/getPackageProducts", method = RequestMethod.GET)
    public CommonResponse getPackageProducts() {
        return CommonResponse.success(packageInfoService.getPackageProducts());
    }
    
    /**
     * 
     * 描述：查询网管子产品
     * @author 李帅
     * @created 2018年12月10日 下午8:55:44
     * @since 
     * @param productId
     * @return
     */
    @ApiOperation(value = "查询网管子产品")
    @ApiImplicitParam(name = "productId", value = "产品ID", dataType = "Long", paramType = "query")
    @RequestMapping(value = "/getGatewayChildProducts", method = RequestMethod.GET)
    public CommonResponse getGatewayChildProducts(@RequestParam("productId") Long productId) {
        return CommonResponse.success(packageInfoService.getGatewayChildProducts(productId));
    }

    /**
     *@description 根据套包id、租户id获取套包下的策略列表信息
     *@author wucheng
     *@params [packageId]
     *@create 2018/12/12 9:36
     *@return com.iot.common.beans.CommonResponse
     */
    @ApiOperation(value = "根据套包id查询策略列表信息")
    @ApiImplicitParam(name = "packageId", value = "套包ID", dataType = "Long", paramType = "query")
    @RequestMapping(value = "/getRuleList", method = RequestMethod.GET)
    public CommonResponse getRuleList(@RequestParam("packageId") Long packageId) {
        return  CommonResponse.success(packageRuleService.getRuleList(packageId));
    }
    /**
     *@description 根据套包id、租户id获取套包下的场景列表信息
     *@author wucheng
     *@params [packageId]
     *@create 2018/12/12 9:46
     *@return com.iot.common.beans.CommonResponse
     */
    @ApiOperation(value = "根据套包id查询场景列表信息")
    @ApiImplicitParam(name = "packageId", value = "套包ID", dataType = "Long", paramType = "query")
    @RequestMapping(value = "/getSceneList", method = RequestMethod.GET)
    public CommonResponse  getSceneList(@RequestParam("packageId") Long packageId) {
        return  CommonResponse.success(packageSceneService.getSceneListByPackageIdAndTenantId(packageId));
    }


    /**
     *@description 根据套包id获取套包详细信息
     *@author wucheng
     *@params [packageId]
     *@create 2018/12/12 9:46
     *@return com.iot.common.beans.CommonResponse
     */
    @ApiOperation(value = "根据套包id查询boss套包详细信息")
    @ApiImplicitParam(name = "packageId", value = "套包ID", dataType = "Long", paramType = "query")
    @RequestMapping(value = "/getBossPackageInfo", method = RequestMethod.GET)
    public CommonResponse  getBossPackageInfo(@RequestParam("packageId") Long packageId) {
        return  CommonResponse.success(packageInfoService.getBossPackageInfo(packageId));
    }

    /**
     *@description 根据套包id查看套包策略详细信息
     *@author wucheng
     *@params [id]
     *@create 2018/12/12 15:22
     *@return com.iot.common.beans.CommonResponse
     */
   /* @ApiOperation(value = "根据套包id查看套包策略详细信息")
    @ApiImplicitParam(name = "packageId", value = "套包id", dataType = "Long", paramType = "query")
    @RequestMapping(value = "/getTemplateRuleListByPackageId", method = RequestMethod.GET)
    public CommonResponse getTemplateRuleByPackageId(@RequestParam("packageId") Long packageId) {
        return  CommonResponse.success(packageRuleService.getTemplateRuleByPackageId(packageId));
    }*/

    /**
     *@description 根据策略id查看策略详细信息
     *@author wucheng
     *@params [id]
     *@create 2018/12/14 11:27
     *@return com.iot.common.beans.CommonResponse
     */
    @ApiOperation(value = "根据策略id查看策略详细信息")
    @ApiImplicitParam(name = "id", value = "策略id", dataType = "Long", paramType = "query")
    @RequestMapping(value = "/getTemplateRuleDetailById", method = RequestMethod.GET)
    public CommonResponse getTemplateRuleDetailById(@RequestParam("id") Long id) {
        return  CommonResponse.success(packageRuleService.getTemplateRuleDetailInfoById(id));
    }

    /**
     *@description 根据场景id查看场景详细信息
     *@author wucheng
     *@params [id]
     *@create 2018/12/12 15:34
     *@return com.iot.common.beans.CommonResponse
     */
    @ApiOperation(value = "根据场景id查看套包场景详细信息")
    @ApiImplicitParam(name = "id", value = "场景id", dataType = "Long", paramType = "query")
    @RequestMapping(value = "/getSceneInfoDetailById", method = RequestMethod.GET)
    public CommonResponse getSceneInfoDetailById(@RequestParam("id") Long id) {
        return  CommonResponse.success(packageSceneService.getSceneInfoDetailById(id));
    }

    /**
     *@description 获取所有的boss创建的套包模板信息
     *@author wucheng
     *@params []
     *@create 2018/12/13 16:14
     *@return com.iot.common.beans.CommonResponse
     */
    @ApiOperation(value = "获取所有Boss创建的套包模板信息")
    @RequestMapping(value = "/getBossPackageList", method = RequestMethod.GET)
    public CommonResponse getBossPackageList() {
        return CommonResponse.success(packageInfoService.getBossPackageList());
    }

    /**
      * @despriction：保存套包基本信息
      * @author  yeshiyuan
      * @created 2018/12/17 9:48
      * @params []
      * @return com.iot.common.beans.CommonResponse
      */
    @ApiOperation(value = "savePackageBaseInfo")
    @RequestMapping(value = "/savePackageBaseInfo", method = RequestMethod.POST)
    public CommonResponse savePackageBaseInfo(@RequestBody SavePackageBaseInfoReq savePackageBaseInfoReq) {
        packageInfoService.savePackageBaseInfo(savePackageBaseInfoReq);
        return CommonResponse.success();
    }

    /**
      * @despriction：根据ifttt类型过滤加载套包下的产品
      * @author  yeshiyuan
      * @created 2018/12/17 10:27
      * @params [packageId, iftttType]
      * @return com.iot.common.beans.CommonResponse
      */
    @ApiOperation(value = "根据ifttt类型过滤加载套包下的产品", notes = "根据ifttt类型过滤加载套包下的产品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "packageId", value = "套包id", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "iftttType", value = "ifttt类型（if：支持if/ifThen；then：支持then/ifthen）", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/getProductByIftttType", method = RequestMethod.GET)
    public CommonResponse getProductByIftttType(@RequestParam("packageId") Long packageId, @RequestParam("iftttType") String iftttType) {
        return CommonResponse.success(packageInfoService.getProductByIftttType(packageId, iftttType));
    }
}
