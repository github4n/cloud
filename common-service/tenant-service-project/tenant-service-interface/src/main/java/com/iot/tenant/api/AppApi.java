package com.iot.tenant.api;

import com.iot.common.helper.Page;
import com.iot.tenant.api.fallback.AppApiFallbackFactory;
import com.iot.tenant.vo.req.GetAppReq;
import com.iot.tenant.vo.req.GetLangReq;
import com.iot.tenant.vo.req.SaveAppProductReq;
import com.iot.tenant.vo.req.SaveAppReq;
import com.iot.tenant.vo.req.SaveGuideReq;
import com.iot.tenant.vo.req.SaveLangReq;
import com.iot.tenant.vo.req.review.AppReviewSearchReq;
import com.iot.tenant.vo.resp.AppInfoResp;
import com.iot.tenant.vo.resp.AppProductResp;
import com.iot.tenant.vo.resp.GetGuideResp;
import com.iot.tenant.vo.resp.GetLangResp;
import com.iot.tenant.vo.resp.review.AppReviewResp;
import io.swagger.annotations.Api;
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
 * 描述：App应用接口
 * 创建人： LaiGuiMing
 * 创建时间： 2018/7/5 17:28
 */
@Api(tags = "App应用接口")
@FeignClient(value = "tenant-service", fallbackFactory = AppApiFallbackFactory.class)
@RequestMapping("/app")
public interface AppApi {

    /////////////////////////////////////////应用配置////////////////////////////////////////////////

    @ApiOperation("保存app信息")
    @RequestMapping(value = "/saveApp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long saveApp(@RequestBody SaveAppReq req);

    @ApiOperation("app信息检查")
    @RequestMapping(value = "/appInfoValidation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List appInfoValidation(@RequestBody SaveAppReq req);

    @ApiOperation("复制app信息")
    @RequestMapping(value = "/copyApp", method = RequestMethod.GET)
    Boolean copyApp(@RequestParam("appId") Long appId);

    @ApiOperation("获取app信息")
    @RequestMapping(value = "/getAppById", method = RequestMethod.GET)
    AppInfoResp getAppById(@RequestParam("id") Long id);

    @ApiOperation("通过APP名称获取app信息")
    @RequestMapping(value = "/getAppByAppName", method = RequestMethod.GET)
    List<AppInfoResp> getAppByAppName(@RequestParam("appName") String appName);
    
    @ApiOperation("用户确认打包")
    @RequestMapping(value = "/customConfirmAppPackage", method = RequestMethod.GET)
    Long customConfirmAppPackage(@RequestParam("id") Long id , @RequestParam("status") Integer status);

    @ApiOperation("分页获取app列表")
    @RequestMapping(value = "/updateDisplayIdentification", method = RequestMethod.GET)
    Long updateDisplayIdentification(@RequestParam("id") Long id , @RequestParam("displayIdentification") Integer displayIdentification);

    @ApiOperation("分页获取app列表")
    @RequestMapping(value = "/getAppPage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<AppInfoResp> getAppPage(@RequestBody GetAppReq req);

    @ApiOperation("批量删除app信息")
    @RequestMapping(value = "/delApp", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    Boolean delApp(@RequestBody List<Long> ids);

    @ApiOperation("根据时间更新app状态")
    @RequestMapping(value = "/updateAppStatusByTime", method = RequestMethod.GET)
    void updateAppStatusByTime();

    /////////////////////////////////////////多语言管理/////////////////////////////////////////////////////

    @ApiOperation("保存多语言信息")
    @RequestMapping(value = "/saveLang", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Boolean saveLang(@RequestBody SaveLangReq req);


    @ApiOperation("获取多语言信息")
    @RequestMapping(value = "/getLang", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    GetLangResp getLang(@RequestBody GetLangReq req);

    ///////////////////////////////////////// 配网信息 ///////////////////////////////////////////////////////////

    @ApiOperation("获取关联产品")
    @RequestMapping(value = "/getAppProduct", method = RequestMethod.GET)
    List<AppProductResp> getAppProduct(@RequestParam("appId") Long appId);

    @ApiOperation("产品设备打包")
    @RequestMapping(value = "/appExecPackageByProduct", method = RequestMethod.GET)
    Map appExecPackageByProduct(@RequestParam("appId") Long appId, @RequestParam("tenantId") Long tenantId);

    @ApiOperation("获取关联产品")
    @RequestMapping(value = "/getAppProductByAppIdAndTenantId", method = RequestMethod.GET)
    List<AppProductResp> getAppProductByAppIdAndTenantId(@RequestParam("appId") Long appId, @RequestParam("tenantId") Long tenantId);


    @ApiOperation("获取关联产品主键列表")
    @RequestMapping(value = "/getAppProductIdList", method = RequestMethod.GET)
    List<Long> getAppProductIdList(@RequestParam("appId") Long appId);

    @ApiOperation("保存关联产品")
    @RequestMapping(value = "/saveAppProduct", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long saveAppProduct(@RequestBody SaveAppProductReq req);

    @ApiOperation("删除关联产品")
    @RequestMapping(value = "/delAppProduct", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    Boolean delAppProduct(@RequestBody List<Long> ids);

    ///////////////////////////////////////// 配网引导 ////////////////////////////////////////////////////////////

    @ApiOperation("保存配网引导")
    @RequestMapping(value = "/saveGuide", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Boolean saveGuide(@RequestBody SaveGuideReq req);

    @ApiOperation("获取配网引导")
    @RequestMapping(value = "/getGuide", method = RequestMethod.GET)
    GetGuideResp getGuide(@RequestParam("id") Long id);

    ///////////////////////////////////////// APP审核 ////////////////////////////////////////////////////////////
    @ApiOperation("APP审核列表查询")
    @RequestMapping(value = "/getAppListByAuditStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<AppReviewResp> getAppListByAuditStatus(@RequestBody AppReviewSearchReq req);

    @ApiOperation("通过打包状态获取appId")
    @RequestMapping(value = "/getAppIdByPackStatus", method = RequestMethod.GET)
    List<Long> getAppIdByPackStatus(@RequestParam("packStatus") Integer packStatus);

    @ApiOperation("批量获取app信息")
    @RequestMapping(value = "/getAppByIds", method = RequestMethod.GET)
    List<AppInfoResp> getAppByIds(@RequestParam("ids") List<Long> ids);

    @ApiOperation("修改app审核状态为空")
    @RequestMapping(value = "/updateAuditStatusToNull", method = RequestMethod.POST)
    void updateAuditStatusToNull(@RequestParam("appId") Long appId);

    @ApiOperation("通过productId统计数量")
    @RequestMapping(value = "/countAppProductByproductId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Integer countAppProductByproductId(Long productId);

    /**
     * @return
     * @despriction：重开可编辑
     * @author yeshiyuan
     * @created 2018/10/25 17:06
     */
    @ApiOperation(value = "重开可编辑", notes = "重开可编辑")
    @RequestMapping(value = "/reOpen", method = RequestMethod.POST)
    void reOpen(@RequestParam("appId") Long appId);

    /**
      * @despriction：解绑产品关联的app,并删除相关的数据（配网文案）
      * @author  yeshiyuan
      * @created 2018/12/15 16:06
      * @params [productId, tenantId]
      * @return void
      */
    @ApiOperation(value = "解绑产品关联的app,并删除相关的数据（配网文案）", notes = "解绑产品关联的app,并删除相关的数据（配网文案）")
    @RequestMapping(value = "/unbindProductRelateApp", method = RequestMethod.POST)
    void unbindProductRelateApp(@RequestParam("productId") Long productId, @RequestParam("tenantId") Long tenantId);

    /**
      * @despriction：校验app是否能够使用(付款成功的app才能使用)
      * @author  yeshiyuan
      * @created 2018/12/28 14:43
      * @params [appId, tenantId]
      * @return boolean
      */
    @ApiOperation(value = "校验app是否能够使用(付款成功的app才能使用)", notes = "校验app是否能够使用(付款成功的app才能使用)")
    @RequestMapping(value = "/checkAppCanUsed", method = RequestMethod.GET)
    boolean checkAppCanUsed(@RequestParam("appId") Long appId, @RequestParam("tenantId") Long tenantId);


}
