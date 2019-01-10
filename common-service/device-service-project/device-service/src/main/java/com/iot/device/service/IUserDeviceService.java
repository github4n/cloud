//package com.iot.device.service;
//
//import com.baomidou.mybatisplus.service.IService;
//import com.iot.common.helper.Page;
//import com.iot.device.model.UserDevice;
//import com.iot.device.vo.req.UserDeviceProductPageReq;
//import com.iot.device.vo.rsp.DeviceInfoListResp;
//import com.iot.device.vo.rsp.DeviceResp;
//import com.iot.device.vo.rsp.UserDeviceProductResp;
//
//import java.util.List;
//
///**
// * <p>
// * 用户设备关系表 服务类
// * </p>
// *
// * @author lucky
// * @since 2018-04-17
// */
//public interface IUserDeviceService extends IService<UserDevice> {
//
//    /**
//     * 根据设备ids、用户id 获取用户设备列表信息
//     * @param userId
//     * @param deviceIds
//     * @return
//     */
//    List<DeviceResp> findUserDeviceListByUserIdAndDeviceIds(Long tenantId, Long userId, List<String> deviceIds);
//
//    /**
//     * 删除设备与用户的关系
//     * @param userId
//     * @param deviceId
//     */
//    void deleteUserDeviceByUserIdAndDeviceId(Long userId, String deviceId);
//
//    /**
//     * 根据用户获取设备信息列表
//     * @param userId
//     * @return
//     */
//    List<DeviceInfoListResp> findDeviceListByUserId(Long userId);
//
//    Page<UserDeviceProductResp> findUserDeviceProductPageByUserId(UserDeviceProductPageReq pageReq);
//
//    int updateEventNotifEnabled(Long userId, String deviceId, Integer eventNotifyEnabled);
//
//    Integer selectEventNotifEnabled(Long userId, String deviceId);
//
//    /**
//      * @despriction：获取设备绑定的主账号id
//      * @author  yeshiyuan
//      * @created 2018/5/24 10:51
//      * @param null
//      * @return
//      */
//    Long getRootUserIdByDeviceId(String deviceId);
//}
