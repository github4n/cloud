package com.iot.device.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 模组-方法-to-参数表
 * </p>
 *
 * @author lucky
 * @since 2018-06-28
 */
@Data
@ToString
@TableName("module_action_to_property")
public class ModuleActionToProperty extends Model<ModuleActionToProperty> {

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
     * 方法id
     */
    @TableField("module_action_id")
    private Long moduleActionId;
    /**
     * 属性id
     */
    @TableField("module_property_id")
    private Long modulePropertyId;
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

    @TableField("param_type")
    @ApiModelProperty(value = "property类型(0:入参 1：出参")
    private Integer paramType;


    //遗漏 属性状态，0:可选,1:必选  by xfz
    @TableField(exist = false)
    private Integer status;

    protected Serializable pkVal() {
        return this.id;
    }


}
