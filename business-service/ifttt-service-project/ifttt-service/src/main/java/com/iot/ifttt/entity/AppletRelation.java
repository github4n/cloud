package com.iot.ifttt.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * 程序关联表
 * </p>
 *
 * @author laiguiming
 * @since 2018-09-29
 */
public class AppletRelation implements Serializable {

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
     * 关联key devId/sunny/spaceId等
     */
    private String relationKey;
    /**
     * 类型 1 dev_status 2 sunny 3 space
     */
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

    public String getRelationKey() {
        return relationKey;
    }

    public void setRelationKey(String relationKey) {
        this.relationKey = relationKey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AppletRelation{" +
                ", id=" + id +
                ", appletId=" + appletId +
                ", relationKey=" + relationKey +
                ", type=" + type +
                "}";
    }
}
