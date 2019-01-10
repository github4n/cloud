package com.iot.product.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.iot.common.helper.Page;
import com.iot.control.device.vo.req.PageUserDeviceInfoReq;
import com.iot.control.device.vo.resp.PageUserDeviceInfoRespVo;
import com.iot.device.api.DeviceApi;
import com.iot.device.vo.rsp.UserDeviceProductResp;
import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceTypeRespVo;
import com.iot.device.vo.rsp.device.ListProductRespVo;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.device.api.DeviceCoreServiceApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 11:29 2018/4/27
 * @Modify by:
 */
@Service
public class ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private DeviceApi deviceApi;

    @Autowired
    private DeviceCoreServiceApi deviceCoreService;


    public Page<UserDeviceProductResp> getProductPageByUserId(int offset, int pageSize, Long userId) {
        LOGGER.debug("getProductList({}, {}, {})", offset, pageSize, userId);
        Long tenantId = SaaSContextHolder.currentTenantId();

        Page<UserDeviceProductResp> resultDataPage = new Page<>(offset, pageSize);
        Page<PageUserDeviceInfoRespVo> resultUserDevicePage = deviceCoreService.findPageUserDevice(PageUserDeviceInfoReq.builder()
                .userId(userId)
                .tenantId(tenantId).pageNumber(offset)
                .pageSize(pageSize).build());

        List<String> deviceIds = Lists.newArrayList();
        if (resultUserDevicePage != null && resultUserDevicePage.getResult() != null && resultUserDevicePage.getResult().size() > 0) {
            resultUserDevicePage.getResult().forEach(userDevice -> {
                deviceIds.add(userDevice.getDeviceId());
            });
            resultDataPage.setPages(resultUserDevicePage.getPages());
            resultDataPage.setTotal(resultUserDevicePage.getTotal());
        }

        List<UserDeviceProductResp> userDeviceProductRespList = Lists.newArrayList();

        Set<Long> productIds = Sets.newHashSet();
        List<ListDeviceInfoRespVo> deviceInfoRespVos = deviceCoreService.listDevicesByDeviceIds(deviceIds);
        if (CollectionUtils.isEmpty(deviceInfoRespVos)) {
            deviceInfoRespVos.forEach(
                    deviceInfo -> {
                        if (deviceInfo.getProductId() != null) {
                            productIds.add(deviceInfo.getProductId());
                        }
                        UserDeviceProductResp userDeviceProduct = new UserDeviceProductResp();
                        userDeviceProduct.setDeviceId(deviceInfo.getUuid());
                        userDeviceProduct.setDeviceName(deviceInfo.getName());
                        userDeviceProduct.setDeviceTypeId(deviceInfo.getDeviceTypeId());
                        userDeviceProduct.setParentId(deviceInfo.getParentId());
                        userDeviceProduct.setProductId(deviceInfo.getProductId());
                        userDeviceProductRespList.add(userDeviceProduct);

                    });
        }
        List<Long> productIdList = Lists.newArrayList(productIds);

        Set<Long> deviceTypeIds = Sets.newHashSet();
        List<ListProductRespVo> productRespVoList = deviceCoreService.listProductByProductIds(productIdList);
        if (!CollectionUtils.isEmpty(productRespVoList)) {
            for (ListProductRespVo product : productRespVoList) {
                if (product.getDeviceTypeId() != null) {
                    deviceTypeIds.add(product.getDeviceTypeId());
                }
                int i = 0;
                for (UserDeviceProductResp usProduct : userDeviceProductRespList) {
                    if (usProduct.getProductId() != null) {
                        if (product.getId() == usProduct.getProductId()) {
                            usProduct.setProductName(product.getProductName());
                            usProduct.setDeviceTypeId(product.getDeviceTypeId());
                            userDeviceProductRespList.set(i, usProduct);
                            break;
                        }
                    }
                    i++;
                }
            }
        }
        List<Long> deviceTypeIdList = Lists.newArrayList(deviceTypeIds);

        List<ListDeviceTypeRespVo> deviceTypeRespVoList = deviceCoreService.listDeviceTypeByDeviceTypeIds(deviceTypeIdList);
        if (!CollectionUtils.isEmpty(deviceTypeRespVoList)) {
            for (ListDeviceTypeRespVo deviceType : deviceTypeRespVoList) {

                int i = 0;
                for (UserDeviceProductResp usProduct : userDeviceProductRespList) {
                    if (usProduct.getProductId() != null) {
                        if (deviceType.getId() == usProduct.getDeviceTypeId()) {
                            usProduct.setType(deviceType.getType());
                            userDeviceProductRespList.set(i, usProduct);
                            break;
                        }
                    }
                    i++;
                }
            }
        }
        resultDataPage.setResult(userDeviceProductRespList);

        return resultDataPage;
    }
}
