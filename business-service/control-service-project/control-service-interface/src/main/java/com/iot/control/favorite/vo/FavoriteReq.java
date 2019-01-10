package com.iot.control.favorite.vo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class FavoriteReq {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 租户id
     */
    private Long tenantId;
    /**
     * 主家id
     */
    private Long spaceId;
    /**
     * 喜爱的设备uuid或场景id
     */
    private String devScene;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 排序id
     */
    private Integer orderId;
    /**
     * 类型id：类型：1表示设备，2表示场景，3表示autho 关联于dev_scene
     */
    private Integer typeId;
    /**
     * 创建者id
     */
    private Long createBy;
    /**
     * 更新者id
     */
    private Long updateBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updatTime;
}
