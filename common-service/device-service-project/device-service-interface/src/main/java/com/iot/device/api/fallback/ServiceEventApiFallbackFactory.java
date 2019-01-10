package com.iot.device.api.fallback;

import com.github.pagehelper.PageInfo;
import com.iot.device.api.ServiceEventApi;
import com.iot.device.vo.req.ServiceModuleEventReq;
import com.iot.device.vo.req.UpdateModuleSupportIftttReq;
import com.iot.device.vo.rsp.ModuleSupportIftttResp;
import com.iot.device.vo.rsp.ServiceModuleEventResp;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 19:55 2018/7/2
 * @Modify by:
 */
@Component
public class ServiceEventApiFallbackFactory implements FallbackFactory<ServiceEventApi> {
    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceEventApiFallbackFactory.class);

    @Override
    public ServiceEventApi create(Throwable cause) {
        return new ServiceEventApi() {


            @Override
            public List<ServiceModuleEventResp> findEventListByServiceModuleId(Long serviceModuleId) {
                return null;
            }

            @Override
            public ServiceModuleEventResp getEventInfoByEventId(Long eventId) {
                return null;
            }

            @Override
            public List<ServiceModuleEventResp> listEventInfoByEventIds(List<Long> eventIds) {
                return null;
            }

            @Override
            public Long saveOrUpdate(ServiceModuleEventReq serviceModuleEventReq) {
                return null;
            }

            @Override
            public void delete(ArrayList<Long> ids) {

            }

            @Override
            public PageInfo list(ServiceModuleEventReq serviceModuleEventReq) {
                return null;
            }

            @Override
            public List<ServiceModuleEventResp> listByServiceModuleId(Long serviceModuleId) {
                return null;
            }

            @Override
            public ModuleSupportIftttResp findSupportIftttEvents(List<Long> serviceModuleIds, Long tenantId) {
                return null;
            }

            @Override
            public int updateEventSupportIfttt(UpdateModuleSupportIftttReq req) {
                return 0;
            }
        };
    }
}