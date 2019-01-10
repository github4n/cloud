package com.iot.portal.tenantaddres.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.portal.tenantaddres.service.TenantAddresManageService;
import com.iot.tenant.vo.req.TenantAddresManageReq;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：聚合层
 * 功能描述：租户地址控制层
 * 创建人： 李帅
 * 创建时间：2018年9月11日 下午4:07:00
 * 修改人：李帅
 * 修改时间：2018年9月11日 下午4:07:00
 */
@Api(description = "租户地址管理")
@RestController
@RequestMapping("/TenantAddresManageController")
public class TenantAddresManageController {

    @Autowired
    private TenantAddresManageService tenantAddresManageService;

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "保存租户地址信息", notes = "保存租户地址信息")
    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse save(@RequestBody TenantAddresManageReq req) {
    	this.tenantAddresManageService.save(req);
        return ResultMsg.SUCCESS.info();
    }
    
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "更新租户地址信息", notes = "更新租户地址信息")
    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse update(@RequestBody TenantAddresManageReq req) {
    	this.tenantAddresManageService.update(req);
        return ResultMsg.SUCCESS.info();
    }
    
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "删除租户地址信息", notes = "删除租户地址信息")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public CommonResponse delete(@RequestParam("id") Long id){
    	this.tenantAddresManageService.delete(id);
        return ResultMsg.SUCCESS.info();
    }
    
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "通过租户ID获取租户地址信息", notes = "通过租户ID获取租户地址信息")
    @RequestMapping(value = "/getAddresByTenantId", method = RequestMethod.GET)
    public CommonResponse getAddresByTenantId() {
        return ResultMsg.SUCCESS.info(this.tenantAddresManageService.getAddresByTenantId());
    }

    
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "联系我们", notes = "联系我们")
    @RequestMapping(value = "/contactUs", method = RequestMethod.GET)
    public CommonResponse contactUs() {
        return ResultMsg.SUCCESS.info(this.tenantAddresManageService.contactUs());
    }
}
