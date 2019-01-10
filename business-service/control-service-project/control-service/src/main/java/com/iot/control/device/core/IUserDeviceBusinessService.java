package com.iot.control.device.core;

import com.iot.control.device.entity.UserDevice;
import com.iot.control.device.vo.req.UpdateUserDeviceInfoReq;
import com.iot.control.device.vo.resp.UpdateUserDeviceInfoResp;

import java.util.List;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 11:41 2018/10/12
 * @Modify by:
 */
public interface IUserDeviceBusinessService {

    UserDevice getUserDevice(Long tenantId, String deviceId);

    UserDevice getUserDevice(Long tenantId, Long orgId, Long userId, String deviceId);

    List<UserDevice> listBatchUserDevice(Long tenantId, Long orgId, Long userId);

    List<UserDevice> listBatchUserDevice(Long tenantId, Long orgId, Long userId, List<String> deviceIds);

    List<UserDevice> listBatchUserDevice(Long tenantId, Long orgId, Long userId, List<String> deviceIds, int batchSize);

    UpdateUserDeviceInfoResp saveOrUpdate(UpdateUserDeviceInfoReq params);

    List<UpdateUserDeviceInfoResp> saveOrUpdateBatch(List<UpdateUserDeviceInfoReq> paramsList);

    void delUserDevice(Long tenantId, Long userId, String deviceId);
}
