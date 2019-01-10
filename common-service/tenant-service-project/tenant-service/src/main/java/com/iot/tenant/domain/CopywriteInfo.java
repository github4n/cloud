package com.iot.tenant.domain;

import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：文案信息服务实例
 * 功能描述：文案信息服务实例
 * 创建人： 李帅
 * 创建时间：2018年9月11日 下午2:03:20
 * 修改人：李帅
 * 修改时间：2018年9月11日 下午2:03:20
 */
@TableName("copywrite_info")
public class CopywriteInfo {

	/***/
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1889135220358626520L;
	
	public static final String TABLE_NAME = "copywrite_info";

	/**
     * 主键id
     */
    private Long id;
    
    /**
     * 租户主键
     */
    private Long tenantId;

    /**
     * 对象ID
     */
    private Long objectId;
    
    /**
     * 对象类型
     */
    private String objectType;

    /**
     * 语言类型
     */
    private String langType;

    /**
     * 国际化键
     */
    private String langKey;

    /**
     * 国际化内容
     */
    private String langValue;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getLangType() {
		return langType;
	}

	public void setLangType(String langType) {
		this.langType = langType;
	}

	public String getLangKey() {
		return langKey;
	}

	public void setLangKey(String langKey) {
		this.langKey = langKey;
	}

	public String getLangValue() {
		return langValue;
	}

	public void setLangValue(String langValue) {
		this.langValue = langValue;
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

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

}
