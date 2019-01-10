package com.iot.device.api.fallback;

import com.iot.device.api.GroupApi;
import com.iot.device.vo.req.group.UpdateGroupDetailReq;
import com.iot.device.vo.req.group.UpdateGroupReq;
import com.iot.device.vo.rsp.group.GetGroupDetailResp;
import com.iot.device.vo.rsp.group.GetGroupInfoResp;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.acl.Group;
import java.util.List;

/**
 * @description:
 * @program: cloud
 * @return:
 * @author: chq
 * @date: 2018/11/16 15:44
 **/
@Slf4j
@Component
public class GroupApiFallbackFactory implements FallbackFactory<GroupApi> {

    @Override
    public GroupApi create(Throwable cause) {
        return new GroupApi() {

            @Override
            public Long save(UpdateGroupReq params) {
                return null;
            }

            @Override
            public boolean update(UpdateGroupReq params) {
                return false;
            }

            @Override
            public List<GetGroupInfoResp> getGroupByUser(UpdateGroupReq params) {
                return null;
            }

            @Override
            public GetGroupInfoResp getGroupById(UpdateGroupReq params) {
                return null;
            }

            @Override
            public List<GetGroupInfoResp> getGroup(UpdateGroupReq params) {
                return null;
            }

            @Override
            public boolean delGroupById(UpdateGroupReq params) {
                return false;
            }

            @Override
            public Long saveGroupDetial(UpdateGroupDetailReq params) {
                return null;
            }

            @Override
            public boolean saveGroupDetialBatch(UpdateGroupDetailReq params) {
                return false;
            }

            @Override
            public boolean delGroupDetial(UpdateGroupDetailReq params) {
                return false;
            }

            @Override
            public List<GetGroupDetailResp> getGroupDetailByDevId(UpdateGroupDetailReq param) {
                return null;
            }

            @Override
            public List<GetGroupDetailResp> getGroupDetailByGroupId(UpdateGroupDetailReq param) {
                return null;
            }

            @Override
            public List<GetGroupDetailResp> getGroupDetial(UpdateGroupDetailReq params) {
                return null;
            }
        };
    }
}
