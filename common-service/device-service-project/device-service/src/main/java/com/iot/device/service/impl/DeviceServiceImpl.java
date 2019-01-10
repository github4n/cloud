package com.iot.device.service.impl;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.iot.common.beans.BeanUtil;
import com.iot.common.helper.Page;
import com.iot.device.core.service.DeviceServiceCoreUtils;
import com.iot.device.core.service.DeviceTypeServiceCoreUtils;
import com.iot.device.core.utils.BeanCopyUtils;
import com.iot.device.mapper.DeviceMapper;
import com.iot.device.mapper.DeviceStateMapper;
import com.iot.device.mapper.DeviceStatusMapper;
import com.iot.device.mapper.DeviceTypeMapper;
import com.iot.device.model.Device;
import com.iot.device.model.DeviceType;
import com.iot.device.model.Product;
import com.iot.device.service.IDeviceExtendService;
import com.iot.device.service.IDeviceService;
import com.iot.device.service.IDeviceStatusService;
import com.iot.device.service.IProductService;
import com.iot.device.vo.req.CommDeviceInfoReq;
import com.iot.device.vo.req.DevTypePageReq;
import com.iot.device.vo.req.DeviceBusinessTypeIDSwitchReq;
import com.iot.device.vo.req.DevicePageReq;
import com.iot.device.vo.req.device.ListDeviceByParamsReq;
import com.iot.device.vo.req.device.PageDeviceInfoReq;
import com.iot.device.vo.rsp.DevicePropertyInfoResp;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.IftttDeviceResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceByParamsRespVo;
import com.iot.device.vo.rsp.device.PageDeviceInfoRespVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.iot.device.comm.utils.DeviceDateUtils.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-04-17
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {
    public static final Logger LOGGER = LoggerFactory.getLogger(DeviceServiceImpl.class);
    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DeviceStateMapper deviceStateMapper;

    @Autowired
    private DeviceStatusMapper deviceStatusMapper;

    @Autowired
    private DeviceTypeMapper deviceTypeMapper;

    @Autowired
    private IDeviceStatusService deviceStatusService;

    @Autowired
    private IDeviceExtendService deviceExtendService;
    
    @Autowired
    private IProductService productService;

    private static Map<String, Object> makeData(Long spaceId, String deviceId, String dateType, String deviceType) {
        int loop = 24;  // 循环次数 day:24, week:7, month:实际情况
        String dateFormat = "%Y-%m-%d %H";
        String dateStart = "";
        String dateEnd = "";
        String msg = "";
        int code = 1;
        // deviceType = "Sensor_Humiture";//Sensor_Humiture    Sensor_Mulitlevel   Sensor_volume


        List<String> completeDates = new ArrayList<>();
        List<Map<String, Object>> datas = new ArrayList<>();
        if (org.apache.commons.lang3.StringUtils.isBlank(dateType)) {
            dateType = "day";
        } else if (!"day".equals(dateType)) {
            dateFormat = "%Y-%m-%d";
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("spaceId", spaceId);  // 限定是这个空间下的设备
        params.put("deviceId", deviceId);    // 空间下具体设备-----转化成主键long id
        params.put("dateType", dateType);
        params.put("dateFormat", dateFormat);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        if (org.apache.commons.lang3.StringUtils.isBlank(dateType) || "day".equals(dateType)) {
            loop = 24;
            completeDates = completeDay(cal, loop, completeDates);
            dateStart = completeDates.get(completeDates.size() - 1);
            dateEnd = completeDates.get(0);
        } else if ("week".equals(dateType)) {
            loop = 7;
            completeDates = completeWeek(cal, loop, completeDates);
            dateStart = completeDates.get(completeDates.size() - 1);
            dateEnd = completeDates.get(0);
        } else if ("month".equals(dateType)) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DATE, 1);//把日期设置为当月第一天
            c.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
            loop = c.get(Calendar.DATE);

            completeDates = completeMonth(cal, loop, completeDates);
            dateStart = completeDates.get(completeDates.size() - 1);
            dateEnd = completeDates.get(0);
        }
        params.put("dateStart", dateStart);
        params.put("dateEnd", dateEnd);

        List<String> propertyList = new ArrayList<>();  // 获取这个设备所有上报属性
        propertyList.add("Temp");
        propertyList.add("Hum");
        propertyList.add("Illuminance");
        // 循环属性
        for (String property : propertyList) {
            List<Float> reports = new ArrayList<>();  // 保存每个属性的数据
            Map<String, Object> reportData = new HashMap<>();

            String propertyName = property;  // 属性名称

            params.put("propertyName", propertyName);

            List<Map<String, Object>> reportList = new ArrayList<>();

            for (int i = completeDates.size() - 1; i >= 0; i--) {  // String completeDate: completeDates 从尾到头循环
                String completeDate = completeDates.get(i);
                Float val = Float.valueOf(0);
                if (reportList != null && reportList.size() > 0) {
                    for (Map<String, Object> report : reportList) {
                        String groupTime = String.valueOf(report.get("groupTime"));
                        String propertyValue = String.valueOf(report.get("propertyValue"));

                        if (completeDate.equals(groupTime)) {
                            val = Float.valueOf(propertyValue);
                            break;
                        } else {
                            if (reports.size() == 0) {
                                val = Float.valueOf(0);
                            } else {
                                val = reports.get(reports.size() - 1);
                            }
                        }
                    }
                    reports.add(val);
                } else {
                    Random random = new Random();
                    Integer value = 0;
                    while (true) {
                        value = random.nextInt(40);
                        if (value >= 20) {
                            break;
                        }
                    }
                    reports.add(Float.parseFloat(String.valueOf(value)));
                }
            }
            reportData.put("name", propertyName);
            reportData.put("data", reports);
            datas.add(reportData);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        Collections.reverse(completeDates);
        result.put("datas", datas);
        result.put("time", completeDates);
        result.put("msg", msg);
        result.put("code", code);
        result.put("deviceType", deviceType);

        return result;
    }

    @Override
    public List<GetDeviceInfoRespVo> findDirectDeviceListByVenderCode(Long tenantId, Long locationId,String venderFlag,Integer isDirectDevice) {
    	isDirectDevice=isDirectDevice==null?1:isDirectDevice;
        return deviceMapper.selectDeviceListByIsDirectDeviceAndVenderCode(isDirectDevice, tenantId, locationId,venderFlag);
    }


    @Override
    public List<DevicePropertyInfoResp> findDeviceListByDeviceIds(List<String> deviceIds) {
        List<DevicePropertyInfoResp> devicePropertyInfoRespList = null;
        List<Device> deviceList = DeviceServiceCoreUtils.findDeviceListByDeviceIds(deviceIds);
        if (!CollectionUtils.isEmpty(deviceList)) {
            devicePropertyInfoRespList = Lists.newArrayList();
            for (Device device : deviceList) {
                DevicePropertyInfoResp target = new DevicePropertyInfoResp();
                BeanCopyUtils.copyDeviceProperty(device, target);
                devicePropertyInfoRespList.add(target);
            }
        }
        return devicePropertyInfoRespList;
    }

    @Override
    public List<DeviceResp> findDeviceListByParentId(String parentId) {

        List<Device> deviceList = DeviceServiceCoreUtils.findChildDeviceListByParentDeviceId(parentId);
        if (!CollectionUtils.isEmpty(deviceList)) {
            List<DeviceResp> deviceRespList = Lists.newArrayList();
            BeanCopyUtils.copyDeviceList(deviceList, deviceRespList);
            return deviceRespList;
        }
        return null;
    }

    @Override
    public Map<String, Object> findDataReport(Long spaceId, String deviceId, String dateType,String deviceType1) {
        int loop = 24;  // 循环次数 day:24, week:7, month:实际情况
        String dateFormat = "%Y-%m-%d %H";
        String dateStart = "";
        String dateEnd = "";
        String msg = "";
        int code = 1;
        String deviceType = "";

        //获取设备类型
        EntityWrapper<Device> wrapper = new EntityWrapper<>();
        wrapper.eq("uuid", deviceId);
        Device device = super.selectOne(wrapper);
        if (device == null) {
            // throw new BusinessException(DeviceExceptionEnum.DEVICE_NOT_EXIST);
            //如果设备不存在造假数据
            return makeData(spaceId, deviceId, dateType,deviceType1);
        }

        if (device.getDeviceTypeId() != null) {
            Long deviceTypeId = device.getDeviceTypeId();

            DeviceType deviceTypeTemp = DeviceTypeServiceCoreUtils.getDeviceTypeByDeviceTypeId(deviceTypeId);
            if (deviceTypeTemp != null) {
                deviceType = deviceTypeTemp.getType();
            }
        }
        List<String> completeDates = new ArrayList<>();
        List<Map<String, Object>> datas = new ArrayList<>();
        if (spaceId != null) {
            if (org.apache.commons.lang3.StringUtils.isBlank(dateType)) {
                dateType = "day";
            } else if (!"day".equals(dateType)) {
                dateFormat = "%Y-%m-%d";
            }
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("spaceId", spaceId);  // 限定是这个空间下的设备
            params.put("deviceId", device.getUuid());    // 空间下具体设备-----转化成主键long id
            params.put("dateType", dateType);
            params.put("dateFormat", dateFormat);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            if (org.apache.commons.lang3.StringUtils.isBlank(dateType) || "day".equals(dateType)) {
                loop = 24;
                completeDates = completeDay(cal, loop, completeDates);
                dateStart = completeDates.get(completeDates.size() - 1);
                dateEnd = completeDates.get(0);
            } else if ("week".equals(dateType)) {
                loop = 7;
                completeDates = completeWeek(cal, loop, completeDates);
                dateStart = completeDates.get(completeDates.size() - 1);
                dateEnd = completeDates.get(0);
            } else if ("month".equals(dateType)) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.DATE, 1);//把日期设置为当月第一天
                c.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
                loop = c.get(Calendar.DATE);

                completeDates = completeMonth(cal, loop, completeDates);
                dateStart = completeDates.get(completeDates.size() - 1);
                dateEnd = completeDates.get(0);
            }
            params.put("dateStart", dateStart);
            params.put("dateEnd", dateEnd);
            List<Map<String, Object>> propertyList = deviceStateMapper.selectPropertyName(params);  // 获取这个设备所有上报属性


            // 循环属性
            if (propertyList != null && propertyList.size() > 0) {
                for (Map<String, Object> property : propertyList) {
                    List<Float> reports = new ArrayList<>();  // 保存每个属性的数据
                    Map<String, Object> reportData = new HashMap<>();

                    String propertyName = String.valueOf(property.get("propertyName"));  // 属性名称

                    params.put("propertyName", propertyName);

                    List<Map<String, Object>> reportList = deviceStateMapper.selectDataReport(params);

                    for (int i = completeDates.size() - 1; i >= 0; i--) {  // String completeDate: completeDates 从尾到头循环
                        String completeDate = completeDates.get(i);
                        Float val = Float.valueOf(0);
                        if (reportList != null && reportList.size() > 0) {
                            for (Map<String, Object> report : reportList) {
                                String groupTime = String.valueOf(report.get("groupTime"));
                                String propertyValue = String.valueOf(report.get("propertyValue"));

                                if (completeDate.equals(groupTime)) {
                                    val = Float.valueOf(propertyValue);
                                    break;
                                } else {
                                    if (reports.size() == 0) {
                                        val = Float.valueOf(0);
                                    } else {
                                        val = reports.get(reports.size() - 1);
                                    }
                                }
                            }
                            reports.add(val);
                        } else {
                            reports.add(Float.valueOf(0));
                        }
                    }
                    reportData.put("name", propertyName);
                    reportData.put("data", reports);
                    datas.add(reportData);
                }
            } else {
                code = 10722;
                msg = "There is no device property in the report";
            }
        } else {
            code = 10723;
            msg = "There is no spaceId in the request parameter";
        }
        Map<String, Object> result = new HashMap<String, Object>();
        Collections.reverse(completeDates);
        result.put("datas", datas);
        result.put("time", completeDates);
        result.put("msg", msg);
        result.put("code", code);
        result.put("deviceType", deviceType);
        return result;
    }


    public Page<DeviceResp> findDirectDevicePageToCenter(DevicePageReq pageReq) {
        Page<DeviceResp> pageResult = new Page<>();
        Pagination pagination = new Pagination(pageReq.getPageNum(), pageReq.getPageSize());
        List<DeviceResp> userDeviceProductRespList = deviceMapper.selectDirectDevicePageToCenter(pagination, pageReq);
        commonDeviceName(userDeviceProductRespList);
        pageResult.setPageNum(pageReq.getPageNum());
        pageResult.setPageSize(pageReq.getPageSize());
        pageResult.setTotal(pagination.getTotal());
        pageResult.setPages(pagination.getPages());
        pageResult.setResult(userDeviceProductRespList);
        return pageResult;
    }
    
    private void commonDeviceName(List<DeviceResp> userDeviceProductRespList) {
		if (!CollectionUtils.isEmpty(userDeviceProductRespList)) {
			for (DeviceResp deviceResp : userDeviceProductRespList) {
				if (deviceResp.getParentId() != null) {
					Device device = DeviceServiceCoreUtils.getDeviceInfoByDeviceId(deviceResp.getParentId());
					if (device != null) {
						deviceResp.setName(device.getName() + "-" + deviceResp.getName());
					}

				}
				if (deviceResp.getProductId() != null) {
					Product product = productService.getProductByProductId(deviceResp.getProductId());
					LOGGER.info(" product ==========================111 " + product.getProductName() );
					if (product != null && product.getProductName() != null) {
						LOGGER.info(" product ==========================222 " + product.getProductName() );
						deviceResp.setProductName(product.getProductName());
					}
				}
			}
		}
	}

    public Page<DeviceResp> findUnDirectDevicePage(DevicePageReq pageReq) {
        Page<DeviceResp> pageResult = new Page<>();
        Pagination pagination = new Pagination(pageReq.getPageNum(), pageReq.getPageSize());
        List<DeviceResp> userDeviceProductRespList = deviceMapper.selectUnDirectDevicePage(pagination, pageReq);
        pageResult.setPageNum(pageReq.getPageNum());
        pageResult.setPageSize(pageReq.getPageSize());
        pageResult.setTotal(pagination.getTotal());
        pageResult.setPages(pagination.getPages());
        pageResult.setResult(userDeviceProductRespList);
        return pageResult;
    }

    public List<DeviceResp> findAllUnDirectDeviceList(DevicePageReq pageReq) {
        List<DeviceResp> userDeviceProductRespList = deviceMapper.selectAllUnDirectDeviceList(pageReq);
        return userDeviceProductRespList;
    }

    @Override
    public List<GetDeviceInfoRespVo> findDirectDeviceByDeviceCatgory(String venderCode,Long tenantId,
    		Long locationId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("isDirectDevice", 1);
        params.put("venderFlag", venderCode);
        params.put("tenantId", tenantId);
        params.put("locationId", locationId);
        return deviceMapper.findDeviceByDeviceCatgory(params);
    }


    @Override
    public Integer getCountByDeviceIdsAndBusinessTypesAndSwitch(DeviceBusinessTypeIDSwitchReq req) {
        return deviceMapper.getCountByDeviceIdsAndBusinessTypesAndSwitch(req);
    }


    @Override
    public Page<DeviceResp> findDevPageByProductId(Integer pageNum, Integer pageSize, Long productId) {
        com.baomidou.mybatisplus.plugins.Page<Device> page = new com.baomidou.mybatisplus.plugins.Page<>(pageNum, pageSize);

        Page<DeviceResp> pageResult = new Page<>(pageNum, pageSize);
        Pagination pagination = new Pagination(pageNum, pageSize);
        List<DeviceResp> deviceRespList = deviceMapper.findDevPageByProductId(pagination, productId);
        pageResult.setTotal(pagination.getTotal());
        pageResult.setPages(pagination.getPages());
        pageResult.setResult(deviceRespList);
        return pageResult;

    }

    @Override
    public List<DeviceResp> findDeviceByCondition(DeviceBusinessTypeIDSwitchReq req) {
        return deviceMapper.findDeviceByCondition(req);
    }

    @Override
    public List<IftttDeviceResp> findIftttDeviceList(CommDeviceInfoReq req) {
        List<IftttDeviceResp> resp = deviceMapper.findIftttDeviceList(req);
        return resp;
    }

    @Override
    public Page<DeviceResp> queryAirCondition(DevicePageReq pageReq) {
        Page<DeviceResp> pageResult = new Page<>();
        Pagination pagination = new Pagination(pageReq.getPageNum(), pageReq.getPageSize());
        List<DeviceResp> userDeviceProductRespList = deviceMapper.queryAirCondition(pagination, pageReq);
        pageResult.setPageNum(pageReq.getPageNum());
        pageResult.setPageSize(pageReq.getPageSize());
        pageResult.setTotal(pagination.getTotal());
        pageResult.setPages(pagination.getPages());
        pageResult.setResult(userDeviceProductRespList);
        return pageResult;
    }

    @Override
    public Map<String, Map<String,Long>> findUuidProductIdMap(List<String> uuIdList) {
        return deviceMapper.findUuidProductIdMap(uuIdList);
    }

    /**
     * 描述：查设备id与租户id对应关系
     * @author nongchongwei
     * @date 2018/11/1 16:40
     * @param
     * @return
     */
    @Override
    public Map<String, Map<String,Long>> findUuidTenantIdMap(List<String> uuIdList) {
        return deviceMapper.findUuidTenantIdMap(uuIdList);
    }

    @Override
    public List<DeviceResp> getVersionByDeviceIdList(List<String> deviceIdList) {
        return deviceMapper.getVersionByDeviceIdList(deviceIdList);
    }


    @Override
	public List<GetDeviceInfoRespVo> selectAllDeviceToCenter(DevicePageReq pageReq){
		return deviceMapper.selectAllDeviceToCenter(pageReq);
	}

    @Override
    public List<Device> findListByDeviceIds(Long tenantId, List<String> deviceIds) {
        List<Device> resultDataList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(deviceIds)) {
            return resultDataList;
        }
        EntityWrapper wrapper = new EntityWrapper<>();
        if (deviceIds.size() == 1) {
            wrapper.eq("uuid", deviceIds.get(0));
        } else {
            wrapper.in("uuid", deviceIds);
        }
        if (null != tenantId) {
            wrapper.eq("tenant_id", tenantId);
        }
        resultDataList = super.selectList(wrapper);
        return resultDataList;
    }

    @Override
    public Device getByDeviceId(Long tenantId, String deviceId) {
        Device resultData = null;
        if (StringUtils.isEmpty(deviceId)) {
            return resultData;
        }
        EntityWrapper wrapper = new EntityWrapper<>();
        wrapper.eq("uuid", deviceId);
        if (null != tenantId) {
            wrapper.eq("tenant_id", tenantId);
        }
        resultData = super.selectOne(wrapper);
        return resultData;
    }

    @Override
    public List<Device> findListByDeviceParentId(Long tenantId, String parentDeviceId) {
        EntityWrapper wrapper = new EntityWrapper<>();
        wrapper.eq("parent_id", parentDeviceId);
        if (null != tenantId) {
            wrapper.eq("tenant_id", tenantId);
        }
        return super.selectList(wrapper);
    }

    @Override
    public Page<PageDeviceInfoRespVo> findPageByParams(PageDeviceInfoReq params) {
        Page<PageDeviceInfoRespVo> pageResult = new Page<>();
        EntityWrapper wrapper = new EntityWrapper();
        if (params.getTenantId() != null) {
            wrapper.eq("tenant_id", params.getTenantId());
        }
        if (!StringUtils.isEmpty(params.getParentId())) {
            wrapper.eq("parent_id", params.getParentId());
        }
        if (!StringUtils.isEmpty(params.getDeviceName())) {
            wrapper.like("name", params.getDeviceName(), SqlLike.DEFAULT);
        }
        if (params.getIsDirectDevice() != null) {
            wrapper.eq("is_direct_device", params.getIsDirectDevice());
        }
        if (params.getProductId() != null) {
            wrapper.eq("product_id", params.getProductId());
        }
        if (params.getDeviceTypeId() != null) {
            wrapper.eq("device_type_id", params.getDeviceTypeId());
        }
        if (params.getLocationId() != null) {
            wrapper.eq("location_id", params.getLocationId());
        }
        if (params.getOrgId() != null) {
            wrapper.eq("org_id", params.getOrgId());
        }
        if (!CollectionUtils.isEmpty(params.getProductIds())) {
            wrapper.in("product_id", params.getProductIds());
        }
        com.baomidou.mybatisplus.plugins.Page<Device> resultDevicePage = super.selectPage(new com.baomidou.mybatisplus.plugins.Page<>(params.getPageNumber(), params.getPageSize()), wrapper);

        List<PageDeviceInfoRespVo> deviceRespList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(resultDevicePage.getRecords())) {
            resultDevicePage.getRecords().forEach(device -> {
                PageDeviceInfoRespVo target = new PageDeviceInfoRespVo();
                BeanUtils.copyProperties(device, target);
                deviceRespList.add(target);
            });
        }
        pageResult.setPageNum(params.getPageNumber());
        pageResult.setPageSize(params.getPageSize());
        pageResult.setTotal(resultDevicePage.getTotal());
        pageResult.setPages(resultDevicePage.getPages());
        pageResult.setResult(deviceRespList);
        return pageResult;
    }

    @Override
    public Page<DeviceResp> findPageByDeviceTypeList(DevTypePageReq pageReq) {
        Page<DeviceResp> pageResult = new Page<>();
        com.baomidou.mybatisplus.plugins.Page page = new com.baomidou.mybatisplus.plugins.Page<>(pageReq.getPageNum(), pageReq.getPageSize());
        EntityWrapper wrapper = new EntityWrapper();
        if (!CollectionUtils.isEmpty(pageReq.getDeviceTypeList())) {
            if (pageReq.getDeviceTypeList().size() == 1) {
                wrapper.eq("dt.type", pageReq.getDeviceTypeList().get(0));
            } else {
                wrapper.in("dt.type", pageReq.getDeviceTypeList());
            }
        }
        if (!CollectionUtils.isEmpty(pageReq.getDeviceIds())) {
            if (pageReq.getDeviceIds().size() == 1) {
                wrapper.eq("p1.uuid", pageReq.getDeviceIds().get(0));
            } else {
                wrapper.in("p1.uuid", pageReq.getDeviceIds());
            }
        }
        if (pageReq.getTenantId() != null) {
            wrapper.eq("p1.tenant_id", pageReq.getTenantId());
            wrapper.eq("ds.tenant_id", pageReq.getTenantId());
        }
        if (pageReq.getOrgId() != null) {
            wrapper.eq("p1.org_id", pageReq.getOrgId());
        }
        List<DeviceResp> deviceRespList = deviceMapper.selectPageByDeviceTypeList(page, wrapper);
        pageResult.setPageNum(pageReq.getPageNum());
        pageResult.setPageSize(pageReq.getPageSize());
        pageResult.setTotal(page.getTotal());
        pageResult.setPages(page.getPages());
        pageResult.setResult(deviceRespList);
        return pageResult;
    }

	@Override
	public GetDeviceInfoRespVo getDeviceByDeviceIp(Long orgId, Long tenantId, String deviceIp) {
		GetDeviceInfoRespVo vo=null;
		List<Device> resultDataList = Lists.newArrayList();
        EntityWrapper wrapper = new EntityWrapper<>();
        if (org.apache.commons.lang.StringUtils.isNotBlank(deviceIp)) {
            wrapper.eq("ip", deviceIp);
        }
        if (null != tenantId) {
            wrapper.eq("tenant_id", tenantId);
        }
        if (null != orgId) {
            wrapper.eq("org_id", orgId);
        }
        resultDataList = super.selectList(wrapper);
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(resultDataList)) {
        	Device devie=resultDataList.get(0);
        	BeanUtil.copyProperties(devie, vo);
        }
		return vo;
	}

    @Override
    public List<ListDeviceByParamsRespVo> listByParams(ListDeviceByParamsReq params) {
        EntityWrapper wrapper = new EntityWrapper();
        if (params.getDeviceBusinessTypeId() != null) {
            wrapper.eq("p1.business_type_id", params.getDeviceBusinessTypeId());
        }
        if (params.getLocationId() != null) {
        	wrapper.eq("p1.location_id", params.getLocationId());
        }
        if (params.getOrgId() != null) {
            wrapper.eq("p1.org_id", params.getOrgId());
        }
        if (params.getTenantId() != null) {
        	wrapper.eq("p1.tenant_id", params.getTenantId());
        }
        if (params.getDeviceTypeId() != null) {
        	wrapper.eq("dt.id", params.getDeviceTypeId());
        }
        if (params.getProductId() != null) {
            wrapper.eq("p.id", params.getProductId());
        }
        if (!StringUtils.isEmpty(params.getDeviceType())) {
            wrapper.eq("dt.type", params.getDeviceType());
        }
        if (!CollectionUtils.isEmpty(params.getDeviceIds())) {
            wrapper.in("p1.uuid", params.getDeviceIds());
        }
        List<Device> deviceList = deviceMapper.selectListByParams(wrapper);

        List<ListDeviceByParamsRespVo> resultDataList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(deviceList)) {
            return resultDataList;
        }
        deviceList.forEach(device -> {
            ListDeviceByParamsRespVo target = new ListDeviceByParamsRespVo();
            BeanUtils.copyProperties(device, target);
            resultDataList.add(target);
        });
        return resultDataList;
    }
    
    public Page<DeviceResp> getGatewayAndSubDeviceList(DevicePageReq pageReq) {
        Page<DeviceResp> pageResult = new Page<>();
        Pagination pagination = new Pagination(pageReq.getPageNum(), pageReq.getPageSize());
        List<DeviceResp> userDeviceProductRespList = deviceMapper.getGatewayAndSubDeviceList(pagination, pageReq);
        pageResult.setPageNum(pageReq.getPageNum());
        pageResult.setPageSize(pageReq.getPageSize());
        pageResult.setTotal(pagination.getTotal());
        pageResult.setPages(pagination.getPages());
        pageResult.setResult(userDeviceProductRespList);
        return pageResult;
    }

	@Override
	public List<Long> getExistProductList(PageDeviceInfoReq params) {
		return deviceMapper.getExistProductList(params);
	}

	@Override
	public List<GetDeviceInfoRespVo> getDeviceListByParentId(CommDeviceInfoReq commDeviceInfoReq) {
		return deviceMapper.getDeviceListByParentId(commDeviceInfoReq);
	}
}
