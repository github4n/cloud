package com.iot.building.template.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.building.device.api.DeviceBusinessTypeApi;
import com.iot.building.device.vo.DeviceBusinessTypeResp;
import com.iot.building.excepiton.BusinessExceptionEnum;
import com.iot.building.helper.SceneTemplateHelper;
import com.iot.building.ifttt.constant.IftttConstants;
import com.iot.building.ifttt.exception.IftttExceptionEnum;
import com.iot.building.ifttt.service.IftttService;
import com.iot.building.ifttt.vo.ActuatorVo;
import com.iot.building.ifttt.vo.RelationVo;
import com.iot.building.ifttt.vo.SensorVo;
import com.iot.building.ifttt.vo.res.RuleResp;
import com.iot.building.scene.service.SceneDetailService;
import com.iot.building.scene.service.SceneService;
import com.iot.building.space.api.SpaceApi;
import com.iot.building.space.mapper.DeploymentMapper;
import com.iot.building.space.vo.DeploymentResp;
import com.iot.building.template.constant.TemplateType;
import com.iot.building.template.domain.Template;
import com.iot.building.template.domain.TemplateDetail;
import com.iot.building.template.domain.TemplateIfttt;
import com.iot.building.template.exception.TemplateExceptionEnum;
import com.iot.building.template.mapper.TemplateIftttMapper;
import com.iot.building.template.mapper.TemplateMapper;
import com.iot.building.template.service.TemplateDetailService;
import com.iot.building.template.service.TemplateIftttService;
import com.iot.building.template.service.TemplateService;
import com.iot.building.template.vo.req.BuildTemplateReq;
import com.iot.building.template.vo.req.CreateSceneFromTemplateReq;
import com.iot.building.template.vo.req.SpaceTemplateReq;
import com.iot.building.template.vo.req.TemplateDetailReq;
import com.iot.building.template.vo.req.TemplateReq;
import com.iot.building.template.vo.rsp.DeviceTarValueResp;
import com.iot.building.template.vo.rsp.SceneTemplateResp;
import com.iot.building.template.vo.rsp.TemplateDetailResp;
import com.iot.building.template.vo.rsp.TemplateResp;
import com.iot.building.template.vo.rsp.TemplateVoResp;
import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.common.util.JsonUtil;
import com.iot.common.util.StringUtil;
import com.iot.control.scene.api.SceneApi;
import com.iot.control.scene.vo.req.SceneDetailReq;
import com.iot.control.scene.vo.req.SceneReq;
import com.iot.control.scene.vo.rsp.SceneResp;
import com.iot.control.space.vo.SpaceDeviceVo;
import com.iot.control.space.vo.SpaceResp;
import com.iot.device.api.CentralControlDeviceApi;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.api.DeviceTypeApi;
import com.iot.device.api.DeviceTypeCoreApi;
import com.iot.device.api.ProductCoreApi;
import com.iot.device.exception.ProductExceptionEnum;
import com.iot.device.vo.req.device.ListDeviceInfoReq;
import com.iot.device.vo.rsp.DeviceTypeResp;
import com.iot.device.vo.rsp.device.GetDeviceTypeInfoRespVo;
import com.iot.device.vo.rsp.device.GetProductInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import com.iot.tenant.exception.TenantExceptionEnum;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import com.iot.util.AssertUtils;

/**
 * 项目名称: IOT云平台 模块名称： 功能描述： 创建人: yuChangXing 创建时间: 2018/5/3 15:57 修改人: 修改时间：
 */

@Service
public class TemplateServiceImpl implements TemplateService {
	private static final Logger log = LoggerFactory.getLogger(TemplateServiceImpl.class);

	@Autowired
	private UserApi userApi;
	@Autowired
	private SpaceApi spaceApi;
	@Autowired
	private ProductCoreApi productApi;
	@Autowired
	private SceneApi sceneApi;
	@Autowired
	private IftttService iftttService;
	@Autowired
	private DeviceTypeCoreApi deviceTypeApi;
	@Autowired
	private TemplateMapper templateMapper;
	@Autowired
	private TemplateIftttMapper templateIftttMapper;
	@Autowired
	private SceneTemplateHelper sceneTemplateHelper;
	@Autowired
	private TemplateIftttService templateIftttService;
	@Autowired
	private DeviceBusinessTypeApi deviceBusinessTypeApi;
	@Autowired
	private TemplateDetailService templateDetailService;
	@Autowired
    private DeviceCoreApi deviceCoreApi;
    @Autowired
    private DeploymentMapper deploymentMapper;

	private int getSceneSort(Long spaceId) {
		int sort = 1;
		SceneResp existScene = sceneApi.maxSortSceneBySpaceId(spaceId);
		if (existScene != null && existScene.getSort() != null) {
			sort = existScene.getSort() + 1;
		}
		return sort;
	}

	@Override
	public void buildSceneTemplate2B(BuildTemplateReq buildTemplateReq) throws Exception{
		AssertUtils.notNull(buildTemplateReq, BusinessExceptionEnum.PARAMETER_ILLEGALITY.getMessageKey());

		AssertUtils.notNull(buildTemplateReq.getTenantId(), TenantExceptionEnum.TENANT_NOT_EXIST.getMessageKey());

		TemplateReq templateReq = buildTemplateReq.getTemplateReq();
		AssertUtils.notNull(templateReq, BusinessExceptionEnum.PARAMETER_ILLEGALITY.getMessageKey());
		AssertUtils.notNull(templateReq.getTemplateType(), TemplateExceptionEnum.TEMPLATE_TYPE_IS_NULL.getMessageKey());

		Long tenantId = buildTemplateReq.getTenantId();
		String templateName = templateReq.getName();

		AssertUtils.notNull(templateName, TemplateExceptionEnum.TEMPLATE_NAME_IS_NULL.getMessageKey());

		// copy 数据
		Template template = new Template();
		BeanUtil.copyProperties(templateReq, template);

		List<TemplateDetailReq> templateDetailReqList = buildTemplateReq.getTemplateDetailList();
		Date currentTime = new Date();
		templateReq.setTenantId(tenantId);
		// 查询模板名是否已存在
		TemplateResp resp=templateMapper.findSameNameOrDeployTemplate(templateReq);
		if (resp !=null) {
			if(resp.getId().compareTo(templateReq.getId()) !=0) {
				// 模板名不能重复
				throw new BusinessException(TemplateExceptionEnum.TEMPLATE_NAME_IS_REPEAT);
			}
		}

		if (templateReq.getId() != null) {// 编辑
			// 更新主表
			template.setUpdateBy(buildTemplateReq.getUpdateBy());
			template.setUpdateTime(currentTime);
			templateMapper.updateTemplate(template);

		} else {// 新增
			// 插入主表
			template.setTemplateType(TemplateType.scene.getValue());
			template.setTenantId(tenantId);
			template.setCreateTime(currentTime);
			template.setUpdateTime(currentTime);
			template.setCreateBy(buildTemplateReq.getCreateBy());
			template.setUpdateBy(buildTemplateReq.getUpdateBy());
			templateMapper.insert(template);
		}

		// ****** 情景模板
		// 删除旧的情景模板
		templateDetailService.delTemplateDetail(template.getId());

		// 保存新的情景模板
		if (CollectionUtils.isNotEmpty(templateDetailReqList)) {
			for (TemplateDetailReq templateDetailReq : templateDetailReqList) {
				TemplateDetail templateDetail = new TemplateDetail();
				BeanUtil.copyProperties(templateDetailReq, templateDetail);
				templateDetail.setTemplateId(template.getId());
				templateDetail.setTenantId(tenantId);
				templateDetail.setCreateBy(buildTemplateReq.getCreateBy());
				templateDetail.setUpdateBy(buildTemplateReq.getCreateBy());
				templateDetail.setUpdateTime(currentTime);
				templateDetail.setCreateTime(currentTime);
				templateDetailService.insert(templateDetail);
			}
		}
	}

	/**
	 * 根据模板生成房间的详细情景
	 * @param template
	 */
	@Override
	public void saveSpaceDetailSceneByTemplate(TemplateReq template) {
		// 生成对应的 部署方式 的房间情景 
		List<SpaceResp> spaceResps = spaceApi.findSpaceListByDeployId(template.getDeployId(), template.getTenantId(),template.getOrgId(), template.getLocationId());
		// 删除对应的情景和微调
		sceneTemplateHelper.deleteSceneToRoomByTemplate(template.getId(), template.getUpdateBy());
		for (SpaceResp spaceResp : spaceResps) {
			// 添加对应的情景和微调
			sceneTemplateHelper.addSceneToRoomByTemplate(spaceResp.getId(), template.getId(), template.getUpdateBy());
		}
	}


	@Override
	public Page<TemplateResp> findTemplateList(String name, String templateType, int pageNum, int pageSize,
											   Long tenantId, Long locationId) {
		// 去掉空字符
		if (org.apache.commons.lang.StringUtils.isBlank(name)) {
			name = null;
		}

		com.github.pagehelper.Page<TemplateResp> pageTemp = PageHelper.startPage(pageNum,
				pageSize);
		List<TemplateResp> templateRespList = getTemplateList(name, templateType, tenantId, locationId);
		List<Long> userIds = getUserIds(templateRespList);
		//获取用户信息
		setUserInfo(templateRespList, userIds);
		Page<TemplateResp> page = setPageInfo(pageNum, pageSize, pageTemp, templateRespList);
		return page;
	}

	private Page<TemplateResp> setPageInfo(int pageNum, int pageSize, com.github.pagehelper.Page<TemplateResp> pageTemp,
			List<TemplateResp> templateRespList) {
		Page<TemplateResp> page = new Page<TemplateResp>();
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setResult(templateRespList);
		page.setTotal(pageTemp.getTotal());
		return page;
	}

	private List<TemplateResp> getTemplateList(String name, String templateType, Long tenantId, Long locationId) {
		TemplateReq template=new TemplateReq();
		template.setName(name);template.setTemplateType(templateType);template.setTenantId(tenantId);
		template.setLocationId(locationId);
		List<TemplateResp> templateRespList = templateMapper.findTemplateList(template);
		return templateRespList;
	}

	private void setUserInfo(List<TemplateResp> templateRespList, List<Long> userIds) {
		if(CollectionUtils.isNotEmpty(userIds)) {
			Map<Long,FetchUserResp> userMap=userApi.getByUserIds(userIds);
			if(MapUtils.isNotEmpty(userMap)) {
				templateRespList.forEach(resp->{
					resp.setCreateName(userMap.get(resp.getCreateBy())==null?null:userMap.get(resp.getCreateBy()).getUserName());
				});
			}
		}
	}

	private List<Long> getUserIds(List<TemplateResp> templateRespList) {
		List<Long> userIds=Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(templateRespList)) {
			for(TemplateResp resp:templateRespList) {
				if(resp.getDeployId() !=null) {
					DeploymentResp deployResp=deploymentMapper.findById(resp.getDeployId());
					resp.setDeployName(deployResp==null?null:deployResp.getDeployName());
				}
				userIds.add(resp.getCreateBy());
			}
		}
		return userIds;
	}


	@Override
	public Long createSceneFromTemplate(CreateSceneFromTemplateReq createSceneFromTemplateReq) throws BusinessException {
		AssertUtils.notNull(createSceneFromTemplateReq, "parameter.is.null");
		AssertUtils.notNull(createSceneFromTemplateReq.getTemplateId(), "templateId.is.null");

		Template template = templateMapper.getTemplateById(createSceneFromTemplateReq.getTemplateId());
		if (template == null) {
			throw new BusinessException(TemplateExceptionEnum.TEMPLATE_IS_NOT_EXIST);
		}

		List<TemplateDetail> templateDetailList = templateDetailService.findByTemplateId(template.getId(),
				createSceneFromTemplateReq.getTenantId());
		if (CollectionUtils.isEmpty(templateDetailList)) {
			throw new BusinessException(TemplateExceptionEnum.TEMPLATE_SCENE_CONTENT_IS_EMPTY);
		}

		// 构建参数 获取 List<DeviceResp>
		if(CollectionUtils.isEmpty(createSceneFromTemplateReq.getDeviceIdList())) {
			return null;
		}
		ListDeviceInfoReq params = new ListDeviceInfoReq();
		params.setDeviceIds(createSceneFromTemplateReq.getDeviceIdList());
		List<ListDeviceInfoRespVo> deviceList = deviceCoreApi.listDevices(params);
		if (deviceList == null) {
			return null;
		}
//		Map<String, List<ListDeviceInfoRespVo>> deviceProductIdMap = Maps.newHashMap();
//		for (ListDeviceInfoRespVo deviceResp : list) {
//			List<ListDeviceInfoRespVo> deviceResps = deviceProductIdMap.get(deviceResp.getProductId() + "");
//			if (CollectionUtils.isEmpty(deviceResps)) {
//				deviceResps = new ArrayList<>();
//			}
//			deviceResps.add(deviceResp);
//			deviceProductIdMap.put(deviceResp.getProductId() + "", deviceResps);
//		}

		Date currentTime = new Date();
//		int sort = getSceneSort(createSceneFromTemplateReq.getSpaceId());
		SceneReq sceneAddReq = new SceneReq();
		sceneAddReq.setSceneName(template.getName()+createSceneFromTemplateReq.getSpaceId());
		sceneAddReq.setTemplateId(template.getId());
		sceneAddReq.setSpaceId(createSceneFromTemplateReq.getSpaceId());
//		sceneAddReq.setSort(sort);
		sceneAddReq.setSetType(2);
		sceneAddReq.setLocationId(createSceneFromTemplateReq.getLocationId());
		sceneAddReq.setTenantId(createSceneFromTemplateReq.getTenantId());
		sceneAddReq.setUploadStatus(0);//未下发的情景
		sceneAddReq.setSilenceStatus(createSceneFromTemplateReq.getSilenceStatus());
		SceneResp scene = sceneApi.insertScene(sceneAddReq);
		log.info("xxxxxx设templateDetail:"+templateDetailList.size());
		for (TemplateDetail templateDetail : templateDetailList) {
			if (CollectionUtils.isNotEmpty(deviceList)) {
				log.info("xxxxxx设备列表共有:"+deviceList.size());
				for (ListDeviceInfoRespVo deviceResp : deviceList) {
					log.info("xxxxxxtemplateDetail.getBusinessTypeId():"+templateDetail.getBusinessTypeId());
					log.info("xxxxxxdeviceResp.getBusinessTypeId()):"+deviceResp.getBusinessTypeId());
					log.info("xxxxxx----deviceId):"+deviceResp.getId());
					if(templateDetail.getBusinessTypeId().equals(deviceResp.getBusinessTypeId())){
						log.info("xxxxxx-------------------模板生成情景 start---------------------------");
						SceneDetailReq sceneDetail = new SceneDetailReq();
						sceneDetail.setSceneId(scene.getId());
						sceneDetail.setTenantId(createSceneFromTemplateReq.getTenantId());
						sceneDetail.setCreateTime(currentTime);
						sceneDetail.setUpdateTime(currentTime);
						sceneDetail.setCreateBy(createSceneFromTemplateReq.getUserId());
						sceneDetail.setUpdateBy(createSceneFromTemplateReq.getUserId());
						sceneDetail.setDeviceId(deviceResp.getUuid());
						Map<String, Object> targetValueMap = (Map<String, Object>) JSON
								.parseObject(templateDetail.getTargetValue(), Map.class);
						targetValueMap.put("deviceId", deviceResp.getUuid());
						sceneDetail.setTargetValue(JSON.toJSONString(targetValueMap));
						sceneDetail.setSort(0);
						sceneDetail.setSpaceId(createSceneFromTemplateReq.getSpaceId());
						sceneDetail.setDeviceTypeId(templateDetail.getDeviceTypeId());
						sceneDetail.setLocationId(createSceneFromTemplateReq.getLocationId());
						sceneApi.insertSceneDetail(sceneDetail);
						log.info("xxxxxx-------------------模板生成情景 end---------------------------");
					}
				}
			}
		}

		return scene.getId();
	}


	@Override
	public Template getByProductId(Long productId,Long orgId, Long tenantId) {
		return templateMapper.getByProductId(productId, orgId,tenantId);
	}

	/**
	 * 描述：查询产品类型目标值
	 *
	 * @param templateId
	 * @return
	 * @author wujianlong
	 * @created 2017年11月15日 上午9:55:46
	 * @since
	 */
	@Override
	public List<DeviceTarValueResp> findDeviceTarValueList(Long templateId) {
		if (templateId == null) {
			throw new BusinessException(BusinessExceptionEnum.PARAMETER_ILLEGALITY);// 参数不能为空
		}

		List<DeviceTarValueResp> respList = templateDetailService.findDeviceTargetValueList(templateId);
		if (respList != null) {
			for (DeviceTarValueResp resp : respList) {
				GetDeviceTypeInfoRespVo deviceTypeResp = deviceTypeApi.get(resp.getDeviceTypeId());
				if (deviceTypeResp != null) {
					resp.setDeviceName(deviceTypeResp.getName());
				}
			}
		}

		return respList;
	}

	/**
	 * 描述：查询情景模板
	 *
	 * @param templateId
	 * @return
	 * @author wujianlong
	 * @created 2017年11月15日 上午11:42:36
	 * @since
	 */
	@Override
	public SceneTemplateResp getSceneTemplate(Long templateId) {
		if (templateId == null) {
			// 参数不能为空
			throw new BusinessException(BusinessExceptionEnum.PARAMETER_ILLEGALITY);
		}

		try {
			SceneTemplateResp sceTem = templateMapper.getTemplate(templateId);
			if (sceTem != null) {
				List<TemplateDetailResp> respList = templateDetailService.findTemplateDetailList(templateId);
				if (respList != null) {
					for (TemplateDetailResp resp : respList) {
						GetDeviceTypeInfoRespVo deviceTypeResp = deviceTypeApi.get(resp.getDeviceTypeId());
						DeviceBusinessTypeResp deviceBusinessType = deviceBusinessTypeApi
								.findById(sceTem.getOrgId(),resp.getTenantId(),resp.getBusinessTypeId());
						if (deviceTypeResp != null) {
							resp.setDeviceType(deviceTypeResp.getType());
						}
						if (deviceBusinessType != null) {
							resp.setBusinessType(deviceBusinessType.getBusinessType());
						}
						//兼容前端
						resp.setDeviceCategoryId(resp.getDeviceTypeId());
						resp.setDeviceCategory(resp.getDeviceType());
					}
				}

				sceTem.setDevTarValueList(respList);
			}
			return sceTem;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
		}
	}

	/**
	 * 描述：删除情景
	 *
	 * @param templateId
	 * @author wujianlong
	 * @created 2017年11月15日 下午3:13:07
	 * @since
	 */
	@Override
	public void deleteSceneTemplate(Long templateId) throws BusinessException {
		if (templateId == null) {
			throw new BusinessException(BusinessExceptionEnum.PARAMETER_ILLEGALITY);// 参数不能为空
		}

		try {
			// 判断情景是否绑定了房间，如果绑定了房间，则不能删除
//			List<String> room = templateDetailService.getRoomByTemplateId(templateId, "scene_template");
//			if (CollectionUtils.isNotEmpty(room)) {
//				throw new BusinessException(BusinessExceptionEnum.SPACE_EXIST_TEMPLATE_MOUNT);
//			}

			templateDetailService.delTemplateDetail(templateId);
			this.delTemplate(templateId);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
		}
	}

	/**
	 * 描述：更新情景
	 *
	 * @param templateDetail
	 * @author wujianlong
	 * @created 2017年11月15日 下午12:00:17
	 * @since
	 */
	@Override
	public void updateSceneTemplate(TemplateDetailReq templateDetail, Long userName, Long tenantId) {
		if (templateDetail == null || null == templateDetail.getTemplateId()) {
			throw new BusinessException(BusinessExceptionEnum.PARAMETER_ILLEGALITY);// 参数不能为空
		}

		try {
			Template tem = new Template();
			tem.setId(templateDetail.getTemplateId());
			tem.setName(templateDetail.getName());
			tem.setUpdateBy(userName);
			tem.setUpdateTime(new Date());
			templateMapper.updateTemplate(tem);
			templateDetailService.delTemplateDetail(templateDetail.getTemplateId());

			List<DeviceTarValueResp> devTarList = (List<DeviceTarValueResp>) JsonUtil
					.fromJsonArray(templateDetail.getDeviceTarValueTo(), DeviceTarValueResp.class);
			if (CollectionUtils.isNotEmpty(devTarList)) {
				TemplateDetail temDetail = null;
				for (DeviceTarValueResp devTar : devTarList) {
					temDetail = new TemplateDetail();
					temDetail.setTemplateId(templateDetail.getTemplateId());
					temDetail.setDeviceCategoryId(devTar.getDeviceCategoryId());
					temDetail.setTargetValue(devTar.getTargetValue());
					temDetail.setCreateBy(userName);
					temDetail.setCreateTime(new Date());
					temDetail.setUpdateBy(userName);
					temDetail.setUpdateTime(new Date());
					temDetail.setTenantId(tenantId);
					templateDetailService.insert(temDetail);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
		}
	}

	@Override
	public void delTemplate(Long templateId) {
		templateMapper.delTemplate(templateId);
	}

	@Override
	public List<TemplateResp> findSceneSpaceTemplateList(TemplateReq templateReq) {
		return templateMapper.findSpaceTemplateList(templateReq);
	}

	/**
	 * @param productModel
	 * @Description:获取模板
	 * @return:
	 * @author: chq
	 * @date: 2018/6/26 15:24
	 **/
	public TemplateVoResp getTempaltesByModel(String productModel,Long orgId) {
		log.info("-----获取ifttt模板的productModel", productModel);
		if (StringUtil.isEmpty(productModel)) {
			return null;
		}
		TemplateVoResp templates = new TemplateVoResp();
		List<Map> iftttTempaltes = new ArrayList<>();
		List<Map> sceneTempaltes = new ArrayList<>();
		GetProductInfoRespVo productResp = productApi.getByProductModel(productModel);
		if (productResp == null || productResp.getId() == null) {
			throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
		}
		// 获取模板
		Template template = getByProductId(productResp.getId(),orgId, productResp.getTenantId());
		if (template == null || template.getId() == null) {
			throw new BusinessException(IftttExceptionEnum.TEMPLATE_IS_NULL);
		}

		// 获取IFTTT模板关系
		List<TemplateIfttt> templateIftttList = templateIftttMapper.selectByTemplateId(template.getId(),
				template.getTenantId(),orgId);
		log.info("-----ifttt模板rule jsonString={}", JSON.toJSON(templateIftttList));
		if (CollectionUtils.isNotEmpty(templateIftttList)) {
			for (TemplateIfttt templateIfttt : templateIftttList) {
				Map<String, Object> returnPayload = getIftttTempaltes(templateIfttt.getRuleId());
				iftttTempaltes.add(returnPayload);
			}

		}

		List<TemplateDetail> templateDetialList = templateDetailService.findByTemplateId(template.getId(),
				template.getTenantId());
		log.info("-----scene模板rule jsonString={}", JSON.toJSON(templateDetialList));
		if (CollectionUtils.isNotEmpty(templateDetialList)) {
			for (TemplateDetail templateDetail : templateDetialList) {
				Map<String, Object> returnPayload = getSceneTempaltes(templateDetail);
				sceneTempaltes.add(returnPayload);
			}
		}
		if (CollectionUtils.isNotEmpty(iftttTempaltes)) {
			if ("kit".equals(template.getTemplateType())) {
				templates.setSecurityTempaltes(iftttTempaltes);
			} else if ("ifttt".equals(template.getTemplateType())) {
				templates.setIftttTempaltes(iftttTempaltes);
			}
		}
		if (CollectionUtils.isNotEmpty(sceneTempaltes)) {
			templates.setSceneTempaltes(sceneTempaltes);
		}

		return templates;
	}

	@Override
	public List<TemplateResp> findSceneTemplateList(TemplateReq templateReq) {
		templateReq.setTemplateType("scene");
		return templateMapper.findTemplateList(templateReq);
	}
	
	@Override
	public List<TemplateResp> findTemplateList(TemplateReq templateReq) {
		return templateMapper.findTemplateList(templateReq);
	}


	public Map<String, Object> getIftttTempaltes(Long ruleId) {
		log.info("-----ifttt模板ruleId", ruleId);
		if (ruleId == null) {
			return null;
		}
		// 获取ifttt相关详情
		RuleResp ruleResp = iftttService.get(ruleId);
		if (ruleResp == null) {
			throw new BusinessException(IftttExceptionEnum.IFTTT_TEMPLATE_IS_NULL);
		}
		Map<String, Object> returnPayload = Maps.newHashMap();
		Map<String, Object> ifMap = Maps.newHashMap();
		List<Map<String, Object>> triggerList = Lists.newArrayList();
		List<Map<String, Object>> thenList = Lists.newArrayList();

		Byte ruleType = ruleResp.getRuleType() != null ? ruleResp.getRuleType() : 0;
		Integer delayTime = ruleResp.getDelay() != null ? ruleResp.getDelay() : 0;

		Integer enabled = 0;
		if (ruleResp.getStatus() != null) {
			if (IftttConstants.STOP.equals(ruleResp.getStatus())) {
				enabled = 0;
			} else if (IftttConstants.RUNNING.equals(ruleResp.getStatus())) {
				enabled = 1;
			}
		}
		List<SensorVo> sensorList = ruleResp.getSensors();
		if (CollectionUtils.isNotEmpty(sensorList)) {
			for (SensorVo sensor : sensorList) {
				String properties = sensor.getProperties();
				triggerList.add(JSON.parseObject(properties, Map.class));
			}
		}
		ifMap.put("trigger", triggerList);
		if (ruleType == 0) { // iftttTempaltes
			String valid = sensorList.get(0).getTiming();
			Map vaildMap = JSON.parseObject(valid, Map.class);
			ifMap.put("valid", vaildMap);
			List<RelationVo> relations = ruleResp.getRelations();
			String logic = relations.get(0).getType();
			ifMap.put("logic", logic);

		}
		returnPayload.put("if", ifMap);

		List<ActuatorVo> actuatorList = ruleResp.getActuators();
		if (CollectionUtils.isNotEmpty(actuatorList)) {
			for (ActuatorVo actuator : actuatorList) {
				String properties = actuator.getProperties();
				thenList.add(JSON.parseObject(properties, Map.class));
			}
		}
		returnPayload.put("then", thenList);

		if (ruleType == 0) { // iftttTempaltes
			returnPayload.put("autoId", ruleResp.getId());
			returnPayload.put("enableDelay", delayTime);
		} else if (ruleType == (byte) 1) { // securityTempaltes
			returnPayload.put("securityType", ruleResp.getSecurityType());
			returnPayload.put("securityId", ruleResp.getId());
			returnPayload.put("defer", delayTime);
		}
		returnPayload.put("enable", enabled);
		return returnPayload;
	}

	public Map<String, Object> getSceneTempaltes(TemplateDetail templateDetail) {
		log.info("-----scene模板rule jsonString={}", JSON.toJSON(templateDetail));
		if (templateDetail == null || templateDetail.getTargetValue() == null) {
			throw new BusinessException(IftttExceptionEnum.TEMPLATE_IS_NULL);
		}
		// 填充scene相关详情
		Map<String, Object> returnPayload = Maps.newHashMap();
		try {
			String TargetValue = templateDetail.getTargetValue();
			List thenList = JSON.parseObject(TargetValue, List.class);

			returnPayload.put("sceneId", templateDetail.getId());
			returnPayload.put("then", thenList);
		} catch (Exception e) {
			log.info("getTempaltes.error：" + e);
			throw new BusinessException(IftttExceptionEnum.TEMPLATE_IS_ERROR, e);
		}
		return returnPayload;
	}

	@Override
	public void delSceneFromTemplate(SpaceTemplateReq spaceTemplateReq) throws BusinessException {
		AssertUtils.notNull(spaceTemplateReq, "parameter.is.null");
		AssertUtils.notNull(spaceTemplateReq.getCreateBy(), "userId.is.null");
		AssertUtils.notNull(spaceTemplateReq.getTemplateId(), "templateId.is.null");

		Template template = templateMapper.getTemplateById(spaceTemplateReq.getTemplateId());
		if (template == null) {
			throw new BusinessException(TemplateExceptionEnum.TEMPLATE_IS_NOT_EXIST);
		}
		SceneReq sceneReq=new SceneReq();
		sceneReq.setTemplateId(spaceTemplateReq.getTemplateId());
		sceneReq.setTenantId(spaceTemplateReq.getTenantId());
		sceneReq.setLocationId(spaceTemplateReq.getLocationId());
		List<SceneResp> scenes = sceneApi.sceneByParamDoing(sceneReq);
		for (SceneResp scene : scenes) {
			SceneReq req=new SceneReq();
			BeanUtil.copyProperties(scene, req);
			sceneApi.deleteScene(req);
		}
	}
}
