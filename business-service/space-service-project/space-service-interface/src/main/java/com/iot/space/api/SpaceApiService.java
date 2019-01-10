package com.iot.space.api;

import com.iot.common.exception.BusinessException;
import com.iot.space.api.fallback.SpaceApiFallbackFactory;
import com.iot.space.domain.Space;
import com.iot.space.domain.SpaceDevice;
import com.iot.space.domain.SpaceDeviceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Api(tags = "空间相关接口")
@FeignClient(value = "space-service", fallbackFactory = SpaceApiFallbackFactory.class)
@RequestMapping(value = "/space")
public interface SpaceApiService {
    /**
     * 新建空间
     *
     * @param space
     * @return
     * @author wanglei
     */
    @ApiOperation(value = "新建空间")
    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void save(@RequestBody Space space);

    /**
     * 更新空间
     *
     * @param space
     * @return
     * @author wanglei
     */
    @ApiOperation(value = "更新空间")
    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void update(@RequestBody Space space);

    /**
     * 根据ID删除空间
     *
     * @param id
     * @return
     * @author wanglei
     */
    @ApiOperation(value = "根据ID删除空间")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    void delete(@PathVariable("id") String id) throws BusinessException;

    /**
     * 根据顶级区域查询空间列表
     *
     * @param locationId
     * @return
     */
    @ApiOperation(value = "根据顶级区域查询空间列表")
    @RequestMapping(value = "/findByLocation/{locationId}", method = RequestMethod.GET)
    List<Space> findByLocation(@PathVariable("locationId") String locationId);

    /**
     * 根据ID查询空间
     *
     * @param id
     * @return
     * @author wanglei
     */
    @ApiOperation(value = "根据ID查询空间")
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    Space findById(@PathVariable("id") String id);

    /**
     * 根据用户分页查询空间列表
     *
     * @param pageNum
     * @return
     * @author wanglei
     */
    // @ApiOperation(value = "根据用户分页查询空间列表")
    // @RequestMapping(value = "/space/findSpacePageByUser", method =
    // RequestMethod.GET)
    // Page<SpaceVO> findSpacePageByUser(@RequestParam("pageNum") String pageNum,
    // @RequestParam("pageSize") String pageSize, @RequestParam("locationId") String
    // locationId, @RequestParam(value = "name", required = false) String name);

    /**
     * 根据用户查询空间列表
     *
     * @param params
     * @return
     * @author wanglei
     */
    @ApiOperation(value = "根据用户查询空间列表")
    @RequestMapping(value = "/findSpaceByUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<Space> findSpaceByUser(@RequestBody Map<String, Object> params);

    /**
     * 根据用户查询顶级空间
     *
     * @param locationId
     * @return
     * @author wanglei
     */
    @ApiOperation(value = "根据用户查询顶级空间")
    @RequestMapping(value = "/findRootByUser/{locationId}", method = RequestMethod.GET)
    List<Space> findRootByUser(@PathVariable("locationId") String locationId);

    /**
     * 添加空间设备挂载数据
     *
     * @param spaceDevice
     * @return
     * @author linjihuang
     */
    @ApiOperation(value = "添加空间设备挂载数据")
    @RequestMapping(value = "/insert", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int insert(@RequestBody SpaceDevice spaceDevice);

    /**
     * 删除设备挂载信息
     *
     * @param spaceDeviceVO
     * @return
     */
    @ApiOperation(value = "删除设备挂载数据")
    @RequestMapping(value = "/deleteBySpace", method = RequestMethod.DELETE)
    int deleteBySpace(@RequestBody SpaceDeviceVO spaceDeviceVO);

    /**
     * 根据父节点查询子节点Map
     *
     * @param parentId
     * @return
     * @author wanglei
     */
    @ApiOperation(value = "根据父节点查询子节点Map")
    @RequestMapping(value = "/findByParent/{parentId}", method = RequestMethod.GET)
    List<Map<String, Object>> findByParent(@PathVariable("parentId") String parentId);

    /**
     * 根据父节点查询子节点对象
     *
     * @param parentId
     * @return
     * @author wanglei
     */
    @ApiOperation(value = "根据父节点查询子节点对象")
    @RequestMapping(value = "/findSpaceByParent/{parentId}", method = RequestMethod.GET)
    List<Space> findSpaceByParent(@PathVariable("parentId") String parentId);

    /**
     * 根据用户的的根节点查询空间树结构
     *
     * @param locationId
     * @return
     * @author wanglei
     */
    @ApiOperation(value = "根据用户的的根节点查询空间树结构")
    @RequestMapping(value = "/findTree/{locationId}", method = RequestMethod.GET)
    List<Map<String, Object>> findTree(@PathVariable("locationId") String locationId);

    /**
     * 挂载设备
     *
     * @param spaceId
     * @return
     * @author wanglei
     */
    @ApiOperation(value = "挂载设备")
    @RequestMapping(value = "/mount", method = RequestMethod.GET)
    void mount(@RequestParam("spaceId") String spaceId, @RequestParam("deviceIds") String deviceIds,
               @RequestParam("locationId") String locationId) throws Exception;

    /**
     * 根据建筑获取楼层
     *
     * @param buildId
     * @param types
     *            逗号相隔设备类型
     * @return
     * @author wanglei
     */
    // @ApiOperation(value = "根据建筑获取楼层")
    // @RequestMapping(value = "/space/getFloorAndDeviceCount", method =
    // RequestMethod.GET)
    // List<Map<String, Object>> getFloorAndDeviceCount(@RequestParam("buildId")
    // String buildId, @RequestParam("types") String types) throws Exception;

    /**
     * 根据空间ID和设备大类查询设备空间下打开的设备
     *
     * @param params
     */
    @ApiOperation(value = "根据空间ID和设备大类查询设备空间下打开的设备")
    @RequestMapping(value = "/space/countOnDevice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Integer countOnDevice(@RequestBody Map<String, Object> params);

    /**
     * 根据空间ID和设备大类查询灯空间下打开的设备
     *
     * @param params
     */
    @ApiOperation(value = "根据空间ID和设备大类查询灯空间下打开的设备")
    @RequestMapping(value = "/countOnLightDevice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Integer countOnLightDevice(@RequestBody Map<String, Object> params);

    /**
     * 查询某个空间下所有的子空间
     *
     * @param space
     * @return
     */
    @ApiOperation(value = "查询某个空间下所有的子空间")
    @RequestMapping(value = "/getAllSpace", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<String> getAllSpace(@RequestBody Space space);

    /**
     * 查询某个空间下所有的子空间
     *
     * @param spaceId
     * @return
     */
    @ApiOperation(value = "查询某个空间下所有的子空间")
    @RequestMapping(value = "/getAllChildSpace/{spaceId}", method = RequestMethod.GET)
    List<Space> getAllChildSpace(@PathVariable("spaceId") String spaceId);

    /**
     * 递归查询List<Sapce>
     *
     * @param map 里面包含oldList和newList
     */
    @ApiOperation(value = "递归查询List")
    @RequestMapping(value = "/findSpaceChild", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void findSpaceChild(@RequestBody Map<String, List<Space>> map);

    /**
     * 根据房间ID获取所有子集的设备
     *
     * @param roomId
     * @return
     * @author wanglei
     */
    @ApiOperation(value = "根据房间ID获取所有子集的设备")
    @RequestMapping(value = "/findDeviceByRoom/{roomId}", method = RequestMethod.GET)
    List<Map<String, Object>> findDeviceByRoom(@PathVariable("roomId") String roomId) throws Exception;

    @RequestMapping(value = "/findDeviceByRoom", method = RequestMethod.POST)
    List<Map<String, Object>> findDeviceByRoom(@RequestParam("roomId") String roomId,
                                               @RequestBody List<String> deviceCategoryTypes) throws Exception;

    /**
     * 群控设备
     *
     * @param spaceId
     * @param deviceType
     * @param propertyMap
     *            目标值
     * @return
     * @author wanglei
     */
    // @ApiOperation(value = "群控设备")
    // @RequestMapping(value = "/groupControl", method = RequestMethod.POST,
    // consumes = MediaType.APPLICATION_JSON_VALUE)
    // Boolean groupControl(@RequestParam("spaceId") String spaceId,
    // @RequestParam("deviceType") String deviceType,
    // @RequestBody Map<String, Object> propertyMap) throws Exception;

    /**
     * 单控设备
     *
     * @param deviceId
     * @return
     * @author wanglei
     */
    // @ApiOperation(value = "单控设备")
    // @RequestMapping(value = "/control/{deviceId}", method =
    // RequestMethod.GET)
    // public Boolean control(@PathVariable("deviceId") String
    // deviceId,Map<String,Object> map) throws Exception;

    /**
     * 设置空间从属关系
     *
     * @param spaceId
     * @param childIds
     */
    @ApiOperation(value = "设置空间从属关系")
    @RequestMapping(value = "/setSpaceRelation", method = RequestMethod.GET)
    void setSpaceRelation(@RequestParam("spaceId") String spaceId, @RequestParam("childIds") String childIds)
            throws Exception;

    /**
     * 获取直接子空间
     *
     * @param parentId
     */
    @ApiOperation(value = "获取直接子空间")
    @RequestMapping(value = "/getChildSpace/{parentId}", method = RequestMethod.GET)
    List<Space> getChildSpace(@PathVariable("parentId") String parentId) throws Exception;

    /**
     * 获取直接子空间
     *
     * @param floorId
     * @param spaceType
     */
    // @ApiOperation(value = "获取直接子空间")
    // @RequestMapping(value = "/getChildSpace", method = RequestMethod.GET)
    // List<SpaceVO> getChildSpace(@RequestParam("floorId") String floorId,
    // @RequestParam("spaceType") String spaceType) throws Exception;

    /**
     * 获取房间健康指数
     *
     * @param spaceId
     * @return
     * @throws Exception
     */
    // @RequestMapping(value = "/getEnvironmentalHealthIndex/{spaceId}",
    // method = RequestMethod.GET)
    // SpaceEhiVO getEnvironmentalHealthIndex(@PathVariable("spaceId") String
    // spaceId) throws Exception;

    /**
     * 获取空间下直接挂载的设备
     *
     * @param spaceId
     */
    @ApiOperation(value = "新建空间")
    @RequestMapping(value = "/getDirectDeviceBySpace/{spaceId}", method = RequestMethod.GET)
    List<Map<String, Object>> getDirectDeviceBySpace(@PathVariable("spaceId") String spaceId);

    /**
     * 根据设备查询归属直接空间
     *
     * @param deviceId
     * @return
     */
    @ApiOperation(value = "新建空间")
    @RequestMapping(value = "/findSpaceNameByDevice/{deviceId}", method = RequestMethod.GET)
    List<Map<String, Object>> findSpaceNameByDevice(@PathVariable("deviceId") String deviceId);

    /**
     * 根据ID查询归属空间
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "新建空间")
    @RequestMapping(value = "/findSpaceNameById/{id}", method = RequestMethod.GET)
    Space findSpaceNameById(@PathVariable("id") String id);

    /**
     * 查询空间挂载的设备信息
     *
     * @param spaceDeviceVO
     * @return
     */
    @ApiOperation(value = "查询空间挂载的设备信息")
    @RequestMapping(value = "/findSpaceMount", method = RequestMethod.GET)
    int findSpaceMount(SpaceDeviceVO spaceDeviceVO);

    /**
     * 根据房间信息判断该房间灯的亮灭标识 0 代表灭 1 亮
     *
     * @param spaceIds
     * @return
     */
    // @ApiOperation(value = "根据房间信息判断该房间灯的亮灭标识 0 代表灭 1 亮")
    // @RequestMapping(value = "/judgeSpaceSwitchStatus", method =
    // RequestMethod.GET)
    // public int judgeSpaceSwitchStatus(List<String> spaceIds);

    /**
     * 查询没有挂载过的空间
     *
     * @param params
     * @return
     * @author wanglei
     */
    @ApiOperation(value = "查询没有挂载过的空间")
    @RequestMapping(value = "/findSpaceUnMount", method = RequestMethod.GET)
    List<Space> findSpaceUnMount(@RequestBody Map<String, Object> params);

    /**
     * 根据空间ID统计设备归属数量
     *
     * @param spaceId
     * @return
     */
    @ApiOperation(value = "查询没有挂载过的空间")
    @RequestMapping(value = "/countSpaceDeviceMount", method = RequestMethod.GET)
    Integer countSpaceDeviceMount(@PathVariable("spaceId") String spaceId);

    @ApiOperation(value = "根据父节点查询空间")
    @RequestMapping(value = "/findSpaceByParent", method = {RequestMethod.POST})
    List<Space> findSpaceByParent(@RequestBody Map<String, Object> params);

    @ApiOperation(value = "保存房间和设备的关系")
    @RequestMapping(value = "/insertSpaceDevice", method = {RequestMethod.POST})
    void insertSpaceDevice(@RequestBody SpaceDevice record);

    @ApiOperation(value = "根据空间删除挂载的设备关系")
    @RequestMapping(value = "/insertSpaceDevice", method = {RequestMethod.POST})
    void deleteBySpace(@RequestParam("spaceId") String spaceId);

    @ApiOperation(value = "根据房间信息判断该房间灯的亮灭标识 0 代表灭  1 亮")
    @RequestMapping(value = "/judgeSpaceSwitchStatus", method = RequestMethod.GET)
    int judgeSpaceSwitchStatus(List<String> spaceIds);

    @ApiOperation(value = "根据build和类型获取空间")
    @RequestMapping(value = "/getSpaceByBuildAndType", method = RequestMethod.GET)
    List<Space> getSpaceByBuildAndType(Map<String, Object> map);

    @ApiOperation(value = "根据分组统计设备总数")
    @RequestMapping(value = "/countByGroupIds", method = RequestMethod.GET)
    Integer countByGroupIds(Map<String, Object> map);

    @ApiOperation(value = "根据分组统计已经开关为打开的设备总数")
    @RequestMapping(value = "/countOnSwitchByGroupIds", method = RequestMethod.GET)
    Integer countOnSwitchByGroupIds(Map<String, Object> map);

    @ApiOperation(value = "查询类型是灯的打开统计数量")
    @RequestMapping(value = "/countLightOnSwitchByGroupIds", method = RequestMethod.GET)
    Integer countLightOnSwitchByGroupIds(Map<String, Object> map);

    @ApiOperation(value = "根据房间ID获取下边分组")
    @RequestMapping(value = "/findGroupIdByRoom", method = RequestMethod.GET)
    List<Map<String, Object>> findGroupIdByRoom(String roomId);

    @ApiOperation(value = "根据分组ID获取归属设备信息")
    @RequestMapping(value = "/findDeviceByGroup", method = RequestMethod.GET)
    List<Map<String, Object>> findDeviceByGroup(Map<String, Object> params);

    @ApiOperation(value = "根据空间ID和设备类型查询设备ID")
    @RequestMapping(value = "/getDeviceIdByTypeAndSpace", method = RequestMethod.GET)
    List<Map<String, Object>> getDeviceIdByTypeAndSpace(Map<String, Object> params);

    @ApiOperation(value = "根据空间ID查询挂载设备ID")
    @RequestMapping(value = "/getDeviceIdBySpaceId", method = RequestMethod.GET)
    List<String> getDeviceIdBySpaceId(String spaceId);

    @ApiOperation(value = "更新设备父ID")
    @RequestMapping(value = "/updateParentIdNull", method = RequestMethod.POST)
    void updateParentIdNull(String spaceId);

    @ApiOperation(value = "设置空间关系")
    @RequestMapping(value = "/setSpaceRelation", method = RequestMethod.POST)
    void setSpaceRelation(Map<String, Object> map);

    @ApiOperation(value = "更新设备挂载状态")
    @RequestMapping(value = "/updateSpaceDeviceStatus", method = RequestMethod.POST)
    int updateSpaceDeviceStatus(SpaceDeviceVO spaceDeviceVO);

    @ApiOperation(value = "查询未挂载到网关的房间信息")
    @RequestMapping(value = "/findSpaceInfo", method = RequestMethod.GET)
    List<SpaceDeviceVO> findSpaceInfo(String status);

    @ApiOperation(value = "根据type查找用户空间数据")
    @RequestMapping(value = "/findSpaceByType", method = RequestMethod.GET)
    List<Space> findSpaceByType(@RequestParam("type") String type, @RequestParam("userId") String userId);

    @ApiOperation(value = "删除空间")
    @RequestMapping(value = "/deletSpace", method = RequestMethod.GET)
    void deleteSpace(@RequestParam("spaceId") String spaceId, @RequestParam("userId") String userId);

    @ApiOperation(value = "查询空间")
    @RequestMapping(value = "/findByIdAuthUserId", method = RequestMethod.GET)
    Space findByIdAuthUserId(@RequestParam("spaceId") String spaceId, @RequestParam("userId") String userId);
}
