package com.iot.domain;

import java.util.Date;

	
public class SpaceDateVO {
	private Date beginTime;

    private Date endTime;
    
    private Date settingDate;
    
    private Integer sumGalleryful;

    private String tenantId;
    
    private Integer AlreadyGalleryfulAmount;
    
    private Integer usedAmount;
    
    private String spaceId;

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getSettingDate() {
		return settingDate;
	}

	public void setSettingDate(Date settingDate) {
		this.settingDate = settingDate;
	}

	public Integer getSumGalleryful() {
		return sumGalleryful;
	}

	public void setSumGalleryful(Integer sumGalleryful) {
		this.sumGalleryful = sumGalleryful;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public Integer getAlreadyGalleryfulAmount() {
		return AlreadyGalleryfulAmount;
	}

	public void setAlreadyGalleryfulAmount(Integer AlreadyGalleryfulAmount) {
		this.AlreadyGalleryfulAmount = AlreadyGalleryfulAmount;
	}

	public Integer getUsedAmount() {
		return usedAmount;
	}

	public void setUsedAmount(Integer usedAmount) {
		this.usedAmount = usedAmount;
	}

	public String getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
	}
    
	
    
}
