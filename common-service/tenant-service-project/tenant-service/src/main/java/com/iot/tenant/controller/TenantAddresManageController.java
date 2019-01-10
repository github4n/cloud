package com.iot.tenant.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.tenant.api.TenantAddresManageApi;
import com.iot.tenant.service.ITenantAddresManageService;
import com.iot.tenant.vo.req.TenantAddresManageReq;
import com.iot.tenant.vo.resp.TenantAddresManageResp;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：租户地址服务
 * 功能描述：租户地址服务
 * 创建人： 李帅
 * 创建时间：2018年9月11日 下午2:31:37
 * 修改人：李帅
 * 修改时间：2018年9月11日 下午2:31:37
 */
@RestController
public class TenantAddresManageController implements TenantAddresManageApi {

	@Autowired
	private ITenantAddresManageService tenantAddresManageService;

	/**
	 * 
	 * 描述：保存租户地址信息
	 * @author 李帅
	 * @created 2018年9月11日 下午2:22:52
	 * @since 
	 * @param req
	 */
	@Transactional
	public void save(@RequestBody TenantAddresManageReq req) {
		tenantAddresManageService.save(req);
	}
	
	/**
	 * 
	 * 描述：更新租户地址信息
	 * @author 李帅
	 * @created 2018年9月11日 下午2:23:41
	 * @since 
	 * @param req
	 */
	@Transactional
	public void update(@RequestBody TenantAddresManageReq req) {
		tenantAddresManageService.update(req);
	}
	
	/**
	 * 
	 * 描述：删除租户地址信息
	 * @author 李帅
	 * @created 2018年9月11日 下午2:24:20
	 * @since 
	 * @param id
	 */
	@Transactional
	public void delete(@RequestParam("id") Long id, @RequestParam("tenantId") Long tenantId) {
		tenantAddresManageService.delete(id, tenantId);
	}
	
	/**
	 * 
	 * 描述：通过租户ID获取租户地址信息
	 * @author 李帅
	 * @created 2018年9月11日 下午2:24:58
	 * @since 
	 * @param tenantId
	 * @return
	 */
	@Transactional
	public List<TenantAddresManageResp> getAddresByTenantId(@RequestParam("tenantId") Long tenantId) {
		return tenantAddresManageService.getAddresByTenantId(tenantId);
	}
}
