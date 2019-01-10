package com.iot.device.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * UUID管理表
 * </p>
 *
 * @author lucky
 * @since 2018-04-12
 */
@Data
@ToString
@TableName("cust_uuid_manage")
public class CustUuidManage extends Model<CustUuidManage> implements Serializable {
	/**
	 * table_name
	 */
	public static final String TABLE_NAME="cust_uuid_manage";

	private static final long serialVersionUID = 1L;
	private Long id;
    /**
     * 批次号
     */
	private String batchNumId;
    /**
     * 生成时间
     */
	private Date createTime;
    /**
     * 客户代码
     */
	private String custCode;
    /**
     * 客户名称
     */
	private String custName;
    /**
     * uuid 类型
     */
	private String uuidType;
    /**
     * 下载次数
     */
	private Integer downLoadedNum;
    /**
     * uuid生成数量
     */
	private Integer uuidAmount;
    /**
     * 生成状态(0：进行中;1：已完成;2：生成失败;3：P2PID不足)
     */
	private Integer status;
    /**
     * UUID列表CVS文件ID
     */
	private String fileId;
	/**
	 * 租户ID
	 */
	private Long tenantId;

	@Override
	protected Serializable pkVal() {
		return id;
	}





}
