package com.iot.tenant.service;

import java.util.List;

import com.iot.tenant.vo.req.TenantAddresManageReq;
import com.iot.tenant.vo.resp.TenantAddresManageResp;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：租户地址服务
 * 功能描述：租户地址服务
 * 创建人： 李帅
 * 创建时间：2018年9月11日 下午2:32:00
 * 修改人：李帅
 * 修改时间：2018年9月11日 下午2:32:00
 */
public interface ITenantAddresManageService {

	/**
	 * 保存租户地址信息
	 */
	void save(TenantAddresManageReq req);
	
	/**
	 * 
	 * 描述：更新租户地址信息
	 * @author 李帅
	 * @created 2018年9月11日 下午2:23:49
	 * @since 
	 * @param req
	 */
	void update(TenantAddresManageReq req);
	
	/**
	 * 
	 * 描述：删除租户地址信息
	 * @author 李帅
	 * @created 2018年9月11日 下午2:24:27
	 * @since 
	 * @param id
	 */
	void delete(Long id, Long tenantId);
	
	/**
	 * 
	 * 描述：通过租户ID获取租户地址信息
	 * @author 李帅
	 * @created 2018年9月11日 下午2:25:06
	 * @since 
	 * @param tenantId
	 * @return
	 */
	List<TenantAddresManageResp> getAddresByTenantId(Long tenantId);

}
