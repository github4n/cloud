package com.iot.tenant.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.tenant.domain.AppReviewRecord;
import com.iot.tenant.vo.req.review.AppReviewRecordReq;
import com.iot.tenant.vo.resp.review.AppReviewRecordResp;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：租户
 * 功能描述：APP审核记录接口定义
 * 创建人： maochengyuan
 * 创建时间：2018/10/23 14:48
 * 修改人： maochengyuan
 * 修改时间：2018/10/23 14:48
 * 修改描述：
 */
public interface IAppReviewRecordService extends IService<AppReviewRecord> {

    /** 
     * 描述：添加app审核记录
     * @author maochengyuan
     * @created 2018/10/23 14:59
     * @param req 审核对象
     * @return void
     */
    Long addAppReviewRecord(AppReviewRecordReq req);

    /** 
     * 描述：依据appId获取审核记录
     * @author maochengyuan
     * @created 2018/10/23 15:00
     * @param appId appId
     * @return java.util.List<com.iot.tenant.vo.resp.review.AppReviewRecordResp>
     */
    List<AppReviewRecordResp> getAppReviewRecord(Long appId);

    /**
     * 描述：查询申请记录
     * @author maochengyuan
     * @created 2018/10/25 10:51
     * @param appIds appId集合
     * @return java.util.List<com.iot.tenant.vo.resp.review.AppReviewRecordResp>
     */
    Map<Long, AppReviewRecordResp> getApplyRecords(Collection<Long> appIds);

    /**
     * 描述：查询处理人
     * @author maochengyuan
     * @created 2018/10/25 10:51
     * @param appIds appId集合
     * @return java.util.List<com.iot.tenant.vo.resp.review.AppReviewRecordResp>
     */
    Map<Long, AppReviewRecordResp> getAuditRecords(Collection<Long> appIds);

    /**
     * @despriction：获取租户id
     * @author  yeshiyuan
     * @created 2018/11/2 13:52
     * @return
     */
    Long getTenantIdById(Long id);

    /**
     * @despriction：记录置为失效
     * @author  yeshiyuan
     * @created 2018/11/14 13:46
     * @return
     */
    void invalidRecord(Long appId, Long tenantId);

}
