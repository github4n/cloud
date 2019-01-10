package com.iot.device.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:28 2018/5/4
 * @Modify by:
 */
@Deprecated
@Data
@ToString
@TableName("device_ota")
public class DeviceOTA extends Model<DeviceOTA> {
    /**
     * table_name
     */
    public static final String TABLE_NAME = "device_ota";

    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    private Long id;
    /**
     * 设备id
     */
    @TableField("device_id")
    private String deviceId;
    /**
     * 分位的类型  固件类型
     * fw_type
     * 0:所有的模块在一个分位里面  1:wifi 模块的分位  2: zigbee模块的分位 3: z-wave模块的分位 4：ble模块的分位 不填默认值为0.
     */
    @TableField("fw_type")
    private Integer fwType;
    /**
     * 设备版本号
     * version
     */
    private String version;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
