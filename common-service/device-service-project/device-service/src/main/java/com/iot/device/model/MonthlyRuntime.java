package com.iot.device.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

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
@TableName("monthly_runtime")
@Document(collection = "monthly_runtime")
public class MonthlyRuntime extends Model<MonthlyRuntime> {

	/**
	 * table_name
	 */
	public static final String TABLE_NAME="monthly_runtime";
	
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Autowired
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
    
    private Integer month;
    /**
     * 年
     */
    private Integer year;


    protected Serializable pkVal() {
        return this.id;
    }

}
