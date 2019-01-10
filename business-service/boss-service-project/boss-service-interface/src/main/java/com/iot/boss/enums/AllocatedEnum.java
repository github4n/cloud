package com.iot.boss.enums;

/**
 * 项目名称：cloud
 * 功能描述：报障单处理状态
 * 创建人：490485964@qq.com
 * 创建时间：2018/5/15 16:21
 * 修改人： 490485964@qq.com
 * 修改时间：2018/5/15 16:21
 * 修改描述：
 */
public enum AllocatedEnum {
    ALLOCATEN(0,"否"),
    ALLOCATEY(1,"是");


    private Integer value;

    private String desc;

    AllocatedEnum(Integer value, String desc) {
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
}
