package com.iot.device.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人： 李帅
 * 创建时间：2018年9月12日 下午6:04:42
 * 修改人：李帅
 * 修改时间：2018年9月12日 下午6:04:42
 */
@Data
@ToString
public class ProductPublishHistory implements Serializable {

	/**
	 * table_name
	 */
	public static final String TABLE_NAME="product_publish_history";
	/***/
	private static final long serialVersionUID = 1885965011239600438L;
	/**
     * 主键id
     */
    private Long id;
    
    /**
     * 租户主键
     */
    private Long tenantId;

    /**
     * 产品ID
     */
	private Long productId;
	
	/**
	 * 产品发布时间
	 */
	private String publishTime;
	
	/**
	 * 产品发布状态
	 */
	private String publishStatus;
	
	/**
	 * 发布失败原因
	 */
	private String failureReason;
	
	/**
     * 创建人
     */
    private Long createBy;
    
    /**
     * 创建时间
     */
    private String createTime;
    
    /**
     * 创建人
     */
    private Long updateBy;
    
    /**
     * 创建时间
     */
    private String updateTime;
    
    /**
     * 数据有效性
     */
    private String isDeleted;


}
