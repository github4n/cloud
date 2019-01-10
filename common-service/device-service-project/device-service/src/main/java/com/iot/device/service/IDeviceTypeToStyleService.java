package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.device.model.DeviceTypeToStyle;
import com.iot.device.vo.req.DeviceTypeToStyleReq;
import com.iot.device.vo.rsp.DeviceTypeToStyleResp;

import java.util.ArrayList;
import java.util.List;

public interface IDeviceTypeToStyleService extends IService<DeviceTypeToStyle> {

    /**
     * 保存或更新
     * @param deviceTypeToStyleReq
     * @return
     */
    Long saveOrUpdate(DeviceTypeToStyleReq deviceTypeToStyleReq);


    void saveMore(DeviceTypeToStyleReq deviceTypeToStyleReq);

    /**
     * 删除
     * @param ids
     */
    void delete(ArrayList<Long> ids);


    /**
     * 根据deviceTypeId获取
     * @param deviceTypeId
     * @return
     */
    List<DeviceTypeToStyleResp> listDeviceTypeStyleByDeviceTypeId(Long deviceTypeId);


}
