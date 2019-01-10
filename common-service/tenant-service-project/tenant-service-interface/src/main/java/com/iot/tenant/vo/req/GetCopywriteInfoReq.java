package com.iot.tenant.vo.req;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 项目名称：立达信IOT云平台 
 * 模块名称：文案信息服务入参 
 * 功能描述：文案信息服务入参
 * 创建人： 李帅 
 * 创建时间：2018年9月11日 下午2:02:17
 * 修改人：李帅 
 * 修改时间：2018年9月11日 下午2:02:17
 */
public class GetCopywriteInfoReq implements Serializable {

    /***/
	private static final long serialVersionUID = 1889135220358626541L;

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
    private List<String> langTypes;

    /**
     * 国际化键
     */
    private List<String> langKeys;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
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

	public List<String> getLangTypes() {
		return langTypes;
	}

	public void setLangTypes(List<String> langTypes) {
		this.langTypes = langTypes;
	}

	public List<String> getLangKeys() {
		return langKeys;
	}

	public void setLangKeys(List<String> langKeys) {
		this.langKeys = langKeys;
	}


}
