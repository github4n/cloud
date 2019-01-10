package com.iot.shcs.space.service;

import com.iot.control.space.vo.SpaceAndSpaceDeviceVo;

import java.util.List;

public interface ISpaceDeviceService  {


    /**
    * @Description: 增加spaceDevice
    *
    * @param spaceDeviceReq
    * @return:
    * @author: chq
    * @date: 2018/10/12 17:14
    **/
    boolean inserSpaceDevice(com.iot.shcs.space.vo.SpaceDeviceReq spaceDeviceReq);

    /**
    * @Description: 更新spaceDevice
    *
    * @param spaceDeviceReq
    * @return:
    * @author: chq
    * @date: 2018/10/12 17:18
    **/
    boolean updateSpaceDevice(com.iot.shcs.space.vo.SpaceDeviceReq spaceDeviceReq);

    boolean insertOrUpdateSpaceDeviceByDevId(com.iot.shcs.space.vo.SpaceDeviceReq spaceDeviceReq);

    /**
     * @Description: 批量插入spaceDevice
     *
     * @param spaceDeviceReqs
     * @return:
     * @author: chq
     * @date: 2018/10/12 11:41
     **/
    boolean saveSpaceDeviceList(List<com.iot.shcs.space.vo.SpaceDeviceReq> spaceDeviceReqs);

    /**
    * @Description: 批量更新spaceDevice
    *
    * @param spaceDeviceReqs
    * @return:
    * @author: chq
    * @date: 2018/10/12 17:35
    **/
    boolean updateSpaceDevices(List<com.iot.shcs.space.vo.SpaceDeviceReq> spaceDeviceReqs);
    /**
    * @Description: 根据条件查询spaeDevice，为增加缓存
    *（可选择查询条件id、device_id、space_id、status、position、tenant_id、
     * location_id、device_type_id、order、business_type_id、product_id）
    * @param spaceDeviceReq
    * @return:
    * @author: chq
    * @date: 2018/10/11 20:14
    **/
    List<com.iot.shcs.space.vo.SpaceDeviceResp> findSpaceDeviceByCondition(com.iot.shcs.space.vo.SpaceDeviceReq spaceDeviceReq);

    /**
    * @Description: 通过spaceId查找spaceDevice
    *   通过order及id 排序
    * @param spaceId
    * @return:
    * @author: chq
    * @date: 2018/10/12 10:02
    **/
    List<com.iot.shcs.space.vo.SpaceDeviceVo> findSpaceDeviceVOBySpaceId(Long spaceId);

    List<com.iot.shcs.space.vo.SpaceDeviceResp> findSpaceDeviceBySpaceIdsOrDeviceIds(SpaceAndSpaceDeviceVo req);

    /**
    * @Description: 根据条件统计spaceDevice数量
    *
    * @param spaceDeviceReq
    * @return:
    * @author: chq
    * @date: 2018/10/11 20:16
    **/
    int countSpaceDeviceByCondition(com.iot.shcs.space.vo.SpaceDeviceReq spaceDeviceReq);

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
    boolean updateSpaceDeviceByCondition(com.iot.shcs.space.vo.SpaceDeviceReqVo reqVo);

    /**
    * @Description: 通过DeviceId删除spaceDevice
    *
    * @param deviceId
    * @return:
    * @author: chq
    * @date: 2018/10/12 9:46
    **/
    int deleteSpaceDeviceByDeviceId(Long tenantId, String deviceId);
    /**
     * @Description: 批量删除spaceDevice
     *
     * @param deviceIds
     * @return:
     * @author: chq
     * @date: 2018/10/12 9:46
     **/
    void deleteBatchSpaceDeviceByDevIds(Long tenantId, List<String> deviceIds);

    /**
    * @Description: 批量删除spaceDevice
    *
    * @param req
    * @return:
    * @author: chq
    * @date: 2018/10/12 11:43
    **/
    boolean deleteSpaceDeviceBySpaceIdsOrDeviceIds(com.iot.shcs.space.vo.SpaceAndSpaceDeviceVo req);

    /**
     * @Description: 通过deviceId 查找spaceDevice及space信息
     *
     * @param req
     * @return: com.iot.control.space.vo.SpaceDeviceVo
     * @author: chq
     * @date: 2018/10/15 12:17
     **/
    public List<com.iot.shcs.space.vo.SpaceDeviceVo> findSpaceDeviceInfoByDeviceIds(com.iot.shcs.space.vo.SpaceAndSpaceDeviceVo req);
}


