package com.iot.center.interceptor;

import java.lang.reflect.Method;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.iot.building.allocation.vo.ActivityRecordReq;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.iot.building.allocation.api.ActivityRecordApi;
import com.iot.center.annotation.SystemLogAnnotation;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.user.api.UserApi;
import com.iot.user.vo.LoginResp;

public class EcmSysemLogInterceptor implements HandlerInterceptor {

	private ActivityRecordApi activityRecordApi=ApplicationContextHelper.getBean(ActivityRecordApi.class);
	
	private static final Logger log = LoggerFactory.getLogger(EcmSysemLogInterceptor.class);

	private UserApi userApi = ApplicationContextHelper.getBean(UserApi.class);

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object handler) throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod) handler;        
		// 从方法处理器中获取出要调用的方法       
		Method method = handlerMethod.getMethod();        
		// 获取出方法上的Access注解        
		SystemLogAnnotation systemLogAnnotation = method.getAnnotation(SystemLogAnnotation.class);        
		if (systemLogAnnotation != null) {        	
			// 如果自定义注解不为空, 则取出配置值        	
			String value = systemLogAnnotation.value();        	
			// 保存操作日志        	
			//addSystemLog(value);
		}	
		return true;      
	}

	/**
	 * 保存日志
	 * @param value
	 */
	private void addSystemLog(String value) {
		try{
			log.info(".............record_active_start.............");
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			if(user !=null) {
				log.info(".............record_active_process1.............");
				ActivityRecordReq activityRecordReq=new ActivityRecordReq();
				activityRecordReq.setActivity(value);
				activityRecordReq.setCreateBy(user.getUserId());
				activityRecordReq.setUserId(user.getUserId());
				activityRecordReq.setTenantId(user.getTenantId());
				activityRecordReq.setLocationId(user.getLocationId());
				activityRecordReq.setType("SYSTEM");
				activityRecordReq.setTime(new Date());
				log.info(".............record_active_process2.............");
				activityRecordReq.setUserName(userApi.getUser(user.getUserId()).getUserName());
				log.info(".............record_active_process3.............");
				activityRecordApi.saveActivityRecordToB(activityRecordReq);
				log.info(".............record active.............");
			}else {
				log.info(".............lost user record active.............");
			}
		}catch (Exception e){
              e.printStackTrace();
		}
	}

}
