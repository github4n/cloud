package com.iot.tenant.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.common.util.CommonUtil;
import com.iot.tenant.domain.AppReviewRecord;
import com.iot.tenant.mapper.AppReviewRecordMapper;
import com.iot.tenant.service.IAppReviewRecordService;
import com.iot.tenant.vo.req.review.AppReviewRecordReq;
import com.iot.tenant.vo.resp.review.AppReviewRecordResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：租户
 * 功能描述：APP审核记录接口实现
 * 创建人： maochengyuan
 * 创建时间：2018/10/23 14:48
 * 修改人： maochengyuan
 * 修改时间：2018/10/23 14:48
 * 修改描述：
 */
@Service
public class AppReviewRecordServiceImpl extends ServiceImpl<AppReviewRecordMapper,AppReviewRecord> implements IAppReviewRecordService {

    @Autowired
    private AppReviewRecordMapper appReviewRecordMapper;

    /** 
     * 描述：添加app审核记录
     * @author maochengyuan
     * @created 2018/10/23 14:56
     * @param req app审核记录
     * @return void
     */
    @Override
    public Long addAppReviewRecord(AppReviewRecordReq req) {
        this.appReviewRecordMapper.addAppReviewRecord(req);
        return req.getId();
    }

    /** 
     * 描述：依据appId获取审核记录
     * @author maochengyuan
     * @created 2018/10/23 14:56
     * @param appId appId
     * @return java.util.List<com.iot.tenant.vo.resp.review.AppReviewRecordResp>
     */
    @Override
    public List<AppReviewRecordResp> getAppReviewRecord(Long appId) {
        return this.appReviewRecordMapper.getAppReviewRecord(appId);
    }

    /**
     * 描述：查询申请记录
     * @author maochengyuan
     * @created 2018/10/25 10:51
     * @param appIds appId集合
     * @return java.util.List<com.iot.tenant.vo.resp.review.AppReviewRecordResp>
     */
    @Override
    public Map<Long, AppReviewRecordResp> getApplyRecords(Collection<Long> appIds){
        List<AppReviewRecordResp> list = this.appReviewRecordMapper.getApplyRecords(appIds);
        if(CommonUtil.isEmpty(list)){
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(AppReviewRecordResp::getAppId, o->o));
    }

    /**
     * 描述：查询处理人
     * @author maochengyuan
     * @created 2018/10/25 10:51
     * @param appIds appId集合
     * @return java.util.List<com.iot.tenant.vo.resp.review.AppReviewRecordResp>
     */
    @Override
    public Map<Long, AppReviewRecordResp> getAuditRecords(Collection<Long> appIds){
        List<AppReviewRecordResp> list = this.appReviewRecordMapper.getAuditRecords(appIds);
        if(CommonUtil.isEmpty(list)){
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(AppReviewRecordResp::getAppId, o->o));
    }

    /**
     * @despriction：获取租户id
     * @author  yeshiyuan
     * @created 2018/11/2 13:52
     * @return
     */
    @Override
    public Long getTenantIdById(Long id) {
        return appReviewRecordMapper.getTenantById(id);
    }


    @Override
    public void invalidRecord(Long appId, Long tenantId) {
        appReviewRecordMapper.invalidRecord(appId, tenantId);
    }
}
