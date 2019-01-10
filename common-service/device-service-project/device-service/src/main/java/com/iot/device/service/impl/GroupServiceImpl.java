package com.iot.device.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.common.exception.BusinessException;
import com.iot.device.core.GroupCacheCoreUtils;
import com.iot.device.exception.GroupExceptionEnum;
import com.iot.device.mapper.GroupMapper;
import com.iot.device.model.Group;
import com.iot.device.service.IGroupDetailService;
import com.iot.device.service.IGroupService;
import com.iot.device.vo.req.group.UpdateGroupDetailReq;
import com.iot.device.vo.req.group.UpdateGroupReq;
import com.iot.device.vo.rsp.group.GetGroupInfoResp;
import com.iot.device.web.utils.GroupCopyUtils;
import com.iot.util.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 组控制表 服务实现类
 * </p>
 *
 * @author chq
 * @since 2018-11-16
 */
@Slf4j
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements IGroupService {

    @Autowired
    private IGroupDetailService groupDetailService;

    @Override
    public Long save(UpdateGroupReq param) {
        AssertUtils.notNull(param, "group.notnull");
        AssertUtils.notNull(param.getCreateBy(), "group.user.notnull");
        Group group = new Group();
        GroupCopyUtils.copyProperties(param, group);
        Long tenantId = group.getTenantId();
        Long userId = group.getCreateBy();
        group.setCreateTime(new Date());
        //删除group相应缓存
        this.delUserCacheGroups(tenantId, userId);
        super.insert(group);
        return group.getId();
    }


    @Override
    public boolean delByCondition(UpdateGroupReq param){
        AssertUtils.notNull(param.getCreateBy(), "group.user.notnull");
        if(param == null){
            log.debug("delete group fail, param is null");
            return false;
        }
        List result = new ArrayList<>();
        List<GetGroupInfoResp> resps = this.getGroup(param);
        if(!CollectionUtils.isEmpty(resps)){
            GetGroupInfoResp groupInfoResp = resps.get(0);
            Long tenantId = groupInfoResp.getTenantId();
            Long userId = groupInfoResp.getCreateBy();
            //删除group相应的缓存
            this.delUserCacheGroups(tenantId, userId);
            resps.forEach(resp->{
                result.add(resp.getId());
            });
            //删除group_detail关联表
            UpdateGroupDetailReq groupDetailReq = new UpdateGroupDetailReq();
            groupDetailReq.setGroupId(param.getId());
            groupDetailReq.setGroupIds(param.getGroupIds());
            groupDetailReq.setTenantId(param.getTenantId());
            groupDetailReq.setCreateBy(param.getCreateBy());
            groupDetailService.delByCondition(groupDetailReq);

            return super.deleteBatchIds(result);
        }
        return false;
    }

    @Override
    public boolean update(UpdateGroupReq param) {
        AssertUtils.notNull(param, "group.notnull");
        AssertUtils.notNull(param.getId(), "groupId.notnull");
        AssertUtils.notNull(param.getUpdateBy(), "group.user.notnull");
        List<GetGroupInfoResp> existGroup = this.getGroup(param);
        if(CollectionUtils.isEmpty(existGroup)){
            log.debug("update group failed , group not exist.");
            throw new BusinessException(GroupExceptionEnum.GROUP_NOT_EXIST);
        }
        Group group = new Group();
        GroupCopyUtils.copyProperties(param, group);
        Long tenantId = group.getTenantId();
        Long userId = group.getUpdateBy();
        group.setUpdateTime(new Date());
        //删除group缓存
        this.delUserCacheGroups(tenantId, userId);
        return super.updateById(group);
    }

    @Override
    public List<GetGroupInfoResp> getGroup(UpdateGroupReq params) {
        List<GetGroupInfoResp> groups = null;
        if(params == null){
            return groups;
        }
        EntityWrapper wrapper = new EntityWrapper();
        if(params.getId() != null){
            wrapper.eq("id", params.getId());
        }
        if(params.getGroupId() != null){
            wrapper.eq("id", params.getGroupId());
        }
        if(!CollectionUtils.isEmpty(params.getGroupIds())){
            wrapper.in("id", params.getGroupIds());
        }
        if(params.getHomeId() != null){
            wrapper.eq("home_id", params.getHomeId());
        }
        if(params.getName() != null){
            wrapper.like("name", params.getName());
        }
        if(params.getDevGroupId() != null){
            wrapper.eq("dev_group_id", params.getDevGroupId());
        }
        if(params.getTenantId() != null){
            wrapper.eq("tenant_id", params.getTenantId());
        }
        if(params.getCreateBy() != null){
            wrapper.eq("create_by", params.getCreateBy());
        }
        if(params.getUpdateBy() != null){
            wrapper.eq("update_by", params.getUpdateBy());
        }
        List<Group> returnGroup = super.selectList(wrapper);
        groups =  GroupCopyUtils.copyProperties(returnGroup);

        return groups;
    }

    @Override
    public List<GetGroupInfoResp> getGroupByUser(UpdateGroupReq params) {
        AssertUtils.notNull(params, "group.notnull");
        AssertUtils.notNull(params.getCreateBy(), "group.userId.notnull");
        List<GetGroupInfoResp> groups = null;
        Long tenantId = params.getTenantId();
        Long userId = params.getCreateBy();
        if (params == null) {
            return groups;
        }
        //获取用户下的groupIds
        List<Map> groupIds = GroupCacheCoreUtils.getCacheGroupIdListByUser(tenantId, userId);
        if (!CollectionUtils.isEmpty(groupIds)) {
            //批量获取group
            groups = GroupCacheCoreUtils.getCacheGroupByGroupId(tenantId, groupIds);

        } else {
            groups = this.getGroup(params);
            log.debug("getGroupByUser result is {}", JSONObject.toJSONString(groups));
            // 批量加入group缓存
            GroupCacheCoreUtils.saveCacheGroups(tenantId, groups);
            // 加入用户groupIds缓存
            GroupCacheCoreUtils.saveCacheGroupIdListByUser(tenantId, userId, groups);
        }

        return groups;
    }

    @Override
    public GetGroupInfoResp getGroupById(UpdateGroupReq params){
        AssertUtils.notNull(params, "group.notnull");
        AssertUtils.notNull(params.getId(), "groupId.notnull");
        Long tenantId = params.getTenantId();
        Long groupId = params.getId();

        Group group = GroupCacheCoreUtils.getCacheGroupByGroupId(tenantId, groupId);
        if(group == null){
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.eq("id", groupId);
            wrapper.eq("tenant_id", tenantId);
            group = super.selectOne(wrapper);
            if(group == null){
                return null;
            }
            GroupCacheCoreUtils.saveCacheGroup(tenantId, groupId, group);
        }
        GetGroupInfoResp resp = new GetGroupInfoResp();
        GroupCopyUtils.copyProperties(group, resp);
        return resp;

    }

    private void delUserCacheGroups(Long tenantId, Long userId){
        //获取用户下的groupIds
        List<Map> groupIds = GroupCacheCoreUtils.getCacheGroupIdListByUser(tenantId, userId);
        //删除用户下的groupIds缓存
        GroupCacheCoreUtils.delCacheGroupIdListByUser(tenantId, userId);
        //删除group信息
        GroupCacheCoreUtils.delCacheGroupByGroupId(tenantId, groupIds);
    }

}
