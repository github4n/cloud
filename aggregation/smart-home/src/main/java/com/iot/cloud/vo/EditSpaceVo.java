package com.iot.cloud.vo;

import com.iot.vo.UserBaseVo;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EditSpaceVo extends UserBaseVo {

    private Long id;
    private String icon;
    private String position;
    private String name;
    private Long parentId;
    private String locationId;
    private Integer sort; //空间排序
    private Integer defaultSpace;
    private String cookieUserId;
    private String cookieUserToken;
    private Long homeId;
    private Long roomId;
    private String meshName; //mesh网络名称

    private String meshPassword; //mesh网络密码

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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

    public Long getHomeId() {
        return homeId;
    }

    public void setHomeId(Long homeId) {
        this.homeId = homeId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Integer getDefaultSpace() {
        return defaultSpace;
    }

    public void setDefaultSpace(Integer defaultSpace) {
        this.defaultSpace = defaultSpace;
    }

}
