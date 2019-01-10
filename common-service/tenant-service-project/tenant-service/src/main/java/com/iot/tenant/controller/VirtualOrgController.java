package com.iot.tenant.controller;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.exception.BusinessException;
import com.iot.redis.RedisCacheUtil;
import com.iot.tenant.api.VirtualOrgApi;
import com.iot.tenant.domain.Tenant;
import com.iot.tenant.domain.UserToVirtualOrg;
import com.iot.tenant.domain.VirtualOrg;
import com.iot.tenant.exception.TenantExceptionEnum;
import com.iot.tenant.service.ITenantService;
import com.iot.tenant.service.IUserToVirtualOrgService;
import com.iot.tenant.service.IVirtualOrgService;
import com.iot.tenant.util.RedisKeyUtil;
import com.iot.tenant.vo.req.AddUserOrgReq;
import com.iot.tenant.vo.req.AddUserReq;
import com.iot.tenant.vo.req.AddVirtualOrgReq;
import com.iot.tenant.vo.req.org.GetOrgByPageReq;
import com.iot.tenant.vo.req.org.SaveOrgReq;
import com.iot.tenant.vo.resp.org.OrgResp;
import com.iot.util.AssertUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * <p>
 * 租户-组织表 前端控制器
 * </p>
 *
 * @author lucky
 * @since 2018-04-26
 */
@RestController
public class VirtualOrgController implements VirtualOrgApi {


    @Autowired
    private ITenantService tenantService;

    @Autowired
    private IVirtualOrgService virtualOrgService;

    @Autowired
    private IUserToVirtualOrgService userToVirtualOrgService;

    @Override
    public boolean add(@RequestBody SaveOrgReq req) {
        VirtualOrg org = new VirtualOrg();
        BeanUtils.copyProperties(req, org);
        Date now = new Date();
        org.setCreateTime(now);
        org.setUpdateTime(now);
        Boolean flag = false;
        if (virtualOrgService.insert(org)) {
            //path
            VirtualOrg parentOrg = virtualOrgService.selectById(req.getParentId());
            String path = org.getId() + "-";
            if (parentOrg != null) {
                path = parentOrg.getPath() + org.getId() + "-";
            }
            org.setPath(path);
            flag = virtualOrgService.updateById(org);
        }

        return flag;
    }

    @Override
    public boolean edit(@RequestBody List<SaveOrgReq> req) {
        boolean flag = false;
        if (!CollectionUtils.isEmpty(req)) {
            List<VirtualOrg> list = Lists.newArrayList();
            for (SaveOrgReq vo : req) {
                VirtualOrg org = new VirtualOrg();
                BeanUtils.copyProperties(vo, org);
                org.setUpdateTime(new Date());
                list.add(org);
            }
            flag = virtualOrgService.updateBatchById(list);
        }
        return flag;
    }

    @Override
    public boolean del(@RequestBody List<Long> ids) {
        return virtualOrgService.deleteBatchIds(ids);
    }

    @Override
    public com.iot.common.helper.Page<OrgResp> selectByPage(@RequestBody GetOrgByPageReq req) {
        Page<VirtualOrg> page = new Page<>(req.getPageNum(), req.getPageSize());
        VirtualOrg org = new VirtualOrg();
        BeanUtils.copyProperties(req, org);
        EntityWrapper<VirtualOrg> eWrapper = new EntityWrapper<>();
        if (req.getName() != null) {
            eWrapper.like("name", req.getName());
        }
        if (req.getParentId() != null) {
            eWrapper.eq("parent_id", req.getParentId());
        }
        if (req.getType() != null) {
            eWrapper.eq("type", req.getType());
        }
        eWrapper.orderBy("create_time", false);
        Page<VirtualOrg> pageRes = virtualOrgService.selectPage(page, eWrapper);

        com.iot.common.helper.Page<OrgResp> newPage = new com.iot.common.helper.Page<>();
        newPage.setPageNum(req.getPageNum());
        newPage.setPageSize(req.getPageSize());
        newPage.setTotal(pageRes.getTotal());
        List<OrgResp> orgRespList = Lists.newArrayList();
        for (VirtualOrg vo : pageRes.getRecords()) {
            OrgResp orgResp = new OrgResp();
            BeanUtils.copyProperties(vo, orgResp);
            orgRespList.add(orgResp);
        }
        newPage.setResult(orgRespList);
        return newPage;
    }

    @Override
    public List<OrgResp> getChildrenTree(@RequestParam("parentId") Long parentId) {
        List<VirtualOrg> list = getListByParenatId(parentId);
        //添加子节点 封装树
        List<OrgResp> root = Lists.newArrayList();
        Map<Long, OrgResp> tempMap = Maps.newHashMap();
        List<OrgResp> children;
        for (VirtualOrg org : list) {
            children = Lists.newArrayList();
            OrgResp item = new OrgResp();
            BeanUtils.copyProperties(org, item);
            item.setChildren(children);
            tempMap.put(item.getId(), item);
            OrgResp parent = tempMap.get(item.getParentId());
            if (parent != null) {
                parent.getChildren().add(item);
            } else {
                root.add(item);
            }
        }

        Comparator<OrgResp> comparator = new Comparator<OrgResp>() {
            public int compare(OrgResp p1, OrgResp p2) {
                if (p1.getOrderNum() > p2.getOrderNum()) {
                    return 1;
                }
                if (p1.getOrderNum() == p2.getOrderNum()) {
                    //若sort字段相同的话，根据创建时间来进行排序
                    Date d1 = p1.getCreateTime();
                    Date d2 = p2.getCreateTime();
                    if (null == d1 || null == d2) return 0;
                    return d1.compareTo(d2);
                }
                return -1;
            }
        };

        //排序
        sortItem(root, comparator);
        return root;
    }

    @Override
    public List<Long> getChildren(@RequestParam("parentId") Long parentId) {
        List<VirtualOrg> list = getListByParenatId(parentId);
        List<Long> resList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(list)) {
            for (VirtualOrg org : list) {
                resList.add(org.getId());
            }
        }
        return resList;
    }

    /**
     * 获取子组织数据
     *
     * @param parentId
     * @return
     */
    private List<VirtualOrg> getListByParenatId(Long parentId) {
        List<VirtualOrg> list = Lists.newArrayList();

        VirtualOrg virtualOrg = virtualOrgService.selectById(parentId);
        if (virtualOrg != null) {
            String path = virtualOrg.getPath();
            EntityWrapper<VirtualOrg> wrapper = new EntityWrapper<>();
            wrapper.like("path", path, SqlLike.RIGHT);
            wrapper.orderBy("path");
            list = virtualOrgService.selectList(wrapper);
        }

        return list;
    }

    /**
     * 组织树排序
     *
     * @param list
     * @param comparator
     */
    public void sortItem(List<OrgResp> list, Comparator<OrgResp> comparator) {
        if (!CollectionUtils.isEmpty(list)) {
            Collections.sort(list, comparator);
            for (OrgResp item : list) {
                sortItem(item.getChildren(), comparator);
            }
        }
    }

    ////////////////////////////////////////////////// 旧接口 /////////////////////////////////////////////////////////

    @Transactional
    public Long addUserOrg(@RequestBody AddUserOrgReq req) {
        AssertUtils.notNull(req, "tenant.org.notnull");
        AssertUtils.notNull(req.getOrgReq(), "tenant.org.notnull");
        AssertUtils.notNull(req.getOrgReq().getTenantId(), "tenant.id.notnull");
        AssertUtils.notNull(req.getUserReq(), "user.id.notnull");
        AssertUtils.notNull(req.getUserReq().getUserId(), "user.id.notnull");

        Tenant tenant = tenantService.selectById(req.getOrgReq().getTenantId());
        if (tenant == null) {
            throw new BusinessException(TenantExceptionEnum.TENANT_NOT_EXIST);
        }
        //1.save org
        AddVirtualOrgReq orgReq = req.getOrgReq();
        VirtualOrg virtualOrg = new VirtualOrg();
        virtualOrg.setName(orgReq.getName());
        virtualOrg.setTenantId(orgReq.getTenantId());
        virtualOrg.setDescription(orgReq.getDescription());
        virtualOrgService.insert(virtualOrg);

        //2.save user -org
        AddUserReq userReq = req.getUserReq();
        Long userId = userReq.getUserId();
        //2.1 check default used
        EntityWrapper<UserToVirtualOrg> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("default_used", UserToVirtualOrg.DEFAULT_USED);
        UserToVirtualOrg userVirtualOrg = userToVirtualOrgService.selectOne(wrapper);
        Integer defaultUsed = UserToVirtualOrg.DEFAULT_USED;
        if (userVirtualOrg != null) {
            //默认的已经存在  则是受邀请的添加
            defaultUsed = UserToVirtualOrg.USER_BY;
        }
        userVirtualOrg = new UserToVirtualOrg();
        userVirtualOrg.setUserId(userId);
        userVirtualOrg.setOrgId(virtualOrg.getId());
        //2.2 save user - org by default used
        userVirtualOrg.setDefaultUsed(defaultUsed);

        userVirtualOrg.setTenantId(virtualOrg.getTenantId()); //添加租户id

        userToVirtualOrgService.insert(userVirtualOrg);

        //删除缓存
        delOrgCache(userId);
        return virtualOrg.getId();
    }

    private void delOrgCache(Long userId) {
        String key = RedisKeyUtil.getUserOrgKey(userId);
        RedisCacheUtil.delete(key);
    }
}
