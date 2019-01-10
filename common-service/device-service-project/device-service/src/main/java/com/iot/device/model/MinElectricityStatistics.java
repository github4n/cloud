package com.iot.device.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.iot.common.annotation.AutoMongoId;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author CHQ
 * @since 2018-05-07
 */
@Data
@ToString
@TableName("min_electricity_statistics")
@Document(collection = "min_electricity_statistics")
public class MinElectricityStatistics extends Model<MinElectricityStatistics> {
	/**
	 * table_name
	 */
	public static final String TABLE_NAME="min_electricity_statistics";

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
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
     * 电量值
     */
    @TableField("electric_value")
    private Double electricValue;
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

    /**
     * 电压
     */
    private Double voltage;

    /**
     * 功率
     */
    private Double power;

    /**
     * 温度
     */
    private Double temperature;

    /**
     * 电流
     */
    private Double current;

    /**
     * 是否总路 0:总路  1:分类
     */
    private Integer isMaster;

    protected Serializable pkVal() {
        return this.id;
    }
}
