package com.iot.boss.enums;

/**
 * 项目名称：cloud
 * 功能描述：查询日期类型
 * 创建人： ouyangjie
 * 创建时间：2018/5/15 16:21
 * 修改人： ouyangjie
 * 修改时间：2018/5/15 16:21
 * 修改描述：
 */
public enum DateTypeEnum {

    CREATE(0,"创建日期"),

    CONFIRM(1,"确认日期"),

    HANDLE(2,"处理中日期"),

    OVER(3,"修复完毕日期");

    private Integer value;

    private String desc;

    DateTypeEnum(Integer value, String desc) {
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

    /**
     * 描述：判断是否属于此枚举
     * @author mao2080@sina.com
     * @created 2018/5/18 9:35
     * @param value
     * @return boolean
     */
    public static boolean contains(int value){
        for(DateTypeEnum typeEnum : DateTypeEnum.values()){
            if(typeEnum.getValue().equals(value)){
                return true;
            }
        }
        return false;
    }


    public static boolean checkIsValid(int value){
        for (DateTypeEnum item: DateTypeEnum.values()) {
            if (value == item.getValue()){
                return true;
            }
        }
        return false;
    }

}
