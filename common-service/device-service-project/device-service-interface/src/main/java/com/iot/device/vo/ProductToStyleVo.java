package com.iot.device.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class ProductToStyleVo implements Serializable{

    private static final long serialVersionUID = 1L;


    private Long id;

    /**
     * 产品id
     */

    private Long productId;

    /**
     * 样式模板id
     */

    private Long styleTemplateId;

    /**
     * 创建人
     */

    private Long createBy;

    /**
     * 创建时间
     */

    private Date createTime;

    /**
     * 修改人
     */

    private Long updateBy;

    /**
     * 修改时间
     */

    private Date updateTime;

    /**
     * 描述
     */

    private String description;


    private String name;

    private String code;

    private String img;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 资源链接
     */
    private String resourceLink;
}
