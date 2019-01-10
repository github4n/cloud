package com.iot.shcs.space.vo;

import java.io.Serializable;
import java.util.Date;

public class SpaceBackgroundImgReq implements Serializable {

	/**
	 *  
	 */
	private static final long serialVersionUID = 6181227045853564578L;
	
	private Long id;
	private Long spaceId;
	private String bgImg;  // 背景图
	private String thumbnailImg;  // 缩略图
	private String viewImg;  // 视角图
	private String thumbnailPos;  // 缩略图坐标
	private String viewPos;  // 视角图坐标
	private Long createBy;
	private Long updateBy;
	private Date createTime;
	private Date updateTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSpaceId() {
		return spaceId;
	}
	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}
	public String getBgImg() {
		return bgImg;
	}
	public void setBgImg(String bgImg) {
		this.bgImg = bgImg;
	}
	public String getThumbnailImg() {
		return thumbnailImg;
	}
	public void setThumbnailImg(String thumbnailImg) {
		this.thumbnailImg = thumbnailImg;
	}
	public String getViewImg() {
		return viewImg;
	}
	public void setViewImg(String viewImg) {
		this.viewImg = viewImg;
	}
	public String getThumbnailPos() {
		return thumbnailPos;
	}
	public void setThumbnailPos(String thumbnailPos) {
		this.thumbnailPos = thumbnailPos;
	}
	public String getViewPos() {
		return viewPos;
	}
	public void setViewPos(String viewPos) {
		this.viewPos = viewPos;
	}
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public Long getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
