package com.iot.airswitch.constant;

/**
 * @Author: Xieby
 * @Date: 2018/10/16
 * @Description: 通讯模块电量类型
 */
public enum CommGwTypeEnum {

    UNIPHASE("80", "单相节点"),
    TRIPHASE("84", "三相节点主路"),
    TRIPHASE_A("85", "三相节点A相"),
    TRIPHASE_B("86", "三相节点B相"),
    TRIPHASE_C("87", "三相节点C相"),
    PLASTIC_CASE("94", "塑壳主路"),
    PLASTIC_CASE_A("95", "塑壳A相"),
    PLASTIC_CASE_B("96", "塑壳B相"),
    PLASTIC_CASE_C("97", "塑壳C相");

    public String code;
    public String desc;

    CommGwTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 判断是否是塑壳产品
     */
    public static boolean isPlasticCase (String type) {
        if (CommGwTypeEnum.PLASTIC_CASE.code.equals(type) ||
            CommGwTypeEnum.PLASTIC_CASE_A.code.equals(type) ||
            CommGwTypeEnum.PLASTIC_CASE_B.code.equals(type) ||
            CommGwTypeEnum.PLASTIC_CASE_C.code.equals(type)) {
            return true;
        }
        return false;
    }
}
