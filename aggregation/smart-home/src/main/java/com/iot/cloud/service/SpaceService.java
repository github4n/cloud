//package com.lds.iot.cloud.service;
//
//import java.util.List;
//import java.util.Map;
//
//import com.lds.iot.cloud.helper.DispatcherMessage;
//import com.lds.iot.cloud.vo.Space;
//import com.lds.iot.cloud.vo.SpaceDevice;
//import com.lds.iot.cloud.vo.SpaceDeviceVo;
//import com.iot.control.scene.vo.SpaceVo;
//import com.iot.common.exception.BusinessException;
//import com.lds.iot.vo.space.SpaceVo;
//
//public interface SpaceService {
//
//    /**
//     * 新建空间
//     *
//     * @param space
//     * @return
//     * @author wanglei
//     */
//    public void save(Space space);
//
//    /**
//     * 更新空间
//     *
//     * @param space
//     * @return
//     * @author wanglei
//     */
//    public void update(Space space);
//
//    /**
//     * 根据空间ID和用户ID删除空间
//     *
//     * @param spaceId
//     * @param userId
//     * @return
//     * @author wanglei
//     */
//    public void delete(String spaceId, String userId) throws BusinessException;
//
//    /**
//     * 根据父节点查询子节点
//     *
//     * @param parentId
//     * @return
//     * @author wanglei
//     */
//    public List<Map<String, Object>> findByParent(String parentId);
//
//    /**
//     * 根据ID查询空间
//     *
//     * @param id
//     * @return
//     * @author wanglei
//     */
//    public Space findById(String id);
//
//    /**
//     * 根据用户ID和类型查询空间
//     *
//     * @param type
//     * @param space
//     * @return
//     * @author fenglijian
//     */
//    public List<Space> findSpaceByType(String type, SpaceVo space);
//
//    /**
//     * 根据用户ID和类型查询空间数量
//     *
//     * @param type
//     * @param space
//     * @return
//     * @author fenglijian
//     */
//    public int findSpaceCountByType(String type, SpaceVo space);
//
//    /**
//     * 根据用户查询空间列表
//     *
//     * @param params
//     * @return
//     * @author wanglei
//     */
//    public List<Space> findSpaceByUser(Map<String, Object> params);
//
//    /**
//     * 根据用户查询顶级空间
//     *
//     * @param userId
//     * @return
//     * @author wanglei
//     */
//    public List<Space> findRootByUser(String userId);
//
//    /**
//     * 查询没有挂载过的空间
//     *
//     * @param params
//     * @return
//     * @author wanglei
//     */
//    public List<Space> findSpaceUnMount(Map<String, Object> params);
//
//    /**
//     * 添加空间设备挂载数据
//     *
//     * @param spaceDevice
//     * @return
//     * @author linjihuang
//     */
//    public int insert(SpaceDevice spaceDevice);
//
//    /**
//     * 根据用户的的根节点查询空间树结构
//     *
//     * @param userId
//     * @return
//     * @author wanglei
//     */
//    public List<Map<String, Object>> findTree(String userId);
//
//    /**
//     * 根据建筑获取楼层和楼层设备总数 ，开的设备总数
//     *
//     * @param buildId
//     * @param types
//     * @return
//     * @author wanglei
//     */
//    public List<Map<String, Object>> getFloorAndDeviceCount(String buildId, String types) throws Exception;
//
//    /**
//     * 根据房间ID获取设备
//     *
//     * @param roomId
//     * @return
//     * @author wanglei
//     */
//    public List<Map<String, Object>> findDeviceByRoom(String roomId) throws Exception;
//
//    /**
//     * 根据房间ID、设备类型获取设备
//     *
//     * @param roomId
//     * @param deviceCategroyTypes
//     * @return
//     * @author QiZY
//     */
//    public List<Map<String, Object>> findDeviceByRoom(String roomId, List<String> deviceCategroyTypes) throws Exception;
//
//    /**
//     * 查询某个空间下所有的分组
//     *
//     * @param space
//     * @return
//     */
//    public List<String> getAllSpace(Space space);
//
//    /**
//     * 查询某个空间下所有的子空间
//     *
//     * @param spaceId
//     * @return
//     */
//    public List<Space> getAllChildSpace(String spaceId);
//
//    /**
//     * 根据空间ID和设备大类查询设备空间下打开的设备
//     *
//     * @param params
//     */
//    public Integer countOnDevice(Map<String, Object> params);
//
//    /**
//     * 根据空间ID和设备大类查询设备空间下打开的设备
//     *
//     * @param params
//     */
//    public Integer countOnLightDevice(Map<String, Object> params);
//
//    /**
//     * 设置空间从属关系
//     *
//     * @param spaceId
//     * @param childIds
//     */
//    public void setSpaceRelation(String spaceId, String childIds) throws Exception;
//
//    /**
//     * 获取直接子空间
//     *
//     * @param spaceId
//     */
//    public List<Space> getChildSpace(String spaceId) throws Exception;
//
//    /**
//     * 获取直接子空间
//     *
//     * @param params
//     */
//    public List<Space> getChildSpace(Map<String, Object> params) throws Exception;
//
//    /**
//     * 获取直接子空间
//     *
//     * @param spaceId
//     * @param spaceType
//     */
//    public List<SpaceVo> getChildSpace(String spaceId, String spaceType) throws Exception;
//
//    /**
//     * 根据房间信息判断该房间灯的亮灭标识 0 代表灭  1 亮
//     *
//     * @param spaceIds
//     * @return
//     */
//    public int judgeSpaceSwitchStatus(List<String> spaceIds);
//
//    /**
//     * 获取空间下直接挂载的设备
//     *
//     * @param spaceId
//     */
//    public List<Map<String, Object>> getDirectDeviceBySpace(String spaceId);
//
//    /**
//     * 获取空间下直接挂载的设备
//     *
//     * @param spaceId
//     * @param deviceJson
//     */
//    public void updateSpaceDevicePosition(String spaceId, String deviceJson);
//
//    /**
//     * 根据设备查询归属直接空间
//     *
//     * @param deviceId
//     * @return
//     */
//    public List<Map<String, Object>> findSpaceNameByDevice(String deviceId);
//
//    /**
//     * 根据ID查询归属空间
//     *
//     * @param id
//     * @return
//     */
//    public Space findSpaceNameById(String id);
//
//    /**
//     * 情景或者群控控制
//     *
//     * @param spaceId     空间Id
//     * @param targerValue 设备的目标值
//     * @param templateId  模板ID  和 targerValue 二选一
//     * @author wanglei
//     */
//    public void publicGroupOrSceneControl(String spaceId, Map<String, Object> targerValue, String templateId) throws BusinessException, Exception;
//
//    /**
//     * 更新设备挂载状态
//     *
//     * @param spaceDeviceVO
//     * @return
//     */
//    public int updateSpaceDeviceStatus(SpaceDeviceVo spaceDeviceVO);
//
//    /**
//     * 查询未挂载到网关的房间信息
//     *
//     * @param status
//     * @return
//     */
//    public List<SpaceDeviceVo> findSpaceInfo(String status);
//
//    /**
//     * 查询空间挂载的设备信息
//     *
//     * @param spaceDeviceVO
//     * @return
//     */
//    public int findSpaceMount(SpaceDeviceVo spaceDeviceVO);
//
//    /**
//     * 删除设备挂载信息
//     *
//     * @param spaceDeviceVO
//     * @return
//     */
//    public int deleteBySpace(SpaceDeviceVo spaceDeviceVO);
//
//    /**
//     * 删除设备与空间的挂载关系
//     *
//     * @param deviceId
//     * @return
//     */
//    int deleteSpaceDeviceByDeviceId(String deviceId);
//
//
//    /**
//     * 根据设备和空间统计数量 wanglei
//     *
//     * @param deviceId
//     * @param spaceList
//     * @return
//     */
//    public Integer countBySpaceAndDevice(String deviceId, List<String> spaceList);
//
//    /**
//     * 查询数据统计
//     *
//     * @param spaceId 房间ID
//     * @author fenglijian
//     */
//    public List<String> queryDataReport(String spaceId);
//
//    /**
//     * 根据设备ID获取房间ID
//     *
//     * @param deviceId 设备ID
//     * @author fenglijian
//     */
//    public SpaceDevice findSpaceIdByDeviceId(String deviceId);
//
//    public void setStyle(String spaceId, String style);
//
//    public String getStyle(String spaceId);
//
//    /**
//     * 根据家庭和用户获取设备列表
//     *
//     * @param userId
//     * @param homeId
//     * @return payload
//     */
//    public Map<String, Object> getHomeDevListAndCount(String userId, String homeId);
//
//    /**
//     * 根据房间获取设备列表
//     *
//     * @param userId
//     * @return payload
//     */
//    public Map<String, Object> getRoomDevListAndCount(String userId, String roomId);
//
//    /**
//     * 根据父节点查询子节点
//     *
//     * @param space
//     * @return
//     * @author fenglijian
//     */
//    public List<Map<String, Object>> findSpaceByParentId(SpaceVo space);
//
//    /**
//     * 根据父节点查询子节点数量
//     *
//     * @param space
//     * @return
//     * @author fenglijian
//     */
//    public int findSpaceCountByParentId(SpaceVo space);
//
//    public void testMQTT(DispatcherMessage message, String topic);
//
//
//    /**
//     * 获取家庭下的设备列表
//     *
//     * @param spaceList
//     * @return payload
//     */
//    public Map<String, Object> spaceToMapAndFindDeviceList(String userId, List<Space> spaceList);
//
//    /**
//     * 获取房间下设备列表
//     *
//     * @param space
//     * @return
//     */
//    public List<Map<String, Object>> getRoomDeviceList(String userId, Space space);
//
//    /**
//     * 查询空间（家或房间）名称是否存在
//     *
//     * @param spaceName
//     * @param userId
//     * @param spaceType
//     * @return flag
//     */
//    public boolean checkSpaceName(String spaceName, String userId, String spaceType, String id, String parentId);
//
//    /**
//     * 查找用户的默认家
//     *
//     * @param userId
//     * @return
//     */
//    Space findUserDefaultSpace(String userId);
//
//    int updateSpaceByUserId(Space space);
//}
