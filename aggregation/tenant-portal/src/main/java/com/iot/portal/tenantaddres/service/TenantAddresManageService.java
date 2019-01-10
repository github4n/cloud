package com.iot.portal.tenantaddres.service;

import java.util.List;
import java.util.Map;

import com.iot.tenant.vo.req.TenantAddresManageReq;
import com.iot.tenant.vo.resp.TenantAddresManageResp;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：聚合层
 * 功能描述：租户地址控制层
 * 创建人： 李帅
 * 创建时间：2018年9月11日 下午4:07:28
 * 修改人：李帅
 * 修改时间：2018年9月11日 下午4:07:28
 */
public interface TenantAddresManageService {

    /**
     * 
     * 描述：保存租户地址信息
     * @author 李帅
     * @created 2018年9月11日 下午4:12:50
     * @since 
     * @param req
     */
	void save(TenantAddresManageReq req);

	/**
	 * 
	 * 描述：更新租户地址信息
	 * @author 李帅
	 * @created 2018年9月11日 下午4:18:28
	 * @since 
	 * @param req
	 */
	void update(TenantAddresManageReq req);
	
	/**
	 * 
	 * 描述：删除租户地址信息
	 * @author 李帅
	 * @created 2018年9月11日 下午4:18:57
	 * @since 
	 * @param id
	 */
	void delete(Long id);
	
	/**
	 * 
	 * 描述：通过租户ID获取租户地址信息
	 * @author 李帅
	 * @created 2018年9月11日 下午4:19:23
	 * @since 
	 * @return
	 */
	List<TenantAddresManageResp> getAddresByTenantId();
	
	/**
	 * 
	 * 描述：联系我们
	 * @author 李帅
	 * @created 2018年9月12日 上午11:05:50
	 * @since 
	 * @return
	 */
	Map<String, String> contactUs();
}
