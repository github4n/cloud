package com.iot.enums;

/**
 * 项目名称：cloud
 * 功能描述：数据状态
 * 创建人： yeshiyuan
 * 创建时间：2018/8/6 20:38
 * 修改人： yeshiyuan
 * 修改时间：2018/8/6 20:38
 * 修改描述：
 */
public enum DataStatusEnum {

    VALID(1,"valid"),
    UN_VALID(0,"invalid");

    private Integer code;

    private String desc;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    DataStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
