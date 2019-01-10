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
 * 遥控器模板详情表
 */
@Data
@ToString
@TableName("device_remote_control_template")
public class DeviceRemoteControlTemplate {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("business_type_id")
    private Long businessTypeId;//设备类型ID
    @TableField("type")
    private String type;// 参数 GROUP 组控；SCENE 情景；SINGLE 单控
    @TableField("function")
    private String function;//功能
    @TableField("remote_id")
    private Long remoteId;// 遥控器模板id  device_remote_template id
    @TableField("press")
    private Integer press;// 按键类型   1短按  2长按
    @TableField("key_code")
    private String keyCode;//
    @TableField("default_value")
    private String defaultValue;//默认值
    @TableField("event_status")
    private String eventStatus;//pressed/released/held down  短按/长按释放/长按
    @TableField("data_status")
    private Integer dataStatus;//0-无效；1-有效

    /**
     * 租户ID
     */
    private Long tenantId;
}