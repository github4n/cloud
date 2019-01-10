package com.iot.device.vo.req;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：视频管理模块
 * 功能描述：设备扩展
 * 创建人： 李帅
 * 创建时间：2017年9月12日 下午1:47:01
 * 修改人：李帅
 * 修改时间：2017年9月12日 下午1:47:01
 */
public class GenerateUUIDReq implements Serializable{

	private static final long serialVersionUID = -6605703057339673972L;
	
	/**
	 *  产品ID
	 */
	private Long productId;
	
	/**
	 *  uuid生成量
	 */
	private int uuidAmount;
	
	/**
	 *  有效时长
	 */
	private BigDecimal uuidValidityDays;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public int getUuidAmount() {
		return uuidAmount;
	}

	public void setUuidAmount(int uuidAmount) {
		this.uuidAmount = uuidAmount;
	}

	public BigDecimal getUuidValidityDays() {
		return uuidValidityDays;
	}

	public void setUuidValidityDays(BigDecimal uuidValidityDays) {
		this.uuidValidityDays = uuidValidityDays;
	}

	
}
