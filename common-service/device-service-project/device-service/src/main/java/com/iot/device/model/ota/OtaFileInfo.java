package com.iot.device.model.ota;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: linjihuang
 * @Descrpiton:
 * @Date: 17:21 2018/9/6
 * @Modify by:
 */
@Data
@ToString
public class OtaFileInfo implements Serializable {

	/**
	 * table_name
	 */
	public static final String TABLE_NAME="ota_file_info";
	
	/**
	 * 主键id 
	 * id
	 */
	private Long id;

	/**
	 * 产品id 
	 * productId
	 */
	private Long productId;
	
	/**
	 * 产品名称 
	 * productName
	 */
	private String productName;

	/**
	 * 版本号 
	 * version
	 */
	private String version;

	/**
	 * 租户id 
	 * tenantId
	 */
	private Long tenantId;

	/**
	 * 区域id 
	 * locationId
	 */
	private Long locationId;

	/**
	 * 创建人id 
	 * create_by
	 */
	private String createBy;
	/**
	 * 创建时间 
	 * create_time
	 */
	private Date createTime;
	/**
	 * 更新人 
	 * update_by
	 */
	private String updateBy;
	/**
	 * 更新时间 
	 * update_time
	 */
	private Date updateTime;

	/**
	 * 下载URL 
	 * download_url
	 */
	private String downloadUrl;
	
	/**
	 * 文件MD5 
	 * md5
	 */
	private String md5;
	
}
