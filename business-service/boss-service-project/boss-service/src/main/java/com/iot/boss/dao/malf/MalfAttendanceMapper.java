package com.iot.boss.dao.malf;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.iot.boss.entity.MalfAttendanceTimer;

/**
 * 项目名称：cloud
 * 模块名称：
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/5/15 14:07
 * 修改人： yeshiyuan
 * 修改时间：2018/5/15 14:07
 * 修改描述：
 */
@Mapper
public interface MalfAttendanceMapper {

	
	/**
	 * 
	 * 描述：查询管理员值班
	 * @author 李帅
	 * @created 2018年5月15日 下午8:20:43
	 * @since 
	 * @param executionCycle
	 * @return
	 */
    @Select("SELECT " + 
    		"	t1.id AS id, " + 
    		"	t1.timer_name AS timerName, " + 
    		"	t1.execution_cycle AS executionCycle, " + 
    		"	t1.start_time AS startTime, " + 
    		"	t1.end_time AS endTime, " + 
    		"	t1.admin_id AS adminId, " + 
    		"	t1.timer_status AS timerStatus, " + 
    		"	t2.admin_name AS adminName " + 
    		"FROM " + 
    		"	malf_attendance_timer t1, " + 
    		"	system_user t2 " + 
    		"WHERE " + 
    		"	t1.admin_id = t2.id " + 
    		"AND t1.timer_status = '0' " + 
    		"AND execution_cycle =#{executionCycle}")
    public List<MalfAttendanceTimer> getMalfAttendance(@Param("executionCycle") int executionCycle);
    
    /**
	 * 
	 * 描述：查询管理员值班
	 * @author 李帅
	 * @created 2018年5月15日 下午8:20:43
	 * @since 
	 * @param executionCycle
	 * @return
	 */
    @Select("SELECT " + 
    		"	t1.id AS id, " + 
    		"	t1.timer_name AS timerName, " + 
    		"	t1.execution_cycle AS executionCycle, " + 
    		"	t1.start_time AS startTime, " + 
    		"	t1.end_time AS endTime, " + 
    		"	t1.admin_id AS adminId, " + 
    		"	t1.timer_status AS timerStatus, " + 
    		"	t2.admin_name AS adminName " + 
    		"FROM " + 
    		"	malf_attendance_timer t1, " + 
    		"	system_user t2 " + 
    		"WHERE " + 
    		"	t1.admin_id = t2.id " + 
    		"AND t1.timer_status = '0' " + 
    		"AND t1.id =#{malfId}")
    public MalfAttendanceTimer getMalfAttendanceById(@Param("malfId") Long malfId);
    
	/**
	 * 
	 * 描述：添加管理员值班
	 * @author 李帅
	 * @created 2018年5月15日 下午6:28:55
	 * @since 
	 * @param malfAttendanceTimer
	 */
    @Insert("insert into malf_attendance_timer ("
//    		+ "id,"
    		+ "timer_name,"
    		+ "execution_cycle,"
    		+ "start_time,"
    		+ "end_time,"
    		+ "admin_id,"
    		+ "timer_status"
    		+ ") values ("
//			+ "#{malfAttendanceTimer.id,jdbcType=BIGINT},"
			+ "#{malfAttendanceTimer.timerName,jdbcType=VARCHAR},"
			+ "#{malfAttendanceTimer.executionCycle,jdbcType=INTEGER}, "
			+ "#{malfAttendanceTimer.startTime,jdbcType=VARCHAR}, "
			+ "#{malfAttendanceTimer.endTime,jdbcType=VARCHAR},"
			+ "#{malfAttendanceTimer.adminId,jdbcType=BIGINT}, "
			+ "#{malfAttendanceTimer.timerStatus,jdbcType=INTEGER}"
			+ ")")
    public void addMalfAttendance(@Param("malfAttendanceTimer")MalfAttendanceTimer malfAttendanceTimer);
    
    /**
     * 
     * 描述：更新管理员值班
     * @author 李帅
     * @created 2018年5月15日 下午8:22:36
     * @since 
     * @param malfAttendanceParam
     */
    @Update({ "<script>", 
			"update malf_attendance_timer" + 
			"	set " + 
			"	 <if test=\"timerName != null\"> timer_name=#{timerName,jdbcType=VARCHAR},</if>" +
			"	 <if test=\"executionCycle != null\"> execution_cycle=#{executionCycle,jdbcType=INTEGER},</if>" + 
			"	 <if test=\"startTime != null\"> start_time=#{startTime,jdbcType=VARCHAR},</if>" + 
			"	 <if test=\"endTime != null\"> end_time=#{endTime,jdbcType=VARCHAR},</if>" + 
			"	 <if test=\"adminId != null\"> admin_id=#{adminId,jdbcType=BIGINT},</if>" + 
			"    id = #{id,jdbcType=BIGINT}" +
			"   where id = #{id,jdbcType=BIGINT} and timer_status = 0 ",
			"</script>" })
    public void updateMalfAttendance(MalfAttendanceTimer malfAttendanceTimer);
    
    /**
     * 
     * 描述：删除管理员值班
     * @author 李帅
     * @created 2018年5月15日 下午6:38:35
     * @since 
     * @param malfAttendanceId
     */
    @Update("update malf_attendance_timer set timer_status = '1' where id = #{malfAttendanceId}")
    public void deleteMalfAttendance(@Param("malfAttendanceId") Long malfAttendanceId);
}
