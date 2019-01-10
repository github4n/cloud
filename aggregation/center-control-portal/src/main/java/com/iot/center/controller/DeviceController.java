package com.iot.center.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.iot.building.device.api.DeviceTobApi;
import com.iot.common.util.StringUtil;

import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.airswitch.api.AirSwitchApi;
import com.iot.building.device.api.DeviceBusinessTypeApi;
import com.iot.building.device.vo.DeviceBusinessTypeResp;
import com.iot.building.device.vo.DeviceDetailVo;
import com.iot.building.ota.api.OtaControlApi;
import com.iot.center.annotation.PermissionAnnotation;
import com.iot.center.annotation.SystemLogAnnotation;
import com.iot.center.helper.BusinessExceptionEnum;
import com.iot.center.helper.Constants;
import com.iot.center.service.DeviceService;
import com.iot.center.service.SpaceService;
import com.iot.center.utils.ExcelUtils;
import com.iot.center.utils.MultiProtocolUDPClient;
import com.iot.center.vo.AirConditionVO;
import com.iot.center.vo.DeviceVO;
import com.iot.common.beans.BeanUtil;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.common.helper.Page;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.device.api.CentralControlDeviceApi;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.vo.req.CommDeviceInfoReq;
import com.iot.device.vo.req.DevicePageReq;
import com.iot.device.vo.req.DeviceReq;
import com.iot.device.vo.req.OtaPageReq;
import com.iot.device.vo.req.device.PageDeviceTypeByParamsReq;
import com.iot.device.vo.req.device.UpdateDeviceInfoReq;
import com.iot.device.vo.req.ota.OtaFileInfoReq;
import com.iot.device.vo.req.ota.UpgradePlanReq;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceTypeRespVo;
import com.iot.device.vo.rsp.ota.OtaFileInfoResp;
import com.iot.device.vo.rsp.ota.UpgradePlanResp;
import com.iot.redis.RedisCacheUtil;
import com.iot.user.vo.LoginResp;
import com.iot.util.ToolUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "设备接口")
@Controller
@RequestMapping("/device")
public class DeviceController {

    private final static Logger LOGGER = LoggerFactory.getLogger(DeviceController.class);
    @Autowired
    DeviceService deviceService;
    @Autowired
    OtaControlApi otaControlApi;
    @Autowired
    private DeviceTobApi deviceTobApi;
    @Autowired
    SpaceService spaceService;
    @Autowired
    DeviceBusinessTypeApi deviceBusinessTypeApi;
    @Autowired
    DeviceCoreApi deviceApi;
    @Autowired
    CentralControlDeviceApi centralControlDeviceApi;
    
    /**
     * 描述：查询直连设备列表
     *
     * @param request
     * @return
     * @author zhouzongwei
     * @created 2017年11月27日 下午2:20:36
     * @since
     */
    @RequestMapping("/addDevicePer")
    @ResponseBody
    public CommonResponse<String> addDevicePer(HttpServletRequest request, String deviceId, String mqttPwd) {
        try {
            String key = deviceId + ":pwd";
            Map<String, String> map = new HashMap<>();
            map.put("dev", DigestUtils.sha256Hex(mqttPwd));
            RedisCacheUtil.hashPutAll(key, map, false);
            return CommonResponse.success();
        } catch (BusinessException e) {
            throw e;
        }
    }

    /**
     * 描述：查询直连设备列表
     *
     * @param request
     * @return
     * @author zhouzongwei
     * @created 2017年11月27日 下午2:20:36
     * @since
     */

    @PermissionAnnotation(value = "DEVICE_LIST,OTA_MANAGER")
    @SystemLogAnnotation(value = "查询设备列表")
    @RequestMapping("/findDirectDeviceList")
    @ResponseBody
    public CommonResponse<Page<DeviceVO>> findDirectDeviceList(HttpServletRequest request, DevicePageReq pageReq) {
        try {
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            pageReq.setTenantId(user.getTenantId());
            pageReq.setLocationId(user.getLocationId());
            pageReq.setOrgId(user.getOrgId());
            Page<DeviceResp> page = deviceService.findDirectDevicePageToCenter(pageReq);
            Page<DeviceVO> pageVO = new Page<>();
            BeanUtil.copyProperties(page, pageVO);
            List<DeviceVO> voList = new ArrayList<DeviceVO>();
            changeResultPage(page, pageVO, voList);
            return CommonResponse.success(pageVO);
        } catch (BusinessException e) {
            throw e;
        }
    }

    private void changeResultPage(Page<DeviceResp> page, Page<DeviceVO> pageVO, List<DeviceVO> voList) {
        if (!page.getResult().isEmpty()) {
            for (DeviceResp deviceResp : (List<DeviceResp>) page.getResult()) {
                DeviceVO deviceVO = new DeviceVO();
                changeDeviceRespToDeviceVO(deviceResp, deviceVO);
                voList.add(deviceVO);
            }
        }
        pageVO.setResult(voList);
    }

    /**
     * 描述：查询非直连设备列表
     *
     * @param request
     * @return
     * @author zhouzongwei
     * @created 2017年11月27日 下午3:02:59
     * @since
     */
    @SystemLogAnnotation(value = "查询非直连设备列表")
    @RequestMapping("/findUnDirectDeviceList")
    @ResponseBody
    public CommonResponse<Page<DeviceVO>> findUnDirectDevicePage(HttpServletRequest request, DevicePageReq pageReq) {
        try {
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            pageReq.setTenantId(user.getTenantId());
            pageReq.setOrgId(user.getOrgId());
            Page<DeviceResp> page = deviceService.findUnDirectDevicePage(pageReq);
            Page<DeviceVO> pageVO = new Page<>();
            BeanUtil.copyProperties(page, pageVO);
            List<DeviceVO> voList = new ArrayList<DeviceVO>();
            changeResultPage(page, pageVO, voList);
            return CommonResponse.success(pageVO);
        } catch (BusinessException e) {
            throw e;
        }
    }

    /**
     * 描述：删除设备
     *
     * @param request
     * @param deviceId
     * @return
     * @author huangxu
     * @created 2018年12月10日 下午21:49:37
     * @since
     */
    @PermissionAnnotation(value = "DEVICE")
    @SystemLogAnnotation(value = "删除设备")
    @RequestMapping("/deleteDevice")
    @ResponseBody
    public CommonResponse<ResultMsg> deleteDevice(HttpServletRequest request, String deviceId) {
        try {
            GetDeviceInfoRespVo getDeviceInfoRespVo = deviceApi.get(deviceId);
            String clientId = null;
            if(StringUtil.isNotBlank(getDeviceInfoRespVo.getParentId())){
                //为子设备
                clientId = getDeviceInfoRespVo.getParentId();
            }else {
                //为网关
            clientId = deviceId;
            //重启网关(网关重置)resetReq
            deviceTobApi.resetReq(clientId);
        }
            return deleteAll(deviceId,clientId);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
        }
    }


    public CommonResponse<ResultMsg> deleteAll(String deviceId,String clientId) throws Exception {
        GetDeviceInfoRespVo getDeviceInfoRespVo = deviceApi.get(deviceId);
        List<String> deviceIds = Lists.newArrayList();
        LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
        if(StringUtil.isBlank(getDeviceInfoRespVo.getParentId())){
            LOGGER.info("删除的设备为网关");
            //删除的设备为网关
            //获取子设备id
            List<ListDeviceInfoRespVo> listDeviceInfoRespVos = deviceApi.listDevicesByParentId(deviceId);
            for (ListDeviceInfoRespVo listDeviceInfoRespVo : listDeviceInfoRespVos){
                deviceIds.add(listDeviceInfoRespVo.getUuid());
            }
            LOGGER.info("删除的设备为网关=====deviceIds.size()"+deviceIds.size());
        }else {
            LOGGER.info("删除的设备为子设备");
            //删除的设备为子设备
            deviceIds.add(deviceId);
            LOGGER.info("删除的设备为子设备=====deviceIds.size()"+deviceIds.size());
        }
        //删除设备对应的空间关系
        spaceService.removeMount(user.getTenantId(),user.getOrgId(),StringUtils.join(deviceIds, ","));
        //子设备删除后删除对应的情景,ifttt
        for(String deviceIdNew : deviceIds){
            boolean flag = deviceTobApi.deleteDeviceRelation(user.getOrgId(),deviceIdNew,user.getTenantId(),true,clientId);
            if(flag == false){
                return new CommonResponse<ResultMsg>(ResultMsg.FAIL);
            }
        }
        //删除设备 删除成功之后
        deviceService.deleteDeviceByDeviceId(deviceId);
        return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
    }

    /**
     * 从内存获取设备属性
     *
     * @param request
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/propertyInfo", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public CommonResponse<Object> propertyInfo(HttpServletRequest request, String deviceId) {
        return CommonResponse.success(deviceService.getDeviceProerty(deviceId));
    }

    /**
     * 同步功能
     *
     * @param deviceId
     * @return
     */
    @PermissionAnnotation(value = "DEVICE")
    @SystemLogAnnotation(value = "同步网关子设备列表")
    @ApiOperation("同步网关子设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId", value = "网关Id", required = false, paramType = "query", dataType = "String")})
    @RequestMapping(value = "/synchronization", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public CommonResponse<ResultMsg> synchronization(@RequestParam(value = "deviceId", required = false) String deviceId) {
        try {
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            deviceService.synchronization(user.getOrgId(), deviceId, user.getTenantId(), user.getLocationId());
            return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
        }
    }

    /**
     * 描述：查询所有非直连设备
     *
     * @param request
     * @return
     * @author zhouzongwei
     * @created 2017年11月29日 下午3:32:59
     * @since
     */
    @SystemLogAnnotation(value = "查询所有非直连设备")
    @RequestMapping("/findAllUnDirectDeviceList")
    @ResponseBody
    public CommonResponse<List<DeviceResp>> findAllUnDirectDeviceList(HttpServletRequest request,
                                                                      DevicePageReq pageReq) {
        try {
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            pageReq.setTenantId(user.getTenantId());
            SpaceDeviceReq spaceDeviceReq = new SpaceDeviceReq();
            spaceDeviceReq.setTenantId(user.getTenantId());
            spaceDeviceReq.setOrgId(user.getOrgId());
            spaceDeviceReq.setLocationId(user.getLocationId());
            List<String> MountDevices = spaceService.getMountDeviceBySpaceId(spaceDeviceReq);
            pageReq.setMountDeviceList(MountDevices);
            List<DeviceResp> deviceResps = deviceService.findAllUnDirectDeviceList(pageReq);
            return CommonResponse.success(deviceResps);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
        }
    }

    /**
     * 描述：查询所有ifttt需要的设备
     *
     * @return
     * @author zhouzongwei
     * @created 2017年11月29日 下午3:32:59
     * @since
     */
    @ApiOperation(value = "查询所有ifttt需要的设备")
    @RequestMapping(value = "/listAllIftttDevice", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<List<Map<String, Object>>> listAllIftttDevice(
            @RequestParam("iftttType") @ApiParam(value = "if/then类型", allowableValues = "if,then") String iftttType,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "spaceId", required = false) Long spaceId) {
        try {
            List<Map<String, Object>> iftttDeviceResps = deviceService.findIftttDeviceList(iftttType, name, spaceId);
            return CommonResponse.success(iftttDeviceResps);
        } catch (BusinessException e) {
            throw e;
        }
    }

    /**
     * 描述：更新设备信息
     *
     * @param request
     * @return
     * @author zhouzongwei
     * @created 2017年11月29日 下午6:03:03
     * @since
     */
    @PermissionAnnotation(value = "DEVICE")
    @SystemLogAnnotation(value = "更新设备信息")
    @RequestMapping("/updateDevice")
    @ResponseBody
    public CommonResponse<ResultMsg> updateDeviceType(HttpServletRequest request, UpdateDeviceInfoReq deviceReq) {
        try {
        	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
        	deviceReq.setOrgId(user.getOrgId());
        	deviceReq.setTenantId(user.getTenantId());
            deviceService.updateDevice(deviceReq);
            return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
        } catch (BusinessException e) {
            throw e;
        }
    }

    /**
     * 描述：添加设备
     *
     * @param request
     * @return
     * @author zhouzongwei
     * @created 2017年11月30日 上午11:02:36
     * @since
     */
    @PermissionAnnotation(value = "DEVICE")
    @SystemLogAnnotation(value = "添加设备")
    @RequestMapping("/addDevice")
    @ResponseBody
    public CommonResponse<ResultMsg> saveDevice(HttpServletRequest request, String deviceIp, String deviceName,
                                                String type) {
        try {
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            UpdateDeviceInfoReq deviceReq = new UpdateDeviceInfoReq();
            deviceReq.setCreateBy(user.getUserId());
            deviceReq.setLocationId(user.getLocationId());
            deviceReq.setCreateBy(user.getUserId());
            deviceReq.setUpdateBy(user.getUserId());
            deviceReq.setName(deviceName);
            deviceReq.setIp(deviceIp);
            deviceReq.setIsDirectDevice(1);
            deviceReq.setTenantId(user.getTenantId());
            deviceReq.setUuid(ToolUtils.getUUID());
            deviceReq.setDeviceTypeId(1L);
            deviceReq.setTenantId(user.getTenantId());
            deviceReq.setOrgId(user.getOrgId());
            deviceService.saveDevice(deviceReq);
            return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
        } catch (BusinessException e) {
            throw e;
        }
    }

    @RequestMapping("/queryAirCondition")
    @ResponseBody
    public CommonResponse<Page<DeviceVO>> queryAirCondition(HttpServletRequest request, HttpServletResponse res) {
        try {
            DeviceReq deviceReq = new DeviceReq();
            CommDeviceInfoReq commDeviceInfoReq = new CommDeviceInfoReq();
            LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            MultiProtocolUDPClient mu = new MultiProtocolUDPClient();
            List<AirConditionVO> li = mu.toJavaBean(mu.receive());
            if (li != null) {
                for (AirConditionVO airCondition : li) {
                    // 通过设备ID去查询，判断数据库中是否存在，不存在就添加
                    GetDeviceInfoRespVo deviceResp = deviceApi.get(airCondition.getAtuId());
                    if (deviceResp == null) {
                        commDeviceInfoReq.setTenantId(user.getTenantId());
                        commDeviceInfoReq.setOrgId(user.getOrgId());
                        commDeviceInfoReq.setLocationId(user.getLocationId());
                        commDeviceInfoReq.setCreateTime(new Date());
                        commDeviceInfoReq.setLastUpdateDate(new Date());
                        commDeviceInfoReq.setIsDirectDevice(0);
                        commDeviceInfoReq.setDeviceId(airCondition.getAtuId());
                        commDeviceInfoReq.setName(airCondition.getAtuName());
                        commDeviceInfoReq.setIp(Constants.AIR_CONDITION_DEVICE_IP);
                        commDeviceInfoReq.setConditional(airCondition.getAtuRoom());// 房间名字
                        commDeviceInfoReq.setDeviceTypeId(Long.valueOf(Constants.AIR_CONDITION_DEVICE_TYPE_ID));
                        deviceReq.setDeviceInfoReq(commDeviceInfoReq);
//                        deviceService.saveDevice(deviceReq);
                    }
                }
            }
            // 获取空调设备列表
            DevicePageReq pageReq = new DevicePageReq();
            pageReq.setTenantId(Long.valueOf(Constants.AIR_CONDITION_DEVICE_TENANT_ID));
            pageReq.setOrgId(user.getOrgId());
            pageReq.setLocationId(user.getLocationId());
            pageReq.setDeviceTypeId(Integer.valueOf(Constants.AIR_CONDITION_DEVICE_TYPE_ID));
            Page<DeviceResp> page = deviceService.queryAirCondition(pageReq);
            Page<DeviceVO> pageVO = new Page<>();
            BeanUtil.copyProperties(page, pageVO);
            List<DeviceVO> voList = new ArrayList<DeviceVO>();
            changeResultPage(page, pageVO, voList);
            return CommonResponse.success(pageVO);
            // commDeviceInfoReq.setLocationId(user.getLocationId());
            // commDeviceInfoReq.setCreateBy(user.getUserId());
            // commDeviceInfoReq.setUpdateBy(user.getUserId());
            // commDeviceInfoReq.setName(deviceName);
            // commDeviceInfoReq.setIp(deviceIp);
            // commDeviceInfoReq.setDeviceTypeId(1L);
            // return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
        } catch (BusinessException e) {
            throw e;
        }
    }

    /**
     * 描述：查询历史告警数据
     *
     * @param request
     * @return
     * @author zhouzongwei
     * @created 2017年11月30日 下午4:12:24
     * @since
     */
    @SystemLogAnnotation(value = "查询历史告警数据")
    @RequestMapping("/detail")
    @ResponseBody
    public CommonResponse<DeviceDetailVo> detail(HttpServletRequest request, String deviceId) {
        try {
        	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            return CommonResponse.success(deviceService.getDeviceByDeviceId(user.getOrgId(), user.getTenantId(), deviceId));
        } catch (BusinessException e) {
            throw e;
        }
    }

    /**
     * 数据转换
     *
     * @param deviceResp
     * @return
     */
    private GetDeviceInfoRespVo changeVO(GetDeviceInfoRespVo deviceResp) {
        if (deviceResp != null) {
            if (deviceResp.getCreateTime() != null) {
                deviceResp.setCreateTime(deviceResp.getCreateTime());
            }
            if (deviceResp.getLastUpdateDate() != null) {
                deviceResp.setLastUpdateDate(deviceResp.getLastUpdateDate());
            }
        }
        return deviceResp;
    }

    /**
     * 数据转换
     *
     * @param deviceResp
     * @return
     */
    private DeviceResp changeDeviceRespToDeviceVO(DeviceResp deviceResp, DeviceVO deviceVO) {
        if (deviceResp != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            BeanUtil.copyProperties(deviceResp, deviceVO);
            if (deviceResp.getCreateTime() != null) {
                String createTime = simpleDateFormat.format(deviceResp.getCreateTime());
                deviceVO.setCreateTime(createTime);
            }
            if (deviceResp.getLastUpdateDate() != null) {
                String updateTime = simpleDateFormat.format(deviceResp.getLastUpdateDate());
                deviceVO.setLastUpdateDate(updateTime);
            }
        }
        return deviceResp;
    }

    /**
     * 描述：查询所有设备业务类型列表
     *
     * @param request
     * @return
     * @author zhouzongwei
     * @created 2017年12月6日 下午4:02:55
     * @since
     */
    @RequestMapping("/findDeviceCategoryList")
    @ResponseBody
    public CommonResponse<List<ListDeviceTypeRespVo>> findDeviceCategoryList(HttpServletRequest request) {
        try {
            List<ListDeviceTypeRespVo> deviceTypeResp = deviceService.findDeviceTypeList();
            return CommonResponse.success(deviceTypeResp);
        } catch (BusinessException e) {
            throw e;
        }
    }

    /**
     * 根据Ip获取设备信息
     *
     * @param request
     * @param deviceIp
     * @return
     */
    @RequestMapping("/findDeviceByIp")
    @ResponseBody
    public CommonResponse<GetDeviceInfoRespVo> findDeviceByIp(HttpServletRequest request, String deviceIp) {
        try {
        	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
        	GetDeviceInfoRespVo deviceResp = centralControlDeviceApi.getDeviceByDeviceIp(user.getOrgId(), user.getTenantId(),deviceIp);
            return CommonResponse.success(deviceResp);
        } catch (BusinessException e) {
            throw e;
        }
    }

    /**
     * 根据Ip获取设备信息
     *
     * @param request
     * @return
     */
    @RequestMapping("/addSensorCondition")
    @ResponseBody
    public CommonResponse<ResultMsg> addSensorCondition(HttpServletRequest request, String deviceId, String condition) {
        try {
            if (StringUtils.isNotBlank(deviceId) && StringUtils.isNotBlank(condition)) {
            	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            	UpdateDeviceInfoReq deviceRep = new UpdateDeviceInfoReq();
            	deviceRep.setUuid(deviceId);
            	deviceRep.setTenantId(user.getTenantId());
            	deviceRep.setOrgId(user.getOrgId());
            	deviceRep.setLocationId(user.getLocationId());
            	deviceRep.setConditional(condition);
                deviceService.updateDevice(deviceRep);
                return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
            } else {
                throw new BusinessException(BusinessExceptionEnum.PARAM_IS_NULL);
            }
        } catch (BusinessException e) {
            throw e;
        }
    }

    /**
     * 修改设备的业务类型
     *
     * @param request
     * @return
     */
    @PermissionAnnotation(value = "DEVICE")
    @SystemLogAnnotation(value = "修改设备的业务类型")
    @RequestMapping(value = "/updateBusinessType", method = {RequestMethod.POST})
    @ResponseBody
    public CommonResponse<ResultMsg> updateBusinessType(HttpServletRequest request, String deviceId, String type) {
        if (StringUtils.isNotBlank(deviceId) && StringUtils.isNotBlank(type)) {
        	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
        	UpdateDeviceInfoReq deviceRep = new UpdateDeviceInfoReq();
            DeviceBusinessTypeResp deviceBusinessType = deviceBusinessTypeApi.getBusinessTypeIdByType(user.getOrgId(), user.getTenantId(), type);
            deviceRep.setUuid(deviceId);
            deviceRep.setTenantId(user.getTenantId());
        	deviceRep.setOrgId(user.getOrgId());
        	deviceRep.setLocationId(user.getLocationId());
            deviceRep.setBusinessTypeId(deviceBusinessType.getId());
            deviceService.updateDevice(deviceRep);
            return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
        } else {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_NULL);
        }
    }

    @ApiOperation(value = "根据model查询业务类型列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantId", value = "租户Id", required = true, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "model", value = "产品model", required = false, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/findBusinessTypeList", method = RequestMethod.GET)
    @ResponseBody
    public CommonResponse<List<DeviceBusinessTypeResp>> findBusinessTypeList(
            @RequestParam(value = "tenantId", required = true) Long tenantId,
            @RequestParam(value = "model", required = false) String model) {
        if (StringUtils.isBlank(model)) {
            model = "";
        }
        LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
        List<DeviceBusinessTypeResp> list = deviceService.findBusinessTypeList(user.getOrgId(), user.getTenantId(), model);
        return CommonResponse.success(list);
    }

    /**
     * 施工app导入网关的uuid，location，tenantId到中控并添加设备权限
     */
    @RequestMapping("/appAddDevice")
    @ResponseBody
    public CommonResponse<ResultMsg> appAddDevice(HttpServletRequest request, String deviceId, Long locationId,
                                                  Long tenantId) {
        try {
            DeviceReq deviceReq = new DeviceReq();
            GetDeviceInfoRespVo deviceResp = deviceApi.get(deviceId);
            if (deviceResp == null) {
            	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
                CommDeviceInfoReq commDeviceInfoReq = new CommDeviceInfoReq();
                commDeviceInfoReq.setTenantId(tenantId);
                commDeviceInfoReq.setOrgId(user.getOrgId());
                commDeviceInfoReq.setDeviceId(deviceId);
                commDeviceInfoReq.setLocationId(locationId);
                commDeviceInfoReq.setLastUpdateDate(new Date());
                commDeviceInfoReq.setCreateTime(new Date());
                commDeviceInfoReq.setIsDirectDevice(1);
                deviceReq.setDeviceInfoReq(commDeviceInfoReq);
//                deviceService.saveDevice(deviceReq);
                // 添加用户和设备的策略
                int acls = deviceService.addAclsToB(deviceId);
                LOGGER.info("mqttPloyApi.addAcls ok, {}, {}", deviceId, acls);
                return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
            } else {
//                int acls = deviceService.addAcls(deviceId);
//                LOGGER.info("mqttPloyApi.addAcls ok, {}, {}", deviceId, acls);
                return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
            }
        } catch (BusinessException e) {
            throw e;
        }
    }

    /**
     * 导入excel，并解析execl数据保存到device_business_type中
     * 施工app导入网关的uuid，location，tenantId到中控并添加设备权限
     */
    //@SystemLogAnnotation(value = "设备上传并添加策略")
    //@PermissionAnnotation(value = "DEVICE,OTA")
    @RequestMapping(value = "/appAddDeviceByExcel", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse appAddDeviceByExcel(HttpServletRequest request,
                                                         MultipartHttpServletRequest multipartRequest, Long locationId, Long tenantId) {
        LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
        try {
            GetDeviceInfoRespVo deviceResp = new GetDeviceInfoRespVo();
            UpdateDeviceInfoReq commDeviceInfoReq = new UpdateDeviceInfoReq();
            //List<String[]> list = deviceService.fileUpload(multipartRequest, tenantId);
            MultipartFile multipartFile = multipartRequest.getFile(multipartRequest.getFileNames().next());
            List<Map<String, Object>> list = Lists.newArrayList();
            Map<String,Object> resultMap=Maps.newHashMap();
            List<String> success = Lists.newArrayList();
            List<String> error = Lists.newArrayList();
            try {
                LOGGER.info("获取解析的list数据=====start");
                list = ExcelUtils.resolveExcelStr(multipartFile);
                LOGGER.info("获取解析的list数据"+list);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (list != null) {
                LOGGER.info("list.size:"+list.size());
                int index = 0;
                for (int i= 0;i<list.size();i++) {
                    // 判断该设备是否存在
                    LOGGER.info("获取解析的uuid===start");
                    if(list.get(i).get("0") == null){
                        index ++;
                        error.add("第"+(i+1)+"行的设备UUID不能为空");
                        LOGGER.info("第"+(i+1)+"行的设备UUID不能为空");
                        continue;
                    }
                    String deviceId = list.get(i).get("0").toString();
                    LOGGER.info("获取解析的uuid"+deviceId);

                    LOGGER.info("获取设备===start");
                    deviceResp = deviceApi.get(deviceId);
                    LOGGER.info("获取设备:"+deviceResp);
                    if (deviceResp == null) {
                        LOGGER.info("该设备不在Redis和库中");
                        // 添加设备和添加用户和设备的策略
                        commDeviceInfoReq.setTenantId(tenantId);
                        LOGGER.info("获取解析的密码===start");
                        if(list.get(i).get("1") == null){
                            index ++;
                            error.add("第"+(i+1)+"行的password不能为空");
                            LOGGER.info("第"+(i+1)+"行的password不能为空");
                            continue;
                        }
                        String password = list.get(i).get("1").toString();
                        LOGGER.info("获取解析的密码"+password);

                        LOGGER.info("获取解析的名字===start");
                        if(list.get(i).get("2") == null){
                            index ++;
                            error.add("第"+(i+1)+"行的名字不能为空");
                            LOGGER.info("第"+(i+1)+"行的名字不能为空");
                            continue;
                        }
                        String deviceName = list.get(i).get("2").toString();
                        LOGGER.info("获取解析的名字"+deviceName);
                        String productId = "";
                        if(list.get(i).size() != 4){
                            //数组越界了，即没有productId
                            LOGGER.info("数组越界了，即没有productId");
                            index ++;
                                error.add("第"+(i+1)+"行的产品Id不能为空");
                                continue;
                        }else {
                            LOGGER.info("获取解析的产品Id===start");
                            if(list.get(i).get("3") == null){
                                index ++;
                                error.add("第"+(i+1)+"行的产品Id不能为空");
                                LOGGER.info("第"+(i+1)+"行的产品Id不能为空");
                                continue;
                            }
                            productId = list.get(i).get("3").toString();
                            LOGGER.info("获取解析的产品Id"+productId);
                        }
                        // 获取ota列表的最新版本
                		if (productId != null) {
                            OtaFileInfoReq otaFileInfoReq = new OtaFileInfoReq();
                            otaFileInfoReq.setTenantId(user.getTenantId());
                            otaFileInfoReq.setOrgId(user.getOrgId());
                            otaFileInfoReq.setLocationId(user.getLocationId());
                            otaFileInfoReq.setProductId(Long.valueOf(productId));
                            OtaFileInfoResp otaFileInfoResp = otaControlApi.findOtaFileInfoByProductId(otaFileInfoReq);
                            if (otaFileInfoResp != null && StringUtils.isNotBlank(otaFileInfoResp.getVersion())) {
                                commDeviceInfoReq.setHwVersion(otaFileInfoResp.getVersion());
                            }
                        }
                        commDeviceInfoReq.setUuid(deviceId);
                        commDeviceInfoReq.setLocationId(user.getLocationId());
                        commDeviceInfoReq.setOrgId(user.getOrgId());
                        commDeviceInfoReq.setUpdateBy(user.getUserId());
                        commDeviceInfoReq.setCreateBy(user.getUserId());
                        commDeviceInfoReq.setIsDirectDevice(1);
                        // 设置空开的设备属性
                        if ("1090210993".equals(productId)) {
                            commDeviceInfoReq.setDeviceTypeId(-4000L);
                        } else {
                            commDeviceInfoReq.setDeviceTypeId(1L);
                        }
                        commDeviceInfoReq.setProductId(Long.valueOf(productId));
                        commDeviceInfoReq.setName(deviceName);
                        LOGGER.info("执行保存============start");
                        deviceService.saveDevice(commDeviceInfoReq);
                        LOGGER.info("执行保存============end");
                        Map<String,Object> map=new HashMap<>();
                        map.put("dev",DigestUtils.sha256Hex(password));

                        try {
                            RedisCacheUtil.hashPutAll(deviceId+":pwd", map, false);
                            LOGGER.info("start=======addAcls=========");
                            RedisCacheUtil.setPush(deviceId+":pub", "iot/v1/s/#", false);
                            RedisCacheUtil.setPush(deviceId+":pub", "iot/v1/cb/#", false);//墙控ifttt回调同步规定的策略
                            RedisCacheUtil.setPush(deviceId+":sub", "iot/v1/c/#", false);
                            LOGGER.info("end=======addAcls=========");
                        } catch (Exception e) {
                            e.printStackTrace();
                            error.add("第"+(i+1)+"行的出现异常问题");
                            continue;
                        }
                        //if(index !=0 ){
                            //throw new BusinessException(BusinessExceptionEnum.BUSINESS_TYPE_IS_EXIST);
                            //return new CommonResponse<ResultMsg>(-1, "please login");
                        //}else {
                            //return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
                        //}

                    } else {
                        LOGGER.info("该设备已经存在Redis和库中");
                        error.add("第"+(i+1)+"行的设备已经存在");
                        Map<String,Object> map2=new HashMap<>();
                        map2.put("dev",DigestUtils.sha256Hex(list.get(i).get("1").toString()));
                        RedisCacheUtil.hashPutAll(deviceId+":pwd", map2, false);
                        //存在的也需要加策略
                        RedisCacheUtil.setPush(deviceId+":pub", "iot/v1/s/#", false);
                        RedisCacheUtil.setPush(deviceId+":pub", "iot/v1/cb/#", false);//墙控ifttt回调同步规定的策略
                        RedisCacheUtil.setPush(deviceId+":sub", "iot/v1/c/#", false);
                        continue;
                    }
                    success.add(deviceId);
                }
            }
            resultMap.put("success", success);
            resultMap.put("fail", error);
            return CommonResponse.success(resultMap);
        } catch (BusinessException e) {
            throw e;
        }
    }

    private String generatePwdKey(String uuid) {
        return new StringBuilder().append(uuid).append(":").append("pwd").toString();
    }

    /**
     * 描述：查询所有ifttt需要的设备
     *
     * @return
     * @author zhouzongwei
     * @created 2017年11月29日 下午3:32:59
     * @since
     */
    @PermissionAnnotation(value = "DEVICE,OTA")
    @SystemLogAnnotation(value = "更新OTA版本")
    @ApiOperation(value = "更新OTA版本")
    @RequestMapping(value = "/updateOtaVersion", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<ResultMsg> updateOtaVersion(
    		@RequestParam(value = "deviceId", required = false) String deviceId,
            @RequestParam(value = "tenantId", required = true) Long tenantId,
            @RequestParam(value = "locationId", required = true) Long locationId) {
        try {
        	if (StringUtils.isBlank(deviceId)) {
        		deviceId = "";
			}
        	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
            deviceService.updateOtaVersion(user.getOrgId(), deviceId,user.getTenantId(),user.getLocationId());
            return CommonResponse.success(ResultMsg.SUCCESS);
        } catch (BusinessException e) {
            throw e;
        }
    }
    
    /**
     * 描述：查询OTA文件列表
     *
     * @return
     * @author linjihuang
     * @created 2018年9月6日 下午3:32:59
     * @since
     */

    @PermissionAnnotation(value = "PROJECT_OTA_PACKAGE_MANAGER")
    @ApiOperation(value = "获取OTA文件列表")
    @RequestMapping(value = "/getOtaFileList", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<Page<OtaFileInfoResp>> getOtaFileList(@RequestBody OtaPageReq pageReq) {
        try {
        	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
        	pageReq.setTenantId(user.getTenantId());
        	pageReq.setOrgId(user.getOrgId());
            pageReq.setLocationId(user.getLocationId());
        	Page<OtaFileInfoResp> otafileInfoResps = deviceService.getOtaFileList(pageReq);
            return CommonResponse.success(otafileInfoResps);
        } catch (BusinessException e) {
            throw e;
        }
    }
    
    /**
     * 描述：获取云端OTA文件下载路径
     *
     * @return
     * @author linjihuang
     * @created 2018年9月10日 下午3:32:59
     * @since
     */
    @ApiOperation(value = "下载云端OTA文件")
    @RequestMapping(value = "/downLoadOtaFile", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<ResultMsg> downLoadOtaFile(@RequestBody OtaFileInfoReq otaFileInfoReq) {
        try {
        	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
        	otaFileInfoReq.setUpdateBy(user.getUserId());
        	otaFileInfoReq.setOrgId(user.getOrgId());
        	otaFileInfoReq.setTenantId(user.getTenantId());
        	otaFileInfoReq.setLocationId(user.getLocationId());
        	deviceService.downLoadOtaFile(otaFileInfoReq);
            return CommonResponse.success(ResultMsg.SUCCESS);
        } catch (BusinessException e) {
            throw e;
        }
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
			@ApiImplicitParam(name = "productId", value = "产品Id", required = true, paramType = "query", dataType = "Long"),
			@ApiImplicitParam(name = "version", value = "版本号", required = true, paramType = "query", dataType = "String") })
	@RequestMapping(value = "/otaFileUpload", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<Integer> fileUpload(MultipartHttpServletRequest multipartRequest,
			@RequestParam("productId") Long productId, @RequestParam("version") String version) throws BusinessException {
		int result = 0;
		try {
			LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
			OtaFileInfoReq otaFileInfoReq = new OtaFileInfoReq();
			otaFileInfoReq.setUpdateBy(user.getUserId());
			otaFileInfoReq.setProductId(productId);
			otaFileInfoReq.setTenantId(user.getTenantId());
			otaFileInfoReq.setOrgId(user.getOrgId());
			otaFileInfoReq.setLocationId(user.getLocationId());
			otaFileInfoReq.setVersion(version);
			result = deviceService.otaFileUpload(multipartRequest, otaFileInfoReq);
		} catch (BusinessException e) {
			e.printStackTrace();
			throw e;
		}
		return CommonResponse.success(result);
	}
	
	/**
     * 描述：获取云端OTA最新版本
     *
     * @return
     * @author linjihuang
     * @created 2018年9月10日 下午3:32:59
     * @since
     */
    @ApiOperation(value = "获取云端OTA最新版本")
    @RequestMapping(value = "/getOtaLatestVersion", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<UpgradePlanResp> getOtaLatestVersion(@RequestBody UpgradePlanReq upgradePlanReq) {
        try {
        	UpgradePlanResp upgradePlanResp = deviceService.getOtaLatestVersion(upgradePlanReq);
            return CommonResponse.success(upgradePlanResp);
        } catch (BusinessException e) {
            throw e;
        }
    }
    
    /**
     * 描述：获取云端OTA最新版本
     *
     * @return
     * @author linjihuang
     * @created 2018年9月10日 下午3:32:59
     * @since
     */
    @ApiOperation(value = "获取设备类型列表")
    @RequestMapping(value = "/getDeviceTypeIdList", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<List<ListDeviceTypeRespVo>> getDeviceTypeIdList(@RequestBody PageDeviceTypeByParamsReq params) {
        try {
        	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
//        	PageDeviceTypeByParamsReq params = new PageDeviceTypeByParamsReq();
        	params.setTenantId(user.getTenantId());
        	params.setOrgId(user.getOrgId());
        	List<ListDeviceTypeRespVo> listDeviceTypeRespVos = deviceService.getDeviceTypeIdList(params);
            return CommonResponse.success(listDeviceTypeRespVos);
        } catch (BusinessException e) {
            throw e;
        }
    }
    
    /**
     * 描述：获取直连设备
     *
     * @return
     * @author wl
     * @created 2018年11月9日 
     * @since
     */
    @ApiOperation(value = "获取直连设备列表")
    @RequestMapping(value = "/getDirectDeviceList", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<List<GetDeviceInfoRespVo>> getDirectDeviceList(@RequestBody DevicePageReq devicePageReq){
    	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
    	devicePageReq.setTenantId(user.getTenantId());
    	devicePageReq.setOrgId(user.getOrgId());
    	devicePageReq.setLocationId(user.getLocationId());
    	LOGGER.info("devicePageReq : 执行保存============locationId : " + devicePageReq.getLocationId());
    	return CommonResponse.success(deviceService.getDirectDevice(devicePageReq));
    }
    
    /**
     * 描述：根据父ID获取子设备列表
     *
     * @return
     * @author ljh
     * @created 2018年11月13日 
     * @since
     */
    @ApiOperation(value = "根据父ID获取子设备列表")
    @RequestMapping(value = "/getDeviceListByParentId", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<List<DeviceDetailVo>> getDeviceListByParentId(@RequestBody CommDeviceInfoReq commDeviceInfoReq){
    	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
    	commDeviceInfoReq.setTenantId(user.getTenantId());
    	commDeviceInfoReq.setOrgId(user.getOrgId());
    	commDeviceInfoReq.setLocationId(user.getLocationId());
    	return CommonResponse.success(deviceService.getDeviceListByParentId(commDeviceInfoReq));
    }
    
    /**
     * 描述：搜索子设备
     *
     * @return
     * @author ljh
     * @created 2018年11月13日 
     * @since
     */
    @ApiOperation(value = "搜索子设备")
    @RequestMapping(value = "/searchStar", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<ResultMsg> searchStar(@RequestBody CommDeviceInfoReq commDeviceInfoReq){
    	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
    	commDeviceInfoReq.setTenantId(user.getTenantId());
    	commDeviceInfoReq.setLocationId(user.getLocationId());
    	deviceService.searchStar(commDeviceInfoReq);
    	return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
    }
    
    /**
     * 描述：搜索子设备
     *
     * @return
     * @author ljh
     * @created 2018年11月13日 
     * @since
     */
    @ApiOperation(value = "停止搜索子设备")
    @RequestMapping(value = "/searchStop", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<ResultMsg> searchStop(@RequestBody CommDeviceInfoReq commDeviceInfoReq){
    	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
    	commDeviceInfoReq.setTenantId(user.getTenantId());
    	commDeviceInfoReq.setLocationId(user.getLocationId());
    	deviceService.searchStop(commDeviceInfoReq);
    	return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
    }
    
    /**
     * 描述：编辑网关信息
     *
     * @return
     * @author ljh
     * @created 2018年11月14日 
     * @since
     */
    @PermissionAnnotation(value = "DEVICE")
    @ApiOperation(value = "编辑网关信息")
    @RequestMapping(value = "/editGatewayInfo", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<ResultMsg> editGatewayInfo(@RequestBody CommDeviceInfoReq commDeviceInfoReq){
    	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
    	commDeviceInfoReq.setTenantId(user.getTenantId());
    	commDeviceInfoReq.setOrgId(user.getOrgId());
    	commDeviceInfoReq.setLocationId(user.getLocationId());
    	deviceService.editGatewayInfo(commDeviceInfoReq);
    	return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
    }
    
    /**
     * 描述：编辑子设备信息
     *
     * @return
     * @author ljh
     * @created 2018年11月14日 
     * @since
     */
    @PermissionAnnotation(value = "DEVICE")
    @ApiOperation(value = "编辑子设备信息")
    @RequestMapping(value = "/editDeviceInfo", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<ResultMsg> editDeviceInfo(@RequestBody CommDeviceInfoReq commDeviceInfoReq){
    	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
    	commDeviceInfoReq.setTenantId(user.getTenantId());
    	commDeviceInfoReq.setOrgId(user.getOrgId());
    	commDeviceInfoReq.setLocationId(user.getLocationId());
    	deviceService.editDeviceInfo(commDeviceInfoReq);
    	return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
    }
    
    /**
     * 描述：编辑子设备信息
     *
     * @return
     * @author ljh
     * @created 2018年11月14日 
     * @since
     */
    @ApiOperation(value = "空开自检")
    @RequestMapping(value = "/airSwichSelfCheck", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<ResultMsg> airSwichSelfCheck(Long spaceId){
    	LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
    	deviceService.airSwitchSelfCheck(user.getOrgId(), spaceId, user.getTenantId());
    	return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
    }
}
