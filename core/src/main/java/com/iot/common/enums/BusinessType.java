package com.iot.common.enums;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：常用工具
 * 功能描述：业务类型（用于锁类型枚举）
 * 创建人： mao2080@sina.com
 * 创建时间：2017年4月20日 下午4:31:51
 * 修改人： mao2080@sina.com
 * 修改时间：2017年4月20日 下午4:31:51
 */
public enum BusinessType {

    Demo("0001", "Demo"),
    Push("0002", "Push"),
    SN("0003", "SN");

    /**
     * 业务类型编码
     */
    private String code;

    /**
     * 业务类型名称
     */
    private String name;

    /**
     * 描述：构建业务类型
     *
     * @param code 业务类型编码
     * @param name 业务类型名称
     * @return
     * @author mao2080@sina.com
     * @created 2017年4月10日 下午3:42:57
     * @since
     */
    private BusinessType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static void main(String[] args) {
        System.out.println(BusinessType.Demo.code);
    }

}
