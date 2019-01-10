package com.iot.device.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author CHQ
 * @since 2018-05-10
 */
@Data
@ToString
@TableName("configuration")
public class Configuration extends Model<Configuration> {

	/**
	 * table_name
	 */
	public static final String TABLE_NAME="configuration";
	
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 参数名称
     */
    private String param;
    /**
     * 参数内容
     */
    private String value;
    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Long tenantId;


    protected Serializable pkVal() {
        return this.id;
    }

}
