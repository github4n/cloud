package com.iot.device.enums;

/**
 * 项目名称：cloud
 * 功能描述：操作步骤记录类型
 * 创建人： yeshiyuan
 * 创建时间：2018/9/11 15:17
 * 修改人： yeshiyuan
 * 修改时间：2018/9/11 15:17
 * 修改描述：
 */
public enum OperateStepRecordEnum {

    PRODUCT("product", "产品"),
    APPLICATION("application", "应用");


    private String key;

    private String desc;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    OperateStepRecordEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public static boolean check(String key){
        if (key == null || "".equals(key)) {
            return false;
        }
        for (OperateStepRecordEnum recordEnum : OperateStepRecordEnum.values()){
            if (recordEnum.getKey().equals(key)){
                return true;
            }
        }
        return false;
    }
}
