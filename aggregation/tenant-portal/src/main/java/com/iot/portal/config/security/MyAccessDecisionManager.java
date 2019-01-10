package com.iot.portal.config.security;

import com.iot.permission.api.PermissionApi;
import com.iot.permission.vo.UserToPermissionDto;
import com.iot.saas.SaaSContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 项目名称：cloud
 * 功能描述：spring security校验当前用户是否有权限访问此url
 * 创建人： yeshiyuan
 * 创建时间：2018/7/11 17:34
 * 修改人： yeshiyuan
 * 修改时间：2018/7/11 17:34
 * 修改描述：
 */
@Component("myAccessDecisionManager")
public class MyAccessDecisionManager implements AccessDecisionManager {

    @Autowired
    private PermissionApi permissionApi;

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        FilterInvocation filterInvocation = (FilterInvocation) object;
        String url = filterInvocation.getHttpRequest().getRequestURI();
        UserToPermissionDto dto = new UserToPermissionDto();
        dto.setUserId(SaaSContextHolder.getCurrentUserId());
        List<String> urls = new ArrayList<>();
        urls.add(url);
        dto.setPermissionUrls(urls);
        Map<String,Boolean> map = permissionApi.verifyUserPermission(dto);
        if (map.get(url)){
            return;
        }
        throw new AccessDeniedException("access denied");
        //throw new BusinessException(BusinessExceptionEnum.ACCESS_DENIED);
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;//必须为true
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;//必须为true
    }
}
