package com.iot.report.service.impl;

import com.iot.common.exception.BusinessException;
import com.iot.common.util.CalendarUtil;
import com.iot.redis.RedisCacheUtil;
import com.iot.report.contants.ReportConstants;
import com.iot.report.dto.req.ActivateBaseReq;
import com.iot.report.dto.resp.*;
import com.iot.report.entity.UserActivatedEntity;
import com.iot.report.entity.UserActiveInfo;
import com.iot.report.exception.ReportExceptionEnum;
import com.iot.report.service.DeviceReportService;
import com.iot.report.service.UserActiveService;
import com.iot.user.api.UserApi;
import com.iot.user.constant.UserConstants;
import com.iot.user.vo.FetchUserResp;
import com.iot.user.vo.UserActivated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.DateOperators;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserActiveServiceImpl implements UserActiveService{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserApi userApi;

    @Autowired
    private DeviceReportService deviceReportService;

    private static final String COLLECTION_NAME="user_activated_info";

    /**
     *@description 保存激活用户数据
     *@author wucheng
     *@params [entity]
     *@create 2018/12/29 14:05
     *@return int
     */
    @Override
    public void saveUserActivated(UserActivatedEntity entity) {
        Query query = Query.query(Criteria.where("uuid").is(entity.getUuid()));
        List<UserActivatedEntity> userActivatedEntity = this.mongoTemplate.find(query, UserActivatedEntity.class);
        if(userActivatedEntity == null || userActivatedEntity.size() == 0) {
            mongoTemplate.insert(entity);
        } else {
            log.debug("user is activated");
        }
    }

    /**
     *@description 根据起始日期和租户id获取用户激活信息
     *@author wucheng
     *@params [req]
     *@create 2019/1/7 11:51
     *@return java.util.List<com.iot.report.dto.resp.ActiveDataResp>
     */
    @Override
    public  List<ActiveDataResp> getUserRegisterByDate(ActivateBaseReq req) {
        // 判断查询参数
        if (req.getBeginDate() == null) {
            throw new BusinessException(ReportExceptionEnum.BEGINDATE_IS_NULL, "startDate is null");
        }
        if (req.getTenantId() == null) {
            throw new BusinessException(ReportExceptionEnum.TENANTID_IS_NULL, "tenantId is null");
        }
        if (req.getEndDate() == null) {
            throw new BusinessException(ReportExceptionEnum.ENDDATE_IS_NULL, "EndDate is null");
        }
        // 组合查询条件
        TypedAggregation agg = Aggregation.newAggregation(
                UserActivatedEntity.class
                ,Aggregation.match(
                        Criteria.where("tenantId").is(req.getTenantId()).and("activatedDate").gte(req.getBeginDate()).lt(req.getEndDate())
                )
                ,Aggregation.project().and(DateOperators.DateToString.dateOf("activatedDate").toString("%Y-%m-%d")).as("activatedDate")
                ,Aggregation.group("activatedDate").count().as("totalNumber")
                ,Aggregation.project("activatedDate", "totalNumber").and("activatedDate").previousOperation()
                ,Aggregation.sort(Sort.Direction.DESC, "activatedDate")
        );
        // 获取查询结果
        AggregationResults<ActiveDataResp> activeDatas = mongoTemplate.aggregate(agg, UserActivatedEntity.class, ActiveDataResp.class);
        List<ActiveDataResp> result = activeDatas.getMappedResults();
        return result;
    }
    /**
     *@description 根据起始日期和租户id获取用户活跃信息
     *@author wucheng
     *@params [req]
     *@create 2019/1/7 11:54
     *@return java.util.List<com.iot.report.dto.resp.ActiveDataResp>
     */
    @Override
    public List<ActiveDataResp> getUserActiveByDate(ActivateBaseReq req) {
        // 判断查询参数
        if (req.getBeginDate() == null) {
            throw new BusinessException(ReportExceptionEnum.BEGINDATE_IS_NULL, "startDate is null");
        }
        if (req.getTenantId() == null) {
            throw new BusinessException(ReportExceptionEnum.TENANTID_IS_NULL, "tenantId is null");
        }
        if (req.getEndDate() == null) {
            throw new BusinessException(ReportExceptionEnum.ENDDATE_IS_NULL, "EndDate is null");
        }
        // 组合查询条件
        TypedAggregation agg = Aggregation.newAggregation(
                UserActiveInfo.class
                ,Aggregation.match(
                        Criteria.where("tenantId").is(req.getTenantId()).and("dataDate").gte(req.getBeginDate()).lt(req.getEndDate())
                )
                ,Aggregation.project().and(DateOperators.DateToString.dateOf("dataDate").toString("%Y-%m-%d")).as("activatedDate")
                ,Aggregation.group("activatedDate").count().as("totalNumber")
                ,Aggregation.project("activatedDate", "totalNumber").and("activatedDate").previousOperation()
                ,Aggregation.sort(Sort.Direction.DESC, "activatedDate")
        );
        // 获取查询结果
        AggregationResults<ActiveDataResp> activeDatas = mongoTemplate.aggregate(agg, UserActiveInfo.class, ActiveDataResp.class);
        List<ActiveDataResp> result = activeDatas.getMappedResults();
        return result;
    }

    /**
     *@description 获取App用户激活总数
     *@author wucheng
     *@params [tenantId]
     *@create 2019/1/8 14:18
     *@return java.lang.Long
     */
    public Long getUserRegisterTotal(Long tenantId) {
        Long totalUser = userApi.getAppUserCount(tenantId);
        return totalUser;
    }

    /**
     *@description 获取用户今日、昨日、一周活跃数量
     *@author wucheng
     *@params [tenantId]
     *@create 2019/1/7 20:01
     *@return com.iot.report.dto.resp.ActivatedDetailDataResp
     */
    @Override
    public UserActivatedOrActivateResp getUserActiveDetail(ActivateBaseReq req) {
        if (req.getTenantId() == null) {
            throw new BusinessException(ReportExceptionEnum.TENANTID_IS_NULL, "tenantId is null");
        }
        // 获取今日用户活跃数量（现在获取的是昨天的）
        Date lastToday1 = CalendarUtil.getDateBeforeOrAfter(req.getBeginDate(), -1);
        ActivateBaseReq todUserActivateReq = new ActivateBaseReq(lastToday1, req.getBeginDate() , req.getTenantId());
        List<ActiveDataResp> odUserActivateList = getUserActiveByDate(todUserActivateReq);
        Long todUserActivated = 0L;
        if (odUserActivateList != null && odUserActivateList.size() != 0) {
            for (ActiveDataResp w: odUserActivateList) {
                todUserActivated += w.getTotalNumber();
            }
        }

        // 获取昨日的用户活跃数量 (现在获取的是前天的)
        Date lastToday2 = CalendarUtil.getDateBeforeOrAfter(req.getBeginDate(), -2);
        ActivateBaseReq yesUserActiveReq = new ActivateBaseReq(lastToday1, lastToday2 , req.getTenantId());
        List<ActiveDataResp> yesUserActivedList = getUserActiveByDate(yesUserActiveReq);
        Long yesUserActivated = 0L;
        for (ActiveDataResp w: yesUserActivedList) {
            yesUserActivated += w.getTotalNumber();
        }

        // 获取本周活跃总数
        Date lastToday7 = CalendarUtil.getDateBeforeOrAfter(req.getBeginDate(), -7);
        ActivateBaseReq lastWeekUserActiveReq = new ActivateBaseReq(lastToday7, req.getBeginDate() , req.getTenantId());
        List<ActiveDataResp> lastWeekUserActiveList = getUserActiveByDate(lastWeekUserActiveReq);
        Long lastWeekUserActivated = 0L;
        for (ActiveDataResp w:lastWeekUserActiveList) {
            lastWeekUserActivated += w.getTotalNumber();
        }

        // 获取最近7日的前7日用户活跃总数
        Date lastToday14 = CalendarUtil.getDateBeforeOrAfter(lastToday7, -7);
        ActivateBaseReq beforeLastWeekUserActiveEq = new ActivateBaseReq(lastToday14, lastToday7, req.getTenantId());
        List<ActiveDataResp> beforeLastWeekUserActiveList = getUserActiveByDate(beforeLastWeekUserActiveEq);
        Long beforeLastWeekUserActivated = 0L;
        for (ActiveDataResp w:beforeLastWeekUserActiveList) {
            beforeLastWeekUserActivated += w.getTotalNumber();
        }
        UserActivatedOrActivateResp result = new UserActivatedOrActivateResp(yesUserActivated, todUserActivated, lastWeekUserActivated, beforeLastWeekUserActivated);
        return result;
    }

    /**
     *@description 获取用户今日、昨日、一周激活数量、总数
     *@author wucheng
     *@params [tenantId]
     *@create 2019/1/7 20:01
     *@return com.iot.report.dto.resp.ActivatedDetailDataResp
     */
    @Override
    public UserActivatedOrActivateResp getUserRegisterDetail(ActivateBaseReq req) {
        if (req.getTenantId() == null) {
            throw new BusinessException(ReportExceptionEnum.TENANTID_IS_NULL, "tenantId is null");
        }
        // 获取今日用户激活数量（现在获取的是昨天的）
        Date lastToday1 = CalendarUtil.getDateBeforeOrAfter(req.getBeginDate(), -1);
        ActivateBaseReq todUserActivatedReq = new ActivateBaseReq(lastToday1, req.getBeginDate() , req.getTenantId());
        List<ActiveDataResp> odUserActivatedList = getUserRegisterByDate(todUserActivatedReq);
        Long todUserActivated = 0L;
        if (odUserActivatedList != null && odUserActivatedList.size() != 0) {
            for (ActiveDataResp w: odUserActivatedList) {
                todUserActivated += w.getTotalNumber();
            }
        }

        // 获取昨日激活的用户数量（现在获取的是前天的）
        Date lastToday2 = CalendarUtil.getDateBeforeOrAfter(req.getBeginDate(), -2);
        ActivateBaseReq yesUserActivatedReq = new ActivateBaseReq(lastToday1, lastToday2 , req.getTenantId());
        List<ActiveDataResp> yesUserActivatedList = getUserRegisterByDate(yesUserActivatedReq);
        Long yesUserActivated = 0L;
        for (ActiveDataResp w:yesUserActivatedList) {
            yesUserActivated += w.getTotalNumber();
        }

        // 获取本周激活总数
        Date lastToday7 = CalendarUtil.getDateBeforeOrAfter(req.getBeginDate(), -7);
        // 获取一周类激活的用户数量
        ActivateBaseReq lastWeekUserActivatedReq = new ActivateBaseReq(lastToday7, req.getBeginDate() , req.getTenantId());
        List<ActiveDataResp> lastWeekUserActivatedList = getUserRegisterByDate(lastWeekUserActivatedReq);
        Long lastWeekUserActivated = 0L;
        for (ActiveDataResp w:lastWeekUserActivatedList) {
            lastWeekUserActivated += w.getTotalNumber();
        }
        lastWeekUserActivated += todUserActivated; // 加上今天的激活数量

        // 获取最近7日的前7日用户激活总数
        Date lastToday14 = CalendarUtil.getDateBeforeOrAfter(lastToday7, -7);
        ActivateBaseReq beforeLastWeekUserActivatedReq = new ActivateBaseReq(lastToday14, lastToday7, req.getTenantId());
        List<ActiveDataResp> beforeLastWeekUserActivatedList = getUserRegisterByDate(beforeLastWeekUserActivatedReq);
        Long beforeLastWeekUserActivated = 0L;
        for (ActiveDataResp w:beforeLastWeekUserActivatedList) {
            beforeLastWeekUserActivated += w.getTotalNumber();
        }
        UserActivatedOrActivateResp result = new UserActivatedOrActivateResp(yesUserActivated, todUserActivated, lastWeekUserActivated, beforeLastWeekUserActivated);
        return result;
    }

    /**
     *@description 描述：获取用户激活与活跃数据
     *@author wucheng
     *@params [req]
     *@create 2019/1/8 19:12
     *@return com.iot.report.dto.resp.UserActiveAndActivatedResp
     */
    @Override
    public UserActiveAndActivatedResp getUserActiveAndActivated(ActivateBaseReq req) {
        // 用户激活
        UserActivatedOrActivateResp userActivatedResp = getUserRegisterDetail(req);
        // 用户活跃
        UserActivatedOrActivateResp userActivateResp = getUserActiveDetail(req);
        // 组合结果集
        UserActiveAndActivatedResp result = new UserActiveAndActivatedResp(userActivateResp.getYesUserActivated(), userActivatedResp.getYesUserActivated(),
                userActivateResp.getTodUserActivated(), userActivatedResp.getTodUserActivated(),
                userActivateResp.getLastWeekUserActivated(), userActivatedResp.getLastWeekUserActivated(),
                userActivateResp.getBeforeLastWeekUserActivated(), userActivatedResp.getBeforeLastWeekUserActivated());

        return result;
    }
    /**
     *@description 获取设备和用户 今日和昨日 激活、活跃数量
     *@author wucheng
     *@params [req]
     *@create 2019/1/9 14:11
     *@return com.iot.report.dto.resp.UserDevActiveAndActivatedResp
     */
    @Override
    public UserDevActiveAndActivatedResp getUserDevActiveAndActivated(ActivateBaseReq req) {
        if (req.getTenantId() == null) {
            throw new BusinessException(ReportExceptionEnum.TENANTID_IS_NULL, "tenantId is null");
        }
        // 获取查询时间
        Date lastToday1 = CalendarUtil.getDateBeforeOrAfter(req.getBeginDate(), -1);
        Date lastToday2 = CalendarUtil.getDateBeforeOrAfter(req.getBeginDate(), -2);

        // 获取用户今日激活总数（实际是获取昨天的）
        ActivateBaseReq todUserActivatedReq = new ActivateBaseReq(lastToday1, req.getBeginDate() , req.getTenantId());
        List<ActiveDataResp> odUserActivatedList = getUserRegisterByDate(todUserActivatedReq);
        Long todUserActivated = 0L;
        if (odUserActivatedList != null && odUserActivatedList.size() != 0) {
            for (ActiveDataResp w: odUserActivatedList) {
                todUserActivated += w.getTotalNumber();
            }
        }

        // 获取昨日激活的用户数量（现在获取的是前天的）
        ActivateBaseReq yesUserActivatedReq = new ActivateBaseReq(lastToday1, lastToday2 , req.getTenantId());
        List<ActiveDataResp> yesUserActivatedList = getUserRegisterByDate(yesUserActivatedReq);
        Long yesUserActivated = 0L;
        for (ActiveDataResp w:yesUserActivatedList) {
            yesUserActivated += w.getTotalNumber();
        }

        // 获取今日用户活跃数量（现在获取的是昨天的）
        ActivateBaseReq todUserActivateReq = new ActivateBaseReq(lastToday1, req.getBeginDate() , req.getTenantId());
        List<ActiveDataResp> odUserActivateList = getUserActiveByDate(todUserActivateReq);
        Long todUserActivate = 0L;
        if (odUserActivateList != null && odUserActivateList.size() != 0) {
            for (ActiveDataResp w: odUserActivateList) {
                todUserActivate += w.getTotalNumber();
            }
        }

        // 获取昨日的用户活跃数量 (现在获取的是前天的)
        ActivateBaseReq yesUserActiveReq = new ActivateBaseReq(lastToday1, lastToday2 , req.getTenantId());
        List<ActiveDataResp> yesUserActivedList = getUserActiveByDate(yesUserActiveReq);
        Long yesUserActivate = 0L;
        for (ActiveDataResp w: yesUserActivedList) {
            yesUserActivate += w.getTotalNumber();
        }

        // 获取今日设备活跃数量（实际获取的是昨天的）
        ActivateBaseReq todDevActiveReq = new ActivateBaseReq(lastToday1, req.getBeginDate() , req.getTenantId());
        List<DailyActiveDeviceResp> todDevActiveList = deviceReportService.getDailyActiveDevice(todDevActiveReq);
        Long todDevcieActivate = 0L;
        if (todDevActiveList != null && todDevActiveList.size() > 0) {
            for (DailyActiveDeviceResp w: todDevActiveList) {
                todDevcieActivate += w.getTotals();
            }
        }

        // 获取昨日的设备活跃数量 （实际获取的是前天的）
        ActivateBaseReq yesDevActiveReq = new ActivateBaseReq(lastToday1, lastToday2 , req.getTenantId());
        List<DailyActiveDeviceResp> yesDevActiveList = deviceReportService.getDailyActiveDevice(yesDevActiveReq);
        Long yesDevcieActivate = 0L;
        if (yesDevActiveList != null && yesDevActiveList.size() > 0) {
            for (DailyActiveDeviceResp w: yesDevActiveList) {
                yesDevcieActivate += w.getTotals();
            }
        }
        // 获取今日激活的设备数量 (实际获取的是昨天的)
        ActivateBaseReq todDevActivatedReq = new ActivateBaseReq(lastToday1, req.getBeginDate() , req.getTenantId());
        List<DailyActivateDeviceResp>  todDevActivatedList = deviceReportService.getDailyActivatedDevice(todDevActivatedReq);
        Long todDevcieActivated = 0L;
        if (todDevActivatedList != null && todDevActivatedList.size() > 0) {
            for (DailyActivateDeviceResp w:todDevActivatedList) {
                todDevcieActivated += w.getTotals();
            }
        }
        // 获取昨日激活的设备数量 (实际获取的是前天的)
        ActivateBaseReq yesDevActivatedReq = new ActivateBaseReq(lastToday1, lastToday2 , req.getTenantId());
        List<DailyActivateDeviceResp>  yesDevActivatedList = deviceReportService.getDailyActivatedDevice(yesDevActivatedReq);
        Long yesDevcieActivated = 0L;
        if (yesDevActivatedList != null && yesDevActivatedList.size() > 0) {
            for (DailyActivateDeviceResp w:yesDevActivatedList) {
                yesDevcieActivated += w.getTotals();
            }
        }

        // 定义返回结果
        UserDevActiveAndActivatedResp result = new UserDevActiveAndActivatedResp(todDevcieActivate, todDevcieActivated, yesDevcieActivate, yesDevcieActivated,
                                                    todUserActivate, todUserActivated, yesUserActivate, yesUserActivated );
        return result;
    }

    /**
     *
     * 描述：整理用户活跃信息
     * @author 李帅
     * @created 2019年1月3日 下午8:04:51
     * @since
     */
    @Override
    public void arrangeUserData() {
//    	Set<String> value = new HashSet<String>();
//    	value.add("b93d67d46de44415b8f02863d8eba671");
//    	RedisCacheUtil.setAdd(ReportConstants.USERACTIVE + CalendarUtil.getYesterdayByCalendar(CalendarUtil.YYYYMMDD), value, false);
        Set<String> userUUIDSet = RedisCacheUtil.setGetAll(ReportConstants.USERACTIVE + CalendarUtil.getYesterdayByCalendar(CalendarUtil.YYYYMMDD), String.class, false);
        if(userUUIDSet == null || userUUIDSet.size() == 0){
            throw new BusinessException(ReportExceptionEnum.DEVICE_ACTIVE_CACHE_IS_NOT_EXIST);
        }
//    	org.springframework.data.mongodb.core.query.Query query = org.springframework.data.mongodb.core.query.Query.query(Criteria.where("dataStatus").is(1));
//        List<UserActiveInfo> users = this.mongoTemplate.find(query, UserActiveInfo.class, collectionName);
//    	if(userUUIDSet.size() > 999){
//    		List<String> sourList = new ArrayList<String>(userUUIDSet);
//        	List<List<String>>  userUUIDListList = dealBySubList(sourList, 900);
//        	for(List<String> userUUIDList : userUUIDListList){
//        		userApi.getByUserIds(userUUIDList);
//        	}
//    	}else{
//    		
//    	}
//    	List<UserActiveInfo> users = new ArrayList<UserActiveInfo>();
        UserActiveInfo userActiveInfo = null;
        for(String uuid : userUUIDSet){
            FetchUserResp user = userApi.getUserByUuid(uuid);
            userActiveInfo = new UserActiveInfo();
            userActiveInfo.setDataDate(new Date());
            userActiveInfo.setTenantId(user.getTenantId());
            userActiveInfo.setUuid(uuid);
            this.mongoTemplate.insert(userActiveInfo);
            RedisCacheUtil.setRemove(ReportConstants.USERACTIVE + CalendarUtil.getYesterdayByCalendar(CalendarUtil.YYYYMMDD), uuid, false);
//    		users.add(userActiveInfo);
        }

    }

//    /**
//	 *
//	 * 描述：通过list的     subList(int fromIndex, int toIndex)方法实现
//	 * @author 李帅
//	 * @created 2017年11月2日 上午10:44:35
//	 * @since
//	 * @param sourList
//	 * @param batchCount
//	 */
//	public List<List<String>> dealBySubList(List<String> sourList, int batchCount) {
//		int sourListSize = sourList.size();
//		int subCount = sourListSize % batchCount == 0 ? sourListSize / batchCount : sourListSize / batchCount + 1;
//		int startIndext = 0;
//		int stopIndext = 0;
//		int endIndext = sourListSize % batchCount == 0 ? batchCount : sourListSize % batchCount;
//		List<List<String>> tempListList = new ArrayList<List<String>>();
//		List<String> tempList = null;
//		for (int i = 0; i < subCount; i++) {
//			stopIndext = (i == subCount - 1) ? stopIndext + endIndext : sto                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                pIndext + batchCount;
//			tempList = new ArrayList<String>(sourList.subList(startIndext, stopIndext));
//			tempListList.add(tempList);
//			startIndext = stopIndext;
//		}
//		return tempListList;
//	}
}
