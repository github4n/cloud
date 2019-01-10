package com.iot.video.dto;

public class AppPayDto {

	/** 套餐价格*/
	private Double packagePrice;

	/** 货币代码*/
	private String currency;

	/**
	 * 套餐名称
	 */
	private String packageName;

	public Double getPackagePrice() {
		return packagePrice;
	}

	public void setPackagePrice(Double packagePrice) {
		this.packagePrice = packagePrice;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
}
