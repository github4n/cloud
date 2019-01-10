package com.iot.systempush.vo;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：系统推送控制
 * 功能描述：系统推送控制
 * 创建人： 李帅
 * 创建时间：2018年11月16日 下午3:45:51
 * 修改人：李帅
 * 修改时间：2018年11月16日 下午3:45:51
 */
public class SystemPushControlVo {

    /**
     * APP应用id
     */
    private Long appId;
    
    /**
     * 证书密码
     */
    private String switCh;

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public String getSwitCh() {
		return switCh;
	}

	public void setSwitCh(String switCh) {
		this.switCh = switCh;
	}

}