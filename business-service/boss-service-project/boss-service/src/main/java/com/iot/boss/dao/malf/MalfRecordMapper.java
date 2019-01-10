package com.iot.boss.dao.malf;

import com.iot.boss.dto.*;
import com.iot.boss.entity.MalfRecord;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：用户报障管理
 * 创建人： yeshiyuan
 * 创建时间：2018/5/15 15:44
 * 修改人： yeshiyuan
 * 修改时间：2018/5/15 15:44
 * 修改描述：
 */
@Mapper
public interface MalfRecordMapper {

    /**
      * @despriction：确认修复
      * @author  yeshiyuan
      * @created 2018/5/15 16:01
      * @param malfId 报障单id
      * @param handleStatus 处理状态
      * @return
      */
    @Update("UPDATE malf_record SET handle_status=#{handleStatus},renovate_time=#{renovatTime} where id= #{malfId} ")
    public int confirmFixProblem(@Param("malfId") Long malfId, @Param("handleStatus") Integer handleStatus, @Param("renovatTime")Date renovatTime);
    /**
     * 描述：提交报账单
     * @author 490485964@qq.com
     * @date 2018/5/16 11:30
     * @param
     * @return
     */
    @Insert("insert into malf_record ("
//    		+ "id,"
    		+ " user_id, tenant_id, malf_desc,"
            + "malf_status, malf_rank, create_time, confirm_time, is_allocated, handle_status,"
            + "pre_handle_admin_id, cur_handle_admin_id, handle_start_time, handle_end_time"
            + ", renovate_time, is_rollback)"
            + "values ("
//            + "#{malfRecord.id,jdbcType=BIGINT},"
            + " #{malfRecord.userId,jdbcType=VARCHAR}, " +
            "#{malfRecord.tenantId,jdbcType=BIGINT}, "
            + "#{malfRecord.malfDesc,jdbcType=VARCHAR}, #{malfRecord.malfStatus,jdbcType=INTEGER},"
            + "#{malfRecord.malfRank,jdbcType=INTEGER}, now(),"
            + "#{malfRecord.confirmTime,jdbcType=DATE}, #{malfRecord.isAllocated,jdbcType=INTEGER},"
            + "#{malfRecord.handleStatus,jdbcType=INTEGER}, #{malfRecord.preHandleAdminId,jdbcType=BIGINT},"
            + "#{malfRecord.curHandleAdminId,jdbcType=BIGINT}, #{malfRecord.handleStartTime,jdbcType=DATE},"
            + "#{malfRecord.handleEndTime,jdbcType=DATE}, #{malfRecord.renovateTime,jdbcType=DATE},"
            + "#{malfRecord.isRollback,jdbcType=INTEGER})")
    int submitMalf(@Param("malfRecord") MalfRecord malfRecord);

    /**
     * 描述：提交报障处理
     * @author 490485964@qq.com
     * @date 2018/5/16 11:31
     * @param
     * @return
     */
    @Update("UPDATE malf_record SET " +
            " malf_rank=#{malfRecord.malfRank,jdbcType=INTEGER}," +
            " confirm_time = now()," +
            " handle_status =  #{malfRecord.handleStatus,jdbcType=INTEGER}," +
            " pre_handle_admin_id =  #{malfRecord.preHandleAdminId,jdbcType=BIGINT}," +
            " cur_handle_admin_id = #{malfRecord.curHandleAdminId,jdbcType=BIGINT}," +
            " handle_start_time = now()" +
            " WHERE id = #{malfRecord.id,jdbcType=BIGINT}")
    int adminSubmitHandleMalf(@Param("malfRecord") MalfRecord malfRecord);

    /**
      * @despriction：校验当前处理人、报账单
      * @author  yeshiyuan
      * @created 2018/5/15 20:18
      * @param null
      * @return
      */
    @Select("select count(1) from malf_record where id=#{malfId} and cur_handle_admin_id=#{curHandleAdminId} and handle_status=#{handleStatus}")
    public int checkCurDealAdmin(@Param("malfId") Long malfId,@Param("curHandleAdminId") Long curHandleAdminId,@Param("handleStatus") Integer handleStatus);

    /**
     * 描述：运维人员提交报障处理结果
     * @author mao2080@sina.com
     * @created 2018/5/15 20:02
     * @param malfRecord 报障单
     * @return void
     */
    @Update("update malf_record m set m.pre_handle_admin_id = #{malfRecord.preHandleAdminId,jdbcType=BIGINT}," +
            " m.handle_status = #{malfRecord.handleStatus,jdbcType=INTEGER}," +
            " m.cur_handle_admin_id = #{malfRecord.curHandleAdminId,jdbcType=BIGINT}," +
            " m.handle_end_time = #{malfRecord.handleEndTime}" +
            " where m.id = #{malfRecord.id,jdbcType=BIGINT}")
    void mainteProcessMalf(@Param("malfRecord") MalfRecord malfRecord);

    /**
     * 描述：超级管理人员退回报障处理
     * @author mao2080@sina.com
     * @created 2018/5/15 20:02
     * @param malfRecord 报障单
     * @return void
     */
    @Update("update malf_record m set m.pre_handle_admin_id = #{malfRecord.preHandleAdminId,jdbcType=BIGINT}," +
            " m.handle_status = #{malfRecord.handleStatus,jdbcType=INTEGER}," +
            " m.cur_handle_admin_id = #{malfRecord.curHandleAdminId,jdbcType=BIGINT}," +
            " m.handle_end_time = null" +
            " where m.id = #{malfRecord.id,jdbcType=BIGINT}")
    void returnMalfToMainte(@Param("malfRecord") MalfRecord malfRecord);

    /**
     * 描述：依据报障单id获取报障单信息
     * @author mao2080@sina.com
     * @created 2018/5/16 10:03
     * @param malfId 报障单id
     * @return com.iot.boss.entity.MalfRecord
     */
    @Select("select id,user_id as userId," +
            "tenant_id as tenantId," +
            "malf_desc as malfDesc," +
            "malf_status as malfStatus," +
            "malf_rank as malfRank," +
            "create_time as createTime," +
            "confirm_time as confirmTime," +
            "is_allocated as isAllocated," +
            "handle_status as handleStatus," +
            "pre_handle_admin_id as preHandleAdminId," +
            "cur_handle_admin_id as curHandleAdminId," +
            "handle_start_time as handleStartTime," +
            "handle_end_time as handleEndTime," +
            "renovate_time as renovateTime," +
            "is_rollback as isRollback from malf_record where id=#{malfId}")
    MalfRecord getMalfRecordById(@Param("malfId") Long malfId);

    /**
      * @despriction：超级管理员确认是问题
      * @author  yeshiyuan
      * @created 2018/5/16 10:28
      * @param malfRecord 报障单信息
      * @return
      */
    @Update("UPDATE malf_record SET is_rollback=#{malfRecord.isRollback,jdbcType=TINYINT}," +
            "cur_handle_admin_id=pre_handle_admin_id," +
            "pre_handle_admin_id=#{malfRecord.preHandleAdminId,jdbcType=BIGINT}," +
            "handle_status=#{malfRecord.handleStatus,jdbcType=TINYINT}" +
            " where id=#{malfRecord.id,jdbcType=BIGINT}")
    int confirmIsProblem(@Param("malfRecord") MalfRecord malfRecord);
    
    /**
     * 
     * 描述：自动分配给管理员后更新报障记录
     * @author 李帅
     * @created 2018年5月16日 下午2:07:29
     * @since 
     * @param curHandleAdminId
     * @param malfRecordId
     * @param isAllocated
     */
    @Update("update malf_record m set m.cur_handle_admin_id = #{curHandleAdminId,jdbcType=BIGINT}, " +
            "m.is_allocated = #{isAllocated,jdbcType=TINYINT} " +
            "where m.id = #{malfRecordId,jdbcType=BIGINT}")
    void updateMalfRecordStatus(@Param("curHandleAdminId") Long curHandleAdminId, @Param("malfRecordId") Long malfRecordId, @Param("isAllocated") int isAllocated);


    @Select({ "<script>",
            "select t1.id as malfId, t2.user_name as userName, t1.malf_status as malfStatus, t1.handle_status as handleStatus, " +
                    " t1.malf_rank as malfRank, t1.tenant_id as tenantId, " +
             "	 <if test=\"search.dateType == 0\"> DATE_FORMAT(t1.create_time, '%Y-%m-%d %H:%i:%s') as dateTime </if>" ,
            "    <if test=\"search.dateType == 1\"> DATE_FORMAT(t1.confirm_time, '%Y-%m-%d %H:%i:%s') as dateTime </if>" ,
            "    <if test=\"search.dateType == 2\"> DATE_FORMAT(t1.handle_start_time, '%Y-%m-%d %H:%i:%s') as dateTime </if>" ,
            "    <if test=\"search.dateType == 3\"> DATE_FORMAT(t1.renovate_time, '%Y-%m-%d %H:%i:%s') as dateTime </if>" ,
            " from malf_record t1 left join iot_db_user.user t2 on (t1.user_id = t2.uuid) where " +
            "	 <if test=\"search.dateType == 0 \"> DATE_FORMAT(t1.create_time, '%Y-%m-%d') >= #{search.dateStart,jdbcType=DATE} and DATE_FORMAT(t1.create_time, '%Y-%m-%d')  <![CDATA[<=]]> #{search.dateEnd,jdbcType=DATE} </if>" ,
            "	 <if test=\"search.dateType == 1 \"> DATE_FORMAT(t1.confirm_time, '%Y-%m-%d') >= #{search.dateStart,jdbcType=DATE} and DATE_FORMAT(t1.confirm_time, '%Y-%m-%d') <![CDATA[<=]]> #{search.dateEnd,jdbcType=DATE} </if>" ,
            "	 <if test=\"search.dateType == 2 \"> DATE_FORMAT(t1.handle_start_time, '%Y-%m-%d') >= #{search.dateStart,jdbcType=DATE} and DATE_FORMAT(t1.handle_start_time, '%Y-%m-%d') <![CDATA[<=]]> #{search.dateEnd,jdbcType=DATE} </if>" ,
            "	 <if test=\"search.dateType == 3 \"> DATE_FORMAT(t1.renovate_time, '%Y-%m-%d') >= #{search.dateStart,jdbcType=DATE} and DATE_FORMAT(t1.renovate_time, '%Y-%m-%d') <![CDATA[<=]]> #{search.dateEnd,jdbcType=DATE} </if>" ,
            "	 <if test=\"search.handleStatus != 3\"> and t1.handle_status=#{search.handleStatus,jdbcType=INTEGER}</if>" ,
            "	 <if test=\"search.handleStatus == 3\"> and t1.handle_status!=#{search.handleStatus,jdbcType=INTEGER}</if>" ,
            "	 <if test=\"search.malfStatus != -1\"> and t1.malf_status=#{search.malfStatus,jdbcType=INTEGER}</if>" ,
            "	 <if test=\"search.malfRank != -1\"> and t1.malf_rank=#{search.malfRank,jdbcType=INTEGER}</if>" ,
            "	 <if test=\"search.malfId != -1\"> and t1.id=#{search.malfId,jdbcType=BIGINT}</if>" ,
            "	 <if test=\"search.userId != null\"> and t1.user_id=#{search.userId,jdbcType=VARCHAR}</if>",
            "</script>" })
    List<UserMalfDto> getUserMalfList(@Param("search")UserMalfListSearch search);

    @Select("select id as id, admin_name as adminName, email as email, nick_name as nickName, phone as phone, " +
            " admin_no as adminNo, admin_desc as adminDesc, create_time as createTime, " +
            " cancel_time as cancelTime, admin_state as adminState, admin_type as adminType from system_user" +
            " where admin_type=#{type,jdbcType=INTEGER} and (admin_state=2 or admin_state=3 or admin_state=4)")
    public List<MalfSysUserDto> getAdminSelectList(@Param("type") int type);

    @Select("select t1.handle_time as handleTime, t1.handle_msg as handleMsg, " +
            " t2.admin_name as userName " +
            " from malf_proces_log t1 left join system_user t2 on(t1.handle_admin_id=t2.id)" +
            " where malf_id=#{id,jdbcType=BIGINT}")
    public List<UserMalfProcessInfoDto> getUserMalfProcessDetails(@Param("id") long id);

    @Select({ "<script>",
            "select t1.id as malfId, t2.uuid as userName, t1.malf_status as malfStatus, t1.handle_status as handleStatus, t1.malf_rank as" +
                    " malfRank, t1.create_time as createTime, t1.confirm_time as confirmTime, t1.tenant_id as tenantId, t1.renovate_time as renovateTime " +
                    " from malf_record t1 left join iot_db_user.user t2 on(t1.user_id = t2.uuid) where handle_status = 3",
            "	 <if test=\"search.malfStatus != -1\"> and t1.handle_status=#{search.malfStatus,jdbcType=INTEGER}</if>" ,
            "	 <if test=\"search.malfRank != -1\"> and t1.malf_rank=#{search.malfRank,jdbcType=INTEGER}</if>" ,
            "	 <if test=\"search.malfId != -1\"> and t1.id=#{search.malfId,jdbcType=BIGINT}</if>" ,
            "	 <if test=\"search.userId != null\"> and t1.user_id=#{search.userId,jdbcType=VARCHAR}</if>" ,
            "	 <if test=\"search.dateType == 0 \"> and DATE_FORMAT(t1.create_time, '%Y-%m-%d')=DATE_FORMAT(#{search.searchDate,jdbcType=DATE}, '%Y-%m-%d')</if>" ,
            "	 <if test=\"search.dateType == 1 \"> and DATE_FORMAT(t1.confirm_time, '%Y-%m-%d')=DATE_FORMAT(#{search.searchDate,jdbcType=DATE}, '%Y-%m-%d')</if>" ,
            "	 <if test=\"search.dateType == 2 \"> and DATE_FORMAT(t1.handle_start_time, '%Y-%m-%d')=DATE_FORMAT(#{search.searchDate,jdbcType=DATE}, '%Y-%m-%d')</if>" ,
            "	 <if test=\"search.dateType == 3 \"> and DATE_FORMAT(t1.renovate_time, '%Y-%m-%d')=DATE_FORMAT(#{search.searchDate,jdbcType=DATE}, '%Y-%m-%d')</if>" ,
            "</script>" })
    public List<UserMalfHistoryDto> getUserMalfHistoryList(@Param("search") UserMalfHistorySearch search);
    
    /**
     * 
     * 描述：获取未分配报障单信息
     * @author 李帅
     * @created 2018年5月17日 下午5:25:57
     * @since 
     * @return
     */
    @Select("select id,user_id as userId," +
            "tenant_id as tenantId," +
            "malf_desc as malfDesc," +
            "malf_status as malfStatus," +
            "malf_rank as malfRank," +
            "create_time as createTime," +
            "confirm_time as confirmTime," +
            "is_allocated as isAllocated," +
            "handle_status as handleStatus," +
            "pre_handle_admin_id as preHandleAdminId," +
            "cur_handle_admin_id as curHandleAdminId," +
            "handle_start_time as handleStartTime," +
            "handle_end_time as handleEndTime," +
            "renovate_time as renovateTime," +
            "is_rollback as isRollback from malf_record where is_allocated != 1")
    List<MalfRecord> getUnassignedMalfRecord();
    
    /**
     * 
     * 描述：获取未定级报障单信息
     * @author 李帅
     * @created 2018年5月17日 下午5:26:43
     * @since 
     * @return
     */
    @Select("select id,user_id as userId," +
            "tenant_id as tenantId," +
            "malf_desc as malfDesc," +
            "malf_status as malfStatus," +
            "malf_rank as malfRank," +
            "create_time as createTime," +
            "confirm_time as confirmTime," +
            "is_allocated as isAllocated," +
            "handle_status as handleStatus," +
            "pre_handle_admin_id as preHandleAdminId," +
            "cur_handle_admin_id as curHandleAdminId," +
            "handle_start_time as handleStartTime," +
            "handle_end_time as handleEndTime," +
            "renovate_time as renovateTime," +
            "is_rollback as isRollback from malf_record where handle_status = 0 and malf_status != 3")
    List<MalfRecord> getUnclassifiedMalfRecord();
    
    /**
     * 
     * 描述：获取处理中报障单信息
     * @author 李帅
     * @created 2018年5月17日 下午5:27:20
     * @since 
     * @return
     */
    @Select("select id,user_id as userId," +
            "tenant_id as tenantId," +
            "malf_desc as malfDesc," +
            "malf_status as malfStatus," +
            "malf_rank as malfRank," +
            "create_time as createTime," +
            "confirm_time as confirmTime," +
            "is_allocated as isAllocated," +
            "handle_status as handleStatus," +
            "pre_handle_admin_id as preHandleAdminId," +
            "cur_handle_admin_id as curHandleAdminId," +
            "handle_start_time as handleStartTime," +
            "handle_end_time as handleEndTime," +
            "renovate_time as renovateTime," +
            "is_rollback as isRollback from malf_record where handle_status = 1")
    List<MalfRecord> getProcessingMalfRecord();
    
    /**
     * 
     * 描述：更新报障记录报障处理进度
     * @author 李帅
     * @created 2018年5月17日 下午6:18:05
     * @since 
     * @param malfRecordId
     * @param malfStatus
     */
    @Update("update malf_record m set m.malf_status = #{malfStatus,jdbcType=INTEGER}" +
            " where m.id = #{malfRecordId,jdbcType=BIGINT}")
    void updateRecordMalfStatus(@Param("malfRecordId") Long malfRecordId, @Param("malfStatus") int malfStatus);
}
