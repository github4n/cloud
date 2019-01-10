package com.iot.center.interceptor;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.iot.center.annotation.PermissionAnnotation;
import com.iot.center.helper.Constants;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.permission.vo.PermissionDto;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import com.iot.user.vo.LoginResp;

public class PermissionInterceptor implements HandlerInterceptor {

	private static final Logger log = LoggerFactory.getLogger(PermissionInterceptor.class);
	
	private UserApi userService= ApplicationContextHelper.getBean(UserApi.class);
	
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
		boolean flag = false;
		HandlerMethod handlerMethod = (HandlerMethod) handler;        
		// 从方法处理器中获取出要调用的方法       
		Method method = handlerMethod.getMethod();        
		// 获取出方法上的Access注解        
		PermissionAnnotation permissionAnnotation = method.getAnnotation(PermissionAnnotation.class);        
		if (permissionAnnotation != null) {
			// 如果自定义注解不为空, 则取出配置值
			String value = permissionAnnotation.value();        	
			// 验证权限        	
			flag = verificationPermission(value);
			if(flag){
				return flag;
			}else {
				handleNotAuthorized(arg0, arg1, handler);
				return flag;
			}
		}	
		return true;      
	}

	protected void handleNotAuthorized(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws ServletException, IOException {
		// 403表示资源不可用。服务器理解用户的请求，但是拒绝处理它，通常是由于权限的问题
		response.sendError(403);
	}

	/**
	 * 验证权限
	 * @param value
	 */
	private Boolean verificationPermission(String value) {
		Boolean flag=false;
		if(StringUtils.isBlank(value)) {//空的值相当于不限制权限
			return false;
		}
		if(SecurityUtils.getSubject() !=null) {
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			if(user !=null) {
				FetchUserResp userInfo=userService.getUser(user.getUserId());
				flag=userInfo.getAdminStatus()==-1?true:judgePermission(user.getUserId(),value);//超级管理员为-1
			}else {
				flag=false;
			}
		}
		return flag;
	}
	
	private Boolean judgePermission(Long userId,String value) {
		Boolean flag=false;
		if(value.contains(",")) {//存在多个权限并存逗号相隔判断其中的某个合法
			String[] permissStr=value.split(",");
			for(String str:permissStr) {
				if(judgeExistPerStr(userId,str)) {
					return true;
				}
			}
			return flag;
		}else {//单独权限判断
			return judgeExistPerStr(userId,value);
		}
	}
	
	/**
	 * 判断是否存在该permission code
	 * @param perStr
	 * @return
	 */
	private Boolean judgeExistPerStr(Long userId,String perStr) {
        Boolean flag = false;
        //获取该用户所有的权限
        List<PermissionDto> permissions = (List<PermissionDto>) Constants.userPermission.get(userId);
        if(CollectionUtils.isEmpty(permissions)) {
        	return flag;
        }
        for (PermissionDto dto : permissions) {
            if (dto.getPermissionCode().equals(perStr)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

}
