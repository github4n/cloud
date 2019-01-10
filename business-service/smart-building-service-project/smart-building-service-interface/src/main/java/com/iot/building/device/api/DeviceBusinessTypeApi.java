package com.iot.building.device.api;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iot.building.device.api.fallback.DeviceBusinessTypeApiFallbackFactory;
import com.iot.building.device.vo.BusinessTypeStatistic;
import com.iot.building.device.vo.DeviceBusinessTypeReq;
import com.iot.building.device.vo.DeviceBusinessTypeResp;
import com.iot.common.helper.Page;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Author: ljh
 * @Descrpiton: 设备业务类型接口
 * @Date: 11:12 2018/5/16
 * @Modify by:
 */
@Api(tags = "设备业务类型接口")
@FeignClient(value = "building-control-service", fallbackFactory = DeviceBusinessTypeApiFallbackFactory.class,configuration = DeviceBusinessTypeApi.MultipartSupportConfig.class)
@RequestMapping("/DeviceBusinessTypeResp")
public interface DeviceBusinessTypeApi {
	
	@ApiOperation(value = "获取业务类型ID")
    @RequestMapping(value = "/getBusinessTypeIdByType", method = RequestMethod.GET)
    DeviceBusinessTypeResp getBusinessTypeIdByType(@RequestParam("orgId") Long orgId, @RequestParam("tenantId") Long tenantId, @RequestParam(value = "businessType") String businessType);
	
	@ApiOperation(value = "获取业务类型ID")
	@RequestMapping(value = "/findById", method = RequestMethod.GET)
	DeviceBusinessTypeResp findById(@RequestParam("orgId") Long orgId, @RequestParam("tenantId") Long tenantId, @RequestParam("id") Long id);
	
	@ApiOperation(value = "删除业务类型ID")
	@RequestMapping(value = "/delBusinessTypeById", method = RequestMethod.GET)
	void delBusinessTypeById(@RequestParam("id") Long id);
	
	@ApiOperation(value = "获取业务类型列表")
    @RequestMapping(value = "/getBusinessTypeList", method = RequestMethod.GET)
	Page<DeviceBusinessTypeResp> getBusinessTypeList(@RequestParam("orgId") Long orgId, @RequestParam(value = "name") String name, @RequestParam(value = "tenantId") Long tenantId, @RequestParam(value = "model") String model, @RequestParam(value = "pageNumber") int pageNumber, @RequestParam(value = "pageSize")  int pageSize);

	@ApiOperation(value = "获取业务类型列表")
	@RequestMapping(value = "/getBusinessTypeListStr", method = RequestMethod.GET)
	List<DeviceBusinessTypeResp> getBusinessTypeList(@RequestParam("orgId") Long orgId, @RequestParam(value = "tenantId") Long tenantId, @RequestParam(value = "model") String model);

	@ApiOperation(value = "保存或修改业务类型")
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	void saveOrUpdate(@RequestBody DeviceBusinessTypeReq DeviceBusinessTypeReq);

	@ApiOperation(value = "获取业务类型和产品的关系")
	@RequestMapping(value = "/businessTypeWithProduct",method = RequestMethod.GET)
    List<DeviceBusinessTypeResp> businessTypeWithProduct(@RequestParam("orgId") Long orgId, @RequestParam("tenantId") Long tenantId);

	@ApiOperation(value = "获取业务类型")
	@RequestMapping(value = "/findBusinessTypeList",method = RequestMethod.GET)
	List<DeviceBusinessTypeResp> findBusinessTypeList(@RequestParam("orgId") Long orgId, @RequestParam(value = "tenantId")Long tenantId,@RequestParam(value = "businessType",required = false)String businessType);

	@ApiOperation(value = "根据遥控器未使用的业务类型列表")
	@RequestMapping(value = "/listDeviceRemoteBusinessType",method = RequestMethod.GET)
	List<DeviceBusinessTypeResp> listDeviceRemoteBusinessType(@RequestParam("orgId") Long orgId, @RequestParam(value = "tenantId",required = false) Long tenantId,@RequestParam(value = "id",required = false) Long id);

	@ApiOperation(value = "保存excel的数据")
	@RequestMapping(value = "/businessTypeDataImport",method = RequestMethod.GET)
	Boolean businessTypeDataImport(@RequestParam("orgId") Long orgId, @RequestParam(value = "list")List<String[]> list, @RequestParam(value = "tenantId")Long tenantId, @RequestParam(value = "userId")Long userId);

	@ApiOperation(value = "根据属性获取业务类型列表")
	@RequestMapping(value = "/getBusinessListByDescription",method = RequestMethod.GET)
	List<String> getBusinessListByDescription(@RequestParam("orgId") Long orgId, @RequestParam(value = "description")String description, @RequestParam(value = "tenantId")Long tenantId);

	@ApiOperation(value = "务类型列查询区域统计数据")
	@RequestMapping(value = "/findStatistic",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	List<BusinessTypeStatistic> findStatistic(@RequestBody BusinessTypeStatistic statistic);
    
	@ApiOperation(value = "统计设备用途的数据")
	@RequestMapping(value = "/initStatistic",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	void initStatistic();
	
	@ApiOperation(value = "获取业务类型列表")
	@RequestMapping(value = "/findByCondition", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	List<DeviceBusinessTypeResp> findByCondition(@RequestBody DeviceBusinessTypeReq req);

    
	/**
     * @despriction：feign文件上传配置
     * @author  yeshiyuan
     * @created 2018/5/8 14:25
     * @return
     */
    class MultipartSupportConfig {
        @Bean
        public Encoder feignFormEncoder() {
            return new SpringFormEncoder();
        }
    }

}
