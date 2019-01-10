package com.iot.control.space.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.control.space.domain.SpaceDevice;
import com.iot.control.space.vo.SpaceAndSpaceDeviceVo;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceDeviceReqVo;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.control.space.vo.SpaceDeviceVo;

import java.util.List;

public interface ISpaceDeviceService extends IService<SpaceDevice> {

    /**
    * @Description: 增加spaceDevice
    *
    * @param spaceDeviceReq
    * @return:
    * @author: chq
    * @date: 2018/10/12 17:14
    **/
    boolean inserSpaceDevice(SpaceDeviceReq spaceDeviceReq);

    /**
    * @Description: 更新spaceDevice
    *
    * @param spaceDeviceReq
    * @return:
    * @author: chq
    * @date: 2018/10/12 17:18
    **/
    boolean updateSpaceDevice(SpaceDeviceReq spaceDeviceReq);

    boolean insertOrUpdateSpaceDeviceByDevId(SpaceDeviceReq spaceDeviceReq);

    /**
     * @Description: 批量插入spaceDevice
     *
     * @param spaceDeviceReqs
     * @return:
     * @author: chq
     * @date: 2018/10/12 11:41
     **/
    boolean saveSpaceDeviceList(List<SpaceDeviceReq> spaceDeviceReqs);

    /**
    * @Description: 批量更新spaceDevice
    *
    * @param spaceDeviceReqs
    * @return:
    * @author: chq
    * @date: 2018/10/12 17:35
    **/
    boolean updateSpaceDevices(List<SpaceDeviceReq> spaceDeviceReqs);
    /**
    * @Description: 根据条件查询spaeDevice，为增加缓存
    *（可选择查询条件id、device_id、space_id、status、position、tenant_id、
     * location_id、device_type_id、order、business_type_id、product_id）
    * @param spaceDeviceReq
    * @return:
    * @author: chq
    * @date: 2018/10/11 20:14
    **/
    List<SpaceDeviceResp> findSpaceDeviceByCondition(SpaceDeviceReq spaceDeviceReq);

    /**
    * @Description: 通过spaceId查找spaceDevice
    *   通过order及id 排序
     *   @param tenantId
     * @param spaceId
    * @return:
    * @author: chq
    * @date: 2018/10/12 10:02
    **/
    List<SpaceDeviceVo> findSpaceDeviceVOBySpaceId(Long tenantId, Long spaceId);


    List<SpaceDeviceResp> findSpaceDeviceBySpaceIdsOrDeviceIds(SpaceAndSpaceDeviceVo req);
    /**
    * @Description: 根据条件统计spaceDevice数量
    *
    * @param spaceDeviceReq
    * @return:
    * @author: chq
    * @date: 2018/10/11 20:16
    **/
    int countSpaceDeviceByCondition(SpaceDeviceReq spaceDeviceReq);

    /**
    * @Description:选择条件更新spaceDevice
    *
    * @param reqVo
    *SpaceDeviceReq setValueParam;需要修改的对象
    *SpaceDeviceReq requstParam;where查找条件
    * @return:
    * @author: chq
    * @date: 2018/10/11 20:43
    **/
    boolean updateSpaceDeviceByCondition(SpaceDeviceReqVo reqVo);

    /**
    * @Description: 通过DeviceId删除spaceDevice
    *@param tenantId
    * @param deviceId
    * @return:
    * @author: chq
    * @date: 2018/10/12 9:46
    **/
    int deleteSpaceDeviceByDeviceId(Long tenantId, String deviceId);

    /**
    * @Description: 批量删除spaceDevice
    *
    * @param req
    * @return:
    * @author: chq
    * @date: 2018/10/12 11:43
    **/
    boolean deleteSpaceDeviceBySpaceIdsOrDeviceIds(SpaceAndSpaceDeviceVo req);

    /**
     * @Description: 通过deviceId 查找spaceDevice及space信息
     *
     * @param req
     * @return: com.iot.control.space.vo.SpaceDeviceVo
     * @author: chq
     * @date: 2018/10/15 12:17
     **/
    List<SpaceDeviceVo> findSpaceDeviceInfoByDeviceIds(SpaceAndSpaceDeviceVo req);
}


