package com.iot.cloud.config.shiro;

import com.iot.cloud.util.ConstantUtil;
import com.iot.common.exception.BusinessException;
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

public class CustomRealm extends AuthorizingRealm{

	@Autowired  
	private UserApi userApi;
	
	@Autowired
	private ConstantUtil constantUtil;
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		 //用户名    
		LoginResp user = (LoginResp) principals.fromRealm(getName()).iterator().next();   
		String username = user.getUserUuid();
        //根据用户名来添加相应的权限和角色  
        if(!StringUtils.isEmpty(username)){  
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();  
            addPermission(username,info);  
            addRole(username, info);  
            return info;  
        }  
        return null;  
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
				req.setUserName(accountName);
				req.setPwd(new String(token.getPassword()));
				req.setTenantId(SaaSContextHolder.currentTenantId());
				user = userApi.login2B(req);
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
	
    /** 
     * 添加权限 
     * @param username 
     * @param info 
     * @return 
     */  
    private SimpleAuthorizationInfo addPermission(String username,SimpleAuthorizationInfo info) {  
//        List<Permission> permissions = permissionDao.findPermissionByName(username);  
//        for (Permission permission : permissions) {  
//            info.addStringPermission(permission.getUrl());//添加权限    
//        }  
        return info;    
    }    
    
    /** 
     * 添加角色 
     * @param username 
     * @param info 
     */  
    private void addRole(String username, SimpleAuthorizationInfo info) {  
//        List<Role> roles = roleDao.findByUser(username);  
//        if(roles!=null&&roles.size()>0){  
//            for (Role role : roles) {  
//                info.addRole(role.getRoleName());  
//            }  
//        }  
    }  
    
    

}
