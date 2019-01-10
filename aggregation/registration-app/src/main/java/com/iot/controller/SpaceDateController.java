package com.iot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.domain.Space;
import com.iot.domain.SpaceDate;
import com.iot.mapper.UserSpaceSubscribeMapper;
import com.iot.service.SpaceDateService;
import com.iot.service.UserSpaceService;
import com.iot.util.ToolUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("签到App接口")
@RestController
@RequestMapping("/spaceDate")
public class SpaceDateController {

    @Autowired
    SpaceDateService spaceDateService;

    @Autowired
    UserSpaceService userSpaceService;

    @Autowired
    UserSpaceSubscribeMapper userSpaceSubscribeMapper;

    /**
     * 根据空间Id查询可预约时间
     *
     * @param message（spaceId,date）
     * @return 返回该空间可选的时间段
     * @throws ParseException
     */
    @ApiOperation(value = "根据空间Id查找可预约时间")
    @RequestMapping(value = "/findSpaceDate", method = RequestMethod.GET)
    public CommonResponse<List<SpaceDate>> findSpaceDateBySpaceIdAndDate(
            @RequestParam("message") @ApiParam(value = "message", required = true) String message) throws ParseException {
        List<SpaceDate> spaceDateList = new ArrayList<SpaceDate>();
        if (StringUtils.isNotBlank(message)) {
            Map<String, Object> map = (Map<String, Object>) JSON.parse(message);
            spaceDateList = spaceDateService.findSpaceDateBySpaceIdAndDate(map);
        }
        return CommonResponse.success(spaceDateList);
    }

    /**
     * 查看用户预约
     *
     * @param userId
     * @return spaceDateList
     */
    @ApiOperation(value = "查看用户预约")
    @RequestMapping(value = "/findUserSubscribe", method = RequestMethod.GET)
    public CommonResponse<Map<String, Object>> findUserSubscribe(
            @RequestParam("userId") @ApiParam(value = "userId", required = true) String userId) {
        Map<String, Object> spaceDateMap = new HashMap<>();
        spaceDateMap = userSpaceService.findUserSubscribeByUserId(userId);
        return CommonResponse.success(spaceDateMap);

    }


    /**
     * 取消客户预约
     *
     * @param message[{userId,spaceId,date,beginTime,endTime,tenantId}]
     * @return 是否删除成功
     * @throws ParseException
     */
    @ApiOperation(value = "删除用户预约")
    @RequestMapping(value = "/deleteUserSubscribe", method = RequestMethod.POST)
    public CommonResponse<ResultMsg> deleteUserSubscribe(
            @RequestParam("message") @ApiParam(value = "message", required = true) String message) throws ParseException {
        if (StringUtils.isNotBlank(message)) {
            List<Map> MapList = JSON.parseArray(message, Map.class);
            for (Map<String, Object> map : MapList) {
                if (map.get("userId") != null && map.get("spaceId") != null && map.get("settingDate") != null
                        && map.get("beginTime") != null && map.get("endTime") != null && map.get("tenantId") != null) {
                    String settingDate = ToolUtils.stampToDate(Long.valueOf(map.get("settingDate").toString()));
                    String beginTime = ToolUtils.stampToDate(Long.valueOf(map.get("beginTime").toString()));
                    String endTime = ToolUtils.stampToDate(Long.valueOf(map.get("endTime").toString()));
                    map.replace("settingDate", settingDate);
                    map.replace("beginTime", beginTime);
                    map.replace("endTime", endTime);
                    userSpaceService.deleteUserSubscribe(map);
                }

            }
        }
        return new CommonResponse<>(ResultMsg.SUCCESS);
    }

    /**
     * 保存用户预约
     *
     * @param messageMapList[{userId,spaceId,date,beginTime,endTime,tenantId}]
     * @return 是否保存成功
     * @throws ParseException
     */
    @ApiOperation(value = "保存用户预约")
    @RequestMapping(value = "/saveUserSubscribe", method = RequestMethod.POST)
    public CommonResponse<ResultMsg> saveUserSubscribe(
            @RequestParam("message") @ApiParam(value = "message", required = true) String message) throws ParseException {
        if (StringUtils.isNotBlank(message)) {
            List<Map> MapList = JSON.parseArray(message, Map.class);
            for (Map<String, Object> map : MapList) {
                if (map.get("userId") != null && map.get("spaceId") != null && map.get("settingDate") != null
                        && map.get("beginTime") != null && map.get("endTime") != null && map.get("tenantId") != null) {

                    String settingDate = ToolUtils.stampToDate(Long.valueOf(map.get("settingDate").toString()));
                    String beginTime = ToolUtils.stampToDate(Long.valueOf(map.get("beginTime").toString()));
                    String endTime = ToolUtils.stampToDate(Long.valueOf(map.get("endTime").toString()));
                    map.replace("settingDate", settingDate);
                    map.replace("beginTime", beginTime);
                    map.replace("endTime", endTime);
                    userSpaceService.saveUserSubscribe(map);
                }

            }
        }
        return new CommonResponse<>(ResultMsg.SUCCESS);

    }

    /**
     * 根据租户Id查询可预约时间
     *
     * @param TenantId
     * @return spaceDateList
     * @throws ParseException
     */
    @ApiOperation(value = "根据租户Id查询可预约时间")
    @RequestMapping(value = "/findBookableSpaceDateByTenantId", method = RequestMethod.POST)
    public CommonResponse<Map<String, Object>> findBookableSpaceDateByTenantId(@RequestParam("tenantId") String tenantId) throws ParseException {
        Map<String, Object> spaceDateMap = new HashMap<>();
        spaceDateMap = userSpaceService.findBookableSpaceDateByTenantId(tenantId);
        return CommonResponse.success(spaceDateMap);
    }

    /**
     * 根据预定时间分配space
     *
     * @param message MapList[{userId:"",settingDate:"",beginTime:"",endTime:"",tenantId:""}]
     * @return spaceDateList
     * @throws ParseException
     */
    @ApiOperation(value = "根据预定时间分配space")
    @RequestMapping(value = "/distributeSpaceBySubscribeTime", method = RequestMethod.POST)
    public CommonResponse<List<SpaceDate>> distributeSpaceBySubscribeTime(
            @RequestParam("message") @ApiParam(value = "message", required = true) String message) throws ParseException {
        List<SpaceDate> ResultspaceDateList = new ArrayList<SpaceDate>();

        if (StringUtils.isNotBlank(message)) {
            List<Map<String, Object>> DateMapList = JSON.parseObject(message, new TypeReference<List<Map<String, Object>>>() {
            });
            ResultspaceDateList = userSpaceService.distributeSpaceBySubscribeTime(DateMapList, ResultspaceDateList);
            if (CollectionUtils.isNotEmpty(ResultspaceDateList)) {
                Map<String, Object> param = new HashMap<>();
                String userId = (String) DateMapList.get(0).get("userId");
                String tenantId = (String) DateMapList.get(0).get("tenantId");
                for (SpaceDate spaceDate : ResultspaceDateList) {
                    String spaceDateId = spaceDate.getSpaceDateId();
                    String userSubscribeId = ToolUtils.getUUID();
                    param.put("userSubscribeId", userSubscribeId);
                    param.put("spaceDateId", spaceDateId);
                    param.put("userId", userId);
                    param.put("tenantId", tenantId);
                    userSpaceSubscribeMapper.saveUserSubscribe(param);
                }
            }
        }


        return CommonResponse.success(ResultspaceDateList);

    }

    /**
     * 保存用户签到
     *
     * @param messageMapList[{userId,spaceId,date,beginTime,endTime,tenantId}]
     * @return 是否保存成功
     */
    @ApiOperation(value = "保存用户签到")
    @RequestMapping(value = "/saveUserUsed", method = RequestMethod.POST)
    public CommonResponse<ResultMsg> saveUserUsed(
            @RequestParam("message") @ApiParam(value = "message", required = true) String message) {
        if (StringUtils.isNotBlank(message)) {
            List<Map> MapList = JSON.parseArray(message, Map.class);
            for (Map<String, Object> map : MapList) {
                userSpaceService.saveUserUsed(map);
            }
        }
        return new CommonResponse<>(ResultMsg.SUCCESS);

    }

    /**
     * 根据租户Id查询楼层空间分布
     *
     * @param tenantId
     * @return map{"A栋":[],"B栋":[]}
     */
    @ApiOperation(value = "根据租户Id查询空间分布")
    @RequestMapping(value = "/showSpaceDistribution", method = RequestMethod.POST)
    public CommonResponse<Map<String, Object>> showSpaceDistribution(@RequestParam("tenantId") String tenantId) {
        Map<String, Object> spaceMap = new HashMap<>();
        spaceMap = spaceDateService.showSpaceDistribution(tenantId);
        return CommonResponse.success(spaceMap);

    }

    /**
     * 根据楼层的spaceId查询人员分布
     *
     * @param spaceId，tenantId
     * @return [{spaceId:"",spaceName:"",usedAmount:"",subscribeAmount:""}]
     */
    @ApiOperation(value = "根据spaceId查询人员分布")
    @RequestMapping(value = "/showPeopleDistribution", method = RequestMethod.POST)
    public CommonResponse<List<Map<String, Object>>> showPeopleDistribution(
            @RequestParam("message") @ApiParam(value = "message", required = true) String message) {
        Map<String, Object> msgMap = (Map<String, Object>) JSON.parse(message);
        List<Map<String, Object>> spaceList = new ArrayList<>();
        spaceList = spaceDateService.showPeopleDistribution(msgMap);
        return CommonResponse.success(spaceList);

    }

    public CommonResponse<ResultMsg> synchronizationSpace(@RequestParam("spaceString")
                                                          @ApiParam(value = "spaceString", required = true) String spaceString,
                                                          @RequestParam("tenantId") String tenantId) {
        List<Space> spaceList = JSON.parseArray(spaceString, Space.class);
        if (StringUtils.isNotBlank(spaceString) && StringUtils.isNotBlank(tenantId)) {
            Integer isSuccess = spaceDateService.deleteSpaceByTenantId(tenantId);
            if (isSuccess != null) {
                spaceDateService.uploadeSpace(spaceList);
            }
        }

        return new CommonResponse<>(ResultMsg.SUCCESS);

    }


}
