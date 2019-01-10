package com.iot.tenant.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.StringUtil;
import com.iot.redis.RedisCacheUtil;
import com.iot.tenant.api.TenantApi;
import com.iot.tenant.domain.Tenant;
import com.iot.tenant.domain.TenantReviewRecord;
import com.iot.tenant.exception.TenantExceptionEnum;
import com.iot.tenant.service.ITenantReviewRecordService;
import com.iot.tenant.service.ITenantService;
import com.iot.tenant.util.BeanCopyUtil;
import com.iot.tenant.util.RedisKeyUtil;
import com.iot.tenant.vo.req.*;
import com.iot.tenant.vo.resp.AppPackResp;
import com.iot.tenant.vo.resp.TenantInfoResp;
import com.iot.tenant.vo.resp.TenantReviewRecordInfoResp;
import com.iot.util.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 租户表 前端控制器
 * </p>
 *
 * @author lucky
 * @since 2018-04-26
 */
@RestController
public class TenantController implements TenantApi {

    /**随机code位数*/
    private final static int CODE_LENGTH = 6;

    @Autowired
    private ITenantService tenantService;

    @Autowired
    private ITenantReviewRecordService tenantReviewRecordService;

    @Transactional
    public Long save(@RequestBody SaveTenantReq req) {
        AssertUtils.notEmpty(req, "tenant.notnull");
        if (req.getId() != null) {
            //修改
            Tenant tenant = BeanCopyUtil.getTenant(req);
            tenant.setUpdateTime(new Date());
            tenantService.updateById(tenant);
            //删除缓存
            delTenantCache(tenant.getId());
            return tenant.getId();
        } else {
            //新增
//            AssertUtils.notEmpty(req.getName(), "tenant.name.notnull");
//            AssertUtils.notEmpty(req.getCellphone(), "tenant.cellphone.notnull");
            Tenant tenant = BeanCopyUtil.getTenant(req);
            tenant.setCode(this.getRandomTenantCode());
            tenant.setCreateTime(new Date());
            this.tenantService.insert(tenant);
            return tenant.getId();
        }
    }

    /**
     * 描述：生产随机租户Code(如果有重复，将再次执行)
     * @author maochengyuan
     * @created 2018/11/21 20:12
     * @param
     * @return java.lang.String
     */
    public String getRandomTenantCode() {
        String code = StringUtil.getRandomString(CODE_LENGTH, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE);
        Long count = this.tenantService.getTenantCountByCode(code, null);
        if (count > 0) {
            return getRandomTenantCode();
        }
        return code;
    }

    @Override
    public Boolean delete(@RequestBody List<Long> ids) {
        return tenantService.deleteBatchIds(ids);
    }

    @Override
    public Page<TenantInfoResp> list(@RequestBody GetTenantReq req) {
        Integer pageNum = req.getPageNum();
        Integer pageSize = req.getPageSize();
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 1000;
        }
        com.baomidou.mybatisplus.plugins.Page page2 = new com.baomidou.mybatisplus.plugins.Page(pageNum, pageSize);
        EntityWrapper<Tenant> wrapper = new EntityWrapper<>();
        wrapper.like("name", req.getName());
        page2 = tenantService.selectPage(page2, wrapper);
        List<Tenant> list = page2.getRecords();
        Page<TenantInfoResp> page = new Page<TenantInfoResp>();
        List<TenantInfoResp> resList = Lists.newArrayList();
        for (Tenant tenant : list) {
            TenantInfoResp tenantInfoResp = BeanCopyUtil.getTenantInfoResp(tenant);
            resList.add(tenantInfoResp);
        }
        page.setResult(resList);
        page.setTotal(page2.getTotal());
        return page;
    }

    public TenantInfoResp getTenantById(@RequestParam("id") Long id) {
        AssertUtils.notEmpty(id, "tenant.id.notnull");
        TenantInfoResp resp = null;
        Tenant source = getTenantByIdCache(id);
        if (source != null) {
            resp = BeanCopyUtil.getTenantInfoResp(source);
        }
        return resp;
    }

    public List<TenantInfoResp> getTenantByIds(@RequestBody List<Long> ids) {
        return tenantService.getTenantByIds(ids);
//        AssertUtils.notEmpty(ids, "tenant.id.notnull");
//        List<TenantInfoResp> resps = new ArrayList<TenantInfoResp>();
//        List<Tenant> sources = getTenantByIdsCache(ids);
//        for(Tenant source : sources) {
//        	resps.add(BeanCopyUtil.getTenantInfoResp(source));
//        }
//        return resps;
    }

//    /**
//     * 根据租户主键获取
//     *
//     * @param tenantId
//     * @return
//     */
//    private List<Tenant> getTenantByIdsCache(List<Long> tenantIds) {
//    	List<Tenant> tenants = new ArrayList<Tenant>();
//    	//从缓存中获取
//        String key = null;
//        Tenant tenant = null;
//        Collection<Long> selectTenantIds = new ArrayList<Long>();
//    	for(Long tenantId : tenantIds) {
//    		key = RedisKeyUtil.getTenantKey(tenantId);
//            tenant = RedisCacheUtil.valueObjGet(key, Tenant.class);
//            if(tenant != null) {
//            	tenants.add(tenant);
//            }else {
//            	selectTenantIds.add(tenantId);
//            }
//    	}
//        //缓存没有值，则从数据库中获取
//    	String selectKey = null;
//        if (selectTenantIds != null && selectTenantIds.size() > 0) {
//            List<Tenant> selectTenants = tenantService.selectBatchIds(selectTenantIds);
//            for(Tenant selectTenant : selectTenants) {
//            	selectKey = RedisKeyUtil.getTenantKey(selectTenant.getId());
//            	RedisCacheUtil.valueObjSet(selectKey, tenant);
//            }
//            tenants.addAll(selectTenants);
//        }
//        return tenants;
//    }

    @Override
    public TenantInfoResp getTenantByCode(@RequestParam("code") String code) {
        AssertUtils.notEmpty(code, "tenant.code.notnull");
        TenantInfoResp resp = null;
        Tenant source = getTenantByCodeCache(code);
        if (source != null) {
            resp = BeanCopyUtil.getTenantInfoResp(source);
        }
        return resp;
    }

    @Override
    public List<TenantInfoResp> getTenantList() {
        EntityWrapper<Tenant> wrapper = new EntityWrapper<>();
        List<Tenant> list = tenantService.selectList(wrapper);
        List<TenantInfoResp> respList = Lists.newArrayList();
        if (!list.isEmpty()) {
            for (Tenant tenant : list) {
                TenantInfoResp resp = BeanCopyUtil.getTenantInfoResp(tenant);
                respList.add(resp);
            }
        }
        return respList;
    }

    @Override
    public Boolean saveAppPack(@RequestBody SaveAppPackReq req) {
        AssertUtils.notEmpty(req.getTenantCode(), "tenant.code.not.null");
        return tenantService.saveAppPack(req);
    }

    @Override
    public AppPackResp getAppPack(@RequestParam("code") String code) {
        return tenantService.getAppPack(code);
    }


    /////////////////////////////////////// 缓存处理 start//////////////////////////////////////////

    /**
     * 根据租户主键获取
     *
     * @param tenantId
     * @return
     */
    private Tenant getTenantByIdCache(Long tenantId) {
        //从缓存中获取
        String key = RedisKeyUtil.getTenantKey(tenantId);
        Tenant tenant = RedisCacheUtil.valueObjGet(key, Tenant.class);
        //缓存没有值，则从数据库中获取
        if (CommonUtil.isEmpty(tenant)) {
            tenant = tenantService.selectById(tenantId);
            if (CommonUtil.isEmpty(tenant)) {
                return null;
            } else {
                RedisCacheUtil.valueObjSet(key, tenant);
            }
        }
        return tenant;
    }


    /**
     * 根据code获取
     *
     * @param code
     * @return
     */
    private Tenant getTenantByCodeCache(String code) {
        String key = RedisKeyUtil.getTenantCodeKey(code);
        Long tenantId = RedisCacheUtil.valueObjGet(key, Long.class);
        if (tenantId == null) {
            EntityWrapper<Tenant> wrapper = new EntityWrapper<>();
            wrapper.eq("code", code);
            Tenant tenant = tenantService.selectOne(wrapper);
            if (CommonUtil.isEmpty(tenant)) {
                return null;
            } else {
                tenantId = tenant.getId();
                RedisCacheUtil.valueObjSet(key, tenantId);
            }
        }
        return getTenantByIdCache(tenantId);
    }

    /**
     * 删除缓存，用户更新
     *
     * @param tenantId
     */
    private void delTenantCache(Long tenantId) {
        String key = RedisKeyUtil.getTenantKey(tenantId);
        RedisCacheUtil.delete(key);
    }

    /**
     * 删除缓存
     * @param code
     */
    private void delTenantCacheByCode(String code) {
        String key = RedisKeyUtil.getTenantCodeKey(code);
        RedisCacheUtil.delete(key);
    }
    //////////////////////////////////////// 缓存处理 end //////////////////////////////////////////

    /**
     *
     * 描述：租户审核信息列表
     * @author 李帅
     * @created 2018年10月18日 下午7:41:36
     * @since
     * @param req
     * @return
     */
    @Override
    public Page<TenantInfoResp> tenantAuditList(@RequestBody GetAuditTenantReq req) {
        if(req.getAuditStatus() == null) {
            throw new BusinessException(TenantExceptionEnum.AUDITSTATUS_IS_NULL);
        }
        Integer pageNum = req.getPageNum();
        Integer pageSize = req.getPageSize();
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 1000;
        }
        com.baomidou.mybatisplus.plugins.Page page2 = new com.baomidou.mybatisplus.plugins.Page(pageNum, pageSize);
        EntityWrapper<Tenant> wrapper = new EntityWrapper<>();

        wrapper.eq("audit_status", req.getAuditStatus());

        // 企业名
        if(StringUtil.isNotEmpty(req.getTenantName()) && StringUtil.isNotEmpty(req.getTenantName().trim())) {
            wrapper.andNew().like("name", req.getTenantName());
        }
        // 联系人
        if (StringUtil.isNotEmpty(req.getContacts()) && StringUtil.isNotEmpty(req.getContacts().trim())) {
          wrapper.andNew().like("contacts", req.getContacts());
        }
        // 账号
        if (req.getTenantId() != null) {
            wrapper.andNew().eq("id", req.getTenantId());
        }
        wrapper.orderBy(" create_time desc");

        page2 = tenantService.selectPage(page2, wrapper);
        List<Tenant> list = page2.getRecords();
        if(list == null || list.size() == 0) {
        	return new Page<TenantInfoResp>();
        }
        Page<TenantInfoResp> page = new Page<TenantInfoResp>();
        List<TenantInfoResp> resList = Lists.newArrayList();

        ArrayList<Long> tenantIds = new ArrayList<>(list.stream().filter(e -> e.getId() != null).map(e -> e.getId()).collect(Collectors.toSet()));
        List<TenantInfoResp> tenants = tenantService.getTenantByIds(tenantIds);
        Map<Long, TenantInfoResp> tenantMap  = tenants.stream().collect(Collectors.toMap(TenantInfoResp::getId, a -> a, (k1, k2) -> k1));

        for (Tenant tenant : list) {
            TenantInfoResp tenantInfoResp = BeanCopyUtil.getTenantInfoResp(tenant);
            resList.add(tenantInfoResp);
        }

        page.setResult(resList);
        page.setTotal(page2.getTotal());
        return page;
    }

    @Override
    public void saveTenantReviewRecord(@RequestBody SaveTenantReviewRecordReq req) {
//        AssertUtils.notEmpty(req.getTenantCode(), "tenant.code.not.null");
        TenantReviewRecord tenantReviewRecord = new TenantReviewRecord();
        tenantReviewRecord.setTenantId(req.getTenantId());
        tenantReviewRecord.setProcessStatus(req.getProcessStatus());
        tenantReviewRecord.setOperateTime(new Date());
        tenantReviewRecord.setOperateDesc(req.getOperateDesc());
        tenantReviewRecord.setCreateBy(req.getCreateBy());
        tenantReviewRecord.setCreateTime(new Date());
        tenantReviewRecord.setIsDeleted("valid");
        tenantReviewRecordService.insert(tenantReviewRecord);
    }

    @Override
    public List<TenantReviewRecordInfoResp> getTenantReviewRecordByTenantId(@RequestParam("tenantId") Long tenantId) {
        AssertUtils.notEmpty(tenantId, "tenantId.notnull");
        EntityWrapper<TenantReviewRecord> wrapper = new EntityWrapper<>();
        wrapper.eq("tenant_id", tenantId);
        wrapper.orderBy(" create_time desc");
        List<TenantReviewRecord> tenantReviewRecords = tenantReviewRecordService.selectList(wrapper);
        List<TenantReviewRecordInfoResp> resps = null;
        if(tenantReviewRecords != null && tenantReviewRecords.size() > 0) {
            resps = new ArrayList<TenantReviewRecordInfoResp>();
            TenantReviewRecordInfoResp resp = null;
            for(TenantReviewRecord tenantReviewRecord : tenantReviewRecords) {
                resp = new TenantReviewRecordInfoResp();
                resp.setOperateDesc(tenantReviewRecord.getOperateDesc());
                resp.setOperateTime(tenantReviewRecord.getOperateTime());
                resp.setProcessStatus(tenantReviewRecord.getProcessStatus());
                resps.add(resp);
            }
        }
        return resps;
    }

    /**
     * @despriction：通过租户名称查询租户id
     * @author  yeshiyuan
     * @created 2018/10/30 16:55
     * @return
     */
    @Override
    public List<Long> searchTenantIdsByName(@RequestParam("name") String name) {
        return tenantService.searchTenantIdsByName(name);
    }

    /**
     * 描述：更改租户code
     * @author maochengyuan
     * @created 2018/11/21 20:15
     * @param newCode 新租户code
     * @param oldCode 旧租户code
     * @param tenantId 租户id
     * @return long
     */
    @Override
    public void updateTenantCode(String newCode, String oldCode, Long tenantId) {
        if(StringUtil.isEmpty(newCode)){
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "code is null.");
        }
        if(ObjectUtils.isEmpty(tenantId)){
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "tenantId is null.");
        }
        long count = this.tenantService.getTenantCountByCode(newCode, tenantId);
        if(count > 0){
            throw new BusinessException(TenantExceptionEnum.TENANT_CODE_EXIST);
        }
        this.tenantService.updateTenantCode(newCode, tenantId);
        this.delTenantCache(tenantId);
        this.delTenantCacheByCode(oldCode);
    }

}
