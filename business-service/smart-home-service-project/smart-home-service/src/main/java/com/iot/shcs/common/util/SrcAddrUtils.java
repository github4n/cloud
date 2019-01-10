package com.iot.shcs.common.util;

import com.iot.common.helper.ApplicationContextHelper;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.device.service.impl.DeviceCoreService;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 17:43 2018/4/27
 * @Modify by:
 */
public class SrcAddrUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SrcAddrUtils.class);
    public static void analyzeSrcAddr(String srcAddr) {
        try {
            if (!StringUtils.isEmpty(srcAddr) && srcAddr.contains(".")) {
                LOGGER.info("parseSrcAddress---{}", srcAddr);
                String[] srcAddrArray = srcAddr.split("\\.");
                String key = srcAddrArray[0];
                String value = srcAddrArray[1];

                switch (key) {
                    case "0":
                        parseUserId(value);
                        break;
                    case "1":
                        parseDeviceId(value);
                        break;
                    case "2":
                        parseDeviceId(value);
                        break;
                    case "3":
                        parseDeviceId(value);
                        break;
                    case "4":
                        parseDeviceId(value);
                        break;
                    case "IPC":
                        parseDeviceId(value);
                        break;
                }
            }
        } catch (Exception e) {
            LOGGER.error("analyze SrcAddr error", e);
            e.printStackTrace();
        }
    }

    private static void parseUserId(String userId) {
        if (StringUtils.isEmpty(userId)) {
            LOGGER.error("parseUserId-null");
            return;
        }
        UserApi userApi = ApplicationContextHelper.getBean(UserApi.class);
        FetchUserResp userResp = userApi.getUserByUuid(userId);
        if (userResp != null) {
            SaaSContextHolder.setCurrentUserId(userResp.getId());
            SaaSContextHolder.setCurrentTenantId(userResp.getTenantId());
        }
    }

    private static void parseDeviceId(String deviceId) {
        if (!StringUtils.isEmpty(deviceId)) {
            DeviceCoreService deviceApi = ApplicationContextHelper.getBean(DeviceCoreService.class);
            GetDeviceInfoRespVo deviceResp = deviceApi.getDeviceInfoByDeviceId(deviceId);
            if (deviceResp != null) {
                Long tenantId = deviceResp.getTenantId();
                if (tenantId != null) {
                    SaaSContextHolder.setCurrentTenantId(tenantId);
                }
            }
        }
    }

}
