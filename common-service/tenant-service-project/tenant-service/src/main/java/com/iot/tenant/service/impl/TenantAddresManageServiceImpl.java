package com.iot.tenant.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.common.exception.BusinessException;
import com.iot.tenant.domain.TenantAddresManage;
import com.iot.tenant.exception.TenantExceptionEnum;
import com.iot.tenant.mapper.TenantAddresManageMapper;
import com.iot.tenant.service.ITenantAddresManageService;
import com.iot.tenant.vo.req.TenantAddresManageReq;
import com.iot.tenant.vo.resp.TenantAddresManageResp;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：租户地址服务
 * 功能描述：租户地址服务
 * 创建人： 李帅
 * 创建时间：2018年9月11日 下午2:32:08
 * 修改人：李帅
 * 修改时间：2018年9月11日 下午2:32:08
 */
@Service
public class TenantAddresManageServiceImpl implements ITenantAddresManageService {

	@Autowired
	private TenantAddresManageMapper tenantAddresManageMapper;

	/**
	 * 
	 * 描述：保存租户地址信息
	 * @author 李帅
	 * @created 2018年9月11日 下午2:23:16
	 * @since 
	 * @param req
	 */
	@Override
	public void save(TenantAddresManageReq req) {
		if (req.getTenantId() == null) {
			throw new BusinessException(TenantExceptionEnum.TENANTID_IS_NULL);
		}
		if (req.getCountry() == null) {
			throw new BusinessException(TenantExceptionEnum.COUNTRY_IS_NULL);
		}
		if (req.getState() == null) {
			throw new BusinessException(TenantExceptionEnum.STATE_IS_NULL);
		}
		if (req.getCity() == null) {
			throw new BusinessException(TenantExceptionEnum.CITY_IS_NULL);
		}
		if (req.getAddres() == null) {
			throw new BusinessException(TenantExceptionEnum.ADDRES_IS_NULL);
		}
		if (req.getContacterName() == null) {
			throw new BusinessException(TenantExceptionEnum.CONTACTERNAME_IS_NULL);
		}
		if (req.getContacterTel() == null) {
			throw new BusinessException(TenantExceptionEnum.CONTACTERTEL_IS_NULL);
		}
		if (req.getZipCode() == null) {
			throw new BusinessException(TenantExceptionEnum.ZIPCODE_IS_NULL);
		}
		if (req.getCreateBy() == null) {
			throw new BusinessException(TenantExceptionEnum.CREATEBY_IS_NULL);
		}
		TenantAddresManage tenantAddresManage = new TenantAddresManage();
		BeanUtils.copyProperties(req, tenantAddresManage);
		tenantAddresManageMapper.save(tenantAddresManage);
	}
	
	/**
	 * 
	 * 描述：更新租户地址信息
	 * @author 李帅
	 * @created 2018年9月11日 下午2:23:56
	 * @since 
	 * @param req
	 */
	@Override
	public void update(TenantAddresManageReq req) {
		if (req.getId() == null) {
			throw new BusinessException(TenantExceptionEnum.ADDRESID_IS_NULL);
		}
		if (req.getUpdateBy() == null) {
			throw new BusinessException(TenantExceptionEnum.UPDATEBY_IS_NULL);
		}
		TenantAddresManage tenantAddresManage = new TenantAddresManage();
		BeanUtils.copyProperties(req, tenantAddresManage);
		tenantAddresManageMapper.update(tenantAddresManage);
	}
	
	/**
	 * 
	 * 描述：删除租户地址信息
	 * @author 李帅
	 * @created 2018年9月11日 下午2:24:35
	 * @since 
	 * @param id
	 */
	@Override
	public void delete(Long id, Long tenantId) {
		if (id == null) {
			throw new BusinessException(TenantExceptionEnum.ADDRESID_IS_NULL);
		}
		if (tenantId == null) {
			throw new BusinessException(TenantExceptionEnum.TENANTID_IS_NULL);
		}
		tenantAddresManageMapper.delete(id, tenantId);
	}
	
	/**
	 * 
	 * 描述：通过租户ID获取租户地址信息
	 * @author 李帅
	 * @created 2018年9月11日 下午2:25:13
	 * @since 
	 * @param tenantId
	 * @return
	 */
	@Override
	public List<TenantAddresManageResp> getAddresByTenantId(Long tenantId) {
		if (tenantId == null) {
			throw new BusinessException(TenantExceptionEnum.TENANTID_IS_NULL);
		}
		List<TenantAddresManage> tenantAddresManages = tenantAddresManageMapper.getAddresByTenantId(tenantId);
		if(tenantAddresManages != null && tenantAddresManages.size() > 0) {
			List<TenantAddresManageResp> tenantAddresManageResps = new ArrayList<TenantAddresManageResp>();
			TenantAddresManageResp tenantAddresManageResp = new TenantAddresManageResp();
			for(TenantAddresManage tenantAddresManage : tenantAddresManages) {
				BeanUtils.copyProperties(tenantAddresManage, tenantAddresManageResp);
				tenantAddresManageResps.add(tenantAddresManageResp);
			}
			return tenantAddresManageResps;
		}
		return null;
		
	}
}
