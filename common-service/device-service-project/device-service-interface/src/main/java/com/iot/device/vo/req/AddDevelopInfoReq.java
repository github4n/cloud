package com.iot.device.vo.req;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 9:45 2018/6/29
 * @Modify by:
 */
@Data
@ToString
public class AddDevelopInfoReq implements Serializable {
    private Long id;
    /**
     * 开发者类型：0,开发者，1开发组
     */
    private Integer type;

    private String name;

    private String code;

    private String description;

    private Long tenantId;

    private Long createBy;
    /**
     * 修改人
     */
    private Long updateBy;
}
