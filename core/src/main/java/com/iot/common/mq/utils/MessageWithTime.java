package com.iot.common.mq.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by littlersmall on 2018/5/16.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class MessageWithTime {
    private long id;
    private long time;
    private Object message;
}