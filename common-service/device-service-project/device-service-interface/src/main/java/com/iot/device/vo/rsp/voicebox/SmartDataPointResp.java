package com.iot.device.vo.rsp.voicebox;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/10/29 10:59
 * @Modify by:
 */

@Data
@ToString
public class SmartDataPointResp implements Serializable {

    private static final long serialVersionUID = 2025580783894328456L;


    private Long id;

    private Long dataPointId;

    private String propertyCode;

    private String smartCode;

    private Integer smart;

    private Long createBy;

    private Date createTime;

    private Long updateBy;

    private Date updateTime;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 模组-属性 id
     */
    private Long propertyId;
}
