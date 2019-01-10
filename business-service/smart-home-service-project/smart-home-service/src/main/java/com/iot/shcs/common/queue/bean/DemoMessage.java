package com.iot.shcs.common.queue.bean;

import com.iot.common.mq.bean.BaseMessageEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 9:24 2018/8/16
 * @Modify by:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DemoMessage extends BaseMessageEntity implements Serializable {

    private Date createTime;

    private String name;

    private String createBy;

    private String content;

}
