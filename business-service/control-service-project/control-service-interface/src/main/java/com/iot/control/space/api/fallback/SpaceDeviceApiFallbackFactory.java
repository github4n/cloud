package com.iot.control.space.api.fallback;

import com.iot.control.space.api.SpaceDeviceApi;
import com.iot.control.space.vo.DelSpaceDeviceReq;
import com.iot.control.space.vo.SpaceAndSpaceDeviceVo;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceDeviceReqVo;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.control.space.vo.SpaceDeviceVo;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description:
 * @program: cloud
 * @return:
 * @author: chq
 * @date: 2018/10/12 11:46
 **/
@Component
public class SpaceDeviceApiFallbackFactory implements FallbackFactory<SpaceDeviceApi> {


    @Override
    public SpaceDeviceApi create(Throwable cause) {
        return new SpaceDeviceApi() {

            @Override
            public boolean inserSpaceDevice(SpaceDeviceReq spaceDeviceReq) {
                return false;
            }

            @Override
            public boolean updateSpaceDevice(SpaceDeviceReq spaceDeviceReq) {
                return false;
            }

            @Override
            public boolean insertOrUpdateSpaceDeviceByDevId(SpaceDeviceReq spaceDeviceReq) {
                return false;
            }

            @Override
            public boolean saveSpaceDeviceList(List<SpaceDeviceReq> spaceDeviceReqs) {
                return false;
            }

            @Override
            public boolean updateSpaceDevices(List<SpaceDeviceReq> spaceDeviceReqs) {
                return false;
            }

            @Override
            public List<SpaceDeviceResp> findSpaceDeviceByCondition(SpaceDeviceReq spaceDeviceReq) {
                return null;
            }

            @Override
            public List<SpaceDeviceVo> findSpaceDeviceVOBySpaceId(Long tenantId, Long spaceId) {
                return null;
            }

            @Override
            public List<SpaceDeviceResp> findSpaceDeviceBySpaceIdsOrDeviceIds(SpaceAndSpaceDeviceVo req) {
                return null;
            }

            @Override
            public int countSpaceDeviceByCondition(SpaceDeviceReq spaceDeviceReq) {
                return 0;
            }

            @Override
            public boolean updateSpaceDeviceByCondition(SpaceDeviceReqVo reqVo) {
                return false;
            }

            @Override
            public int deleteSpaceDeviceByDeviceId(Long tenantId, String deviceId) {
                return 0;
            }

            @Override
            public int deleteSpaceDeviceByBatchDeviceIds(DelSpaceDeviceReq params) {
                return 0;
            }

            @Override
            public boolean deleteSpaceDeviceBySpaceIdsOrDeviceIds(SpaceAndSpaceDeviceVo req) {
                return false;
            }

            @Override
            public List<SpaceDeviceVo> findSpaceDeviceInfoByDeviceIds(SpaceAndSpaceDeviceVo req) {
                return null;
            }


        };
    }
}
