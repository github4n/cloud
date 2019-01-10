package com.iot.tenant.vo.resp;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：租户地址服务出参
 * 功能描述：租户地址服务出参
 * 创建人： 李帅
 * 创建时间：2018年9月11日 下午2:02:17
 * 修改人：李帅
 * 修改时间：2018年9月11日 下午2:02:17
 */
public class TenantAddresManageResp {

    /**
     * 主键id
     */
    private Long id;
    
    /**
     * 租户主键
     */
    private Long tenantId;

    /**
     * 国家
     */
    private String country;

    /**
     * 省
     */
    private String state;

    /**
     * 市
     */
    private String city;
    
    /**
     * 详细地址
     */
    private String addres;

    /**
     * 备注
     */
    private String remark;
    
    /**
     * 联系人姓名
     */
    private String contacterName;
    
    /**
     * 联系人电话
     */
    private String contacterTel;

    /**
     * 邮编
     */
    private String zipCode;
    
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
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddres() {
		return addres;
	}

	public void setAddres(String addres) {
		this.addres = addres;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getContacterName() {
		return contacterName;
	}

	public void setContacterName(String contacterName) {
		this.contacterName = contacterName;
	}

	public String getContacterTel() {
		return contacterTel;
	}

	public void setContacterTel(String contacterTel) {
		this.contacterTel = contacterTel;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
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

}
