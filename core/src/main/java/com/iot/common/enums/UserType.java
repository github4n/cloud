package com.iot.common.enums;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：常用工具
 * 功能描述：用户类型枚举
 * 创建人： mao2080@sina.com
 * 创建时间： 2017年3月22日 上午13:50:58
 * 修改人： mao2080@sina.com
 * 修改时间： 2017年3月22日 上午13:50:58
 */
public enum UserType {

    UserType01("01", "UserType01", "UserType01 desc"),

    UserType02("02", "UserType02", "UserType02 desc"),

    ROOT("03", "root", "administrator"),

    SUB("04", "sub", "guest");

    /**
     * 用户类型编码
     */
    private String code;

    /**
     * 用户类型名称
     */
    private String name;

    /**
     * 用户类型描述
     */
    private String desc;

    /**
     * 描述：构建用户类型
     *
     * @param code 用户类型编码
     * @param name 用户类型名称
     * @param desc 用户类型描述
     * @return
     * @author mao2080@sina.com
     * @created 2017年3月22日 上午13:50:58
     * @since
     */
    private UserType(String code, String name, String desc) {
        this.code = code;
        this.name = name;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
