package com.iot.systempush.service;

import com.iot.systempush.vo.SystemPushControlVo;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：聚合层
 * 功能描述：系统推送控制
 * 创建人： 李帅
 * 创建时间：2018年11月19日 下午2:21:03
 * 修改人：李帅
 * 修改时间：2018年11月19日 下午2:21:03
 */
public interface SystemPushBusinessService {

    /**
     * 
     * 描述：查询系统推送控制
     * @author 李帅
     * @created 2018年11月19日 下午2:18:56
     * @since 
     * @param appId
     * @return
     */
	String getSystemPushControl(Long appId);

	/**
	 * 
	 * 描述：添加或修改系统推送控制
	 * @author 李帅
	 * @created 2018年11月19日 下午2:31:41
	 * @since 
	 * @param systemPushControlVo
	 */
	void addSystemPushControl(SystemPushControlVo systemPushControlVo);
}
