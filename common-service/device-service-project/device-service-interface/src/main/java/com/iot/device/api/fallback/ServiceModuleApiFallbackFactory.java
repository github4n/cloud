package com.iot.device.api.fallback;

import com.github.pagehelper.PageInfo;
import com.iot.device.api.ServiceModuleApi;
import com.iot.device.vo.req.AddOrUpdateServiceModuleReq;
import com.iot.device.vo.req.GenerateModuleReq;
import com.iot.device.vo.req.ServiceModuleReq;
import com.iot.device.vo.rsp.GenerateModuleAgreementRsp;
import com.iot.device.vo.rsp.ServiceModuleInfoResp;
import com.iot.device.vo.rsp.ServiceModuleListResp;
import com.iot.device.vo.rsp.ServiceModuleResp;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 9:03 2018/7/2
 * @Modify by:
 */
@Component
public class ServiceModuleApiFallbackFactory implements FallbackFactory<ServiceModuleApi> {
    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceModuleApiFallbackFactory.class);

    @Override
    public ServiceModuleApi create(Throwable cause) {
        return new ServiceModuleApi() {

            @Override
            public List<ServiceModuleListResp> findServiceModuleListByDeviceTypeId(Long deviceTypeId) {
                return null;
            }

            @Override
            public List<ServiceModuleListResp> findServiceModuleListByParentIdNull() {
                return null;
            }

            @Override
            public List<ServiceModuleListResp> findServiceModuleListByProductId(Long productId) {
                return null;
            }

            @Override
            public ServiceModuleInfoResp getServiceModuleInfoByServiceModuleId(Long serviceModuleId) {
                return null;
            }

            @Override
            public void updateServiceModuleInfo(AddOrUpdateServiceModuleReq serviceModuleReq) {

            }

            @Override
            public Long saveOrUpdate(ServiceModuleReq serviceModuleReq) {
                return null;
            }

            @Override
            public void delete(ArrayList<Long> ids) {

            }

            @Override
            public PageInfo<ServiceModuleResp> list(ServiceModuleReq serviceModuleReq) {
                return null;
            }

            @Override
            public List<ServiceModuleResp> listByDeviceTypeId(Long deviceTypeId, Long tenantId) {
                return null;
            }


            @Override
            public List<ServiceModuleResp> listByProductId(Long productId) {
                return null;
            }

            @Override
            public List<Long> listServiceModuleIdsByProductId(Long productId) {
                return null;
            }

            @Override
            public String generateModuleId(GenerateModuleReq generateModuleReq) {
                return null;
            }

            @Override
            public PageInfo generateModuleList(GenerateModuleReq generateModuleReq) {
                return null;
            }

            @Override
            public List<GenerateModuleAgreementRsp> generateModuleAgreementList() {
                return null;
            }

            @Override
            public void copyModule(Long sourceDeviceTypeId, Long targetProductId) {

            }

            @Override
            public void delModuleByProductId(Long tenantId, Long productId) {

            }
        };
    }
}
