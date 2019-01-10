package com.iot.tenant.vo.req.org;

import lombok.Data;

/**
 * 描述：分页获取组织请求
 * 创建人：LaiGuiMing
 * 创建时间：2019/01/04 17:20
 */
@Data
public class GetOrgByPageReq {
    private String name;
    private Long parentId;
    private Integer type;
    private Integer pageNum;
    private Integer pageSize;
}
