package com.iot.device.vo.rsp;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 样式出参
 */
@Data
@ToString
public class StyleTemplateResp implements Serializable {

    private Long id;

    /**
     * 样式名称
     */
    private String name;
    /**
     * 样式唯一标识code
     */
    private String code;
    /**
     * 图片
     */
    private String img;
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

    private Long otherId;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 资源链接
     */
    private String resourceLink;

    /**
     * MD5效验
     */
    private String resourceLinkValidation;

}
