package com.iot.smarthome.util;

import com.iot.smarthome.model.DeviceClassify;
import com.iot.smarthome.model.ThirdPartyInfo;
import com.iot.smarthome.vo.resp.DeviceClassifyResp;
import com.iot.smarthome.vo.resp.ThirdPartyInfoResp;
import lombok.extern.slf4j.Slf4j;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/13 13:38
 * @Modify by:
 */

@Slf4j
public class BeanCopyUtil {

    public static void copyDeviceClassify(DeviceClassifyResp target, DeviceClassify source) {
        if (target == null || source == null) {
            log.error("copyDeviceClassify, target or source is null.");
            return;
        }
        target.setId(source.getId());
        target.setTypeName(source.getTypeName());
        target.setTypeCode(source.getTypeCode());
        target.setProductId(source.getProductId());
        target.setCreateTime(source.getCreateTime());
        target.setCreateBy(source.getCreateBy());
        target.setUpdateTime(source.getUpdateTime());
        target.setUpdateBy(source.getUpdateBy());
    }

    public static void copyThirdPartyInfo(ThirdPartyInfoResp target, ThirdPartyInfo source) {
        if (target == null || source == null) {
            log.error("copyThirdPartyInfo, target or source is null.");
            return;
        }
        target.setId(source.getId());
        target.setType(source.getType());
        target.setDescription(source.getDescription());
        target.setCompanyName(source.getCompanyName());
        target.setCompanyWebsite(source.getCompanyWebsite());
        target.setAppWebsite(source.getAppWebsite());
        target.setLogo(source.getLogo());
        target.setRedirectUri(source.getRedirectUri());
        target.setWebhookUrl(source.getWebhookUrl());
        target.setClientId(source.getClientId());
        target.setClientSecret(source.getClientSecret());
        target.setCreateTime(source.getCreateTime());
        target.setCreateBy(source.getCreateBy());
        target.setUpdateTime(source.getUpdateTime());
        target.setUpdateBy(source.getUpdateBy());
    }
}
