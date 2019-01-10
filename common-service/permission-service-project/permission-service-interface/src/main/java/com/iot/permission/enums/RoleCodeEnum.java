package com.iot.permission.enums;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：权限
 * 功能描述：角色编码枚举
 * 创建人： maochengyuan
 * 创建时间：2018/7/14 10:28
 * 修改人： maochengyuan
 * 修改时间：2018/7/14 10:28
 * 修改描述：
 */
public enum RoleCodeEnum {

    Owner("Owner", "拥有者"),

    Manager("Manager", "管理者"),

    ProductManager("ProductManager", "产品经理"),

    APPDeveloper("APPDeveloper", "App开发"),

    EmbeddedDeveloper("EmbeddedDeveloper", "嵌入式开发"),

    Tester("Tester", "测试"),

    Operator("Operator", "运营"),

    DataQueryer("DataQueryer", "数据查询"),

	;

    RoleCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

	/** 类型编码 */
    private String code;

	/** 类型描述 */
    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
