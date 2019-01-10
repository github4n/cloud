package com.iot.report.entity;

import java.util.Date;
import org.springframework.data.mongodb.core.mapping.Document;
/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：用户活跃明细
 * 功能描述：用户活跃明细
 * 创建人： 李帅
 * 创建时间：2019年1月3日 下午8:05:34
 * 修改人：李帅
 * 修改时间：2019年1月3日 下午8:05:34
 */
@Document(collection="user_active_info")
public class UserActiveInfo {
	
	/**
	 * 用户uuid，主键
	 */
	private String uuid;
	
	/**
	 * 租户id
	 */
    private Long tenantId;

    /**
	 * 活跃日期
	 */
    private Date dataDate;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Date getDataDate() {
		return dataDate;
	}

	public void setDataDate(Date dataDate) {
		this.dataDate = dataDate;
	}
    
}
