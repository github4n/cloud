package com.iot.user.constant;

import com.iot.common.constant.SystemConstants;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：用户模块
 * 功能描述：模块常量
 * 创建人： mao2080@sina.com
 * 创建时间：2017年4月12日 下午4:31:20
 * 修改人： mao2080@sina.com
 * 修改时间：2017年4月12日 下午4:31:20
 */
public class UserConstants extends SystemConstants {

    /**
     * 用户状态（0-未激活，1-已激活，2-在线，3-离线，4-已冻结，5-已注销）
     */
    public static final Byte USERSTATE_NOT_ACTIVE = 0;
    public static final Byte USERSTATE_ACTIVE = 1;
    public static final Byte USERSTATE_ONLINE = 2;
    public static final Byte USERSTATE_OFFLINE = 3;
    public static final Byte USERSTATE_FROZEN = 4;
    public static final Byte USERSTATE_CANCEL = 5;

    /**
     * 终端类型
     */
    public static final String TERMINALMARK_APP = "app";
    public static final String TERMINALMARK_WEB = "web";

    /**
     * 验证码失效时间（单位：秒）
     */
    public static final Long EXPIRATION_TIME = 10 * 60l;
    /**
     * 登录错误记录失效时间（单位：秒）
     */
    public static final Long LOGIN_FAIL_EXPIRATION_TIME = 60 * 10l;

    /**
     * 2B用户登录错误记录失效时间（单位：秒）
     */
    public static final Long B_LOGIN_FAIL_EXPIRATION_TIME = 90600l;
    
    /**
     * 锁定10分钟
     */
    public static final Long LOCK_10_MINUTES = 600l;
    
    /**
     * 锁定1小时
     */
    public static final Long LOCK_1_HOUR = 3600L;
    
    /**
     * 锁定24小时
     */
    public static final Long LOCK_24_HOURS = 86400L;
    
    /**
     * 请求头参数
     */
    public static final String HEADER_ACCESS_TOKEN = "token";
    public static final String HEADER_TERMINALMARK = "terminal";

    /**
     * 隐藏账号、密码 角色code 角色type
     */
    public static final String HIDE_USER_NAME = "_admin";
    public static final String HIDE_USER_PWD = "iotAdmin";
    public static final String HIDE_USER_ROLE_CODE = "Owner";
    public static final String HIDE_USER_ROLE_TYPE = "Portal";

    // 用户激活redis缓存前缀
    public static final String USER_ACTIVATED = "user-activated:";
}

