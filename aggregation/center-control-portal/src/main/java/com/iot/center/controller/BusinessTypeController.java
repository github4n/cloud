package com.iot.center.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.common.collect.Maps;
import com.iot.building.device.api.DeviceBusinessTypeApi;
import com.iot.building.device.vo.DeviceBusinessTypeReq;
import com.iot.building.device.vo.DeviceBusinessTypeResp;
import com.iot.center.annotation.PermissionAnnotation;
import com.iot.center.annotation.SystemLogAnnotation;
import com.iot.center.helper.BusinessExceptionEnum;
import com.iot.center.service.DeviceBusinessTypeService;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.common.helper.Page;
import com.iot.device.api.ProductCoreApi;
import com.iot.device.vo.req.ProductReq;
import com.iot.device.vo.rsp.device.ListProductRespVo;
import com.iot.user.vo.LoginResp;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/businessType")
public class BusinessTypeController {
	
	@Autowired
	private DeviceBusinessTypeApi deviceBusinessTypeApi;

	@Autowired
	private DeviceBusinessTypeService deviceBusinessTypeService;

	@Autowired
	private ProductCoreApi productApi;
	
	private static String CONTROL_PROPERTY="1";//控制属性
	private static String NOT_CONTROL_PROPERTY="0";//一般属性
	private static final Logger log = LoggerFactory.getLogger(BusinessTypeController.class);

	/**
	 * 业务类型和产品的关系
	 * @return
	 */
	@SystemLogAnnotation(value = "设备用途列表")
	@ResponseBody
	@RequestMapping(value = "/businessTypeWithProduct", method = {RequestMethod.GET })
	public CommonResponse<List<DeviceBusinessTypeResp>> businessTypeWithProduct() {
		/*List<DeviceBusinessType> list=deviceBusinessTypeApi.businessTypeWithProduct();
		return CommonResponse.success(list);*/
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		CommonResponse<List<DeviceBusinessTypeResp>> result = new CommonResponse<>(ResultMsg.SUCCESS, deviceBusinessTypeApi.businessTypeWithProduct(user.getOrgId(), user.getTenantId()));
		return  result;
	}

	/**
	 * 通过租户id获取产品列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findProductListByTenantId", method = {RequestMethod.GET })
	public CommonResponse<List<ListProductRespVo>> findProductListByTenantId() {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		ProductReq productReq = new ProductReq();
		productReq.setTenantId(user.getTenantId());
		CommonResponse<List<ListProductRespVo>> result = new CommonResponse<>(ResultMsg.SUCCESS, productApi.listProductAll());
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/findById", method = {RequestMethod.GET })
	public CommonResponse<DeviceBusinessTypeResp> findById(Long id) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		DeviceBusinessTypeResp deviceBusinessType=deviceBusinessTypeApi.findById(user.getOrgId(), user.getTenantId(), id);
		return CommonResponse.success(deviceBusinessType);
	}

	@PermissionAnnotation(value = "DEVICE_PURPOSE")
	@SystemLogAnnotation(value = "添加/修改业务类型")
	@ResponseBody
	@RequestMapping(value = "/addOrUpdate", method = { RequestMethod.POST, RequestMethod.GET })
	public CommonResponse<String> addBussinessType(Long id,String name,Long productId) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		DeviceBusinessTypeReq deviceBusinessType=new DeviceBusinessTypeReq();
		String desc=CONTROL_PROPERTY;
		if(id !=null) {
			deviceBusinessType.setId(id);
			deviceBusinessType.setUpdateTime(new Date());
			deviceBusinessType.setUpdateBy(user.getUserId());
			deviceBusinessType.setProductId(productId);
			deviceBusinessType.setIsHomeShow(null);
		}else {
			deviceBusinessType.setCreateTime(new Date());
			deviceBusinessType.setCreateBy(user.getUserId());
			deviceBusinessType.setTenantId(user.getTenantId());
			deviceBusinessType.setOrgId(user.getOrgId());
			deviceBusinessType.setProductId(productId);
			deviceBusinessType.setIsHomeShow(null);
		}
//		if(flag==0) {
//			desc=NOT_CONTROL_PROPERTY;
//		}
		deviceBusinessType.setBusinessType(name);
		deviceBusinessType.setDescription(desc);
		DeviceBusinessTypeResp existDeviceBusinessType=deviceBusinessTypeApi.getBusinessTypeIdByType(user.getOrgId(), user.getTenantId(), name);
		if(existDeviceBusinessType ==null || (id !=null && existDeviceBusinessType.getId().compareTo(id)==0)) {
			deviceBusinessTypeApi.saveOrUpdate(deviceBusinessType);
			return CommonResponse.success();
		}else {
			throw new BusinessException(BusinessExceptionEnum.BUSINESS_TYPE_IS_EXIST);
		}
	}

	@PermissionAnnotation(value = "DEVICE_PURPOSE")
	@SystemLogAnnotation(value = "删除业务类型")
	@ResponseBody
	@RequestMapping(value = "/del", method = { RequestMethod.POST, RequestMethod.GET })
	public CommonResponse<String> delBussinessType(Long id) {
		deviceBusinessTypeApi.delBusinessTypeById(id);
		return CommonResponse.success();
	}

	@PermissionAnnotation(value = "DEVICE_PURPOSE")
	@SystemLogAnnotation(value = "批量删除业务类型")
	@ResponseBody
	@RequestMapping(value = "/delBatch", method = { RequestMethod.POST, RequestMethod.GET })
	public CommonResponse<String> delBatchBussinessType(String ids) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		if(StringUtils.isNotBlank(ids)) {
			String[] idArry=ids.split(",");
			for(String idStr:idArry) {
				Long id=Long.valueOf(idStr);
				deviceBusinessTypeApi.delBusinessTypeById(id);
			}
		}
		return CommonResponse.success();
	}

	@PermissionAnnotation(value = "DEVICE_PURPOSE")
	@ResponseBody
	@RequestMapping(value = "/list", method = {RequestMethod.GET })
	public CommonResponse<Page<DeviceBusinessTypeResp>> list(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "pageNumber") int pageNumber,@RequestParam(value = "pageSize")  int pageSize) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		Long tenantId=user.getTenantId();
		Page<DeviceBusinessTypeResp> page=deviceBusinessTypeApi.getBusinessTypeList(user.getOrgId(), name,tenantId,"",pageNumber, pageSize);
		return CommonResponse.success(page);
	}

	/**
	 * EXCEL 文件数据上传 并保存数据到数据库中，表device_business_type中
	 * @param multipartRequest
	 * @return
	 * @throws BusinessException
	 */
	@PermissionAnnotation(value = "DEVICE_PURPOSE")
	@SystemLogAnnotation(value = "EXCEL文件数据上传")
	@ApiOperation("EXCEL文件数据上传")
	@ApiImplicitParams({@ApiImplicitParam(name = "multipartRequest", value = "文件", required = false, paramType = "query", dataType = "String")})
	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<Map<String,Object>> fileUpload(MultipartHttpServletRequest multipartRequest)throws BusinessException {
			Boolean flag = false;Map<String,Object> result=Maps.newHashMap();
			try {
				LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
				Long tenantId = user.getTenantId();
				Long userId = user.getUserId();
				result= deviceBusinessTypeService.fileUpload(multipartRequest, tenantId,user.getLocationId(), userId);
			} catch (BusinessException e) {
				e.printStackTrace();
				throw e;
			}
			return CommonResponse.success(result);
	}

	/**
	 * EXCEL 文件下载根据fileId获取url
	 * @param fileId 文件id
	 * @return
	 * @throws BusinessException
	 */
	@PermissionAnnotation(value = "DEVICE_PURPOSE")
	@ApiOperation("EXCEL 文件下载根据fileId获取url")
	@ApiImplicitParams({@ApiImplicitParam(name = "fileId", value = "文件id", required = true, paramType = "query", dataType = "String")})
	@RequestMapping(value = "/getFileUrl", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<String> getFileUrl(@RequestParam(value = "fileId", required = true) String fileId)throws BusinessException {
		String fileUrl = null;
		try {
//			fileUrl = deviceBusinessTypeService.getFileUrl(fileId);
		} catch (BusinessException e) {
			e.printStackTrace();
			throw e;
		}
		return CommonResponse.success(fileUrl);
	}

}
