package com.iot.device.web.utils;

import com.iot.common.beans.BeanUtil;
import com.iot.device.model.Device;
import com.iot.device.model.Group;
import com.iot.device.model.GroupDetail;
import com.iot.device.vo.req.group.UpdateGroupDetailReq;
import com.iot.device.vo.req.group.UpdateGroupReq;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import com.iot.device.vo.rsp.group.GetGroupDetailResp;
import com.iot.device.vo.rsp.group.GetGroupInfoResp;

import java.util.ArrayList;
import java.util.List;


public class GroupCopyUtils {
    public static void copyProperties(UpdateGroupReq res, Group target) {
        if (target == null) {
            return;
        }
        if (res != null) {
            target.setId(res.getId());
            target.setIcon(res.getIcon());
            target.setName(res.getName());
            target.setDevGroupId(res.getDevGroupId());
            target.setHomeId(res.getHomeId());
            target.setTenantId(res.getTenantId());
            target.setCreateBy(res.getCreateBy());
            target.setCreateTime(res.getCreateTime());
            target.setUpdateBy(res.getUpdateBy());
            target.setUpdateTime(res.getUpdateTime());
        }
    }

    public static void copyProperties(Group res, GetGroupInfoResp target) {
        if (target == null) {
            return;
        }
        if (res != null) {

            target.setId(res.getId());
            target.setGroupId(res.getId());
            target.setDevGroupId(res.getDevGroupId());
            target.setIcon(res.getIcon());
            target.setName(res.getName());
            target.setHomeId(res.getHomeId());
            target.setTenantId(res.getTenantId());
            target.setCreateBy(res.getCreateBy());
            target.setCreateTime(res.getCreateTime());
            target.setUpdateBy(res.getUpdateBy());
            target.setUpdateTime(res.getUpdateTime());

        }
    }

    public static List<GetGroupInfoResp> copyProperties(List<Group> groups){
        List<GetGroupInfoResp> resps = new ArrayList<>();
        groups.forEach(group->{
            GetGroupInfoResp resp = new GetGroupInfoResp();
            copyProperties(group, resp);
            resps.add(resp);
        });
        return resps;
    }

    public static void copyProperties(UpdateGroupDetailReq res, GroupDetail target){
        if (target == null) {
            return;
        }
        if (res != null) {
            target.setId(res.getId());
            target.setDeviceId(res.getDeviceId());
            target.setGroupId(res.getGroupId());
            target.setTenantId(res.getTenantId());
            target.setCreateBy(res.getCreateBy());
            target.setCreateTime(res.getCreateTime());
            target.setUpdateBy(res.getUpdateBy());
            target.setUpdateTime(res.getUpdateTime());
        }
    }

    public static void copyProperties(GroupDetail res, GetGroupDetailResp target) {
        if (target == null) {
            return;
        }
        if (res != null) {
            target.setId(res.getId());
            target.setGroupId(res.getGroupId());
            target.setDeviceId(res.getDeviceId());
            target.setTenantId(res.getTenantId());
            target.setCreateBy(res.getCreateBy());
            target.setCreateTime(res.getCreateTime());
            target.setUpdateBy(res.getUpdateBy());
            target.setUpdateTime(res.getUpdateTime());
        }
    }

    public static List<GetGroupDetailResp> copyGroupDetailToResp(List<GroupDetail> groupDetails){
        List<GetGroupDetailResp> resps = new ArrayList<>();
        groupDetails.forEach(group->{
            GetGroupDetailResp resp = new GetGroupDetailResp();
            copyProperties(group, resp);
            resps.add(resp);
        });
        return resps;
    }
}
