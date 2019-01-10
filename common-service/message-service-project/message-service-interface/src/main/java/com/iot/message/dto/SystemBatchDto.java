package com.iot.message.dto;

import java.util.List;
import java.util.Map;

/**
 * 
 * 项目名称：IOT云平台
 * 模块名称：
 * 功能描述：邮件批量推送对象
 * 创建人： 李帅
 * 创建时间：2018年3月16日 上午11:46:36
 * 修改人：李帅
 * 修改时间：2018年3月16日 上午11:46:36
 */
public class SystemBatchDto {

	/**
	 * 用户ID列表
	 */
	private List<String> userIds;
	
	/**
	 * 邮件内容
	 */
	private Map<String, String> noticeMap;

	/**
	 * 重试次数
	 */
	private Integer retryNum;
	
	public Map<String, String> getNoticeMap() {
		return noticeMap;
	}

	public void setNoticeMap(Map<String, String> noticeMap) {
		this.noticeMap = noticeMap;
	}

	public List<String> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}

	public Integer getRetryNum() {
		return retryNum;
	}

	public void setRetryNum(Integer retryNum) {
		this.retryNum = retryNum;
	}
	
	
}
