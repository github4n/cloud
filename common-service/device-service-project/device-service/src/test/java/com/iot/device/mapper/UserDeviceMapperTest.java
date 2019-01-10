//package com.iot.device.mapper;
//
//import com.baomidou.mybatisplus.plugins.pagination.Pagination;
//import com.iot.device.BaseTest;
//import com.iot.device.vo.rsp.UserDeviceProductResp;
//import com.iot.util.AssertUtils;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @Author: xfz
// * @Descrpiton:
// * @Date: 10:20 2018/4/23
// * @Modify by:
// */
//public class UserDeviceMapperTest extends BaseTest {
//
//    @Override
//    public String getBaseUrl() {
//        return null;
//    }
//
//    @Autowired
//    private UserDeviceMapper userDeviceMapper;
//    @Test
//    public void selectUserDeviceListByUserIdAndDeviceIds() {
//
//        List<String> deviceIds = new ArrayList<>();
//        deviceIds.add("3254354");
//
//        userDeviceMapper.selectUserDeviceListByUserIdAndDeviceIds(11232L,deviceIds);
//    }
//    @Test
//    public void selectUserDeviceProductListByUserId(){
//        Pagination page = new Pagination(1,10);
//        List<UserDeviceProductResp> respList = userDeviceMapper.selectUserDeviceProductListByUserId(page,2L);
//        AssertUtils.notEmpty(respList,"resp notnull");
//    }
//}