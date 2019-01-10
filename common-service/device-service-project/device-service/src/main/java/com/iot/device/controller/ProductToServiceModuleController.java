package com.iot.device.controller;


import com.iot.device.api.ProductToServiceModuleApi;
import com.iot.device.api.ServiceToActionApi;
import com.iot.device.service.IProductToServiceModuleService;
import com.iot.device.service.IServiceToActionService;
import com.iot.device.vo.req.ProductToServiceModuleReq;
import com.iot.device.vo.req.ServiceToActionReq;
import com.iot.device.vo.rsp.servicemodule.PackageServiceModuleDetailResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * <p>
 * 模组表 前端控制器
 * </p>
 *
 * @author zhangyue
 * @since 2018-06-27
 */
@RestController
public class ProductToServiceModuleController implements ProductToServiceModuleApi {

    @Autowired
    private IProductToServiceModuleService iProductToServiceModuleService;


    @Override
    public void save(@RequestBody ProductToServiceModuleReq productToServiceModuleReq) {
        iProductToServiceModuleService.save(productToServiceModuleReq);
    }

    @Override
    public void saveMore(@RequestBody ProductToServiceModuleReq productToServiceModuleReq) {
        iProductToServiceModuleService.saveMore(productToServiceModuleReq);
    }

    @Override
    public void delete(@RequestBody ArrayList<Long> ids) {
        iProductToServiceModuleService.delete(ids);
    }

    /**
     * @despriction：校验产品是否有支持iftttType,并有至少一个iftttType属性、方法、事件
     * @author  yeshiyuan
     * @created 2018/11/22 14:00
     */
    @Override
    public boolean checkProductHadIftttType(Long productId) {
        return iProductToServiceModuleService.checkProductHadIftttType(productId);
    }

    /**
     * @despriction：根据ifttt类型找到对应的模组信息
     * @author  yeshiyuan
     * @created 2018/11/22 15:53
     * @return
     */
    @Override
    public PackageServiceModuleDetailResp queryServiceModuleDetailByIfttt(@RequestParam("productId") Long productId,
                                                                          @RequestParam("iftttType") String iftttType) {
        return iProductToServiceModuleService.queryServiceModuleDetailByIfttt(productId, iftttType);
    }
}

