package com.iot.tenant.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.tenant.domain.AppReviewRecord;
import com.iot.tenant.vo.req.review.AppReviewRecordReq;
import com.iot.tenant.vo.resp.review.AppReviewRecordResp;
import org.apache.ibatis.annotations.*;

import java.util.Collection;
import java.util.List;

/**
 * 项目名称：云平台
 * 功能描述：APP审核记录
 * 创建人： maochengyuan
 * 创建时间：2018/10/23 16:54
 * 修改人： maochengyuan
 * 修改时间：2018/10/23 16:54
 * 修改描述：
 */
@Mapper
public interface AppReviewRecordMapper extends BaseMapper<AppReviewRecord>{

    /** 
     * 描述：添加app审核记录
     * @author maochengyuan
     * @created 2018/10/23 14:58
     * @param req
     * @return void
     */
    @Insert(" insert into" +
            " app_review_record" +
            " (tenant_id, app_id, operate_time, process_status, operate_desc, create_by, create_time, update_by, update_time)" +
            " values (#{tenantId}, #{appId}, #{operateTime}, #{processStatus}, #{operateDesc}, #{createBy}, #{createTime}, #{updateBy}, now())")
    @Options(useGeneratedKeys=true,keyProperty = "id", keyColumn="id")
    void addAppReviewRecord(AppReviewRecordReq req);

    /** 
     * 描述：依据appId获取审核记录
     * @author maochengyuan
     * @created 2018/10/23 14:58
     * @param appId
     * @return java.util.List<com.iot.tenant.vo.resp.review.AppReviewRecordResp>
     */
    @Select(" select" +
            " id," +
            " create_by as createBy," +
            " create_time as createTime," +
            " process_status as processStatus," +
            " operate_desc as operateDesc," +
            " operate_time as operateTime" +
            " from" +
            " app_review_record" +
            " where app_id = #{appId} " +
            " and is_deleted = 'valid' " +
            " order by operateTime desc")
    List<AppReviewRecordResp> getAppReviewRecord(@Param("appId") Long appId);

    /** 
     * 描述：查询申请记录
     * @author maochengyuan
     * @created 2018/10/25 10:51
     * @param appIds appId集合
     * @return java.util.List<com.iot.tenant.vo.resp.review.AppReviewRecordResp>
     */
    @Select("<script>" +
            " select app_id, min(r.create_time) as createTime, r.create_by as createBy" +
            " from app_review_record r" +
            " where r.process_status = 0" +
            " and app_id in <foreach collection='appIds' open='(' close=')' item='id' separator=','>#{id,jdbcType=BIGINT}</foreach>" +
            " and is_deleted = 'valid' " +
            " group by app_id" +
            "</script>")
    List<AppReviewRecordResp> getApplyRecords(@Param("appIds") Collection<Long> appIds);

    /** 
     * 描述：查询处理人
     * @author maochengyuan
     * @created 2018/10/25 10:51
     * @param appIds appId集合
     * @return java.util.List<com.iot.tenant.vo.resp.review.AppReviewRecordResp>
     */
    @Select("<script>" +
            " select app_id, max(r.create_time) as createTime, create_by as createBy" +
            " from app_review_record r" +
            " where r.process_status in (1, 2)" +
            " and app_id in <foreach collection='appIds' open='(' close=')' item='id' separator=','>#{id,jdbcType=BIGINT}</foreach>" +
            " and is_deleted = 'valid' " +
            " group by app_id" +
            "</script>")
    List<AppReviewRecordResp> getAuditRecords(@Param("appIds") Collection<Long> appIds);

    @Select("select tenant_id from app_review_record where id = #{id}")
    Long getTenantById(@Param("id") Long id);

    /**
     * @despriction：记录置为失效
     * @author  yeshiyuan
     * @created 2018/11/14 13:46
     * @return
     */
    @Update("update app_review_record set is_deleted = 'invalid', update_time = now() where app_id = #{appId} and tenant_id = #{tenantId}")
    void invalidRecord(@Param("appId") Long appId, @Param("tenantId") Long tenantId);
}
