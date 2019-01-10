//package com.iot.shcs.device.service.impl;
//
//import com.iot.common.exception.BusinessException;
//import com.iot.control.space.api.SmarthomeSpaceApi;
//import com.iot.control.space.api.SpaceDeviceApi;
//import com.iot.control.space.vo.SpaceDeviceReq;
//import com.iot.control.space.vo.SpaceDeviceResp;
//import com.iot.control.space.vo.SpaceReq;
//import com.iot.control.space.vo.SpaceResp;
//import com.iot.shcs.device.enums.DeviceCoreExceptionEnum;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.util.CollectionUtils;
//
//import java.util.List;
//
///**
// * @Author: lucky
// * @Descrpiton:
// * @Date: 14:13 2018/10/12
// * @Modify by:
// */
//@Component
//public class SpaceCoreService {
//
//    @Autowired
//    private SmarthomeSpaceApi spaceApi;
//
//    @Autowired
//    private SpaceDeviceApi spaceDeviceApi;
//
//
//    public SpaceResp getDefaultSpaceByParams(Long tenantId, Long userId) {
//        List<SpaceResp> spaceRespList = spaceApi.findSpaceByCondition(
//                SpaceReq.builder()
//                        .userId(userId).tenantId(tenantId)
//                        .build()
//        );
//        SpaceResp defaultSpace = null;
//        if (CollectionUtils.isEmpty(spaceRespList)) {
//            throw new BusinessException(DeviceCoreExceptionEnum.SPACE_DEFAULT_NOT_EXIST);
//        }
//        for (SpaceResp spaceResp : spaceRespList) {
//            if (spaceResp.getDefaultSpace() == 1 && spaceResp.getType().equals("HOME")) {
//                //默认家
//                defaultSpace = spaceResp;
//                break;
//            }
//        }
//        if (defaultSpace == null) {
//            throw new BusinessException(DeviceCoreExceptionEnum.SPACE_DEFAULT_NOT_EXIST);
//        }
//        return defaultSpace;
//    }
//
//
//    public void saveOrUpdateUpdateSpaceDevice(Long tenantId, Long spaceId, String deviceId) {
//        SpaceDeviceReq spaceDeviceReq = SpaceDeviceReq.builder()
//                .tenantId(tenantId).deviceId(deviceId).spaceId(spaceId).locationId(spaceId).build();
//        List<SpaceDeviceResp> spaceDeviceResps = spaceDeviceApi.findSpaceDeviceByCondition(spaceDeviceReq);
//        if (!CollectionUtils.isEmpty(spaceDeviceResps)) {
//            return;
//        }
//        //新增
//        spaceDeviceApi.inserSpaceDevice(spaceDeviceReq);
//    }
//
//    public int getSpaceSeq(Long tenantId, Long userId) {
//        //4.检查并获取用户默认家
//        SpaceResp space = this.getDefaultSpaceByParams(tenantId, userId);
//        //5.构建 默认设备名称+租户信息 进行存储
//        Integer oldSeq = space != null && space.getSeq() == null ? 0 : space.getSeq();
//        Integer seq = oldSeq + 1;
//        return seq;
//    }
//
//}
