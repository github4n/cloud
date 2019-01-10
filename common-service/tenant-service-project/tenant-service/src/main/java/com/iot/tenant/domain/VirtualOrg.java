package com.iot.tenant.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 租户-组织表
 * </p>
 *
 * @author lucky
 * @since 2018-04-26
 */
@Data
@TableName("virtual_org")
public class VirtualOrg extends Model<VirtualOrg> {


    public static final String TABLE_NAME = "virtual_org";

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Long tenantId;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 描述
     */
    private String description;

    @TableField("update_time")
    private Date updateTime;

    @TableField("parent_id")
    private Long parentId;

    @TableField("order_num")
    private Integer orderNum;

    private String path;

    private Integer type;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
