package com.iot.message.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.iot.message.entity.AppCertInfo;
import com.iot.message.entity.SystemPushControl;
import com.iot.message.entity.UserPhoneRelate;

public interface SystemMapper {

	/**
	 * 
	 * 描述：新增用户和手机端关系
	 * @author 李帅
	 * @created 2018年3月12日 下午3:15:39
	 * @since 
	 * @param userPhoneRelate
	 * @return
	 * @throws Exception
	 */
	@Insert({ "<script>", 
		"insert into user_phone_relate " + 
		"    <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >" + 
//		"      <if test=\"id != null\" > id, </if>" + 
		"      <if test=\"userId != null\" > user_id, </if>" + 
		"      <if test=\"phoneId != null\" > phone_id, </if>" + 
		"      <if test=\"phoneType != null\" > phone_type, </if>" + 
		"      <if test=\"appId != null\" > app_id, </if>" + 
		" create_time," +
		"      <if test=\"creator != null\" > creator, </if>" +
		"      <if test=\"dataStatus != null\" > data_status, </if>" +
		"      <if test=\"tenantId != null\" > tenant_id, </if>" +
		"    </trim>" +
		"    <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >" +
//		"      <if test=\"id != null\" > #{id,jdbcType=BIGINT}, </if>" + 
		"      <if test=\"userId != null\" > #{userId,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"phoneId != null\" > #{phoneId,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"phoneType != null\" > #{phoneType,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"appId != null\" > #{appId,jdbcType=BIGINT}, </if>" + 
		" now(), " +
		"      <if test=\"creator != null\" > #{creator,jdbcType=VARCHAR}, </if>" +
		"      <if test=\"dataStatus != null\" > #{dataStatus,jdbcType=VARCHAR}, </if>" +
		"      <if test=\"tenantId != null\" > #{tenantId,jdbcType=VARCHAR}, </if>" +
		"    </trim>",
		"</script>" })
	void addUserPhoneRelate(UserPhoneRelate userPhoneRelate);
	
	/**
	 * 
	 * 描述：更新用户和手机端关系
	 * @author 李帅
	 * @created 2018年3月12日 下午3:15:39
	 * @since 
	 * @param userPhoneRelate
	 * @return
	 * @throws Exception
	 */
	@Update({ "<script>", 
		"update user_phone_relate" + 
		"			set " + 
		"			<if test=\"userId != null\"> user_id=#{userId,jdbcType=VARCHAR},</if>" +
		"			<if test=\"phoneId != null\"> phone_id=#{phoneId,jdbcType=VARCHAR},</if>" + 
		"			<if test=\"phoneType != null\"> phone_type=#{phoneType,jdbcType=VARCHAR},</if>" + 
		"			<if test=\"appId != null\"> app_id=#{appId,jdbcType=VARCHAR},</if>" + 
		"			<if test=\"creator != null\"> creator=#{creator,jdbcType=VARCHAR},</if>" + 
		"           update_time = now()" +
		"		where user_id = #{userId,jdbcType=VARCHAR}",
		"</script>" })
	void updateUserPhoneRelate(UserPhoneRelate userPhoneRelate);
	
	/**
	 * 
	 * 描述：查询用户和手机端关系
	 * @author 李帅
	 * @created 2018年3月17日 下午2:27:39
	 * @since 
	 * @param userIds
	 * @return
	 * @throws Exception
	 */
	@Select({ "<script>", 
		"select  id      AS id," + 
		"			user_id        AS userId," + 
		"			phone_id       AS phoneId," + 
		"			app_id       AS appId," + 
		"			phone_type     AS phoneType," + 
		"			creator        AS creator," + 
		"			create_time    AS createTime," +
		"			update_time    AS updateTime" +
		"    from user_phone_relate" + 
		"    where 1=1" + 
		"    <if test=\"userIds != null\"> and user_id in" + 
		"       <foreach collection=\"userIds\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\">" + 
		"         #{item}" + 
		"       </foreach>" + 
		"    </if>", 
		"</script>" })
	List<UserPhoneRelate>  getUserPhoneRelates(@Param("userIds")List<String> userIds);

	/**
	 * @despriction：删除用户和手机端关系
	 * @author  yeshiyuan
	 * @created 2018/5/29 11:50
	 * @return
	 */
	@Delete("delete from user_phone_relate where user_id = #{userUuid}")
	int deleteUserPhoneRelate(@Param("userUuid") String userUuid);
	
	/**
	 * 
	 * 描述：删除用户手机ID
	 * @author 李帅
	 * @created 2018年12月5日 下午5:44:24
	 * @since 
	 * @param userUuid
	 * @return
	 */
	@Update({ "<script>", 
		"update user_phone_relate" + 
		"			set " + 
		"			phone_id = null," + 
		"			phone_type = null," + 
		"           update_time = now()" +
		"		where user_id = #{userUuid,jdbcType=VARCHAR}",
		"</script>" })
	int deleteUserPhoneId(@Param("userUuid") String userUuid);
	
	/**
	 * 
	 * 描述：查询APP证书信息
	 * @author 李帅
	 * @created 2018年7月24日 下午7:03:32
	 * @since 
	 * @param appId
	 * @return
	 */
	@Select({ "<script>", 
		"SELECT " + 
		"	id AS id, " + 
		"	tenant_id AS tenantId, " + 
		"	app_id AS appId, " + 
		"	cert_info AS certInfo, " + 
		"	cert_pass_word AS certPassWord, " + 
		"	service_host AS serviceHost, " + 
		"	service_port AS servicePort, " + 
		"	android_push_key AS androidPushKey, " + 
		"	android_push_url AS androidPushUrl, " + 
		"	create_time AS createTime, " + 
		"	update_time AS updateTime " + 
		"FROM	app_cert_info " + 
		"WHERE	app_id = #{appId}", 
		"</script>" })
	AppCertInfo getAppCertInfo(@Param("appId") Long appId);
	
	/**
	 * 
	 * 描述：添加租户邮箱信息
	 * @author 李帅
	 * @created 2018年7月24日 下午8:13:43
	 * @since 
	 * @param appCertInfo
	 */
	@Insert({ "<script>", 
		"insert into app_cert_info " + 
		"    <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >" + 
//		"      <if test=\"id != null\" > id, </if>" + 
		"      <if test=\"tenantId != null\" > tenant_id, </if>" + 
		"      <if test=\"appId != null\" > app_id, </if>" + 
		"      <if test=\"certInfo != null\" > cert_info, </if>" + 
		" create_time," +
		"      <if test=\"certPassWord != null\" > cert_pass_word, </if>" +
		"      <if test=\"serviceHost != null\" > service_host, </if>" +
		"      <if test=\"servicePort != null\" > service_port, </if>" +
		"      <if test=\"androidPushKey != null\" > android_push_key, </if>" +
		"      <if test=\"androidPushUrl != null\" > android_push_url, </if>" +
		"    </trim>" +
		"    <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >" +
//		"      <if test=\"id != null\" > #{id,jdbcType=BIGINT}, </if>" + 
		"      <if test=\"tenantId != null\" > #{tenantId,jdbcType=BIGINT}, </if>" + 
		"      <if test=\"appId != null\" > #{appId,jdbcType=BIGINT}, </if>" + 
		"      <if test=\"certInfo != null\" > #{certInfo,jdbcType=VARCHAR}, </if>" + 
		" now(), " +
		"      <if test=\"certPassWord != null\" > #{certPassWord,jdbcType=VARCHAR}, </if>" +
		"      <if test=\"serviceHost != null\" > #{serviceHost,jdbcType=VARCHAR}, </if>" +
		"      <if test=\"servicePort != null\" > #{servicePort,jdbcType=VARCHAR}, </if>" +
		"      <if test=\"androidPushKey != null\" > #{androidPushKey,jdbcType=VARCHAR}, </if>" +
		"      <if test=\"androidPushUrl != null\" > #{androidPushUrl,jdbcType=VARCHAR}, </if>" +
		"    </trim>",
		"</script>" })
	void addAppCertInfo(AppCertInfo appCertInfo);
	
	/**
	 * 
	 * 描述：修改租户邮箱信息
	 * @author 李帅
	 * @created 2018年7月24日 下午8:13:52
	 * @since 
	 * @param appCertInfo
	 */
	@Update({ "<script>", 
		"update app_cert_info" + 
		"			set " + 
		"			<if test=\"tenantId != null\"> tenant_id=#{tenantId,jdbcType=BIGINT},</if>" +
		"			<if test=\"appId != null\"> app_id=#{appId,jdbcType=BIGINT},</if>" + 
		"			<if test=\"certInfo != null\"> cert_info=#{certInfo,jdbcType=VARCHAR},</if>" + 
		"			<if test=\"certPassWord != null\"> cert_pass_word=#{certPassWord,jdbcType=VARCHAR},</if>" + 
		"			<if test=\"serviceHost != null\"> service_host=#{serviceHost,jdbcType=VARCHAR},</if>" + 
		"			<if test=\"servicePort != null\"> service_port=#{servicePort,jdbcType=VARCHAR},</if>" + 
		"			<if test=\"androidPushKey != null\"> android_push_key=#{androidPushKey,jdbcType=VARCHAR},</if>" + 
		"			<if test=\"androidPushUrl != null\"> android_push_url=#{androidPushUrl,jdbcType=VARCHAR},</if>" + 
		"           update_time = now()" +
		"		where id = #{id,jdbcType=BIGINT}",
		"</script>" })
	void updateAppCertInfo(AppCertInfo appCertInfo);
	
	/**
	 * 
	 * 描述：查询系统推送控制
	 * @author 李帅
	 * @created 2018年11月16日 下午4:42:34
	 * @since 
	 * @param userId
	 * @param appId
	 * @return
	 */
	@Select({ "<script>", 
		"SELECT " + 
		"	id AS id, " + 
		"	app_id AS appId, " + 
		"	user_id AS userId, " + 
		"	switch AS switch, " + 
		"	create_by AS createBy, " + 
		"	create_time AS createTime, " + 
		"	update_by AS updateBy, " + 
		"	update_time AS updateTime, " + 
		"	is_deleted AS isDeleted, " + 
		"	tenant_id AS tenantId " + 
		"FROM " + 
		"	system_push_control " + 
		"WHERE " + 
		"	1 = 1 " + 
		"AND user_id = #{userId,jdbcType=VARCHAR} " + 
		"AND app_id = #{appId,jdbcType=BIGINT}", 
		"</script>" })
	SystemPushControl getSystemPushControl(@Param("userId") String userId, @Param("appId") Long appId);
	
	/**
	 * 
	 * 描述：添加系统推送控制
	 * @author 李帅
	 * @created 2018年11月16日 下午4:43:48
	 * @since 
	 * @param systemPushControl
	 */
	@Insert({ "<script>", 
		"insert into system_push_control " + 
		"    <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >" + 
		"      <if test=\"tenantId != null\" > tenant_id, </if>" + 
		"      <if test=\"appId != null\" > app_id, </if>" + 
		"      <if test=\"userId != null\" > user_id, </if>" + 
		"      <if test=\"createBy != null\" > create_by, </if>" + 
		"      <if test=\"switCh != null\" > switch, </if>" + 
		" create_time, " +
		" is_deleted,  " +
		"    </trim>" +
		"    <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >" +
		"      <if test=\"tenantId != null\" > #{tenantId,jdbcType=BIGINT}, </if>" + 
		"      <if test=\"appId != null\" > #{appId,jdbcType=BIGINT}, </if>" + 
		"      <if test=\"userId != null\" > #{userId,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"createBy != null\" > #{createBy,jdbcType=BIGINT}, </if>" + 
		"      <if test=\"switCh != null\" > #{switCh,jdbcType=VARCHAR}, </if>" + 
		" now(), " +
		" 'valid', " +
		"    </trim>",
		"</script>" })
	void addSystemPushControl(SystemPushControl systemPushControl);
	
	/**
	 * 
	 * 描述：修改系统推送控制
	 * @author 李帅
	 * @created 2018年11月16日 下午4:44:01
	 * @since 
	 * @param systemPushControl
	 */
	@Update({ "<script>", 
		"update system_push_control" + 
		"			set " + 
		"			<if test=\"tenantId != null\"> tenant_id=#{tenantId,jdbcType=BIGINT},</if>" +
		"			<if test=\"appId != null\"> app_id=#{appId,jdbcType=BIGINT},</if>" + 
		"			<if test=\"userId != null\"> user_id=#{userId,jdbcType=VARCHAR},</if>" + 
		"			<if test=\"updateBy != null\"> update_by=#{createBy,jdbcType=BIGINT},</if>" + 
		"			<if test=\"switCh != null\"> switch=#{switCh,jdbcType=VARCHAR},</if>" + 
		"           update_time = now()" +
		"		where id = #{id,jdbcType=BIGINT}",
		"</script>" })
	void updateSystemPushControl(SystemPushControl systemPushControl);
}