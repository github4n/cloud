package com.iot.boss.enums;

/**
 * 项目名称：cloud
 * 功能描述：报障单处理类型
 * 创建人： yeshiyuan
 * 创建时间：2018/5/15 16:21
 * 修改人： yeshiyuan
 * 修改时间：2018/5/15 16:21
 * 修改描述：
 */
public enum MalfHandleTypeEnum {
    //0:派单完毕 1：确认故障  2：处理完毕 3：确认已修复 4：确认未修复 5:确认是问题

    DISPATCH_COMPLETE(0,"派单完毕"),

    CONFIRM_MALF(1,"确认故障"),

    HANDLE_COMPLETE(2,"处理完毕"),

    REPAIR_COMPLETE(3,"确认已修复"),

    REPAIR_NO_COMPLETE(4,"确认未修复"),

    CONFIRM_IS_PROPLEM(5,"确认是问题")

    ;

    private Integer value;

    private String desc;

    MalfHandleTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static String getDescByValue(Integer value){
        String str = null;
        for ( MalfHandleTypeEnum malfHandleTypeEnum: MalfHandleTypeEnum.values()) {
            if (value==malfHandleTypeEnum.getValue()){
                str = malfHandleTypeEnum.getDesc();
                break;
            }
        }
        return str;
    }
}
