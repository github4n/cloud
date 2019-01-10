package com.iot.building.device.vo;


import lombok.Data;
import lombok.ToString;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/29
 * 遥控器模板详情表
 */
@Data
@ToString
public class DeviceRemoteControlTemplateReq {
    private Long id;
    private Long businessTypeId;//设备类型ID
    private String type;//GROUP 组控；SCENE 情景；SINGLE 单控
    private String function;//功能
    private Long remoteId;// 遥控器模板id  device_remote_template id
    private String keyCode;//
    private String defaultValue;//默认值
    private String eventStatus;//pressed/released/held down  短按/长按释放/长按
    private String scenes; //如果type是scene 那么这个是scene template的id 逗号分隔
    private Integer press;// 按键类型   1短按  2长按
    /**
     * 租户ID
     */
    private Long tenantId;

}