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
@TableName("device_remote_control_scene_template")
public class DeviceRemoteControlSceneTemplate {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;//主键
    @TableField("control_template_id")
    private Long controlTemplateId;// device_remote_control_template的id
    @TableField("relation_id")
    private Long relationId;//template表的 id
    @TableField("data_status")
    private Integer dataStatus;//0-无效；1-有效
    @TableField("sort")
    private Integer sort;//排序 升序


    /**
     * 租户ID
     */
    private Long tenantId;
}