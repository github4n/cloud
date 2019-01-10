package com.iot.tenant.vo.resp.org;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 描述：组织应答类
 * 创建人：LaiGuiMing
 * 创建时间：2019/01/04 17:37
 */
@Data
public class OrgResp {
    private Long id;
    private String name;
    private Long tenantId;
    private Date createTime;
    private String description;
    private Date updateTime;
    private Long parentId;
    private Integer orderNum;
    private String path;
    private Integer type;
    private List<OrgResp> children;
}
