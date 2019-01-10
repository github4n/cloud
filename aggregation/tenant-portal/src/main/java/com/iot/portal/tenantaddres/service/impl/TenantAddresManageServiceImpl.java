package com.iot.portal.tenantaddres.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.iot.common.util.ValidateUtil;
import com.iot.portal.common.service.CommonServiceImpl;
import com.iot.portal.tenantaddres.service.TenantAddresManageService;
import com.iot.saas.SaaSContextHolder;
import com.iot.system.api.DictApi;
import com.iot.system.api.LangApi;
import com.iot.tenant.api.TenantAddresManageApi;
import com.iot.tenant.vo.req.TenantAddresManageReq;
import com.iot.tenant.vo.resp.TenantAddresManageResp;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：聚合层
 * 功能描述：租户地址控制层
 * 创建人： 李帅
 * 创建时间：2018年9月11日 下午4:07:17
 * 修改人：李帅
 * 修改时间：2018年9月11日 下午4:07:17
 */
@Service("TenantAddresManageService")
public class TenantAddresManageServiceImpl extends CommonServiceImpl implements TenantAddresManageService{

    private final static Logger logger = LoggerFactory.getLogger(TenantAddresManageServiceImpl.class);

    @Autowired
    private TenantAddresManageApi tenantAddresManageApi;

    @Autowired
    private LangApi langApi;
    
    @Autowired
    private DictApi dictApi;
    /**
     * 
     * 描述：保存租户地址信息
     * @author 李帅
     * @created 2018年9月11日 下午4:12:44
     * @since 
     * @param req
     */
    @Override
	public void save(TenantAddresManageReq req) {
		// 参数校验
		String language = LocaleContextHolder.getLocale().toString();
		if (req.getContacterTel() != null) {
			ValidateUtil.isNumeric(req.getContacterTel());
		}
		if ("zh_CN".equals(language)) {
			CommonServiceImpl.checkStringParam(req.getCountry(), 0, 25, language);
			CommonServiceImpl.checkStringParam(req.getState(), 0, 50, language);
			CommonServiceImpl.checkStringParam(req.getCity(), 0, 25, language);
			CommonServiceImpl.checkStringParam(req.getAddres(), 0, 50, language);
			CommonServiceImpl.checkStringParam(req.getContacterName(), 0, 50, language);
			CommonServiceImpl.checkStringParam(req.getZipCode(), 0, 10, "en_US");
		} else {
			CommonServiceImpl.checkStringParam(req.getCountry(), 0, 100, language);
			CommonServiceImpl.checkStringParam(req.getState(), 0, 100, language);
			CommonServiceImpl.checkStringParam(req.getCity(), 0, 100, language);
			CommonServiceImpl.checkStringParam(req.getAddres(), 0, 500, language);
			CommonServiceImpl.checkStringParam(req.getContacterName(), 0, 100, language);
			CommonServiceImpl.checkStringParam(req.getZipCode(), 0, 10, language);
		}
		Long userId = SaaSContextHolder.getCurrentUserId();
		Long tenantId = SaaSContextHolder.currentTenantId();
		req.setCreateBy(userId);
		req.setTenantId(tenantId);
		tenantAddresManageApi.save(req);
	}

    /**
     * 
     * 描述：更新租户地址信息
     * @author 李帅
     * @created 2018年9月11日 下午4:18:34
     * @since 
     * @param req
     */
    @Override
	public void update(TenantAddresManageReq req) {
		// 参数校验
		String language = LocaleContextHolder.getLocale().toString();
		if (req.getContacterTel() != null) {
			ValidateUtil.isNumeric(req.getContacterTel());
		}
		if ("zh_CN".equals(language)) {
			if (req.getCountry() != null) {
				CommonServiceImpl.checkStringParam(req.getCountry(), 0, 50, language);
			}
			if (req.getState() != null) {
				CommonServiceImpl.checkStringParam(req.getState(), 0, 50, language);
			}
			if (req.getCity() != null) {
				CommonServiceImpl.checkStringParam(req.getCity(), 0, 50, language);
			}
			if (req.getAddres() != null) {
				CommonServiceImpl.checkStringParam(req.getAddres(), 0, 250, language);
			}
			if (req.getContacterName() != null) {
				CommonServiceImpl.checkStringParam(req.getContacterName(), 0, 50, language);
			}
			if (req.getZipCode() != null) {
				CommonServiceImpl.checkStringParam(req.getZipCode(), 0, 10, "en_US");
			}
		} else {
			if (req.getCountry() != null) {
				CommonServiceImpl.checkStringParam(req.getCountry(), 0, 100, language);
			}
			if (req.getState() != null) {
				CommonServiceImpl.checkStringParam(req.getState(), 0, 100, language);
			}
			if (req.getCity() != null) {
				CommonServiceImpl.checkStringParam(req.getCity(), 0, 100, language);
			}
			if (req.getAddres() != null) {
				CommonServiceImpl.checkStringParam(req.getAddres(), 0, 500, language);
			}
			if (req.getContacterName() != null) {
				CommonServiceImpl.checkStringParam(req.getContacterName(), 0, 100, language);
			}
			if (req.getZipCode() != null) {
				CommonServiceImpl.checkStringParam(req.getZipCode(), 0, 10, language);
			}
		}
		Long userId = SaaSContextHolder.getCurrentUserId();
		Long tenantId = SaaSContextHolder.currentTenantId();
		req.setUpdateBy(userId);
		req.setTenantId(tenantId);
		tenantAddresManageApi.update(req);
	}
    
    /**
     * 
     * 描述：删除租户地址信息
     * @author 李帅
     * @created 2018年9月11日 下午4:19:04
     * @since 
     * @param id
     */
    @Override
	public void delete(Long id) {
    	Long tenantId = SaaSContextHolder.currentTenantId();
    	tenantAddresManageApi.delete(id, tenantId);
    }
    
    /**
     * 
     * 描述：通过租户ID获取租户地址信息
     * @author 李帅
     * @created 2018年9月11日 下午4:19:30
     * @since 
     * @return
     */
    @Override
	public List<TenantAddresManageResp> getAddresByTenantId() {
        Long tenantId = SaaSContextHolder.currentTenantId();
    	return tenantAddresManageApi.getAddresByTenantId(tenantId);
    }
    
    /**
     * 
     * 描述：联系我们
     * @author 李帅
     * @created 2018年9月12日 上午11:06:04
     * @since 
     * @return
     */
    @Override
	public Map<String, String> contactUs() {
		String language = LocaleContextHolder.getLocale().toString();
		Map<String, String> map = dictApi.getDictItemNames((short) 10);
		Collection<String> keys = new ArrayList<String>();
		for (String value : map.values()) {
			keys.add(value);
		}
		Map<String, String> langMap = langApi.getLangValueByKey(keys, language);
		Map<String, String> returnMap = null;
		if(langMap != null) {
			returnMap = new HashMap<String, String>();
			if(langMap.get("contactus:address") != null) {
				returnMap.put("address", langMap.get("contactus:address"));
			}
			if(langMap.get("contactus:zipcode") != null) {
				returnMap.put("zipcode", langMap.get("contactus:zipcode"));
			}
			if(langMap.get("contactus:phone") != null) {
				returnMap.put("phone", langMap.get("contactus:phone"));
			}
			if(langMap.get("contactus:fax") != null) {
				returnMap.put("fax", langMap.get("contactus:fax"));
			}
			if(langMap.get("contactus:email") != null) {
				returnMap.put("email", langMap.get("contactus:email"));
			}
		}
		return returnMap;
	}

}
