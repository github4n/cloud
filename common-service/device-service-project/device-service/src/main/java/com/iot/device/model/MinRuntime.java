package com.iot.device.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.iot.common.annotation.AutoMongoId;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author CHQ
 * @since 2018-05-09
 */
@Data
@ToString
@TableName("min_runtime")
@Document(collection = "min_runtime")
public class MinRuntime extends Model<MinRuntime> {

	/**
	 * table_name
	 */
	public static final String TABLE_NAME="min_runtime";
	
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @AutoMongoId
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户id(bigint)
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 设备id
     */
    @TableField("device_id")
    private String deviceId;
    /**
     * 统计间隔时长
     */
    private Integer step;
    /**
     * 统计时间点
     */
    private Date time;
    /**
     * 运行时间
     */
    private Long runtime;
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
    /**
     * 地区
     */
    @TableField("area")
    private String area;
    /**
     * 本地时间
     */
    @TableField("localtime")
    private Date localtime;

    private String timeStr;

    private String localtimeStr;

    private String hourse;

    protected Serializable pkVal() {
        return this.id;
    }

}
