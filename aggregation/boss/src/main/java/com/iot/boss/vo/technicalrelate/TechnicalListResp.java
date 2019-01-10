package com.iot.boss.vo.technicalrelate;

import io.swagger.annotations.ApiModel;

/**
 * 项目名称：cloud
 * 功能描述：技术方案信息出参
 * 创建人： yeshiyuan
 * 创建时间：2018/10/15 17:41
 * 修改人： yeshiyuan
 * 修改时间：2018/10/15 17:41
 * 修改描述：
 */
@ApiModel(description = "技术方案列表出参")
public class TechnicalListResp {

    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TechnicalListResp() {
    }

    public TechnicalListResp(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
