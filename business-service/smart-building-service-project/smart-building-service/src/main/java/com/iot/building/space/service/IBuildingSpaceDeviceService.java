package com.iot.building.space.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.iot.common.exception.BusinessException;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceDeviceResp;

import io.swagger.annotations.ApiOperation;

public interface IBuildingSpaceDeviceService{

	public List<SpaceDeviceResp> findSpaceDeviceByCondition(SpaceDeviceReq spaceDeviceReq);

    /**
     * @Description: 条件查找spaceDevice
     * @param spaceDeviceReq
     * @return: java.util.List<com.iot.control.space.vo.SpaceDeviceResp>
     * @author: chq
     * @date: 2018/10/9 21:20
     **/
    public List<SpaceDeviceResp> countSpaceDeviceByCondition(SpaceDeviceReq spaceDeviceReq);

    /**
     * 根据房间ID获取设备信息
     *
     * @param spaceDeviceReq
     * @return
     * @author linjihuang
     */
    public List<Map<String, Object>> getDeviceListBySpace(SpaceDeviceReq spaceDeviceReq);
}


