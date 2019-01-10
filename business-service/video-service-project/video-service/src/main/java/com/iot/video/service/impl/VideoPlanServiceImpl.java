package com.iot.video.service.impl;



import java.util.Calendar;
import java.util.List;

import com.iot.common.util.CalendarUtil;
import com.iot.redis.RedisCacheUtil;
import com.iot.video.contants.ModuleConstants;
import com.iot.video.dto.*;
import com.iot.video.entity.VideoPayRecord;
import com.iot.video.mongo.entity.VideoEventEntity;
import com.iot.video.mongo.vo.MongoPageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iot.common.util.StringUtil;
import com.iot.video.dao.VideoPlanMapper;
import com.iot.video.entity.VideoPlan;
import com.iot.video.service.VideoPlanService;
import com.iot.video.util.CommonUtil;
import com.iot.video.vo.EventVo;
import com.iot.video.vo.VideoEventVo;

@Service("videoPlanService")
public class VideoPlanServiceImpl implements VideoPlanService {


    @Autowired
    private VideoPlanMapper videoPlanMapper;

    /**
     * 描述：修改录影排序
     * @author mao2080@sina.com
     * @created 2018/3/23 14:38
     * @param poDto 排序对象
     * @return void
     */
    @Override
    public void updatePlanOrder(PlanOrderParamDto poDto){
        List<String> planIds = poDto.getPlanIds();
        List<String> orderIds = poDto.getOrderIds();
        if (null != planIds && null != orderIds && planIds.size() > 0 && orderIds.size() > 0 && planIds.size()==orderIds.size()){
            for (int i = 0 ; i < planIds.size() ; i++){
                this.videoPlanMapper.updatePlanOrder(orderIds.get(i),planIds.get(i));
            }
        }
    }

    /**
     * 
     * 描述：根据设备id查询计划类型
     * @author 李帅
     * @created 2018年4月27日 下午4:03:38
     * @since 
     * @param tenantId
     * @param userId
     * @param deviceId
     * @return com.lds.iot.video.dto.PlanInfoDto
     */
    @Override
    public PlanInfoDto getPlanType(Long tenantId, String userId, String deviceId){
        return this.videoPlanMapper.getPlanType(tenantId, userId, deviceId);
    }

    @Override
    public VideoPlan getVideoPlan(String planId, String userId) {
        return this.videoPlanMapper.getVideoPlan(planId,userId);
    }

    /**
     * 描述：查询套餐Id
     * @author nongchongwei
     * @date 2018/7/17 17:10
     * @param
     * @return
     */
    @Override
    public Long getPackageIdByPlanId(String planId) {
        return this.videoPlanMapper.getPackageId(planId);
    }



    @Override
    public VideoPayRecord getLastVideoPayRecord(String planId) {
        return this.videoPlanMapper.getLastVideoPayRecord(planId);
    }

    /**
     * @despriction：查找计划绑定的用户id
     * @author  yeshiyuan
     * @created 2018/8/13 17:05
     * @param null
     * @return
     */
    @Override
    public String getUserIdByPlanId(String planId) {
        String redisKey = ModuleConstants.VIDEO_PLAN_INFO + planId;
        String userId = RedisCacheUtil.hashGetString(redisKey, ModuleConstants.VIDEO_PLAN_INFO_KEY_USER_ID);
        if (StringUtil.isBlank(userId)){
            userId = this.videoPlanMapper.getUserIdByPlanId(planId);
            RedisCacheUtil.hashPut(redisKey, ModuleConstants.VIDEO_PLAN_INFO_KEY_USER_ID, userId, false);
        }
        return userId;
    }
}

