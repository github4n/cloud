package com.iot.tenant.api.fallback;

import com.iot.tenant.api.AppReviewApi;
import com.iot.tenant.vo.req.review.AppReviewRecordReq;
import com.iot.tenant.vo.resp.review.AppReviewRecordResp;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：租户
 * 功能描述：App审核
 * 创建人： maochengyuan
 * 创建时间：2018/10/23 14:35
 * 修改人： maochengyuan
 * 修改时间：2018/10/23 14:35
 * 修改描述：
 */
@Component
public class AppReviewApiFallbackFactory implements FallbackFactory<AppReviewApi> {

    @Override
    public AppReviewApi create(Throwable throwable) {
        return new AppReviewApi() {
            @Override
            public void submitAudit(AppReviewRecordReq req) {

            }

            @Override
            public void review(AppReviewRecordReq req) {

            }

            @Override
            public List<AppReviewRecordResp> getAppReviewRecord(Long appId) {
                return null;
            }

            @Override
            public Long getTenantIdById(Long id) {
                return null;
            }

            @Override
            public Long addRecord(AppReviewRecordReq req) {
                return null;
            }

            @Override
            public void invalidRecord(Long appId, Long tenantId) {

            }
        };
    }
}
