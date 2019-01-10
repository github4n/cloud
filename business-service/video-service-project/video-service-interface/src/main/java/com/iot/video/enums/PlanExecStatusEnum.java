package com.iot.video.enums;

/**
 * 项目名称：cloud
 * 功能描述：计划执行状态枚举类
 * 创建人： yeshiyuan
 * 创建时间：2018/9/4 14:05
 * 修改人： yeshiyuan
 * 修改时间：2018/9/4 14:05
 * 修改描述：
 */
public enum PlanExecStatusEnum {
    CLOSE(0, "计划关闭"),
    OPEN(1, "计划开启"),
    EXPIRE(2,"计划过期"),
    UN_EFFECT(3, "计划失效");

    PlanExecStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /** 类型编码 */
    private Integer code;

    /** 描述 */
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
}
