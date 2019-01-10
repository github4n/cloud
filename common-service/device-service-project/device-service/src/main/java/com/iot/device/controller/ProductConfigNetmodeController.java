package com.iot.device.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.google.common.collect.Lists;
import com.iot.common.constant.SystemConstants;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ExceptionEnum;
import com.iot.common.helper.Page;
import com.iot.device.api.ProductApi;
import com.iot.device.api.ProductConfigNetmodeApi;
import com.iot.device.business.DeviceTypeBusinessService;
import com.iot.device.business.ProductBusinessService;
import com.iot.device.core.service.DeviceTypeServiceCoreUtils;
import com.iot.device.core.service.ProductServiceCoreUtils;
import com.iot.device.core.utils.BeanCopyUtils;
import com.iot.device.exception.DataPointExceptionEnum;
import com.iot.device.exception.DevelopInfoEnum;
import com.iot.device.exception.DeviceTypeExceptionEnum;
import com.iot.device.exception.ProductExceptionEnum;
import com.iot.device.model.*;
import com.iot.device.model.enums.DevelopStatusEnum;
import com.iot.device.service.*;
import com.iot.device.service.core.ModuleCoreService;
import com.iot.device.vo.req.*;
import com.iot.device.vo.req.DataPointReq.SmartWraper;
import com.iot.device.vo.req.ota.ProductOtaReq;
import com.iot.device.vo.req.product.ProductAuditPageReq;
import com.iot.device.vo.req.product.ProductConfirmReleaseReq;
import com.iot.device.vo.req.product.ReopenAuditReq;
import com.iot.device.vo.req.servicereview.ServiceAuditPageReq;
import com.iot.device.vo.rsp.*;
import com.iot.device.vo.rsp.product.ProductAuditListResp;
import com.iot.device.vo.rsp.product.ProductMinimumSubsetResp;
import com.iot.device.vo.rsp.servicereview.ServiceAuditListResp;
import com.iot.file.api.FileApi;
import com.iot.file.dto.FileDto;
import com.iot.saas.SaaSContextHolder;
import com.iot.util.AssertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 前端控制器
 *
 * @author lucky
 * @since 2018-04-12
 */
@RestController
public class ProductConfigNetmodeController implements ProductConfigNetmodeApi {

	public static final Logger LOGGER = LoggerFactory.getLogger(ProductConfigNetmodeController.class);

	@Autowired
	private IProductConfigNetmodeService iProductConfigNetmodeService;

	@Override
	public Long insert(@RequestBody ProductConfigNetmodeReq productConfigNetmodeReq) {
		return iProductConfigNetmodeService.insert(productConfigNetmodeReq);
	}


	@Override
	public void insertMore(@RequestBody List<ProductConfigNetmodeReq> productConfigNetmodeReq) {
		iProductConfigNetmodeService.insertMore(productConfigNetmodeReq);
	}

	@Override
	public List<ProductConfigNetmodeRsp> listByProductId(@RequestParam("productId") Long productId) {
		return iProductConfigNetmodeService.listByProductId(productId);
	}

	@Override
	public void deleteMore(@RequestBody List ids) {
		iProductConfigNetmodeService.deleteMore(ids);
	}
}
