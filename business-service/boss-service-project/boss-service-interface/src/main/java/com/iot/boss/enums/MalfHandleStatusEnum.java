package com.iot.boss.enums;

/**
 * 项目名称：cloud
 * 功能描述：报障单处理状态
 * 创建人： yeshiyuan
 * 创建时间：2018/5/15 16:21
 * 修改人： yeshiyuan
 * 修改时间：2018/5/15 16:21
 * 修改描述：
 */
public enum MalfHandleStatusEnum {

    CREATE(0,"创建"),

    HANDLE(1,"处理中"),

    DEAL_COMPLETE(2,"处理完成"),

    REPAIR_COMPLETE(3,"修复完毕");

    private Integer value;

    private String desc;

    MalfHandleStatusEnum(Integer value, String desc) {
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
        for(MalfHandleStatusEnum typeEnum : MalfHandleStatusEnum.values()){
            if(typeEnum.getValue().equals(value)){
                return true;
            }
        }
        return false;
    }


    public static boolean checkIsValid(int value){
        for (MalfHandleStatusEnum item: MalfHandleStatusEnum.values()) {
            if (value == item.getValue()){
                return true;
            }
        }
        return false;
    }
}
