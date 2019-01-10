package com.iot.control.favorite.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 最喜爱设备及场景表
 * </p>
 *
 * @author lucky
 * @since 2019-01-02
 */
@Data
@ToString
@TableName("favorite")
public class FavoriteEntity extends Model<FavoriteEntity> {

    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return id;
    }

    /**
     * 主键id
     */
    private Long id;
    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Long tenantId;
    /**
     * 主家id
     */
    @TableField("space_id")
    private Long spaceId;
    /**
     * 喜爱的设备uuid或场景id
     */
    @TableField("dev_scene")
    private String devScene;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 排序id
     */
    @TableField("order_id")
    private Integer orderId;
    /**
     * 类型id：类型：1表示设备，2表示场景，3表示autho 关联于dev_scene
     */
    @TableField("type_id")
    private Integer typeId;
    /**
     * 创建者id
     */
    @TableField("create_by")
    private Long createBy;
    /**
     * 更新者id
     */
    @TableField("update_by")
    private Long updateBy;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("updat_time")
    private Date updatTime;


}
