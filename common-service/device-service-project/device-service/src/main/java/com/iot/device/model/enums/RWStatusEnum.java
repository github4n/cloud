package com.iot.device.model.enums;

import com.baomidou.mybatisplus.enums.IEnum;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 11:32 2018/6/28
 * @Modify by:
 */
public enum RWStatusEnum implements IEnum {
    ENABLE_RW(0, "可读可写"),
    DISABLE_RW(1, "不可读不可写"),
    DISABLE_W(2, "可读不可写");
    private int value;
    private String desc;

    RWStatusEnum(final int value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static RWStatusEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        RWStatusEnum[] enums = RWStatusEnum.values();
        for (RWStatusEnum iEnum : enums) {
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
