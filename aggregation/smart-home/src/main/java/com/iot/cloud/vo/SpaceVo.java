package com.iot.cloud.vo;

import com.iot.vo.UserBaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("家庭房间模型")
public class SpaceVo extends UserBaseVo {

    private String id;

    private String icon;

    private String position;

    private String name;

    private Long parentId;

    private String locationId;

    @ApiModelProperty(value = "家庭HOME,房间ROOM", allowableValues = "HOME, ROOM")
    private String type;

    @ApiModelProperty(value = "空间排序")
    private Integer sort;

    private Integer defaultSpace = 0;

    private String cookieUserId;

    private String cookieUserToken;

    private Long homeId;

    private Long orgId;

    private int pageSize;

    private int offset;

    private String meshName;

    private String meshPassword;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getHomeId() {
        return homeId;
    }

    public void setHomeId(Long homeId) {
        this.homeId = homeId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getDefaultSpace() {
        return defaultSpace;
    }

    public void setDefaultSpace(Integer defaultSpace) {
        this.defaultSpace = defaultSpace;
    }

    public String getCookieUserId() {
        return cookieUserId;
    }

    public void setCookieUserId(String cookieUserId) {
        this.cookieUserId = cookieUserId;
    }

    public String getCookieUserToken() {
        return cookieUserToken;
    }

    public void setCookieUserToken(String cookieUserToken) {
        this.cookieUserToken = cookieUserToken;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getMeshName() {
        return meshName;
    }

    public void setMeshName(String meshName) {
        this.meshName = meshName;
    }

    public String getMeshPassword() {
        return meshPassword;
    }

    public void setMeshPassword(String meshPassword) {
        this.meshPassword = meshPassword;
    }
}
