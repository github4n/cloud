package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.common.helper.Page;
import com.iot.device.model.Device;
import com.iot.device.vo.req.CommDeviceInfoReq;
import com.iot.device.vo.req.DevTypePageReq;
import com.iot.device.vo.req.DeviceBusinessTypeIDSwitchReq;
import com.iot.device.vo.req.DevicePageReq;
import com.iot.device.vo.req.device.ListDeviceByParamsReq;
import com.iot.device.vo.req.device.PageDeviceInfoReq;
import com.iot.device.vo.rsp.DevicePropertyInfoResp;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.IftttDeviceResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceByParamsRespVo;
import com.iot.device.vo.rsp.device.PageDeviceInfoRespVo;

import java.util.List;
import java.util.Map;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 11:50 2018/4/16
 * @Modify by:
 */
public interface IDeviceService extends IService<Device> {

    List<GetDeviceInfoRespVo> findDirectDeviceListByVenderCode(Long tenantId, Long locationId,String venderFlag,Integer isDirectDevice);

    
    List<DevicePropertyInfoResp> findDeviceListByDeviceIds(List<String> deviceIds);

    List<DeviceResp> findDeviceListByParentId(String parentId);

    Map<String,Object> findDataReport(Long spaceId, String deviceId, String dateType,String deviceType);

    Page<DeviceResp> findUnDirectDevicePage(DevicePageReq pageReq);


    Page<DeviceResp> findDirectDevicePageToCenter(DevicePageReq pageReq);

    List<DeviceResp> findAllUnDirectDeviceList(DevicePageReq pageReq);

    
    List<GetDeviceInfoRespVo> findDirectDeviceByDeviceCatgory(String venderCode,Long tenantId,
    		Long locationId);

    Integer getCountByDeviceIdsAndBusinessTypesAndSwitch(DeviceBusinessTypeIDSwitchReq req);


    Page<DeviceResp> findDevPageByProductId(Integer pageNum, Integer pageSize, Long productId);

    List<DeviceResp> findDeviceByCondition(DeviceBusinessTypeIDSwitchReq req);
    
    List<IftttDeviceResp> findIftttDeviceList(CommDeviceInfoReq req);

    Page<DeviceResp> queryAirCondition(DevicePageReq pageReq);

    Map<String, Map<String,Long>> findUuidProductIdMap(List<String> uuIdList);

    /**
     * 描述：查设备id与租户id对应关系
     * @author nongchongwei
     * @date 2018/11/1 16:40
     * @param
     * @return
     */
    Map<String, Map<String,Long>> findUuidTenantIdMap(List<String> uuIdList);

    List<DeviceResp> getVersionByDeviceIdList(List<String> deviceIdList);


    List<GetDeviceInfoRespVo> selectAllDeviceToCenter(DevicePageReq pageReq);

    /**
     * 获取设备列表
     *
     * @param tenantId
     * @param deviceIds
     * @return
     * @author lucky
     * @date 2018/9/28 16:08
     */
    List<Device> findListByDeviceIds(Long tenantId, List<String> deviceIds);


    Device getByDeviceId(Long tenantId, String deviceId);

    List<Device> findListByDeviceParentId(Long tenantId, String parentDeviceId);

    /**
     * 分页获取设备列表信息【new】
     *
     * @param params
     * @return
     */
    Page<PageDeviceInfoRespVo> findPageByParams(PageDeviceInfoReq params);


    Page<DeviceResp> findPageByDeviceTypeList(DevTypePageReq pageReq);

    GetDeviceInfoRespVo getDeviceByDeviceIp(Long orgId, Long tenantId,String deviceIp);

    List<ListDeviceByParamsRespVo> listByParams(ListDeviceByParamsReq params);
    
    Page<DeviceResp> getGatewayAndSubDeviceList(DevicePageReq pageReq);
    
    List<Long> getExistProductList(PageDeviceInfoReq params);
    
    List<GetDeviceInfoRespVo> getDeviceListByParentId(CommDeviceInfoReq commDeviceInfoReq);
}
