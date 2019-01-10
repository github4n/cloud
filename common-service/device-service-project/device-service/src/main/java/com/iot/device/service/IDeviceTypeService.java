package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.common.helper.Page;
import com.iot.device.model.DeviceType;
import com.iot.device.vo.req.DeviceTypeReq;
import com.iot.device.vo.rsp.DeviceTypeResp;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lucky
 * @since 2018-04-17
 */
public interface IDeviceTypeService extends IService<DeviceType> {


    /**
     * 校验deviceType 是否存在
     *
     * @param deviceTypeId
     * @return
     * @author lucky
     * @date 2018/6/28 16:03
     */
    void checkDeviceTypExistByDeviceTypeId(Long deviceTypeId);

    /**
     * 删除
     * @param id
     */
    void delete(Long id,Long tenantId);

    /**
     * 根据条件获取设备类型
     * @param req
     */
    public Page<DeviceTypeResp> getDeviceTypeByCondition(@RequestBody DeviceTypeReq req);

    List<DeviceType> findListByDeviceTypeIds(List<Long> deviceTypeIds);

    DeviceType getDeviceTypeById(Long deviceTypeId);

    DeviceType getDeviceTypeByTypeModel(String typeModel);

    List<DeviceTypeResp> getByIds(List<Long> deviceTypeIds);

    /**
     * @despriction：根据ifttt类型过滤
     * @author  yeshiyuan
     * @created 2018/11/23 17:21
     */
    List<DeviceTypeResp> getByIdsAndIfffType(List<Long> ids, String iftttType);

    List<String> getDeviceTypeNameByIds(List<Long> deviceTypeIds);
    /**
     *@description 根据设备类型id，获取设备类型信息
     *@author wucheng
     *@params [deviceTypeIds]
     *@create 2018/12/12 10:55
     *@return java.util.List<com.iot.device.vo.rsp.DeviceTypeResp>
     */
    List<DeviceTypeResp> getDeviceTypeIdAndNameByIds(List<Long> deviceTypeIds);
}
