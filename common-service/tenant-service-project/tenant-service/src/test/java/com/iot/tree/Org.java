package com.iot.tree;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 组织对象
 */
@Data
public class Org implements Serializable {
    private Long id;
    private String name;
    private Long parentId;
    private Integer type;
    private String path;
    private Integer order;
    private String desc;
    private Long tenantId;
    private Date createTime;
}
