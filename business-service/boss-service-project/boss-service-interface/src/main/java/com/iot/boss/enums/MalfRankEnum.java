package com.iot.boss.enums;

/**
 * 项目名称：cloud
 * 功能描述：报障单等级
 * 创建人： ouyangjie
 * 创建时间：2018/5/15 16:21
 * 修改人： ouyangjie
 * 修改时间：2018/5/15 16:21
 * 修改描述：
 */
public enum MalfRankEnum {

    NOTPROBLEM(0,"非问题"),

    MILD(1,"轻度"),

    GENERAL(2,"普通"),

    URGENT(3,"紧急");

    private Integer value;

    private String desc;

    MalfRankEnum(Integer value, String desc) {
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
        for(MalfRankEnum typeEnum : MalfRankEnum.values()){
            if(typeEnum.getValue().equals(value)){
                return true;
            }
        }
        return false;
    }


    public static boolean checkIsValid(int value){
        for (MalfRankEnum item: MalfRankEnum.values()) {
            if (value == item.getValue()){
                return true;
            }
        }
        return false;
    }
}
