package com.iot.device.vo.req;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 10:05 2018/6/29
 * @Modify by:
 */
public class DevelopInfoListResp implements Serializable {

    private Long id;
    /**
     * 开发者类型：0,开发者，1开发组
     */
    private Integer type;

    private String name;

    private String code;

    private String description;

    private String createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
