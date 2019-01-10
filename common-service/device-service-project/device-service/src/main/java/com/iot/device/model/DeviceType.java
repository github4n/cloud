package com.iot.device.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.iot.device.vo.req.DataPointReq.SmartWraper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * <p>
 *  设备类型 ---extends DeviceCatalog
 * </p>
 *
 * @author lucky
 * @since 2018-04-12
 */
@Data
@ToString
@TableName("device_type")
public class DeviceType extends Model<DeviceType> implements Serializable {

	public static final Integer IS_SOC = 1;

	public static final Integer IS_NOT_SOC = 0;
	/**
	 * table_name
	 */
	public static final String TABLE_NAME="device_type";

	private static final long serialVersionUID = 1L;
	private Long id;
    /**
     * 类型名称
     */
	private String name;
	/**
	 * catalog大分类的名称
	 */
	@TableField(exist = false)
	private String deviceCatalogName;
    /**
     * 类型描述
     */
	private String description;
    /**
     * 设备分类id  extend device_catalog table  id
	 * device_catalog_id
     */
	private Long deviceCatalogId;
	/**
	 * tenant_id
	 */
	private Long tenantId;
	/**
	 * create_time
	 */
	private Date createTime;
	/**
	 * update_time
	 */
	private Date updateTime;
	/**
	 * create_by
	 */
	private Long createBy;
	/**
	 * update_by
	 */
	private Long updateBy;
	/**
	 * is_deleted
	 */
	private Integer isDeleted;
    /**
     * 厂商标识
	 * vender_flag
     */
	private String venderFlag;
    /**
     * 设备真实类型
	 *
     */
	private String type;
	/**
	 * icon圖片id
	 */
	private String img;
	/**
	 * 是否为免开发产品
	 */
	private Integer whetherSoc;

	/**
	 * 支持的配网模式（对应network_type表id字段，用,拼接）
	 */
	private String networkType;

	@TableField("ifttt_type")
	@ApiModelProperty(value = "ifttt类型(0:不支持 1：支持if 2:支持then 3:支持if支持then)")
	private Integer iftttType;

	@TableField(exist = false)
	private ArrayList<SmartWraper> smart;

	@Override
	protected Serializable pkVal() {
		return id;
	}

}
