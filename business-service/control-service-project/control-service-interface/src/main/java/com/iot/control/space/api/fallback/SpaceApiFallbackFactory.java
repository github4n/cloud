package com.iot.control.space.api.fallback;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.control.space.api.SpaceApi;
import com.iot.control.space.vo.*;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class SpaceApiFallbackFactory implements FallbackFactory<SpaceApi> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpaceApiFallbackFactory.class);

    @Override
    public SpaceApi create(Throwable cause) {
        return new SpaceApi() {

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
            public PageInfo findSpacePageByCondition(SpaceReq spaceReq) {
                return null;
            }

            @Override
            public int countSpaceByCondition(SpaceReq spaceReq) {
                return 0;
            }

			@Override
			public List<Map<String, Object>> findTree(Long locationId, Long tenantId) {
				return null;
			}

			@Override
			public List<SpaceResp> findChild(Long tenantId, Long spaceId) {
				return null;
			}

        };
    }
}
