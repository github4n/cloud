package com.iot.message.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.iot.message.entity.PushNoticeLog;
import com.iot.message.entity.PushNoticeTemplate;
import com.iot.message.entity.TenantMailInfo;

public interface MessageMapper {

	/**
	 * 
	 * 描述：添加推送日志
	 * @author 李帅
	 * @created 2018年3月12日 下午5:36:48
	 * @since 
	 * @param pushNoticeLog
	 * @throws Exception
	 */
	@Insert({ "<script>", 
		"insert into push_notice_log " + 
		"    <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >" + 
//		"      <if test=\"id != null\" > id, </if>" + 
		"      <if test=\"pushFrom != null\" > push_from, </if>" + 
		"      <if test=\"pushTo != null\" > push_to, </if>" + 
		"      <if test=\"pushType != null\" > push_type, </if>" + 
		"      <if test=\"pushTime != null\" > push_time, </if>" + 
		"      <if test=\"templateId != null\" > template_id, </if>" + 
		"      <if test=\"noticeSubject != null\" > notice_subject, </if>" + 
		"      <if test=\"paramValue != null\" > param_value, </if>" + 
		"      <if test=\"resultCode != null\" > result_code, </if>" + 
		"      <if test=\"resultType != null\" > result_type, </if>" + 
		"      <if test=\"resultMessage != null\" > result_message, </if>" + 
		"      <if test=\"resultAnswerTime != null\" > result_answer_time, </if>" + 
		"      <if test=\"dataStatus != null\" > data_status, </if>" + 
		"    </trim>" + 
		"    <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >" + 
//		"      <if test=\"id != null\" > #{id,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"pushFrom != null\" > #{pushFrom,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"pushTo != null\" > #{pushTo,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"pushType != null\" > #{pushType,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"pushTime != null\" > #{pushTime,jdbcType=TIMESTAMP}, </if>" + 
		"      <if test=\"templateId != null\" > #{templateId,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"noticeSubject != null\" > #{noticeSubject,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"paramValue != null\" > #{paramValue,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"resultCode != null\" > #{resultCode,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"resultType != null\" > #{resultType,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"resultMessage != null\" > #{resultMessage,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"resultAnswerTime != null\" > #{resultAnswerTime,jdbcType=TIMESTAMP}, </if>" + 
		"      <if test=\"dataStatus != null\" > #{dataStatus,jdbcType=VARCHAR}, </if>" + 
		"    </trim>", 
		"</script>" })
    void addPushNoticeLog(PushNoticeLog pushNoticeLog);
    
    /**
     * 
     * 描述：推送日志查询
     * @author 李帅
     * @created 2018年3月12日 下午3:30:18
     * @since 
     * @param pushNoticeLog
     * @return
     * @throws Exception
     */
	@Select({ "<script>", 
		"select  id            AS id," + 
		"			push_from          AS pushFrom," + 
		"			push_to            AS pushTo," + 
		"			push_type          AS pushType," + 
		"			push_time          AS pushTime," + 
		"			template_id        AS templateId," + 
		"			notice_subject     AS noticeSubject," + 
		"			param_value        AS paramValue," + 
		"			result_code        AS resultCode," + 
		"			result_type        AS resultType," + 
		"			result_message     AS resultMessage," + 
		"			result_answer_time AS resultAnswerTime," + 
		"			data_status         AS dataStatus" + 
		"    from push_notice_log" + 
		"    where 1=1" + 
		"    <if test=\"id != null\" >and id = #{id,jdbcType=BIGINT}</if>" + 
		"    <if test=\"pushFrom != null\" >and push_from = #{pushFrom,jdbcType=VARCHAR}</if>" + 
		"    <if test=\"pushTo != null\" >and push_to = #{pushTo,jdbcType=VARCHAR}</if>" + 
		"    <if test=\"pushType != null\" >and push_type = #{pushType,jdbcType=VARCHAR}</if>" + 
		"    <if test=\"pushTime != null\" >and push_time = #{pushTime,jdbcType=TIMESTAMP}</if>" + 
		"    <if test=\"templateId != null\" >and template_id = #{templateId,jdbcType=VARCHAR}</if>" + 
		"    <if test=\"noticeSubject != null\" >and notice_subject = #{noticeSubject,jdbcType=VARCHAR}</if>" + 
		"    <if test=\"paramValue != null\" >and param_value = #{paramValue,jdbcType=VARCHAR}</if>" + 
		"    <if test=\"resultCode != null\" >and result_code = #{resultCode,jdbcType=VARCHAR}</if>" + 
		"    <if test=\"resultType != null\" >and result_type = #{resultType,jdbcType=VARCHAR}</if>" + 
		"    <if test=\"resultMessage != null\" >and result_message = #{resultMessage,jdbcType=VARCHAR}</if>" + 
		"    <if test=\"resultAnswerTime != null\" >and result_answer_time = #{resultAnswerTime,jdbcType=TIMESTAMP}</if>", 
		"</script>" })
    List<PushNoticeLog> getPushNoticeLog(PushNoticeLog pushNoticeLog);

    /**
     * 
     * 描述：推送模板的添加/更新功能
     * @author 李帅
     * @created 2018年3月12日 下午3:30:18
     * @since 
     * @param pushNoticeLog
     * @return
     * @throws Exception
     */
	@Insert({ "<script>", 
		"insert into push_notice_template " + 
		"    <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >" + 
//		"      <if test=\"id != null\" > id, </if>" + 
		"      <if test=\"templateId != null\" > template_id, </if>" + 
		"      <if test=\"templateName != null\" > template_name, </if>" + 
		"      <if test=\"templateContent != null\" > template_content, </if>" + 
		"      <if test=\"templateType != null\" > template_type, </if>" + 
		"      <if test=\"creator != null\" > creator, </if>" + 
		"      <if test=\"createTime != null\" > create_time, </if>" + 
		"      <if test=\"isDelete != null\" > is_delete, </if>" + 
		"    </trim>" + 
		"    <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >" + 
//		"      <if test=\"id != null\" > #{id,jdbcType=BIGINT}, </if>" + 
		"      <if test=\"templateId != null\" > #{templateId,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"templateName != null\" > #{templateName,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"templateContent != null\" > #{templateContent,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"templateType != null\" > #{templateType,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"creator != null\" > #{creator,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"createTime != null\" > #{createTime,jdbcType=TIMESTAMP}, </if>" + 
		"      <if test=\"isDelete != null\" > #{isDelete,jdbcType=VARCHAR}, </if>" + 
		"    </trim>", 
		"</script>" })
    void addPushNoticeTemplate(PushNoticeTemplate pushNoticeTemplate);
    
    /**
     * 
     * 描述：推送模板查询
     * @author 李帅
     * @created 2018年3月12日 下午3:30:18
     * @since 
     * @param pushNoticeLog
     * @return
     * @throws Exception
     */
	@Select({ "<script>", 
		"select  id      AS id," + 
		"			template_id      AS templateId," + 
		"			template_name    AS templateName," + 
		"			template_content AS templateContent," + 
		"			template_type    AS templateType," + 
		"			creator          AS creator," + 
		"			create_time      AS createTime," + 
		"			is_delete        AS isDelete" + 
		"    from push_notice_template" + 
		"    where 1=1 and is_delete='0'" + 
		"    <if test=\"id != null\" >and id = #{id,jdbcType=BIGINT}</if>" + 
		"    <if test=\"templateId != null\" >and template_id = #{templateId,jdbcType=VARCHAR}</if>" + 
		"    <if test=\"templateName != null\" >and template_name = #{templateName,jdbcType=VARCHAR}</if>" + 
		"    <if test=\"templateContent != null\" >and template_content = #{templateContent,jdbcType=VARCHAR}</if>" + 
		"    <if test=\"templateType != null\" >and template_type = #{templateType,jdbcType=VARCHAR}</if>" + 
		"    <if test=\"creator != null\" >and creator = #{creator,jdbcType=VARCHAR}</if>" + 
		"    <if test=\"createTime != null\" >and create_time = #{createTime,jdbcType=TIMESTAMP}</if>" + 
		"    <if test=\"isDelete != null\" >and is_delete = #{isDelete,jdbcType=VARCHAR}</if>", 
		"</script>" })
    List<PushNoticeTemplate> getPushNoticeTemplate(PushNoticeTemplate pushNoticeTemplate);
    
    /**
     * 
     * 描述：通过推送模板名称查询推送模板
     * @author 李帅
     * @created 2018年3月17日 下午2:23:58
     * @since 
     * @param templateName
     * @return
     * @throws Exception
     */
	@Select({ "<script>", 
		"select  id      AS id," + 
	    "			template_id    AS template_id," + 
		"			template_name    AS templateName," + 
		"			template_content AS templateContent," + 
		"			template_type    AS templateType," + 
		"			creator          AS creator," + 
		"			create_time      AS createTime," + 
		"			is_delete        AS isDelete" + 
		"    from push_notice_template" + 
		"    where 1=1 and is_delete='0'" + 
		"    <if test=\"tenantId != null\" >and tenant_id = #{tenantId,jdbcType=BIGINT}</if>" + 
		"    <if test=\"tenantId == null\" >and tenant_id is null</if>" + 
		"    <if test=\"templateId != null\" >and template_id = #{templateId,jdbcType=VARCHAR}</if>" + 
		"    <if test=\"templateType != null\" >and template_type = #{templateType,jdbcType=VARCHAR}</if>", 
		"</script>" })
    public PushNoticeTemplate getPushNoticeTemplateById(@Param("tenantId") Long tenantId, @Param("templateId") String templateId, @Param("templateType") String templateType);

	/**
	 * 
	 * 描述：查询租户邮箱信息
	 * @author 李帅
	 * @created 2018年7月24日 下午4:22:41
	 * @since 
	 * @param tenantId
	 * @return
	 * @throws Exception
	 */
	@Select({ "<script>", 
		"SELECT " + 
		"	id AS id, " + 
		"	tenant_id AS tenantId, " + 
		"	app_id AS appId, " + 
		"	mail_host AS mailHost, " + 
		"	mail_port AS mailPort, " + 
		"	mail_name AS mailName, " + 
		"	pass_word AS PASSWORD, " + 
		"	create_time AS createTime, " + 
		"	update_time AS updateTime " + 
		"FROM " + 
		"	tenant_mail_info " + 
		"where app_id = #{appId}", 
		"</script>" })
    public TenantMailInfo getTenantMailInfo(@Param("appId") Long appId);

	/**
	 * 
	 * 描述：添加APP证书信息
	 * @author 李帅
	 * @created 2018年7月24日 下午8:15:09
	 * @since 
	 * @param tenantMailInfo
	 */
	@Insert({ "<script>", 
		"insert into tenant_mail_info " + 
		"    <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >" + 
//		"      <if test=\"id != null\" > id, </if>" + 
		"      <if test=\"tenantId != null\" > tenant_id, </if>" + 
		"      <if test=\"appId != null\" > app_id, </if>" + 
		"      <if test=\"mailHost != null\" > mail_host, </if>" + 
		"      <if test=\"mailPort != null\" > mail_port, </if>" + 
		"      <if test=\"mailName != null\" > mail_name, </if>" + 
		" create_time," +
		"      <if test=\"passWord != null\" > pass_word, </if>" +
		"    </trim>" +
		"    <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >" +
//		"      <if test=\"id != null\" > #{id,jdbcType=BIGINT}, </if>" + 
		"      <if test=\"tenantId != null\" > #{tenantId,jdbcType=BIGINT}, </if>" + 
		"      <if test=\"appId != null\" > #{appId,jdbcType=BIGINT}, </if>" + 
		"      <if test=\"mailHost != null\" > #{mailHost,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"mailPort != null\" > #{mailPort,jdbcType=BIGINT}, </if>" + 
		"      <if test=\"mailName != null\" > #{mailName,jdbcType=VARCHAR}, </if>" + 
		" now(), " +
		"      <if test=\"passWord != null\" > #{passWord,jdbcType=VARCHAR}, </if>" +
		"    </trim>",
		"</script>" })
	void addTenantMailInfo(TenantMailInfo tenantMailInfo);
	
	/**
	 * 
	 * 描述：修改APP证书信息
	 * @author 李帅
	 * @created 2018年7月24日 下午8:15:17
	 * @since 
	 * @param tenantMailInfo
	 */
	@Update({ "<script>", 
		"update tenant_mail_info" + 
		"			set " + 
		"			<if test=\"tenantId != null\"> tenant_id=#{tenantId,jdbcType=BIGINT},</if>" +
		"			<if test=\"appId != null\"> app_id=#{appId,jdbcType=BIGINT},</if>" +
		"			<if test=\"mailHost != null\"> mail_host=#{mailHost,jdbcType=VARCHAR},</if>" + 
		"			<if test=\"mailPort != null\"> mail_port=#{mailPort,jdbcType=BIGINT},</if>" + 
		"			<if test=\"mailName != null\"> mail_name=#{mailName,jdbcType=VARCHAR},</if>" + 
		"			<if test=\"passWord != null\"> pass_word=#{passWord,jdbcType=VARCHAR},</if>" + 
		"           update_time = now()" +
		"		where id = #{id,jdbcType=BIGINT}",
		"</script>" })
	void updateTenantMailInfo(TenantMailInfo tenantMailInfo);
}