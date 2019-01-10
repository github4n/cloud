package com.iot.shcs.space.api.fallback;

import com.iot.common.helper.Page;
import com.iot.shcs.space.api.SmarthomeSpaceApi;
import com.iot.shcs.space.vo.SpaceAndSpaceDeviceVo;
import com.iot.shcs.space.vo.SpacePageResp;
import com.iot.shcs.space.vo.SpaceReq;
import com.iot.shcs.space.vo.SpaceReqVo;
import com.iot.shcs.space.vo.SpaceResp;
import com.iot.shcs.space.vo.SpaceRespVo;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SmarthomeSpaceApiFallbackFactory implements FallbackFactory<SmarthomeSpaceApi> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmarthomeSpaceApiFallbackFactory.class);

    @Override
    public SmarthomeSpaceApi create(Throwable cause) {
        return new SmarthomeSpaceApi() {


            @Override
            public Long save(SpaceReq spaceReq) {
                return null;
            }

            @Override
            public void update(SpaceReq spaceReq) {

            }

            @Override
            public boolean updateSpaceByCondition(SpaceReqVo reqVo) {
                return false;
            }

            @Override
            public boolean deleteSpaceBySpaceId(Long tenantId, Long spaceId) {
                return false;
            }

            @Override
            public boolean deleteSpaceByIds(SpaceAndSpaceDeviceVo req) {
                return false;
            }

            @Override
            public SpaceResp findSpaceInfoBySpaceId(Long tenantId, Long spaceId) {
                return null;
            }

            @Override
            public List<SpaceResp> findSpaceInfoBySpaceIds(SpaceAndSpaceDeviceVo req) {
                return null;
            }

            @Override
            public List<SpaceResp> findSpaceByParentId(SpaceReq spaceReq) {
                return null;
            }

            @Override
            public List<SpaceResp> findSpaceByCondition(SpaceReq spaceReq) {
                return null;
            }

            @Override
            public int countSpaceByCondition(SpaceReq spaceReq) {
                return 0;
            }

            @Override
            public Page<SpacePageResp> getHomePage(SpaceReq req) {
                return null;
            }

            @Override
            public SpaceRespVo addSpace(SpaceReq req) {
                return null;
            }

            @Override
            public void editSpace(SpaceReq space) {

            }

            @Override
            public void deleteSpaceBySpaceIdAndUserId(Long spaceId, Long userId, Long tenantId) {

            }

            @Override
            public boolean checkSpaceName(SpaceReq spaceReq) {
                return false;
            }

            @Override
            public SpaceResp findUserDefaultSpace(Long userId, Long tenantId) {
                return null;
            }

            @Override
            public List<SpaceResp> findSpaceByType(SpaceReq spaceReq) {
                return null;
            }

            @Override
            public Page<SpacePageResp> getRoomPage(SpaceReq req) {
                return null;
            }

            @Override
            public List<String> getDirectDeviceUuidBySpaceId(Long tenantId, Long spaceId) {
                return null;
            }
        };
    }
}
