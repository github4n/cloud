package com.iot.message.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.message.contants.ModuleConstants;
import com.iot.message.dao.SystemMapper;
import com.iot.message.entity.AppCertInfo;
import com.iot.message.entity.SystemPushControl;
import com.iot.message.entity.UserPhoneRelate;
import com.iot.message.service.SystemService;
import com.iot.redis.RedisCacheUtil;

@Service("systemService")
public class SystemServiceImpl implements SystemService {


    @Autowired
    private SystemMapper systemMapper;

    /**
	 * 
	 * 描述：新增用户和手机端关系
	 * @author 李帅
	 * @created 2018年3月12日 下午3:13:21
	 * @since 
	 * @param userPhoneRelate
	 * @return
	 */
    @Override
	public void addUserPhoneRelate(UserPhoneRelate userPhoneRelate) {
		systemMapper.addUserPhoneRelate(userPhoneRelate);
	}
	
    /**
	 * 
	 * 描述：更新用户和手机端关系
	 * @author 李帅
	 * @created 2018年3月12日 下午3:13:21
	 * @since 
	 * @param userPhoneRelate
	 * @return
	 */
    @Override
	public void updateUserPhoneRelate(UserPhoneRelate userPhoneRelate) {
		systemMapper.updateUserPhoneRelate(userPhoneRelate);
	}
    
    /**
     * 
     * 描述：查询用户和手机端关系
     * @author 李帅
     * @created 2018年3月17日 下午2:27:30
     * @since 
     * @param userIds
     * @return
     */
    @Override
    public List<UserPhoneRelate>  getUserPhoneRelates(List<String> userIds) {
    	List<UserPhoneRelate> resultList = new ArrayList<UserPhoneRelate>();
    	List<String> needSelectId = new ArrayList<String>();
    	for(String userId : userIds) {
    		UserPhoneRelate userPhoneRelate = RedisCacheUtil.valueObjGet(ModuleConstants.USERPHONERELATEREDIS + userId, UserPhoneRelate.class);
    		if(userPhoneRelate == null) {
    			needSelectId.add(userId);
    		}else {
    			resultList.add(userPhoneRelate);
    		}
    	}
    	List<UserPhoneRelate> selectList = systemMapper.getUserPhoneRelates(userIds);
    	for(UserPhoneRelate userPhoneRelate : selectList) {
    		RedisCacheUtil.valueObjSet(ModuleConstants.USERPHONERELATEREDIS + userPhoneRelate.getUserId(), userPhoneRelate);
    	}
    	resultList.addAll(selectList);
    	return resultList;
    }

    /**
     * 
     * 描述：删除用户和手机端关系
     * @author 李帅
     * @created 2018年7月24日 下午7:35:19
     * @since 
     * @param userUuid
     * @return
     */
    @Override
    public int deleteUserPhoneRelate(String userUuid) {
        return systemMapper.deleteUserPhoneRelate(userUuid);
    }
    
    /**
     * 
     * 描述：删除用户手机ID
     * @author 李帅
     * @created 2018年12月5日 下午5:44:12
     * @since 
     * @param userUuid
     * @return
     */
    @Override
    public int deleteUserPhoneId(String userUuid) {
        return systemMapper.deleteUserPhoneId(userUuid);
    }
    
    /**
     * 
     * 描述：查询APP证书信息
     * @author 李帅
     * @created 2018年7月24日 下午7:03:21
     * @since 
     * @param appId
     * @return
     */
    @Override
    public AppCertInfo getAppCertInfo(Long appId) {
    	return systemMapper.getAppCertInfo(appId);
    }
    
    /**
     * 
     * 描述：添加租户邮箱信息
     * @author 李帅
     * @created 2018年7月24日 下午8:13:22
     * @since 
     * @param appCertInfo
     */
    @Override
    public void addAppCertInfo(AppCertInfo appCertInfo) {
    	systemMapper.addAppCertInfo(appCertInfo);
    }
    
    /**
     * 
     * 描述：修改租户邮箱信息
     * @author 李帅
     * @created 2018年7月24日 下午8:13:31
     * @since 
     * @param appCertInfo
     */
    @Override
    public void updateAppCertInfo(AppCertInfo appCertInfo) {
    	systemMapper.updateAppCertInfo(appCertInfo);
    }
    
    /**
     * 
     * 描述：查询系统推送控制
     * @author 李帅
     * @created 2018年11月16日 下午4:42:24
     * @since 
     * @param userId
     * @param appId
     * @return
     */
    @Override
    public SystemPushControl getSystemPushControl(String userId, Long appId) {
    	return systemMapper.getSystemPushControl(userId, appId);
    }
    
    /**
     * 
     * 描述：添加系统推送控制
     * @author 李帅
     * @created 2018年11月16日 下午4:43:40
     * @since 
     * @param systemPushControl
     */
    @Override
    public void addSystemPushControl(SystemPushControl systemPushControl) {
    	systemMapper.addSystemPushControl(systemPushControl);
    }
    
    /**
     * 
     * 描述：修改系统推送控制
     * @author 李帅
     * @created 2018年11月16日 下午4:44:08
     * @since 
     * @param systemPushControl
     */
    @Override
    public void updateSystemPushControl(SystemPushControl systemPushControl) {
    	systemMapper.updateSystemPushControl(systemPushControl);
    }
}
