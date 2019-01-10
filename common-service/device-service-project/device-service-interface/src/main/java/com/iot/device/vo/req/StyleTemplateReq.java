package com.iot.device.vo.req;

import com.iot.common.beans.SearchParam;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * 样式入参
 */
@Data
@ToString
public class StyleTemplateReq extends SearchParam implements Serializable {

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

    /**
     * 租户ID
     */
    private Long tenantId;


    private ArrayList<Long> filterIds;

    private String searchValues;

    /**
     * 资源链接
     */
    private String resourceLink;

    /**
     * MD5效验
     */
    private String resourceLinkValidation;
}
