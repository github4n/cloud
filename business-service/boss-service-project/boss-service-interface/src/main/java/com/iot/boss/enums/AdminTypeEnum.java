package com.iot.boss.enums;

/**
 * 项目名称：cloud
 * 功能描述：管理员类型
 * 创建人： yeshiyuan
 * 创建时间：2018/5/15 19:46
 * 修改人： yeshiyuan
 * 修改时间：2018/5/15 19:46
 * 修改描述：
 */
public enum AdminTypeEnum {

    SUPER_ADMIN(0,"超级管理员"),

    COMMOM_ADMIN(1,"普通管理员"),

    OPERAT_ADMIN(2,"运维人员");

    private Integer value;

    private String desc;

    AdminTypeEnum(Integer value, String desc) {
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
        for(AdminTypeEnum typeEnum : AdminTypeEnum.values()){
            if(typeEnum.getValue().equals(value)){
                return true;
            }
        }
        return false;
    }


    public static boolean checkIsValid(int value){
        for (AdminTypeEnum item: AdminTypeEnum.values()) {
            if (value == item.getValue()){
                return true;
            }
        }
        return false;
    }
}
