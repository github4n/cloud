package com.iot.device.vo.req.voicebox;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/10/29 9:14
 * @Modify by:
 */

@Data
@ToString
public class SmartDataPointReq implements Serializable {

    private static final long serialVersionUID = 2025580783894328456L;

    private String smartCode;

    private Integer smart;

}
