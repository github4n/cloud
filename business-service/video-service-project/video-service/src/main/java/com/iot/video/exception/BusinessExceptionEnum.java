package com.iot.video.exception;

import com.iot.common.exception.IBusinessException;

/** 
 * 
 * 项目名称：IOT视频云
 * 模块名称：聚合层
 * 功能描述：视频云聚合层异常枚举
 * 创建人： mao2080@sina.com 
 * 创建时间：2018/3/22 15:16
 * 修改人： mao2080@sina.com 
 * 修改时间：2018/3/22 15:16
 * 修改描述：
 */
public enum BusinessExceptionEnum implements IBusinessException {

	/** 参数tenantId为空*/
	EMPTY_OF_TENANTID("videoService.tenantId.empty"),
	
	EMPTY_OF_USERID("videoService.userId.empty"),

	EMPTY_OF_DEVICE_ID("videoService.deviceId.empty"),
	
	EMPTY_OF_PLANEXECSTATUS("videoService.planExecStatus.empty"),
	
	EMPTY_OF_TASKPLANLIST("videoService.taskPlanList.empty"),
	
	PLANCYCLE_IS_NOT_WEEK_OR_DAY("videoService.planCycle.error"),
	
	UPDATE_PLAN_FAIL("videoService.updatePlan.fail"),
	
	UPDATE_PLANEXECSTATUS_FAIL("videoService.updatePlanExecStatus.fail"),
	
	EMPTY_OF_PLANID("videoService.planId.empty"),
	
	GET_VIDEOTASKLIST_FAIL("videoService.getVideoTaskList.fail"),
	
	GET_PLANLIST_FAIL("videoService.getPlanList.fail"),

	GET_PLANINFO_FAIL("videoService.getPlanInfoByDevId.fail"),
	
	EMPTY_OF_DEVICETYPE("videoService.deviceType.empty"),
	
	GET_VIDEOPACKAGELIST_FAIL("videoService.getVideoPackageList.fail"),

    PARAM_ERROR("videoService.param.error"),

    CREATE_PLAN_FAILED("videoService.createPlan.failed"),

    UPDATE_PLAN_NAME_FAILED("videoService.updatePlanName.failed"),

    GET_BUY_HIS_RECORD_FAILED("videoService.getBuyHisRecordList.failed"),

    GET_BUY_RECORD_FAILED("videoService.getBuyRecordList.failed"),

    RENEW_PLAN_FAILED("videoService.renewPlan.failed"),

    GET_VIDEOEVENTLIST_FAILED("videoService.getVideoEventList.failed"),

    GET_VIDEOFILELIST_FAILED("videoService.getVideoFileList.failed"),

	GET_VIDEOFILECOUNT_FAILED("videoService.getVideoFileCount.failed"),

    UPDATE_PLANORDER_FAILED("videoService.updatePlanOrder.failed"),

    GET_EVENTPHOTOLIST_FAILED("videoService.getEventPhotoList.failed"),

    GET_PLANLASTPIC_FAILED("videoService.getPlanLastPic.failed"),

    ISBINDPLAN_FAILED("videoService.isBindPlan.failed"),
    
    /** 参数deviceId为空*/
	EMPTY_OF_DEVICEID("videoService.deviceId.empty"),
	
    INSERT_VIDEO_EVENT_FAILED("videoService.insertVideoEvent.failed"),

    PLAN_BANDING_DEVICE_FAILED("videoService.planBandingDevice.failed"),

    PLAN_BANDING_DEVICE_ALREADYBOUND("videoService.planBandingDevice.alreadyBound"),

    GET_DEVICEID_FAILED("videoService.getDeviceId.failed"),

	GET_PLANID_FAILED("videoService.getPlanId.failed"),

	COUNT_DEVICEBANDINGPLAN_FAILED("videoService.judgeDeviceBandPlan.failed"),

    GET_SYNCTASKINFO_FAILED("videoService.getSyncTaskInfo.failed"),

    CREATE_VIDEOFILE_FAILED("videoService.createVideoFile.failed"),
    /** 获取全时录影失效文件ID*/
	GETLAPSEFILEIDANDEVENTIDLIST_FAILED("videoService.getLapseFileIdAndEventIdList.failed"),
	/** 删除全时录影失效文件数据*/
	DELETELAPSEFILE_FAILED("videoService.deleteLapseFile.failed"),
	/** 删除全时录影失效事件数据*/
	DELETELAPSEEVENT_FAILED("videoService.deleteLapseEvent.failed"),
	/** 获取全时录影失效事件ID*/
	GETSCHEDULEPLANINFOLIST_FAILED("videoService.getSchedulePlanInfoList.failed"),
	/** 批量更新计划提醒状态*/
	BATCHUPDATEPLANREMINDSTATE_FAILED("videoService.batchUpdatePlanRemindState.failed"),
	/** 获取所有厂商所有计划列表失败*/
	GET_ALLVENDER_PLANLIST_FAILED("videoService.getAllVenderPlanList.failed"),
	/** 排序获取前n条事件信息失败*/
	GET_SORT_VIDEO_EVENTINFO_FAILED("videoService.getSortVideoEventInfo.failed"),
	/** 获取事件关联的ts文件信息失败*/
	GET_EVENT_TS_FILEINFO_FAILED("videoService.getEventTsFileInfo.failed"),
    PLAN_NOT_BOUND_DEVICE("videoService.Plan.not.bound.device"),
	PLAN_NOT_EXISTS("videoService.plan.notExists"),

	PACKAGEID_NOT_EXISTS("videoService.packageId.notExists"),

	PACKAGEINFO_NOT_EXISTS("videoService.packageInfo.notExists"),
	
	GET_UNTIE_TASK_INFO_FAILED("videoService.getUntieTaskInfo.failed"),
	
	EVENT_NOT_EXIST("videoService.VideoEvent.does.not.exist"),

	DELETE_VIDEO_EVENT_FAILED("videoService.deleteVideoEvent.failed"),

	/**
	 * 计划过期
	 */
	PLAN_EXPIRE("videoService.plan.expire")
    ;

    private String messageKey;

    BusinessExceptionEnum(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public int getCode() {
        return 0;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }

}