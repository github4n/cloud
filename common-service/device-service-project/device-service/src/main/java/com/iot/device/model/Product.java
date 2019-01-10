package com.iot.device.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.iot.device.vo.rsp.ProductConfigNetmodeRsp;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author lucky
 * @since 2018-04-12
 */
@Data
@ToString
@TableName("product")
public class Product extends Model<Product> implements Serializable {

	/**
	 * table_name
	 */
	public static final String TABLE_NAME="product";

	private static final long serialVersionUID = 1L;
	private Long id;
    /**
     * 设备类型id
	 * device_type_id
     */
	private Long deviceTypeId;
	
	private Long tenantId;
    /**
     * 产品名称
	 * product_name
     */
	private String productName;
    /**
	 * 通信类型（0 WIFI 1 蓝牙 2 网关 3 IPC）
	 * communication_mode
     */
	private Integer communicationMode;
    /**
     * 数据传输方式（定长<所有数据点一并上报>、变长<只上报有变化的数据点数据>）
	 * transmission_mode
     */
	private Integer transmissionMode;
	/**
	 * create_time
	 */
	private Date createTime;
	/**
	 * update_time
	 */
	private Date updateTime;
    /**
     * 产品型号
     */
	private String model;
	/**
	 * 产品型号
	 */
	private String remark;
    /**
     * 配置网络方式
	 * config_net_mode
     */
	private String configNetMode;

	@TableField(exist = false)
	private List<ProductConfigNetmodeRsp> configNetmodeRsps;
	/**
	 * 是否套包产品
	 */
	private Integer isKit;
	/**
	 * 是否直连设备
	 */
	private Integer isDirectDevice;
	/**
	 * 图标 icon
	 */
	private String icon;
	/**
	 * 开发状态,0:未开发,1:开发中,2:已上线
	 */
	private Integer developStatus;
	/**
	 * 企业开发者表关联的id
	 */
	private Long enterpriseDevelopId;

//	/**
//	 * 当前产品对应的步骤
//	 */
//	private Integer step;

	@TableField(exist = false)
	private String deviceTypeName;

	/**
	 * 是否为免开发产品 0非免开发产品，1免开发产品
	 */
	@TableField(exist = false)
	private Integer whetherSoc;

	@TableField(exist = false)
	private Long deviceCatalogId;

	@TableField(exist = false)
	private String deviceCatalogName;

	@TableField("audit_status")
	private Integer auditStatus;

	/**
	 * service_goo_audit_status
	 */
	@TableField("service_goo_audit_status")
	private Integer serviceGooAuditStatus;
	
	/**
	 * service_alx_audit_status
	 */
	@TableField("service_alx_audit_status")
	private Integer serviceAlxAuditStatus;
	/**
	 * 创建者
	 */
	@TableField("create_by")
	private Long createBy;

	@Override
	protected Serializable pkVal() {
		return id;
	}

}
