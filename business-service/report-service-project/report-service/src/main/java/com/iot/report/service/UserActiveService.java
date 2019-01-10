package com.iot.report.service;

import com.iot.report.dto.req.ActivateBaseReq;
import com.iot.report.dto.resp.*;
import com.iot.report.entity.UserActivatedEntity;

import java.util.List;

public interface UserActiveService {
    /**
     *@description 保存激活用户数据
     *@author wucheng
     *@params [entity]
     *@create 2018/12/29 14:05
     *@return int
     */
    void saveUserActivated(UserActivatedEntity entity);

    /**
     *@description 根据起始日期和租户id获取用户激活信息
     *@author wucheng
     *@params [req]
     *@create 2019/1/7 11:51
     *@return java.util.List<com.iot.report.dto.resp.ActiveDataResp>
     */
    List<ActiveDataResp> getUserRegisterByDate(ActivateBaseReq req);

    /**
     *@description 获取App用户激活总数
     *@author wucheng
     *@params [tenantId]
     *@create 2019/1/8 14:04
     *@return java.lang.Long
     */
    Long getUserRegisterTotal(Long tenantId);
   /**
    *@description 获取用户今日、昨日、一周激活数量
    *@author wucheng
    *@params [tenantId]
    *@create 2019/1/7 20:01
    *@return com.iot.report.dto.resp.ActivatedDetailDataResp
    */
    UserActivatedOrActivateResp getUserRegisterDetail(ActivateBaseReq req);

    /**
     *@description 根据起始日期和租户id获取用户活跃信息
     *@author wucheng
     *@params [req]
     *@create 2019/1/7 11:52
     *@return java.util.List<com.iot.report.dto.resp.ActiveDataResp>
     */
    List<ActiveDataResp> getUserActiveByDate(ActivateBaseReq req);

    /**
     *@description 获取用户今日、昨日、一周活跃数量
     *@author wucheng
     *@params [tenantId]
     *@create 2019/1/8 14:31
     *@return com.iot.report.dto.resp.ActivatedDetailDataResp
     */
    UserActivatedOrActivateResp getUserActiveDetail(ActivateBaseReq req);
    
   /**
    *@description 描述：获取用户激活与活跃数据
    *@author wucheng
    *@params [req]
    *@create 2019/1/8 19:12
    *@return com.iot.report.dto.resp.UserActiveAndActivatedResp
    */
    UserActiveAndActivatedResp getUserActiveAndActivated(ActivateBaseReq req);
    /**
     * 
     * 描述：整理用户活跃信息
     * @author 李帅
     * @created 2019年1月3日 下午8:04:45
     * @since
     */
	void arrangeUserData();

	/**
	 *@description 获取设备和用户 今日和昨日 激活、活跃数量
	 *@author wucheng
	 *@params [req]
	 *@create 2019/1/9 13:57
	 *@return com.iot.report.dto.resp.UserDevActiveAndActivatedResp
	 */
    UserDevActiveAndActivatedResp getUserDevActiveAndActivated(ActivateBaseReq req);
}
