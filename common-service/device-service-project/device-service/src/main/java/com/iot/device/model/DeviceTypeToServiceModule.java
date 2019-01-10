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
 * 设备类型对应模组表
 * </p>
 *
 * @author lucky
 * @since 2018-07-03
 */
@Data
@ToString
@TableName("device_type_to_service_module")
public class DeviceTypeToServiceModule extends Model<DeviceTypeToServiceModule> {

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
     * 设备类型id
     */
    @TableField("device_type_id")
    private Long deviceTypeId;
    /**
     * 模组id
     */
    @TableField("service_module_id")
    private Long serviceModuleId;
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

    private Integer status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
