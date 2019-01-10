package com.iot.device.model.enums;

import com.baomidou.mybatisplus.enums.IEnum;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 11:32 2018/6/28
 * @Modify by:
 */
public enum PropertyStatusEnum implements IEnum {
    OPTIONAL(0, "可选"),
    MANDATORY(1, "必选");
    private int value;
    private String desc;

    PropertyStatusEnum(final int value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static PropertyStatusEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        PropertyStatusEnum[] enums = PropertyStatusEnum.values();
        for (PropertyStatusEnum iEnum : enums) {
            if ((int) iEnum.getValue() == value) {
                return iEnum;
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
}
