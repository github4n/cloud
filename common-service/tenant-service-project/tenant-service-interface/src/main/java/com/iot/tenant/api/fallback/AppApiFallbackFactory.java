package com.iot.tenant.api.fallback;

import com.iot.common.helper.Page;
import com.iot.tenant.api.AppApi;
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
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 描述：
 * 创建人： LaiGuiMing
 * 创建时间： 2018/7/5 17:31
 */
@Component
public class AppApiFallbackFactory implements FallbackFactory<AppApi> {

    @Override
    public AppApi create(Throwable throwable) {
        return new AppApi() {
            @Override
            public Long saveApp(SaveAppReq req) {
                return null;
            }

            @Override
            public List appInfoValidation(SaveAppReq req) {
                return null;
            }

            @Override
            public Boolean copyApp(Long appId) {
                return false;
            }

            @Override
            public AppInfoResp getAppById(Long id) {
                return null;
            }

            @Override
			public List<AppInfoResp> getAppByAppName(String appName){
            	return null;
            }
            
            @Override
            public Long customConfirmAppPackage(Long id, Integer status) {
                return null;
            }

            @Override
            public Long updateDisplayIdentification(Long id, Integer displayIdentification) {
                return null;
            }

            @Override
            public Page<AppInfoResp> getAppPage(GetAppReq req) {
                return null;
            }

            @Override
            public Boolean delApp(List<Long> ids) {
                return false;
            }

            @Override
            public void updateAppStatusByTime() {

            }


            @Override
            public Boolean saveLang(SaveLangReq req) {
                return false;
            }

            @Override
            public GetLangResp getLang(GetLangReq req) {
                return null;
            }

            @Override
            public List<AppProductResp> getAppProduct(Long appId) {
                return null;
            }

            @Override
            public Map appExecPackageByProduct(Long appId, Long tenantId) {
                return null;
            }

            @Override
            public List<Long> getAppProductIdList(Long appId) {
                return null;
            }

            @Override
            public Long saveAppProduct(SaveAppProductReq req) {
                return null;
            }

            @Override
            public Boolean delAppProduct(List<Long> ids) {
                return false;
            }

            @Override
            public Boolean saveGuide(SaveGuideReq req) {
                return false;
            }

            @Override
            public GetGuideResp getGuide(Long id) {
                return null;
            }

            @Override
            public Page<AppReviewResp> getAppListByAuditStatus(AppReviewSearchReq req) {
                return null;
            }

            @Override
            public List<AppProductResp> getAppProductByAppIdAndTenantId(Long appId, Long tenantId) {
                return null;
            }

            @Override
            public List<Long> getAppIdByPackStatus(Integer packStatus) {
                return null;
            }

            @Override
            public List<AppInfoResp> getAppByIds(List<Long> ids) {
                return null;
            }

            @Override
            public void updateAuditStatusToNull(Long appId) {

            }

            @Override
            public Integer countAppProductByproductId(Long productId) {
                return null;
            }

            @Override
            public void reOpen(Long appId) {

            }

            @Override
            public void unbindProductRelateApp(Long productId, Long tenantId) {

            }

            @Override
            public boolean checkAppCanUsed(Long appId, Long tenantId) {
                return false;
            }


        };
    }
}
