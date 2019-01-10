package com.iot.tenant.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.iot.tenant.domain.AppInfo;
import com.iot.tenant.vo.req.review.AppReviewSearchReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * App应用 Mapper 接口
 * </p>
 *
 * @author laiguiming
 * @since 2018-07-09
 */
@Mapper
public interface AppInfoMapper extends BaseMapper<AppInfo> {

    /**
     * 描述：依据appId查询APP信息
     *
     * @param appId appId
     * @return java.lang.Byte
     * @author maochengyuan
     * @created 2018/10/24 11:05
     */
    @Select("select audit_status as auditStatus, tenant_id as tenantId from app_info where id = #{appId}")
    AppInfo getAppAuditStatus(@Param("appId") Long appId);

    /**
     * 描述：依据appId更新APP审核状态
     *
     * @param appId       appId
     * @param auditStatus 审核状态
     * @return void
     * @author maochengyuan
     * @created 2018/10/24 11:05
     */
    @Update("update app_info set audit_status = #{auditStatus}, apply_audit_time = now() where id = #{appId}")
    int setAppAuditStatus(@Param("appId") Long appId, @Param("auditStatus") Byte auditStatus);

    /**
     * 描述：查询APP信息
     *
     * @return java.lang.Byte
     * @author maochengyuan
     * @created 2018/10/24 11:05
     * @update wucheng
     * @update date 2018-11-19
     * @update description: 新增按用户id查询
     */
    @Select("<script>" +
            "select a.id, a.app_name as appName, a.tenant_id as tenantId, audit_status as auditStatus, apply_audit_time as applyAuditTime, " +
            " create_by as createBy from app_info a " +
            " where 1=1 " +
            " <if test=\"req.createBy != null\">" +
            "  and a.create_by = #{req.createBy} " +
            " </if>" +
            " <if test=\"req.auditStatus == 0\">" +
            "  and a.audit_status = 0 " +
            " </if>" +
            " <if test=\"req.auditStatus == 2\">" +
            " and a.audit_status in (1,2) " +
            " </if>" +
            " <if test=\"req.tenantName!=null and req.tenantName!=''\">" +
            " and tenant_id in (select id from tenant where name like concat('%',#{req.tenantName},'%')) " +
            " </if>" +
            " <if test=\"req.appName!=null and req.appName!=''\">" +
            "  and app_name like concat('%',#{req.appName},'%')" +
            " </if>" +
            " order by apply_audit_time desc" +
            "</script>")
    List<AppInfo> getAppInfoList(Page page, @Param("req") AppReviewSearchReq req);

    /**
     * @return
     * @despriction：通过打包状态获取appId
     * @author yeshiyuan
     * @created 2018/11/14 16:42
     */
    @Select("select id from app_info where status = #{packStatus}")
    List<Long> getAppIdByPackStatus(@Param("packStatus") Integer packStatus);

    @Update("update app_info set audit_status = null, apply_audit_time = null where id = #{appId}")
    int updateAuditStatusToNull(@Param("appId") Long appId);
}
