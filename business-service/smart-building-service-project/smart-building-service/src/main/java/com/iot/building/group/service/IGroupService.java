package com.iot.building.group.service;

import java.util.List;

import com.iot.building.group.vo.GroupReq;
import com.iot.building.group.vo.GroupResp;
import com.iot.control.space.vo.SpaceReq;

public interface IGroupService {

    /**
     * 新建或更新分组
     *
     * @return
     * @author wanglei
     */
    Long saveOrUpdate(List<GroupReq> groupReqList);

    /**
     * 根据遥控器ID获取控制的所有市级分组
     * @return
     * @author wanglei
     */
    List<GroupResp> getGroupListByRemoteId(String remoteId);
    
    /**
     * 根据网关ID删除相关分组信息
     * @return
     * @author wanglei
     */
    int delGroupListByGatewayId(String gatewayId);
    
    /**
     * 根据SpaceId初始化Group
     * @return
     * @author linjihuang
     */
    void initGroupBySpaceId(SpaceReq spaceReq);
    
    /**
     * 根据遥控器ID获取分组列表
     * @return
     * @author linjihuang
     */
    List<GroupResp> getGroupVoListByRemoteId(String remoteId);
}
