package com.iot.shcs.space.service;

import com.iot.shcs.space.vo.DeviceVo;
import com.iot.shcs.space.vo.SpaceAndSpaceDeviceVo;
import com.iot.shcs.space.vo.SpaceDeviceResp;
import com.iot.shcs.space.vo.SpaceReq;
import com.iot.shcs.space.vo.SpaceReqVo;
import com.iot.shcs.space.vo.SpaceResp;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface ISpaceService  {

    /**
     * 新建空间
     *
     * @return
     * @author wanglei
     */
    Long save(SpaceReq spaceReq);

    /**
     * 更新空间
     *
     * @return
     * @author wanglei
     */
    void update(SpaceReq spaceReq);

    boolean updateSpaceByCondition(SpaceReqVo reqVo);
    /**
    * @Description: 根据spaceId删除space
    *@param tenantId
    * @param spaceId
    * @return:
    * @author: chq
    * @date: 2018/10/11 19:48
    **/
    boolean deleteSpaceBySpaceId(Long tenantId, Long spaceId);

    /**
    * @Description: 批量删除space
    *
    * @param req
    * @return:
    * @author: chq
    * @date: 2018/10/11 19:49
    **/
    boolean deleteSpaceByIds(SpaceAndSpaceDeviceVo req);

    /**
    * @Description: 通过spaceId查找space详情
    *@param tenantId
    * @param spaceId
    * @return:
    * @author: chq
    * @date: 2018/10/11 19:50
    **/
    SpaceResp findSpaceInfoBySpaceId(Long tenantId, Long spaceId);

    /**
    * @Description: 通过spaceIds查找space详情
    *
    * @param req
    * @return:
    * @author: chq
    * @date: 2018/10/11 19:51
    **/
    List<SpaceResp> findSpaceInfoBySpaceIds(SpaceAndSpaceDeviceVo req);

    /**
    * @Description: 根据父级Id查询space
    *
    * @param space
    * @return:
    * @author: chq
    * @date: 2018/10/12 15:48
    **/
    List<SpaceResp> findSpaceByParentId(SpaceReq space);
    /**
    * @Description: 条件查询space
    *（可选择查询条件id、parent_id、name、user_id、location_id、
     * type、sort、tenant_id、default_space、org_id）
    * @param spaceReq
    * @return:
    * @author: chq
    * @date: 2018/10/11 19:52
    **/
    List<SpaceResp> findSpaceByCondition(SpaceReq spaceReq);

    /**
    * @Description: 通过条件统计space数量
    *（可选择查询条件id、parent_id、name、user_id、location_id、
     * type、sort、tenant_id、default_space、org_id）
    * @param spaceReq
    * @return:
    * @author: chq
    * @date: 2018/10/11 19:54
    **/
    int countSpaceByCondition(SpaceReq spaceReq);

    /**
     * 查询空间（家或房间）名称是否存在
     *
     * @param spaceReq
     * @return flag
     */
    boolean checkSpaceName(com.iot.shcs.space.vo.SpaceReq spaceReq);
    
    void deleteSpaceBySpaceIdAndUserId(Long spaceId, Long userId, Long tenantId);

    /**
     * 根据家庭和用户获取设备列表
     *
     * @param userId
     * @param homeId
     * @return payload
     */
    Map<String, Object> getHomeDevListAndCount(Long tenantId,Long userId, Long homeId);


    /**
     * 查找用户的默认家
     *
     * @param userId
     * @return
     */
    SpaceResp findUserDefaultSpace(Long userId, Long tenantId);


    /**
     * 根据用户ID和类型查询空间
     *
     * @param spaceReq
     * @return
     * @author fenglijian
     */
    List<SpaceResp> findSpaceByType(SpaceReq spaceReq);


    //2c
    public List<DeviceVo> getUserUnMountDevice(Long tenantId, Long spaceId, Long userId, Integer room);

    //2c
    public List<DeviceVo> getDeviceByUserRoom(Long tenantId, Long userId, Long spaceId);

    SpaceDeviceResp getSpaceDeviceByDeviceUuid(String deviceUuid, Long tenantId);

    public Integer getDeviceSeqByUserId(Long tenantId, Long userId);

    List<String> getDirectDeviceUuidBySpaceId(Long tenantId, Long spaceId);

}
