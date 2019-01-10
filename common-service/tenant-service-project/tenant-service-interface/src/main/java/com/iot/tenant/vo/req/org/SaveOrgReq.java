package com.iot.tenant.vo.req.org;

import lombok.Data;

/**
 * 描述：保存组织请求
 * 创建人：LaiGuiMing
 * 创建时间：2019/01/04 17:11
 */
@Data
public class SaveOrgReq {
    private Long id;
    private String name;
    private Long tenantId;
    private String description;
    private Long parentId;
    private Integer orderNum;
    private Integer type;
}
