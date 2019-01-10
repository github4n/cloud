package com.iot.center.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.iot.center.annotation.PermissionAnnotation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.iot.building.allocation.api.EnvironmentPropertyApi;
import com.iot.building.reservation.vo.ReservationResp;
import com.iot.building.space.vo.CalendarReq;
import com.iot.building.space.vo.CalendarResp;
import com.iot.building.space.vo.DeploymentResp;
import com.iot.building.space.vo.SpaceBackgroundImgReq;
import com.iot.building.space.vo.SpaceBackgroundImgResp;
import com.iot.building.warning.vo.WarningReq;
import com.iot.building.warning.vo.WarningResp;
import com.iot.center.annotation.SystemLogAnnotation;
import com.iot.center.helper.BusinessExceptionEnum;
import com.iot.center.helper.Constants;
import com.iot.center.service.SpaceService;
import com.iot.center.utils.ConstantUtil;
import com.iot.center.utils.HttpUtil;
import com.iot.center.vo.SpaceEhiVO;
import com.iot.center.vo.SpaceVO;
import com.iot.common.beans.BeanUtil;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.common.helper.Page;
import com.iot.common.util.QrCodeUtil;
import com.iot.control.space.api.SpaceApi;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceReq;
import com.iot.control.space.vo.SpaceResp;
import com.iot.control.space.vo.SpaceVo;
import com.iot.device.api.DeviceApi;
import com.iot.saas.SaaSContextHolder;
import com.iot.user.api.UserApi;
import com.iot.user.vo.LoginResp;
import com.iot.util.ToolUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = { "空间管理接口", "健康指数接口" })
@Controller
@RequestMapping("/space")
public class SpaceController {

	private static final Logger log = LoggerFactory.getLogger(SpaceController.class);

	public final static String SPACE_ROOM = "ROOM";// 房间
	@Resource
	private ResourceLoader resourceLoader;

	@Autowired
	private ConstantUtil constantUtil;

	@Autowired
	private Environment environment;
	@Autowired
	private EnvironmentPropertyApi environmentPropertyApi;
	@Autowired
	SpaceService spaceService;
	@Autowired
	DeviceApi deviceApi;
	@Autowired
	UserApi userApi;
	@Autowired
	SpaceApi spaceApi;

	@ApiOperation("findSpaceList")
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/findSpaceList")
	@ResponseBody
	public CommonResponse<List<SpaceVO>> findSpaceList(String name) throws BusinessException {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		if (name == null) {
			name = "";
		}
		SpaceReq req = new SpaceReq();
		req.setTenantId(user.getTenantId());
		req.setLocationId(user.getLocationId());
		req.setName(name);
		// req.setPageSize(pageSize);
		// req.setPageNumber(pageNumber);
		List<SpaceVO> voList = new ArrayList<SpaceVO>();
		List<SpaceResp> list = spaceApi.findSpaceByCondition(req);
		for (SpaceResp space : list) {
			spaceService.changeVO(voList, space);
		}
		return CommonResponse.success(voList);
	}

	// @PermissionAnnotation(value = "SPACE_MANAGER")
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/findSpacePage")
	@ResponseBody
	public CommonResponse<Page<SpaceVO>> findSapcePage(String name, int pageNumber, int pageSize)
			throws BusinessException {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		if (name == null) {
			name = "";
		}
		// pageSize = 10;
		Map<String, Object> params = new HashMap<String, Object>();
		Page page = spaceService.findSpaceByLocationId(user.getLocationId(), user.getTenantId(), name, pageNumber,
				pageSize);
		return CommonResponse.success(page);
	}

    @PermissionAnnotation(value = "SPACE")
	@SystemLogAnnotation(value = "创建空间")
	@RequestMapping("/create")
	@ResponseBody
	public CommonResponse<ResultMsg> create(HttpServletRequest request, String name, String icon, String position,
			Long parentId, String type, Integer sort, Long deployId) {
		SpaceReq space = new SpaceReq();
		space.setName(name);
		space.setIcon(icon);
		space.setPosition(position);
		space.setParentId(parentId);
		space.setType(type);
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		space.setUserId(user.getUserId());
		space.setLocationId(user.getLocationId());
		space.setCreateBy(user.getUserId());
		space.setUpdateBy(user.getUserId());
		space.setCreateTime(new Date());
		space.setUpdateTime(new Date());
		space.setTenantId(user.getTenantId());
		space.setSort(sort);
		space.setDeployId(deployId);
		//Long spaceId = spaceService.save(space);
		return spaceService.save(space);
		//space.setId(spaceId);
		// syncSpace2Sign(user.getTenantId());
		//return CommonResponse.success(space);
	}

    @PermissionAnnotation(value = "SPACE")
	@SystemLogAnnotation(value = "修改空间")
	@RequestMapping("/update")
	@ResponseBody
	public CommonResponse<ResultMsg> update(HttpServletRequest request, Long id, String name, String icon,
			String position, Long parentId, String type, Integer sort, Long deployId) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		SpaceResp space = spaceService.findById(user.getTenantId(), id);
		if (space == null) {
			throw new BusinessException(BusinessExceptionEnum.NOT_EXIST_SPACE);
		}
		if (StringUtils.isNotBlank(name)) {
			space.setName(name);
		}
		if (StringUtils.isNotBlank(icon)) {
			space.setIcon(icon);
		}
		if (StringUtils.isNotBlank(position)) {
			space.setPosition(position);
		}
		if (parentId != null) {
			space.setParentId(parentId);
		}
		if (StringUtils.isNotBlank(type)) {
			space.setType(type);
		}
		if (user.getLocationId() != null) {
			space.setLocationId(user.getLocationId());
		}
		if (sort != null) {
			space.setSort(sort);
		}
		if (deployId != null) {
			space.setDeployId(deployId);
		}
		space.setUpdateBy(user.getUserId());
		SpaceReq req = new SpaceReq();
		try {
			BeanUtil.copyProperties(space, req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		req.setUpdateTime(new Date());
		return spaceService.update(req);
	}

    @PermissionAnnotation(value = "SPACE")
	@SystemLogAnnotation(value = "删除空间")
	@RequestMapping("/delete")
	@ResponseBody
	public CommonResponse<ResultMsg> delete(HttpServletRequest request, Long id) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		spaceService.delete(user.getTenantId(), id);
		return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
	}

    @PermissionAnnotation(value = "SPACE")
	@SystemLogAnnotation(value = "删除空间")
	@RequestMapping("/deleteBatch")
	@ResponseBody
	public CommonResponse<ResultMsg> deleteBatch(String spaceIds) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		List<Long> spaceIdList = Lists.newArrayList();
		String[] spaceIdArry = spaceIds.split(",");
		if (StringUtils.isNotBlank(spaceIds)) {
			for (String spaceIdStr : spaceIdArry) {
				spaceIdList.add(Long.valueOf(spaceIdStr));
			}
		}
		spaceService.deleteBatch(user.getTenantId(), spaceIdList);
		return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
	}

	@RequestMapping("/findById")
	@ResponseBody
	public CommonResponse<SpaceResp> findById(HttpServletRequest request, Long id) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		SpaceResp space = spaceService.findById(user.getTenantId(), id);
		if (space.getType().equals("ROOM") && space.getModel() != null && space.getModel() == 1) {
			space.setType("MEETING");
		}
		return CommonResponse.success(space);
	}

	@RequestMapping("/findListByUser")
	@ResponseBody
	public CommonResponse<List<SpaceResp>> findListByUser(HttpServletRequest request) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		String name = request.getParameter("name") == null ? "" : request.getParameter("name").toString();
		List<SpaceResp> spliceList = spaceService.findSpaceByLocationId(user.getLocationId(), user.getOrgId(),user.getTenantId(), name);
		return CommonResponse.success(spliceList);
	}

	@RequestMapping("/findSpaceUnMount")
	@ResponseBody
	public CommonResponse<List<SpaceResp>> findSpaceUnMount(HttpServletRequest request, Long spaceId, String name) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		SpaceResp resq = spaceService.findById(user.getTenantId(), spaceId);
		SpaceReq req = new SpaceReq();
		req.setType(resq.getType());
		req.setTenantId(resq.getTenantId());
		req.setName(name);
		List<SpaceResp> spliceList = spaceService.findSpaceUnMount(req);
		return CommonResponse.success(spliceList);
	}

	@RequestMapping("/searchByName")
	@ResponseBody
	public CommonResponse<List<SpaceResp>> searchByName(HttpServletRequest request, String name) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("locationId", user.getLocationId());
		List<SpaceResp> spliceList = spaceService.findSpaceByLocationId(user.getLocationId(),user.getOrgId(), user.getTenantId(), name);
		return CommonResponse.success(spliceList);
	}

	// @PermissionAnnotation(value = "RELATION_MANAGER")
	@RequestMapping("/findTree")
	@ResponseBody
	public CommonResponse<List<Map<String, Object>>> findTree(HttpServletRequest request, Long tenantId,
			Long locationId) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		List<Map<String, Object>> spaceList = spaceService.findTree(user.getLocationId(), user.getTenantId());
		return CommonResponse.success(spaceList);
	}

	@RequestMapping("/getChild")
	@ResponseBody
	public CommonResponse<List<SpaceResp>> getChild(HttpServletRequest request, Long spaceId, String name) {
		List<SpaceResp> spaceList = null;
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		try {
			spaceList = spaceService.getChildSpace(user.getTenantId(), spaceId, name);
		} catch (BusinessException e) {
			e.printStackTrace();
			throw e;
		}
		return CommonResponse.success(spaceList);
	}

	@ApiOperation("获取楼层的房间")
	@RequestMapping(value = "/getFloorRooms", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public CommonResponse<List<SpaceVo>> findFloorRooms(@RequestParam("spaceId") Long spaceId)
			throws BusinessException {
		List<SpaceVo> spaceList = null;
		try {
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			spaceList = spaceService.getChildSpaceStatus(user.getTenantId(),user.getOrgId(), spaceId, SPACE_ROOM);
		} catch (BusinessException e) {
			e.printStackTrace();
			throw e;
		}
		return CommonResponse.success(spaceList);
	}

	/**
	 * 转换
	 * 
	 * @param spaceRespList
	 * @return
	 */
	private List<SpaceVO> spaceRespToSpaceVo(List<SpaceResp> spaceRespList) {
		List<SpaceVO> spaceList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(spaceRespList)) {
			spaceRespList.forEach(spaceResp -> {
				SpaceVO vo = new SpaceVO();
				vo.setCreateTime(ToolUtils.dateToSting(spaceResp.getCreateTime()));
				vo.setName(spaceResp.getName());
				vo.setParentId(spaceResp.getParentId().toString());
				vo.setSort(spaceResp.getSort());
				vo.setType(spaceResp.getType());
				vo.setUpdateTime(ToolUtils.dateToSting(spaceResp.getUpdateTime()));
				vo.setId(spaceResp.getId().toString());
				spaceList.add(vo);
			});
		}
		return spaceList;
	}

	@ApiOperation("获取房间健康指数")
	@RequestMapping(value = "/getEnvironmentalHealthIndex", method = RequestMethod.GET)
	@ResponseBody
	public CommonResponse<SpaceEhiVO> getRoomHealthValues(@RequestParam("spaceId") Long spaceId)
			throws BusinessException {
		SpaceEhiVO spaceEhiVO = null;
		try {
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			spaceEhiVO = spaceService.getEnvironmentalHealthIndex(user.getTenantId(),user.getOrgId(),spaceId);
		} catch (BusinessException e) {
			e.printStackTrace();
			throw e;
		}
		return CommonResponse.success(spaceEhiVO);
	}

	// TODO 设备挂载
	// @ApiOperation("设备挂载接口")
	// @RequestMapping(value = "/mount", method = RequestMethod.POST)
	// @ResponseBody
	// public CommonResponse<ResultMsg> mount(@RequestParam("spaceId") Long spaceId,
	// @RequestParam("deviceIds") String deviceIds) {
	// LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
	// try {
	// spaceService.mount(user.getTenantId(), spaceId, deviceIds,
	// user.getLocationId());
	// } catch (Exception e) {
	// e.printStackTrace();
	// throw new BusinessException(BusinessExceptionEnum.GATEWAY_UNCONNECT);
	// }
	// return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
	// }

	@RequestMapping("/getFloor")
	@ResponseBody
	public CommonResponse<List<Map<String, Object>>> getFloor(Long buildId, String types) {
		List<Map<String, Object>> floorAndDeviceList = null;
		if (StringUtils.isBlank(types)) {
			throw new BusinessException(BusinessExceptionEnum.BUILDID_OR_TYPES_EMPTY);
		}
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		Long tenantId = user.getTenantId();
		SaaSContextHolder.setCurrentTenantId(tenantId);
		floorAndDeviceList = spaceService.getFloorAndDeviceCount(tenantId,user.getOrgId(), buildId, types);
		return CommonResponse.success(floorAndDeviceList);
	}

	@RequestMapping("/findDeviceByRoom")
	@ResponseBody
	public CommonResponse<List<Map<String, Object>>> findDeviceByRoom(Long spaceId) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		List<Map<String, Object>> map = spaceService.findDeviceByRoom(spaceId,user.getOrgId(), user.getTenantId());
		return CommonResponse.success(map);
	}

	@SystemLogAnnotation(value = "群控接口")
	@ApiOperation("群控接口")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/groupControl", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<ResultMsg> groupControl(@RequestParam("spaceId") Long spaceId,
			@RequestParam("deviceType") String deviceType, @RequestParam("value") String value) {
		try {
			log.info("----------" + "spaceId=" + spaceId + "deviceType=" + deviceType + "value=" + value);
			Map<String, Object> propertyMap = (Map<String, Object>) JSON.parse(value);
			if (spaceService.groupControl(spaceId, deviceType, propertyMap)) {
				return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
			} else {
				return new CommonResponse<ResultMsg>(ResultMsg.FAIL);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * @param request
	 * @param deviceId
	 *            设备id
	 * @param deviceType
	 *            设备类型
	 * @param value
	 *            命令
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/control")
	@ResponseBody
	public CommonResponse<ResultMsg> control(HttpServletRequest request, HttpServletResponse response, String deviceId,
			String deviceType, String value) {
		if (StringUtils.isNotBlank(value)) {
			Map<String, Object> propertyMap = (Map<String, Object>) JSON.parse(value);
			if (spaceService.control(deviceId, propertyMap)) {
				return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
			} else {
				return new CommonResponse<ResultMsg>(ResultMsg.FAIL);
			}
		} else {
			throw new BusinessException(BusinessExceptionEnum.PARAM_IS_NULL);
		}
	}

	@RequestMapping("/callBack")
	@ResponseBody
	public void callBack(HttpServletRequest request, String code, String state) {
		try {
			String a = code;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存
	 * 
	 * @param request
	 * @param spaceId
	 * @param childIds
	 * @return
	 */
    @PermissionAnnotation(value = "SPACE")
	@RequestMapping("/setRelation")
	@ResponseBody
	public CommonResponse<ResultMsg> relation(HttpServletRequest request, Long spaceId, String childIds) {
		try {
			if (spaceId != null) {
				LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
				spaceService.setSpaceRelation(user.getTenantId(),user.getOrgId(), spaceId, childIds);
				return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
			} else {
				throw new BusinessException(BusinessExceptionEnum.PLEASE_SELECT_THE_SPACE_FIRST);
			}
		} catch (Exception e) {
			throw new BusinessException(BusinessExceptionEnum.PARAMETER_ILLEGALITY);
		}

	}

	/**
	 * 获取空间下直接挂载的设备
	 * 
	 * @param spaceId
	 */
	@RequestMapping("/getDirectDeviceBySpace")
	@ResponseBody
	public CommonResponse<List<Map<String, Object>>> getDirectDeviceBySpace(Long spaceId) {
		if (spaceId == null) {
			throw new BusinessException(BusinessExceptionEnum.NOT_EXIST_SPACE);
		}
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		List<Map<String, Object>> directDeviceList = spaceService.getDirectDeviceBySpace(user.getTenantId(),user.getOrgId(),spaceId);
		return CommonResponse.success(directDeviceList);
	}

	/**
	 * 编辑空间下的设备位置
	 * 
	 */
    @PermissionAnnotation(value = "SPACE")
	@SystemLogAnnotation(value = "编辑空间下的设备位置")
	@RequestMapping("/updateSpaceDevicePosition")
	@ResponseBody
	public CommonResponse<ResultMsg> updateSpaceDevicePosition(String message) {
		if (StringUtils.isBlank(message)) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_IS_NULL);
		}
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		Map<String, Object> params = (Map<String, Object>) JSON.parse(message);
		Long spaceId = Long.parseLong(params.get("spaceId").toString());
		spaceService.updateSpaceDevicePosition(user.getTenantId(), spaceId, message);
		return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
	}

	/**
	 * 
	 * 描述：保存楼层风格
	 * 
	 * @author chenxiaolin
	 * @created 2018年2月26日 下午3:19:06
	 * @param spaceId
	 * @param style
	 * @return
	 */
    @PermissionAnnotation(value = "SPACE")
	@SystemLogAnnotation(value = "保存楼层风格")
	@RequestMapping("/setStyle")
	@ResponseBody
	public CommonResponse<ResultMsg> setStyle(Long spaceId, String style) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		SpaceReq req = new SpaceReq();
		req.setId(spaceId);
		req.setStyle(style);
		req.setTenantId(user.getTenantId());
		spaceService.update(req);
		return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
	}

	/**
	 * 描述：获取楼层自定义
	 * 
	 * @author chenxiaolin
	 * @created 2018年2月26日 下午5:53:43
	 * @param spaceId
	 * @return
	 */
	@RequestMapping("/getStyle")
	@ResponseBody
	public CommonResponse<ResultMsg> getStyle(Long spaceId) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		SpaceResp spaceResp = spaceService.findById(user.getTenantId(), spaceId);
		String style = spaceResp.getStyle();
		return new CommonResponse(ResultMsg.SUCCESS, style);
	}

	/**
	 * 描述：获取房间控制二维码
	 * 
	 * @author chenxiaolin
	 * @created 2018年2月26日 下午5:54:01
	 * @param req
	 * @param name
	 * @param spaceId
	 * @return
	 */
    @PermissionAnnotation(value = "SPACE")
	@ResponseBody
	@RequestMapping("showQrCode")
	public CommonResponse<String> showQrCode(HttpServletRequest req, String name, String spaceId) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		Long tenantId = user.getTenantId();
		Long locationId = user.getLocationId();
		String mqttUUID = constantUtil.getAdminUuid();
		String mqttPwd = constantUtil.getAdminPwd();
		String host = environmentPropertyApi.getPropertyValue(Constants.CONTROL_URL) + "?spaceId=" + spaceId
				+ "&locationId=" + locationId + "&tenantId=" + tenantId + "&uuid=" + mqttUUID + "&mqttPwd=" + mqttPwd;
		String base64Data = QrCodeUtil.qrCodeToString(host, name);
		String result = "data:image/jpg;base64," + base64Data;
		// StringBuffer sb = req.getRequestURL();
		// String host = sb.substring(0, sb.length() - req.getRequestURI().length());
		// host = host + "/index.html?roomEdit_" + spaceId + "_" + name;
		// String base64Data = QrCodeUtil.qrCodeToString(host, name);
		// String result = "data:image/jpg;base64," + base64Data;
		return CommonResponse.success(result);
	}

	/**
	 * 描述：下载房间控制二维码
	 * 
	 * @author chenxiaolin
	 * @created 2018年2月26日 下午5:54:23
	 * @param req
	 * @param res
	 * @param name
	 * @param spaceId
	 */
	@ResponseBody
	@RequestMapping("downQrCode")
	public void downQrCode(HttpServletRequest req, HttpServletResponse res, String name, String spaceId) {
		res.setContentType("text/html;charset=utf-8");
		res.setHeader("Content-Disposition", "attachment;filename=qrcode.jpg");
		StringBuffer sb = req.getRequestURL();
		String host = sb.substring(0, sb.length() - req.getRequestURI().length());
		host = host + "/index.html?roomEdit_" + spaceId + "_" + name;
		byte[] data = QrCodeUtil.qrCodeToByte(host, name);
		try {
			OutputStream os = res.getOutputStream();
			os.write(data);
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询数据统计
	 * 
	 * @author fenglijian
	 * @param spaceId
	 *            房间ID
	 * @param deviceId
	 *            传感器id[温度，湿度，PM2.5 ...]
	 * @param dateType
	 *            日期类型[天（24小时） day，星期 week，月 month]
	 */
	@RequestMapping("/getReportData")
	@ResponseBody
	public CommonResponse<Map<String, Object>> queryDataReport(HttpServletRequest request, Long spaceId,
			String deviceId, String dateType, @RequestParam(required = false, name = "deviceType") String deviceType) {
		Map<String, Object> map = spaceService.queryDataReport(spaceId, deviceId, dateType, deviceType);
		return CommonResponse.success(map);
	}

	@ApiOperation(value = "获取所有空间")
	@RequestMapping(value = "/getAllSpace", method = { RequestMethod.GET })
	@ResponseBody
	public CommonResponse<List<SpaceResp>> getAllSpace(Long locationId) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		List<SpaceResp> spliceList = spaceService.findConstructorSpaceByLocationId(locationId,user.getOrgId(),user.getTenantId());
		return CommonResponse.success(spliceList);
	}

	public static void main(String[] args) {
		String b = "23";
		String ids = "a.jpg";
		// list.forEach(str ->
		// {spaceIdList.add(str);System.out.println(spaceIdList.toArray().toString());});
		System.out.println(ids.replace(ids.split("\\.")[0], b));
	}

    @PermissionAnnotation(value = "SPACE")
	@SystemLogAnnotation(value = "保存空间设备信息")
	@ApiOperation("保存空间设备信息")
	@RequestMapping(value = "/saveSpaceDevice", method = { RequestMethod.POST })
	@ResponseBody
	public CommonResponse<String> saveSpaceDevice(@RequestBody String spaceDeviceStr) throws BusinessException {
		CommonResponse<String> result = null;
		try {
			List<SpaceDeviceReq> spaceDeviceList = JSON.parseArray(spaceDeviceStr, SpaceDeviceReq.class);
			spaceService.saveSpaceDevice(spaceDeviceList);
			result = CommonResponse.success();
		} catch (BusinessException e) {
			e.printStackTrace();
			result = new CommonResponse(ResultMsg.FAIL, "save space device error");
		}
		return result;
	}

	/**
	 * 调用签到APP接口
	 */
	@SystemLogAnnotation(value = "同步空间信息到签到APP")
	@ApiOperation("同步空间信息到签到APP")
	@RequestMapping(value = "/syncSpace2Sign", method = { RequestMethod.POST })
	@ResponseBody
	public void syncSpace2Sign(@RequestBody Long tenantId) {
//		new Thread(() -> {
//			List<SpaceResp> spaceList = spaceService.findSpaceByTenantId(tenantId);
//			String spaceListStr = JSON.toJSONString(spaceList);
//
//			environment = ApplicationContextHelper.getBean(Environment.class);
//
//			String url = environment.getProperty("task.url.push-space-url");
//
//			String param = "spaceListStr=" + spaceListStr + "&tenantId=" + tenantId;
//
//			HttpUtil.sendPost(url, param);
//		}).start();
	}

    @PermissionAnnotation(value = "SCHEDULE")
    @SystemLogAnnotation(value = "添加/修改日程")
	@RequestMapping("/addOrUpdateCalendar")
	@ResponseBody
	public CommonResponse<ResultMsg> addOrUpdateCalendar(HttpServletRequest request, CalendarReq calendarReq) {
		try {
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			calendarReq.setTenantId(user.getTenantId());
			calendarReq.setLocationId(user.getLocationId());
			if (calendarReq.getId() == null) {// add
				calendarReq.setCreateBy(user.getUserId());
				calendarReq.setCreateTime(new Date());
			} else {// updateeditDeploy
				calendarReq.setUpdateBy(user.getUserId());
				calendarReq.setUpdateTime(new Date());
			}
			return spaceService.addOrUpdateCalendar(calendarReq);
			//return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
		} catch (BusinessException e) {
			throw e;
		}
	}

    @PermissionAnnotation(value = "SCHEDULE")
	@SystemLogAnnotation(value = "删除日程")
	@RequestMapping("/deleteCalendar")
	@ResponseBody
	public CommonResponse<ResultMsg> deleteCalendar(HttpServletRequest request, String id) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		String[] ids = id.split(",");//批量删除
		for(String str : ids){
			spaceService.deleteCalendar(user.getTenantId(),user.getOrgId(), Long.valueOf(str));
		}
		return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
	}

	@RequestMapping("/getCalendarIofoById")
	@ResponseBody
	public CommonResponse<CalendarResp> getCalendarIofoById(HttpServletRequest request, Long id) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		Long tenantId = user.getTenantId();
		Long orgId = user.getOrgId();
		CalendarResp calendarResp = spaceService.getCalendarIofoById(tenantId, user.getOrgId(),id);
		return CommonResponse.success(calendarResp);
	}

	@RequestMapping("/findCalendarList")
	@ResponseBody
	public CommonResponse<Page<CalendarResp>> findCalendarList(HttpServletRequest request,
			@RequestParam("pageNum") String pageNum, @RequestParam("pageSize") String pageSize,
			@RequestParam(value = "name", required = false) String name) {
		try {
			Page<CalendarResp> page = spaceService.findCalendarList(pageNum, pageSize, name);
			return CommonResponse.success(page);
		} catch (BusinessException e) {
			throw e;
		}
	}

	/**
	 * 
	 * 描述：添加告警信息
	 * 
	 * @author zhouzongwei
	 * @created 2017年11月30日 下午3:20:37
	 * @since
	 * @param request
	 * @param warning
	 * @return
	 */
    @PermissionAnnotation(value = "WARNING")
	@SystemLogAnnotation(value = "添加告警信息")
	@RequestMapping("/addWarning")
	@ResponseBody
	public CommonResponse<ResultMsg> addWarning(HttpServletRequest request, WarningReq warning) {
		try {
			//LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			//warning.setTenantId(String.valueOf(user.getTenantId()));
			spaceService.addWarning(warning);
			return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
		} catch (BusinessException e) {
			throw e;
		}
	}

	/**
	 * 
	 * 描述：查询历史告警数据
	 * 
	 * @author zhouzongwei
	 * @created 2017年11月30日 下午4:12:24
	 * @since
	 * @param request
	 * @return
	 */
	@RequestMapping("/findHistoryWarningList")
	@ResponseBody
	public CommonResponse<Page<WarningResp>> findHistoryWarningList(HttpServletRequest request, String pageNum,
			String pageSize, @RequestParam(value = "eventType", required = false) String eventType,
			@RequestParam(value = "timeType", required = false) String timeType) {
		try {
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			Long tenantId=user.getTenantId();
			Long orgId=user.getOrgId();
			Long locationId=user.getLocationId();
			Page<WarningResp> page = spaceService.findHistoryWarningList(pageNum, pageSize, eventType, timeType,
					tenantId,orgId,locationId);
			return CommonResponse.success(page);
		} catch (BusinessException e) {
			throw e;
		}
	}

	@RequestMapping("/findHistoryWarningListNoPage")
	@ResponseBody
	public CommonResponse<List<WarningResp>> findHistoryWarningListNoPage(HttpServletRequest request, String eventType,
			String count) {
		try {
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			Long tenantId=user.getTenantId();
			Long orgId=user.getOrgId();
			Long locationId=user.getLocationId();
			List<WarningResp> list = spaceService.findHistoryWarningListNoPage(eventType, count,tenantId,orgId,locationId);
			return CommonResponse.success(list);
		} catch (BusinessException e) {
			throw e;
		}
	}

	/**
	 * 
	 * 描述：查询未读告警数据 TODO 告警
	 * 
	 * @since
	 * @param request
	 * @return
	 */
	@RequestMapping("/findUnreadWarningList")
	@ResponseBody
	public CommonResponse<List<String>> findUnreadWarningList(HttpServletRequest request) {
		try {
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			Long tenantId=user.getTenantId();
			Long orgId=user.getOrgId();
			Long locationId=user.getLocationId();
			List<String> warningLsit = spaceService.findUnreadWarningList(tenantId,orgId,locationId);
			return CommonResponse.success(warningLsit);
		} catch (BusinessException e) {
			throw e;
		}
	}

	/**
	 * 
	 * 描述：更新告警消息状态 TODO 告警
	 * 
	 * @since
	 * @param request
	 * @return
	 */
    @PermissionAnnotation(value = "WARNING")
	@SystemLogAnnotation(value = "更新告警消息状态")
	@RequestMapping("/updateWarningStatus")
	@ResponseBody
	public CommonResponse<ResultMsg> updateWarningStatus(HttpServletRequest request, Long id, String status) {
		try {
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			WarningReq warning = new WarningReq();
			warning.setId(id);
			warning.setStatus(status);
			warning.setTenantId(user.getTenantId());
			warning.setOrgId(user.getOrgId());
			spaceService.updateWarningStatus(warning);
			return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
		} catch (BusinessException e) {
			throw e;
		}
	}

	/**
	 * 同步空间状态信息
	 */
	@SystemLogAnnotation(value = "同步空间状态信息")
	@ResponseBody
	@RequestMapping(value = "/syncSpaceStatus")
	public void syncSpaceStatus(String deviceId) {
		spaceService.syncSpaceStatus(deviceId);
	}

	/**
	 * 同步空间状态信息
	 */
	@ResponseBody
	@RequestMapping(value = "/getReservation")
	public CommonResponse<ReservationResp> getReservation(Long spaceId, Long currentDate) {
		ReservationResp resp = spaceService.getReservation(spaceId, currentDate);
		return CommonResponse.success(resp);
	}

	private static String GET_OAUTH_CODE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=%s&scope=%s&state=%s#wechat_redirect";

	// 网页授权OAuth2.0获取code
	public static String getOAuthCodeUrl(String appId, String redirectUrl, String scope, String state) {
		return String.format(GET_OAUTH_CODE, new String[] { appId, urlEncodeUTF8(redirectUrl), "code", scope, state });
	}

	public static String urlEncodeUTF8(String str) {
		String result = str;
		try {
			result = URLEncoder.encode(str, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 描述：下载房间控制二维码
	 * 
	 * @author chenxiaolin
	 * @created 2018年2月26日 下午5:54:23
	 * @param req
	 * @param res
	 * @param spaceId
	 */
	@ResponseBody
	@RequestMapping("/confirmReservationAdd")
	public CommonResponse<String> confirmReservationAdd(HttpServletRequest req, HttpServletResponse res, Long spaceId,
			String randomStr) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		Long tenantId = user.getTenantId();
		Long locationId = user.getLocationId();
		// String userUUID=user.getUserUuid();
		// FetchUserResp userInfo=userApi.getUserByUuid(userUUID);
		String mqttUUID = constantUtil.getAdminUuid();
		String mqttPwd = constantUtil.getAdminPwd();
		String host = environmentPropertyApi.getPropertyValue(Constants.MEETING_HOST) + "?spaceId=" + spaceId
				+ "&locationId=" + locationId + "&tenantId=" + tenantId + "&uuid=" + mqttUUID + "&randomStr="
				+ randomStr + "&mqttPwd=" + mqttPwd;
		// String host=getOAuthCodeUrl("wxd2a8841d92ccdc5d", redirect_url,
		// "snsapi_base", "");
		String base64Data = QrCodeUtil.qrCodeToString(host, "");
		String result = "data:image/jpg;base64," + base64Data;
		return CommonResponse.success(result);
	}

	// @ResponseBody
	// @RequestMapping("/confirmMeetingCode")
	// public CommonResponse<String> confirmMeetingCode(HttpServletRequest req,
	// HttpServletResponse res,Long spaceId,Long reservationId) {
	// LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
	// Long tenanId=user.getLocationId();
	// Long locationId=user.getLocationId();
	// String userUUID=user.getUserUuid();
	// FetchUserResp userInfo=userApi.getUserByUuid(userUUID);
	// String host =
	// environmentPropertyApi.getPropertyValue(CenterControlConstants.CONFIRM_MEETING_HOST)
	// + "?spaceId=" + spaceId+
	// "&locationId="+locationId+"&tenanId="+tenanId+"&uuid="+userUUID+
	// "&reservationId=" + reservationId+"&mqttPwd="+userInfo.getMqttPassword();
	// String base64Data = QrCodeUtil.qrCodeToString(host, "");
	// String result = "data:image/jpg;base64," + base64Data;
	// return CommonResponse.success(result);
	// }

	@ResponseBody
	@RequestMapping("/startMeetingCode")
	public CommonResponse<String> startMeetingCode(HttpServletRequest req, HttpServletResponse res, Long spaceId,
			Long currentDate) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		Long tenantId = user.getTenantId();
		Long locationId = user.getLocationId();
		// String userUUID=user.getUserUuid();
		// FetchUserResp userInfo=userApi.getUserByUuid(userUUID);
		String mqttUUID = constantUtil.getAdminUuid();
		String mqttPwd = constantUtil.getAdminPwd();
		String result = null;
		ReservationResp resp = spaceService.getStartReservation(spaceId, currentDate);
		if (resp != null && resp.getFlag() == 0) {
			String host = environmentPropertyApi.getPropertyValue(Constants.CONFIRM_MEETING_URL) + "?spaceId="
					+ resp.getSpaceId() + "&locationId=" + locationId + "&tenantId=" + tenantId + "&uuid=" + mqttUUID
					+ "&reservationId=" + resp.getId() + "&mqttPwd=" + mqttPwd;
			String base64Data = QrCodeUtil.qrCodeToString(host, "");
			result = "data:image/jpg;base64," + base64Data;
		}
		return CommonResponse.success(result);
	}

	/**
	 * 空间数据EXCEL导入
	 * 
	 * @return
	 * @throws BusinessException
	 */
    @PermissionAnnotation(value = "SPACE")
	@SystemLogAnnotation(value = "空间数据导入")
	@ApiOperation("空间数据导入")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "multipartRequest", value = "excel文件", required = false, paramType = "query", dataType = "String") })
	@RequestMapping(value = "/spaceDataImport", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse spaceDataImport(MultipartHttpServletRequest multipartRequest) throws BusinessException {
		Boolean flag = false;
		try {
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			Long tenantId = user.getTenantId();
			Long locationId = user.getLocationId();
			Long userId = user.getUserId();
			Long orgId=user.getOrgId();
			return spaceService.spaceDataImport(multipartRequest, tenantId, locationId, userId,orgId);
			// return CommonResponse.success();
			// return new CommonResponse(ResultMsg.FAIL, "用户名重复,请重新提交！");
		} catch (BusinessException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 空间背景图保存
	 * 
	 * @return
	 * @throws BusinessException
	 */
    @PermissionAnnotation(value = "SPACE")
	@SystemLogAnnotation(value = "空间背景图保存")
	@ApiOperation("空间背景图保存")
	@RequestMapping(value = "/spaceBackgroundImgImport", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<String> spaceBackgroundImgImport(Long id, Long spaceId, String bgImg)
			throws BusinessException {
		String fileId = null;
		try {
			SpaceBackgroundImgReq req = new SpaceBackgroundImgReq();
			// 修改图片名称
			if (StringUtils.isNotBlank(bgImg)) {
				bgImg = bgImg.replace(bgImg.split("\\.")[0], spaceId.toString());
			}
			req.setId(id);
			req.setSpaceId(spaceId);
			req.setBgImg(bgImg);
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			Long userId = user.getUserId();
			if (req.getId() == null || req.getId() == 0L) {
				fileId = spaceService.saveSpaceBackgroundImgImport(user.getTenantId(), user.getLocationId(), userId,
						req);
			} else {
				spaceService.updateSpaceBackgroundImg(user.getTenantId(), user.getLocationId(), userId, req);
				fileId = req.getBgImg();
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			throw e;
		}
		return CommonResponse.success(fileId);
	}

	/**
	 * 文件上传
	 * 
	 * @return
	 * @throws BusinessException
	 */
	@SystemLogAnnotation(value = "文件上传")
	@ApiOperation("文件上传")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "multipartRequest", value = "文件", required = false, paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "tenantId", value = "租户Id", required = true, paramType = "query", dataType = "Long"),
			@ApiImplicitParam(name = "spaceId", value = "空间Id", required = false, paramType = "query", dataType = "Long") })
	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<String> fileUpload(MultipartHttpServletRequest multipartRequest,
			@RequestParam("tenantId") Long tenantId, @RequestParam("spaceId") Long spaceId) throws BusinessException {
		String fileId = null;
		if (spaceId == null) {
			spaceId = 0L;
		}
		try {
			fileId = spaceService.fileUpload(multipartRequest, tenantId, spaceId);
		} catch (BusinessException e) {
			e.printStackTrace();
			throw e;
		}
		return CommonResponse.success(fileId);
	}

	@ApiOperation("通过文件在项目中的路径进行下载")
	@RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
	@ResponseBody
	public void downloadFile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "fileName", required = true) String fileName) throws BusinessException {
		InputStream inputStream = null;
		ServletOutputStream servletOutputStream = null;
		try {
			String filename = "udp.txt";
			String path = "templates/udp.txt";
			org.springframework.core.io.Resource resource = resourceLoader.getResource("classpath:" + path);

			response.setContentType("application/octet-stream");
			response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.addHeader("charset", "utf-8");
			response.addHeader("Pragma", "no-cache");
			String encodeName = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString());
			response.setHeader("Content-Disposition",
					"attachment; filename=\"" + encodeName + "\"; filename*=utf-8''" + encodeName);

			inputStream = resource.getInputStream();
			servletOutputStream = response.getOutputStream();
			IOUtils.copy(inputStream, servletOutputStream);
			response.flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (servletOutputStream != null) {
					servletOutputStream.close();
					servletOutputStream = null;
				}
				if (inputStream != null) {
					inputStream.close();
					inputStream = null;
				}
				// 召唤jvm的垃圾回收器
				System.gc();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 根据fileId获取url
	 * 
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation("根据fileId获取url")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "fileId", value = "文件id", required = true, paramType = "query", dataType = "String") })
	@RequestMapping(value = "/getFileUrl", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<String> getFileUrl(@RequestParam(value = "fileId", required = true) String fileId)
			throws BusinessException {
		String fileUrl = null;
		try {
			fileUrl = spaceService.getFileUrl(fileId);
		} catch (BusinessException e) {
			e.printStackTrace();
			throw e;
		}
		return CommonResponse.success(fileUrl);
	}

	/**
	 * 根据spaceId获取背景图片
	 * 
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation("根据spaceId获取背景图片")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "spaceId", value = "空间id", required = true, paramType = "query", dataType = "Long") })
	@RequestMapping(value = "/getSpaceBackgroundImg", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<List<SpaceBackgroundImgResp>> getSpaceBackgroundImg(
			@RequestParam(value = "spaceId", required = true) Long spaceId) throws BusinessException {
		List<SpaceBackgroundImgResp> imgList = null;
		try {
			imgList = spaceService.getSpaceBackgroundImg(spaceId);
		} catch (BusinessException e) {
			e.printStackTrace();
			throw e;
		}
		return CommonResponse.success(imgList);
	}

	/**
	 * 根据ID修改背景图片
	 * 
	 * @return
	 * @throws BusinessException
	 */
	@SystemLogAnnotation(value = "根据ID修改背景图片")
	@ApiOperation("根据ID修改背景图片")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "tenantId", value = "租户Id", required = true, paramType = "query", dataType = "Long"),
			@ApiImplicitParam(name = "locationId", value = "区域Id", required = true, paramType = "query", dataType = "Long") })
	@RequestMapping(value = "/updateSpaceBackgroundImg", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<String> updateSpaceBackgroundImg(
			@RequestParam(value = "tenantId", required = true) Long tenantId,
			@RequestParam(value = "locationId", required = true) Long locationId,
			@RequestBody SpaceBackgroundImgReq req) throws BusinessException {
		try {
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			Long userId = user.getUserId();
			spaceService.updateSpaceBackgroundImg(tenantId, locationId, userId, req);
		} catch (BusinessException e) {
			e.printStackTrace();
			throw e;
		}
		return CommonResponse.success();
	}

	/**
	 * 根据ID删除背景图片
	 * 
	 * @return
	 * @throws BusinessException
	 */
	@SystemLogAnnotation(value = "根据ID删除背景图片")
	@ApiOperation("根据ID删除背景图片")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "id", required = true, paramType = "query", dataType = "Long") })
	@RequestMapping(value = "/deleteSpaceBackgroundImg", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<String> deleteSpaceBackgroundImg(@RequestParam(value = "id", required = true) Long id)
			throws BusinessException {
		try {
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			spaceService.deleteSpaceBackgroundImg(user.getTenantId(),user.getOrgId(), id);
		} catch (BusinessException e) {
			e.printStackTrace();
			throw e;
		}
		return CommonResponse.success();
	}

	/**
	 * 根据ID删除背景图片
	 * 
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation("获取部署方式列表")
	@RequestMapping(value = "/getDeploymentList", method = RequestMethod.GET)
	@ResponseBody
	public CommonResponse<List<DeploymentResp>> getDeploymentList(Long tenantId) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		tenantId = user.getTenantId();
		Long locationId = user.getLocationId();
		return CommonResponse.success(spaceService.getDeplymentList(tenantId, locationId));
	}

	/**
	 * 根据ID删除背景图片
	 * 
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation("获取部署方式列表")
	@RequestMapping(value = "/getDeployPage", method = RequestMethod.GET)
	@ResponseBody
	public CommonResponse<Page<DeploymentResp>> getDeployPage(Integer pageNumber, Integer pageSize, String deployName) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		Long tenantId = user.getTenantId();
		Long locationId = user.getLocationId();
		return CommonResponse
				.success(spaceService.getDeployPage(tenantId, locationId, pageNumber, pageSize, deployName));
	}

	/**
	 * 根据ID删除背景图片
	 * 
	 * @return
	 * @throws BusinessException
	 */
    @PermissionAnnotation(value = "SPACE")
	@ApiOperation("添加或修改部署方式")
	@RequestMapping(value = "/editDeploy", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<List<DeploymentResp>> editDeploy(Long id, String name) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		Long tenantId = user.getTenantId();
		Long locationId = user.getLocationId();
		Long userId = user.getUserId();
		Long orgId = user.getOrgId();
		return spaceService.saveOrUpdateDeploy(tenantId,orgId,locationId, id, name, userId);
		//return CommonResponse.success();
	}

	/**
	 * 根据ID删除背景图片
	 * 
	 * @return
	 * @throws BusinessException
	 */
    @PermissionAnnotation(value = "SPACE")
	@ApiOperation("批量删除部署方式")
	@RequestMapping(value = "/delDeploy", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<List<DeploymentResp>> delDeploy(String ids) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		Long tenantId = user.getTenantId();
		spaceService.deleteBatchDeploy(tenantId, ids);
		return CommonResponse.success();

	}

	/**
	 * 保存施工APP SpaceBackGroundImg
	 * 
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation("施工APP上传背景图片")
	@RequestMapping(value = "/appSaveBgImg", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<String> appSaveBgImg(MultipartHttpServletRequest multipartRequest, Long tenantId,
			Long spaceId, @RequestBody SpaceBackgroundImgReq req) {
		return CommonResponse.success(spaceService.appSaveBgImg(multipartRequest, tenantId, req, spaceId));
	}

	/**
	 * 获取栋的列表信息
	 */
	@ApiOperation("获取栋的列表信息")
	@RequestMapping(value = "/getBuildList")
	@ResponseBody
	public CommonResponse<List<SpaceResp>> getBuildList() {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		Long tenantId = user.getTenantId();
		Long orgId = user.getOrgId();
		return CommonResponse.success(spaceService.getBuildListByTenantId(tenantId,orgId));
	}

	/**
	 * 根据BUILD ID 获取FLOOR的列表信息
	 */
	@ApiOperation("根据BUILD ID 获取FLOOR的列表信息")
	@RequestMapping(value = "/getFloorListBySpaceId")
	@ResponseBody
	public CommonResponse<List<SpaceResp>> getFloorListBySpaceId(
			@RequestParam(value = "buildId", required = true) Long buildId) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		Long tenantId = user.getTenantId();
		return CommonResponse.success(spaceService.getFloorListByBuildId(tenantId, buildId));
	}

	/**
	 * 根据空间获取设备列表
	 */
	@ApiOperation("根据空间获取设备列表")
	@RequestMapping(value = "/getDeviceListBySpace", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<List<Map<String, Object>>> getDeviceListBySpace(@RequestBody SpaceDeviceReq spaceDeviceReq) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		spaceDeviceReq.setTenantId(user.getTenantId());
		spaceDeviceReq.setLocationId(user.getLocationId());
		return CommonResponse.success(spaceService.getDeviceListBySpace(spaceDeviceReq));
	}

	@ApiOperation("设备挂载接口")
	@RequestMapping(value = "/mount", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<ResultMsg> mount(@RequestBody SpaceDeviceReq spaceDeviceReq) {
		try {
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			spaceDeviceReq.setTenantId(user.getTenantId());
			spaceDeviceReq.setLocationId(user.getLocationId());
			spaceService.mount(spaceDeviceReq);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(BusinessExceptionEnum.GATEWAY_UNCONNECT);
		}
		return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
	}

	@ApiOperation("移除设备挂载接口")
	@RequestMapping(value = "/removeMount", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<ResultMsg> removeMount(@RequestBody SpaceDeviceReq spaceDeviceReq) {
		try {
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			spaceDeviceReq.setTenantId(user.getTenantId());
			spaceDeviceReq.setLocationId(user.getLocationId());
			spaceService.removeMount(spaceDeviceReq);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(BusinessExceptionEnum.GATEWAY_UNCONNECT);
		}
		return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
	}

    @PermissionAnnotation(value = "SPACE")
	@ApiOperation("设备替换")
	@RequestMapping(value = "/replaceDevice", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<ResultMsg> replaceDevice(@RequestBody SpaceDeviceReq spaceDeviceReq) {
		try {
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			spaceDeviceReq.setUserId(user.getUserId());
			spaceDeviceReq.setTenantId(user.getTenantId());
			spaceDeviceReq.setLocationId(user.getLocationId());
			spaceService.replaceDevice(spaceDeviceReq);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(BusinessExceptionEnum.GATEWAY_UNCONNECT);
		}
		return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
	}
}
