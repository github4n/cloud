package com.iot.tenant.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.tenant.domain.AppInfo;
import com.iot.tenant.domain.Tenant;
import com.iot.tenant.mapper.TenantMapper;
import com.iot.tenant.service.IAppInfoService;
import com.iot.tenant.service.ITenantService;
import com.iot.tenant.util.BeanCopyUtil;
import com.iot.tenant.util.RedisKeyUtil;
import com.iot.tenant.vo.req.SaveAppPackReq;
import com.iot.tenant.vo.resp.AppPackResp;
import com.iot.tenant.vo.resp.TenantInfoResp;
import com.iot.util.AssertUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 租户表 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-04-26
 */
@Service
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements ITenantService {

//	@Autowired
//    private ITenantService tenantService;

    @Autowired
    private TenantMapper tenantMapper;

    @Autowired
    private IAppInfoService appInfoService;

    @Override
    public Boolean saveAppPack(SaveAppPackReq req) {
        AppInfo appInfo = new AppInfo();
        appInfo.setAppName(req.getAppName());
        appInfo.setAppCode(req.getTenantCode());
        appInfo.setTheme(req.getStyle());
        appInfo.setVerFlag(req.getVerFlag());
        appInfo.setAndroidVer(req.getAndroidVer());
        appInfo.setIosVer(req.getIosVer());
        appInfo.setFileId(req.getFileId());
        appInfo.setTenantId(SaaSContextHolder.currentTenantId());
        if (req.getId() != null && req.getId() != 0) {
            //修改
            appInfoService.updateById(appInfo);
        } else {
            //新增
            appInfo.setCreateTime(new Date());
            appInfoService.insert(appInfo);
        }
        return true;
    }

    @Override
    public AppPackResp getAppPack(String code) {
        EntityWrapper<AppInfo> wrapper = new EntityWrapper<>();
        wrapper.eq("tenant_code", code);
        List<AppInfo> appPackList = appInfoService.selectList(wrapper);
        if (!appPackList.isEmpty()) {
            AppInfo appInfo = appPackList.get(0);
            AppPackResp resp = new AppPackResp();
            BeanUtils.copyProperties(appInfo, resp);
            return resp;
        }
        return null;
    }
    @Override
    public List<TenantInfoResp> getTenantByIds(Collection<Long> ids){
        AssertUtils.notEmpty(ids, "tenant.id.notnull");
        List<TenantInfoResp> resps = new ArrayList<TenantInfoResp>();
        List<Tenant> sources = getTenantByIdsCache(ids);
        for(Tenant source : sources) {
            resps.add(BeanCopyUtil.getTenantInfoResp(source));
        }
        return resps;
    }

    /**
     * 根据租户主键获取
     *
     * @param tenantIds
     * @return
     */
    private List<Tenant> getTenantByIdsCache(Collection<Long> tenantIds) {
        List<Tenant> tenants = new ArrayList<Tenant>();
        //从缓存中获取
        String key = null;
        Tenant tenant = null;
        Collection<Long> selectTenantIds = new ArrayList<Long>();
        for(Long tenantId : tenantIds) {
            key = RedisKeyUtil.getTenantKey(tenantId);
            tenant = RedisCacheUtil.valueObjGet(key, Tenant.class);
            if(tenant != null) {
                tenants.add(tenant);
            }else {
                selectTenantIds.add(tenantId);
            }
        }
        //缓存没有值，则从数据库中获取
        String selectKey = null;
        if (selectTenantIds != null && selectTenantIds.size() > 0) {
            List<Tenant> selectTenants = this.selectBatchIds(selectTenantIds);
            for(Tenant selectTenant : selectTenants) {
                selectKey = RedisKeyUtil.getTenantKey(selectTenant.getId());
                RedisCacheUtil.valueObjSet(selectKey, selectTenant);
            }
            tenants.addAll(selectTenants);
        }
        return tenants;
    }

    /**
     * @despriction：通过租户名称查询租户id
     * @author  yeshiyuan
     * @created 2018/10/30 16:55
     * @return
     */
    @Override
    public List<Long> searchTenantIdsByName(String name) {
        return tenantMapper.searchTenantIdsByName(name);
    }

    /**
     * 描述：查询租户code条数
     * @author maochengyuan
     * @created 2018/11/22 10:57
     * @param code 租户code
     * @param tenantId 租户id
     * @return long
     */
    @Override
    public long getTenantCountByCode(String code, Long tenantId) {
        return this.tenantMapper.getTenantCountByCode(code, tenantId);
    }

    /**
     * 描述：更改租户code
     * @author maochengyuan
     * @created 2018/11/21 20:15
     * @param code 租户code
     * @param tenantId 租户id
     * @return long
     */
    @Override
    public long updateTenantCode(String code, Long tenantId) {
        return this.tenantMapper.updateTenantCode(code, tenantId);
    }

}
