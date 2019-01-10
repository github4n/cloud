package com.iot.message.service;

import java.util.List;

import com.iot.message.entity.AppCertInfo;
import com.iot.message.entity.SystemPushControl;
import com.iot.message.entity.UserPhoneRelate;

public interface SystemService {

	/**
	 * 
	 * 描述：新增用户和手机端关系
	 * @author 李帅
	 * @created 2018年3月12日 下午3:13:21
	 * @since 
	 * @param userPhoneRelate
	 * @return
	 */
	void addUserPhoneRelate(UserPhoneRelate userPhoneRelate);
	
	/**
	 * 
	 * 描述：更新用户和手机端关系
	 * @author 李帅
	 * @created 2018年3月12日 下午3:13:21
	 * @since 
	 * @param userPhoneRelate
	 * @return
	 */
	void updateUserPhoneRelate(UserPhoneRelate userPhoneRelate);
	
	/**
	 * 
	 * 描述：查询用户和手机端关系
	 * @author 李帅
	 * @created 2018年3月17日 下午2:27:19
	 * @since 
	 * @param userIds
	 * @return
	 */
	List<UserPhoneRelate> getUserPhoneRelates(List<String> userIds);

	/**
	  * @despriction：删除用户和手机端关系
	  * @author  yeshiyuan
	  * @created 2018/5/29 13:40
	  * @param null
	  * @return
	  */
	int deleteUserPhoneRelate(String userUuid);
	
	/**
	 * 
	 * 描述：删除用户手机ID
	 * @author 李帅
	 * @created 2018年12月5日 下午5:44:04
	 * @since 
	 * @param userUuid
	 * @return
	 */
	int deleteUserPhoneId(String userUuid);
	
	/**
	 * 
	 * 描述：查询APP证书信息
	 * @author 李帅
	 * @created 2018年7月24日 下午5:04:31
	 * @since 
	 * @param appId
	 * @return
	 */
	AppCertInfo getAppCertInfo(Long appId);
	
	/**
	 * 
	 * 描述：添加租户邮箱信息
	 * @author 李帅
	 * @created 2018年7月24日 下午8:13:02
	 * @since 
	 * @param appCertInfo
	 */
	void addAppCertInfo(AppCertInfo appCertInfo);
	
	/**
	 * 
	 * 描述：修改租户邮箱信息
	 * @author 李帅
	 * @created 2018年7月24日 下午8:13:10
	 * @since 
	 * @param appCertInfo
	 */
	void updateAppCertInfo(AppCertInfo appCertInfo);
	
	/**
	 * 
	 * 描述：查询系统推送控制
	 * @author 李帅
	 * @created 2018年11月16日 下午4:42:14
	 * @since 
	 * @param userId
	 * @param appId
	 * @return
	 */
	SystemPushControl getSystemPushControl(String userId, Long appId);
	
	/**
	 * 
	 * 描述：添加系统推送控制
	 * @author 李帅
	 * @created 2018年11月16日 下午4:43:33
	 * @since 
	 * @param systemPushControl
	 */
    void addSystemPushControl(SystemPushControl systemPushControl);
    
    /**
     * 
     * 描述：修改系统推送控制
     * @author 李帅
     * @created 2018年11月16日 下午4:44:15
     * @since 
     * @param systemPushControl
     */
    void updateSystemPushControl(SystemPushControl systemPushControl);
}
