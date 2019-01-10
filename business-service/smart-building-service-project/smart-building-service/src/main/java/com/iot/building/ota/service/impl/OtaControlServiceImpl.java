package com.iot.building.ota.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.building.gateway.MultiProtocolGatewayHepler;
import com.iot.building.ota.mapper.OtaControlMapper;
import com.iot.building.ota.service.OtaControlService;
import com.iot.common.helper.Page;
import com.iot.common.util.StringUtil;
import com.iot.device.api.CentralControlDeviceApi;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.api.OTAServiceApi;
import com.iot.device.api.ProductCoreApi;
import com.iot.device.vo.req.DevicePageReq;
import com.iot.device.vo.req.OtaPageReq;
import com.iot.device.vo.req.ProductReq;
import com.iot.device.vo.req.device.ListProductInfoReq;
import com.iot.device.vo.req.device.PageDeviceInfoReq;
import com.iot.device.vo.req.device.UpdateDeviceInfoReq;
import com.iot.device.vo.req.ota.OtaFileInfoReq;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.ListProductRespVo;
import com.iot.device.vo.rsp.ota.OtaFileInfoResp;

/**
 * @Author: linjihuang
 * @Descrpiton:
 * @Date: 10:54 2018/10/12
 * @Modify by:
 */
@Service
public class OtaControlServiceImpl implements OtaControlService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OtaControlServiceImpl.class);
	@Autowired
	private CentralControlDeviceApi centralControlDeviceApi;
	@Autowired
	private DeviceCoreApi deviceCoreApi;
	@Autowired
	private ProductCoreApi productCoreApi;
	@Autowired
	private OTAServiceApi otaServiceApi;
	@Autowired
	private OtaControlMapper otaControlMapper;

	public void updateOtaVersion(Long orgId, String deviceId, Long tenantId, Long locationId) {
		// String downloadPath = environment.getProperty(HTTP_FILE_DOWN_PATH) +
		// fileName;
		// String loadPath = environment.getProperty(LOCAL_FILE_UPLOAD_PATH) + fileName;

		// File file = new File(loadPath);// 获取ota包
		// String md5 = GetBigFileMD5.getMD5(file);// 进行md5加密
		if (StringUtil.isBlank(deviceId)) {
			// List<DeviceResp> directDeviceList = centralControlDeviceApi
			// .findDirectDeviceListByVenderCode(Long.valueOf(tenantId), 0L);// 获取所有直连设备
			DevicePageReq pageReq = new DevicePageReq();
			pageReq.setTenantId(tenantId);
			pageReq.setOrgId(orgId);
			pageReq.setLocationId(locationId);
			List<GetDeviceInfoRespVo> deviceResps = centralControlDeviceApi.selectAllDeviceToCenter(pageReq);
			if (CollectionUtils.isNotEmpty(deviceResps)) {
				for (GetDeviceInfoRespVo deviceResp : deviceResps) {
					try {
						getAndSendOtaFileInfo(deviceResp);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			try {
				String[] devices = deviceId.split(",");
				for (String deviceId_ : devices) {
					GetDeviceInfoRespVo deviceResp = deviceCoreApi.get(deviceId_);
					if (deviceResp != null) {
						getAndSendOtaFileInfo(deviceResp);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void getAndSendOtaFileInfo(GetDeviceInfoRespVo deviceResp) throws Exception {
		String parentId;
		UpdateDeviceInfoReq params = new UpdateDeviceInfoReq();
		params.setTenantId(deviceResp.getTenantId());
		params.setOrgId(deviceResp.getOrgId());
		params.setLocationId(deviceResp.getLocationId());
		params.setUuid(deviceResp.getUuid());
		params.setVersion("upgrading");
		deviceCoreApi.saveOrUpdate(params);
		OtaFileInfoReq otaFileInfoReq = new OtaFileInfoReq();
		otaFileInfoReq.setProductId(deviceResp.getProductId());
		otaFileInfoReq.setTenantId(deviceResp.getTenantId());
		otaFileInfoReq.setOrgId(deviceResp.getOrgId());
		otaFileInfoReq.setLocationId(deviceResp.getLocationId());
		OtaFileInfoResp otaFileInfoResp = otaControlMapper.findOtaFileInfoByProductId(otaFileInfoReq);
		parentId = deviceResp.getParentId() != null ? deviceResp.getParentId() : deviceResp.getUuid();
		if (otaFileInfoResp != null) {
			MultiProtocolGatewayHepler.otaReq(parentId, deviceResp.getUuid(), otaFileInfoResp.getDownloadUrl(),
					otaFileInfoResp.getMd5());
		}
	}

	public void downLoadOtaFile(@RequestBody OtaFileInfoReq otaFileInfoReq) {
		if (otaFileInfoReq.getProductId() == null) {
			ProductReq productReq = new ProductReq();
			if (otaFileInfoReq.getTenantId() != null) {
				productReq.setTenantId(otaFileInfoReq.getTenantId());
				// 现有的设备 相关的产品列表
				OtaPageReq otaPageReq = new OtaPageReq();
//				otaPageReq.setPageNum(1);
//				otaPageReq.setPageSize(9999);
				otaPageReq.setTenantId(otaFileInfoReq.getTenantId());
				otaPageReq.setOrgId(otaFileInfoReq.getOrgId());
				otaPageReq.setLocationId(otaFileInfoReq.getLocationId());
//				Pagination pagination = new Pagination(otaPageReq.getPageNum(), otaPageReq.getPageSize());
				// TODO 是否分页
				List<OtaFileInfoResp> otaFileInfoResps = otaControlMapper.getOtaFileList(otaPageReq);
				if (otaFileInfoResps != null && CollectionUtils.isNotEmpty(otaFileInfoResps)) {
					for (OtaFileInfoResp otaFileInfoResp : otaFileInfoResps) {
						otaFileInfoReq.setProductId(otaFileInfoResp.getProductId());
						getUrlAndDownLoad(otaFileInfoReq);
					}
				}
			}
		} else {
			getUrlAndDownLoad(otaFileInfoReq);
		}

	}

	private void insertOrUpdateOtaFileInfo(OtaFileInfoReq otaFileInfoReq) {
		// 查找该产品是否已存在OTA信息
		OtaFileInfoResp otaFileInfoResp = otaControlMapper.findOtaFileInfoByProductId(otaFileInfoReq);
		if (otaFileInfoResp != null) {
			otaFileInfoReq.setId(otaFileInfoResp.getId());
			otaFileInfoReq.setUpdateTime(new Date());
			otaControlMapper.updateOtaFileInfo(otaFileInfoReq);
		} else {
			otaFileInfoReq.setCreateTime(new Date());
			otaFileInfoReq.setUpdateTime(new Date());
			otaFileInfoReq.setCreateBy(otaFileInfoReq.getUpdateBy());
			otaControlMapper.saveOtaFileInfo(otaFileInfoReq);
		}
	}

	private void getUrlAndDownLoad(OtaFileInfoReq otaFileInfoReq) {
		// 获取云端 OTA包下载URL
		Map<String, String> params = otaServiceApi.getFirmwareUrl(otaFileInfoReq.getProductId(),
				otaFileInfoReq.getVersion());
		if (MapUtils.isNotEmpty(params)) {
			String url = params.get("url");
			String md5 = params.get("md5");
			// String fileName = otaFileInfoReq.getProductId() + ".zip";
			// path = path/ota/tenantId/productId.zip
			// String path = environment.getProperty(LOCAL_FILE_UPLOAD_PATH) + "ota" +
			// File.separator + otaFileInfoReq.getTenantId();
			// try {
			// // 判断文件是否存在 , 如果存在就删除旧文件
			// File file = new File(path + File.separator + otaFileInfoReq.getProductId() +
			// ".zip");
			// if (file.exists()) {
			// file.delete();
			// }
			// FileUtil.downLoadFromUrl(url, fileName, path);
			otaFileInfoReq.setDownloadUrl(url);
			otaFileInfoReq.setMd5(md5);
			// 添加或更新数据库 ota_file_info 表数据
			insertOrUpdateOtaFileInfo(otaFileInfoReq);
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
		}
	}

	@Override
	public Page<OtaFileInfoResp> getOtaFileList(OtaPageReq pageReq) {
		Page<OtaFileInfoResp> pageResult = new Page<OtaFileInfoResp>();
		List<OtaFileInfoResp> otaFileInfoResps = otaControlMapper.getOtaFileList(pageReq);
		List<OtaFileInfoResp> results = Lists.newArrayList();
		List<Long> productIds = Lists.newArrayList();
		Map<Long, Object> versionMap = Maps.newHashMap();
		for (OtaFileInfoResp otaFileInfoResp : otaFileInfoResps) {
			productIds.add(otaFileInfoResp.getProductId());
			if (otaFileInfoResp.getVersion() != null) {
				versionMap.put(otaFileInfoResp.getProductId(), otaFileInfoResp.getVersion());
			}
		}
		PageDeviceInfoReq params = new PageDeviceInfoReq();
		params.setProductIds(productIds);
		params.setTenantId(pageReq.getTenantId());
		params.setOrgId(pageReq.getOrgId());
		params.setLocationId(pageReq.getLocationId());
		productIds = centralControlDeviceApi.getExistProductList(params);
		ListProductInfoReq listProductInfoReq = new ListProductInfoReq();
		listProductInfoReq.setProductIds(productIds);
		List<ListProductRespVo> listProductRespVos = productCoreApi.listProducts(listProductInfoReq);
		if (CollectionUtils.isNotEmpty(listProductRespVos)) {
			int num = (pageReq.getPageNum() - 1) * pageReq.getPageSize();
			for (int i = num; i < listProductRespVos.size(); i++) {
				ListProductRespVo listProductRespVo = listProductRespVos.get(i);
				OtaFileInfoResp otaFileInfoResp = new OtaFileInfoResp();
				otaFileInfoResp.setProductId(listProductRespVo.getId());
				otaFileInfoResp.setProductName(listProductRespVo.getProductName());
				otaFileInfoResp.setModel(listProductRespVo.getModel());
				if (versionMap.get(listProductRespVo.getId()) != null) {
					otaFileInfoResp.setVersion(versionMap.get(listProductRespVo.getId()).toString());
				}
				results.add(otaFileInfoResp);
			}
		}
		pageResult.setPageNum(pageReq.getPageNum());
		pageResult.setPageSize(pageReq.getPageSize());
		pageResult.setTotal(listProductRespVos.size());
		pageResult.setPages((listProductRespVos.size()/pageReq.getPageSize())+1);
		pageResult.setResult(results);
		return pageResult;
	}

	@Override
	public int saveOtaFileInfo(OtaFileInfoReq otaFileInfoReq) {
		return otaControlMapper.saveOtaFileInfo(otaFileInfoReq);
	}

	@Override
	public int updateOtaFileInfo(OtaFileInfoReq otaFileInfoReq) {
		return otaControlMapper.updateOtaFileInfo(otaFileInfoReq);
	}

	@Override
	public OtaFileInfoResp findOtaFileInfoByProductId(OtaFileInfoReq otaFileInfoReq) {
		return otaControlMapper.findOtaFileInfoByProductId(otaFileInfoReq);
	}

}
