package com.iot.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.device.core.GroupCacheCoreUtils;
import com.iot.device.mapper.GroupDetailMapper;
import com.iot.device.model.GroupDetail;
import com.iot.device.service.IGroupDetailService;
import com.iot.device.vo.req.group.UpdateGroupDetailReq;
import com.iot.device.vo.rsp.group.GetGroupDetailResp;
import com.iot.device.web.utils.GroupCopyUtils;
import com.iot.util.AssertUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 组控制详情表 服务实现类
 * </p>
 *
 * @author chq
 * @since 2018-11-16
 */
@Service
public class GroupDetailServiceImpl extends ServiceImpl<GroupDetailMapper, GroupDetail> implements IGroupDetailService {

    @Override
    public Long save(UpdateGroupDetailReq param) {
        AssertUtils.notNull(param, "group.notnull");
        AssertUtils.notNull(param.getGroupId(), "groupId.notnull");
        AssertUtils.notEmpty(param.getDeviceId(), "devId.notnull");
        GroupDetail groupDetail = new GroupDetail();
        GroupCopyUtils.copyProperties(param, groupDetail);
        groupDetail.setCreateTime(new Date());
        Long groupId = groupDetail.getGroupId();
        Long tenantId = groupDetail.getTenantId();
        String devId = groupDetail.getDeviceId();
        //删除缓存
        GroupCacheCoreUtils.delCacheGroupDetailByGroupId(tenantId, groupId);
        GroupCacheCoreUtils.delCacheGroupDetailByDevId(tenantId, devId);
        super.insert(groupDetail);
        return groupDetail.getId();
    }

    @Override
    public boolean saveBatch(UpdateGroupDetailReq param) {
        AssertUtils.notNull(param, "groupDetail.notnull");
        AssertUtils.notNull(param.getGroupId(), "groupId.notnull");
        AssertUtils.notEmpty(param.getMembers(), "group.devs.notnull");

        Long tenantId = param.getTenantId();
        Long groupId = param.getGroupId();
        List<GroupDetail> requests = new ArrayList<>();
        List<String> devIds = param.getMembers();
        if(!CollectionUtils.isEmpty(devIds)){
            devIds.forEach(devId->{
                GroupDetail groupDetail = new GroupDetail();
                GroupCopyUtils.copyProperties(param, groupDetail);
                groupDetail.setDeviceId(devId);
                groupDetail.setCreateTime(new Date());
                requests.add(groupDetail);
            });
        }
        //删除缓存
        GroupCacheCoreUtils.delCacheGroupDetailByGroupId(tenantId, groupId);
        GroupCacheCoreUtils.delCacheGroupDetailByDevIds(tenantId, devIds);
        return super.insertBatch(requests);
    }

    @Override
    //可能根据devId、也可能根据groupId 删除groupDetail，
    public boolean delByCondition(UpdateGroupDetailReq param){
        AssertUtils.notNull(param, "groupDetail.notnull");
        List result = new ArrayList<>();
        Long tenantId = param.getTenantId();
        List<GetGroupDetailResp> resps = this.getGroupDetail(param);
        List<Long> groupIds = new ArrayList<>();
        List<String> devIds = new ArrayList<>();
        if(!CollectionUtils.isEmpty(resps)){
            resps.forEach(resp->{
                result.add(resp.getId());
                groupIds.add(resp.getGroupId());
                devIds.add(resp.getDeviceId());
            });

            //删除缓存
            GroupCacheCoreUtils.delCacheGroupDetailByGroupId(tenantId, groupIds);
            GroupCacheCoreUtils.delCacheGroupDetailByDevIds(tenantId, devIds);
            return super.deleteBatchIds(result);
        }
        return false;
    }

    @Override
    public List<GetGroupDetailResp> getGroupDetailByDevId(UpdateGroupDetailReq param){
        AssertUtils.notNull(param, "groupDetail.notnull");
        AssertUtils.notEmpty(param.getDeviceId(), "devId.notnull");
        List<GetGroupDetailResp> resps = new ArrayList<>();
        Long tenantId = param.getTenantId();
        String devId = param.getDeviceId();

        resps = GroupCacheCoreUtils.getCacheGroupDetailByDevId(tenantId, devId);
        if(CollectionUtils.isEmpty(resps)){
            resps = this.getGroupDetail(param);
            //加入缓存
            GroupCacheCoreUtils.saveCacheGroupDetailByDevId(tenantId, devId, resps);
        }
        return resps;
    }

    @Override
    public List<GetGroupDetailResp> getGroupDetailByGroupId(UpdateGroupDetailReq param){
        AssertUtils.notNull(param, "groupDetail.notnull");
        AssertUtils.notNull(param.getGroupId(), "groupId.notnull");
        List<GetGroupDetailResp> resps = new ArrayList<>();
        Long tenantId = param.getTenantId();
        Long groupId = param.getGroupId();

        resps = GroupCacheCoreUtils.getCacheGroupDetailByGroupId(tenantId, groupId);
        if(CollectionUtils.isEmpty(resps)){
            resps = this.getGroupDetail(param);
            //加入缓存
            GroupCacheCoreUtils.saveCacheGroupDetailByGroupId(tenantId, groupId, resps);
        }
        return resps;
    }

    @Override
    public List<GetGroupDetailResp> getGroupDetail(UpdateGroupDetailReq param){
        if(param == null){
            return null;
        }
        EntityWrapper wrapper = new EntityWrapper();
        if(param.getId() != null){
            wrapper.eq("id", param.getId());
        }
        if(param.getGroupId() != null){
            wrapper.eq("group_id", param.getGroupId());
        }
        if (!CollectionUtils.isEmpty(param.getGroupIds())){
            wrapper.in("group_id", param.getGroupIds());
        }
        if(param.getDeviceId() != null){
            wrapper.eq("device_id", param.getDeviceId());
        }
        if(param.getTenantId() != null){
            wrapper.eq("tenant_id", param.getTenantId());
        }
        if(param.getCreateBy() != null){
            wrapper.eq("create_by", param.getCreateBy());
        }
        if(param.getUpdateBy() != null){
            wrapper.eq("update_by", param.getUpdateBy());
        }
        if(param.getMembers() != null){
            wrapper.in("device_id", param.getMembers());
        }
        List<GroupDetail> groupDetails = super.selectList(wrapper);
        return GroupCopyUtils.copyGroupDetailToResp(groupDetails);
    }

}
