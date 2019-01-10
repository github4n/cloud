package com.iot.boss.service.tenant.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.iot.boss.exception.BossExceptionEnum;
import com.iot.boss.service.tenant.TenantService;
import com.iot.boss.util.AppPackUtil;
import com.iot.boss.util.HttpClientUtils;
import com.iot.boss.vo.tenant.req.TenantAuditRecordReq;
import com.iot.boss.vo.tenant.req.TenantAuditReq;
import com.iot.boss.vo.tenant.resp.TenantAuditResp;
import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.common.util.StringUtil;
import com.iot.file.api.FileApi;
import com.iot.file.dto.FileDto;
import com.iot.file.util.FileUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.tenant.api.TenantApi;
import com.iot.tenant.vo.req.GetAuditTenantReq;
import com.iot.tenant.vo.req.SaveAppPackReq;
import com.iot.tenant.vo.req.SaveTenantReq;
import com.iot.tenant.vo.req.SaveTenantReviewRecordReq;
import com.iot.tenant.vo.resp.AppPackResp;
import com.iot.tenant.vo.resp.TenantInfoResp;
import com.iot.tenant.vo.resp.TenantReviewRecordInfoResp;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;

/**
 * 描述：租户管理实现类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/5/22 17:04
 */
@Service
public class TenantServiceImpl implements TenantService {
    @Autowired
    private TenantApi tenantApi;

    @Autowired
    private FileApi fileApi;
    
    /**
     * 用户服务
     */
    @Autowired
    private UserApi userApi;

    @Override
    public List<TenantInfoResp> getTenantList() {
        return tenantApi.getTenantList();
    }

    @Override
    public Boolean saveAppPack(MultipartHttpServletRequest multipartRequest) throws IOException {
        //基本配置
        SaveAppPackReq saveAppPackReq = setSaveAppPackReq(multipartRequest);
        //打包压缩文件
        AppPackUtil appPackUtil = new AppPackUtil(saveAppPackReq);
        File zipfile = appPackUtil.packFile(multipartRequest,saveAppPackReq);
        //上传文件服务器
        MultipartFile multipartFile = FileUtil.toMultipartFile(zipfile);
        String fileId = this.fileApi.upLoadFile(multipartFile, SaaSContextHolder.currentTenantId());

        //删除临时文件
        appPackUtil.deleteDirectory();
        //保存配置信息
        saveAppPackReq.setFileId(fileId);
        return tenantApi.saveAppPack(saveAppPackReq);
    }

    @Override
    public AppPackResp getAppPack(String code) {
        return tenantApi.getAppPack(code);
    }

    @Override
    public void execAppPack(String fileId) {
        FileDto fileDto = this.fileApi.getGetUrl(fileId);
        String packUrl="http://172.16.55.96:8686/pack";
        Map<String, String> params = new HashMap<String, String>();
        params.put("fileUrl",fileDto.getPresignedUrl());
        HttpClientUtils.getInstance().httpPost(packUrl, params);
    }

    //////////////////////////////////////////////////////////////////////////////////

    /**
     * 封装req对象
     * @param mr
     * @return
     */
    public SaveAppPackReq setSaveAppPackReq(MultipartHttpServletRequest mr) {
        SaveAppPackReq req = new SaveAppPackReq();
        req.setAppName(getParam(mr, "appName"));
        req.setStyle(Integer.parseInt(getParam(mr, "style")));
        if(getParam(mr, "id")!=null){
            req.setId(Long.parseLong(getParam(mr, "id")));
        }
        req.setLang(getParam(mr, "lang"));
        req.setTenantCode(getParam(mr, "tenantCode"));
        req.setIosVer(getParam(mr, "iosVer"));
        req.setAndroidVer(getParam(mr, "androidVer"));
        req.setVerFlag(Integer.parseInt(getParam(mr, "verFlag")));
        req.setProductIds(getParam(mr, "productIds"));
        req.setIosAppKey(getParam(mr, "iosAppKey"));
        req.setAndroidAppKey(getParam(mr, "androidAppKey"));
        return req;
    }

    public String getParam(MultipartHttpServletRequest multipartRequest, String key) {
        return multipartRequest.getParameter(key);
    }
    
    /**
     * 
     * 描述：租户审核信息列表
     * @author 李帅
     * @created 2018年10月22日 下午6:34:06
     * @since 
     * @param req
     * @return
     */
    public Page<TenantAuditResp> tenantAuditList(TenantAuditReq tenantAuditReq){
    	GetAuditTenantReq req = new GetAuditTenantReq();
    	BeanUtils.copyProperties(tenantAuditReq,req);
    	if(!StringUtil.isEmpty(tenantAuditReq.getUserName())) {
            FetchUserResp user = userApi.getAdminUserByUserName(tenantAuditReq.getUserName());
            if(user != null) {
            	req.setTenantId(user.getTenantId());
            } else {
                return new Page<TenantAuditResp>();
            }
        }
    	
    	Page<TenantAuditResp> result = new Page<TenantAuditResp>();
    	
    	Page<TenantInfoResp> tenantPage = tenantApi.tenantAuditList(req);
    	BeanUtil.copyProperties(tenantPage, result);
    	
    	List<TenantInfoResp> tenantInfoResps = tenantPage.getResult();
    	
    	List<TenantAuditResp> tenantAuditResps = new ArrayList<TenantAuditResp>();
    	TenantAuditResp tenantAuditResp = null;
    	
    	if(tenantInfoResps != null && tenantInfoResps.size() > 0) {
    		List<Long> tenantIds = new ArrayList<Long>();
        	
        	for(TenantInfoResp tenantInfoResp : tenantInfoResps) {
        		tenantAuditResp = new TenantAuditResp(); 
    			BeanUtil.copyProperties(tenantInfoResp, tenantAuditResp);
    			tenantAuditResp.setTenantId(tenantInfoResp.getId());
    			tenantAuditResp.setTenantName(tenantInfoResp.getName());
    			if(tenantInfoResp.getUpdateTime() != null) {
    				tenantAuditResp.setApplyTime(tenantInfoResp.getUpdateTime());
    			}else {
    				tenantAuditResp.setApplyTime(tenantInfoResp.getCreateTime());
    			}
    			if(tenantInfoResp.getId() != null) {
    				tenantIds.add(tenantInfoResp.getId());
    			}
        		tenantAuditResps.add(tenantAuditResp);
        	}
        	List<FetchUserResp> fetchUserResps = userApi.getAdminUserByTenantId(tenantIds);
        	Map<Long, String> tenantIdToNameMap = new HashMap<Long, String>();
        	for(FetchUserResp fetchUserResp : fetchUserResps) {
        		tenantIdToNameMap.put(fetchUserResp.getTenantId(), fetchUserResp.getUserName());
        	}
        	
        	for(TenantAuditResp auditResp : tenantAuditResps) {
        		auditResp.setUserName(tenantIdToNameMap.get(auditResp.getTenantId()));
        	}
    	}
    	result.setResult(tenantAuditResps);
    	return result;
    	
    }
    
    /**
     * 
     * 描述：保存租户审核记录
     * @author 李帅
     * @created 2018年10月22日 下午6:33:45
     * @since 
     * @param req
     */
    public void saveTenantReviewRecord(TenantAuditRecordReq req) {
    	SaveTenantReq saveTenantReq = new SaveTenantReq();
    	saveTenantReq.setId(req.getTenantId());
    	saveTenantReq.setAuditStatus(req.getAuditStatus());
    	if(req.getAuditStatus() != null && 2 == req.getAuditStatus()) {
    		if(req.getCode() != null) {
    			if(req.getCode().length() < 1 || req.getCode().length() > 6) {
    				throw new BusinessException(BossExceptionEnum.TENANT_CODE_ERROR, "Tenant Code must be a 3 to 6 bit string.");
    			}else {
    				saveTenantReq.setCode(req.getCode());
    			}
    		}
    	}
    	tenantApi.save(saveTenantReq);
    	//在Header中获取数据
        Long userId = SaaSContextHolder.getCurrentUserId();
    	SaveTenantReviewRecordReq saveTenantReviewRecordReq = new SaveTenantReviewRecordReq();
    	saveTenantReviewRecordReq.setCreateBy(userId);
    	saveTenantReviewRecordReq.setOperateDesc(req.getOperateDesc());
    	saveTenantReviewRecordReq.setProcessStatus(req.getAuditStatus());
    	saveTenantReviewRecordReq.setTenantId(req.getTenantId());
    	tenantApi.saveTenantReviewRecord(saveTenantReviewRecordReq);
    }
    
    /**
     * 
     * 描述：锁定及解锁账号
     * @author 李帅
     * @created 2018年10月22日 下午6:33:07
     * @since 
     * @param tenantId
     * @param lockStatus
     */
    public void lockedTenant(Long tenantId, Integer lockStatus) {
    	SaveTenantReq saveTenantReq = new SaveTenantReq();
    	saveTenantReq.setId(tenantId);
    	saveTenantReq.setLockStatus(lockStatus);
    	tenantApi.save(saveTenantReq);
    }
    
    /**
     * 
     * 描述：获取租户审核记录
     * @author 李帅
     * @created 2018年10月22日 下午6:33:27
     * @since 
     * @param tenantId
     * @return
     */
    public List<TenantReviewRecordInfoResp> getTenantReviewRecordByTenantId(Long tenantId){
    	return tenantApi.getTenantReviewRecordByTenantId(tenantId);
    }
}
