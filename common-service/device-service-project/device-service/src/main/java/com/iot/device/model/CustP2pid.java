package com.iot.device.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * P2PID表
 * </p>
 *
 * @author lucky
 * @since 2018-04-12
 */
@Data
@ToString
@TableName("cust_p2pid")
public class CustP2pid extends Model<CustP2pid> implements Serializable {
	/**
	 * table_name
	 */
	public static final String TABLE_NAME="cust_p2pid";

    private static final long serialVersionUID = 1L;
	private Long id;
	/**
     * P2PID
	 * p2p_id
     */

	private String p2pId;
    /**
     * 使用标识(0-未使用，1-已使用)
	 * user_mark
     */
	private Integer useMark;
    /**
     * 创建时间
	 * create_time
     */
	private Date createTime;
    /**
     * 过期时间
	 * overdue_time
     */
	private Date overdueTime;
	/**
	 * 租户ID
	 */
	@TableField("tenant_id")
	private Long tenantId;

	@Override
	protected Serializable pkVal() {
		return id;
	}


}
