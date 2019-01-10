package com.iot.device.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.constant.SystemConstants;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.CommonUtil;
import com.iot.device.api.DataPointApi;
import com.iot.device.core.service.ProductServiceCoreUtils;
import com.iot.device.exception.DataPointExceptionEnum;
import com.iot.device.exception.DeviceExceptionEnum;
import com.iot.device.exception.ProductExceptionEnum;
import com.iot.device.exception.SmartDataPointExceptionEnum;
import com.iot.device.model.DataPoint;
import com.iot.device.model.Device;
import com.iot.device.model.Product;
import com.iot.device.model.ProductDataPoint;
import com.iot.device.model.SmartDataPoint;
import com.iot.device.service.IDataPointService;
import com.iot.device.service.IDeviceService;
import com.iot.device.service.IDeviceTypeDataPointService;
import com.iot.device.service.IProductDataPointService;
import com.iot.device.service.IProductService;
import com.iot.device.service.ISmartDataPointService;
import com.iot.device.vo.req.DataPointReq;
import com.iot.device.vo.req.DataPointReq.SmartWraper;
import com.iot.device.vo.rsp.DataPointResp;
import com.iot.device.vo.rsp.DeviceFunResp;
import com.iot.device.vo.rsp.voicebox.SmartDataPointResp;
import com.iot.saas.SaaSContextHolder;
import com.iot.util.AssertUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lucky
 * @since 2018-04-12
 */
@RestController
public class DataPointController implements DataPointApi {

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private IDataPointService dataPointService;
    
    @Autowired
    private IProductDataPointService productDataPointService;
    
    @Autowired
    private IDeviceTypeDataPointService deviceTypeDataPointService;

    @Autowired
    private IProductService productService;
    
    @Autowired
    private ISmartDataPointService smartDataPointService;

    /**
     * override说明：
     * @author chenxiaolin 修改
     */
    public List<DeviceFunResp> findDataPointListByDeviceId(@RequestParam(value = "deviceId") String deviceId) {
        AssertUtils.notEmpty(deviceId,"deviceId.notnull");
        List<DeviceFunResp> deviceFunRespList = null;
        EntityWrapper<Device> wrapper = new EntityWrapper<>();
        wrapper.eq("uuid", deviceId);
        Device orig = deviceService.selectOne(wrapper);
        if (orig == null) {
            throw new BusinessException(DeviceExceptionEnum.DEVICE_NOT_EXIST);
        }
        Long productId = orig.getProductId();
        if (!StringUtils.isEmpty(productId)) {
            deviceFunRespList =  dataPointService.findDataPointsByProductId(productId);
        }
        return deviceFunRespList;
    }

	@Override
	public List<DeviceFunResp> findDataPointListByProductId(@RequestParam(value = "productId") Long productId) {
		return dataPointService.findDataPointsByProductId(productId);
	}

	@Override
	public Map<Long, List<DeviceFunResp>> findDataPointListByProductIds(@RequestBody List<Long> productIds) {
		if (CollectionUtils.isEmpty(productIds)) {
			return Maps.newHashMap();
		}
		Map<Long, List<DeviceFunResp>> dataResultMap = Maps.newHashMap();
		productIds.forEach(productId -> {
			List<DeviceFunResp> dataResultList = dataPointService.findDataPointsByProductId(productId);
			if (CollectionUtils.isEmpty(dataResultList)) {
				dataResultList = Lists.newArrayList();
			}
			dataResultMap.put(productId, dataResultList);
		});
		return dataResultMap;
	}

	/**
     * override说明：
     * @author chenxiaolin
     */
    @Transactional
	public boolean addDataPoint(@RequestBody @Validated DataPointReq req) {
		req.setId(null);
		String propertyCode = req.getPropertyCode();

		DataPoint point = dataPointService.insertDataPoint(req);

		List<SmartWraper> smart = req.getSmart();
		List<SmartDataPoint> ss = new ArrayList<SmartDataPoint>();
		if (smart != null && !smart.isEmpty()) {
			smart.forEach((s) -> {
				SmartDataPoint sd = new SmartDataPoint();
				sd.setSmart(s.getSmart());
				sd.setPropertyCode(propertyCode);
				sd.setSmartCode(s.getCode());
				sd.setCreateBy(req.getCreateBy());
				sd.setCreateTime(new Date());
				sd.setDataPointId(point.getId());
				sd.setTenantId(req.getTenantId());

				ss.add(sd);
			});
			smartDataPointService.insertBatchDataPoint(ss);
		}
		return true;
	}
	
    /**
     * override说明：
     * @author chenxiaolin
     */
	public boolean deleteByIds(@RequestBody ArrayList<Long> ids) {
		int size = ids.size();
		EntityWrapper queryQ = new EntityWrapper();
		for (int i = 0; i < size; i++) {
			queryQ.eq("data_point_id", ids.get(i));
			if((i + 1) != size) {
				queryQ.or();
			}
		}
		if (productDataPointService.selectCount(queryQ) > 0) {
			throw new BusinessException(DataPointExceptionEnum.POINT_IS_USED_BY_PRODUCT);
		}
		if (deviceTypeDataPointService.selectCount(queryQ) > 0) {
			throw new BusinessException(DataPointExceptionEnum.POINT_IS_USED_BY_DEVTYPE);
		}
		return dataPointService.deleteBatchIds(ids);
	}
	
    /**
     * override说明：
     * @author chenxiaolin
     */
	@Transactional
	public boolean updateDataPoint(@RequestBody DataPointReq req) {
		DataPoint point = new DataPoint();
		BeanUtils.copyProperties(req, point);
		Long userId = req.getCreateBy();
		List<SmartWraper> smart = req.getSmart();
		List<SmartDataPoint> adds = new ArrayList<SmartDataPoint>();
		List<SmartDataPoint> ups = new ArrayList<SmartDataPoint>();
		List<Long> dels = new ArrayList<Long>();
		if (smart != null) {
			smart.forEach((s) -> {
				SmartDataPoint sd = new SmartDataPoint();
				sd.setId(s.getId());
				sd.setSmartCode(s.getCode());
				sd.setUpdateBy(userId);
				sd.setUpdateTime(new Date());
				sd.setCreateBy(userId);
				sd.setCreateTime(new Date());
				if (s.getId() == null) {
					sd.setSmart(s.getSmart());
					sd.setPropertyCode(req.getPropertyCode());
					sd.setDataPointId(point.getId());
					adds.add(sd);
				} else if (s.getCode() != null && !s.getCode().isEmpty()) {
					ups.add(sd);
				} else {
					dels.add(sd.getId());
				}
				
			});
		}
		if (!adds.isEmpty()) {
			smartDataPointService.insertBatch(adds);
		}
		if (!ups.isEmpty()) {
			smartDataPointService.updateBatchById(ups);
		}
		if (!dels.isEmpty()) {
			smartDataPointService.delBatchDataPoint(dels);
		}
		return dataPointService.updateById(point);
	}

	@Override
    public List<DeviceFunResp> findDataPointListByDeviceTypeId(@RequestParam("deviceTypeId")Long deviceTypeId) {
		AssertUtils.notEmpty(deviceTypeId,"deviceId.notnull");
        return dataPointService.findDataPointListByDeviceTypeId(deviceTypeId);
	}

	/**
	 * 描述：
	 * @author chenxiaolin
	 * @created 2018年6月7日 下午5:36:05
	 * @return
	 */
	@Override
	public PageInfo findExceptCustom(@RequestBody DataPointReq req) {
		AssertUtils.notEmpty(req,"DataPointReq.notnull");
		Page<DataPoint> page = new Page<>(CommonUtil.getPageNum(req),CommonUtil.getPageSize(req));
		EntityWrapper<DataPoint> eq = new EntityWrapper<>();
		List<DataPointResp> res = new ArrayList<DataPointResp>();
		Map<String, ArrayList<SmartWraper>> cache = new HashMap<String, ArrayList<SmartWraper>>();
		List<Long> selectIds = new ArrayList<>();
		eq.eq("is_custom", 0);
		if (!StringUtils.isEmpty(req.getLabelName())){
			eq.like("label_name",req.getLabelName());
		}
		if (!StringUtils.isEmpty(req.getPropertyCode())){
			eq.like("property_code",req.getPropertyCode());
		}
		eq.orderDesc(Arrays.asList("create_time"));
		page = dataPointService.selectPage(page, eq);
		List<DataPoint> rs = page.getRecords();

		if (rs != null && !rs.isEmpty()) {
			rs.forEach((d) -> {
				DataPointResp re = new DataPointResp();
				BeanUtils.copyProperties(d, re);
				cache.put(re.getId().toString(), re.getSmart());
				selectIds.add(re.getId());
				res.add(re);
			});
			List<SmartDataPoint> smarts = smartDataPointService.selectList(new EntityWrapper<SmartDataPoint>()
					.in("data_point_id", selectIds));
			for (SmartDataPoint smartDataPoint : smarts) {
				String key = smartDataPoint.getDataPointId().toString();
				SmartWraper sw = new SmartWraper();
				sw.setCode(smartDataPoint.getSmartCode());
				sw.setId(smartDataPoint.getId());
				sw.setSmart(smartDataPoint.getSmart());
				cache.get(key).add(sw);
			}
		}
		PageInfo pageInfo = new PageInfo();
		pageInfo.setList(res);
		pageInfo.setTotal(page.getTotal());
		pageInfo.setPages(page.getPages());
		pageInfo.setPageSize(page.getSize());
		pageInfo.setPageNum(req.getPageNum());
		return pageInfo;
	}

	@Override
	public String getSmartCode(@PathVariable("smart") Integer smart, @RequestParam("propertyCode") String propertyCode) {
		EntityWrapper<SmartDataPoint> eqs = new EntityWrapper<>();
		eqs.eq("smart", smart).eq("property_code", propertyCode);
		SmartDataPoint s = smartDataPointService.selectOne(eqs);
		if (s == null)
			return null;
		return s.getSmartCode();
	}

	@Transactional
	public boolean deleteByIdsAndProduct(Long productId, ArrayList<Long> ids) {
		Product exitPro = ProductServiceCoreUtils.getProductByProductId(productId);
		if (exitPro == null) {
			throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
		}
		Long tenantId = SaaSContextHolder.currentTenantId();
		if (SystemConstants.BOSS_TENANT != tenantId && tenantId != exitPro.getTenantId()){
			throw new BusinessException(ProductExceptionEnum.ACCESS_DENIED_EXCEPTION);//权限不足
		}
		List<DataPoint> dps = dataPointService.selectBatchIds(ids);
		dps.forEach((dp) -> {
			if (dp.getIsCustom() != null && dp.getIsCustom() == 0) {
				throw new BusinessException(DataPointExceptionEnum.POINT_IS_NOT_CUSTOM);
			}
		});
		EntityWrapper<ProductDataPoint> eq = new EntityWrapper<>();
		eq.eq("product_id", productId);
		eq.in("data_point_id", ids);
		List<ProductDataPoint> pdps = productDataPointService.selectList(eq);
		
		if (!productDataPointService.delete(eq)) {
			throw new BusinessException(DataPointExceptionEnum.CUSTOM_POINT_DEL_ERROR);
		}
		ids.clear();
		pdps.forEach((pdp) -> {
			ids.add(pdp.getDataPointId());
		});
		EntityWrapper<SmartDataPoint> sdeq = new EntityWrapper<>();
		sdeq.in("data_point_id", ids);
		if (!smartDataPointService.delete(sdeq)) {
			throw new BusinessException(SmartDataPointExceptionEnum.SMARTDATAPOINT_POINT_DEL_ERROR);
		}
		if (!dataPointService.deleteBatchIds(ids)) {
			throw new BusinessException(DataPointExceptionEnum.CUSTOM_POINT_DEL_ERROR);
		}
		return true;
	}

	@Override
	public List<SmartDataPointResp> getSmartByDataPointId(@PathVariable("dataPointId") Long dataPointId) {
		EntityWrapper<SmartDataPoint> eq = new EntityWrapper<>();
		eq.eq("data_point_id", dataPointId);
		List<SmartDataPoint> ss = smartDataPointService.selectList(eq);
		List<SmartDataPointResp> res = new ArrayList<>();
		if (ss != null) {
			ss.forEach((s) -> {
				SmartDataPointResp sr = new SmartDataPointResp();
				BeanUtils.copyProperties(s, sr);
				res.add(sr);
			});
		}
		return res;
	}

	@Override
	public DataPointResp findExceptCustomById(Long dataPointId) {
		// TODO Auto-generated method stub
		return null;
	}
}
