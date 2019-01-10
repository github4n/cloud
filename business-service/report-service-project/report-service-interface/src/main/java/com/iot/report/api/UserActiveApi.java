package com.iot.report.api;

import com.iot.report.dto.req.ActivateBaseReq;
import com.iot.report.dto.resp.UserActivatedOrActivateResp;
import com.iot.report.dto.resp.ActiveDataResp;
import com.iot.report.dto.resp.UserActiveAndActivatedResp;
import com.iot.report.dto.resp.UserDevActiveAndActivatedResp;
import com.iot.report.entity.UserActivatedEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.List;
import com.iot.report.fallback.UserActiveApiFallbackFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *@description
 *@author wucheng
 *@create 2018/12/29 14:40
 */
@Api(tags = "用户激活Api")
@FeignClient(value = "report-service", fallbackFactory = UserActiveApiFallbackFactory.class)
@RequestMapping("/userActiveService")
public interface UserActiveApi {
    /**
     * 
     * 描述：整理用户活跃信息
     * @author 李帅
     * @created 2019年1月3日 下午7:56:58
     * @since
     */
    @ApiOperation(value = "整理用户活跃信息", notes = "整理用户活跃信息")
    @RequestMapping(value = "/arrangeUserData", method = RequestMethod.GET)
    void arrangeUserData();
    
    /**
     *@description 根据起始日期查询用户激活信息
     *@author wucheng
     *@params [beginDate, endDate]
     *@create 2019/1/5 16:22
     *@return void
     */
    @ApiOperation(value = "根据起始时间和租户id查询用户激活信息", notes = "根据起始时间和租户id查询用户激活信息")
    @RequestMapping(value = "/getUserRegisterByDate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<ActiveDataResp> getUserRegisterByDate(@RequestBody ActivateBaseReq req);

    /**
     *@description 根据起始日期查询用户活跃信息
     *@author wucheng
     *@params [beginDate, endDate]
     *@create 2019/1/5 16:22
     *@return void
     */
    @ApiOperation(value = "根据起始日期查询用户活跃信息", notes = "根据开始日期和起始日期查询用户活跃信息")
    @RequestMapping(value = "/getUserActiveByDate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<ActiveDataResp> getUserActiveByDate(@RequestBody ActivateBaseReq req);

    /**
     *@description 测试添加用户注册数据, 后期删除该接口
     *@author wucheng
     *@params [entity]
     *@create 2019/1/7 10:37
     *@return void
     */
    @ApiOperation(value = "测试添加用户注册数据, 后期删除该接口", notes = "测试添加用户注册数据, 后期删除该接口")
    @RequestMapping(value = "/testSaveActivated", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void testSaveActivated(@RequestBody UserActivatedEntity entity);

    /**
     *@description 根据租户id获取用户今日、昨日、一周激活数量
     *@author wucheng
     *@params [tenantId]
     *@create 2019/1/7 20:32
     *@return com.iot.report.dto.resp.ActivatedDetailDataResp
     */
    @ApiOperation(value = "根据租户id获取用户今日、昨日、一周激活数量", notes = "根据租户id获取用户今日、昨日、一周激活数量")
    @RequestMapping(value = "/getUserRegisterDetail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    UserActivatedOrActivateResp getUserRegisterDetail(@RequestBody ActivateBaseReq req);

    /**
     *@description 根据租户id获取用户今日、昨日、一周活跃数量、总数
     *@author wucheng
     *@params [tenantId]
     *@create 2019/1/8 14:38
     *@return com.iot.report.dto.resp.ActivatedDetailDataResp
     */
    @ApiOperation(value = "根据租户id获取用户今日、昨日、一周活跃数量", notes = "根据租户id获取用户今日、昨日、一周活跃数量")
    @RequestMapping(value = "/getUserActiveDetail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    UserActivatedOrActivateResp getUserActiveDetail(@RequestBody ActivateBaseReq req);

    /**
     *@description 描述：获取用户激活与活跃数据
     *@author wucheng
     *@params [req]
     *@create 2019/1/8 19:12
     *@return com.iot.report.dto.resp.UserActiveAndActivatedResp
     */
    @ApiOperation(value = "获取用户激活与活跃数据", notes = "获取用户激活与活跃数据")
    @RequestMapping(value = "/getUserActiveAndActivated", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    UserActiveAndActivatedResp getUserActiveAndActivated(@RequestBody ActivateBaseReq req);

    /**
     *@description 根据租户id获取用户激活总数
     *@author wucheng
     *@params [tenantId]
     *@create 2019/1/8 14:38
     *@return com.iot.report.dto.resp.ActivatedDetailDataResp
     */
    @ApiOperation(value = "根据租户id获取用户激活总数", notes = "根据租户id获取用户激活总数")
    @RequestMapping(value = "/getUserRegisterTotal", method = RequestMethod.POST)
    Long getUserRegisterTotal(Long tenantId);

    /**
     *@description 获取设备和用户 今日和昨日 激活、活跃数量
     *@author wucheng
     *@params [req]
     *@create 2019/1/9 14:13
     *@return com.iot.report.dto.resp.UserDevActiveAndActivatedResp
     */
    @ApiOperation(value = "获取设备和用户 今日和昨日 激活、活跃数量", notes = "获取设备和用户 今日和昨日 激活、活跃数量")
    @RequestMapping(value = "/getUserDevActiveAndActivated", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    UserDevActiveAndActivatedResp getUserDevActiveAndActivated(@RequestBody  ActivateBaseReq req);
}
