package com.iot.portal.report.service.impl;

import com.iot.common.beans.BeanUtil;
import com.iot.common.beans.SearchParam;
import com.iot.common.helper.Page;
import com.iot.common.util.CommonUtil;
import com.iot.portal.common.service.CommonServiceImpl;
import com.iot.portal.report.enums.ReportCycleEnums;
import com.iot.portal.report.service.ReportService;
import com.iot.portal.report.vo.resp.*;
import com.iot.report.api.DeviceActivateApi;
import com.iot.report.api.DeviceReportApi;
import com.iot.report.api.UserActiveApi;
import com.iot.report.dto.req.ActivateBaseReq;
import com.iot.report.dto.req.DevPageReq;
import com.iot.report.dto.resp.*;
import com.iot.saas.SaaSContextHolder;
import com.iot.user.api.UserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

/** 
 * 
 * 项目名称：IOT云平台
 * 模块名称：聚合层
 * 功能描述：报表Service实现
 * 创建人： maochengyuan 
 * 创建时间：2019/1/5 15:18
 * 修改人： maochengyuan
 * 修改时间：2019/1/5 15:18
 * 修改描述：
 */
@Service("ReportService")
public class ReportServiceImpl extends CommonServiceImpl implements ReportService {

    /**查询周期*/
    private final static List<String> CYCLE_LIST = Arrays.asList(new String[]{"1", "7", "14","30"});

    @Autowired
    private UserActiveApi userActiveApi;

    @Autowired
    private UserApi userApi;

    @Autowired
    private DeviceActivateApi deviceActivateApi;

    @Autowired
    private DeviceReportApi deviceReportApi;

    /**
     * 描述：获取首页概览
     * @author maochengyuan
     * @created 2019/1/9 11:24
     * @param
     * @return com.iot.portal.report.vo.resp.OverviewResp
     */
    @Override
    public OverviewResp getOverview() {
        OverviewResp res = new OverviewResp();
        String cycle = ReportCycleEnums.CYCLE_1.getCode();
        UserDevActiveAndActivatedResp resp = this.userActiveApi.getUserDevActiveAndActivated(this.getCommParam(cycle));
        if(resp != null){
            BeanUtil.copyProperties(resp, res);
        }
        res.setUserActivatedDetail(this.getUserActivatedByCycle(cycle));
        res.setUserActiveDetail(this.getUserActiveByCycle(cycle));
        res.setDeviceActivatedDetail(this.getDailyActivatedDeviceTotals(cycle));
        res.setDeviceActiveDetail(this.getDailyActiveDeviceTotals(cycle));
        return res;
    }

    /**
     * 描述：查询地区激活统计信息
     * @author maochengyuan
     * @created 2019/1/9 10:30
     * @param cycle 查询周期
     * @return java.util.List<com.iot.report.dto.resp.DevDistributionResp>
     */
    @Override
    public List<DevDistributionResp> getActiveDistributionData(String cycle){
        return this.deviceActivateApi.getActiveDistributionData(this.getCommParam(cycle));
    }

    /** 
     * 描述：查询地区活跃统计信息
     * @author maochengyuan
     * @created 2019/1/9 10:30
     * @param cycle 查询周期
     * @return java.util.List<com.iot.report.dto.resp.DevDistributionResp>
     */
    @Override
    public List<DevDistributionResp> getActivateDistributionData(String cycle){
        return this.deviceActivateApi.getActivateDistributionData(this.getCommParam(cycle));
    }

    /**
     * 描述：获取用户活跃页面数据
     * @author maochengyuan
     * @created 2019/1/7 10:36
     * @param
     * @return com.iot.portal.report.vo.resp.UserActivateResp
     */
    @Override
    public UserActivateResp getUserActiveAll(){
        DevActivateResp res = this.deviceActivateApi.getDeviceActive(this.getCommParam(ReportCycleEnums.CYCLE_1));
        if(res == null){
            return new UserActivateResp();
        }
        return new UserActivateResp(res.getYesDevcieActivated(), res.getLastWeekDevcieActivated());
    }

    /**
     * 描述：按周期获取用户活跃数据
     * @author maochengyuan
     * @created 2019/1/7 11:54
     * @param cycle 查询周期
     * @return java.util.Collection<com.iot.portal.report.vo.resp.BaseBean>
     */
    @Override
    public Collection<BaseBean> getUserActiveByCycle(String cycle){
        List<ActiveDataResp> list = this.userActiveApi.getUserRegisterByDate(this.getCommParam(cycle));
        LinkedHashMap<String, BaseBean> restMap = bulidBaseBean(cycle, BaseBean.class);
        if(!CommonUtil.isEmpty(list)){
            list.forEach(o->{
                restMap.put(o.getActivatedDate(), new BaseBean(o.getActivatedDate(), o.getTotalNumber()));
            });
        }
        return restMap.values();
    }

    /**
     * 描述：获取用户激活页面数据
     * @author maochengyuan
     * @created 2019/1/7 10:10
     * @return com.iot.portal.report.vo.resp.UserActivatedResp
     */
    @Override
    public UserActivatedResp getUserActivatedAll(){
        /**查询设备激活数据*/
        DevActivateResp res = this.deviceActivateApi.getDeviceActive(this.getCommParam(ReportCycleEnums.CYCLE_1));
        /**查询设备激活总数*/
        long count = this.userApi.getAppUserCount(SaaSContextHolder.currentTenantId());
        if(res == null){
            return new UserActivatedResp(count);
        }
        return new UserActivatedResp(res.getYesDevcieActivated(), res.getLastWeekDevcieActivated(), count);
    }

    /**
     * 描述：按周期获取用户激活数据
     * @author maochengyuan
     * @created 2019/1/7 11:54
     * @param cycle 查询周期
     * @return java.util.Collection<com.iot.portal.report.vo.resp.BaseBean>
     */
    @Override
    public Collection<BaseBean> getUserActivatedByCycle(String cycle){
        List<ActiveDataResp> list = this.userActiveApi.getUserActiveByDate(this.getCommParam(cycle));
        LinkedHashMap<String, BaseBean> restMap = bulidBaseBean(cycle, BaseBean.class);
        if(!CommonUtil.isEmpty(list)){
            list.forEach(o->{
                restMap.put(o.getActivatedDate(), new BaseBean(o.getActivatedDate(), o.getTotalNumber()));
            });
        }
        return restMap.values();
    }

    /**
     * 描述：获取设备活跃页面数据
     * @author maochengyuan
     * @created 2019/1/7 10:14
     * @param
     * @return com.iot.portal.report.vo.resp.DevcieActivateResp
     */
    @Override
    public DevcieActivateResp getDeviceActiveAll(){
        DevActivateResp res = this.deviceActivateApi.getDeviceActive(this.getCommParam(ReportCycleEnums.CYCLE_1));
        if(res == null){
            return new DevcieActivateResp();
        }
        return new DevcieActivateResp(res.getYesDevcieActivate(), res.getLastWeekDevcieActivate());
    }

    /**
     * 描述：按周期获取设备活跃数据
     * @author maochengyuan
     * @created 2019/1/7 11:56
     * @param cycle 查询周期
     * @return java.util.Collection<com.iot.report.dto.resp.DailyActivateDeviceResp>
     */
    @Override
    public Collection<DailyActivateDeviceResp> getDeviceActiveByCycle(String cycle){
        List<DailyActivateDeviceResp> list = this.deviceReportApi.getDailyActivatedDevice(this.getCommParam(cycle));
        LinkedHashMap<String, DailyActivateDeviceResp> restMap = bulidBaseBean(cycle, DailyActivateDeviceResp.class);
        if(!CommonUtil.isEmpty(list)){
            list.forEach(o->{
                restMap.put(o.getDataDate(), o);
            });
        }
        return restMap.values();
    }

    /**
     * 描述：设备活跃明细分页查询
     * @author maochengyuan
     * @created 2019/1/7 11:12
     * @param param 分页参数
     * @return com.iot.common.helper.Page<com.iot.report.dto.resp.DevPageResp>
     */
    @Override
    public Page<DevPageResp> getDeviceActivePage(SearchParam param){
        return this.deviceActivateApi.getPageDeviceActive(this.getCommParam(param));
    }

    /**
     * 描述：获取设备激活页面数据
     * @author maochengyuan
     * @created 2019/1/7 10:28
     * @param
     * @return com.iot.portal.report.vo.resp.DevcieActivatedResp
     */
    @Override
    public DevcieActivatedResp getDeviceActivatedAll(){
        /**查询设备激活数据*/
        DevActivateResp res = this.deviceActivateApi.getDeviceActive(this.getCommParam(ReportCycleEnums.CYCLE_1));
        /**查询设备激活总数*/
        Long count = this.deviceActivateApi.getDeviceActivatedCount(SaaSContextHolder.currentTenantId());
        if(res == null){
            return new DevcieActivatedResp(count);
        }
        return new DevcieActivatedResp(res.getYesDevcieActivated(), res.getLastWeekDevcieActivated(), count);
    }

    /**
     * 描述：按周期获取设备激活数据
     * @author maochengyuan
     * @created 2019/1/7 11:56
     * @param cycle 查询周期
     * @return java.util.Collection<com.iot.report.dto.resp.DailyActiveDeviceResp>
     */
    @Override
    public Collection<DailyActiveDeviceResp> getDeviceActivatedByCycle(String cycle){
        List<DailyActiveDeviceResp> list = this.deviceReportApi.getDailyActiveDevice(this.getCommParam(cycle));
        LinkedHashMap<String, DailyActiveDeviceResp> restMap = bulidBaseBean(cycle, DailyActiveDeviceResp.class);
        if(!CommonUtil.isEmpty(list)){
            list.forEach(o->{
                restMap.put(o.getDataDate(), o);
            });
        }
        return restMap.values();
    }

    /**
     * 描述：设备激活明细分页查询
     * @author maochengyuan
     * @created 2019/1/7 11:12
     * @param param 分页参数
     * @return com.iot.common.helper.Page<com.iot.report.dto.resp.DevPageResp>
     */
    @Override
    public Page<DevPageResp> getDeviceActivatedByPage(SearchParam param){
        return this.deviceActivateApi.getPageDeviceActivate(this.getCommParam(param));
    }

    /**
     * 描述：查询设备激活总计
     * @author maochengyuan
     * @created 2019/1/9 14:12
     * @param cycle 查询周期
     * @return java.util.Collection<com.iot.portal.report.vo.resp.BaseBean>
     */
    private Collection<BaseBean> getDailyActivatedDeviceTotals(String cycle){
        List<DailyActivateDeviceTotalsResp> list = this.deviceReportApi.getDailyActivatedDeviceTotals(this.getCommParam(cycle));
        LinkedHashMap<String, BaseBean> restMap = bulidBaseBean(cycle, BaseBean.class);
        if(!CommonUtil.isEmpty(list)){
            list.forEach(o->{
                restMap.put(o.getDataDate(), new BaseBean(o.getDataDate(), o.getTotals()));
            });
        }
        return restMap.values();
    }

    /**
     * 描述：查询设备活跃总计
     * @author maochengyuan
     * @created 2019/1/9 14:12
     * @param cycle 查询周期
     * @return java.util.Collection<com.iot.portal.report.vo.resp.BaseBean>
     */
    private Collection<BaseBean> getDailyActiveDeviceTotals(String cycle){
        List<DailyActiveDeviceTotalsResp> list = this.deviceReportApi.getDailyActiveDeviceTotals(this.getCommParam(cycle));
        LinkedHashMap<String, BaseBean> restMap = bulidBaseBean(cycle, BaseBean.class);
        if(!CommonUtil.isEmpty(list)){
            list.forEach(o->{
                restMap.put(o.getDataDate(), new BaseBean(o.getDataDate(), o.getTotals()));
            });
        }
        return restMap.values();
    }

    /**
     * 描述：构建查询参数
     * @author maochengyuan
     * @created 2019/1/9 9:36
     * @param param  查询条件
     * @return com.iot.report.dto.req.DevPageReq
     */
    private DevPageReq getCommParam(SearchParam param){
        return new DevPageReq(param, SaaSContextHolder.currentTenantId());
    }

    /**
     * 描述：构建查询参数
     * @author maochengyuan
     * @created 2019/1/8 19:23
     * @param cycle 周期
     * @return com.iot.report.dto.req.ActivateBaseReq
     */
    private ActivateBaseReq getCommParam(String cycle){
        return this.getCommParam(ReportCycleEnums.getCycleEnums(cycle));
    }

    /**
     * 描述：构建查询参数
     * @author maochengyuan
     * @created 2019/1/8 19:23
     * @param reportCycle 周期
     * @return com.iot.report.dto.req.ActivateBaseReq
     */
    private ActivateBaseReq getCommParam(ReportCycleEnums reportCycle){
        return new ActivateBaseReq(super.getDateArray(CYCLE_LIST, reportCycle.getCode()), SaaSContextHolder.currentTenantId());
    }

}
