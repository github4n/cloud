package com.iot.tenant.api.fallback;

import com.iot.common.helper.Page;
import com.iot.tenant.api.VirtualOrgApi;
import com.iot.tenant.vo.req.AddUserOrgReq;
import com.iot.tenant.vo.req.org.GetOrgByPageReq;
import com.iot.tenant.vo.req.org.SaveOrgReq;
import com.iot.tenant.vo.resp.org.OrgResp;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:57 2018/4/26
 * @Modify by:
 */
@Component
public class VirtualOrgApiFallbackFactory implements FallbackFactory<VirtualOrgApi> {

    @Override
    public VirtualOrgApi create(Throwable cause) {
        return new VirtualOrgApi() {


            @Override
            public boolean add(SaveOrgReq req) {
                return false;
            }

            @Override
            public boolean edit(List<SaveOrgReq> req) {
                return false;
            }

            @Override
            public boolean del(List<Long> ids) {
                return false;
            }

            @Override
            public Page<OrgResp> selectByPage(GetOrgByPageReq req) {
                return null;
            }

            @Override
            public List<OrgResp> getChildrenTree(Long parentId) {
                return null;
            }

            @Override
            public List<Long> getChildren(Long parentId) {
                return null;
            }

            @Override
            public Long addUserOrg(AddUserOrgReq req) {
                return null;
            }
        };
    }
}
