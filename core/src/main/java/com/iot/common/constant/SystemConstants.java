package com.iot.common.constant;


import java.util.Locale;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：常用工具
 * 功能描述：系统常量
 * 创建人： mao2080@sina.com
 * 创建时间：2017年3月24日 下午2:04:44
 * 修改人： mao2080@sina.com
 * 修改时间：2017年3月24日 下午2:04:44
 */
public class SystemConstants {

    /**
     * spring加载项地址
     */
    public static final String[] CONFIG_RESOUCES = {"spring/spring-context.xml"};

    /**
     * 分页大小
     */
    public static final Integer PAGE_SIZE = 10;

    /**
     * 查询参数标识
     */
    public static final String SEARCH_WORD = "search_";

    /**
     * 拼接表名
     */
    public static final String TABLE_NAME = "tableName";

    /**
     * 拼接分页开头
     */
    public static final String PAGE_SQL_STA = "PAGE_SQL_STA";

    /**
     * 拼接分页结尾
     */
    public static final String PAGE_SQL_END = "PAGE_SQL_END";

    /**
     * Zookeeper锁根目录
     */
    public static final String ZK_BASE_LOCK_PATH = "/Locks/";

    /**
     * ZookeeperUUID基础节点
     */
    public static final String ZK_BASE_NODE = "/GUIDService/";

    /**
     * AWS-token过期时间
     */
    public static final long TOKEN_DEFAULT_DURATION = 7200; //86400;
    /**
     * refreshtoken过期时间15天
     */
    public static final long REFRESHTOKEN_DEFAULT_DURATION = 86400 * 15; //86400;

    /**
     * 验证码过期时间
     */
    public static final long VERIFYCODE_DEFAULT_DURATION = 21600;

    /**
     * AWS-token过期时间
     */
    public static final long KEEPALIVE_DEFAULT_DURATION = 30;

    /**
     * App账号分享-邮件方式-过期时间
     */
    public static final long APPUSERSHARE_EMAIL = 900;

    public static final String HEADER_TENANT_ID = "tenant_id";

    public static final String HEADER_USER_ID = "user_id";

    public static final String HEADER_LOG_ID = "iot_log_id";

    public static final String LOG_REQUEST_ID = "logRequestId";

    /**
     * 国际化请求参数 post get 参数增加 lang属性=zh_CN  en_US
     */
    public static final String LOCALE_REQUEST_PARAM_LANG = "lang";

    public static final String HEADER_LOCALE_LANGUAGE = "Accept-Language";

    public static final String HEADER_ACTIVE_LANGUAGE = "active-language";
    /**
     * 系统默认国际化标准语言 中国
     */
    public static final Locale DEFAULT_LOCALE_LANGUAGE = Locale.CHINA;

    /**
     * 请求头 拦截 日志跟踪
     */
    public static final String HEADER_TRACE_LOG_KEY = "iot_traceLog";

    /**
     * 平台级别的租户ID
     */
    public static final Long BOSS_TENANT = -1L;

    /**默认临时文件夹路径*/
    public static final String DEFAULT_UPLOAD_PATH = "/data/upload_tmp";

    /**系统异常提示*/
    public static final String SYSTEM_ERROR_TIPS = "System error, please try again.";

}
