package com.iot.device.api.fallback;

import com.github.pagehelper.PageInfo;
import com.iot.device.api.ServiceActionApi;
import com.iot.device.vo.req.ServiceModuleActionReq;
import com.iot.device.vo.req.UpdateActionInfoReq;
import com.iot.device.vo.req.UpdateModuleSupportIftttReq;
import com.iot.device.vo.rsp.ModuleSupportIftttResp;
import com.iot.device.vo.rsp.ServiceModuleActionResp;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 19:54 2018/7/2
 * @Modify by:
 */
@Component
public class ServiceActionApiFallbackFactory implements FallbackFactory<ServiceActionApi> {
    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceActionApiFallbackFactory.class);

    @Override
    public ServiceActionApi create(Throwable cause) {
        return new ServiceActionApi() {
            @Override
            public List<ServiceModuleActionResp> findActionListByServiceModuleId(Long serviceModuleId) {
                return null;
            }

            @Override
            public ServiceModuleActionResp getActionInfoByActionId(Long actionId) {
                return null;
            }

            @Override
            public List<ServiceModuleActionResp> listActionInfoByActionIds(List<Long> actionIds) {
                return null;
            }

            @Override
            public void updateActionInfo(UpdateActionInfoReq actionInfoReq) {

            }

            @Override
            public Long saveOrUpdate(ServiceModuleActionReq serviceModuleActionReq) {
                return null;
            }

            @Override
            public void delete(ArrayList<Long> ids) {

            }


            @Override
            public PageInfo list(ServiceModuleActionReq serviceModuleActionReq) {
                return null;
            }

            @Override
            public List<ServiceModuleActionResp> listByServiceModuleId(Long serviceModuleId) {
                return null;
            }

            @Override
            public ModuleSupportIftttResp findSupportIftttActions(List<Long> serviceModuleIds, Long tenantId) {
                return null;
            }

            @Override
            public int updateActionSupportIfttt(UpdateModuleSupportIftttReq req) {
                return 0;
            }
        };
    }
}