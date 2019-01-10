package com.iot.report.service.impl;

import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.common.util.CalendarUtil;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.StringUtil;
import com.iot.redis.RedisCacheUtil;
import com.iot.report.constants.ModuleConstants;
import com.iot.report.contants.ReportConstants;
import com.iot.report.dto.req.ActivateBaseReq;
import com.iot.report.dto.req.DevActivateOrActiveReq;
import com.iot.report.dto.req.DevActivatedVo;
import com.iot.report.dto.req.DevDistributionReq;
import com.iot.report.dto.resp.*;
import com.iot.report.entity.DeviceActivatedInfo;
import com.iot.report.entity.DeviceActiveInfo;
import com.iot.report.enums.QueryTypeEnums;
import com.iot.report.exception.ReportExceptionEnum;
import com.iot.report.service.DeviceActivateService;
import com.iot.report.service.DeviceReportService;
import com.iot.report.utils.RedisKeyUtils;
import com.iot.tenant.api.TenantApi;
import com.iot.tenant.vo.resp.TenantInfoResp;
import com.iot.user.constant.UserConstants;
import com.iot.user.vo.UserActivated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Description:    立达信IOT云平台 设备报表
 * @Author:         nongchongwei
 * @CreateDate:     2019/1/4 15:12
 * @UpdateUser:     nongchongwei
 * @UpdateDate:     2019/1/4 15:12
 * @UpdateRemark:
 * @Version:        1.0
 */
@Service
public class DeviceActivateServiceImpl implements DeviceActivateService {
    private static final Logger loger = LoggerFactory.getLogger(DeviceActivateServiceImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private TenantApi tenantApi;
    @Autowired
    private DeviceReportService deviceReportService;

    /**
     * 描述: 定时保存设备激活信息
     * @author  nongchongwei
     * @param
     * @return  void
     * @exception
     * @date     2019/1/5 16:48
     */
    @Override
    public void saveDeviceActiveInfo() {
        List<TenantInfoResp> tenantInfoRespList = tenantApi.getTenantList();
        if(null == tenantInfoRespList || tenantInfoRespList.size() == 0){
            return ;
        }
        String dateStr = CalendarUtil.getYesterdayByCalendar(CalendarUtil.YYYYMMDD);
        for(TenantInfoResp tenantInfoResp : tenantInfoRespList){
            Long tenantId = tenantInfoResp.getId();
            String key = RedisKeyUtils.getDevActivatedKey(dateStr,tenantId);
            List<DevActivatedVo> devActivatedVoList = RedisCacheUtil.listGetAll(key,DevActivatedVo.class);
            if(null != devActivatedVoList && devActivatedVoList.size() > 0){
                for(DevActivatedVo devActivatedVo : devActivatedVoList){
                    try{
                        DeviceActivatedInfo deviceActivateInfo = new DeviceActivatedInfo();
                        String distributionKey =  RedisKeyUtils.getDistributionKey(devActivatedVo.getDevId());
                        DeviceActivateCacheInfo deviceActivateCacheInfo = RedisCacheUtil.valueObjGet(distributionKey, DeviceActivateCacheInfo.class);
                        deviceActivateInfo.setCountryCode(deviceActivateCacheInfo.getCountryCode());
                        deviceActivateInfo.setProvince(deviceActivateCacheInfo.getProvince());
                        deviceActivateInfo.setCity(deviceActivateCacheInfo.getCity());
                        deviceActivateInfo.setDeviceType(devActivatedVo.getDeviceTypeId());
                        deviceActivateInfo.setActivateDate(devActivatedVo.getTime());
                        deviceActivateInfo.setUuid(devActivatedVo.getDevId());
                        deviceActivateInfo.setTenantId(tenantId);
                        mongoTemplate.insert(deviceActivateInfo);
                    }catch (Exception e){
                        loger.error("",e);
                    }
                }
            }
            RedisCacheUtil.delete(key);
        }
        return ;
    }
    /**
     * 描述: 查询激活信息
     * @author  nongchongwei
     * @param uuid
     * @return  com.iot.report.entity.DeviceActiveInfo
     * @exception
     * @date     2019/1/5 16:49
     */
    @Override
    public DeviceActivatedInfo getDeviceActiveInfoByUuid(String uuid) {
        Query query = new Query().addCriteria(Criteria.where("uuid").is(uuid));
        DeviceActivatedInfo deviceActivateInfo = mongoTemplate.findOne(query, DeviceActivatedInfo.class);
        return deviceActivateInfo;
    }

    @Override
    public List<DevDistributionResp> getDistributionAmount(DevDistributionReq req) {
        String date_field = "activateDate";
        String collection = ModuleConstants.TABLE_ACTIVATED_INFO;
        String key = RedisKeyUtils.getDevDistributionActivateKey();
        if(QueryTypeEnums.ACTIVE.getCode() == req.getActiveOrActivate()){
            collection = ModuleConstants.TABLE_ACTIVE_INFO;
            date_field = "activeDate";
            key = RedisKeyUtils.getDevDistributionActiveKey();
        }
        List<DevDistributionResp> devDistributionRespList = RedisCacheUtil.listGetAll(key,DevDistributionResp.class);
        if(null != devDistributionRespList && devDistributionRespList.size() > 0){
            return devDistributionRespList;
        }

        devDistributionRespList = new ArrayList<>();
        //统计各国激活设备
        Aggregation distributionAgg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("tenantId").is(req.getTenantId())
                        .and(date_field).gte(req.getBeginDate()).lte(req.getEndDate())),
                Aggregation.group("countryCode","deviceType").count().as("amount").first("countryCode").as("countryCode")
                        .last("deviceType").as("productTypeId"),
                Aggregation.project("countryCode","amount","productTypeId"));
        List<DistributionResp> distributionActivateRespList = mongoTemplate.aggregate(distributionAgg, collection, DistributionResp.class).getMappedResults();
        Map<String,Map<Long,Long>> distributionActivateMap = new HashMap<>();
        if(!CommonUtil.isEmpty(distributionActivateRespList)){
            for(DistributionResp distributionResp : distributionActivateRespList){
                if(StringUtil.isBlank(distributionResp.getCountryCode())){
                    continue;
                }
                Map<Long,Long>  devAmountMap  = distributionActivateMap.get(distributionResp.getCountryCode());
                if(null == devAmountMap){
                    devAmountMap = new HashMap<>();
                    distributionActivateMap.put(distributionResp.getCountryCode(),devAmountMap);
                }
                devAmountMap.put(distributionResp.getProductTypeId(),distributionResp.getAmount());
                Long allAmount = devAmountMap.get(-1L);
                if(null == allAmount){
                    devAmountMap.put(-1L,distributionResp.getAmount());
                }else {
                    devAmountMap.put(-1L,allAmount+distributionResp.getAmount());
                }
            }
            if(!distributionActivateMap.isEmpty()){
                for (Map.Entry<String, Map<Long,Long>> distributionMap : distributionActivateMap.entrySet()) {
                    DevDistributionResp devDistributionResp = new DevDistributionResp();
                    devDistributionResp.setCode(distributionMap.getKey());
                    devDistributionResp.setProductTypeIdAmountMap(distributionMap.getValue());
                    devDistributionRespList.add(devDistributionResp);
                }
            }
            RedisCacheUtil.listSet(key,devDistributionRespList,10*60L,true);
        }
        return devDistributionRespList;
    }

    @Override
    public Page<DevPageResp> getPageDeviceActivateOrActive(DevActivateOrActiveReq req) {

        String date_field = "activateDate";
        String collection = ModuleConstants.TABLE_ACTIVATED_INFO;
        if(QueryTypeEnums.ACTIVE.getCode() == req.getQueryType()){
            collection = ModuleConstants.TABLE_ACTIVE_INFO;
            date_field = "activeDate";
        }
        com.iot.common.helper.Page<DevPageResp> myPage = new com.iot.common.helper.Page<DevPageResp>();
        myPage.setPageNum(req.getPageNum());
        myPage.setPageSize(req.getPageSize());
        Aggregation activateAgg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("tenantId").is(req.getTenantId())),
                Aggregation.project(date_field).andExpression("{$dateToString:{format:'%Y-%m-%d',date:'$"+date_field+"'}}").as("dateField"),
                Aggregation.group("dateField").first("dateField").as("dateField"),
                Aggregation.project("dateField"),
                Aggregation.sort(new Sort(Sort.Direction.DESC, "dateField")));
        List<DevPageBean> activatedDateList  = mongoTemplate.aggregate(activateAgg, collection, DevPageBean.class).getMappedResults();
        if(CommonUtil.isEmpty(activatedDateList)){
            return myPage;
        }
        List<String> dateList = new ArrayList<>();
        for(DevPageBean devPageBean : activatedDateList){
            if(StringUtil.isNotBlank(devPageBean.getDateField())){
                dateList.add(devPageBean.getDateField());
            }
        }
        long resultsCount = dateList.size();
        int startNum = req.getPageNum()*req.getPageSize()-1;
        int startIndex = startNum < 0 ? 0 : startNum;
        int endIndex = startIndex+req.getPageSize();
        if(startIndex > dateList.size()-1){
            return myPage;
        }
        if(endIndex > dateList.size()){
            endIndex = dateList.size();
        }
        List<String> queryDateList = new ArrayList<>();
        for(int i = startIndex ; i <endIndex ;i++){
            queryDateList.add(dateList.get(i));
        }

        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(Aggregation.project(date_field,"deviceType","tenantId").andExpression("{$dateToString:{format:'%Y-%m-%d',date:'$"+date_field+"'}}").as("dateField"));
        operations.add(Aggregation.match(Criteria.where("tenantId").is(req.getTenantId()).and("dateField").in(queryDateList)));
        operations.add(Aggregation.group("dateField","deviceType").count().as("count").first("dateField").as("dateField")
                .last("deviceType").as("productTypeId"));
        operations.add(Aggregation.project("dateField","count","productTypeId"));
        operations.add( Aggregation.sort(new Sort(Sort.Direction.DESC, "dateField")));
        Aggregation aggregationCount = Aggregation.newAggregation(operations);  //operations为空，会报错
        List<DevPageBean> devPageBeanList  = mongoTemplate.aggregate(aggregationCount, collection, DevPageBean.class).getMappedResults();


        Map<String,Long> dayTotalMap = new HashMap<>();
        List<DevPageResp> devPageRespList = new ArrayList<>();
        Map<String,List<DevPageAmount>> devPageAmountMap = new HashMap<>();
        if(!CommonUtil.isEmpty(devPageBeanList)){
            for(DevPageBean devPageBean : devPageBeanList){
                Long total = dayTotalMap.get(devPageBean.getDateField());
                if(null == total){
                    dayTotalMap.put(devPageBean.getDateField(),devPageBean.getCount());
                }else {
                    dayTotalMap.put(devPageBean.getDateField(),devPageBean.getCount()+total);
                }
                DevPageAmount devPageAmount = new DevPageAmount();
                devPageAmount.setAmount(devPageBean.getCount());
                devPageAmount.setProductTypeId(devPageBean.getProductTypeId());
                List<DevPageAmount> devPageAmountList = devPageAmountMap.get(devPageBean.getDateField());
                if(null == devPageAmountList){
                    devPageAmountList = new ArrayList<>();
                    devPageAmountMap.put(devPageBean.getDateField(),devPageAmountList);
                }
                devPageAmountList.add(devPageAmount);
            }
            for (Map.Entry<String, Long> m : dayTotalMap.entrySet()) {
                Long total = m.getValue();
                List<DevPageAmount> devActivateAmountList = devPageAmountMap.get(m.getKey());
                DevPageResp devPageResp = new DevPageResp(m.getKey(),total,devActivateAmountList);
                devPageRespList.add(devPageResp);
            }
            Collections.sort(devPageRespList, new Comparator<DevPageResp>() {
                public int compare(DevPageResp devActivateResp1, DevPageResp devActivateResp2) {
                    return devActivateResp2.getDate().compareTo(devActivateResp1.getDate());//降序
                }
            });
        }

        myPage.setTotal(resultsCount);
        myPage.setResult(devPageRespList);
        return myPage;
    }
    /**
     * 描述：获取设备活跃页面数据
     * @author maochengyuan
     * @created 2019/1/8 11:40
     * @param req
     * @return com.iot.report.dto.resp.DevActivateResp
     */
    @Override
    public DevActivateResp getDeviceActive(ActivateBaseReq req){
        if (req.getTenantId() == null) {
            throw new BusinessException(ReportExceptionEnum.TENANTID_IS_NULL, "tenantId is null");
        }
        DevActivateResp devActivateResp = new DevActivateResp();

        // 获取今日设备活跃数量（实际获取的是昨天的）
        Date lastToday1 = CalendarUtil.getDateBeforeOrAfter(req.getBeginDate(), -1);
        ActivateBaseReq todDevActiveReq = new ActivateBaseReq(lastToday1, req.getBeginDate() , req.getTenantId());
        List<DailyActiveDeviceResp> todDevActiveList = deviceReportService.getDailyActiveDevice(todDevActiveReq);
        Long todDevActivate = 0L;
        if (todDevActiveList != null && todDevActiveList.size() > 0) {
            for (DailyActiveDeviceResp w: todDevActiveList) {
                todDevActivate += w.getTotals();
            }
        }
        devActivateResp.setTodDevcieActivate(todDevActivate);

        // 获取昨日的设备活跃数量 （实际获取的是前天的）
        Date lastToday2 = CalendarUtil.getDateBeforeOrAfter(req.getBeginDate(), -2);
        ActivateBaseReq yesDevActiveReq = new ActivateBaseReq(lastToday1, lastToday2 , req.getTenantId());
        List<DailyActiveDeviceResp> yesDevActiveList = deviceReportService.getDailyActiveDevice(yesDevActiveReq);
        Long yesDevActivated = 0L;
        if (yesDevActiveList != null && yesDevActiveList.size() > 0) {
            for (DailyActiveDeviceResp w: yesDevActiveList) {
                yesDevActivated += w.getTotals();
            }
        }
        devActivateResp.setYesDevcieActivate(yesDevActivated);

        // 获取本周活跃总数
        Date lastToday7 = CalendarUtil.getDateBeforeOrAfter(req.getBeginDate(), -7);
        ActivateBaseReq lastWeekUserActiveReq = new ActivateBaseReq(lastToday7, req.getBeginDate() , req.getTenantId());
        List<DailyActiveDeviceResp> lastWeekDevActiveList = deviceReportService.getDailyActiveDevice(lastWeekUserActiveReq);
        Long lastWeekDevActivated = 0L;
        if (lastWeekDevActiveList != null && lastWeekDevActiveList.size() > 0) {
            for (DailyActiveDeviceResp w: lastWeekDevActiveList) {
                lastWeekDevActivated += w.getTotals();
            }
        }
        devActivateResp.setLastWeekDevcieActivate(lastWeekDevActivated);

        // 获取最近7日的前7日设备活跃总数
        Date lastToday14 = CalendarUtil.getDateBeforeOrAfter(lastToday7, -7);
        ActivateBaseReq beforeLastWeekDevActiveEq = new ActivateBaseReq(lastToday14, lastToday7, req.getTenantId());
        List<DailyActiveDeviceResp> beforeLastWeekUserActiveList = deviceReportService.getDailyActiveDevice(beforeLastWeekDevActiveEq);
        Long beforeLastWeekDevActivated = 0L;
        if (beforeLastWeekUserActiveList != null && lastWeekDevActiveList.size() > 0) {
            for (DailyActiveDeviceResp w : beforeLastWeekUserActiveList) {
                beforeLastWeekDevActivated += w.getTotals();
            }
        }
        devActivateResp.setBeforeLastWeekDevcieActivate(beforeLastWeekDevActivated);
        return devActivateResp;
    }

    /**
     * 描述：获取设备激活页面数据
     * @author maochengyuan
     * @created 2019/1/8 11:41
     * @param req
     * @return com.iot.report.dto.resp.DevActivateResp
     */
    @Override
    public DevActivateResp getDeviceActivated(ActivateBaseReq req){
        if (req.getTenantId() == null) {
            throw new BusinessException(ReportExceptionEnum.TENANTID_IS_NULL, "tenantId is null");
        }
        DevActivateResp devActivateResp = new DevActivateResp();

        // 获取今日激活的设备数量 (实际获取的是昨天的)
        Date lastToday1 = CalendarUtil.getDateBeforeOrAfter(req.getBeginDate(), -1);
        ActivateBaseReq todDevActivatedReq = new ActivateBaseReq(lastToday1, req.getBeginDate() , req.getTenantId());
        List<DailyActivateDeviceResp>  todDevActivatedList = deviceReportService.getDailyActivatedDevice(todDevActivatedReq);
        Long todDevActivated = 0L;
        if (todDevActivatedList != null && todDevActivatedList.size() > 0) {
            for (DailyActivateDeviceResp w:todDevActivatedList) {
                todDevActivated += w.getTotals();
            }
        }
        devActivateResp.setTodDevcieActivated(todDevActivated);

        // 获取昨日激活的设备数量 (实际获取的是前天的)
        Date lastToday2 = CalendarUtil.getDateBeforeOrAfter(req.getBeginDate(), -2);
        ActivateBaseReq yesDevActivatedReq = new ActivateBaseReq(lastToday1, lastToday2 , req.getTenantId());
        List<DailyActivateDeviceResp>  yesDevActivatedList = deviceReportService.getDailyActivatedDevice(yesDevActivatedReq);
        Long yesDevActivated = 0L;
        if (yesDevActivatedList != null && yesDevActivatedList.size() > 0) {
            for (DailyActivateDeviceResp w:yesDevActivatedList) {
                yesDevActivated += w.getTotals();
            }
        }
        devActivateResp.setYesDevcieActivated(yesDevActivated);

        // 获取本周激活总数
        Date lastToday7 = CalendarUtil.getDateBeforeOrAfter(req.getBeginDate(), -7);
        // 获取一周类激活的设备数量
        ActivateBaseReq lastWeekDevActivatedReq = new ActivateBaseReq(lastToday7, req.getBeginDate() , req.getTenantId());
        List<DailyActivateDeviceResp> lastWeekDevActivatedList = deviceReportService.getDailyActivatedDevice(lastWeekDevActivatedReq);
        Long lastWeekDevActivated = 0L;
        if (lastWeekDevActivatedList != null && lastWeekDevActivatedList.size() > 0) {
            for (DailyActivateDeviceResp w:lastWeekDevActivatedList) {
                lastWeekDevActivated += w.getTotals();
            }
        }
        devActivateResp.setLastWeekDevcieActivated(lastWeekDevActivated);

        // 获取最近7日的前7日设备激活总数
        Date lastToday14 = CalendarUtil.getDateBeforeOrAfter(lastToday7, -7);
        ActivateBaseReq beforeLastWeekDevActivatedReq = new ActivateBaseReq(lastToday14, lastToday7, req.getTenantId());
        List<DailyActivateDeviceResp> beforeLastWeekDevActivatedList = deviceReportService.getDailyActivatedDevice(beforeLastWeekDevActivatedReq);
        Long beforeLastWeekDevActivated = 0L;
        if (beforeLastWeekDevActivatedList != null && beforeLastWeekDevActivatedList.size() > 0) {
            for (DailyActivateDeviceResp w:beforeLastWeekDevActivatedList) {
                beforeLastWeekDevActivated += w.getTotals();
            }
            devActivateResp.setBeforeLastWeekDevcieActivated(beforeLastWeekDevActivated);
        }
        return devActivateResp;
    }

    /**
     * 描述：获取设备激活/活跃数据
     * @author maochengyuan
     * @created 2019/1/8 11:41
     * @param req
     * @return com.iot.report.dto.resp.DevActivateResp
     */
    @Override
    public DevActivateResp getDeviceActiveAndActivated(ActivateBaseReq req){
        DevActivateResp devActivate = getDeviceActive(req);
        DevActivateResp devActivated = getDeviceActivated(req);
        DevActivateResp result = new DevActivateResp(devActivate.getYesDevcieActivate(), devActivated.getYesDevcieActivated(),
                devActivate.getTodDevcieActivate(),  devActivated.getTodDevcieActivated(),
                devActivate.getLastWeekDevcieActivate(), devActivated.getLastWeekDevcieActivated(),
                devActivate.getBeforeLastWeekDevcieActivate(), devActivated.getBeforeLastWeekDevcieActivated());
        return result;
    }
    
    /**
     *@description 获取设备激活总数
     *@author wucheng
     *@params [tenantId]
     *@create 2019/1/9 10:42
     *@return java.lang.Long
     */
    @Override
    public Long getDeviceActivatedCount(Long tenantId) {
        Query query = new Query().addCriteria(Criteria.where("tenantId").is(tenantId));
        Long count = mongoTemplate.count(query, DeviceActivatedInfo.class);
        return count;
    }
}
