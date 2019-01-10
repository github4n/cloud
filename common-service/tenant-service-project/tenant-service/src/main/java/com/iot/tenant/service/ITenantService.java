package com.iot.tenant.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.tenant.domain.Tenant;
import com.iot.tenant.vo.req.SaveAppPackReq;
import com.iot.tenant.vo.resp.AppPackResp;
import com.iot.tenant.vo.resp.TenantInfoResp;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 租户表 服务类
 * </p>
 *
 * @author lucky
 * @since 2018-04-26
 */
public interface ITenantService extends IService<Tenant> {

    /**
     * 保存app打包配置
     * @param req
     * @return
     */
    Boolean saveAppPack(SaveAppPackReq req);

    /**
     * 根据code获取打包配置
     * @param code
     * @return
     */
    AppPackResp getAppPack(String code);

    List<TenantInfoResp> getTenantByIds(Collection<Long> ids);

    /**
     * @despriction：通过租户名称查询租户id
     * @author  yeshiyuan
     * @created 2018/10/30 16:55
     * @return
     */
    List<Long> searchTenantIdsByName(String name);

    /**
     * 描述：查询租户code条数
     * @author maochengyuan
     * @created 2018/11/22 10:57
     * @param code 租户code
     * @param tenantId 租户id
     * @return long
     */
    long getTenantCountByCode(String code, Long tenantId);

    /**
     * 描述：更改租户code
     * @author maochengyuan
     * @created 2018/11/21 20:15
     * @param code 租户code
     * @param tenantId 租户id
     * @return long
     */
    long updateTenantCode(@Param("code") String code, @Param("tenantId") Long tenantId);

}
