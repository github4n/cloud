//package com.iot.device.service.impl;
//
//import com.baomidou.mybatisplus.plugins.pagination.Pagination;
//import com.baomidou.mybatisplus.service.impl.ServiceImpl;
//import com.iot.common.helper.Page;
//import com.iot.device.core.service.DeviceServiceCoreUtils;
//import com.iot.device.core.service.UserDeviceServiceCoreUtils;
//import com.iot.device.mapper.DataPointMapper;
//import com.iot.device.mapper.DeviceMapper;
//import com.iot.device.mapper.UserDeviceMapper;
//import com.iot.device.model.UserDevice;
//import com.iot.device.service.IUserDeviceService;
//import com.iot.device.service.core.DeviceCoreService;
//import com.iot.device.vo.req.UserDeviceProductPageReq;
//import com.iot.device.vo.rsp.DeviceFunResp;
//import com.iot.device.vo.rsp.DeviceInfoListResp;
//import com.iot.device.vo.rsp.DeviceResp;
//import com.iot.device.vo.rsp.UserDeviceProductResp;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.CollectionUtils;
//
//import java.util.List;
//
///**
// * <p>
// * 用户设备关系表 服务实现类
// * </p>
// *
// * @author lucky
// * @since 2018-04-17
// */
//@Service
//public class UserDeviceServiceImpl extends ServiceImpl<UserDeviceMapper, UserDevice> implements IUserDeviceService {
//
//    public static final Logger LOGGER = LoggerFactory.getLogger(UserDeviceServiceImpl.class);
//    @Autowired
//    private UserDeviceMapper userDeviceMapper;
//
//    @Autowired
//    private DeviceMapper deviceMapper;
//
//    @Autowired
//    private DataPointMapper dataPointMapper;
//
//    @Autowired
//    private DeviceCoreService deviceCoreService;
//
//    @Override
//    public List<DeviceResp> findUserDeviceListByUserIdAndDeviceIds(Long tenantId, Long userId, List<String> deviceIds) {
//
//        return deviceCoreService.findDeviceListByUserId(tenantId, userId, deviceIds);
//
////        return userDeviceMapper.selectUserDeviceListByUserIdAndDeviceIds(userId, deviceIds);
//    }
//
//    @Transactional
//    public void deleteUserDeviceByUserIdAndDeviceId(Long userId, String deviceId) {
//        UserDevice userDevice = UserDeviceServiceCoreUtils.getUserDeviceByUserId(userId, deviceId);
//        if (userDevice != null) {
//            super.deleteById(userDevice.getId());
//        }
//        //remove cache user device
//        UserDeviceServiceCoreUtils.removeAllCacheUserDeviceByUserId(userId);
//        UserDeviceServiceCoreUtils.removeAllCacheUserDeviceByDeviceId(deviceId);
//    }
//
//    @Override
//    public List<DeviceInfoListResp> findDeviceListByUserId(Long userId) {
//        List<DeviceInfoListResp> deviceInfoLists;
//        //get cache
//        deviceInfoLists = DeviceServiceCoreUtils.findCacheDevicesByUserId(userId);
//        if (!CollectionUtils.isEmpty(deviceInfoLists)) {
//            int i =0;
//            for (DeviceInfoListResp device : deviceInfoLists
//                    ) {
//                List<DeviceFunResp> funList = dataPointMapper.selectDataPointsByProductId(device.getProductId());
//                device.setDeviceFunList(funList);
//                deviceInfoLists.set(i,device);
//                i++;
//            }
//        }
//        return deviceInfoLists;
//    }
//
//    @Override
//    public Page<UserDeviceProductResp> findUserDeviceProductPageByUserId(UserDeviceProductPageReq pageReq) {
//
//        Page<UserDeviceProductResp> pageResult = new Page<>();
//        Pagination pagination = new Pagination(pageReq.getPageNum(),pageReq.getPageSize());
//        List<UserDeviceProductResp> userDeviceProductRespList = userDeviceMapper.selectUserDeviceProductListByUserId(pagination,pageReq.getUserId());
//        pageResult.setPageNum(pageReq.getPageNum());
//        pageResult.setPageSize(pageReq.getPageSize());
//        pageResult.setTotal(pagination.getTotal());
//        pageResult.setResult(userDeviceProductRespList);
//        return  pageResult;
//    }
//
//    @Override
//    public int updateEventNotifEnabled(Long userId, String deviceId, Integer eventNotifyEnabled) {
//    	UserDeviceServiceCoreUtils.removeAllCacheUserDeviceByUserId(userId);
//        UserDeviceServiceCoreUtils.removeAllCacheUserDeviceByDeviceId(deviceId);
//        return this.userDeviceMapper.updateEventNotifEnabled(userId,deviceId,eventNotifyEnabled);
//    }
//
//    @Override
//    public Integer selectEventNotifEnabled(Long userId, String deviceId) {
//        return this.userDeviceMapper.selectEventNotifEnabled(userId,deviceId);
//    }
//
//    @Override
//    public Long getRootUserIdByDeviceId(String deviceId) {
//        return this.userDeviceMapper.getRootUserIdByDeviceId(deviceId);
//    }
//}
