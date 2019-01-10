package com.iot.report.fallback;

import com.iot.report.api.UserActiveApi;
import com.iot.report.dto.req.ActivateBaseReq;
import com.iot.report.dto.resp.ActiveDataResp;
import com.iot.report.dto.resp.UserActivatedOrActivateResp;
import com.iot.report.dto.resp.UserActiveAndActivatedResp;
import com.iot.report.dto.resp.UserDevActiveAndActivatedResp;
import com.iot.report.entity.UserActivatedEntity;
import feign.hystrix.FallbackFactory;

import java.util.List;

public class UserActiveApiFallbackFactory implements FallbackFactory<UserActiveApi> {

    @Override
    public UserActiveApi create(Throwable cause) {
        return new UserActiveApi() {
            @Override
            public void arrangeUserData() {

            }

            @Override
            public List<ActiveDataResp> getUserRegisterByDate(ActivateBaseReq req) {
                return null;
            }

            @Override
            public List<ActiveDataResp> getUserActiveByDate(ActivateBaseReq req) {
                return null;
            }

            @Override
            public void testSaveActivated(UserActivatedEntity entity) {

            }

            @Override
            public UserActivatedOrActivateResp getUserRegisterDetail(ActivateBaseReq req) {
                return null;
            }

            @Override
            public UserActivatedOrActivateResp getUserActiveDetail(ActivateBaseReq req) {
                return null;
            }

            @Override
            public UserActiveAndActivatedResp getUserActiveAndActivated(ActivateBaseReq req) {
                return null;
            }

            @Override
            public Long getUserRegisterTotal(Long tenantId) {
                return null;
            }

            @Override
            public UserDevActiveAndActivatedResp getUserDevActiveAndActivated(ActivateBaseReq req) {
                return null;
            }
        };
    }
}
