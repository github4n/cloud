package com.iot.center.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.iot.common.util.StringUtil;
import io.swagger.annotations.ApiImplicitParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.center.annotation.PermissionAnnotation;
import com.iot.building.scene.vo.req.LocationSceneDetailReq;
import com.iot.building.scene.vo.req.LocationSceneReq;
import com.iot.building.scene.vo.resp.LocationSceneDetailResp;
import com.iot.building.scene.vo.resp.LocationSceneResp;
import com.iot.center.annotation.SystemLogAnnotation;
import com.iot.center.helper.BusinessExceptionEnum;
import com.iot.center.service.SceneService;
import com.iot.center.service.SpaceService;
import com.iot.center.vo.LocationSceneVo;
import com.iot.common.beans.BeanUtil;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.common.helper.Page;
import com.iot.control.scene.vo.req.SceneDetailReq;
import com.iot.control.scene.vo.req.SceneReq;
import com.iot.control.scene.vo.rsp.SceneDetailResp;
import com.iot.control.scene.vo.rsp.SceneResp;
import com.iot.user.vo.LoginResp;

/**
 * 
 * 项目名称：立达信IOT云平台 
 * 模块名称： 
 * 功能描述：情景controller 
 * 创建人： wujianlong 
 * 创建时间：2017年11月10日 下午3:10:45 
 * 修改人： wujianlong 
 * 修改时间：2017年11月10日 下午3:10:45
 */
@RestController()
@RequestMapping("/sceneController")
public class SceneController {

	@Autowired
	private SceneService sceneService;

	@Autowired
	private SpaceService spaceService;
	/**
	 * 
	 * 描述：保存情景模板
	 * 
	 * @author wujianlong
	 * @created 2017年11月14日 下午4:45:12
	 * @since
	 * @param request
	 * @param sceneTemplate
	 * @return
	 */
	/*@RequestMapping("/saveSceneTemplate")
	public CommonResponse<ResultMsg> saveSceneTemplate(HttpServletRequest request,
			@ModelAttribute("sceneTemplate") SceneTemplate sceneTemplate) {
		try {
			sceneService.saveSceneTemplate(sceneTemplate);
			return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
		} catch (BusinessException e) {
			throw e;
		}
	}*/

	/**
	 * TODO control-service 缺少对应接口
	 * 描述：查询产品类型目标值
	 * 
	 * @author wujianlong
	 * @created 2017年11月15日 上午10:23:07
	 * @since
	 * @param request
	 * @param templateId
	 * @return
	 */
//	@RequestMapping("/getDeviceTarValueList")
//	public CommonResponse<List<DeviceTarValue>> getDeviceTarValueList(HttpServletRequest request, String templateId) {
//		try {
//			List<DeviceTarValue> deviceTarValueList = sceneService.getDeviceTarValueList(templateId);
//			return CommonResponse.success(deviceTarValueList);
//		} catch (BusinessException e) {
//			throw e;
//		}
//	}

	/**
	 * 
	 * 描述：查询情景
	 * 
	 * @author wujianlong
	 * @created 2017年11月15日 上午11:57:14
	 * @since
	 * @param request
	 * @param templateId
	 * @return
	 */
	/*@RequestMapping("/getSceneTemplate")
	public CommonResponse<SceneTemplateTO> getSceneTemplate(HttpServletRequest request, String templateId) {
		try {
			SceneTemplateTO sceneTemplateTO = sceneService.getSceneTemplate(templateId);
			return CommonResponse.success(sceneTemplateTO);
		} catch (BusinessException e) {
			throw e;
		}
	}*/

	/**
	 * 
	 * 描述：更新情景
	 * 
	 * @author wujianlong
	 * @created 2017年11月15日 下午3:51:16
	 * @since
	 * @param request
	 * @param templateDetail
	 * @return
	 */
	/*@RequestMapping("/updateSceneTemplate")
	public CommonResponse<ResultMsg> updateSceneTemplate(HttpServletRequest request,
			@ModelAttribute("templateDetail") TemplateDetailTO templateDetail) {
		try {
			sceneService.updateSceneTemplate(templateDetail);
			return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
		} catch (BusinessException e) {
			throw e;
		}
	}*/

	/**
	 * 
	 * 描述：删除情景
	 * 
	 * @author wujianlong
	 * @created 2017年11月15日 下午3:52:05
	 * @since
	 * @param request
	 * @param templateId
	 * @return
	 */
	/*@RequestMapping("/deleteSceneTemplate")
	public CommonResponse<ResultMsg> deleteSceneTemplate(HttpServletRequest request, String templateId) {
		try {
			sceneService.deleteSceneTemplate(templateId);
			return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
		} catch (BusinessException e) {
			throw e;
		}
	}*/

	/**
	 * 
	 * 描述：查询模板
	 * 
	 * @author wujianlong
	 * @created 2017年11月16日 上午11:39:26
	 * @since
	 * @param request
	 * @param name
	 * @return
	 */
//	@RequestMapping("/getTemplateList")
//	public CommonResponse<List<TemplateVO>> getTemplateList(HttpServletRequest request, String name) {
//		try {
//			List<TemplateVO> templateVOList = sceneService.getTemplateList(name);
//			return CommonResponse.success(templateVOList);
//		} catch (BusinessException e) {
//			throw e;
//		}
//	}

	/**
	 * 
	 * 描述：保存房间情景
	 * 
	 * @author wujianlong
	 * @created 2017年11月16日 下午3:32:45
	 * @since
	 * @param request
	 * @param roomTemplate
	 * @return
	 */
//	@RequestMapping("/saveRoomTemplate")
//	public CommonResponse<ResultMsg> saveRoomTemplate(HttpServletRequest request,
//			@ModelAttribute("roomTemplate") RoomTemplateTO roomTemplate) {
//		try {
//			sceneService.saveRoomTemplate(roomTemplate);
//			return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
//		} catch (BusinessException e) {
//			throw e;
//		}
//	}

	/**
	 * 
	 * 描述：保存情景微调
	 * 
	 * @author wujianlong
	 * @created 2017年11月16日 下午4:40:20
	 * @since
	 * @param request
	 * @param deviceTarValues
	 * @return
	 */
    @PermissionAnnotation(value = "SCENE")
	@SystemLogAnnotation(value = "保存情景微调")
	@RequestMapping("/saveSceneDetail")
	public CommonResponse<ResultMsg> saveSceneDetail(HttpServletRequest request, String deviceTarValues,Integer setType) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		try {
			sceneService.saveSceneDetail(deviceTarValues,user.getUserId(),setType,user.getLocationId());
			return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
		} catch (BusinessException e) {
			throw e;
		}
	}

	/**
	 * 
	 * 描述：房间编辑页面保存情景微调
	 * 
	 * @author wujianlong
	 * @created 2017年11月16日 下午4:40:20
	 * @since
	 * @param request
	 * @param deviceTarValues
	 * @return
	 */
    @PermissionAnnotation(value = "SCENE")
	@SystemLogAnnotation(value = "房间编辑页面保存情景微调")
	@RequestMapping("/saveSceneDetailBySpace")
	public CommonResponse<ResultMsg> saveSceneDetailBySpace(HttpServletRequest request, String deviceTarValues,Integer setType) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		try {
			sceneService.saveSceneDetail(deviceTarValues,user.getUserId(),setType,user.getLocationId());
			return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
		} catch (BusinessException e) {
			throw e;
		}
	}

	/**
	 * 
	 * 描述：情景执行
	 * 
	 * @author wujianlong
	 * @created 2017年11月21日 下午6:20:43
	 * @since
	 * @return
	 */
    @PermissionAnnotation(value = "SCENE")
	@SystemLogAnnotation(value = "情景执行")
	@RequestMapping("/sceneExecute")
	public CommonResponse<ResultMsg> sceneExecute(Long sceneId) {
		try {
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			sceneService.sceneExecute(user.getTenantId(), sceneId);
			return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
		} catch (BusinessException e) {
			throw e;
		}
	}



	/**
	 * TODO 该方法应该由设备提供
	 * 描述：获取产品类型
	 * 
	 * @author wujianlong
	 * @created 2017年11月30日 上午11:58:46
	 * @since
	 * @param request
	 * @return
	 */
//	@RequestMapping("/getDeviceCategoryList")
//	public CommonResponse<List<ProductResp>> getDeviceCategoryList(HttpServletRequest request) {
//		try {
//			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
//			ProductReq product = new ProductReq();
//			product.setTenantId(user.getTenantId());
//			List<ProductResp> productResps = sceneService.findProductListByTenantId(product);
//			return CommonResponse.success(productResps);
//		} catch (BusinessException e) {
//			throw e;
//		}
//	}

	/**
	 * 
	 * 描述：查询情景微调
	 * 
	 * @author wujianlong
	 * @created 2017年12月12日 下午4:26:46
	 * @since
	 * @param request
	 * @param spaceId
	 * @param templateId
	 * @return
	 */
	@RequestMapping("/getSceneDetailListBySpaceId")
	public CommonResponse<List<SceneDetailResp>> getSceneDetailListBySpaceId(HttpServletRequest request, Long spaceId,
			Long templateId) {
		try {
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			SceneDetailReq sceneDetailReq = new SceneDetailReq();
			sceneDetailReq.setSpaceId(spaceId);
			sceneDetailReq.setSceneId(templateId);
			sceneDetailReq.setTenantId(user.getTenantId());
			sceneDetailReq.setLocationId(user.getLocationId());
			List<SceneDetailResp> sceneDetailResp = sceneService.findSceneDetailListBySpaceId(sceneDetailReq);
			return CommonResponse.success(sceneDetailResp);
		} catch (BusinessException e) {
			throw e;
		}
	}

	/**
	 * 
	 * 描述：查询情景模板和微调
	 * 
	 * @author wujianlong
	 * @created 2017年12月12日 下午4:26:46
	 * @since
	 * @param request
	 * @param spaceId
	 * @return
	 */
	@RequestMapping("/getSceneDetailList")
	public CommonResponse<List<SceneResp>> getSceneDetailList(HttpServletRequest request, Long spaceId) {
		try {
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			List<SceneResp> sceneResp = sceneService.findSceneDetailList(user.getOrgId(),user.getTenantId(), spaceId);
			return CommonResponse.success(sceneResp);
		} catch (BusinessException e) {
			throw e;
		}
	}
	
	/**
	 * 
	 * 描述：查询情景模板和微调(适配前端属性)
	 * 
	 * @author fenglijian
	 * @created 2018年5月2日 下午3:26:46
	 * @since
	 * @param request
	 * @param spaceId
	 * @return
	 */
	@RequestMapping("/getSceneDetailListAdapter")
	public CommonResponse<List<Map<String, Object>>> getSceneDetailListAdapter(HttpServletRequest request, Long spaceId) {
		try {
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			List<SceneResp> sceneResps = sceneService.findSceneDetailList(user.getOrgId(),user.getTenantId(), spaceId);
			List<Map<String, Object>> result =Lists.newArrayList();
			if(CollectionUtils.isNotEmpty(sceneResps)){
				for(SceneResp sceneResp : sceneResps){
					Map<String, Object> map = Maps.newHashMap();
					Long templateId = sceneResp.getId();
					String sceneName = sceneResp.getSceneName();
					
					map.put("templateId", templateId);
					map.put("name", sceneName);
					
					result.add(map);
				}
			}
			return CommonResponse.success(result);
		} catch (BusinessException e) {
			throw e;
		}
	}


	/**
	 * 
	 * 描述：查询情景微调信息
	 * 
	 * @author linjihuang
	 * @created 2018年3月1日 上午10:26:46
	 * @since
	 * @param request
	 * @param spaceId
	 * @param templateId
	 * @return
	 */
	@RequestMapping("/getSceneDetailInfo")
	public CommonResponse<List<SceneDetailResp>> getSceneDetailInfo(HttpServletRequest request, Long spaceId,
			Long templateId) {
		try {
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			SceneDetailReq sceneDetailReq = new SceneDetailReq();
			sceneDetailReq.setSpaceId(spaceId);
			sceneDetailReq.setSceneId(templateId);
			sceneDetailReq.setTenantId(user.getTenantId());
			sceneDetailReq.setLocationId(user.getLocationId());
			List<SceneDetailResp> sceneDetailResp = sceneService.findSceneDetailInfo(sceneDetailReq);
			return CommonResponse.success(sceneDetailResp);
		} catch (BusinessException e) {
			throw e;
		}
	}

	/**
	 * 
	 * 描述：更新情景微调信息
	 * 
	 * @author linjihuang
	 * @created 2018年3月1日 上午10:26:46
	 * @since
	 * @param request
	 * @param deviceTarValues
	 * @return
	 */
    @PermissionAnnotation(value = "SCENE")
	@SystemLogAnnotation(value = "更新情景微调信息")
	@RequestMapping("/updateSceneDetailInfo")
	public CommonResponse<ResultMsg> updateSceneDetailInfo(HttpServletRequest request, Long sceneId,
			String deviceTarValues, Integer setType) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		try {
			sceneService.updateSceneDetailInfo(sceneId, deviceTarValues, user.getUserId(), setType);
			return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
		} catch (BusinessException e) {
			throw e;
		}
	}

    @PermissionAnnotation(value = "SCENE")
	@SystemLogAnnotation(value = "删除情景微调信息")
	@RequestMapping("/deleteSceneDetail")
	@ResponseBody
	public CommonResponse<ResultMsg> deleteSceneDetail(HttpServletRequest request,Long sceneId, Long spaceId) {
		try {
			if (sceneId != null) {
				LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
				sceneService.deleteSceneDetail(user.getTenantId(), sceneId,spaceId,user.getUserId());
				return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
			} else {
				throw new BusinessException(BusinessExceptionEnum.PARAM_IS_NULL);
			}
		} catch (BusinessException e) {
			throw e;
		}
	}



	/**
	 * 保存/修改整校的locationScene
	 * @param request
	 * @param id
	 * @param name
	 * @return
	 */
    @PermissionAnnotation(value = "SCENE,LOCATION_SCENE")
	@SystemLogAnnotation(value = "保存/修改整校情景")
	@RequestMapping(value="/saveLocationScene", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<ResultMsg> saveLocationScene(HttpServletRequest request, Long id, String name, String[] sceneIds,
													   @RequestParam(required = false) Long buildId, @RequestParam(required = false) Long floorId,
													   Integer shortcut) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		try {
			shortcut=1;//需求变更，全部为1
			LocationSceneReq locationSceneReq = new LocationSceneReq();
			Long tenantId = user.getTenantId();
			Long locationId = user.getLocationId();
			Long userId = user.getUserId();
			locationSceneReq.setId(id);
			locationSceneReq.setName(name);
			locationSceneReq.setTenantId(tenantId);
			locationSceneReq.setLocationId(locationId);
			locationSceneReq.setBuildId(buildId);
			locationSceneReq.setFloorId(floorId);
			locationSceneReq.setCreateBy(userId);
			locationSceneReq.setCreateTime(new Date());
			locationSceneReq.setShortcut(shortcut);
			LocationSceneReq req=new LocationSceneReq();
			req.setName(name);
			req.setTenantId(tenantId);
			req.setLocationId(locationId);
			//List<LocationSceneResp> list =sceneService.findLocationSceneList(req);
			List<LocationSceneResp> list =sceneService.findLocationSceneListByName(req);
			if(id == null){
				if(CollectionUtils.isNotEmpty(list)) {
					throw new BusinessException(BusinessExceptionEnum.SCENE_TEGOTHER_IS_EXIST);
				}
				//添加
                Long idStr = sceneService.saveLocationScene(locationSceneReq);
				saveLocationSceneDetail(idStr,sceneIds);
			}else {
				if(CollectionUtils.isNotEmpty(list)) {
					for(LocationSceneResp resp:list) {
						if(resp.getId().intValue()!=id) {
							throw new BusinessException(BusinessExceptionEnum.SCENE_TEGOTHER_IS_EXIST);
						}
					}
				}
				//修改，先修改location_scene的数据，再删除location_scene_detail的数据和重新添加子表的数据
				sceneService.updateLocationScene(locationSceneReq);
				deleteLocationSceneDetail(id);
				saveLocationSceneDetail(id,sceneIds);
			}
			return CommonResponse.success();
		} catch (BusinessException e) {
			throw e;
		}
	}

	/**
	 * 查询整校locationScene的列表，通过tenantId查询,分页
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = "/findLocationSceneList", method = RequestMethod.POST)
	public CommonResponse<Page<LocationSceneVo>> findLocationSceneList(HttpServletRequest request, @RequestParam(value = "name", required = false) String name,
	@RequestParam(value = "pageNum", required = true) String pageNum ,@RequestParam(value = "pageSize", required = true) String pageSize) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		LocationSceneReq locationSceneReq = new LocationSceneReq();
		locationSceneReq.setTenantId(user.getTenantId());
		locationSceneReq.setLocationId(user.getLocationId());
		locationSceneReq.setPageNum(Integer.valueOf(pageNum));
		locationSceneReq.setPageSize(Integer.valueOf(pageSize));
		if(StringUtil.isNotBlank(name)){
			locationSceneReq.setName(name);
		}
		Page<LocationSceneResp> page = sceneService.findLocationSceneListStr(locationSceneReq);
		Page<LocationSceneVo> pageVO = new Page<>();
		BeanUtil.copyProperties(page, pageVO);
		List<LocationSceneVo> voList = new ArrayList<LocationSceneVo>();
		changeResultPage(page, pageVO, voList);
		//List<LocationSceneResp> list = sceneService.findLocationSceneList(locationSceneReq);
		//CommonResponse<List<LocationSceneResp>> result = new CommonResponse<>(ResultMsg.SUCCESS,list);
		//return result;
		return CommonResponse.success(pageVO);
	}

	/**
	 * 查询整校locationScene的列表，通过tenantId查询
	 * @return
	 */
	/*@ResponseBody
	@RequestMapping(value = "/findLocationSceneList", method = RequestMethod.POST)
	public CommonResponse<List<LocationSceneResp>> findLocationSceneList() {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		LocationSceneReq locationSceneReq = new LocationSceneReq();
		locationSceneReq.setTenantId(user.getTenantId());
		List<LocationSceneResp> list = sceneService.findLocationSceneList(locationSceneReq);
		CommonResponse<List<LocationSceneResp>> result = new CommonResponse<>(ResultMsg.SUCCESS,list);
		return result;
		//return CommonResponse.success(pageVO);
	}*/


	/*public CommonResponse<Page<DeviceVO>> findUnDirectDevicePage(HttpServletRequest request, DevicePageReq pageReq) {
		try {
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			pageReq.setTenantId(user.getTenantId());
			Page<DeviceResp> page = deviceService.findUnDirectDevicePage(pageReq);
			Page<DeviceVO> pageVO = new Page<>();
			BeanUtil.copyProperties(page, pageVO);
			List<DeviceVO> voList = new ArrayList<DeviceVO>();
			changeResultPage(page, pageVO, voList);
			return CommonResponse.success(pageVO);
		} catch (BusinessException e) {
			throw e;
		}
	}*/


	private void changeResultPage(Page<LocationSceneResp> page, Page<LocationSceneVo> pageVO, List<LocationSceneVo> voList) {
		if (!page.getResult().isEmpty()) {
			for (LocationSceneResp deviceResp : (List<LocationSceneResp>) page.getResult()) {
				LocationSceneVo locationSceneVo = new LocationSceneVo();
				changeDeviceRespToDeviceVO(deviceResp, locationSceneVo);
				voList.add(locationSceneVo);
			}
		}
		pageVO.setResult(voList);
	}

	private LocationSceneResp changeDeviceRespToDeviceVO(LocationSceneResp deviceResp,LocationSceneVo locationSceneVo) {
		if (deviceResp != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			BeanUtil.copyProperties(deviceResp, locationSceneVo);
			if (deviceResp.getCreateTime() != null) {
				String createTime = simpleDateFormat.format(deviceResp.getCreateTime());
				locationSceneVo.setCreateTime(deviceResp.getCreateTime());
			}
			/*if (deviceResp.getLastUpdateDate() != null) {
				String updateTime = simpleDateFormat.format(deviceResp.getLastUpdateDate());
				deviceVO.setLastUpdateDate(updateTime);
			}*/
		}
		return deviceResp;
	}
    /**
     * 查询整校的所有情景详情
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findAllLocationScene", method = RequestMethod.POST)
    public CommonResponse<List<LocationSceneResp>> findAllLocationScene(HttpServletRequest request) {
        List<LocationSceneResp> resutlList = new ArrayList<LocationSceneResp>();

        LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
        LocationSceneReq locationSceneReq = new LocationSceneReq();
        locationSceneReq.setTenantId(user.getTenantId());
        List<LocationSceneResp> list = sceneService.findLocationSceneList(locationSceneReq);
        for(LocationSceneResp locationSceneResp:list){
            //通过tenantId 和 locationSceneId 查询下级集合
            locationSceneResp.setLocationSceneDetailResp(getCommonDetailList(locationSceneResp.getId(),user.getTenantId()));
            resutlList.add(locationSceneResp);
        }
        CommonResponse<List<LocationSceneResp>> result = new CommonResponse<>(ResultMsg.SUCCESS,resutlList);
        return result;
    }


    /**
     * 删除整校情景
     * @param id
     * @return
     */
    @PermissionAnnotation(value = "SCENE,LOCATION_SCENE")
	@SystemLogAnnotation(value = "删除整校情景")
	@RequestMapping("/deleteLocationScene")
	@ResponseBody
	public CommonResponse<ResultMsg> deleteLocationScene(Long id) {
		try {
			if (id != null) {
				LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
				Long tenantId = user.getTenantId();
				//删除location_scene表中的数据,通过id
				sceneService.delLocalSceneById(id, tenantId);
				return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
			} else {
				throw new BusinessException(BusinessExceptionEnum.PARAM_IS_NULL);
			}
		} catch (BusinessException e) {
			throw e;
		}
	}

	/**
	 * 删除整校情景
	 * @return
	 */
    @PermissionAnnotation(value = "SCENE,LOCATION_SCENE")
	@SystemLogAnnotation(value = "删除整校情景")
	@RequestMapping("/deleteLocationSceneBatch")
	@ResponseBody
	public CommonResponse<ResultMsg> deleteLocationSceneBatch(String ids) {
		try {
			if (StringUtils.isNotBlank(ids)) {
				LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
				Long tenantId = user.getTenantId();
				String[] idArry=ids.split(",");
				for(String idStr:idArry) {
					sceneService.delLocalSceneById(Long.valueOf(idStr), tenantId);
				}
				return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
			} else {
				throw new BusinessException(BusinessExceptionEnum.PARAM_IS_NULL);
			}
		} catch (BusinessException e) {
			throw e;
		}
	}

    @PermissionAnnotation(value = "SCENE,LOCATION_SCENE")
	@RequestMapping("/deleteLocationSceneDetail")
	@ResponseBody
	public CommonResponse<ResultMsg> deleteLocationSceneDetail(Long locationSceneId) {
		try {
			if (locationSceneId != null) {
				LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
				Long tenantId = user.getTenantId();
				sceneService.deleteLocationSceneDetailStr(tenantId, locationSceneId);
				return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
			} else {
				throw new BusinessException(BusinessExceptionEnum.PARAM_IS_NULL);
			}
		} catch (BusinessException e) {
			throw e;
		}
	}


	/**
	 * 保存/修改整校的locationSceneDetail
	 * @param locationSceneId
	 * @param sceneIds  情景id的数组
	 * @return
	 */
    @PermissionAnnotation(value = "SCENE,LOCATION_SCENE")
	@RequestMapping(value="/saveLocationSceneDetail", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<ResultMsg> saveLocationSceneDetail(Long locationSceneId,String[] sceneIds) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		try {
            for(String sceneId:sceneIds){
                LocationSceneDetailReq locationSceneDetailReq = new LocationSceneDetailReq();
                Long tenantId = user.getTenantId();
                Long userId = user.getUserId();
                locationSceneDetailReq.setLocationSceneId(locationSceneId);
                locationSceneDetailReq.setSceneId(Long.valueOf(sceneId));
                locationSceneDetailReq.setTenantId(tenantId);
                //locationSceneDetailReq.setId(id);
                locationSceneDetailReq.setCreateBy(userId);
                locationSceneDetailReq.setCreateTime(new Date());
                sceneService.saveOrUpdateLocationSceneDetail(locationSceneDetailReq);
            }
			return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
		} catch (BusinessException e) {
			throw e;
		}
	}


	/**
	 * 查询整校locationSceneDetail的列表，通过tenantId和locationSceneId查询
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findLocationSceneDetailList", method = RequestMethod.POST)
	public CommonResponse<Map<String,Object>> findLocationSceneDetailList(HttpServletRequest request,Long locationSceneId) {
		Map<String,Object> map=Maps.newHashMap();
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		LocationSceneReq locationSceneReq =new LocationSceneReq();
		locationSceneReq.setId(locationSceneId);
		locationSceneReq.setTenantId(user.getTenantId());
		locationSceneReq.setLocationId(user.getLocationId());
		List<LocationSceneDetailResp> list = getCommonDetailList(locationSceneId, user.getTenantId());
		LocationSceneResp resp=sceneService.findLocationSceneList(locationSceneReq).get(0);
		map.put("detail", resp);
		map.put("list", list);
		return CommonResponse.success(map);
	}

	private List<LocationSceneDetailResp> getCommonDetailList(Long locationSceneId,Long tenantId) {
		LocationSceneDetailReq locationSceneDetailReq = new LocationSceneDetailReq();
		locationSceneDetailReq.setLocationSceneId(locationSceneId);
		locationSceneDetailReq.setTenantId(tenantId);
		List<LocationSceneDetailResp> list = sceneService.findLocationSceneDetailList(locationSceneDetailReq);
		return list;
	}

	@RequestMapping("/findTree")
	@ResponseBody
	public CommonResponse<List<Map<String, Object>>> findTree(HttpServletRequest request) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		List<Map<String, Object>> spaceListStr = sceneService.findTree(user.getOrgId(),user.getTenantId(), user.getLocationId());
		//List<Map<String, Object>> spaceList = spaceService.findTree(user.getLocationId());
		return CommonResponse.success(spaceListStr);
	}

	/**
	 * 描述：整校执行
	 * @param locationSceneId
	 * @return
	 */
    @PermissionAnnotation(value = "SCENE,LOCATION_SCENE")
	@SystemLogAnnotation(value = "整校情景执行")
	@RequestMapping("/locationSceneExecute")
	public CommonResponse<ResultMsg> locationSceneExecute(Long locationSceneId) {
		try {
			//通过locationSceneId 查询 sceneId的集合
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			List<Long> sceneIds = sceneService.findSceneIds(user.getTenantId(), locationSceneId);
			for(Long sceneId:sceneIds){
				sceneService.sceneExecute(user.getTenantId(), sceneId);
			}
			return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
		} catch (BusinessException e) {
			throw e;
		}
	}

	@SystemLogAnnotation(value = "查询情景列表")
	@RequestMapping("querySceneList")
	public CommonResponse<List<SceneResp>> querySceneList(SceneDetailReq req) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		req.setTenantId(user.getTenantId());
		req.setLocationId(user.getLocationId());
		return sceneService.querySceneList(req);
	}

    @PermissionAnnotation(value = "SCENE")
	@SystemLogAnnotation(value = "情景下发")
	@RequestMapping("issueScene")
	public CommonResponse<ResultMsg> issueScene(@RequestBody SceneReq req) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		req.setTenantId(user.getTenantId());
		req.setLocationId(user.getLocationId());
		return sceneService.issueScene(req);
	}
}
