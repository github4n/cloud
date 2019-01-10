package com.iot.device.model.enums;

import com.baomidou.mybatisplus.enums.IEnum;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 11:32 2018/6/28
 * @Modify by:
 */
public enum ParamTypeEnum implements IEnum {
    INT(0, "int"),
    FLOAT(1, "float"),
    BOOL(2, "bool"),
    ENUM(3, "enum"),
    STRING(4, "string");
    private int value;
    private String desc;

    ParamTypeEnum(final int value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static ParamTypeEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        ParamTypeEnum[] enums = ParamTypeEnum.values();
        for (ParamTypeEnum iEnum : enums) {
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
