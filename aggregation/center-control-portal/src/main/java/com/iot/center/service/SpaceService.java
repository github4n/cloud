package com.iot.center.service;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.iot.building.ifttt.vo.res.RuleResp;
import com.iot.common.exception.ResultMsg;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.building.allocation.api.EnvironmentPropertyApi;
import com.iot.building.index.api.IndexApi;
import com.iot.building.index.vo.IndexDetailReq;
import com.iot.building.index.vo.IndexDetailResp;
import com.iot.building.index.vo.IndexReq;
import com.iot.building.index.vo.IndexResp;
import com.iot.building.reservation.api.ReservationApi;
import com.iot.building.reservation.vo.ReservationReq;
import com.iot.building.reservation.vo.ReservationResp;
import com.iot.building.space.api.SpaceApi;
import com.iot.building.space.vo.CalendarReq;
import com.iot.building.space.vo.CalendarResp;
import com.iot.building.space.vo.DeploymentReq;
import com.iot.building.space.vo.DeploymentResp;
import com.iot.building.space.vo.LocationReq;
import com.iot.building.space.vo.LocationResp;
import com.iot.building.space.vo.QueryParamReq;
import com.iot.building.space.vo.SpaceBackgroundImgReq;
import com.iot.building.space.vo.SpaceBackgroundImgResp;
import com.iot.building.space.vo.SpaceExcelReq;
import com.iot.building.warning.api.WarningApi;
import com.iot.building.warning.vo.WarningReq;
import com.iot.building.warning.vo.WarningResp;
import com.iot.center.helper.BusinessExceptionEnum;
import com.iot.center.helper.Constants;
import com.iot.center.helper.SpaceEnum;
import com.iot.center.utils.ConstantUtil;
import com.iot.center.utils.ExcelUtils;
import com.iot.center.utils.HealthSensorPropertyUtils;
import com.iot.center.vo.IndexVo;
import com.iot.center.vo.SpaceEhiVO;
import com.iot.center.vo.SpaceVO;
import com.iot.common.beans.BeanUtil;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.common.util.CommonUtil;
import com.iot.control.space.vo.SpaceAndSpaceDeviceVo;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceReq;
import com.iot.control.space.vo.SpaceResp;
import com.iot.control.space.vo.SpaceVo;
import com.iot.device.api.CentralControlDeviceApi;
import com.iot.device.api.DeviceStateCoreApi;
import com.iot.device.vo.req.device.AddCommDeviceStateInfoReq;
import com.iot.device.vo.req.device.UpdateDeviceStateReq;
import com.iot.file.api.FileApi;
import com.iot.saas.SaaSContextHolder;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import com.iot.user.vo.LoginResp;
import com.iot.util.ToolUtils;

@Service
@Transactional
public class SpaceService {

	@Autowired
	private SpaceApi spaceApi;
	@Autowired
	private UserApi userApi;
	@Autowired
	private ReservationApi reservationApi;
	@Autowired
	private IndexApi indexApi;
	@Autowired
	private WarningApi warningApi;
	@Autowired
	private com.iot.control.space.api.SpaceApi commonSpaceApi;
	@Autowired
	private com.iot.control.space.api.SpaceDeviceApi commonSpaceDeviceApi;
	@Autowired
	private DeviceStateCoreApi deviceStateServiceApi;

	@Autowired
	private FileApi fileApi;

	@Autowired
	private CentralControlDeviceApi centralControlDeviceApi;

	@Autowired
	private EnvironmentPropertyApi environmentPropertyApi;

	public static final String LOCAL_FILE_UPLOAD_PATH = "local.file.upload-path";

	public static final String LOCAL_FILE_RELATIVE_PATH = "local.file.relative-path";

	public Page<SpaceVO> findSpaceByLocationId(Long locationId, Long tenantId, String name, int pageNumber,
			int pageSize) throws BusinessException {
		SpaceReq req =new SpaceReq();
		req.setTenantId(tenantId);
		req.setLocationId(locationId);
		req.setName(name);
		req.setPageSize(pageSize);
		req.setPageNumber(pageNumber);
		req.setReorder(true);
		PageInfo page =commonSpaceApi.findSpacePageByCondition(req);
		Page<SpaceVO> backPage=new Page();
		backPage.setTotal(page.getTotal());
		backPage.setPageNum(page.getPageNum());
		backPage.setPages(page.getPages());
		backPage.setPageSize(page.getPageSize());
		List<SpaceVO> voList = new ArrayList<SpaceVO>();
		if (!page.getList().isEmpty()) {
			String jsonStr=JSON.toJSONString(page.getList());
			List<SpaceResp> respList=JSON.parseArray(jsonStr, SpaceResp.class);
			for (SpaceResp space : respList) {
				changeVO(voList, space);
			}
		}
		backPage.setResult(voList);
		return backPage;
	}
	
	/**
	 * 对象转换
	 * 
	 * @param voList
	 * @param space
	 */
	public void changeVO(List<SpaceVO> voList, SpaceResp space) {
		SpaceVO vo = new SpaceVO();
		if (space.getCreateTime() != null) {
			vo.setCreateTime(ToolUtils.dateToSting(space.getCreateTime()));
		}
		vo.setId(space.getId().toString());
		vo.setName(space.getName());
		if (space.getParentId() != null) {
			SpaceResp sapce = findById(space.getTenantId(), space.getParentId());
			if (sapce != null) {
				vo.setParentId(sapce.getName());
			}
		}
		if (space != null && space.getUpdateTime() != null) {
			vo.setUpdateTime(ToolUtils.dateToSting(space.getUpdateTime()));
		}
		vo.setType(space.getType());
		vo.setSort(space.getSort());
		if (space.getType() !=null && space.getType().equals("ROOM") && space.getModel() != null && space.getModel() == 1) {
			vo.setType("MEETING");
		}
		DeploymentResp resp=spaceApi.findDeploymentById(space.getTenantId(),space.getOrgId(), space.getDeployId());
		vo.setDeployName(resp==null?null:resp.getDeployName());
		voList.add(vo);
	}

	public List<SpaceResp> findSpaceByLocationId(Long locationId,Long orgId, Long tenantId, String name) throws BusinessException {
		return spaceApi.findSpaceByLocationId(locationId,orgId, tenantId, name);
	}

	public List<SpaceResp> findSpaceUnMount(SpaceReq spaceReq) {
		SpaceReq req=new SpaceReq();
		req.setTenantId(spaceReq.getTenantId());
		req.setParentId(-1L);
		req.setName(spaceReq.getName());
		if(spaceReq.getType().equals("BUILD")) {
			req.setType("FLOOR");
		}else if(spaceReq.getType().equals("FLOOR")) {
			req.setType("ROOM");
		}else if(spaceReq.getType().equals("GROUP")) {
			req.setType("BUILD");
		}else if(spaceReq.getType().equals("ROOM")) {
			req.setType("NONE");
		}
		return commonSpaceApi.findSpaceByCondition(req);
	}

	public List<Map<String, Object>> findTree(Long locationId, Long tenantId) {
		return commonSpaceApi.findTree(locationId, tenantId);
	}

	public List<SpaceResp> getChildSpace(Long tenantId,Long parentId, String name) {
		SpaceReq req = new SpaceReq();
		req.setParentId(parentId);
		req.setName(name);
		req.setTenantId(tenantId);
		req.setReorder(true);
		return commonSpaceApi.findSpaceByCondition(req);
	}

	public List<SpaceVo> getChildSpaceStatus(Long tenantId,Long orgId, Long buildId, String type) {
		return spaceApi.getChildSpaceStatus(tenantId,orgId, buildId, type);
	}

	public List<SpaceResp> getSpaceByCondition(SpaceReq spaceReq) {
		return spaceApi.getSpaceByCondition(spaceReq);
	}

	public SpaceResp findById(Long tenantId, Long spaceId) {
		SpaceResp resp=commonSpaceApi.findSpaceInfoBySpaceId(tenantId, spaceId);
		if(resp !=null && StringUtils.isNotBlank(resp.getStyle())) {
			try {
//				if(Constants.judegeIsBase64Encoder(resp.getStyle())) {
					resp.setStyle(Constants.base64Decoder(resp.getStyle()));
//				}
			} catch (Exception e) {
				return resp;
			}
		}
		return resp;
	}

	public CommonResponse<ResultMsg> save(SpaceReq spaceReq) {
		//判断name是否重复
		boolean flag = spaceApi.checkSpaceName(spaceReq);
		if(!flag) {
			//名字有重复
			return new CommonResponse<ResultMsg>(400, "名字重复");
		}
		commonSpaceApi.save(spaceReq);
		return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
	}

	public CommonResponse<ResultMsg> update(SpaceReq spaceReq) {
		boolean flag = spaceApi.checkSpaceName(spaceReq);
		if(!flag) {
			//名字有重复
			return new CommonResponse<ResultMsg>(400, "名字重复");
		}
		commonSpaceApi.update(spaceReq);
		return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
	}

	public void delete(Long tenantId, Long id) {
		commonSpaceApi.deleteSpaceBySpaceId(tenantId, id);
	}
	
	public void deleteBatch(Long tenantId, List<Long> spaceIds) {
		SpaceAndSpaceDeviceVo req=new SpaceAndSpaceDeviceVo();
		req.setSpaceIds(spaceIds);
		req.setTenantId(tenantId);
		commonSpaceApi.deleteSpaceByIds(req);
	}

	public List<SpaceResp> findSpaceByTenantId(Long tenantId,Long orgId) {
		return spaceApi.findSpaceByTenantId(tenantId,orgId);
	}

	public void mount(SpaceDeviceReq spaceDeviceReq) throws Exception {
		spaceApi.mount(spaceDeviceReq);
	}

	public void removeMount(SpaceDeviceReq spaceDeviceReq) throws Exception {
		spaceApi.removeMount(spaceDeviceReq);
	}
	public void removeMount(Long tenantId,Long orgId, String deviceIds) throws Exception {
		spaceApi.deleteMountByDeviceIds(tenantId, orgId,deviceIds);
	}
	public List<Map<String, Object>> getFloorAndDeviceCount(Long tenantId,Long orgId, Long spaceId, String deviceIds) {
		return spaceApi.getFloorAndDeviceCount(tenantId,orgId, spaceId, deviceIds);
	}

	public List<Map<String, Object>> findDeviceByRoom(Long spaceId,Long orgId, Long tenantId) {
		return spaceApi.findDeviceByRoom(spaceId,orgId,tenantId);
	}

	public Boolean groupControl(Long spaceId, String deviceType, Map<String, Object> propertyMap) {
		propertyMap.put("spaceId", spaceId);
		propertyMap.put("deviceType", deviceType);
		return spaceApi.groupControl(propertyMap);
	}

	public Boolean control(String deivceId, Map<String, Object> propertyMap) {
		propertyMap.put("deviceId", deivceId);
		return spaceApi.control(propertyMap);
	}

	public void setSpaceRelation(Long tenantId,Long orgId, Long spaceId, String childIds) throws BusinessException {
		spaceApi.setSpaceRelation(tenantId,orgId, spaceId, childIds);
	}

	public List<Map<String, Object>> getDirectDeviceBySpace(Long tenantId,Long orgId, Long spaceId) {
		return spaceApi.getDirectDeviceBySpace(tenantId,orgId, spaceId);
	}

	public List<String> getMountDeviceBySpaceId(SpaceDeviceReq spaceDeviceReq) {
		return spaceApi.getMountDeviceBySpaceId(spaceDeviceReq);
	}

	public void updateSpaceDevicePosition(Long tenantId,Long spaceId, String deviceJson) {
		Map<String, Object> devices = (Map<String, Object>) JSON.parse(deviceJson);
		List<Map<String, Object>> mapList = (List<Map<String, Object>>) devices.get("devices");
		SpaceDeviceReq spaceDeviceReq = new SpaceDeviceReq();
		spaceDeviceReq.setSpaceId(spaceId);
		spaceDeviceReq.setSpaceInfoMapList(mapList);
		spaceDeviceReq.setTenantId(tenantId);
		spaceApi.updateSpaceDevicePosition(spaceDeviceReq);
	}

	public Map<String, Object> queryDataReport(Long spaceId, String deviceId, String dateType,String deviceType) {
		return centralControlDeviceApi.findDataReport(spaceId, deviceId, dateType,deviceType);
	}

	public List<SpaceResp> findConstructorSpaceByLocationId(Long locationId,Long orgId,Long tenantId) {
		return spaceApi.findSpaceByLocationId(locationId,orgId, tenantId, "");
	}

	public void saveSpaceDevice(List<SpaceDeviceReq> spaceDeviceList) {
		commonSpaceDeviceApi.saveSpaceDeviceList(spaceDeviceList);
	}

	public void addWarning(WarningReq warning) {
		warningApi.addWarning(warning);
	}

	public List<String> findUnreadWarningList(Long tenantId,Long orgId,Long locationId) {
		List<WarningResp> respList = warningApi.findUnreadWarningList(tenantId,orgId,locationId);
		List<String> contentList = new ArrayList<>();
		if (!CollectionUtils.sizeIsEmpty(respList)) {
			respList.forEach(resp -> {
				Map<String, Object> content = JSON.parseObject(resp.getContent(), Map.class);
				content.put("id", resp.getId());
				contentList.add(JSON.toJSONString(content));
			});
		}
		return contentList;
	}

	public List<IndexVo> getIndexInfo(Long tenantId,Long orgId, Long locationId) {
		if (CollectionUtils.sizeIsEmpty(ConstantUtil.indexPage)) {
			List<IndexResp> respList = indexApi.getInexInfoByLocationId(tenantId,orgId, locationId);
			if (CollectionUtils.isNotEmpty(respList)) {
				respList.forEach(indexResp -> {
					IndexVo vo = new IndexVo();
					BeanUtil.copyProperties(indexResp, vo);
					if (indexResp.getBuildId() != null) {
						SpaceResp resp = commonSpaceApi.findSpaceInfoBySpaceId(indexResp.getTenantId(), indexResp.getBuildId());
						vo.setName(resp != null ? resp.getName() : "");
					}
					ConstantUtil.indexPage.add(vo);
				});
			}
		}
		return ConstantUtil.indexPage;
	}

	public void updateWarningStatus(WarningReq warning) {
		warningApi.updateWarningStatus(warning);
	}

	public void syncSpaceStatus(String deviceId) {
		spaceApi.syncSpaceStatus(deviceId);
	}

	public ReservationResp getReservation(Long spaceId, Long currentDate) {
		ReservationReq req = new ReservationReq();
		req.setSpaceId(spaceId);
		req.setCurrentDate(currentDate);
		return reservationApi.currentReservationStatus(req);
	}

	public ReservationResp getStartReservation(Long spaceId, Long currentDate) {
		ReservationReq req = new ReservationReq();
		req.setCurrentDate(currentDate);
		req.setSpaceId(spaceId);
		return reservationApi.getStartReservation(req);
	}

	public Page<WarningResp> findHistoryWarningList(String pageNum, String pageSize,String eventType,String timeType,
			Long tenanId,Long orgId,Long locationId) throws BusinessException {
		return warningApi.findHistoryWarningList(pageNum, pageSize,eventType,timeType,
				tenanId,orgId,locationId);
	}

	public SpaceEhiVO getEnvironmentalHealthIndex(Long tenantId,Long orgId, Long spaceId) {
		SpaceResp space = commonSpaceApi.findSpaceInfoBySpaceId(tenantId, spaceId);
		SpaceEhiVO result = new SpaceEhiVO();
		String types = HealthSensorPropertyUtils.getProperty(HealthSensorPropertyUtils.PROPERTY_HEALTH_SENSOR_TYPES);
		if (StringUtils.isBlank(types)) {
			return result;
		}
		// 传感器类型集合
		List<String> deviceCategoryTypes = Lists.newArrayList(types.split(","));
		if (CollectionUtils.isEmpty(deviceCategoryTypes)) {
			return result;
		}
		// 传感器属性名字
		String propertyNamesStr = HealthSensorPropertyUtils
				.getProperty(HealthSensorPropertyUtils.PROPERTY_HEALTH_SENSOR_PROPERTYNAMES);
		List<String> propertyNames = Lists.newArrayList();
		if (StringUtils.isNotBlank(propertyNamesStr)) {
			propertyNames = Lists.newArrayList(propertyNamesStr.split(","));
		}

		Map<String, List<String>> sensorProperties = Maps.newHashMap();
		for (int i = 0; i < deviceCategoryTypes.size(); i++) {
			List<String> properties = sensorProperties.get(deviceCategoryTypes.get(i));
			if (CollectionUtils.isEmpty(properties)) {
				properties = Lists.newArrayList();
			}
			if (propertyNames.get(i) != null) {
				properties.add(propertyNames.get(i));
			}
			sensorProperties.put(deviceCategoryTypes.get(i), properties);
		}

		QueryParamReq req = new QueryParamReq();
		req.setDeviceTypeIds(deviceCategoryTypes);
		req.setSpaceId(spaceId);
 		List<Map<String, Object>> devices = spaceApi.findDeviceByRoomAndDeviceType(req);
		if (CollectionUtils.isEmpty(devices)) {
			result.getProperties().put("name", space == null ? "" : space.getName());
			return result;
		}

		for (Map<String, Object> device : devices) {
			String type = (String) device.get("devType");
			if (StringUtils.isBlank(type)) {
				continue;
			}
			String deviceId = String.valueOf(device.get("deviceId"));
			// TODO 无法获取
			// 获取设备的属性
			Map<String, Object> map = deviceStateServiceApi.get(tenantId,deviceId);
			if (MapUtils.isEmpty(map)) {
				continue;
			}
			result.getProperties().put("ehi", map.get("eci"));
			result.getProperties().put("Temp", map.get("Temp"));
			result.getProperties().put("Hum", map.get("Hum"));
			// result.getProperties().put("Brightness",map.get("Brightness"));Illuminance
			result.getProperties().put("Illuminance", map.get("Illuminance"));
			result.getProperties().put("pm", map.get("pm"));
			result.getProperties().put("noise", map.get("noise"));
			result.getProperties().put("co", map.get("co"));
			result.getProperties().put("formaldehyde", map.get("formaldehyde"));

			List<String> props = sensorProperties.get(type);
			if (CollectionUtils.isEmpty(props)) {
				continue;
			}
		}

		String spaceName = space == null ? "" : space.getName();
		result.getProperties().put("spaceName", spaceName);
		// 将虚拟数据保存至数据库
		saveVirtualHealthValues(tenantId,orgId, spaceId, result);
		return result;
	}

	/**
	 * 保存虚拟健康数据进数据库
	 * 
	 * @param spaceId
	 *            空间ID
	 */
	public void saveVirtualHealthValues(Long tenantId, Long orgId,Long spaceId, SpaceEhiVO spaceEhiVO) {
		try {
			// 处理虚拟数据
			String propertyNamesStr = HealthSensorPropertyUtils
					.getProperty(HealthSensorPropertyUtils.PROPERTY_HEALTH_SENSOR_PROPERTYNAMES);
			List<String> propertyNames = Lists.newArrayList();
			if (StringUtils.isNotBlank(propertyNamesStr)) {
				propertyNames = Lists.newArrayList(propertyNamesStr.split(","));
			}
			Map<String, Object> properties = spaceEhiVO.getProperties();

			List<AddCommDeviceStateInfoReq> infoReqList=Lists.newArrayList();
			for (Map.Entry<String, Object> property : properties.entrySet()) {
				AddCommDeviceStateInfoReq req=new AddCommDeviceStateInfoReq();
				String propertyName = property.getKey(); // 属性名称
				Object propertyValue = property.getValue();// 属性值
				if (propertyNames.contains(propertyName)) {
					req.setPropertyName(propertyName);
					req.setPropertyValue(propertyValue.toString());
					infoReqList.add(req);
				}
			}
			List<Map<String, Object>> devices = spaceApi.findDeviceByRoom(tenantId,orgId,spaceId);
			if (devices != null && devices.size() > 0) {
				List<UpdateDeviceStateReq> params=Lists.newArrayList();
				for (Map<String, Object> device : devices) {
					Date date = new Date();
					String deviceId = String.valueOf(device.get("id"));
					// 拼装参数对象
					UpdateDeviceStateReq req=new UpdateDeviceStateReq();
					
					req.setDeviceId(deviceId);
					infoReqList.forEach(info -> {
						info.setGroupId(spaceId);
					});
					req.setStateList(infoReqList);
					req.setDeviceId(deviceId);
					req.setTenantId(tenantId);
					params.add(req);
				}
				deviceStateServiceApi.saveOrUpdateBatch(params);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public List<LocationResp> getLocationByTenantId(Long tenantId) throws BusinessException {
		LocationReq locationReq = new LocationReq();
		locationReq.setTenantId(tenantId);
		return spaceApi.findLocationByCondition(locationReq);
	}

	public CommonResponse spaceDataImport(MultipartHttpServletRequest multipartRequest, Long tenantId, Long locationId,
										  Long userId,Long orgId) {
		if ((multipartRequest != null && !multipartRequest.getFileNames().hasNext())) {
			throw new BusinessException(BusinessExceptionEnum.UPLOAD_FILE_EMPTY);
		}
		String fileId = null;
		try {
			MultipartFile multipartFile = multipartRequest.getFile(multipartRequest.getFileNames().next());
			Map<String, Object> result = ExcelUtils.readExcelNew(multipartFile);
			//fileId = this.fileApi.upLoadFile(multipartFile, tenantId);
			//String fileUrl  = this.fileApi.getGetUrl(fileId).getPresignedUrl();
			SpaceExcelReq spaceExcelReq = new SpaceExcelReq(locationId,userId,tenantId,orgId,result);
			return spaceApi.spaceDataImport(spaceExcelReq);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			/*
			 * if(StringUtils.isNotBlank(fileId)) this.fileApi.deleteObject(fileId);
			 */
		}
		return CommonResponse.success();
	}

	public String saveSpaceBackgroundImgImport(Long tenantId, Long locationId, Long userId, SpaceBackgroundImgReq req) {
		String result = null;
		try {
			req.setCreateBy(userId);
			req.setCreateTime(new Date());
			req.setLocationId(locationId);
			req.setTenantId(tenantId);
			spaceApi.saveSpaceBackgroundImgImport(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String fileUpload(MultipartHttpServletRequest multipartRequest, Long tenantId, Long spaceId) {
		if ((multipartRequest != null && !multipartRequest.getFileNames().hasNext())) {
			throw new BusinessException(BusinessExceptionEnum.UPLOAD_FILE_EMPTY);
		}
		String fileId = null;
		try {
			MultipartFile multipartFile = multipartRequest.getFile(multipartRequest.getFileNames().next());
			// fileId = this.fileApi.upLoadFile(multipartFile, tenantId);
			String path = environmentPropertyApi.getPropertyValue(LOCAL_FILE_UPLOAD_PATH);
			fileId = this.uploadFile(multipartFile, tenantId, path, spaceId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileId;
	}

	public String uploadFile(MultipartFile multipartFile, Long tenantId, String path, Long spaceId) {
		if (CommonUtil.isEmpty(multipartFile)) {
			throw new BusinessException(BusinessExceptionEnum.VIDEO_UPLOADFILE_ERROR);
		}
		String fileName = multipartFile.getOriginalFilename();
		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
		try {
			File parentContextFile = new File(path);
			if (!parentContextFile.exists()) {
				parentContextFile.mkdirs();
			}
			// String path = saveFile(multipartFile.getInputStream(), parentContextPath,
			// fileName);
			if (spaceId != 0L) {
				path = path + spaceId + "." + fileType;
				fileName = spaceId + "." + fileType;
			}else {
				path = path + fileName;
			}
			// 判断文件是否上传过 , 如果存在就删除旧文件
			File file = new File(path);
			if (file.exists()) {
				file.delete();
				file.createNewFile();
			}
			multipartFile.transferTo(new File(path));
			// String storageKey = tenantId+"/"+fileType+"/"+fileName;
			// 返回key格式：1000/ts/a1b9a1479d724fb6a275baa994fdacb8.ts -> 租户id/文件类型/fileId.文件后缀
			// 文件信息保存至数据库（推送至队列，由定时任务去存储）
			// FileBean fileBean = FileUtil.createFileBean(storageKey,fileType);
			// FileInfoProducer.addToQueue(fileBean);
//			String relativePath = environmentPropertyApi.getPropertyValue(LOCAL_FILE_RELATIVE_PATH) + File.separator + spaceId + "." + fileType;
			return fileName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getFileUrl(String fileId) {
		String fileUrl = null;
		try {
			fileUrl = this.fileApi.getGetUrl(fileId).getPresignedUrl();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(BusinessExceptionEnum.VIDEO_UPLOADFILE_ERROR);
		}
		return fileUrl;
	}

	public List<SpaceBackgroundImgResp> getSpaceBackgroundImg(Long spaceId) {
		String fileUrl = null;
		try {
			SpaceBackgroundImgReq req = new SpaceBackgroundImgReq();
			req.setSpaceId(spaceId);
			String path = environmentPropertyApi.getPropertyValue(LOCAL_FILE_RELATIVE_PATH) + File.separator;
			List<SpaceBackgroundImgResp> imgList = spaceApi.getSpaceBackgroundImg(req);
			if (!CollectionUtils.isEmpty(imgList)) {
				for (SpaceBackgroundImgResp resp : imgList) {
					resp.setBgImgUrl(path + resp.getBgImg());
//					resp.setThumbnailImgUrl(this.getFileUrl(resp.getThumbnailImg()));
//					resp.setViewImgUrl(this.getFileUrl(resp.getViewImg()));
				}
			}
			return imgList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updateSpaceBackgroundImg(Long tenantId, Long locationId, Long userId, SpaceBackgroundImgReq req) {
		String fileUrl = null;
		try {
			req.setUpdateBy(userId);
			req.setUpdateTime(new Date());
			req.setTenantId(tenantId);
			req.setLocationId(locationId);
			spaceApi.updateSpaceBackgroundImg(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteSpaceBackgroundImg(Long tenantId,Long orgId, Long id) {
		String fileUrl = null;
		try {
			SpaceBackgroundImgReq req = new SpaceBackgroundImgReq();
			req.setId(id);
			SpaceBackgroundImgResp resp = spaceApi.getSpaceBackgroundImgById(tenantId,orgId, id);
			String bgImgFileId = resp.getBgImg();
			this.fileApi.deleteObject(bgImgFileId);
			spaceApi.deleteSpaceBackgroundImg(tenantId,orgId,id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String appSaveBgImg(MultipartHttpServletRequest multipartRequest, Long tenantId,
			SpaceBackgroundImgReq spaceBackgroundImgReq, Long spaceId) {
		// 先进行本地文件上传
		String fileName = fileUpload(multipartRequest, tenantId, spaceId);
		String relativePath = environmentPropertyApi.getPropertyValue(LOCAL_FILE_RELATIVE_PATH) + File.separator
				+ fileName;
		// 保存房间背景图片
		// SpaceBackgroundImgReq spaceBackgroundImgReq = new SpaceBackgroundImgReq();
		spaceBackgroundImgReq.setBgImg(fileName);
		// List<SpaceBackgroundImgResp> spaceBackgroundImgResps = spaceApi
		// .getSpaceBackgroundImg(spaceBackgroundImgReq);
		if (spaceBackgroundImgReq.getId() != null) {
			spaceBackgroundImgReq.setSpaceId(spaceBackgroundImgReq.getSpaceId());
			spaceBackgroundImgReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
			spaceBackgroundImgReq.setUpdateBy(SaaSContextHolder.getCurrentUserId());
			spaceBackgroundImgReq.setCreateTime(new Date());
			spaceBackgroundImgReq.setUpdateTime(new Date());
			spaceApi.saveSpaceBackgroundImgImport(spaceBackgroundImgReq);
		} else {
			spaceBackgroundImgReq.setUpdateBy(SaaSContextHolder.getCurrentUserId());
			spaceBackgroundImgReq.setUpdateTime(new Date());
			spaceApi.updateSpaceBackgroundImg(spaceBackgroundImgReq);
		}
		return relativePath;
	}

	public List<DeploymentResp> getDeplymentList(Long tenantId,Long locationId) {
		DeploymentReq req=new DeploymentReq();
		req.setTenantId(tenantId);
		req.setLocationId(locationId);
		List<DeploymentResp> list=spaceApi.getDeploymentList(req);
		commonList(list);
		return list;
	}

	private void commonList(List<DeploymentResp> list) {
		if(CollectionUtils.isNotEmpty(list)) {
			List<Long> userIdList=Lists.newArrayList();
			list.forEach(resp->{
				userIdList.add(resp.getCreateBy());
				userIdList.add(resp.getUpdateBy());
			});
			//组装用户信息
			Map<Long,String> userInfo=Maps.newHashMap();
			if(CollectionUtils.isNotEmpty(userIdList)) {
				 Map<Long, FetchUserResp> users =userApi.getByUserIds(userIdList);
				 if(MapUtils.isNotEmpty(users)) {
					 for(Long userId:users.keySet()) {
						 userInfo.put(userId, users.get(userId).getUserName());
					 }
				 }
			}
			list.forEach(resp->{
				resp.setCreateName(userInfo.get(resp.getCreateBy()));
				resp.setUpdateName(userInfo.get(resp.getUpdateBy()));
			});
		}
	}
	
	public Page<DeploymentResp> getDeployPage(Long tenantId,Long locationId,Integer pageNumber,Integer pageSize,String deployName) {
		DeploymentReq req=new DeploymentReq();
		req.setTenantId(tenantId);
		req.setLocationId(locationId);
		req.setDeployName(deployName);
		req.setPageNumber(pageNumber==null?1:pageNumber);
		req.setPageSize(pageSize==null?15:pageSize);
		Page<DeploymentResp> page=spaceApi.getDeploymentPage(req);
		commonList(page.getResult());
		return page;
	}
	
	public CommonResponse saveOrUpdateDeploy(Long tenantId,Long orgId,Long locationId,Long id,String name,Long userId) {
		DeploymentReq req=new DeploymentReq();
		if(id==null) {
			req.setCreateBy(userId);
			req.setCreateTime(new Date());
		}
		req.setId(id);
		req.setDeployName(name);
		req.setTenantId(tenantId);
		req.setLocationId(locationId);
		DeploymentResp resp = spaceApi.findByName(orgId, name);
        if(resp !=null){
        	if(id ==null || resp.getId().compareTo(id) !=0) {
        		//名字有重复
                return new CommonResponse<ResultMsg>(400,"名字重复");
        	}
        }
		req.setUpdateBy(userId);
		req.setUpdateTime(new Date());
		spaceApi.saveOrUpdateDeploy(req);
		return CommonResponse.success();
	}
	
	public void deleteBatchDeploy(Long tenantId,String ids) {
		spaceApi.deleteBatchDeploy(ids);
	}

	/**
	 * 保存/修改index_Content
	 * @param indexReq
	 * @return
	 */
    public Long saveOrUpdateIndexContent(IndexReq indexReq) {
		return indexApi.saveIndexContent(indexReq);
    }

	/**
	 * 保存/修改index_detail
	 * @param indexDetailReq
	 */
	public void saveOrUpdateIndexDetail(IndexDetailReq indexDetailReq) {
		indexApi.saveIndexDetail(indexDetailReq);
	}

	/**
	 * 查询自定义首页列表
	 * @param indexReq
	 * @return
	 */
	public Page<IndexResp> findCustomIndex(IndexReq indexReq) {
		Page<IndexResp> pageResult = new Page<>();
		Pagination pagination = new Pagination(indexReq.getPageNumber(),indexReq.getPageSize());
		indexReq.setPagination(pagination);
		Page<IndexResp> list = indexApi.findCustomIndex(indexReq);
		/*for(IndexResp indexResp : list){
			indexResp.setUserName(userApi.getUser(indexResp.getCreateBy()).getUserName());
		}
		pageResult.setPageNum(indexReq.getPageNumber());
		pageResult.setPageSize(indexReq.getPageSize());
		pageResult.setTotal(indexReq.getPagination().getTotal());
		pageResult.setPages(indexReq.getPagination().getPages());
		pageResult.setResult(list);*/
		return list;
	}

	/**
	 * 查询index_detail通过IndexId
	 * @param indexContentIdStr
	 * @return
	 */
	public List<IndexDetailResp> findIndexDetailByIndexId(Long tenantId,Long orgId, Long indexContentIdStr) {
		List<IndexDetailResp> list = indexApi.findIndexDetailByIndexId(tenantId,orgId, indexContentIdStr);
		return list;
	}

	/**
	 * 根据indexContentId删除index_detail
	 * @param indexContentId
	 */
	public void deleteIndexDatailByIndexId(Long tenantId,Long orgId, Long indexContentId) {
		indexApi.deleteIndexDatailByIndexId(tenantId, orgId,indexContentId);
	}

	/**
	 * 删除index_content
	 * @param indexReq
	 */
	public void deleteIndexContent(IndexReq indexReq) {
		indexApi.deleteIndexContent(indexReq);
	}
	public List<SpaceResp> getBuildListByTenantId(Long tenantId,Long orgId) {
		SpaceReq req = new SpaceReq();
		req.setTenantId(tenantId);
		req.setType(SpaceEnum.BUILD.getCode());
		req.setOrgId(orgId);
		return spaceApi.getSpaceByCondition(req);
	}

	public List<SpaceResp> getFloorListByBuildId(Long tenantId,Long buildId) {
		SpaceReq req = new SpaceReq();
		req.setType(SpaceEnum.FLOOR.getCode());
		req.setParentId(buildId);
		req.setTenantId(tenantId);
		return spaceApi.getSpaceByCondition(req);
	}

	/**
	 * 查询index_content 通过id
	 * @param indexContentIdStr
	 * @return
	 */
    public IndexResp findIndexContentById(Long tenantId,Long orgId, Long indexContentIdStr) {
		IndexResp indexResp = indexApi.findIndexContentById(tenantId, orgId,indexContentIdStr);
		return  indexResp;
    }

	/**
	 * 把所有的index_content数据的enable 置为0
	 * @param enable
	 */
	public void setAllEnableOff(LoginResp user, int enable) {
		indexApi.setAllEnableOff(user.getTenantId(),user.getOrgId(),user.getLocationId(), enable);
	}

	/**
	 * 登录获取已经启动的自定义首页
	 * @return
	 */
	public IndexResp getEnableIndex(LoginResp user) {
		IndexResp indexResp = indexApi.getEnableIndex(user.getTenantId(),user.getOrgId(), user.getLocationId());
		return indexResp;
	}

	/**
	 * add calendar
	 * @param calendarReq
	 */
	public CommonResponse<ResultMsg> addOrUpdateCalendar(CalendarReq calendarReq) {
		//判断name是否重复
		List<CalendarResp> list =  spaceApi.findCalendarListNoPage(calendarReq.getName());
		if(calendarReq.getId() == null){//修改的时候不需要校验保存的名字是否重复
			if(list.size() !=0){
				//名字有重复
				return new CommonResponse<ResultMsg>(400,"名字重复");
			}
		}
		spaceApi.addOrUpdateCalendar(calendarReq);
		return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
	}

	/**
	 * update calendar
	 * @param tenantId
	 * @param id
	 */
	public void deleteCalendar(Long tenantId,Long orgId, Long id) {
		spaceApi.deleteCalendar(tenantId,orgId,id);
	}

	/**
	 * get calendar info
	 * @param tenantId
	 * @param id
	 * @return
	 */
	public CalendarResp getCalendarIofoById(Long tenantId,Long orgId, Long id) {
		CalendarResp calendarResp = spaceApi.getCalendarIofoById(tenantId,orgId,id);
		return calendarResp;
	}

	/**
	 * find calendar list
	 * @param pageNum
	 * @param pageSize
	 * @param name
	 * @return
	 */
	public Page<CalendarResp> findCalendarList(String pageNum, String pageSize, String name) {
		return spaceApi.findCalendarList(pageNum, pageSize,name);
	}

	public List<WarningResp> findHistoryWarningListNoPage(String eventType,String count,Long tenantId,
			Long orgId,Long locationId) {
		return warningApi.findHistoryWarningListNoPage(eventType,count,tenantId,orgId,locationId);
	}
	
	public List<Map<String, Object>> getDeviceListBySpace(SpaceDeviceReq spaceDeviceReq) {
		return spaceApi.getDeviceListBySpace(spaceDeviceReq);
	}
	
	/**
	 * 设备替换
	 * @param spaceDeviceReq
	 */
	public void replaceDevice(SpaceDeviceReq spaceDeviceReq) {
		spaceApi.replaceDevice(spaceDeviceReq);
	}
}
