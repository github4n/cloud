package com.iot.building.device.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/29
 * 遥控器模板主表
 */
@Data
@ToString
@TableName("device_remote_template")
public class DeviceRemoteTemplate {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;///主键
    @TableField("tenant_id")
    private Long tenantId;/// '租户ID',
    @TableField("name")
    private String name;//'遥控器模板名称',
    @TableField("business_type_id")
    private Long businessTypeId;//'业务类型',关联 device_business_type 表id
    @TableField("type")
    private Long type;///遥控器类型 device_remote_type id
    @TableField("create_by")
    private String createBy;//'创建人',
    @TableField("create_time")
    private Date createTime;//'创建时间',
    @TableField("update_by")
    private String updateBy;// '修改人',
    @TableField("update_time")
    private Date updateTime;//'修改时间',
    @TableField("data_status")
    private Integer dataStatus;//0-无效；1-有效

}
