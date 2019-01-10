//package com.iot.device.mapper;
//
//import com.baomidou.mybatisplus.mapper.BaseMapper;
//import com.baomidou.mybatisplus.plugins.pagination.Pagination;
//import com.iot.device.mapper.sql.UserDeviceSqlProvider;
//import com.iot.device.model.UserDevice;
//import com.iot.device.vo.rsp.DeviceResp;
//import com.iot.device.vo.rsp.UserDeviceProductResp;
//import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Param;
//import org.apache.ibatis.annotations.SelectProvider;
//import org.apache.ibatis.annotations.UpdateProvider;
//
//import java.util.List;
//
///**
// * <p>
//  * 用户设备关系表 Mapper 接口
// * </p>
// *
// * @author lucky
// * @since 2018-04-12
// */
//@Mapper
//public interface UserDeviceMapper extends BaseMapper<UserDevice> {
//
//
//    @SelectProvider(type = UserDeviceSqlProvider.class,method = "selectUserDeviceListByUserIdAndDeviceIds")
//    List<DeviceResp> selectUserDeviceListByUserIdAndDeviceIds(@Param("userId") Long userId, @Param("deviceIds") List<String> deviceIds);
//
//    @SelectProvider(type = UserDeviceSqlProvider.class,method = "selectUserDeviceProductListByUserId")
//    List<UserDeviceProductResp> selectUserDeviceProductListByUserId(Pagination pagination, Long userId);
//
//    @UpdateProvider(type = UserDeviceSqlProvider.class,method = "updateeventNotifEnabled")
//    int updateEventNotifEnabled(@Param("userId") Long userId, @Param("deviceId") String deviceId,@Param("eventNotifyEnabled")Integer eventNotifyEnabled);
//
//    @SelectProvider(type = UserDeviceSqlProvider.class,method = "selectEventNotifEnabled")
//    Integer selectEventNotifEnabled(@Param("userId") Long userId, @Param("deviceId") String deviceId);
//
//    @SelectProvider(type = UserDeviceSqlProvider.class,method = "getRootUserIdByDeviceId" )
//    Long getRootUserIdByDeviceId(String deviceId);
//}