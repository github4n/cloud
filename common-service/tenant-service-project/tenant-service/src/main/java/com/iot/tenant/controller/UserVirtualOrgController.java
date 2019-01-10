package com.iot.tenant.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.iot.common.exception.BusinessException;
import com.iot.redis.RedisCacheUtil;
import com.iot.tenant.api.UserVirtualOrgApi;
import com.iot.tenant.domain.UserToVirtualOrg;
import com.iot.tenant.domain.VirtualOrg;
import com.iot.tenant.exception.TenantExceptionEnum;
import com.iot.tenant.service.IUserToVirtualOrgService;
import com.iot.tenant.service.IVirtualOrgService;
import com.iot.tenant.util.RedisKeyUtil;
import com.iot.tenant.vo.req.AddUserVirtualOrgReq;
import com.iot.tenant.vo.resp.UserDefaultOrgInfoResp;
import com.iot.tenant.vo.resp.UserOrgInfoResp;
import com.iot.tenant.vo.resp.VirtualOrgResp;
import com.iot.util.AssertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户-组织表 前端控制器
 * </p>
 *
 * @author lucky
 * @since 2018-04-26
 */
@RestController
public class UserVirtualOrgController implements UserVirtualOrgApi {

    public static final Logger LOGGER = LoggerFactory.getLogger(UserVirtualOrgController.class);
    @Autowired
    private IUserToVirtualOrgService userToVirtualOrgService;

    @Autowired
    private IVirtualOrgService virtualOrgService;

    @Transactional
    public Long addUserVirtualOrg(@RequestBody AddUserVirtualOrgReq req) {
        AssertUtils.notEmpty(req, "tenant.user.org.notnull");
        AssertUtils.notEmpty(req.getUserId(), "user.id.notnull");
        AssertUtils.notEmpty(req.getOrgId(), "tenant.org.id.notnull");

        VirtualOrg virtualOrg = virtualOrgService.selectById(req.getOrgId());
        if (virtualOrg != null) {
            EntityWrapper<UserToVirtualOrg> wrapper = new EntityWrapper<>();
            wrapper.eq("user_id", req.getUserId());
            wrapper.eq("org_id", req.getOrgId());
            UserToVirtualOrg source = userToVirtualOrgService.selectOne(wrapper);
            if (source != null) {
                throw new BusinessException(TenantExceptionEnum.USER_VIRTUAL_ORG_EXIST);
            }
            source = new UserToVirtualOrg();
            source.setOrgId(req.getOrgId());
            source.setUserId(req.getUserId());
            source.setDefaultUsed(UserToVirtualOrg.DEFAULT_USED);
            source.setTenantId(virtualOrg.getTenantId()); //添加租户id
            userToVirtualOrgService.insert(source);

            //删除缓存
            delDefaultOrgCache(req.getUserId());
            return source.getId();
        } else {
            return null;
        }
    }

    @Override
    public UserOrgInfoResp getOrgInfoByUserId(@RequestParam("userId") Long userId) {
        AssertUtils.notEmpty(userId, "user.id.notnull");
        return getOrgCache(userId);
    }

    @Override
    public UserDefaultOrgInfoResp getDefaultUsedOrgInfoByUserId(@RequestParam("userId") Long userId) {
        AssertUtils.notEmpty(userId, "user.id.notnull");
        return getDefaultOrgCache(userId);
    }

    @Transactional
    public void deleteOrgByUserIdAndOrgId(@RequestParam("userId") Long userId, @RequestParam("orgId") Long orgId) {
        EntityWrapper<UserToVirtualOrg> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("org_id", orgId);
        UserToVirtualOrg org = userToVirtualOrgService.selectOne(wrapper);
        if (org != null) {
            if (UserToVirtualOrg.DEFAULT_USED.equals(org.getDefaultUsed())) {
                //默认无法删除
                throw new BusinessException(TenantExceptionEnum.USER_VIRTUAL_DEFAULT_USED_NOT_DELETE);
            }
            userToVirtualOrgService.deleteById(org.getId());
            //删除缓存
            delOrgCache(userId);
            return;
        }
        LOGGER.info("select org isnull");
        throw new BusinessException(TenantExceptionEnum.USER_VIRTUAL_ORG_NOT_EXIST);
    }


    ////////////////////////////////////// Redis 操作 start ///////////////////////////////////////////////////

    private UserDefaultOrgInfoResp getDefaultOrgCache(Long userId) {
        String key = RedisKeyUtil.getUserDefaultOrgKey(userId);
        UserDefaultOrgInfoResp orgInfoResp = RedisCacheUtil.valueObjGet(key, UserDefaultOrgInfoResp.class);
        if (orgInfoResp == null) {
            orgInfoResp = new UserDefaultOrgInfoResp();
            orgInfoResp.setUserId(userId);
            EntityWrapper<UserToVirtualOrg> wrapper = new EntityWrapper<>();
            wrapper.eq("user_id", userId);
            wrapper.eq("default_used", UserToVirtualOrg.DEFAULT_USED); //正常情况只有一条数据
            UserToVirtualOrg userToVirtualOrg = userToVirtualOrgService.selectOne(wrapper);
            if (userToVirtualOrg != null) {
                VirtualOrg virtualOrg = virtualOrgService.selectById(userToVirtualOrg.getOrgId());
                orgInfoResp.setId(userToVirtualOrg.getOrgId());
                orgInfoResp.setTenantId(virtualOrg.getTenantId());
                RedisCacheUtil.valueObjSet(key, orgInfoResp);
            }
        }
        return orgInfoResp;
    }

    private UserOrgInfoResp getOrgCache(Long userId) {
        String key = RedisKeyUtil.getUserOrgKey(userId);
        UserOrgInfoResp orgInfoResp = RedisCacheUtil.valueObjGet(key, UserOrgInfoResp.class);
        if (orgInfoResp != null) {
            return orgInfoResp;
        }

        //从数据库中取
        orgInfoResp = new UserOrgInfoResp();
        orgInfoResp.setUserId(userId);
        List<VirtualOrgResp> orgRespList = null;
        EntityWrapper<UserToVirtualOrg> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", userId);
        List<UserToVirtualOrg> orgList = userToVirtualOrgService.selectList(wrapper);

        Long tenantId = null;
        boolean isSelect = false;
        if (!CollectionUtils.isEmpty(orgList)) {
            orgRespList = new ArrayList<>();
            for (UserToVirtualOrg org : orgList) {
                VirtualOrgResp orgResp = new VirtualOrgResp();
                if (isSelect) {
                    VirtualOrg virtualOrg = virtualOrgService.selectById(org.getOrgId());
                    isSelect = true;
                    tenantId = virtualOrg.getTenantId();
                }
                orgResp.setId(org.getOrgId());
                orgResp.setTenantId(tenantId);
                orgRespList.add(orgResp);
            }
            orgInfoResp.setOrgRespList(orgRespList);
        }
        RedisCacheUtil.valueObjSet(key, orgInfoResp);

        return orgInfoResp;
    }

    private void delDefaultOrgCache(Long userId) {
        String key = RedisKeyUtil.getUserDefaultOrgKey(userId);
        RedisCacheUtil.delete(key);
    }

    private void delOrgCache(Long userId) {
        String key = RedisKeyUtil.getUserOrgKey(userId);
        RedisCacheUtil.delete(key);
    }

    ////////////////////////////////////// Redis 操作 end ///////////////////////////////////////////////////
}

