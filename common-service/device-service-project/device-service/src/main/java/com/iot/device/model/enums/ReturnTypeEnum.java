package com.iot.device.model.enums;

import com.baomidou.mybatisplus.enums.IEnum;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 11:32 2018/6/28
 * @Modify by:
 */
public enum ReturnTypeEnum implements IEnum {
    OPTIONAL(0, "array"),
    MANDATORY(1, "object");
    private int value;
    private String desc;

    ReturnTypeEnum(final int value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static ReturnTypeEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        ReturnTypeEnum[] enums = ReturnTypeEnum.values();
        for (ReturnTypeEnum iEnum : enums) {
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
