package com.iot.shcs.space.service;

import com.iot.control.share.api.ShareSpaceApi;
import com.iot.control.share.vo.resp.ShareSpaceResp;
import com.iot.mqttploy.api.MqttPloyApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author lucky
 * @ClassName ShareSpaceService
 * @Description 分享家聚合业务
 * @date 2019/1/7 11:16
 * @Version 1.0
 */
@Slf4j
@Component
public class ShareSpaceService {
    @Autowired
    private MqttPloyApi mqttPloyApi;

    @Autowired
    private ShareSpaceApi shareSpaceApi;

    /**
     * @param userId   所属家用户id
     * @param deviceId 解绑设备id
     * @return
     * @Description 批量解绑 分享家下所有用户跟直连设备
     * @Author lucky
     * @Date 11:31 2019/1/7
     * @Param tenantId
     **/
    public void unbindMultiAcls(Long tenantId, Long userId, String deviceId) {
        List<ShareSpaceResp> resultDataList = shareSpaceApi.listByFromUserId(tenantId, userId);
        if (CollectionUtils.isEmpty(resultDataList)) {
            return;
        }
        resultDataList.forEach(shareSpaceResp -> {
            int acls = mqttPloyApi.addAcls(shareSpaceResp.getToUserUuid(), deviceId);
            log.info("mqttPloyApi.addAcls.byShareSpace ok, fromUserUUID:{}, toUserUUID:{}, deviceID:{}, acls:{}", shareSpaceResp.getFromUserUuid(), shareSpaceResp.getToUserUuid(), deviceId, acls);
        });
    }

    /**
     * @param userId   所属家用户id
     * @param deviceId 解绑设备id
     * @return
     * @Description 批量绑定 分享家下所有用户跟直连设备
     * @Author lucky
     * @Date 11:31 2019/1/7
     * @Param tenantId
     **/
    public void addMultiAcls(Long tenantId, Long userId, String deviceId) {
        List<ShareSpaceResp> resultDataList = shareSpaceApi.listByFromUserId(tenantId, userId);
        if (CollectionUtils.isEmpty(resultDataList)) {
            return;
        }
        resultDataList.forEach(shareSpaceResp -> {
            int acls = mqttPloyApi.separationAcls(shareSpaceResp.getToUserUuid(), deviceId);
            log.info("mqttPloyApi.separationAcls.byShareSpace ok, fromUserUUID:{}, toUserUUID:{}, deviceID:{}, acls:{}", shareSpaceResp.getFromUserUuid(), shareSpaceResp.getToUserUuid(), deviceId, acls);

        });
    }
}
