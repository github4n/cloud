package com.iot.building.device.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.ToString;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/29
 * 遥控器键关联的场景表
 */
@Data
@ToString
@TableName("device_remote_control_scene")
public class DeviceRemoteControlScene {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;//主键
    @TableField("control_id")
    private Long controlId;// device_remote_control_template的id
    @TableField("relation_id")
    private Long relationId;//template表的 id
    @TableField("sort")
    private Integer sort;//排序 升序


    /**
     * 租户ID
     */
    private Long tenantId;

}