package com.iot.building.index.domain;

/**
 * 项目名称：IOT云平台
 * 模块名称：
 * 功能描述：告警
 * 创建人： zhouzongwei
 * 创建时间：2017年11月30日 下午2:35:27
 * 修改人： zhouzongwei
 * 修改时间：2017年11月30日 下午2:35:27
 */
public class Index {

    private Long id;

    private String image;

    private Long width;

    private int type;

    private Long top;

    private Long left;

    private Long tenantId;
    
    private Long buildId;
    
    private Long locationId;

    private int zindex;

	public int getZindex() {
		return zindex;
	}

	public void setZindex(int zindex) {
		this.zindex = zindex;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Long getWidth() {
		return width;
	}

	public void setWidth(Long width) {
		this.width = width;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Long getTop() {
		return top;
	}

	public void setTop(Long top) {
		this.top = top;
	}

	public Long getLeft() {
		return left;
	}

	public void setLeft(Long left) {
		this.left = left;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Long getBuildId() {
		return buildId;
	}

	public void setBuildId(Long buildId) {
		this.buildId = buildId;
	}

}
