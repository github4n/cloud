package com.iot.device.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.iot.common.exception.BusinessException;
import com.iot.device.api.ModuleActionToPropertyApi;
import com.iot.device.api.ServiceModuleApi;
import com.iot.device.core.utils.BeanCopyUtils;
import com.iot.device.exception.ServiceModuleExceptionEnum;
import com.iot.device.model.DeviceTypeToServiceModule;
import com.iot.device.model.ModuleActionToProperty;
import com.iot.device.model.ProductToServiceModule;
import com.iot.device.model.ServiceModule;
import com.iot.device.service.IDeviceTypeToServiceModuleService;
import com.iot.device.service.IModuleActionToPropertyService;
import com.iot.device.service.IProductToServiceModuleService;
import com.iot.device.service.IServiceModuleService;
import com.iot.device.service.core.ModuleCoreService;
import com.iot.device.vo.req.AddOrUpdateServiceModuleReq;
import com.iot.device.vo.req.ModuleActionToPropertyReq;
import com.iot.device.vo.req.ServiceModuleReq;
import com.iot.device.vo.rsp.ModuleActionToPropertyRsp;
import com.iot.device.vo.rsp.ServiceModuleInfoResp;
import com.iot.device.vo.rsp.ServiceModuleListResp;
import com.iot.device.vo.rsp.ServiceModuleResp;
import com.iot.util.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 模组表 前端控制器
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@RestController
public class ModuleActionToPropertyController implements ModuleActionToPropertyApi {

    @Autowired
    private IModuleActionToPropertyService iModuleActionToPropertyService;

    @Override
    public void save(@RequestBody ModuleActionToPropertyReq moduleActionToPropertyReq) {
        iModuleActionToPropertyService.save(moduleActionToPropertyReq);
    }

    @Override
    public void saveMore(@RequestBody ModuleActionToPropertyReq moduleActionToPropertyReq) {
        iModuleActionToPropertyService.saveMore(moduleActionToPropertyReq);
    }

    @Override
    public void delete(@RequestBody ArrayList<Long> ids) {
        iModuleActionToPropertyService.delete(ids);
    }

    @Override
    public List<ModuleActionToPropertyRsp> listByModuleActionIdAndModulePropertyId(@RequestParam("moduleActionId") Long moduleActionId,@RequestParam("modulePropertyId") Long modulePropertyId) {
        return iModuleActionToPropertyService.listByModuleActionIdAndModulePropertyId(moduleActionId,modulePropertyId);
    }
}

