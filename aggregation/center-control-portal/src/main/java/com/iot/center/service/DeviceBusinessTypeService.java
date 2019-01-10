package com.iot.center.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.building.device.api.DeviceBusinessTypeApi;
import com.iot.building.device.vo.DeviceBusinessTypeReq;
import com.iot.building.device.vo.DeviceBusinessTypeResp;
import com.iot.center.helper.BusinessExceptionEnum;
import com.iot.center.utils.ExcelUtils;
import com.iot.common.exception.BusinessException;
import com.iot.redis.RedisCacheUtil;
import com.iot.user.vo.LoginResp;

@Service
@Transactional
public class DeviceBusinessTypeService {

	@Autowired
	private DeviceBusinessTypeApi deviceBusinessTypeApi;
	
	private final static Logger LOGGER = LoggerFactory.getLogger(DeviceBusinessTypeService.class);

	/**
	 * EXCEL 文件数据上传 并保存数据到数据库中，表device_business_type中
	 * @param multipartRequest
	 * //@param tenantId 租户id
	 * @return
	 */
	public Map<String,Object> fileUpload(MultipartHttpServletRequest multipartRequest, Long tenantId,Long locationId, Long userId) throws BusinessException{
		if ((multipartRequest != null && !multipartRequest.getFileNames().hasNext())) {
			throw new BusinessException(BusinessExceptionEnum.UPLOAD_FILE_EMPTY);
		}
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		Map<String,Object> result=Maps.newHashMap();
		List<String> successList=Lists.newArrayList();
		List<String> errorList=Lists.newArrayList();
		try{
			MultipartFile multipartFile = multipartRequest.getFile(multipartRequest.getFileNames().next());
			List<Map<String, Object>> list = Lists.newArrayList();
            list = ExcelUtils.resolveExcelStr(multipartFile);
            LOGGER.info("获取解析的list数据"+list);
            if (list != null) {
                LOGGER.info("list.size:"+list.size());
                int index = 0;
                for (int i= 0;i<list.size();i++) {
                    // 判断该设备是否存在
                    if(list.get(i).get("0") == null){
						index ++;
						LOGGER.info("第"+(i+1)+"行的设备deviceBusiness不能为空");
						errorList.add("设备deviceBusiness不能为空");
						continue;
					}
					String deviceBusiness = list.get(i).get("0").toString();
					LOGGER.info("获取解析的deviceBusiness"+deviceBusiness);
					DeviceBusinessTypeResp existDeviceBusinessType=deviceBusinessTypeApi.getBusinessTypeIdByType(user.getOrgId(), tenantId, deviceBusiness);
					if(existDeviceBusinessType !=null) {
						errorList.add("设备用途"+deviceBusiness+"已经存在");
						continue;
					}

					if(list.get(i).get("1") == null){
						index ++;
						LOGGER.info("第"+(i+1)+"行的productId不能为空");
						errorList.add(deviceBusiness+"的productId不能为空");
						continue;
					}
                    String productId = list.get(i).get("1").toString();
                    LOGGER.info("获取productId"+productId);

                    DeviceBusinessTypeReq deviceBusinessType=new DeviceBusinessTypeReq();
        			deviceBusinessType.setCreateTime(new Date());
        			deviceBusinessType.setCreateBy(userId);
        			deviceBusinessType.setTenantId(tenantId);
        			deviceBusinessType.setOrgId(user.getOrgId());
        			deviceBusinessType.setProductId(Long.valueOf(productId));
        			deviceBusinessType.setIsHomeShow(null);
	                deviceBusinessType.setBusinessType(deviceBusiness);
            		deviceBusinessTypeApi.saveOrUpdate(deviceBusinessType);
            		successList.add(deviceBusiness);
                }
            }
		}catch(Exception e){
			e.printStackTrace();
        	throw new BusinessException(BusinessExceptionEnum.UPLOAD_FILE_EXCEPTION);
		}
		result.put("success", successList);
		result.put("fail", errorList);
		return result;
	}
}
