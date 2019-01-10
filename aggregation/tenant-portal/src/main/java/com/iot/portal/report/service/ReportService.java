package com.iot.portal.report.service;

import com.iot.common.beans.SearchParam;
import com.iot.common.helper.Page;
import com.iot.portal.report.vo.resp.*;
import com.iot.report.dto.resp.DailyActivateDeviceResp;
import com.iot.report.dto.resp.DailyActiveDeviceResp;
import com.iot.report.dto.resp.DevDistributionResp;
import com.iot.report.dto.resp.DevPageResp;

import java.util.Collection;
import java.util.List;

/**
 * 
 * 项目名称：IOT云平台
 * 模块名称：聚合层
 * 功能描述：报表Service
 * 创建人： maochengyuan 
 * 创建时间：2019/1/5 15:18
 * 修改人： maochengyuan
 * 修改时间：2019/1/5 15:18
 * 修改描述：
 */
public interface ReportService {

    /**
     * 描述：获取首页概览
     * @author maochengyuan
     * @created 2019/1/9 11:24
     * @param
     * @return com.iot.portal.report.vo.resp.OverviewResp
     */
    OverviewResp getOverview();

    /**
     * 描述：查询地区激活统计信息
     * @author maochengyuan
     * @created 2019/1/9 10:30
     * @param cycle 查询周期
     * @return java.util.List<com.iot.report.dto.resp.DevDistributionResp>
     */
    List<DevDistributionResp> getActiveDistributionData(String cycle);

    /**
     * 描述：查询地区活跃统计信息
     * @author maochengyuan
     * @created 2019/1/9 10:30
     * @param cycle 查询周期
     * @return java.util.List<com.iot.report.dto.resp.DevDistributionResp>
     */
    List<DevDistributionResp> getActivateDistributionData(String cycle);

    /**
     * 描述：获取用户活跃页面数据
     * @author maochengyuan
     * @created 2019/1/7 10:36
     * @param
     * @return com.iot.portal.report.vo.resp.UserActivateResp
     */
    UserActivateResp getUserActiveAll();

    /**
     * 描述：按周期获取用户活跃数据
     * @author maochengyuan
     * @created 2019/1/7 11:54
     * @param cycle 查询周期
     * @return java.util.List<com.iot.portal.report.vo.resp.BaseBean>
     */
    Collection<BaseBean> getUserActiveByCycle(String cycle);

    /**
     * 描述：获取用户激活页面数据
     * @author maochengyuan
     * @created 2019/1/7 10:10
     * @return com.iot.portal.report.vo.resp.UserActivatedResp
     */
    UserActivatedResp getUserActivatedAll();

    /**
     * 描述：按周期获取用户激活数据
     * @author maochengyuan
     * @created 2019/1/7 11:54
     * @param cycle 查询周期
     * @return java.util.List<com.iot.portal.report.vo.resp.BaseBean>
     */
    Collection<BaseBean> getUserActivatedByCycle(String cycle);

    /**
     * 描述：获取设备活跃页面数据
     * @author maochengyuan
     * @created 2019/1/7 10:14
     * @param
     * @return com.iot.portal.report.vo.resp.DevcieActivateResp
     */
    DevcieActivateResp getDeviceActiveAll();

    /**
     * 描述：按周期获取设备活跃数据
     * @author maochengyuan
     * @created 2019/1/7 11:56
     * @param cycle 查询周期
     * @return java.util.List<com.iot.report.dto.resp.DailyActivateDeviceResp>
     */
    Collection<DailyActivateDeviceResp> getDeviceActiveByCycle(String cycle);

    /**
     * 描述：设备活跃明细分页查询
     * @author maochengyuan
     * @created 2019/1/7 11:12
     * @param param 分页参数
     * @return com.iot.common.helper.Page<com.iot.report.dto.resp.DevPageResp>
     */
    Page<DevPageResp> getDeviceActivePage(SearchParam param);

    /**
     * 描述：获取设备激活页面数据
     * @author maochengyuan
     * @created 2019/1/7 10:28
     * @param
     * @return com.iot.portal.report.vo.resp.DevcieActivatedResp
     */
    DevcieActivatedResp getDeviceActivatedAll();

    /**
     * 描述：按周期获取设备激活数据
     * @author maochengyuan
     * @created 2019/1/7 11:56
     * @param cycle 查询周期
     * @return java.util.List<com.iot.report.dto.resp.DailyActiveDeviceResp>
     */
    Collection<DailyActiveDeviceResp> getDeviceActivatedByCycle(String cycle);

    /**
     * 描述：设备激活明细分页查询
     * @author maochengyuan
     * @created 2019/1/7 11:12
     * @param param 分页参数
     * @return com.iot.common.helper.Page<com.iot.report.dto.resp.DevPageResp>
     */
    Page<DevPageResp> getDeviceActivatedByPage(SearchParam param);

}
