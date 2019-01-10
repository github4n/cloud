package com.iot.device.model.enums;

import com.baomidou.mybatisplus.enums.IEnum;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 11:32 2018/6/28
 * @Modify by:
 */
public enum DevelopStatusEnum implements IEnum {
    IDLE(0, "未开发"),
    OPERATING(1, "开发中"),
    OPERATED(2, "已上线"),
    RELEASE(3,"发布中");
    private int value;
    private String desc;

    DevelopStatusEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static DevelopStatusEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        DevelopStatusEnum[] statusEnums = DevelopStatusEnum.values();
        for (DevelopStatusEnum status : statusEnums) {
            if ((int) status.getValue() == value) {
                return status;
            }
        }
        return null;
    }

    @Override
    public Serializable getValue() {
        return this.value;
    }

    public String getDesc() {
        return this.desc;
    }

    public int getValueInt(){
        return this.value;
    }
}
