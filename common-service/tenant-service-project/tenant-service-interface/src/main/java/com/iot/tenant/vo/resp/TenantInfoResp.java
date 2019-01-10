package com.iot.tenant.vo.resp;

import java.io.Serializable;
import java.util.Date;

public class TenantInfoResp implements Serializable {

    /***/
	private static final long serialVersionUID = 5182007819891429696L;
	/**
     * 主键id
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 主营业务
     */
    private String business;

    /**
     * 租户唯一code
     */
    private String code;
    /**
     * 电话
     */
    private String cellphone;
    /**
     * 邮箱
     */
    private String email;

    private String contacts;
    /**
     * 职务
     */
    private String job;

    private String country;

    private String province;

    private String city;

    private String address;
    /**
     * 创建时间
     */
    private Date createTime;
    
    private Date updateTime;

    /**
     * 类型 0:2c,1:2B
     */
    private Integer type;

    /**
     * 审核状态
     */
    private Integer auditStatus;
    
    /**
     * 锁定状态
     */
    private Integer lockStatus;
    
    public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Integer getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(Integer lockStatus) {
		this.lockStatus = lockStatus;
	}

    @Override
    public String toString() {
        return "TenantInfoResp{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", business='" + business + '\'' +
                ", code='" + code + '\'' +
                ", cellphone='" + cellphone + '\'' +
                ", email='" + email + '\'' +
                ", contacts='" + contacts + '\'' +
                ", job='" + job + '\'' +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", createTime=" + createTime +
                ", type=" + type +
                ", auditStatus=" + auditStatus +
                ", lockStatus=" + lockStatus +
                '}';
    }
}
