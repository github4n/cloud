package com.iot.device.api.fallback;

import com.github.pagehelper.PageInfo;
import com.iot.device.api.ServicePropertyApi;
import com.iot.device.vo.req.ServiceModulePropertyReq;
import com.iot.device.vo.req.UpdateModuleSupportIftttReq;
import com.iot.device.vo.rsp.ModuleSupportIftttResp;
import com.iot.device.vo.rsp.ServiceModulePropertyResp;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 19:56 2018/7/2
 * @Modify by:
 */
@Component
public class ServicePropertyApiFallbackFactory implements FallbackFactory<ServicePropertyApi> {
    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServicePropertyApiFallbackFactory.class);

    @Override
    public ServicePropertyApi create(Throwable cause) {
        return new ServicePropertyApi() {


            @Override
            public List<ServiceModulePropertyResp> findPropertyListByServiceModuleId(Long serviceModuleId) {
                return null;
            }

            @Override
            public List<ServiceModulePropertyResp> findPropertyListByServiceModuleIds(List<Long> serviceModuleIds) {
                return null;
            }

            @Override
            public List<ServiceModulePropertyResp> findPropertyListByActionId(Long actionId) {
                return null;
            }

            @Override
            public List<ServiceModulePropertyResp> findPropertyListByEventId(Long eventId) {
                return null;
            }

            @Override
            public ServiceModulePropertyResp getPropertyInfoByPropertyId(Long propertyId) {
                return null;
            }

            @Override
            public List<ServiceModulePropertyResp> listPropertyInfoByPropertyIds(List<Long> ids) {
                return null;
            }

            @Override
            public Long saveOrUpdate(ServiceModulePropertyReq serviceModulePropertyReq) {
                return null;
            }

            @Override
            public List<Long> saveOrUpdateBatch(List<ServiceModulePropertyReq> serviceModulePropertyReqs) {
                return null;
            }

            @Override
            public void delete(ArrayList<Long> ids) {

            }

            @Override
            public PageInfo list(ServiceModulePropertyReq serviceModulePropertyReq) {
                return null;
            }

            @Override
            public List<ServiceModulePropertyResp> listByServiceModuleId(Long serviceModuleId) {
                return null;
            }

            @Override
            public List<ServiceModulePropertyResp> listByActionModuleId(Long actionModuleId) {
                return null;
            }

            @Override
            public List<ServiceModulePropertyResp> listByEventModuleId(Long eventModuleId) {
                return null;
            }

            @Override
            public ModuleSupportIftttResp findSupportIftttProperties(List<Long> serviceModuleIds, Long tenantId) {
                return null;
            }

            @Override
            public int updatePropertySupportIfttt(UpdateModuleSupportIftttReq req) {
                return 0;
            }
        };
    }
}