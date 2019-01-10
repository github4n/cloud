package com.iot.boss.controller;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.iot.boss.service.file.FileService;
import com.iot.boss.service.module.IServiceModuleService;
import com.iot.boss.service.user.UserService;
import com.iot.boss.util.ModuleUtils;
import com.iot.boss.vo.FileResp;
import com.iot.boss.vo.module.BossActionInfoResp;
import com.iot.boss.vo.module.BossServiceModuleResp;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.device.api.ProductApi;
import com.iot.device.api.ServiceActionApi;
import com.iot.device.api.ServiceModuleApi;
import com.iot.device.exception.ProductExceptionEnum;
import com.iot.device.vo.req.AddOrUpdateServiceModuleReq;
import com.iot.device.vo.req.GenerateModuleReq;
import com.iot.device.vo.req.ServiceModuleReq;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.device.vo.rsp.ServiceModuleActionResp;
import com.iot.device.vo.rsp.ServiceModuleListResp;
import com.iot.device.vo.rsp.ServiceModuleResp;
import com.iot.util.AssertUtils;
import com.iot.saas.SaaSContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 模组表 前端控制器
 * </p>
 *
 * @author zhangyue
 * @since 2018-06-27
 */
@RestController
@Slf4j
@Api(description = "模组",value = "模组")
@RequestMapping("/api/service/module")
public class ServiceModuleController {

    @Autowired
    private ServiceModuleApi serviceModuleApi;

    @Autowired
    private FileService fileService;

    @Autowired
    private IServiceModuleService serviceModuleService;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private ServiceActionApi serviceActionApi;

    @Autowired
    private UserService userService;

    @ApiOperation("增加或者更新")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
    public CommonResponse saveOrUpdate(@RequestBody ServiceModuleReq serviceModuleReq) {
        log.debug("增加或者更新:{}", JSONObject.toJSON(serviceModuleReq));
        serviceModuleReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
        serviceModuleReq.setUpdateBy(SaaSContextHolder.getCurrentUserId());
        serviceModuleReq.setTenantId(SaaSContextHolder.currentTenantId());
        CommonResponse<Long> result = new CommonResponse<>(ResultMsg.SUCCESS,serviceModuleApi.saveOrUpdate(serviceModuleReq));
        return result;
    }

    @ApiOperation("根据id删除")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public CommonResponse delete(@RequestBody ArrayList<Long> ids) {
        log.debug("根据id删除",ids.toArray());
        serviceModuleApi.delete(ids);
        return CommonResponse.success();
    }

    @ApiOperation("根据id删除")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "deleteByPost", method = RequestMethod.POST)
    public CommonResponse deleteByPost(@RequestBody ArrayList<Long> ids) {
        log.debug("根据id删除",ids.toArray());
        serviceModuleApi.delete(ids);
        return CommonResponse.success();
    }

    @ApiOperation("获取列表")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public CommonResponse list(@RequestBody ServiceModuleReq serviceModuleReq) {
        PageInfo<ServiceModuleResp> list = serviceModuleApi.list(serviceModuleReq);
        List<String> imgId = new ArrayList<>();
        list.getList().forEach(m->{
            if (StringUtils.isNotEmpty(m.getImg())){
                imgId.add(m.getImg());
            }
            if (StringUtils.isNotEmpty(m.getChangeImg())){
                imgId.add(m.getChangeImg());
            }
        });
        Map map = fileService.getUrl(imgId);
        if (map!=null){
            list.getList().forEach(n->{
                    if (map.get(n.getImg())!=null){
                        n.setImg(map.get(n.getImg()).toString());
                    }
                    if (map.get(n.getChangeImg())!=null){
                        n.setChangeImg(map.get(n.getChangeImg()).toString());
                    }
            });
        }
        return CommonResponse.success(list);
    }

    @ApiOperation("根据deviceTypeId获取列表")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "listByDeviceTypeId/{deviceTypeId}", method = RequestMethod.GET)
    public CommonResponse listByDeviceTypeId(@PathVariable(value = "deviceTypeId",required = true) Long deviceTypeId) {
        log.debug("根据deviceTypeId获取列表",deviceTypeId);
        List<ServiceModuleResp> list = serviceModuleApi.listByDeviceTypeId(deviceTypeId,SaaSContextHolder.currentTenantId());
        List<String> imgId = new ArrayList<>();
        list.forEach(m->{
            if (StringUtils.isNotEmpty(m.getImg())){
                imgId.add(m.getImg());
            }
            if (StringUtils.isNotEmpty(m.getChangeImg())){
                imgId.add(m.getChangeImg());
            }
        });
        Map map = fileService.getUrl(imgId);
        if (map!=null){
            list.forEach(n->{
                if (map.get(n.getImg())!=null){
                    n.setImg(map.get(n.getImg()).toString());
                }
                if (map.get(n.getChangeImg())!=null){
                    n.setChangeImg(map.get(n.getChangeImg()).toString());
                }
            });
        }

        return CommonResponse.success(list);
    }

    @ApiOperation("根据productId获取列表")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "listByProductId/{productId}", method = RequestMethod.GET)
    public CommonResponse listByProductId(@PathVariable(value = "productId",required = true) Long productId) {
        log.debug("根据productId获取列表",productId);
        List<ServiceModuleResp> list = serviceModuleApi.listByProductId(productId);
        List<String> imgId = new ArrayList<>();
        list.forEach(m->{
            if (StringUtils.isNotEmpty(m.getImg())){
                imgId.add(m.getImg());
            }
            if (StringUtils.isNotEmpty(m.getChangeImg())){
                imgId.add(m.getChangeImg());
            }
        });
        Map map = fileService.getUrl(imgId);
        if (map!=null){
            list.forEach(n->{
                    if (map.get(n.getImg())!=null){
                        n.setImg(map.get(n.getImg()).toString());
                    }
                    if (map.get(n.getChangeImg())!=null){
                        n.setChangeImg(map.get(n.getChangeImg()).toString());
                    }
            });
        }

        return CommonResponse.success(list);
    }


    @ApiOperation("获取产品对应的功能组定义列表【事件方法属性【包括是否选中问题】】")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "/findServiceModuleListByProductId", method = RequestMethod.GET)
    public CommonResponse<List<BossServiceModuleResp>> findServiceModuleListByProductId(@RequestParam("productId") Long productId) {
        AssertUtils.notNull(productId, "productId.notnull");
        return CommonResponse.success(serviceModuleService.findServiceModuleListByProductId(productId));
    }

    @ApiOperation("修改模组信息")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "/updateServiceModuleInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse<AddOrUpdateServiceModuleReq> updateServiceModuleInfo(@RequestBody AddOrUpdateServiceModuleReq serviceModuleReq) {
        log.info("updateServiceModuleInfo : {}", serviceModuleReq);
        AssertUtils.notNull(serviceModuleReq, "serviceModuleReq.notnull");
        AssertUtils.notNull(serviceModuleReq.getProductId(), "productId.notnull");
        AssertUtils.notEmpty(serviceModuleReq.getServiceModuleList(), "serviceModule.notnull");
        serviceModuleApi.updateServiceModuleInfo(serviceModuleReq);
        return CommonResponse.success();
    }

    @ApiOperation("刪除产品下的功能组")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "/bossDelModuleByProductId", method = RequestMethod.GET)
    public CommonResponse bossDelModuleByProductId(@RequestParam("tenantId") Long tenantId, @RequestParam("productId") Long productId) {
        AssertUtils.notNull(productId, "productId.notnull");
        Long userId = SaaSContextHolder.getCurrentUserId();
        userService.checkUserHadRight(userId);
        serviceModuleApi.delModuleByProductId(tenantId, productId);
        return CommonResponse.success();
    }

    @ApiOperation("生成ModuleId")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "generateModuleId", method = RequestMethod.POST)
    public CommonResponse generateModuleId(@RequestBody GenerateModuleReq generateModuleReq){
        log.debug("生成ModuleId",JSONObject.toJSON(generateModuleReq));
        String result = serviceModuleApi.generateModuleId(generateModuleReq);
        if (result.equals("0")){
            return new CommonResponse(0, "the module id is too long",result);
        }
        return CommonResponse.success(result);
    }

    @ApiOperation("分页获取moduleId")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "generateModuleList", method = RequestMethod.POST)
    public CommonResponse generateModuleList(@RequestBody GenerateModuleReq generateModuleReq){
        log.debug("分页获取moduleId",JSONObject.toJSON(generateModuleReq));
        PageInfo result = serviceModuleApi.generateModuleList(generateModuleReq);
        return CommonResponse.success(result);
    }

    @ApiOperation("获取协议")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "generateModuleAgreementList", method = RequestMethod.GET)
    public CommonResponse generateModuleAgreementList(){
        log.debug("获取协议");
        List result = serviceModuleApi.generateModuleAgreementList();
        return CommonResponse.success(result);
    }


    /**
      * @despriction：查询产品的模组详情
      * @author  yeshiyuan
      * @created 2018/10/26 15:25
      * @return
      */
    @LoginRequired
    @ApiOperation("查询产品的模组详情")
    @RequestMapping(value = "/queryProductModule", method = RequestMethod.GET)
    public CommonResponse queryProductModule(@RequestParam("productId") Long productId) {
        return CommonResponse.success(serviceModuleService.queryProductModule(productId));
    }


}

