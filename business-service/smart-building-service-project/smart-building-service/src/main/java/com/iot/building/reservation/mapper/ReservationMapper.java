package com.iot.building.reservation.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.iot.building.reservation.vo.ReservationReq;
import com.iot.building.reservation.vo.ReservationResp;

public interface ReservationMapper {
	
	 @Select(
                "select count(id)                     " +
                "from reservation   				  " +
                "where  						      " +
                "space_id=#{spaceId}		          " +
                "and ((start_time between #{startTime} and #{endTime} or end_time between #{startTime} and #{endTime} )" +
                "  or (start_time < #{startTime} and end_time >= #{endTime} ))  " 
	    )
	public int countReservByStartAndEndTime(ReservationReq reservationReq);
	
	 @Select(
			 "select count(id)                     " +
					 "from reservation   				  " +
					 "where  						      " +
					 "space_id=#{spaceId}		          " +
					 "and ((start_time between #{startTime} and #{endTime} or end_time between #{startTime} and #{endTime} )" +
					 "  or (start_time < #{startTime} and end_time >= #{endTime} ))  "
					 + "and id !=#{id}" 
			 )
	 public int countReservByStartAndEndTimeOutId(ReservationReq reservationReq);
	 
	 @Select(
			 "select                               " +
					 "id,							      " +
					 "start_time as startTime ,		      " +
					 "end_time as endTime ,		          " +
					 "space_id as spaceId,				  " +
					 "open_id as openId ,				  " +
					 "name,							      " +
					 "phone,							      " +
					 "flag,			                      " +
					 "type                                 " +
					 "from reservation   				  " +
					 "where  						      " +
					 "space_id=#{spaceId}		          " +
					 "and start_time > #{currentDate}	  " +
					 "order by start_time asc		  " 
			 )
	 public List<ReservationResp> getNextReservation(@Param("spaceId") Long spaceId, @Param("currentDate") Long currentDate);
	 
    @Select(
                    "select                               " +
                    "id,							      " +
                    "start_time as startTime ,		      " +
                    "end_time as endTime ,		          " +
                    "space_id as spaceId,				  " +
                    "open_id as openId ,				  " +
                    "name,							      " +
                    "phone,							      " +
                    "flag,			                      " +
                    "type,                                 " +
                    "model,                                 " +
                    "position                                 " +
                    "from reservation   				  " +
                    "where  						      " +
                    "space_id=#{spaceId}		          " +
                    "and start_time < #{currentDate}	  " +
                    "and end_time >= #{currentDate}		  " 
    )
    public ReservationResp getCurrentReservation(@Param("spaceId") Long spaceId, @Param("currentDate") Long currentDate);
    
    @Select(
    		" select                               " +
    				"id,							      " +
    				"start_time as startTime ,		      " +
    				"end_time as endTime ,		          " +
    				"space_id as spaceId,				  " +
    				"open_id as openId ,				  " +
    				"name,							      " +
    				"phone,							      " +
    				"flag,			                      " +
    				"type,                                " +
    				"model,                               " +
    				"position                             " +
    				"from reservation   				  " +
    				"where   space_id=#{spaceId}    and ( " +
    				"(start_time <= #{currentDate}	      " +
    				"and end_time >= #{currentDate}	) 	  " + 
    				"OR	                                  " +
    				"(start_time <= #{nextTenTime}		  " +
    				"and end_time >= #{nextTenTime}	)	) " 
    		)
    public ReservationResp getStartReservation(@Param("spaceId") Long spaceId, @Param("currentDate") Long currentDate, @Param("nextTenTime") Long nextTenTime);
    
    @Select(
    		"<script>                                     " +
    				"select                               " +
    				"id,							      " +
    				"start_time as startTime ,		      " +
    				"end_time as endTime ,		          " +
    				"space_id as spaceId,				  " +
    				"open_id as openId ,				  " +
    				"name,							      " +
    				"phone,							      " +
    				"flag,			                      " +
    				"type,                                 " +
    				"model,                                 " +
    				"org_id,                                 " +
    				"position                                 " +
    				"from reservation   				  " +
    				"where 1=1 						      " +
    				"<if test=\"id != null\">			  " +
    				"and id=#{id}				          " +
    				"</if>							      " +
    				"<if test=\"startTime != null\">	  " +
    				"and start_time &gt;= #{startTime}		  " +
    				"</if>							      " +
    				"<if test=\"endTime != null\">		  " +
    				"and end_time &lt;= #{endTime}			  " +
    				"</if>							      " +
    				"<if test=\"openId != null\">		  " +
    				"and open_id=#{openId}			  " +
    				"</if>							      " +
    				"<if test=\"spaceId != null\">		  " +
    				"and space_id=#{spaceId}		      " +
    				"</if>							      " +
    				"<if test=\"name != null\">		  " +
    				"and name=#{name}		      " +
    				"</if>							      " +
    				"<if test=\"tenantId != null\">	  " +
    				"and tenant_id=#{tenantId}			  " +
    				"</if>							      " +
    				"<if test=\"orgId != null\">	  " +
    				"and org_id=#{orgId}			  " +
    				"</if>							      " +
    				"<if test=\"phone != null\">	  " +
    				"and phone=#{phone}		  " +
    				"</if>							      " +
    				"<if test=\"flag != null\">	  " +
    				"and flag=#{flag}	  " +
    				"</if>							      " +
    				"<if test=\"type != null\">	  " +
    				"and type=#{type}	  " +
    				"</if>							      " +
    				"</script>"
    		)
    public List<ReservationResp> findResercationByCondition(ReservationReq reservationReq);
    
    @Select(
    		"<script>                                     " +
    				"select                               " +
    				"id,							      " +
    				"start_time as startTime ,		      " +
    				"end_time as endTime ,		          " +
    				"space_id as spaceId,				  " +
    				"open_id as openId ,				  " +
    				"name,							      " +
    				"phone,							      " +
    				"flag,			                      " +
    				"type,                                " +
    				"model,                               " +
    				"position                             " +
    				"from reservation   				  " +
    				"where  						      " +
    				"open_id=#{openId}	     		  " +
//    				"and space_id=#{spaceId}	     		  " +
    				"order by id desc	limit 1	      " +
    				"</script>"
    		)
    public ReservationResp findNearResercationByOpenId(ReservationReq reservationReq);
    
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table space_device
     *
     * @mbggenerated
     */
    @Insert({
                    "insert into reservation (" ,
                    "				create_time, " ,
                    "				space_id, " ,
                    "				tenant_id, " ,
                    "				name, " ,
                    "				phone, " ,
                    "				open_id, " ,
                    "				flag, " ,
                    "				start_time, " ,
                    "				end_time, " ,
                    "				type,model,position,org_id) values(" ,
                    "				#{creatTime}, " ,
                    "				#{spaceId}, " ,
                    "				#{tenantId}, " ,
                    "				#{name}, " ,
                    "				#{phone}, " ,
                    "				#{openId}, " ,
                    "				#{flag}, " ,
                    "				#{startTime}, " ,
                    "				#{endTime}, " ,
                    "				#{type},#{model},#{position},#{orgId})" 
    })
    int save(ReservationReq reservationReq);


    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table space_device
     *
     * @mbggenerated
     */
    @Update({
            "<script> " +
                    "update reservation " +
                    "		<set> " +
                    "			<if test=\"creatTime != null\"> " +
                    "				create_time = #{creatTime}, " +
                    "			</if> " +
                    "			<if test=\"name != null\"> " +
                    "				name = #{name}, " +
                    "			</if> " +
                    "			<if test=\"phone != null\"> " +
                    "				phone = #{phone}, " +
                    "			</if> " +
                    "			<if test=\"spaceId != null\"> " +
                    "				space_id = #{spaceId}, " +
                    "			</if> " +
                    "			<if test=\"tenantId != null\"> " +
                    "				tenant_id = #{tenantId}, " +
                    "			</if> " +
                    "			<if test=\"flag != null\"> " +
                    "				flag = #{flag}, " +
                    "			</if> " +
                    "			<if test=\"openId != null\"> " +
                    "				open_id = #{openId}, " +
                    "			</if> " +
                    "			<if test=\"startTime != null\"> " +
                    "				start_time = #{startTime}, " +
                    "			</if> " +
                    "			<if test=\"endTime != null\"> " +
                    "				end_time = #{endTime}, " +
                    "			</if> " +
                    "			<if test=\"type != null\"> " +
                    "				type = #{type}, " +
                    "			</if> " +
                    "			<if test=\"model != null\"> " +
                    "				model = #{model}, " +
                    "			</if> " +
                    "			<if test=\"position != null\"> " +
                    "				position = #{position}, " +
                    "			</if> " +
                    "		</set> " +
                    "		where id = #{id}" +
                    "</script>                                                        "
    })
    int update(ReservationReq reservationReq);

    /**
     * 删除关系
     *
     * @mbggenerated
     */
    @Delete({
            "delete from " +
                    "		reservation " +
                    "		where id = #{id}  "
    })
    int delete(Long id);
}