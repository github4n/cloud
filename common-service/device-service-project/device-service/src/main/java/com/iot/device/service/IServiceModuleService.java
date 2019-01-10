package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.pagehelper.PageInfo;
import com.iot.device.model.ServiceModule;
import com.iot.device.vo.req.AddServiceModuleReq;
import com.iot.device.vo.req.GenerateModuleReq;
import com.iot.device.vo.req.ServiceModuleReq;
import com.iot.device.vo.rsp.ServiceModuleResp;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 模组表 服务类
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
public interface IServiceModuleService extends IService<ServiceModule> {


    void delServicesByServiceModuleId(Long serviceModuleId);

    ServiceModule addOrUpdateServiceModule(Long serviceModuleId, Long parentServiceModuleId);

    ServiceModule addOrUpdateServiceModule(AddServiceModuleReq moduleReq);

    Long saveOrUpdate(ServiceModuleReq serviceModuleReq);

    void delete(ArrayList<Long> ids);

    PageInfo<ServiceModuleResp> list(ServiceModuleReq serviceModuleReq);

    List<ServiceModuleResp> listByDeviceTypeId(Long deviceTypeId,Long tenantId);

    List<ServiceModuleResp> listByProductId(Long productId);

    Long copyServiceModule(Long serviceModuleId);
}
