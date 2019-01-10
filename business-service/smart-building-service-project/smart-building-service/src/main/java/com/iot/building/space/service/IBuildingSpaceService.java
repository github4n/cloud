package com.iot.building.space.service;

import java.util.List;
import java.util.Map;

import com.iot.building.space.vo.*;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.control.space.vo.SpaceReq;
import com.iot.control.space.vo.SpaceResp;
import com.iot.control.space.vo.SpaceVo;
import com.iot.device.vo.req.DeviceBusinessTypeIDSwitchReq;
import org.springframework.web.multipart.MultipartFile;


public interface IBuildingSpaceService {


    /**
     * 根据用户查询空间列表
     *
     * @return
     * @author wanglei
     */
    List<SpaceResp> findSpaceByLocationId(Long locationId, Long orgId,Long tenantId, String name);

    /**
     * 根据用户查询空间列表
     *
     * @return
     * @author wanglei
     */
    Page<SpaceResp> findSpacePageByLocationId(Long locationId,Long orgId, Long tenantId, String name, int pageNumber, int pageSize);

    /**
     * 根据locationId和TenantId查询空间列表
     *
     * @return
     * @author wanglei
     */
    List<Map<String, Object>> findTreeSpaceByLocationId(Long locationId,Long orgId,Long tenantId);

    /**
     * 查询没有挂载过的空间
     *
     * @return
     * @author wanglei
     */
//    List<SpaceResp> findSpaceUnMountByLocationIdAndName(Long locationId, String name);

    /**
     * 根据用户的的根节点查询空间树结构
     *
     * @param locationId
     * @return
     * @author wanglei
     */
//    List<Map<String, Object>> findTree(Long locationId,Long orgId, Long tenantId);

    /**
     * 挂载设备
     *
     * @return
     * @author wanglei
     */
    void mount(SpaceDeviceReq spaceDeviceReq) throws Exception;

    /**
     * 移除挂载设备
     *
     * @return
     * @author linjihuang
     */
    void removeMount(SpaceDeviceReq spaceDeviceReq) throws Exception;

    /**
     * 根据建筑获取楼层和楼层设备总数 ，开的设备总数
     *
     * @param buildId
     * @param types
     * @return
     * @author wanglei
     */
    List<Map<String, Object>> getFloorAndDeviceCount(Long buildId, String types,Long orgId,Long tenantId) throws BusinessException;

    /**
     * 根据房间ID获取设备
     *
     * @param spaceId
     * @return
     * @author wanglei
     */
    List<Map<String, Object>> findDeviceByRoom(Long spaceId,Long orgId,Long tenantId) throws BusinessException;

    /**`
     * 根据房间ID、设备类型获取设备
     *
     * @param roomId
     * @return
     * @author QiZY
     */
    List<Map<String, Object>> findDeviceByRoomAndDeviceType(Long roomId, List<String> devicTypes,Long orgId,Long tenantId) throws BusinessException;
    /**
     * 根据房间ID、设备类型获取设备
     *
     * @param roomId
     * @return
     * @author QiZY
     */
    List<Map<String, Object>> findDeviceByRoomAndDeviceBusinessType(Long roomId, List<String> businessTypes,Long orgId,Long tenantId) throws BusinessException;

    /**
     * 控制设备
     *
     * @param spaceId
     * @param deviceType
     * @param propertyMap 目标值
     * @return
     * @author wanglei
     */
    Boolean groupControl(Long spaceId, String deviceType, Map<String, Object> propertyMap,Long orgId,Long tenantId) throws BusinessException;

    /**
     * 单控设备
     *
     * @param deviceId
     * @param propertyMap 目标值
     * @return
     * @author wanglei
     */
    Boolean control(String deviceId, Map<String, Object> propertyMap) throws BusinessException;

    /**
     * 查询某个空间下所有的分组
     *
     * @param space
     * @return
     */
    List<Long> getAllSpace(SpaceReq space);

    /**
     * 根据空间ID和设备大类查询设备空间下打开的设备
     *
     */
    Integer countOnDevice(DeviceBusinessTypeIDSwitchReq req);

    /**
     * 根据空间ID和设备大类查询设备空间下打开的设备
     *
     * @param params
     */
    Integer countOnLightDevice(Map<String, Object> params);

    /**
     * 设置空间从属关系
     *
     * @param spaceId
     * @param childIds
     */
    void setSpaceRelation(Long spaceId, String childIds,Long orgId,Long tenantId) throws BusinessException;

    /**
     * 获取直接子空间
     *
     */
    List<SpaceResp> getChildSpace(SpaceReq spaceReq) throws BusinessException;

    /**
     * 获取直接子空间
     *
     * @param params
     */
//    List<Space> getChildSpace(Map<String, Object> params) throws BusinessException;

    /**
     * 获取直接子空间
     *
     * @param spaceId
     * @param spaceType
     */
    List<SpaceVo> getChildSpaceStatus(Long spaceId, String spaceType,Long orgId,Long tenantId);

    /**
     * 获取空间下直接挂载的设备
     *
     * @param spaceId
     */
    List<Map<String, Object>> getDirectDeviceBySpace(Long spaceId,Long orgId,Long tenantId);

    /**
     * 获取已挂载的设备列表
     *
     * @param spaceDeviceReq
     */
//    List<String> getMountDeviceBySpaceId(SpaceDeviceReq spaceDeviceReq);

    /**
     * 获取空间下直接挂载的设备
     *
     */
    void updateSpaceDevicePosition(SpaceDeviceReq spaceDeviceReq);
    /**
     * 获取空间下直接挂载的设备
     *
     */
    void updateSpaceDevice(SpaceDeviceReq spaceDeviceReq);

    /**
     * 情景或者群控控制
     *
     * @param spaceId     空间Id
     * @param targerValue 设备的目标值
     * @param templateId  模板ID  和 targerValue 二选一
     * @author wanglei
     */
    void publicGroupOrSceneControl(Long spaceId, Map<String, Object> targerValue, Long templateId,Long orgId,Long tenantId) throws BusinessException;

    /**
     * 查询未挂载到网关的房间信息
     *
     * @param status
     * @return
     */
//    List<SpaceDeviceVo> findSpaceInfo(String status);

    /**
     * 查询空间挂载的设备信息
     *
     * @param spaceDeviceVo spaceId,deviceId
     * @return
     */
//    int findSpaceMount(SpaceDeviceVo spaceDeviceVo);

    /**
     * 根据设备和空间统计数量 wanglei
     *
     * @param deviceId
     * @param spaceList
     * @return
     */
//    Integer countBySpaceAndDevice(String deviceId, List<String> spaceList);

    /**
     * 根据设备ID获取房间ID
     *
     * @param deviceId 设备ID
     * @author fenglijian
     */
    SpaceDeviceResp findSpaceIdByDeviceId(String deviceId,Long orgId,Long tenantId);

    /**
     * 查询网关设备在楼层的数量
     *
     * @return
     */
    int saveSpaceDevice(List<SpaceDeviceReq> spaceDeviceList);

    /**
     * 根据租户查询空间列表
     *
     * @param tenantId
     * @return
     * @author fenglijian
     */
    List<SpaceResp> findSpaceByTenantId(Long orgId,Long tenantId);

    void syncSpaceStatus(String deviceId);


    /////////////////////////////////////////////////

    /**
     * 根据父节点查询子节点数量
     *
     * @param space
     * @return
     * @author fenglijian
     */
    int findSpaceCountByParentId(SpaceReq space);

    /**
     * 查询空间（家或房间）名称是否存在
     *
     * @param spaceReq
     * @return flag
     */
    boolean checkSpaceName(SpaceReq spaceReq);

    /**
     * 根据用户ID和类型查询空间
     *
     * @param spaceReq
     * @return
     * @author fenglijian
     */
    List<SpaceResp> findSpaceByType(SpaceReq spaceReq);

    /**
     * 根据用户ID和类型查询空间数量
     *
     * @param space
     * @return
     * @author fenglijian
     */
//    int findSpaceCountByType(SpaceReq space);

//    public Set<String> countGatewayDeviceOnRoom(Long spaceId);

    /**
     * 查询没有挂载过的空间
     *
     * @return
     * @author wanglei
     */
//    public List<SpaceResp> findSpaceUnMount(SpaceReq spaceReq);


    List<SpaceResp> getSpaceByCondition(SpaceReq spaceReq);

    public List<Map<String, Object>> getMeetingSpaceTree(Long tenantId,Long orgId, Long locatinoId);

    public List<String> getDeviceIdBySpaceId(Long spaceId,Long orgId,Long tenantId);

    public List<LocationResp> findLocationByCondition(LocationReq locationReq);

    public void saveLocation(LocationReq locationReq);

    public void updateLocation(LocationReq locationReq);

    public void delLocation(Long id);

    public CommonResponse spaceDataImport(SpaceExcelReq spaceExcelReq);

    public String saveSpaceBackgroundImgImport(SpaceBackgroundImgReq req);

    public List<SpaceBackgroundImgResp> getSpaceBackgroundImg(SpaceBackgroundImgReq req);

    public SpaceBackgroundImgResp getSpaceBackgroundImgById(Long id);

    public Integer deleteSpaceBackgroundImg(Long id);

    public Integer updateSpaceBackgroundImg(SpaceBackgroundImgReq req);

    List<String> getDirectDeviceUuidBySpaceId(Long spaceId);
    
    public List<DeploymentResp> getDeploymentList(DeploymentReq req);
	
	public DeploymentResp findDeploymentById(Long tenantId,Long orgId,Long id);
	
	public List<SpaceResp> findSpaceListByDeployId(Long deployId, Long tenantId,Long orgId, Long locationId);

    public void deleteMountByDeviceIds(String deviceIds,Long tenantId,Long orgId);

    int getSpaceStatus(Long spaceId,Long orgId,Long tenantId);
    
    int setSpaceStatus(Long spaceId,Long orgId,Long tenantId);

    List<SpaceResp> findByIds(Long tenantId,Long orgId, List<Long> ids);
    
    void saveOrUpdate(DeploymentReq req);
    
    void deleteBatchDeploy(String deployIds);

    void addOrUpdateCalendar(CalendarReq calendarReq);

    void deleteCalendar(Long tenantId,Long orgId, Long id);

    Page<CalendarResp> findCalendarList(String pageNum, String pageSize, String name);

    List<CalendarResp> findCalendarListNoPage(String name);

    CalendarResp getCalendarIofoById(Long tenantId,Long orgId, Long id);

    Page<DeploymentResp> getDeploymentPage(DeploymentReq req);
    
    List<Long> getLocationTenant();
    
    Integer countExistCalendar(Long currentTime,int type,Long locationId,Long orgId,Long tenantId);
    
    public DeploymentResp findByName(Long orgId,String name);
}
