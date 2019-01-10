package com.iot.interceptors;

import com.google.common.collect.Lists;
import com.iot.common.annotation.AuthorizeShareRequired;
import com.iot.common.enums.PermissionEnum;
import com.iot.common.exception.BusinessException;
import com.iot.exception.BusinessExceptionEnum;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;

@Slf4j
@Component
@Aspect
@Order(3)//登入验证之后
public class AuthorizeShareAspect {

    public static final String SHARE_HEAD = "s_uuid";

    @Autowired
    private UserApi userApi;

    @Autowired
    private RedisCacheUtil redisCacheUtil;

    @Pointcut("@within(com.iot.common.annotation.AuthorizeShareRequired)")
    public void aspect() {
    }

    /**
     * 方法上注解情形
     */
    @Pointcut("@annotation(com.iot.common.annotation.AuthorizeShareRequired)")
    public void aspect2() {
    }

    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
    }

    @Around("aspect()||aspect2()")
    public Object aroundCheckFormSubmit(ProceedingJoinPoint joinPoint) throws Throwable {
        // 业务处理 拦截对应的资源获取对应的注解后 校验请求头 shareUUID 是否存在
        // 存在则需要校验对应的值是否存在对应的用户进行修改对应的 SaaSContextHolder.getCurrentUserId() 替换成受邀人的用户id
        // 方便后面查询受邀人业务信息
        String shareHead = getRequest().getHeader(SHARE_HEAD);
        if (!StringUtils.isEmpty(shareHead)) {
            FetchUserResp fetchUserResp = userApi.getUserByUuid(shareHead);
            if (fetchUserResp == null) {
                //share head share uuid error.
                throw new BusinessException(BusinessExceptionEnum.SHARE_HEAD_ERROR);
            }
            if (SaaSContextHolder.getCurrentUserId() != null &&
                    fetchUserResp.getId().compareTo(SaaSContextHolder.getCurrentUserId()) != 0) {
                String targetName = joinPoint.getTarget().getClass().getName();
                String methodName = joinPoint.getSignature().getName();
                Object[] arguments = joinPoint.getArgs();
                Class targetClass = Class.forName(targetName);
                Method[] methods = targetClass.getMethods();

                AuthorizeShareRequired required = null;
                required = getRequired(methodName, arguments, methods);
                if (checkPermission(required)) {
                    log.info("AuthorizeShareAspect:[currentUserId:{} changeTo target share userId:{}]"
                            , SaaSContextHolder.getCurrentUserId(), fetchUserResp.getId());
                    SaaSContextHolder.setCurrentUserId(fetchUserResp.getId());
                    SaaSContextHolder.setCurrentTenantId(fetchUserResp.getTenantId());
                    SaaSContextHolder.setCurrentUserUuid(fetchUserResp.getUuid());
                    SaaSContextHolder.setCurrentOrgId(fetchUserResp.getOrgId());
                } else {
                    throw new BusinessException(BusinessExceptionEnum.ARNOO_SHARE_NOT_PERMISSION);
                }
            }
        }
        return joinPoint.proceed();
    }

    @AfterThrowing(value = "aspect()||aspect2()", throwing = "e")
    public void afterThrowing(Throwable e) throws Throwable {
    }

    private AuthorizeShareRequired getRequired(String methodName, Object[] arguments, Method[] methods) {
        AuthorizeShareRequired required = null;
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazz = method.getParameterTypes();
                if (clazz.length == arguments.length) {
                    required = method.getAnnotation(AuthorizeShareRequired.class);
                    break;
                }
            }
        }
        return required;
    }

    private List<PermissionEnum> listPermissions() {
        return Lists.newArrayList(PermissionEnum.values());
    }

    private boolean checkPermission(AuthorizeShareRequired reqRequired) {
        if (reqRequired == null) {
            return false;
        }
        for (PermissionEnum permissionEnum : listPermissions()) {
            if (reqRequired.value().getCode().equals(permissionEnum.getCode())) {
                return true;
            }
        }
        return false;
    }
}