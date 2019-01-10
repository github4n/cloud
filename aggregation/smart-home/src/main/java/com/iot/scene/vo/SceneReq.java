package com.iot.scene.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@ApiModel
public class SceneReq implements Serializable {
    private static final long serialVersionUID = 840590056656341922L;

    private Long sceneId;
    private String name;
    private String icon;
    @NotBlank
    @ApiModelProperty("用户UUID")
    private String cookieUserId;
    @NotBlank
    @ApiModelProperty("空间ID")
    private Long homeId;

    public Long getSceneId() {
        return sceneId;
    }

    public void setSceneId(Long sceneId) {
        this.sceneId = sceneId;
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

    public String getCookieUserId() {
        return cookieUserId;
    }

    public void setCookieUserId(String cookieUserId) {
        this.cookieUserId = cookieUserId;
    }

    public Long getHomeId() {
        return homeId;
    }

    public void setHomeId(Long homeId) {
        this.homeId = homeId;
    }

}
