package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.device.model.GroupDetail;
import com.iot.device.vo.req.group.UpdateGroupDetailReq;
import com.iot.device.vo.rsp.group.GetGroupDetailResp;

import java.util.List;

/**
 * <p>
 * 组控制详情表 服务类
 * </p>
 *
 * @author chq
 * @since 2018-11-16
 */
public interface IGroupDetailService extends IService<GroupDetail> {

    Long save(UpdateGroupDetailReq param);

    boolean saveBatch(UpdateGroupDetailReq param);

    boolean delByCondition(UpdateGroupDetailReq param);

    List<GetGroupDetailResp> getGroupDetailByDevId(UpdateGroupDetailReq param);

    List<GetGroupDetailResp> getGroupDetailByGroupId(UpdateGroupDetailReq param);

    List<GetGroupDetailResp> getGroupDetail(UpdateGroupDetailReq param);
}
