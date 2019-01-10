package com.iot.ifttt.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * 子规则表
 * </p>
 *
 * @author laiguiming
 * @since 2018-09-29
 */
public class AppletItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 程序主键
     */
    private Long appletId;
    /**
     * this/that主键
     */
    private Long eventId;
    /**
     * 规则体
     */
    private String json;

    private String type;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppletId() {
        return appletId;
    }

    public void setAppletId(Long appletId) {
        this.appletId = appletId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
