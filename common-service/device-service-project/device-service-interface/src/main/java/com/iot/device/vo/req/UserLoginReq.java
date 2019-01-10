package com.iot.device.vo.req;

import java.io.Serializable;

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
public class UserLoginReq implements Serializable{

	private static final long serialVersionUID = -6605703057339673972L;

	/**
	 *  租户ID
	 */
	private Long tenantId;
	
	/**
	 *  用户名
	 */
	private String userName;
	
	/**
	 *  密码
	 */
	private String passWord;

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}


	
}
