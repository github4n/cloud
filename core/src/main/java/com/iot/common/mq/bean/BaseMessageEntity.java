package com.iot.common.mq.bean;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 17:58 2018/8/31
 * @Modify by:
 */
@Data
@ToString
public class BaseMessageEntity implements Serializable {

    public Long tenantId;

    public String message;

    public String logRequestId;

    public Date createTime = new Date();


}
