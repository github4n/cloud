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
 * 类型-样式表
 */
@Data
@ToString
@TableName("device_type_to_style")
public class DeviceTypeToStyle extends Model<DeviceTypeToStyle> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 类型id
     */
    @TableField("device_type_id")
    private Long deviceTypeId;

    /**
     * 样式模板id
     */
    @TableField("style_template_id")
    private Long styleTemplateId;

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
    @TableField("description")
    private String description;

    /**
     * 租户ID
     */
    private Long tenantId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
