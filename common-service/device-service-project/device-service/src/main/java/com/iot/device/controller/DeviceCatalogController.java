package com.iot.device.controller;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.device.api.DeviceCatalogApi;
import com.iot.device.exception.DeviceTypeExceptionEnum;
import com.iot.device.model.DeviceCatalog;
import com.iot.device.model.DeviceType;
import com.iot.device.service.IDeviceCatalogService;
import com.iot.device.service.IDeviceTypeService;
import com.iot.device.vo.req.DeviceCatalogReq;
import com.iot.device.vo.rsp.DeviceCatalogListResp;
import com.iot.device.vo.rsp.DeviceCatalogRes;
import com.iot.util.AssertUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 功能描述：
 * 创建人：chenxiaolin
 * 创建时间：2018年5月2日 下午6:05:09
 * 修改人： chenxiaolin
 * 修改时间：2018年5月2日 下午6:05:09
 */
@RestController
public class DeviceCatalogController implements DeviceCatalogApi {

	@Autowired
	private IDeviceCatalogService deviceCatalogService;
	
	@Autowired
	private IDeviceTypeService deviceTypeService;
	
	@Override
	public boolean addDeviceCatalog(@RequestBody DeviceCatalogReq req) {
	    AssertUtils.notEmpty(req.getName(),"catalogname.notnull");
	    req.setId(null);
		DeviceCatalog catalog = new DeviceCatalog();
		BeanUtils.copyProperties(req, catalog);
		catalog.setCreateTime(new Date());
        catalog.setTenantId(req.getTenantId());
        catalog.setUpdateBy(req.getUpdateBy());
		return deviceCatalogService.insert(catalog);
	}
	
	@Transactional
	public boolean updateDeviceCatalog(@RequestBody DeviceCatalogReq req) {
		String catalogName = req.getName();
		Long catalogId = req.getId();
		AssertUtils.notEmpty(catalogName,"catalogname.notnull");
		AssertUtils.notEmpty(catalogId,"catalogId.notnull");

		DeviceCatalog dc = deviceCatalogService.selectById(catalogId);
		if (dc == null) {
			throw new BusinessException(DeviceTypeExceptionEnum.DEVICETYP_NOT_EXIST);
		}
		//检查名称重复
		if (!dc.getName().equals(catalogName)) {
			EntityWrapper<DeviceCatalog> wrapper = new EntityWrapper<>();
			wrapper.eq("name", catalogName);
			int count = deviceCatalogService.selectCount(wrapper);
			if(count > 0 ){
				throw new BusinessException(DeviceTypeExceptionEnum.NAME_IS_EXIT);
			}
		}
		DeviceCatalog catalog = new DeviceCatalog();
		BeanUtils.copyProperties(req, catalog);
		catalog.setUpdateTime(new Date());
        catalog.setUpdateBy(req.getUpdateBy());
		return deviceCatalogService.updateById(catalog);
	}

	@Override
	public List<DeviceCatalogRes> getDeviceCatalog() {
		List<DeviceCatalog> res = deviceCatalogService.selectList(null);
		List<DeviceCatalogRes> rst = new ArrayList<DeviceCatalogRes>();
		res.forEach((log) -> {
			DeviceCatalogRes r = new DeviceCatalogRes();
			BeanUtils.copyProperties(log, r);
			rst.add(r);
		});
		return rst;
	}

	public Page<DeviceCatalogRes> getDevCatalogPageByCondition(@RequestBody DeviceCatalogReq req){
		AssertUtils.notEmpty(req, "deviceCatalog.notnull");
		Page<DeviceCatalogRes> page = new Page<>();
		int pageNum = req.getPageNum();
		int pageSize = req.getPageSize();
		if (pageNum < 1 || pageSize <= 0 || pageSize > 100) {
			throw new BusinessException(DeviceTypeExceptionEnum.DEVICETYP_PAGE_ILLEGAL);
		}
		EntityWrapper<DeviceCatalog> wrapper = new EntityWrapper<>();

		if (req.getTenantId() != null) {
			wrapper.eq("tenant_id", req.getTenantId());
		}
		if (!StringUtils.isEmpty(req.getSearchValues())) {
			wrapper.andNew(true, ""  )
					.like("name", req.getSearchValues(), SqlLike.DEFAULT)
					.or().like("description", req.getSearchValues(), SqlLike.DEFAULT);
		}
		wrapper.orderBy(true, "id", false);
		com.baomidou.mybatisplus.plugins.Page selectPage = new com.baomidou.mybatisplus.plugins.Page(pageNum, pageSize);
		selectPage = deviceCatalogService.selectPage(selectPage, wrapper);

		List<DeviceCatalogRes> res = new ArrayList<DeviceCatalogRes>();
		List<DeviceCatalog> types = selectPage.getRecords();
		if (res != null)
			types.forEach((catalog) -> {
				DeviceCatalogRes resp = new DeviceCatalogRes();
				BeanUtils.copyProperties(catalog, resp);
				res.add(resp);
			});
		page.setResult(res);
		page.setTotal(selectPage.getTotal());
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setPages(selectPage.getPages());
		return page;
	}
	/**
	 * @author chenxiaolin
	 * @created 2018年5月4日 上午11:19:28
	 */
	@Override
	@Transactional
	public Integer deleteCatalogs(@RequestBody ArrayList<Long> ids) {
		int delcount = 0;
		EntityWrapper<DeviceCatalog> idQ = new EntityWrapper<>();
		idQ.eq("1", "1");
		List<String> idList = new ArrayList<>();

		for (Long id : ids) {
			EntityWrapper<DeviceType> cq = new EntityWrapper<DeviceType>();
			cq.eq("device_catalog_id", id);
			if (deviceTypeService.selectCount(cq) < 1) {
				idList.add(String.valueOf(id));
				delcount++;
			}
		}
		idQ.and().in("id", idList);
		if (!idList.isEmpty() && !deviceCatalogService.delete(idQ)) {
			delcount = 0;
		}
		if(delcount != ids.size()){
			throw new BusinessException(DeviceTypeExceptionEnum.DEVICCATALOG_IS_USED);
		}
		return delcount;
	}


	public List<DeviceCatalogListResp> findAllCatalogList() {
		List<DeviceCatalogListResp> respList = Lists.newArrayList();
		List<DeviceCatalog> catalogList = deviceCatalogService.selectList(null);
		if (!CollectionUtils.isEmpty(catalogList)) {
			respList = Lists.newArrayList();
			for (DeviceCatalog catalog : catalogList) {
				DeviceCatalogListResp target = new DeviceCatalogListResp();
				target.setId(catalog.getId());
				target.setName(catalog.getName());
				respList.add(target);
			}
		}
		return respList;
	}
}
