package com.iot.shcs.template.service;

import com.iot.shcs.template.entity.Package;
import com.baomidou.mybatisplus.service.IService;
import com.iot.shcs.template.vo.InitPackReq;

import java.util.List;

/**
 * <p>
 * 套包表 服务类
 * </p>
 *
 * @author laiguiming
 * @since 2018-10-12
 */
public interface IPackageService extends IService<Package> {

    /**
     * 初始化套包
     *
     * @param req
     */
    void initPack(InitPackReq req);

    Package getByProductId(Long productId, Long tenantId);
}
