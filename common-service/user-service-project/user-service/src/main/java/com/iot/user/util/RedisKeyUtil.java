package com.iot.user.util;

import com.iot.saas.SaaSContextHolder;

import java.io.Serializable;

public class RedisKeyUtil implements Serializable {

    // 默认7天
    public static final Long DEFAULT_CACHE_TIME = 60 * 60 * 24 * 7L;

    /**
     * redis key 拼接
     */
    //1.accessToken缓存
    public static final String ACCESS_TOKEN_KEY = "accessToken:%s";
    //2.refreshToken缓存
    public static final String REFRESH_TOKEN_KEY = "refreshToken:%s";
    //3.userToken缓存
    public static final String USER_TOKEN_KEY = "userToken:%d";
    //4.注册验证码缓存
    public static final String REGISTER_CODE_KEY = "registerCode:%s";
    //5.重置密码验证码缓存
    public static final String RESET_PWD_CODE_KEY = "resetPwdCode:%s:%d";
    //6.登录失败验证码缓存
    public static final String LOGIN_FAIL_CODE_KEY = "loginFailCode:%s:%d";
    //7.登录失败次数缓存
    public static final String LOGIN_FAIL_NUM_KEY = "loginFailNum:%s:%d";
    //23.用户登录失败锁定时间
    public static final String LOGIN_FAIL_LOCKED_TIME = "loginFailLockedTime:%s:%d";
    //8.用户缓存
    public static final String USER_KEY = "user:%d";
    //9.用户主键-uuid关系缓存
    public static final String USER_UUID_KEY = "uuid-userId:%s";
    //10.用户主键-userName关系缓存
    public static final String USER_NAME_KEY = "userNme-userId:%s:%d";
    //11.用户登录信息缓存
    public static final String USER_LOGIN_KEY = "userLoginInfo:%d:%d";
    //12.OAuth-code缓存
    public static final String OAUTH_CODE_KEY = "OAuth2.0:%s";
    //13.OAuth-accessToken缓存
    public static final String OAUTH_ACCESS_TOKEN_KEY = "OAuth2.0-accessToken:%s:%s";
    //14.OAuth-refreshToken缓存
    public static final String OAUTH_REFRESH_TOKEN_KEY = "OAuth2.0-refreshToken:%s:%s";
    //15.UserTokenTerminalKey缓存
    public static final String USER_TOKEN_TERMINAL_KEY = "userToken-terminal:%d:%s";
    //16.企业用户主键-userName关系缓存
    public static final String CORP_USER_KEY = "corpUser-userId:%s:%d";
    //17.2B登录失败次数缓存
    public static final String CORP_LOGIN_FAIL_NUM_KEY = "corpLoginFailNum:%s";
    //17.1.2B登录失败次数缓存
    public static final String CORP_LOGIN_LOCK_KEY = "corpLoginLock:%s";
    //18.2B登录失败验证码缓存
    public static final String CORP_LOGIN_FAIL_CODE_KEY = "corpLoginFailCode:%s";
    //19.2B重置密码验证码缓存
    public static final String CORP_RESET_PWD_CODE_KEY = "corpResetPwdCode:%s";
    //20.注册验证码缓存
    public static final String CORP_REGISTER_CODE_KEY = "corpRegisterCode:%s";
    //21.用户在线Debug缓存
    public static final String ONLINE_DEBUG_KEY = "onlineDebug:%s";
    //22. 用户 smartToken表的缓存(alexa、googleHome使用)
    public static final String SMART_TOKEN_KEY = "smartToken:%d:%d";
    //23. 验证码输入错误次数记录
    public static  final  String VERIFY_CODE_ERROR_NUMB="%d:VerifyCodeErrorNumb:%s";
    //24. 租户信息是否完善标志
    public static  final  String TENANT_PERFECT="tenant:perfectFlag:%d";

    public static String getTenantPerfectFlagKey(Long tenantId) {
        return String.format(TENANT_PERFECT, tenantId);
    }
    
    public static String getSmartTokenKey(Long userId, Integer smartType) {
        return String.format(SMART_TOKEN_KEY, userId, smartType);
    }

    public static String getOnlineDebugKey(String uuid) {
        return String.format(ONLINE_DEBUG_KEY, uuid);
    }

    public static String getAccessTokenKey(String accessToken) {
        return String.format(ACCESS_TOKEN_KEY, accessToken);
    }

    public static String getRefreshTokenKey(String refreshToken) {
        return String.format(REFRESH_TOKEN_KEY, refreshToken);
    }

    public static String getUserTokenKey(Long userId) {
        return String.format(USER_TOKEN_KEY, userId);
    }

    public static String getRegisterCodeKey(String userName) {
        return String.format(REGISTER_CODE_KEY, userName != null ? userName.toLowerCase() : "");
    }
    public static String getVerifyCodeErrorNumbKey(String userName) {
        return String.format(VERIFY_CODE_ERROR_NUMB, getTenantId(),userName);
    }

    public static String getCorpRegisterCodeKey(String userName) {
        return String.format(CORP_REGISTER_CODE_KEY, userName != null ? userName.toLowerCase() : "");
    }

    public static String getResetPwdCodeKey(String userName) {
        return String.format(RESET_PWD_CODE_KEY, userName != null ? userName.toLowerCase() : "", getTenantId());
    }

    public static String getCorpResetPwdCodeKey(String userName) {
        return String.format(CORP_RESET_PWD_CODE_KEY, userName != null ? userName.toLowerCase() : "");
    }

    public static String getLoginFailCodeKey(String userName) {
        return String.format(LOGIN_FAIL_CODE_KEY, userName != null ? userName.toLowerCase() : "", getTenantId());
    }

    public static String getCorpLoginFailCodeKey(String userName) {
        return String.format(CORP_LOGIN_FAIL_CODE_KEY, userName != null ? userName.toLowerCase() : "");
    }

    public static String getLoginFailNumKey(String userName) {
        return String.format(LOGIN_FAIL_NUM_KEY, userName != null ? userName.toLowerCase() : "", getTenantId());
    }

    public static String getLoginFailLockedTime(String userName) {
        return String.format(LOGIN_FAIL_LOCKED_TIME, userName != null ? userName.toLowerCase() : "", getTenantId());
    }


    public static String getCorpLoginFailNumKey(String userName) {
        return String.format(CORP_LOGIN_FAIL_NUM_KEY, userName != null ? userName.toLowerCase() : "");
    }

    public static String getCorpLoginLockKey(String userName) {
        return String.format(CORP_LOGIN_LOCK_KEY, userName != null ? userName.toLowerCase() : "");
    }

    public static String getUserKey(Long userId) {
        return String.format(USER_KEY, userId);
    }

    public static String getUserUuidKey(String uuid) {
        return String.format(USER_UUID_KEY, uuid);
    }

    public static String getUserNameKey(String userName) {
        return String.format(USER_NAME_KEY, userName != null ? userName.toLowerCase() : "", getTenantId());
    }

    public static String getUserLoginKey(Long userId) {
        return String.format(USER_LOGIN_KEY, userId, getTenantId());
    }

    public static String getOauthCodeKey(String code) {
        return String.format(OAUTH_CODE_KEY, code);
    }

    public static String getOauthAccessTokenKey(String type, String token) {
        return String.format(OAUTH_ACCESS_TOKEN_KEY, type, token);
    }

    public static String getOauthRefreshTokenKey(String type, String token) {
        return String.format(OAUTH_REFRESH_TOKEN_KEY, type, token);
    }

    public static String getUserTokenTerminalKey(Long userId, String terminalMark) {
        String mark = terminalMark != null ? terminalMark.toLowerCase() : "";
        return String.format(USER_TOKEN_TERMINAL_KEY, userId, mark);
    }

    private static Long getTenantId() {
        return SaaSContextHolder.currentTenantId();
    }

    public static String getCorpUserNameKey(String userName, Integer userLevel) {
        return String.format(CORP_USER_KEY, userName != null ? userName.toLowerCase() : "", userLevel);
    }
}
