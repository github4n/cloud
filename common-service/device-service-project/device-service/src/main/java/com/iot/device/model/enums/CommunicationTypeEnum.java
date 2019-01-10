package com.iot.device.model.enums;

import com.baomidou.mybatisplus.enums.IEnum;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton: 通讯方案类型
 * @Date: 21:10 2018/7/2
 * @Modify by:
 */
public enum CommunicationTypeEnum implements IEnum {
    WIFI(0, "WIFI"),
    BLUETOOTH(1, "蓝牙"),
    GETWAY(2, "网关"),
    IPC(2, "IPC");
    private int value;
    private String desc;

    CommunicationTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static CommunicationTypeEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        CommunicationTypeEnum[] enums = CommunicationTypeEnum.values();
        for (CommunicationTypeEnum iEnum : enums) {
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
