package com.iot.report.restful;

import com.iot.report.api.UserActiveApi;
import com.iot.report.dto.req.ActivateBaseReq;
import com.iot.report.dto.resp.UserActivatedOrActivateResp;
import com.iot.report.dto.resp.ActiveDataResp;
import com.iot.report.dto.resp.UserActiveAndActivatedResp;
import com.iot.report.dto.resp.UserDevActiveAndActivatedResp;
import com.iot.report.entity.UserActivatedEntity;
import com.iot.report.service.UserActiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *@description 用户激活信息保存
 *@author wucheng
 *@create 2018/12/29 14:50
 */
@RestController
public class UserActiveRestful implements UserActiveApi{

    @Autowired
    private UserActiveService userActiveService;

    /**
     *
     * 描述：整理用户活跃信息
     * @author 李帅
     * @created 2019年1月3日 下午8:04:38
     * @since
     */
    @Override
    public void arrangeUserData() {
    	userActiveService.arrangeUserData();
    }

    /**
     *@description 根据起始日期和租户id查询用户激活信息
     *@author wucheng
     *@params
     *@create 2019/1/5 16:23
     *@return
     */
    @Override
    public List<ActiveDataResp> getUserRegisterByDate(@RequestBody ActivateBaseReq req) {
        return userActiveService.getUserRegisterByDate(req);
    }

    /**
     *@description 根据起始日期和租户id查询用户活跃信息
     *@author wucheng
     *@params
     *@create 2019/1/5 16:23
     *@return
     */
    @Override
    public List<ActiveDataResp> getUserActiveByDate(@RequestBody ActivateBaseReq req) {
        return userActiveService.getUserActiveByDate(req);
    }

    /**
     *@description 测试添加用户激活数据，后期删除这个接口
     *@author wucheng
     *@params [entity]
     *@create 2019/1/7 11:29
     *@return void
     */
    @Override
    public void testSaveActivated(@RequestBody UserActivatedEntity entity) {
        userActiveService.saveUserActivated(entity);
    }

    /**
     *@description 获取用户今日、昨日、一周激活数量详情
     *@author wucheng
     *@params [tenantId]
     *@create 2019/1/7 20:01
     *@return com.iot.report.dto.resp.ActivatedDetailDataResp
     */
    @Override
    public UserActivatedOrActivateResp getUserRegisterDetail(@RequestBody ActivateBaseReq req) {
        return userActiveService.getUserRegisterDetail(req);
    }

    /**
     *@description 获取用户今日、昨日、一周活跃数量
     *@author wucheng
     *@params [tenantId]
     *@create 2019/1/7 20:01
     *@return com.iot.report.dto.resp.ActivatedDetailDataResp
     */
    @Override
    public UserActivatedOrActivateResp getUserActiveDetail(@RequestBody ActivateBaseReq req) {
        return userActiveService.getUserActiveDetail(req);
    }

    /**
     *@description 描述：获取用户激活与活跃数据
     *@author wucheng
     *@params [req]
     *@create 2019/1/8 19:12
     *@return com.iot.report.dto.resp.UserActiveAndActivatedResp
     */
    @Override
    public UserActiveAndActivatedResp getUserActiveAndActivated(@RequestBody ActivateBaseReq req) {
        return userActiveService.getUserActiveAndActivated(req);
    }

    /**
     *@description 获取用户总数
     *@author wucheng
     *@params [tenantId]
     *@create 2019/1/8 19:24
     *@return java.lang.Long
     */
    @Override
    public Long getUserRegisterTotal(@RequestParam("tenantId") Long tenantId) {
        return userActiveService.getUserRegisterTotal(tenantId);
    }

    /**
     *@description 获取设备和用户 今日和昨日 激活、活跃数量
     *@author wucheng
     *@params [req]
     *@create 2019/1/9 14:14
     *@return com.iot.report.dto.resp.UserDevActiveAndActivatedResp
     */
    @Override
    public UserDevActiveAndActivatedResp getUserDevActiveAndActivated(@RequestBody ActivateBaseReq req) {
        return userActiveService.getUserDevActiveAndActivated(req);
    }
}
