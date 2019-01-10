package com.iot.portal.web.vo;

/**
 * 项目名称：cloud
 * 功能描述：技术方案
 * 创建人： yeshiyuan
 * 创建时间：2018/10/16 11:46
 * 修改人： yeshiyuan
 * 修改时间：2018/10/16 11:46
 * 修改描述：
 */
public class PortalTechnicalResp {

    private Long id;

    private String name;

    private String icon;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public PortalTechnicalResp() {
    }

    public PortalTechnicalResp(Long id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }
}
