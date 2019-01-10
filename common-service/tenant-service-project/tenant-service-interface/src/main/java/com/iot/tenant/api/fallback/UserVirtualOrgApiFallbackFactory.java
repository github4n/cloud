package com.iot.tenant.api.fallback;

import com.iot.tenant.api.UserVirtualOrgApi;
import com.iot.tenant.vo.req.AddUserVirtualOrgReq;
import com.iot.tenant.vo.resp.UserDefaultOrgInfoResp;
import com.iot.tenant.vo.resp.UserOrgInfoResp;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 15:40 2018/4/26
 * @Modify by:
 */
@Component
public class UserVirtualOrgApiFallbackFactory implements FallbackFactory<UserVirtualOrgApi> {

    @Override
    public UserVirtualOrgApi create(Throwable cause) {
        return new UserVirtualOrgApi() {

            @Override
            public Long addUserVirtualOrg(AddUserVirtualOrgReq req) {
                return null;
            }

            @Override
            public UserOrgInfoResp getOrgInfoByUserId(Long userId) {
                return null;
            }

            @Override
            public UserDefaultOrgInfoResp getDefaultUsedOrgInfoByUserId(Long userId) {
                return null;
            }

            @Override
            public void deleteOrgByUserIdAndOrgId(Long userId, Long orgId) {

            }
        };
    }
}
