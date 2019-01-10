package com.iot.tenant.controller;

import com.iot.common.exception.BusinessException;
import com.iot.common.util.CommonUtil;
import com.iot.redis.RedisCacheUtil;
import com.iot.tenant.api.AppReviewApi;
import com.iot.tenant.domain.AppInfo;
import com.iot.tenant.enums.AuditStatusEnum;
import com.iot.tenant.exception.TenantExceptionEnum;
import com.iot.tenant.service.IAppInfoService;
import com.iot.tenant.service.IAppReviewRecordService;
import com.iot.tenant.util.RedisKeyUtil;
import com.iot.tenant.vo.req.review.AppReviewRecordReq;
import com.iot.tenant.vo.resp.review.AppReviewRecordResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：租户
 * 功能描述：App审核
 * 创建人： maochengyuan
 * 创建时间：2018/10/23 14:44
 * 修改人： maochengyuan
 * 修改时间：2018/10/23 14:44
 * 修改描述：
 */
@RestController
public class AppReviewApiController implements AppReviewApi {

    private final Logger logger = LoggerFactory.getLogger(AppReviewApiController.class);

    @Autowired
    private IAppReviewRecordService appReviewRecordService;

    @Autowired
    private IAppInfoService appInfoService;

    /**
     * 描述：app提交审核
     * @author maochengyuan
     * @created 2018/10/23 15:02
     * @param req
     * @return java.lang.Long
     */
    @Override
    public void submitAudit(@RequestBody AppReviewRecordReq req) {
        this.checkTenantId(req.getTenantId());
        AppInfo appInfo = this.appInfoService.getAppAuditStatus(req.getAppId());
        //判断APP信息是否存在
        if(appInfo == null){
            throw new BusinessException(TenantExceptionEnum.APP_INFO_EMPTY_ERROR);
        }
        //判断是否是当前租户在操作
        if(!req.getTenantId().equals(appInfo.getTenantId())){
            throw new BusinessException(TenantExceptionEnum.APP_REVIEW_TENANT_INCONSISTENCY);
        }
        req.setProcessStatus(AuditStatusEnum.Pending.getAuditStatus());
        this.appInfoService.setAppAuditStatus(req.getAppId(), req.getProcessStatus());
        this.appReviewRecordService.addAppReviewRecord(req);
        //清除app能否使用标志
        RedisCacheUtil.delete(RedisKeyUtil.getAppUsedKey(req.getAppId()));
    }

    /** 
     * 描述：app审核操作
     * @author maochengyuan
     * @created 2018/10/23 15:02
     * @param req
     * @return java.lang.Long
     */
    @Override
    public void review(@RequestBody AppReviewRecordReq req) {
        AppInfo appInfo = this.appInfoService.getAppAuditStatus(req.getAppId());
        //判断APP信息是否存在
        if(appInfo == null){
            throw new BusinessException(TenantExceptionEnum.APP_INFO_EMPTY_ERROR);
        }
        //只有当状态为待审核才允许进行审核操作
        if(!AuditStatusEnum.Pending.getAuditStatus().equals(appInfo.getAuditStatus())){
            throw new BusinessException(TenantExceptionEnum.APP_REVIEW_STATUS_ERROR);
        }
        //判断目标状态是否为"审核通过"或为"审核不通过"
        if(!AuditStatusEnum.checkAuditStatus(req.getProcessStatus())){
            throw new BusinessException(TenantExceptionEnum.APP_REVIEW_TARGET_STATUS_ERROR);
        }
        req.setTenantId(appInfo.getTenantId());
        this.appInfoService.setAppAuditStatus(req.getAppId(), req.getProcessStatus());
        this.appReviewRecordService.addAppReviewRecord(req);
    }

    /** 
     * 描述：依据appId获取审核记录
     * @author maochengyuan
     * @created 2018/10/23 15:03
     * @param appId
     * @return java.util.List<com.iot.tenant.vo.resp.review.AppReviewRecordResp>
     */
    @Override
    public List<AppReviewRecordResp> getAppReviewRecord(@RequestParam("appId") Long appId) {
        return this.appReviewRecordService.getAppReviewRecord(appId);
    }

    /**
     * 描述：检查租户是合法
     * @author maochengyuan
     * @created 2018/7/25 14:57
     * @param tenantId
     * @return void
     */
    private void checkTenantId(Long tenantId){
        if(CommonUtil.isEmpty(tenantId)){
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "tenantId is empty");
        }
    }

    /**
      * @despriction：获取租户id
      * @author  yeshiyuan
      * @created 2018/11/2 13:52
      * @return
      */
    @Override
    public Long getTenantIdById(Long id) {
        return appReviewRecordService.getTenantIdById(id);
    }

    /**
     * 添加app审核记录
     * @param req
     * @return
     */
    @Override
    public Long addRecord(@RequestBody AppReviewRecordReq req) {
        AppInfo appInfo = this.appInfoService.getAppAuditStatus(req.getAppId());
        //判断APP信息是否存在
        if(appInfo == null){
            throw new BusinessException(TenantExceptionEnum.APP_INFO_EMPTY_ERROR);
        }
        req.setTenantId(appInfo.getTenantId());
        return appReviewRecordService.addAppReviewRecord(req);
    }

    /**
     * @despriction：记录置为失效
     * @author  yeshiyuan
     * @created 2018/11/14 13:46
     * @return
     */
    @Override
    public void invalidRecord(@RequestParam("appId")Long appId, @RequestParam("tenantId") Long tenantId) {
        appReviewRecordService.invalidRecord(appId, tenantId);
    }
}
