package com.iot.device.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.device.api.DeviceTypeApi;
import com.iot.device.business.DeviceTypeBusinessService;
import com.iot.device.comm.cache.VersionEnum;
import com.iot.device.core.DeviceTypeCacheCoreUtils;
import com.iot.device.exception.DeviceTypeExceptionEnum;
import com.iot.device.model.*;
import com.iot.device.service.*;
import com.iot.device.vo.req.DataPointReq.SmartWraper;
import com.iot.device.vo.req.DeviceType2PointsReq;
import com.iot.device.vo.req.DeviceTypeReq;
import com.iot.device.vo.req.SaveDeviceTypeToGoodsReq;
import com.iot.device.vo.rsp.DeviceTypeListResp;
import com.iot.device.vo.rsp.DeviceTypeResp;
import com.iot.device.vo.rsp.SmartDeviceTypeResp;
import com.iot.saas.SaaSContextHolder;
import com.iot.util.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lucky
 * @since 2018-04-12
 */
@Slf4j
@RestController
@Validated
public class DeviceTypeController implements DeviceTypeApi{

    public static final Logger LOGGER = LoggerFactory.getLogger(DeviceTypeController.class);
    @Autowired
    private IDeviceTypeService deviceTypeService;

    @Autowired
    private IDeviceTypeDataPointService deviceTypeDataPointService;

    @Autowired
    private ISmartDeviceTypeService smartDeviceTypeService;

    @Autowired
    private IProductService productService;

	@Autowired
	private DeviceTypeBusinessService deviceTypeBusinessService;

	@Autowired
	private IDeviceTypeToServiceModuleService deviceTypeToServiceModuleService;

	@Autowired
	private IDeviceTypeToStyleService deviceTypeToStyleService;

	@Autowired
	private IDeviceTypeToGoodsService deviceTypeToGoodsService;

	@Autowired
	private IGoodsSmartService goodsSmartService;


	public DeviceTypeResp getDeviceTypeById(@RequestParam(value = "deviceTypeId") Long deviceTypeId) {
        DeviceTypeResp deviceTypeResp = null;
		DeviceType deviceType = deviceTypeBusinessService.getDeviceType(deviceTypeId);
        if (deviceType != null) {
            deviceTypeResp = new DeviceTypeResp();
            try{
                PropertyUtils.copyProperties(deviceTypeResp,deviceType);
            }catch (Exception e){
                LOGGER.info("getDeviceTypeById error",e);
                throw new BusinessException(DeviceTypeExceptionEnum.DEVICETYP_ERROR);
            }
        }
        return deviceTypeResp;
    }
    /**
     * chenxiaolin
     * override说明：
     */
    public List<DeviceTypeResp> findDeviceTypeList() {
        List<DeviceTypeResp> deviceTypeRespList = null;
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq("tenant_id",SaaSContextHolder.currentTenantId());
        List<DeviceType> deviceTypeList = deviceTypeService.selectList(wrapper);
        Map<String,ArrayList<SmartWraper>> deviceTypeMappingRobotTypeCache = new HashMap<String, ArrayList<SmartWraper>>();
        List<Long> selectIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(deviceTypeList)) {
            deviceTypeRespList = new ArrayList<>();
            for (DeviceType deviceType : deviceTypeList) {
                DeviceTypeResp deviceTypeResp = new DeviceTypeResp();
                try{
                    PropertyUtils.copyProperties(deviceTypeResp,deviceType);
                    ArrayList<SmartWraper> smart = new ArrayList<>();
                    deviceTypeResp.setSmart(smart);
                    deviceTypeMappingRobotTypeCache.put(deviceTypeResp.getId().toString(), smart);
                    selectIds.add(deviceTypeResp.getId());
                }catch (Exception e){
                    LOGGER.info("findDeviceTypeList error",e);
                    throw new BusinessException(DeviceTypeExceptionEnum.DEVICETYP_ERROR);
                }
                deviceTypeRespList.add(deviceTypeResp);
            }
            List<SmartDeviceType> smarts = smartDeviceTypeService.selectList(new EntityWrapper<SmartDeviceType>()
            		.in("device_type_id", selectIds));
            for (SmartDeviceType smartDeviceType : smarts) {
            	String key = smartDeviceType.getDeviceTypeId().toString();
            	SmartWraper sw = new SmartWraper();
            	sw.setId(smartDeviceType.getId());
            	sw.setSmart(smartDeviceType.getSmart());
            	sw.setCode(smartDeviceType.getSmartType());
            	deviceTypeMappingRobotTypeCache.get(key).add(sw);
			}
        }
        return deviceTypeRespList;
    }

    /**
     * override说明：
     * @author chenxiaolin
     */
	@Override
	public List<DeviceTypeResp> getDeviceTypeByCataLogId(@RequestParam(value = "catalogId") Long catalogId) {
//		Long tenantId = SaaSContextHolder.currentTenantId();
		EntityWrapper wrapper = new EntityWrapper();
		wrapper.eq("device_catalog_id",catalogId);
		wrapper.eq("tenant_id",SaaSContextHolder.currentTenantId());
		List<DeviceType> types = deviceTypeService.selectList(wrapper);
		List<DeviceTypeResp> res = new ArrayList<DeviceTypeResp>();
		if (res != null)
			types.forEach((type) -> {
				DeviceTypeResp r = new DeviceTypeResp();
				BeanUtils.copyProperties(type, r);
				res.add(r);
			});
		return res;
	}

	@Override
	public Page<DeviceTypeResp> getDeviceTypeByCondition(@RequestBody DeviceTypeReq req) {

		return deviceTypeService.getDeviceTypeByCondition(req);
	}

	/**
	 * override说明：
	 * @author chenxiaolin
	 */
	@Transactional
	public boolean addDeviceType(@RequestBody DeviceTypeReq req) {
		AssertUtils.notEmpty(req.getName(),"deviceType.name.notnull");
		AssertUtils.notEmpty(req.getDeviceCatalogId(),"deviceCatalogId.notnull");
		Long userId = SaaSContextHolder.getCurrentUserId();
		req.setId(null);
		DeviceType type = new DeviceType();
		BeanUtils.copyProperties(req, type);
		type.setCreateTime(new Date());
		if (req.getTenantId() != null) {
			type.setTenantId(req.getTenantId());
		} else {
			type.setTenantId(SaaSContextHolder.currentTenantId());
		}
		type.setUpdateBy(userId);
		type.setCreateBy(userId);
		type.setImg(req.getImg());
		deviceTypeService.insert(type);

		//smartDeviceTypeService.saveOrUpdateBatchSmartDeviceType(type.getTenantId(), userId, type.getId(), req.getSmart());
        updateDeviceTypeGoods(type.getId(),req.getGoodsList(),userId);
		return true;
	}

	/**
	 * override说明：
	 * @author chenxiaolin
	 */
	@Transactional
	public boolean updateDeviceType(@RequestBody DeviceTypeReq req) {
		AssertUtils.notEmpty(req.getId(),"deviceType.id.notnull");
		AssertUtils.notEmpty(req.getName(),"deviceType.name.notnull");
		AssertUtils.notEmpty(req.getDeviceCatalogId(),"deviceCatalogId.notnull");
		Long userId = SaaSContextHolder.getCurrentUserId();

		DeviceType deviceType = deviceTypeBusinessService.getDeviceType(req.getId());
		if (deviceType == null) {
			throw new BusinessException(DeviceTypeExceptionEnum.DEVICETYP_NOT_EXIST);
		}
		List<SmartWraper> smart = req.getSmart();
		List<SmartDeviceType> add = new ArrayList<SmartDeviceType>(10);
		List<SmartDeviceType> ups = new ArrayList<SmartDeviceType>(10);
		List<Long> deletes = new ArrayList<Long>(10);
		if (smart != null && !smart.isEmpty()) {
			smart.forEach((s) -> {
				if (s.getId() == null) {
					SmartDeviceType sd = new SmartDeviceType();
					sd.setSmart(s.getSmart());
					sd.setDeviceTypeId(req.getId());
					sd.setSmartType(s.getCode());
					sd.setCreateBy(userId);
					sd.setTenantId(req.getTenantId());
					sd.setCreateTime(new Date());
					add.add(sd);
				} else {
					if(s.getCode()!= null && !s.getCode().isEmpty()) {
						SmartDeviceType sd = new SmartDeviceType();
						sd.setSmartType(s.getCode());
						sd.setUpdateBy(userId);
						sd.setUpdateTime(new Date());
						sd.setTenantId(req.getTenantId());
						ups.add(sd);
					}else{
						deletes.add(s.getId());
					}
				}

			});
			if (!add.isEmpty()) {
				smartDeviceTypeService.insertBatch(add);
			}
			if (!ups.isEmpty()) {
				smartDeviceTypeService.updateBatchById(ups);
			}
			if(!deletes.isEmpty()){
				smartDeviceTypeService.deleteBatchIds(deletes);
				deletes.clear();
			}
		}
		updateDeviceTypeGoods(req.getId(),req.getGoodsList(),userId);
		DeviceType type = new DeviceType();
		BeanUtils.copyProperties(req, type);
		type.setUpdateBy(userId);
		type.setUpdateTime(new Date());
		type.setImg(req.getImg());
		deletes.add(type.getId());
		DeviceTypeCacheCoreUtils.removeDataList(deletes, VersionEnum.V1);
		return deviceTypeService.updateById(type);
	}

	public void updateDeviceTypeGoods(Long deviceTypeId, List<SaveDeviceTypeToGoodsReq> list,Long userId) {
		Map<String, Object> delparams = Maps.newHashMap();
		delparams.put("device_type_id", deviceTypeId);
		/*
		 * 删除device_type_to_goods和smart_device_type
		 */
		this.deviceTypeToGoodsService.deleteByMap(delparams);
		this.smartDeviceTypeService.deleteByMap(delparams);
		/*
		 * 构建需要保存的数据
		 */
		if (com.baomidou.mybatisplus.toolkit.CollectionUtils.isNotEmpty(list)) {
			EntityWrapper wrapper = new EntityWrapper();
			List<GoodsSmart> goodsSmarts = this.goodsSmartService.selectList(wrapper);
			Map<String, Integer> goodsSmartMap = goodsSmarts.stream().collect(Collectors.toMap(GoodsSmart::getGoodsCode, GoodsSmart::getSmart, (k1, k2) -> k1));
			List<DeviceTypeToGoods> deviceTypeToGoodsList = new ArrayList<>();
			List<SmartDeviceType> smartDeviceTypeList = new ArrayList<>();
			Date nowDate = new Date();
			list.forEach(v -> {
				DeviceTypeToGoods deviceTypeToGoods = new DeviceTypeToGoods();
				deviceTypeToGoods.setDeviceTypeId(deviceTypeId);
				deviceTypeToGoods.setGoodsCode(v.getGoodsCode());
				deviceTypeToGoods.setCreateTime(nowDate);
				deviceTypeToGoods.setCreateBy(userId);
				deviceTypeToGoodsList.add(deviceTypeToGoods);
				if (StringUtils.isNotEmpty(v.getSubCode())) {
					SmartDeviceType smartDeviceType = new SmartDeviceType();
					smartDeviceType.setDeviceTypeId(deviceTypeId);
					smartDeviceType.setSmartType(v.getSubCode());
					smartDeviceType.setSmart(goodsSmartMap.get(v.getGoodsCode()));
					smartDeviceType.setCreateBy(userId);
					smartDeviceType.setCreateTime(nowDate);
					smartDeviceTypeList.add(smartDeviceType);
				}
			});
			/*
			 * 全量新增
			 */
			if(com.baomidou.mybatisplus.toolkit.CollectionUtils.isNotEmpty(deviceTypeToGoodsList)){
				deviceTypeToGoodsService.insertBatch(deviceTypeToGoodsList);
			}
			if(com.baomidou.mybatisplus.toolkit.CollectionUtils.isNotEmpty(smartDeviceTypeList)){
				smartDeviceTypeService.insertBatch(smartDeviceTypeList);
			}
		}
	}
	/**
	 * override说明：
	 * @author chenxiaolin
	 */
	@Transactional
	public boolean deleteByDeviceTypeId(@RequestParam(value = "deviceTypeId") Long deviceTypeId) {
		AssertUtils.notEmpty(deviceTypeId,"deviceType.id.notnull");
		EntityWrapper<Product> peq = new EntityWrapper<>();
		peq.eq("device_type_id", deviceTypeId);
		int count = productService.selectCount(peq);
		if (count > 0) {
			throw new BusinessException(DeviceTypeExceptionEnum.DEVICETYP_IS_USED_PRODUCT);
		}

		EntityWrapper delQ2 = new EntityWrapper<>();
		delQ2.eq("device_type_id", deviceTypeId);

		deviceTypeBusinessService.deleteByDeviceTypeId(deviceTypeId);
		smartDeviceTypeService.delete(new EntityWrapper<SmartDeviceType>().eq("device_type_id", deviceTypeId));
		deviceTypeToServiceModuleService.delete(delQ2);
		deviceTypeToStyleService.delete(delQ2);
		return deviceTypeDataPointService.delete(delQ2);
	}

	/**
     * override说明：
     * @author chenxiaolin
     */
	@Transactional
	public boolean deleteByDeviceTypeIds(@RequestBody ArrayList<Long> ids) {
		ids.forEach((id) -> {
			deleteByDeviceTypeId(id);
		});
		DeviceTypeCacheCoreUtils.removeDataList(ids, VersionEnum.V1);
		return true;
	}

	@Override
	@Transactional
	public boolean addDataPoint(@RequestBody DeviceType2PointsReq req) {
		Long userId = SaaSContextHolder.getCurrentUserId();
		Long tenantId = SaaSContextHolder.currentTenantId();
		List<DeviceTypeDataPoint> dts = new ArrayList<>();
		Long deviceTypeId = req.getDeviceTypeId();
		List<Long> ds = req.getPointIds();
		for (Long id : ds) {
			DeviceTypeDataPoint dp = new DeviceTypeDataPoint();
			dp.setDataPointId(id);
			dp.setDeviceTypeId(deviceTypeId);
			dp.setCreateTime(new Date());
			dp.setCreateBy(userId);
			dts.add(dp);
		}
		EntityWrapper<DeviceTypeDataPoint> eq = new EntityWrapper<>();
		eq.eq("device_type_id", deviceTypeId);
		LOGGER.info("先删除后插入");
		if (deviceTypeDataPointService.delete(eq)) {
			return deviceTypeDataPointService.insertBatch(dts);
		}
		return false;
	}
    @Override
    public String getSmartCode(@RequestParam("keyEnum")Integer keyEnum, @RequestParam("deviceTypeId")Long deviceTypeId) {
        SmartDeviceType smart =  smartDeviceTypeService.selectOne(new EntityWrapper<SmartDeviceType>().eq("device_type_id", deviceTypeId).
                eq("smart", keyEnum));
        if (smart != null) {
            return smart.getSmartType();
        }
        return null;
    }

    public List<DeviceTypeListResp> findDeviceTypeListByCatalogId(@RequestParam(value = "catalogId") Long catalogId) {
        AssertUtils.notNull(catalogId, "deviceCatalogId.notnull");
		List<DeviceTypeListResp> respList = Lists.newArrayList();
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq("device_catalog_id", catalogId);
		wrapper.eq("whether_soc", DeviceType.IS_NOT_SOC);
        List<DeviceType> typeList = deviceTypeService.selectList(wrapper);
        if (!CollectionUtils.isEmpty(typeList)) {
            respList = Lists.newArrayList();
            for (DeviceType res : typeList) {
                DeviceTypeListResp target = new DeviceTypeListResp();
                target.setId(res.getId());
                target.setName(res.getName());
                target.setCode(res.getType());
                target.setImg(res.getImg());
				target.setWhetherSoc(res.getWhetherSoc());
				target.setIftttType(res.getIftttType());
				EntityWrapper smartTypeWrapper = new EntityWrapper();
				smartTypeWrapper.eq("device_type_id", res.getId());
				List<SmartDeviceType> smartDeviceTypes = smartDeviceTypeService.selectList(smartTypeWrapper);
				List<SmartDeviceTypeResp> smartDeviceTypeResps = Lists.newArrayList();
				if (!CollectionUtils.isEmpty(smartDeviceTypes)) {
					smartDeviceTypes.forEach(m -> {
						SmartDeviceTypeResp smartDeviceTypeResp = new SmartDeviceTypeResp();
						smartDeviceTypeResp.setSmartType(m.getSmartType());
						smartDeviceTypeResp.setSmart(m.getSmart());
						smartDeviceTypeResps.add(smartDeviceTypeResp);
					});
				}
				target.setSmartDeviceTypeResps(smartDeviceTypeResps);
                respList.add(target);
            }
        }
        return respList;
    }

	public List<DeviceTypeListResp> findAllDeviceTypeList() {
		LOGGER.info("findAllDeviceTypeList....begin.");
		List<DeviceTypeListResp> respList = Lists.newArrayList();
		EntityWrapper wrapper = new EntityWrapper();
		wrapper.eq("whether_soc", DeviceType.IS_SOC);
		List<DeviceType> typeList = deviceTypeService.selectList(wrapper);
		if (!CollectionUtils.isEmpty(typeList)) {
			respList = Lists.newArrayList();
			for (DeviceType res : typeList) {
				DeviceTypeListResp target = new DeviceTypeListResp();
				target.setId(res.getId());
				target.setName(res.getName());
				target.setCode(res.getType());
				target.setImg(res.getImg());
				target.setWhetherSoc(res.getWhetherSoc());
				target.setIftttType(res.getIftttType());
				respList.add(target);
			}
		}
		return respList;
	}

	@Override
	public void delete(@RequestParam("id") Long id,@RequestParam("tenantId") Long tenantId) {
		deviceTypeService.delete(id,tenantId);

		DeviceTypeCacheCoreUtils.removeData(id, VersionEnum.V1);
	}


	@Override
	public List<DeviceTypeResp> getByIds(@RequestParam("ids") List<Long> ids) {
		return deviceTypeService.getByIds(ids);
	}

	/**
	  * @despriction：根据ifttt类型过滤
	  * @author  yeshiyuan
	  * @created 2018/11/23 17:21
	  */
	@Override
	public List<DeviceTypeResp> getByIdsAndIfffType(@RequestParam("ids") List<Long> ids, @RequestParam("iftttType") String iftttType) {
		return deviceTypeService.getByIdsAndIfffType(ids, iftttType);
	}

	@Override
	public List<String> getDeviceTypeNameByIds(@RequestParam("ids") List<Long> ids) {
		return deviceTypeService.getDeviceTypeNameByIds(ids);
	}

	@Override
	public List<DeviceTypeResp> getDeviceTypeIdAndNameByIds(@RequestParam("ids")List<Long> deviceTypeIds) {
		return deviceTypeService.getDeviceTypeIdAndNameByIds(deviceTypeIds);
	}
}
