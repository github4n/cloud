package com.iot.device.mapper;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.iot.device.BaseTest;
import com.iot.device.comm.utils.ListUtils;
import com.iot.device.model.Device;
import com.iot.device.vo.req.DevicePageReq;
import com.iot.device.vo.rsp.DeviceResp;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 10:11 2018/4/23
 * @Modify by:
 */
public class DeviceMapperTest extends BaseTest {

    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public String getBaseUrl() {
        return null;
    }

    @Test
    public void selectDeviceListByIsDirectDeviceAndVenderCode() {

        deviceMapper.selectDeviceListByIsDirectDeviceAndVenderCode(1, 11L, 1L,"MultiProtocolGatetay");
    }

    @Test
    public void selectDeviceListByIsDirectDeviceAndUserId() {
//        List<DeviceResp> deviceRespList = deviceMapper.selectDeviceListByIsDirectDeviceAndUserId(1,2L);
//        System.out.println(deviceRespList);
    }

    @Test
    public void selectDevicesByUserId() {

    }

    @Test
    public void selectDeviceListByDeviceIds() {

    }

    @Test
    public void updateDevice(){

        Device device = new Device();
        device.setUuid("23534546457568bfgdhd");
        device.setProductId(12235L);
        device.setIsDirectDevice(0);
        deviceMapper.insert(device);
    }
    @Test
    public void selectById() {
        Device device = deviceMapper.selectById(988321703050731522L);
        System.out.println(device);
    }

    @Test
    public void selectDeviceListByParentId() {
        List<DeviceResp> devices = deviceMapper.selectDeviceListByParentId("000010000000562744551lo46");
        System.out.println(devices);
    }

    @Test
    public void selectDirectDevicePage() {
        Pagination page = new Pagination(1,10);
        DevicePageReq pageReq = new DevicePageReq();

        List<DeviceResp> devices = deviceMapper.selectDirectDevicePage(page,pageReq);
        System.out.println(devices);
    }
    @Test
    public void selectUnDirectDevicePage() {
        Pagination page = new Pagination(1,10);
        DevicePageReq pageReq = new DevicePageReq();

        List<DeviceResp> devices = deviceMapper.selectUnDirectDevicePage(page,pageReq);
        System.out.println(devices);
    }

    @Test
    public void selectAllUnDirectDeviceList() {
        DevicePageReq pageReq = new DevicePageReq();

        List<DeviceResp> devices = deviceMapper.selectAllUnDirectDeviceList(pageReq);
        System.out.println(devices);
    }

    @Test
    public void selectDevListByDeviceIds() {

    }

    @Test
    public void selectDevListByDeviceType() {

    }

    @Test
    public void findDeviceListByIds() {
        List<String> deviceIds = new ArrayList<>();
        deviceIds.add("8dc986b7f10e04d333a9dc4e055de75c");
        String deviceIdsStr = ListUtils.changeListToStrBySeparator(deviceIds, ",");

        deviceMapper.findDeviceListByIds(deviceIdsStr);
    }

}