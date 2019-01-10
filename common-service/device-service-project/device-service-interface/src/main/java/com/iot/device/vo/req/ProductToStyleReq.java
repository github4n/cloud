package com.iot.device.vo.req;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class ProductToStyleReq {

    private Long id;
    private Long productId;
    private Long styleTemplateId;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
    private String description;
    /**
     * 租户ID
     */
    private Long tenantId;

}
