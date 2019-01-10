package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.device.model.Group;
import com.iot.device.vo.req.group.UpdateGroupReq;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.group.GetGroupInfoResp;

import java.util.List;

/**
 * <p>
 * 组控制表 服务类
 * </p>
 *
 * @author chq
 * @since 2018-11-16
 */
public interface IGroupService extends IService<Group> {

    Long save(UpdateGroupReq params);

    boolean update(UpdateGroupReq params);

    List<GetGroupInfoResp> getGroup(UpdateGroupReq params);

    List<GetGroupInfoResp> getGroupByUser(UpdateGroupReq params);

    GetGroupInfoResp getGroupById(UpdateGroupReq params);

    boolean delByCondition(UpdateGroupReq params);
}
