package com.iot.control.share.api.fallback;

import com.iot.control.share.api.ShareSpaceApi;
import com.iot.control.share.vo.req.AddShareSpaceReq;
import com.iot.control.share.vo.resp.ShareSpaceResp;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ShareSpaceApiFallbackFactory implements FallbackFactory<ShareSpaceApi> {

    @Override
    public ShareSpaceApi create(Throwable cause) {
        return new ShareSpaceApi() {
            @Override
            public ShareSpaceResp saveOrUpdate(AddShareSpaceReq params) {
                return null;
            }

            @Override
            public ShareSpaceResp getByShareUUID(Long tenantId, String shareUUID) {
                return null;
            }

            @Override
            public ShareSpaceResp getByToUserId(Long tenantId, Long spaceId, Long toUserId) {
                return null;
            }

            @Override
            public int countByFromUserId(Long tenantId, Long fromUserId) {
                return 0;
            }

            @Override
            public List<ShareSpaceResp> listByFromUserId(Long tenantId, Long fromUserId) {
                return null;
            }

            @Override
            public List<ShareSpaceResp> listBySpaceId(Long tenantId, Long spaceId) {
                return null;
            }

            @Override
            public List<ShareSpaceResp> listByToUserId(Long tenantId, Long toUserId) {
                return null;
            }

            @Override
            public ShareSpaceResp getById(Long tenantId, Long shareId) {
                return null;
            }

            @Override
            public void delById(Long tenantId, Long shareId) {
                return;
            }

            @Override
            public void delBySpaceId(Long tenantId, Long spaceId) {

            }
        };
    }
}
