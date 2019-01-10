package com.iot.tree;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Item {
    private Long id;
    private String name;
    private Long parentId;
    private Integer order;
    private String desc;
    private Date createTime;
    private List<Item> children;
}
