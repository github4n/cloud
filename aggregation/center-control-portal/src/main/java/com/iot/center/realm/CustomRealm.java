package com.iot.center.realm;

import com.google.common.collect.Sets;
import com.iot.center.helper.BusinessExceptionEnum;
import com.iot.center.service.UserService;
import com.iot.center.vo.UserInfoVo;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.permission.vo.PermissionDto;
import com.iot.permission.vo.RoleDto;
import com.iot.saas.SaaSContextHolder;
import com.iot.user.api.UserApi;
import com.iot.user.vo.LoginReq;
import com.iot.user.vo.LoginResp;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class CustomRealm extends AuthorizingRealm{

	@Autowired  
	private UserApi userApi;
	@Autowired
	private UserService userService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println(">>>>>>>>>enter auth..................");
		 //用户名    
		LoginResp user = (LoginResp) principals.fromRealm(getName()).iterator().next();   
		Long userId = user.getUserId();
		//根据用户ID来添加相应的权限和角色
		if (userId != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//			addPermissionAndRole(userId, info);
			return info;
		}
        return null;
	}

	private void addPermissionAndRole(Long userId, LoginResp user) {
		CommonResponse<UserInfoVo> response = userService.getUserPermission(userId);
		if (response.isError()) {
			return;
		}
		UserInfoVo userInfo = response.getData();
		// add role
		Set<String> roles = Sets.newHashSet();
		for (RoleDto role : userInfo.getRoles()) {
			roles.add(role.getRoleCode());
		}
		user.setRoles(roles);

		// add permission
		Set<String> permissions = Sets.newHashSet();
		for (PermissionDto p : userInfo.getFunctionPermissions()) {
			permissions.add(p.getPermissionCode());
		}

		user.setPermissions(permissions);
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		 //令牌——基于用户名和密码的令牌    
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        //令牌中可以取出用户名
        String accountName = token.getUsername();  
        //让shiro框架去验证账号密码  
        LoginResp user=null;
        if(!StringUtils.isEmpty(accountName)){  
			try {
				LoginReq req=new LoginReq();
				req.setTenantId(SaaSContextHolder.currentTenantId());
				req.setUserName(accountName);
				req.setPwd(new String(token.getPassword()));
				user = userApi.login2B(req);
				addPermissionAndRole(user.getUserId(), user);
			} catch (BusinessException e) {
				e.printStackTrace();
				throw new AuthenticationException(e.getMessage());
			}
			if(user != null){
				return new SimpleAuthenticationInfo(user, token.getPassword(), getName());  
			}
        }  
		return null;
	}

}
