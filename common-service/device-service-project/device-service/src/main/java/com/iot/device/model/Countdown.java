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
 * 
 * </p>
 *
 * @author CHQ
 * @since 2018-04-28
 */
@Data
@ToString
@TableName("countdown")
public class Countdown extends Model<Countdown> {

	/**
	 * table_name
	 */
	public static final String TABLE_NAME="countdown";
	
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 设备id
     */
    @TableField("device_id")
    private String deviceId;
    /**
     * 用户id(bigint)
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 倒计时时间（单位s）
     */
    private Long countdown;
    /**
     * 是否使能（0关闭，1开启）
     */
    @TableField("is_enable")
    private Integer isEnable;
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
    /**
     * 用户所属组织id
     */
    @TableField("org_id")
    private Long orgId;
    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private Long tenantId;

    protected Serializable pkVal() {
        return this.id;
    }


}
