package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.device.model.DeviceTypeToServiceModule;
import com.iot.device.vo.req.DeviceTypeToServiceModuleReq;
import com.iot.device.vo.rsp.DeviceTypeToServiceModuleResp;
import com.iot.device.vo.rsp.servicemodule.PackageServiceModuleDetailResp;
import org.springframework.jdbc.support.lob.LobCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 设备类型对应模组表 服务类
 * </p>
 *
 * @author lucky
 * @since 2018-07-03
 */
public interface IDeviceTypeToServiceModuleService extends IService<DeviceTypeToServiceModule> {

    Long saveOrUpdate(DeviceTypeToServiceModuleReq deviceTypeToServiceModuleReq);

    void saveMore(DeviceTypeToServiceModuleReq deviceTypeToServiceModuleReq);

    void delete(ArrayList<Long> ids);

    List<DeviceTypeToServiceModuleResp> listByDeviceTypeId(Long deviceTypeId, Long tenantId);

    List<DeviceTypeToServiceModuleResp> listByServiceModuleId(Long serviceModuleId,Long tenantId);

    void update(Long id, Integer status);

    void copyService(Long sourceDeviceTypeId, Long targetProductId);

    /**
     * @despriction：校验设备类型是否有iftttType属性、方法、事件
     * @author  yeshiyuan
     * @created 2018/11/22 14:00
     */
    boolean checkDeviceTypeHadIftttType(Long deviceTypeId);

    /**
     * @despriction：根据ifttt类型找到对应的模组信息
     * @author  yeshiyuan
     * @created 2018/11/22 15:53
     * @return
     */
    PackageServiceModuleDetailResp queryServiceModuleDetailByIfttt(Long deviceTypeId, String iftttType);
}
