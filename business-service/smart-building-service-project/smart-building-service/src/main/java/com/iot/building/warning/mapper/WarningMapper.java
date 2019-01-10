package com.iot.building.warning.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.building.warning.domain.Warning;
import com.iot.building.warning.vo.WarningReq;
import com.iot.building.warning.vo.WarningResp;

public interface WarningMapper extends BaseMapper<Warning> {
	
	/**
     * 描述：插入告警数据
     *
     * @param warning
     * @throws Exception
     * @author zhouzongwei
     * @created 2017年11月30日 下午2:51:45
     * @since
     */
    @Insert({"insert into warning(id, device_id, content, create_time,type,`status`,tenant_id,location_id,space_name,`use`,event_name,event_type,org_id) " +
            "		values " +
            "		(#{id}, #{deviceId},#{content},#{createTime},#{type},#{status},#{tenantId},#{locationId},#{spaceName},#{use},#{eventName},#{eventType},#{orgId}) "
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertWarning(WarningReq warning) throws Exception;

    /**
     * 描述：获取历史告警数据
     *
     * @return
     * @throws Exception
     * @author zhouzongwei
     * @created 2017年11月30日 下午3:14:00
     * @since
     */
    @Select("<script>"+
            "select                                                "+
            "d.id as id,						       "+
            "d.device_id as deviceId,						       "+
            "d.content as content,					       "+
            "d.create_time as createTime,						       "+
            "d.type as type,					       "+
            "d.`status` as status,					       "+
            "d.space_name as spaceName,					       "+
            "d.`use` as purpose,					       "+
            "d.event_name as eventName,					       "+
            "d.event_type as eventType,					       "+
            "d.tenant_id as tenantId,					       "+
            "d.org_id as orgId,					       "+
            "d.location_id as locationId					       "+
            "from warning d 					       "+
            "WHERE 1=1      "+
            "<if test=\"eventType != null and eventType !=''\">	       "+
           /* "and d.event_type like CONCAT(CONCAT('%', #{content}), '%')   "+*/
            "and d.event_type = #{eventType}   "+
            "</if>		"+
            "<if test=\"timeType !=null and timeType !=''\">	       "+
               " and DATE_SUB(CURDATE(), INTERVAL #{timeType} DAY) &lt;= date(d.create_time) " +
            "</if>						       "+
            "<if test=\"tenantId !=null\">	       "+
            " and tenant_id=#{tenantId} " +
            "</if>						       "+
            "<if test=\"orgId !=null \">	       "+
            " and org_id=#{orgId} " +
            "</if>						       "+
            "<if test=\"locationId !=null \">	       "+
            " and location_id=#{locationId} " +
            "</if>						       "+
           /* "<if test=\"timeType2 !=null and timeType2 !=''\">	       "+
            " and DATE_SUB(CURDATE(), INTERVAL 7 DAY) &lt;= date(d.create_time) " +
            "</if>						       "+
            "<if test=\"timeType3 !=null and timeType3 !=''\">	       "+
            " and DATE_SUB(CURDATE(), INTERVAL 30 DAY) &lt;= date(d.create_time) " +
            "</if>						       "+*/
//            "<if test=\"timeType =='30'\">	       "+
//                "and DATE_SUB(CURDATE(), INTERVAL 30 DAY) &lt;= date(d.create_time)   "+
//            "</if>						       "+
//            "<if test=\"timeType =='1'\">	       "+
//                "and DATE_SUB(CURDATE(), INTERVAL 1 DAY) &lt;= date(d.create_time)   "+
//            "</if>						       "+
            /*"<if test=\"timeType != null and timeType =='30'\">      "+
                "and DATE_SUB(CURDATE(), INTERVAL 30 DAY) <= date(d.create_time)   "+
            "</if>						       "+*/
            "order by d.create_time	desc			       "+
            "</script>				       "
    )
    List<WarningResp> findHistoryWarningList(WarningReq warningReq) throws Exception;


    @Select("<script>"+
            "select                                                "+
            "d.id as id,						       "+
            "d.device_id as deviceId,						       "+
            "d.content as content,					       "+
            "d.create_time as createTime,						       "+
            "d.type as type,					       "+
            "d.`status` as status,					       "+
            "d.space_name as spaceName,					       "+
            "d.`use` as purpose,					       "+
            "d.event_name as eventName,					       "+
            "d.event_type as eventType					       "+
            "from warning d 					       "+
            "WHERE 1=1      "+
            "<if test=\"eventType != null and eventType !=''\">	       "+
            "and d.event_type = #{eventType}   "+
            "</if>		"+
            "<if test=\"id != null and id !=''\">	       "+
            "and d.id = #{id}   "+
            "</if>		"+
            "<if test=\"deviceId != null and deviceId !=''\">	       "+
            "and d.device_id = #{deviceId}   "+
            "</if>		"+
            "<if test=\"type != null and type !=''\">	       "+
            "and d.type = #{type}   "+
            "</if>		"+
            "<if test=\"tenantId != null and tenantId !=''\">	       "+
            "and d.tenant_id = #{tenantId}   "+
            "</if>		"+
            "<if test=\"status != null and status !=''\">	       "+
            "and d.status = #{status}   "+
            "</if>		"+
            "<if test=\"locationId != null and locationId !=''\">	       "+
            "and d.location_id = #{locationId}   "+
            "</if>		"+
            "<if test=\"spaceName != null and spaceName !=''\">	       "+
            "and d.space_name = #{spaceName}   "+
            "</if>		"+
            "<if test=\"eventName != null and eventName !=''\">	       "+
            "and d.event_name = #{eventName}   "+
            "</if>		"+
            "<if test=\"tenantId !=null\">	       "+
            " and tenant_id=#{tenantId} " +
            "</if>						       "+
            "<if test=\"orgId !=null \">	       "+
            " and org_id=#{orgId} " +
            "</if>						       "+
            "<if test=\"locationId !=null \">	       "+
            " and location_id=#{locationId} " +
            "</if>						       "+
            "order by d.create_time	desc			       "+
            "<if test=\"count != null and count !=''\">	       "+
            " limit 100   "+
            "</if>		"+
            "</script>				       "
    )
    List<WarningResp> findHistoryWarningListNoPage(WarningReq warningReq) throws Exception;

    /**
     * 描述：获取未读告警数据
     *
     * @return
     * @throws Exception
     * @since
     */
    @Select({"select id,content " +
            "		from warning where status = '0' and tenant_id=#{tenantId} and org_id=#{orgId} and location_id=#{locationId} ORDER BY create_time desc"})
    List<WarningResp> findUnreadWarningList(@Param("tenantId")Long tenantId,@Param("orgId")Long orgId,@Param("locationId")Long locationId) throws Exception;

    /**
     * 描述：更新告警消息状态
     *
     * @return
     * @throws Exception
     * @since
     */
    @Update({"update warning " +
            "		set status = #{status} " +
            "		where id = #{id}"})
    int updateWarningStatus(WarningReq warningReq) throws Exception;

    /**
     * 描述：检查id是否已存在
     *
     * @return
     * @throws Exception
     * @since
     */
    @Select({"select count(*) from warning " +
            "		where id=#{id}"})
    int countWarningById(Long id) throws Exception;


    @Select("<script>"+
            "select                                                "+
            "d.id as id,						       "+
            "d.device_id as deviceId,						       "+
            "d.content as content,					       "+
            "d.create_time as createTime,						       "+
            "d.type as type,					       "+
            "d.`status` as status,					       "+
            "d.space_name as spaceName,					       "+
            "d.`use` as purpose,					       "+
            "d.event_name as eventName,					       "+
            "d.event_type as eventType					       "+
            "from warning d 					       "+
            "WHERE 1=1      "+
            "<if test=\"eventType != null and eventType !=''\">	       "+
            "and d.event_type = #{eventType}   "+
            "</if>		"+
            "<if test=\"id != null and id !=''\">	       "+
            "and d.id = #{id}   "+
            "</if>		"+
            "<if test=\"deviceId != null and deviceId !=''\">	       "+
            "and d.device_id = #{deviceId}   "+
            "</if>		"+
            "<if test=\"type != null and type !=''\">	       "+
            "and d.type = #{type}   "+
            "</if>		"+
            "<if test=\"tenantId != null and tenantId !=''\">	       "+
            "and d.tenant_id = #{tenantId}   "+
            "</if>		"+
            "<if test=\"status != null and status !=''\">	       "+
            "and d.status = #{status}   "+
            "</if>		"+
            "<if test=\"locationId != null and locationId !=''\">	       "+
            "and d.location_id = #{locationId}   "+
            "</if>		"+
            "<if test=\"orgId != null and orgId !=''\">	       "+
            "and d.org_id = #{orgId}   "+
            "</if>		"+
            "<if test=\"spaceName != null and spaceName !=''\">	       "+
            "and d.space_name = #{spaceName}   "+
            "</if>		"+
            "<if test=\"eventName != null and eventName !=''\">	       "+
            "and d.event_name = #{eventName}   "+
            "</if>		"+
            "order by d.create_time	desc			       "+
            "</script>				       "
    )
    List<WarningResp> findWarningListByCondition(WarningReq warningReq);
}
