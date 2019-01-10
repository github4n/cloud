package com.iot.ifttt.vo;

import com.iot.common.mq.bean.BaseMessageEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 描述：动作消息
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/27 10:38
 */
@Data
public class ActionMessage extends BaseMessageEntity implements Serializable {

    private String route; // 2C/2B

    public Long createTenantId() {
        return getTenantId();
    }
}
