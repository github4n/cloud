package com.iot.schedule.job;

import com.iot.common.exception.BusinessException;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.common.util.JsonUtil;
import com.iot.common.util.StringUtil;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.message.api.MessageApi;
import com.iot.payment.api.GoodsServiceApi;
import com.iot.shcs.ipc.api.IpcApi;
import com.iot.system.api.LangApi;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import com.iot.video.api.VideoManageApi;
import com.iot.video.api.VideoTimerApi;
import com.iot.video.dto.SchedulePlanDto;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：视频模块
 * 功能描述：更新全时录影任务
 * 创建人： 李帅
 * 创建时间：2017年11月1日 下午2:28:20
 * 修改人：李帅
 * 修改时间：2017年11月1日 下午2:28:20
 */
@Component
public class UpdatePlanScheduleJob implements Job {

	private final Logger logger = LoggerFactory.getLogger(getClass());


	VideoTimerApi videoTimerApi = ApplicationContextHelper.getBean(VideoTimerApi.class);
	
    MessageApi messageApi = ApplicationContextHelper.getBean(MessageApi.class);

	IpcApi ipcApi = ApplicationContextHelper.getBean(IpcApi.class);
	
	UserApi userApiService = ApplicationContextHelper.getBean(UserApi.class);

	DeviceCoreApi deviceCoreApi = ApplicationContextHelper.getBean(DeviceCoreApi.class);

	VideoManageApi videoManageApi = ApplicationContextHelper.getBean(VideoManageApi.class);

	GoodsServiceApi goodsServiceApi = ApplicationContextHelper.getBean(GoodsServiceApi.class);

	LangApi langApi = ApplicationContextHelper.getBean(LangApi.class);

	private String homePageUrl;
	
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		homePageUrl = (String) (jobExecutionContext.getMergedJobDataMap().get("homePageUrl"));
		try {

			Map<String, String> map = null;
			List<SchedulePlanDto> inPeriodPlanInfos = null;
			List<SchedulePlanDto> comeDuePlanInfos = null;
			List<SchedulePlanDto> planInfos = null;
			List<SchedulePlanDto> outDatePlanInfos = null;
			List<String> lapseFileIds = null;

			/*----------------处理临期计划 start--------------------*/
			// 获取所有临期计划
			inPeriodPlanInfos = videoTimerApi.getSchedulePlanInfoList("0");
			// 循环发送邮件提醒用户计划临期
			List<String> inPeriodPlanIds = new ArrayList<String>();
			for (SchedulePlanDto schedulePlan : inPeriodPlanInfos) {
				inPeriodPlanIds.add(schedulePlan.getPlanId());
				String subjectSuffix = "The plan is about to expire in 5 days";
				this.sendMail(schedulePlan,"CH00006",subjectSuffix);
			}
			// 更新计划到期提醒状态为“计划临期”
			if (inPeriodPlanIds != null && inPeriodPlanIds.size() > 0) {
				logger.info("UpdatePlanScheduleJob start  : deal will expire plan -> planIdSet:{}", JsonUtil.toJson(inPeriodPlanIds));
				videoTimerApi.batchUpdatePlanRemindState(inPeriodPlanIds, "1");
			}
			/*----------------处理临期计划 end--------------------*/

			/*----------------处理到期计划 start--------------------*/
			// 获取所有到期计划
			comeDuePlanInfos = videoTimerApi.getSchedulePlanInfoList("1");
			// 循环发送邮件提醒用户计划到期
			List<String> comeDuePlanIds = new ArrayList<String>();
			List<String> needNotifyIpc = new ArrayList<>();
			for (SchedulePlanDto schedulePlan : comeDuePlanInfos) {
				comeDuePlanIds.add(schedulePlan.getPlanId());
				if (StringUtil.isNotBlank(schedulePlan.getDeviceId())){
					needNotifyIpc.add(schedulePlan.getPlanId());
				}
				String subjectSuffix = "The plan expires and the service is turned off";
				this.sendMail(schedulePlan,"CH00004",subjectSuffix);

			}
			// 更新计划到期提醒状态为“计划到期”
			if (comeDuePlanIds != null && comeDuePlanIds.size() > 0) {
				// 更新计划到期提醒状态为“计划到期”,执行状态为“计划过期”,关闭录影
				logger.info("UpdatePlanScheduleJob start : deal plan expire within five day  -> planIdSet:{}", JsonUtil.toJson(comeDuePlanIds));
				//操作类型(1:计划过期5天之内)
				videoTimerApi.bacthDealPlanExpire(comeDuePlanIds,"1");
			}
			//通知设备停止录影
			for (String planId:needNotifyIpc ) {
				ipcApi.notifyDevicePlanInfo(planId);
			}

			//计划到期五天之内每天通知一次
			for (int i = 1; i <= 4; i++) {
				// 获取所有过期计划
				planInfos = videoTimerApi.getSchedulePlanInfoList("2" + i);
				// 循环发送邮件提醒用户计划过期
				for (SchedulePlanDto schedulePlan : planInfos) {
					String subjectSuffix = "(Plan has expired " + i  + " day)";
					this.sendMail(schedulePlan,"CH00007",subjectSuffix);
				}
			}
			/*----------------处理到期计划 end--------------------*/


			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// 获取所有过期计划
			outDatePlanInfos = videoTimerApi.getSchedulePlanInfoList("2");
			// 循环发送邮件提醒用户计划过期
			List<String> outDatePlanIds = new ArrayList<String>();
			List<SchedulePlanDto> outDateSchedulePlans = new ArrayList<SchedulePlanDto>();
			for (SchedulePlanDto schedulePlan : outDatePlanInfos) {
				outDatePlanIds.add(schedulePlan.getPlanId());
				outDateSchedulePlans.add(schedulePlan);
				String subjectSuffix = "The plan has expired, please re-purchase the new plan";
				this.sendMail(schedulePlan,"CH00007",subjectSuffix);
			}
			// 更新计划到期提醒状态为“计划过期”
			if (outDatePlanIds != null && outDatePlanIds.size() > 0) {
				// 更新计划到期提醒状态为“计划过期”，执行状态为“执行失效”,
				//操作类型(2:计划过期5天之后)
				logger.info("UpdatePlanScheduleJob start : deal plan expire after five day  -> planIdSet:{}", JsonUtil.toJson(outDatePlanIds));
				videoTimerApi.bacthDealPlanExpire(outDatePlanIds,"2");
				for (String planId:outDatePlanIds){
					//解除设备绑定计划，删掉redis中计划缓存信息
					this.videoManageApi.dealPlanExpireRelateData(planId);
				}
			}
		} catch (BusinessException e) {
			logger.error("UpdatePlanScheduleJob run error:",e);
		}
	}

	/**
	  * @despriction：发邮件通知用户计划情况
	  * @author  yeshiyuan
	  * @created 2018/6/14 13:57
	  * @param schedulePlan
	  * @param templateId 模板id
	  * @param subjectSuffix 内容后缀
	  * @return
	  */
	public void sendMail(SchedulePlanDto schedulePlan,String templateId,String subjectSuffix){
		Map<String,String> map = new HashMap<String, String>();
		try {
			FetchUserResp user = userApiService.getUserByUuid(schedulePlan.getUserId());
			if (StringUtil.isNotBlank(schedulePlan.getDeviceId())){
				GetDeviceInfoRespVo device = deviceCoreApi.get(schedulePlan.getDeviceId());
				if (StringUtil.isEmpty(device.getName())) {
				map.put("deviceName", "");
				} else {
					map.put("deviceName", device.getName());
				}
			}else{
				map.put("deviceName", "");
			}
			map.put("subject", (schedulePlan.getPlanName() + "- " + subjectSuffix ));
			map.put("templateId", templateId);
			map.put("userName", user.getUserName());
			map.put("planName", schedulePlan.getPlanName());
			map.put("planStartTime", schedulePlan.getPlanStartTime());
			map.put("planEndTime", schedulePlan.getPlanEndTime());
			Map<Long, String> goodsNameMap = goodsServiceApi.getGoodsNameByGoodsId(Arrays.asList(schedulePlan.getPackageId()));
			String langKey = goodsNameMap.get(schedulePlan.getPackageId());
			Map<String,String> langMap = langApi.getLangValueByKey(Arrays.asList(langKey), LocaleContextHolder.getLocale().toString());
			map.put("packageName", langMap.get(langKey));
			//map.put("renewalConcat", homePageUrl);
			map.put("renewalConcat", "test");
			// 录影计划名称-到期时间（精确到小时）- （计划即将在5天后到期）请及时续费确保计划使用
			messageApi.mailSinglePush(schedulePlan.getTenantId(), user.getUserName(), map, 0, LocaleContextHolder.getLocale().toString());
		} catch (Exception e) {
			logger.error("Email(inPeriod) send fail: " + schedulePlan.getPlanId(), e);
		}
	}

}
