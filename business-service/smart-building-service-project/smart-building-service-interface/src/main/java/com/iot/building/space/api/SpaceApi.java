package com.iot.building.space.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iot.building.space.api.fallback.SpaceApiFallbackFactory;
import com.iot.building.space.vo.CalendarReq;
import com.iot.building.space.vo.CalendarResp;
import com.iot.building.space.vo.DeploymentReq;
import com.iot.building.space.vo.DeploymentResp;
import com.iot.building.space.vo.LocationReq;
import com.iot.building.space.vo.LocationResp;
import com.iot.building.space.vo.QueryParamReq;
import com.iot.building.space.vo.SpaceBackgroundImgReq;
import com.iot.building.space.vo.SpaceBackgroundImgResp;
import com.iot.building.space.vo.SpaceExcelReq;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceReq;
import com.iot.control.space.vo.SpaceResp;
import com.iot.control.space.vo.SpaceVo;
import com.iot.device.vo.req.DeviceBusinessTypeIDSwitchReq;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("空间接口")
@FeignClient(value = "building-control-service", fallbackFactory = SpaceApiFallbackFactory.class)
@RequestMapping("/buildSpace")
public interface SpaceApi {

    /**
     * 根据用户查询空间列表
     *
     * @return
     * @author wanglei
     */
    @ApiOperation("根据用户查询空间列表")
    @RequestMapping(value = "/findSpacePageByLocationId", method = RequestMethod.GET)
    Page<SpaceResp> findSpacePageByLocationId(@RequestParam("locationId") Long locationId,@RequestParam("orgId") Long orgId,
                                              @RequestParam("tenantId") Long tenantId, @RequestParam("name") String name,
                                              @RequestParam("pageNumber") int pageNumber, @RequestParam("pageSize") int pageSize);

    @ApiOperation("根据用户查询空间列表")
    @RequestMapping(value = "/findSpaceByLocationId", method = RequestMethod.GET)
    List<SpaceResp> findSpaceByLocationId(@RequestParam("locationId") Long locationId,@RequestParam("orgId") Long orgId,
                                          @RequestParam("tenantId") Long tenantId, @RequestParam("name") String name);

    /**
     * 根据locationId和TenantId查询空间列表
     *
     * @return
     * @author wanglei
     */
    @ApiOperation("根据locationId和TenantId查询空间列表")
    @RequestMapping(value = "/findTreeSpaceByLocationId", method = RequestMethod.GET)
    List<Map<String, Object>> findTreeSpaceByLocationId(@RequestParam("tenantId") Long tenantId,@RequestParam("orgId") Long orgId, @RequestParam("locationId") Long locationId);

    /**
     * 根据用户的的根节点查询空间树结构
     *
     * @return
     * @author wanglei
     */
//    @ApiOperation("根据用户的的根节点查询空间树结构")
//    @RequestMapping(value = "/findTree", method = RequestMethod.GET)
//    List<Map<String, Object>> findTree(@RequestParam("locationId") Long locationId,@RequestParam("orgId") Long orgId,@RequestParam("tenantId")Long tenantId);

    /**
     * 挂载设备
     *
     * @return
     * @author wanglei
     */
    @ApiOperation("挂载设备")
    @RequestMapping(value = "/mount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void mount(@RequestBody SpaceDeviceReq spaceDeviceReq) throws Exception;

    /**
     * 移除挂载设备
     *
     * @return
     * @author linjihuang
     */
    @ApiOperation("移除挂载设备")
    @RequestMapping(value = "/removeMount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void removeMount(@RequestBody SpaceDeviceReq spaceDeviceReq) throws Exception;


    @ApiOperation("移除挂载设备")
    @RequestMapping(value = "/deleteMountByDeviceIds", method = RequestMethod.GET)
    void deleteMountByDeviceIds(@RequestParam("tenantId") Long tenantId,@RequestParam("orgId") Long orgId, @RequestParam("deviceIds") String deviceIds)throws Exception;

    /**
     * 根据建筑获取楼层和楼层设备总数 ，开的设备总数
     *
     * @param buildId
     * @param types
     * @return
     * @author wanglei
     */
    @ApiOperation("根据建筑获取楼层和楼层设备总数 ，开的设备总数")
    @RequestMapping(value = "/getFloorAndDeviceCount", method = RequestMethod.GET)
    List<Map<String, Object>> getFloorAndDeviceCount(@RequestParam("tenantId") Long tenantId,@RequestParam("orgId") Long orgId, @RequestParam("buildId") Long buildId, @RequestParam("types") String types) throws BusinessException;

    /**
     * 根据房间ID获取设备
     *
     * @param spaceId
     * @return
     * @author wanglei
     */
    @ApiOperation("根据房间ID获取设备")
    @RequestMapping(value = "/findDeviceByRoomId", method = RequestMethod.GET)
    List<Map<String, Object>> findDeviceByRoom(@RequestParam("spaceId") Long spaceId,@RequestParam("orgId") Long orgId, @RequestParam("tenantId") Long tenantId) throws BusinessException;

    /**
     * 根据房间ID、设备类型获取设备
     *
     * @param req
     * @return
     * @author QiZY
     */
    @ApiOperation("根据房间ID、设备类型获取设备")
    @RequestMapping(value = "/findDeviceByRoomAndDeviceBusinessType", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<Map<String, Object>> findDeviceByRoomAndDeviceBusinessType(@RequestBody QueryParamReq req) throws BusinessException;
    /**
     * 根据房间ID、设备类型获取设备
     *
     * @param req
     * @return
     * @author QiZY
     */
    @ApiOperation("根据房间ID、设备类型获取设备")
    @RequestMapping(value = "/findDeviceByRoomAndDeviceType", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<Map<String, Object>> findDeviceByRoomAndDeviceType(@RequestBody QueryParamReq req) throws BusinessException;

    /**
     * 控制设备
     *
     * @param propertyMap
     * @param propertyMap 目标值
     * @return
     * @author wanglei
     */
    @ApiOperation("控制设备")
    @RequestMapping(value = "/groupControl", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Boolean groupControl(@RequestBody Map<String, Object> propertyMap) throws BusinessException;

    /**
     * 单控设备
     *
     * @param propertyMap 目标值
     * @return
     * @author wanglei
     */
    @ApiOperation("单控设备")
    @RequestMapping(value = "/control", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Boolean control(@RequestBody Map<String, Object> propertyMap) throws BusinessException;

    /**
     * 查询某个空间下所有的分组
     *
     * @param spaceReq
     * @return
     */
    @ApiOperation("查询某个空间下所有的分组")
    @RequestMapping(value = "/getAllSpace", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<Long> getAllSpace(@RequestBody SpaceReq spaceReq);

    /**
     * 查询某个空间下所有的子空间
     *
     * @param spaceId
     * @return
     */
//    @ApiOperation("查询某个空间下所有的子空间")
//    @RequestMapping(value = "/getAllChildSpace", method = RequestMethod.GET)
//    List<SpaceResp> getAllChildSpace(@RequestParam("tenantId") Long tenantId, @RequestParam("spaceId") Long spaceId);

    /**
     * 根据空间ID和设备大类查询设备空间下打开的设备
     *
     */
    @ApiOperation("根据空间ID和设备大类查询设备空间下打开的设备")
    @RequestMapping(value = "/countOnDevice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Integer countOnDevice(@RequestBody DeviceBusinessTypeIDSwitchReq req);

    /**
     * 根据空间ID和设备大类查询设备空间下打开的设备
     *
     */
    @ApiOperation("根据空间ID和设备大类查询设备空间下打开的设备")
    @RequestMapping(value = "/countOnLightDevice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Integer countOnLightDevice(@RequestBody DeviceBusinessTypeIDSwitchReq req);

    /**
     * 设置空间从属关系
     *
     * @param spaceId
     * @param childIds
     */
    @ApiOperation("设置空间从属关系")
    @RequestMapping(value = "/setSpaceRelation", method = RequestMethod.GET)
    void setSpaceRelation(@RequestParam("tenantId") Long tenantId, @RequestParam("orgId") Long orgId,@RequestParam("spaceId") Long spaceId, @RequestParam("childIds") String childIds) throws BusinessException;

    /**
     * 获取直接子空间
     *
     * @param spaceReq
     */
    @ApiOperation("获取直接子空间")
    @RequestMapping(value = "/getChildSpace", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<SpaceResp> getChildSpace(@RequestBody SpaceReq spaceReq) throws BusinessException;

    @ApiOperation("获取直接子空间")
    @RequestMapping(value = "/getChildSpaceStatus", method = RequestMethod.GET)
    List<SpaceVo> getChildSpaceStatus(@RequestParam("tenantId") Long tenantId,@RequestParam("orgId") Long orgId, @RequestParam("buildId") Long buildId, @RequestParam("type") String type) throws BusinessException;

    @ApiOperation("获取直接子空间")
    @RequestMapping(value = "/getSpaceByCondition", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<SpaceResp> getSpaceByCondition(@RequestBody SpaceReq spaceReq) throws BusinessException;

    /**
     * 获取直接子空间
     *
     * @param params
     */
//    List<Space> getChildSpace(Map<String, Object> params) throws BusinessException;

    /**
     * 获取空间下直接挂载的设备
     *
     * @param spaceId
     */
    @ApiOperation("获取空间下直接挂载的设备")
    @RequestMapping(value = "/getDirectDeviceBySpace", method = RequestMethod.GET)
    List<Map<String, Object>> getDirectDeviceBySpace(@RequestParam("tenantId") Long tenantId,@RequestParam("orgId") Long orgId, @RequestParam("spaceId") Long spaceId);

    /**
     * 获取已挂载的设备列表
     *
     * @param spaceDeviceReq
     */
    @ApiOperation("获取已挂载的设备列表")
    @RequestMapping(value = "/getMountDeviceBySpaceId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<String> getMountDeviceBySpaceId(@RequestBody SpaceDeviceReq spaceDeviceReq);

    /**
     * 获取空间下直接挂载的设备
     */
    @ApiOperation("更新空间位置")
    @RequestMapping(value = "/updateSpaceDevicePosition", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateSpaceDevicePosition(@RequestBody SpaceDeviceReq spaceDeviceReq);

    /**
     * 更新设备挂载状态
     *
     * @return
     */
    @ApiOperation("更新设备挂载状态")
    @RequestMapping(value = "/updateSpaceDeviceStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int updateSpaceDeviceStatus(@RequestBody SpaceDeviceReq spaceDeviceReq);

    /**
     * 根据设备和空间统计数量 wanglei
     *
     * @param queryParamReq
     * @return
     */
    //@RequestParam("deviceId") String deviceId, List<String> spaceList
    @ApiOperation("根据设备和空间统计数量")
    @RequestMapping(value = "/countBySpaceAndDevice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Integer countBySpaceAndDevice(@RequestBody QueryParamReq queryParamReq);

    /**
     * 查询数据统计
     *
     * @param spaceId  房间ID
     * @param deviceId 设备ID[温度，湿度，PM2.5 ...]
     * @param dateType 日期类型[天（24小时），星期，月]
     * @author fenglijian
     */
//    Map<String, Object> queryDataReport(Long spaceId, String deviceId, String dateType);

    /**
     * 描述：查询房间挂载信息
     *
     * @return
     * @throws Exception
     * @since
     */
//    @ApiOperation("查询房间挂载信息")
//    @RequestMapping(value = "/spaceDeviceInfo", method = RequestMethod.GET)
//    List<SpaceDeviceResp> spaceDeviceInfo(@RequestParam("tenantId") Long tenantId);

    /**
     * 根据租户查询空间列表
     *
     * @param tenantId
     * @return
     * @author fenglijian
     */
    @ApiOperation("根据租户查询空间列表")
    @RequestMapping(value = "/findSpaceByTenantId", method = RequestMethod.GET)
    List<SpaceResp> findSpaceByTenantId(@RequestParam("tenantId") Long tenantId,@RequestParam("orgId") Long orgId);

    @ApiOperation("同步空间状态信息")
    @RequestMapping(value = "/syncSpaceStatus", method = RequestMethod.GET)
    void syncSpaceStatus(@RequestParam("deviceId") String deviceId);

    /**
     * 根据父节点查询子节点数量
     *
     * @param space
     * @return
     * @author fenglijian
     */
    @ApiOperation("根据父节点查询子节点数量")
    @RequestMapping(value = "/findSpaceCountByParentId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int findSpaceCountByParentId(@RequestBody SpaceReq space);

    /**
     * 查询空间（家或房间）名称是否存在
     *
     * @param spaceReq
     * @return flag
     */
    @ApiOperation("查询空间（家或房间）名称是否存在")
    @RequestMapping(value = "/checkSpaceName", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean checkSpaceName(@RequestBody SpaceReq spaceReq);

    /**
     * 根据用户ID和类型查询空间
     *
     * @param spaceReq
     * @return
     * @author fenglijian
     */
    @ApiOperation("根据用户ID和类型查询空间")
    @RequestMapping(value = "/findSpaceByType", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<SpaceResp> findSpaceByType(@RequestBody SpaceReq spaceReq);

    @ApiOperation("统计网关设备在房间的数量")
    @RequestMapping(value = "/countGatewayDeviceOnRoom", method = RequestMethod.GET)
    Set<String> countGatewayDeviceOnRoom(@RequestParam("tenantId") Long tenantId, @RequestParam("orgId") Long orgId,@RequestParam("spaceId") Long spaceId);

    @ApiOperation("查询未挂载的房间")
    @RequestMapping(value = "/findSpaceUnMount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<SpaceResp> findSpaceUnMount(@RequestBody SpaceReq spaceReq);

    @ApiOperation("查询会议树结构")
    @RequestMapping(value = "/getMeetingSpaceTree", method = RequestMethod.GET)
    List<Map<String, Object>> getMeetingSpaceTree(@RequestParam("tenatnId") Long tenatnId,@RequestParam("orgId") Long orgId, @RequestParam("locationId") Long locationId);

    @ApiOperation("条件查询location")
    @RequestMapping(value = "/findLocationByCondition", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<LocationResp> findLocationByCondition(@RequestBody LocationReq locationReq);

    @ApiOperation("保存location")
    @RequestMapping(value = "/saveLocation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveLocation(@RequestBody LocationReq locationReq);

    @ApiOperation("更新location")
    @RequestMapping(value = "/updateLocation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateLocation(@RequestBody LocationReq locationReq);

    @ApiOperation("删除location")
    @RequestMapping(value = "/delLocation", method = RequestMethod.GET)
    void delLocation(@RequestParam("tenantId") Long tenantId,@RequestParam("orgId") Long orgId, @RequestParam("id") Long id);

    @ApiOperation("EXCEL导入空间")
    @RequestMapping(value = "/spaceDataImport", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    CommonResponse spaceDataImport(@RequestBody SpaceExcelReq spaceExcelReq);

    @ApiOperation("空间背景图保存")
    @RequestMapping(value = "/saveSpaceBackgroundImgImport", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveSpaceBackgroundImgImport(@RequestBody SpaceBackgroundImgReq req);

    @ApiOperation("获取空间背景图")
    @RequestMapping(value = "/getSpaceBackgroundImg", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<SpaceBackgroundImgResp> getSpaceBackgroundImg(@RequestBody SpaceBackgroundImgReq req);

    @ApiOperation("根据ID获取空间背景图")
    @RequestMapping(value = "/getSpaceBackgroundImgById", method = RequestMethod.GET)
    SpaceBackgroundImgResp getSpaceBackgroundImgById(@RequestParam("tenantId") Long tenantId,@RequestParam("orgId") Long orgId, @RequestParam("id") Long id);

    @ApiOperation("根据ID删除空间背景图")
    @RequestMapping(value = "/deleteSpaceBackgroundImgById", method = RequestMethod.GET)
    Integer deleteSpaceBackgroundImg(@RequestParam("tenantId") Long tenantId,@RequestParam("orgId") Long orgId, @RequestParam("id") Long id);

    @ApiOperation("修改空间背景图")
    @RequestMapping(value = "/updateSpaceBackgroundImg", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Integer updateSpaceBackgroundImg(@RequestBody SpaceBackgroundImgReq req);

    @ApiOperation("获取部署方式列表")
    @RequestMapping(value = "/getDeploymentList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<DeploymentResp> getDeploymentList(@RequestBody DeploymentReq req);

    @ApiOperation("根据用户查询空间列表")
    @RequestMapping(value = "/getDeploymentPage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<DeploymentResp> getDeploymentPage(@RequestBody DeploymentReq req);

    @ApiOperation("添加或修改部署方式")
    @RequestMapping(value = "/saveOrUpdateDeploy", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveOrUpdateDeploy(@RequestBody DeploymentReq req);

    @ApiOperation("批量删除部署方式")
    @RequestMapping(value = "/deleteBatchDeploy", method = RequestMethod.GET)
    public void deleteBatchDeploy(@RequestParam("deployIds") String deployIds);

    @ApiOperation("根据id获取部署方式")
    @RequestMapping(value = "/findDeploymentById", method = RequestMethod.GET)
    public DeploymentResp findDeploymentById(@RequestParam("tenantId") Long tenantId,@RequestParam("orgId") Long orgId, @RequestParam("id") Long id);

    @ApiOperation("根据部署id获取空间列表")
    @RequestMapping(value = "/findSpaceListByDeployId", method = RequestMethod.GET)
    public List<SpaceResp> findSpaceListByDeployId(@RequestParam("deployId") Long deployId, @RequestParam("tenantId") Long tenantId, @RequestParam("orgId") Long orgId,@RequestParam("locationId") Long locationId);

    @ApiOperation("获取房间的状态")
    @RequestMapping(value = "/getSpaceStatus", method = RequestMethod.GET)
    int getSpaceStatus(@RequestParam("tenantId") Long tenantId,@RequestParam("orgId") Long orgId, @RequestParam("spaceId") Long spaceId);

    @ApiOperation("更新房间的状态")
    @RequestMapping(value = "/setSpaceStatus", method = RequestMethod.GET)
    int setSpaceStatus(@RequestParam("tenantId") Long tenantId,@RequestParam("orgId") Long orgId, @RequestParam("spaceId") Long spaceId);

    @ApiOperation("根据SpaceId查找空间信息")
    @RequestMapping(value = "/findBySpaceIds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<SpaceResp> findBySpaceIds(@RequestBody QueryParamReq req);

    @ApiOperation("添加或修改日程")
    @RequestMapping(value = "/addOrUpdateCalendar", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void addOrUpdateCalendar(@RequestBody CalendarReq calendarReq);

    @ApiOperation("删除日程")
    @RequestMapping(value = "/deleteCalendar", method = RequestMethod.GET)
    void deleteCalendar(@RequestParam("tenantId")Long tenantId,@RequestParam("orgId") Long orgId, @RequestParam("id") Long id);

    @ApiOperation("查询日程--分页")
    @RequestMapping(value = "/findCalendarList", method = RequestMethod.GET)
    public Page<CalendarResp> findCalendarList(@RequestParam("pageNum") String pageNum, @RequestParam("pageSize") String pageSize, @RequestParam(value = "name", required = false) String name) throws BusinessException;

    @ApiOperation("查询日程--没分页")
    @RequestMapping(value = "/findCalendarListNoPage", method = RequestMethod.GET)
    public List<CalendarResp> findCalendarListNoPage(@RequestParam(value = "name", required = false) String name) throws BusinessException;


    @ApiOperation("根据id查询日程")
    @RequestMapping(value = "/getCalendarIofoById", method = RequestMethod.GET)
    CalendarResp getCalendarIofoById(@RequestParam("tenantId")Long tenantId,@RequestParam("orgId") Long orgId, @RequestParam("id") Long id);

    @ApiOperation("获取所有区域的租户")
    @RequestMapping(value = "/getLocationTenant", method = RequestMethod.GET)
    List<Long> getLocationTenant();

    @ApiOperation("设备替换")
    @RequestMapping(value = "/replaceDevice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void replaceDevice(@RequestBody SpaceDeviceReq spaceDeviceReq);

    /**
     * 根据房间ID获取设备信息
     *
     * @param spaceDeviceReq
     * @return
     * @author linjihuang
     */
    @ApiOperation("根据房间ID获取设备信息")
    @RequestMapping(value = "/getDeviceListBySpace", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<Map<String, Object>> getDeviceListBySpace(@RequestBody SpaceDeviceReq spaceDeviceReq);
    
    @ApiOperation("以名称获取部署方式")
    @RequestMapping(value = "/findByName", method = RequestMethod.GET)
    public DeploymentResp findByName(@RequestParam("orgId")Long orgId,@RequestParam("name")String name);

}
