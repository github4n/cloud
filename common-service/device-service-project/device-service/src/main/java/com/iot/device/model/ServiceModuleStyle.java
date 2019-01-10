package com.iot.device.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 模组-样式表
 * </p>
 *
 * @author lucky
 * @since 2018-06-28
 */
@Data
@ToString
@TableName("service_module_style")
public class ServiceModuleStyle extends Model<ServiceModuleStyle> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private Long tenantId;
    /**
     * 模组id
     */
    @TableField("service_module_id")
    private Long serviceModuleId;
    /**
     * 模组样式名称
     */
    private String name;
    /**
     * 模组样式唯一标识code
     */
    private String code;
    /**
     * 缩略图
     */
    private String thumbnail;
    /**
     * 创建人
     */
    @TableField("create_by")
    private Long createBy;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 修改人
     */
    @TableField("update_by")
    private Long updateBy;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 描述
     */
    private String description;

    protected Serializable pkVal() {
        return this.id;
    }

}
