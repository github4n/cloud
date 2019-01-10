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
@TableName("weekly_electricity_statistics")
@Document(collection = "weekly_electricity_statistics")
public class WeeklyElectricityStatistics extends Model<WeeklyElectricityStatistics> {

	/**
	 * table_name
	 */
	public static final String TABLE_NAME="weekly_electricity_statistics";
	
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
     * 周
     */
    private Integer week;
    /**
     * 年
     */
    private Integer year;


    protected Serializable pkVal() {
        return this.id;
    }

}
