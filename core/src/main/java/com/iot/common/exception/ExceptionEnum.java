package com.iot.common.exception;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：常用工具
 * 功能描述：异常枚举类，只对组件划分异常代码范围，各组件定义各组件的异常枚举，整个云平台基础组件范围（20000~40000）
 * 创建人： mao2080@sina.com
 * 创建时间： 2017年3月20日 下午17:13:00
 * 修改人： mao2080@sina.com
 * 修改时间： 2017年3月20日 下午17:13:00
 */
public enum ExceptionEnum implements IBusinessException {

    /**
     * 成功
     **/
    SUCCESS(20000, "Success"),

    /**
     * 未知异常
     **/
    UNKNOWN_EXCEPTION(20001, "Unknown exception."),

    /**
     * 入参为空
     **/
    ARGUMENT_EMPTY_EXCEPTION(20002, "Argument cannot be null or empty."),

    /**
     * 入参残缺
     **/
    ARGUMENT_SIGNATURE_EXCEPTION(20003, "Argument signature exception."),

    /**
     * 权限不足
     **/
    ACCESS_DENIED_EXCEPTION(20004, "Access denied exception."),

    /**
     * 配置文件key重复
     **/
    PROPERTY_REPEAT_EXCEPTION(20020, "Property Key Repeat."),

    /**
     * 邮件地址为空
     **/
    EMAIL_IS_ENPTY(20021, "Email address is empty."),

    /**
     * 邮件地址非法
     **/
    EMAIL_FORMAT_INCORRECT(20022, "Incorrect email address format."),

    /**
     * 对象转json字符串异常
     **/
    OBJECT_TO_JSONSTRING_ERROR(20023, "Object to jsonString error."),

    /**
     * json字符串转对象异常
     **/
    JSONSTRING_TO_OBJECT_ERROR(20024, "jsonString to Object error."),

    /**
     * MD5加密失败（MD5）
     **/
    MD5_ENCRYPTION_FAILED(20025, "Md5 encryption failed."),

    /**
     * jwt令牌已过期
     **/
    JWT_EXPIRED_EXCEPTION(20026, "Jwt expired exception."),

    /**
     * jwt令牌残缺
     **/
    JWT_SIGNATURE_EXCEPTION(20027, "Jwt signature exception."),

    /**
     * jwt令牌格式错误
     **/
    JWT_MALFORMED_EXCEPTION(20028, "Jwt malformed exception."),

    /**
     * jwt令牌参数为空
     **/
    JWT_ARGUMENT_EMPTY_EXCEPTION(20029, "Jwt argument cannot be null or empty."),

    /**
     * jwt令牌参数id为空
     **/
    JWT_ARGUMENT_ID_EMPTY_EXCEPTION(20030, "Jwt argument id cannot be null or empty."),

    /**
     * jwt令牌参数subject为空
     **/
    JWT_ARGUMENT_SUBJECT_EMPTY_EXCEPTION(20031, "Jwt argument subject cannot be null or empty."),

    /**
     * aes-key不能为空
     **/
    AES_KEY_EMPTY_EXCEPTION(20032, "Aes key cannot be null or empty."),

    /**
     * aes-key必须为8的整数倍
     **/
    AES_KEY_MALFORMED_EXCEPTION(20033, "Aes key Must be an integer multiple of 8."),

    /**
     * AES加密失败
     **/
    AES_ENCRYPTION_FAILED(20034, "Md5 encryption failed."),

    /**
     * AES解密失败
     **/
    AES_DECRYPT_FAILED(20035, "Md5 decrypt failed."),

    /**
     * RSA-key不能为空
     **/
    RSA_PUB_KEY_EMPTY_EXCEPTION(20036, "RSA publicKey cannot be null or empty."),

    /**
     * RSA-key不能为空
     **/
    RSA_PRI_KEY_EMPTY_EXCEPTION(20037, "RSA privateKey cannot be null or empty."),

    /**
     * RSA加密失败
     **/
    RSA_ENCRYPTION_FAILED(20038, "RSA encryption failed."),

    /**
     * RSA解密失败
     **/
    RSA_DECRYPT_FAILED(20039, "RSA decrypt failed."),

    /**
     * RSA公钥生成失败
     **/
    RSA_PUB_KEY_GENERATE_EXCEPTION(20040, "RSA publicKey generate failed."),

    /**
     * RSA私钥生成失败
     **/
    RSA_PRI_KEY_GENERATE_EXCEPTION(20041, "RSA privateKey generate failed."),

    /**
     * UUID-zkclient连接失败
     **/
    UUID_ZKCLIENT_CONNECTION_FAILED(20042, "Unable to connect to zookeeper server."),

    /**
     * UUID-zkclient未初始化
     **/
    UUID_ZKCLIENT_UNINITIALIZED(20043, "ZkClient uninitialized."),

    /**
     * UUID生成失败
     **/
    UUID_GENERATE_EXCEPTION(20044, "UUID generate failed."),

    /**
     * UUID长度错误
     **/
    UUID_LENGTH_EXCEPTION(20045, "UUID length must between 10 and 42."),

    /**
     * UUID数量错误
     **/
    UUID_COUNT_EXCEPTION(20046, "UUID count must between 1 and 100."),

    /**
     * 分页构造出错
     **/
    DB_TABLE_NAME_IS_EMPTY(20047, "Pagenation Constructor exception tableName is empty."),

    /**
     * ZK创建lock失败
     **/
    ZK_LOCK_CREATE_FAIL(20048, "ZKLockUtil exception create lock fail."),

    /**
     * ZK-server为空
     **/
    ZK_LOCK_ZK_SERVER_IS_EMPTY(20049, "ZKLockUtil exception server is empty."),

    /**
     * ZK-业务类型为空
     **/
    ZK_LOCK_BUSINESS_TYPE_IS_EMPTY(20050, "ZKLockUtil exception BusinessType is empty."),

    /**
     * 厂商代码不正确
     **/
    VENDER_CODE_INCORRECT(20051, "Vender code incorrect."),

	/**
     * 手机号非法
     **/
    PHONE_FORMAT_INCORRECT(20052, "Incorrect phone format."),
    
	/**
     * 字符串超长
     **/
    STRING_TOO_LONG(20053, "String is too long."),
    
	/**
     * 字符串太短
     **/
    STRING_TOO_SHORT(20054, "String is too short."),


    TENANT_ID_IS_NULL(20055,"tanant id is null");
    
    /**
     * 异常代码
     */
    private int code;

    /**
     * 异常描述
     */
    private String messageKey;

    ExceptionEnum(String messageKey) {
        this.code = 0;
        this.messageKey = messageKey;
    }

    /**
     * 描述：构建异常
     *
     * @param code       错误代码
     * @param messageKey 错误描述
     * @return
     * @author mao2080@sina.com
     * @created 2017年3月21日 上午10:50:58
     * @since
     */
    ExceptionEnum(Integer code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

    public int getCode() {
        return code;
    }

    public String getMessageKey() {
        return messageKey;
    }

}
