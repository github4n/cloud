package com.iot.boss.service.tenant;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.iot.boss.vo.tenant.req.TenantAuditRecordReq;
import com.iot.boss.vo.tenant.req.TenantAuditReq;
import com.iot.boss.vo.tenant.resp.TenantAuditResp;
import com.iot.common.helper.Page;
import com.iot.tenant.vo.resp.AppPackResp;
import com.iot.tenant.vo.resp.TenantInfoResp;
import com.iot.tenant.vo.resp.TenantReviewRecordInfoResp;

/**
 * 描述：租户管理
 * 创建人： LaiGuiMing
 * 创建时间： 2018/5/22 17:04
 */
public interface TenantService {

    /**
     * 获取租户信息列表
     */
    List<TenantInfoResp> getTenantList();

    /**
     * 保存app打包配置
     */
    Boolean saveAppPack(MultipartHttpServletRequest multipartRequest) throws IOException;


    /**
     * 根据客户代码获取打包信息
     */
    AppPackResp getAppPack(String code);

    /**
     * 执行app打包
     * @param fileId
     */
    void execAppPack(String fileId);
    
    /**
     * 
     * 描述：租户审核信息列表
     * @author 李帅
     * @created 2018年10月22日 下午6:33:56
     * @since 
     * @param req
     * @return
     */
    Page<TenantAuditResp> tenantAuditList(TenantAuditReq req);
    
    /**
     * 
     * 描述：保存租户审核记录
     * @author 李帅
     * @created 2018年10月22日 下午6:33:38
     * @since 
     * @param req
     */
    void saveTenantReviewRecord(TenantAuditRecordReq req);

    /**
     * 
     * 描述：锁定及解锁账号
     * @author 李帅
     * @created 2018年10月22日 下午6:32:58
     * @since 
     * @param tenantId
     * @param lockStatus
     */
    void lockedTenant(Long tenantId, Integer lockStatus);
    
    /**
     * 
     * 描述：获取租户审核记录
     * @author 李帅
     * @created 2018年10月22日 下午6:33:20
     * @since 
     * @param tenantId
     * @return
     */
    List<TenantReviewRecordInfoResp> getTenantReviewRecordByTenantId(Long tenantId);
}
