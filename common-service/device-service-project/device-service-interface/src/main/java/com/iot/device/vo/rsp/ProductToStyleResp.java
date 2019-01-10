package com.iot.device.vo.rsp;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class ProductToStyleResp {

    private Long id;
    private Long productId;
    private Long styleTemplateId;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
    private String description;

    private String name;

    private String code;

    private String img;
    /**
     * 租户ID
     */
    private Long tenantId;


    private String resourceLink;

}
