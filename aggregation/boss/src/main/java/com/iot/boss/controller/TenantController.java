package com.iot.boss.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.iot.boss.service.tenant.TenantService;
import com.iot.boss.vo.tenant.req.TenantAuditRecordReq;
import com.iot.boss.vo.tenant.req.TenantAuditReq;
import com.iot.boss.vo.tenant.resp.TenantAuditResp;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.common.helper.Page;
import com.iot.tenant.api.TenantApi;
import com.iot.tenant.vo.req.GetTenantReq;
import com.iot.tenant.vo.req.SaveTenantReq;
import com.iot.tenant.vo.resp.AppPackResp;
import com.iot.tenant.vo.resp.TenantInfoResp;
import com.iot.tenant.vo.resp.TenantReviewRecordInfoResp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 描述：客户管理控制器
 * 创建人： LaiGuiMing
 * 创建时间： 2018/5/21 15:54
 */
@RestController
@Api(description = "客户接口",value = "客户接口")
@RequestMapping("/api/tenant")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @Autowired
    private TenantApi tenantApi;

    @LoginRequired(value = Action.Normal)
    @ApiOperation("分页获取客户列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public CommonResponse<Page<TenantInfoResp>> list(@RequestBody GetTenantReq req) {
        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", tenantApi.list(req));
    }

    //@LoginRequired(value = Action.Normal)
    @ApiOperation("保存客户信息")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResponse<Long> save(@RequestBody SaveTenantReq req) {
        return new CommonResponse<>(ResultMsg.SUCCESS, tenantApi.save(req));
    }

    //@LoginRequired(value = Action.Normal)
    @ApiOperation("删除客户信息")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse<Boolean> delete(@RequestBody ArrayList<Long> ids) {
        Boolean flag = tenantApi.delete(ids);
        return new CommonResponse<>(ResultMsg.SUCCESS, flag);
    }

    @ApiOperation("删除客户信息")
    @RequestMapping(value = "/deleteByPost", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse<Boolean> deleteByPost(@RequestBody ArrayList<Long> ids) {
        Boolean flag = tenantApi.delete(ids);
        return new CommonResponse<>(ResultMsg.SUCCESS, flag);
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation("获取全部客户列表")
    @RequestMapping(value = "/getTenantList", method = RequestMethod.GET)
    public CommonResponse<List<TenantInfoResp>> getTenantList() {
        return new CommonResponse<>(ResultMsg.SUCCESS, tenantService.getTenantList());
    }

    //@LoginRequired(value = Action.Normal)
    @ApiOperation("保存app打包配置")
    @RequestMapping(value = "/saveAppPack", method = RequestMethod.POST)
    public CommonResponse<Boolean> saveAppPack(MultipartHttpServletRequest multipartRequest) {
        CommonResponse<Boolean> result = new CommonResponse<>(ResultMsg.SUCCESS);
        try {
            if (!tenantService.saveAppPack(multipartRequest)) {
                result = new CommonResponse<>(ResultMsg.FAIL);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("file.deal.error", e);
        }
        return result;
    }

    //@LoginRequired(value = Action.Normal)
    @ApiOperation("根据客户代码获取打包信息")
    @RequestMapping(value = "/getAppPack", method = RequestMethod.GET)
    CommonResponse<AppPackResp> getAppPack(@RequestParam("code") String code) {
        return new CommonResponse<>(ResultMsg.SUCCESS, tenantService.getAppPack(code));
    }

    @ApiOperation("执行打包")
    @RequestMapping(value = "/execAppPack", method = RequestMethod.GET)
    CommonResponse<Boolean> execAppPack(@RequestParam("fileId") String fileId) {
        tenantService.execAppPack(fileId);
        return new CommonResponse<>(ResultMsg.SUCCESS);
    }
    
    @ApiOperation("租户审核信息列表")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "/tenantAuditList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    CommonResponse<Page<TenantAuditResp>> tenantAuditList(@RequestBody TenantAuditReq req){
    	return new CommonResponse<>(ResultMsg.SUCCESS, tenantService.tenantAuditList(req));
    }

    @ApiOperation("保存租户审核记录")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "/saveTenantReviewRecord", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    CommonResponse<Boolean> saveTenantReviewRecord(@RequestBody TenantAuditRecordReq req) {
    	tenantService.saveTenantReviewRecord(req);
    	return new CommonResponse<>(ResultMsg.SUCCESS);
    }
    
    @ApiOperation("锁定及解锁账号")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "/lockedTenant", method = RequestMethod.GET)
    CommonResponse<Boolean> lockedTenant(Long tenantId, Integer lockStatus) {
    	tenantService.lockedTenant(tenantId, lockStatus);
    	return new CommonResponse<>(ResultMsg.SUCCESS);
    }
    
    @ApiOperation("获取租户审核记录")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "/getTenantReviewRecordByTenantId", method = RequestMethod.GET)
    CommonResponse<List<TenantReviewRecordInfoResp>> getTenantReviewRecordByTenantId(@RequestParam("tenantId") Long tenantId){
    	return new CommonResponse<>(ResultMsg.SUCCESS, tenantService.getTenantReviewRecordByTenantId(tenantId));
    }
}
