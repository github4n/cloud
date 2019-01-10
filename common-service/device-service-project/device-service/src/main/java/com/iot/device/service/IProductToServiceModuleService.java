package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.device.model.ProductToServiceModule;
import com.iot.device.vo.req.AddServiceModuleReq;
import com.iot.device.vo.req.ProductToServiceModuleReq;
import com.iot.device.vo.rsp.servicemodule.PackageServiceModuleDetailResp;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 产品对应模组表 服务类
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
public interface IProductToServiceModuleService extends IService<ProductToServiceModule> {

    void addOrUpdateServicesByProductId(Long productId, Long serviceModuleId, Long parentServiceModuleId);

    void addOrUpdateServicesByProductId(Long product, AddServiceModuleReq moduleReq);

    void delServicesByProductId(Long productId);

    void copyService(Long origProductId, Long targetProductId);

    void save(ProductToServiceModuleReq productToServiceModuleReq);

    void saveMore(ProductToServiceModuleReq productToServiceModuleReq);

    void delete(ArrayList<Long> ids);

    /**
     * @despriction：校验产品是否有支持iftttType,并有至少一个iftttType属性、方法、事件
     * @author  yeshiyuan
     * @created 2018/11/22 14:00
     */
    boolean checkProductHadIftttType(Long productId);

    /**
     * @despriction：根据ifttt类型找到对应的模组信息
     * @author  yeshiyuan
     * @created 2018/11/22 15:53
     * @return
     */
    PackageServiceModuleDetailResp queryServiceModuleDetailByIfttt(Long productId,String iftttType);

    List<ProductToServiceModule> listByProductIds(List<Long> productIds);
}
