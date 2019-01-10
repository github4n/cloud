package com.iot.control.space.api;

import com.iot.control.space.api.fallback.SpaceDeviceApiFallbackFactory;
import com.iot.control.space.vo.DelSpaceDeviceReq;
import com.iot.control.space.vo.SpaceAndSpaceDeviceVo;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceDeviceReqVo;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.control.space.vo.SpaceDeviceVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Api("空间设备接口")
@FeignClient(value = "control-service", fallbackFactory = SpaceDeviceApiFallbackFactory.class)
@RequestMapping("/spaceDevice")
public interface SpaceDeviceApi {

    @ApiOperation("增加spaceDevice")
    @RequestMapping(value = "/inserSpaceDevice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean inserSpaceDevice(@RequestBody @Valid SpaceDeviceReq spaceDeviceReq);

    @ApiOperation("更新spaceDevice")
    @RequestMapping(value = "/updateSpaceDevice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean updateSpaceDevice(@RequestBody @Valid SpaceDeviceReq spaceDeviceReq);

    @ApiOperation("新增或更新spaceDevice")
    @RequestMapping(value = "/insertOrUpdateSpaceDeviceByDevId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean insertOrUpdateSpaceDeviceByDevId(@RequestBody @Valid SpaceDeviceReq spaceDeviceReq);

    @ApiOperation("批量增加spaceDevice")
    @RequestMapping(value = "/saveSpaceDeviceList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean saveSpaceDeviceList(@RequestBody @Valid List<SpaceDeviceReq> spaceDeviceReqs);

    @ApiOperation("批量更新spaceDevice")
    @RequestMapping(value = "/updateSpaceDevices", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean updateSpaceDevices(@RequestBody @Valid List<SpaceDeviceReq> spaceDeviceReqs);

    @ApiOperation("根据条件查询spaeDevice")
    @RequestMapping(value = "/findSpaceDeviceByCondition", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<SpaceDeviceResp> findSpaceDeviceByCondition(@RequestBody @Valid SpaceDeviceReq spaceDeviceReq);

    //通过order及id 排序
    @ApiOperation("通过spaceId查找spaceDevice")
    @RequestMapping(value = "/findSpaceDeviceVOBySpaceId", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<SpaceDeviceVo> findSpaceDeviceVOBySpaceId(@RequestParam("tenantId") Long tenantId, @RequestParam("spaceId") Long spaceId);

    @ApiOperation("通过spaceIds/deviceIds查找spaceDevice")
    @RequestMapping(value = "/findSpaceDeviceBySpaceIdsOrDeviceIds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<SpaceDeviceResp> findSpaceDeviceBySpaceIdsOrDeviceIds(@RequestBody @Valid SpaceAndSpaceDeviceVo req);

    @ApiOperation("根据条件统计spaceDevice数量")
    @RequestMapping(value = "/countSpaceDeviceByCondition", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int countSpaceDeviceByCondition(@RequestBody @Valid SpaceDeviceReq spaceDeviceReq);

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
    @ApiOperation("选择条件更新spaceDevice")
    @RequestMapping(value = "/updateSpaceDeviceByCondition", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean updateSpaceDeviceByCondition(@RequestBody @Valid SpaceDeviceReqVo reqVo);

    @ApiOperation("通过DeviceId删除spaceDevice")
    @RequestMapping(value = "/deleteSpaceDeviceByDeviceId", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    int deleteSpaceDeviceByDeviceId(@RequestParam("tenantId") Long tenantId, @RequestParam("deviceId")String deviceId);

    @ApiOperation("批量DeviceId删除spaceDevice")
    @RequestMapping(value = "/deleteSpaceDeviceByBatchDeviceIds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int deleteSpaceDeviceByBatchDeviceIds(@RequestBody @Valid DelSpaceDeviceReq params);

    @ApiOperation("批量删除spaceDevice")
    @RequestMapping(value = "/deleteSpaceDeviceBySpaceIdsOrDeviceIds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean deleteSpaceDeviceBySpaceIdsOrDeviceIds(@RequestBody @Valid SpaceAndSpaceDeviceVo req);

    @ApiOperation("通过deviceIds查找spaceDevice和space信息")
    @RequestMapping(value = "/findSpaceDeviceInfoByDeviceIds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<SpaceDeviceVo> findSpaceDeviceInfoByDeviceIds(@RequestBody @Valid SpaceAndSpaceDeviceVo req);
}
