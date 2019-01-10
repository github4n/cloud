package com.iot.device.web.utils;

import com.iot.device.model.Product;
import com.iot.device.vo.rsp.device.GetProductInfoRespVo;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 20:12 2018/11/13
 * @Modify by:
 */
public class ProductCopyUtils {

    public static void copyProduct(Product product, GetProductInfoRespVo target) {
        if (target == null) {
            return;
        }
        if (product != null) {
            target.setId(product.getId());
            target.setProductName(product.getProductName());
            target.setTenantId(product.getTenantId());
            target.setCommunicationMode(product.getCommunicationMode());
            target.setTransmissionMode(product.getTransmissionMode());
            target.setCreateTime(product.getCreateTime());
            target.setUpdateTime(product.getUpdateTime());
            target.setModel(product.getModel());
            target.setConfigNetMode(product.getConfigNetMode());
            target.setIsDirectDevice(product.getIsDirectDevice());
            target.setIsKit(product.getIsKit());
            target.setDeviceTypeId(product.getDeviceTypeId());
            target.setIcon(product.getIcon());
            target.setRemark(product.getRemark());
        }

    }
}
