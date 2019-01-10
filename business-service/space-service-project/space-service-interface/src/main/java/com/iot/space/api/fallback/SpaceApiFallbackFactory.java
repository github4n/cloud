package com.iot.space.api.fallback;

import com.iot.common.exception.BusinessException;
import com.iot.space.api.SpaceApiService;
import com.iot.space.domain.Space;
import com.iot.space.domain.SpaceDevice;
import com.iot.space.domain.SpaceDeviceVO;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SpaceApiFallbackFactory implements FallbackFactory<SpaceApiService> {
    @Override
    public SpaceApiService create(Throwable throwable) {
        return new SpaceApiService() {
            @Override
            public void save(Space space) {

            }

            @Override
            public void update(Space space) {

            }

            @Override
            public void delete(String id) throws BusinessException {

            }

            @Override
            public List<Space> findByLocation(String locationId) {
                return null;
            }

            @Override
            public Space findById(String id) {
                return null;
            }

            @Override
            public List<Space> findSpaceByUser(Map<String, Object> params) {
                return null;
            }

            @Override
            public List<Space> findRootByUser(String locationId) {
                return null;
            }

            @Override
            public int insert(SpaceDevice spaceDevice) {
                return 0;
            }

            @Override
            public int deleteBySpace(SpaceDeviceVO spaceDeviceVO) {
                return 0;
            }

            @Override
            public List<Map<String, Object>> findByParent(String parentId) {
                return null;
            }

            @Override
            public List<Space> findSpaceByParent(String parentId) {
                return null;
            }

            @Override
            public List<Map<String, Object>> findTree(String locationId) {
                return null;
            }

            @Override
            public void mount(String spaceId, String deviceIds, String locationId) {

            }

            @Override
            public Integer countOnDevice(Map<String, Object> params) {
                return null;
            }

            @Override
            public Integer countOnLightDevice(Map<String, Object> params) {
                return null;
            }

            @Override
            public List<String> getAllSpace(Space space) {
                return null;
            }

            @Override
            public List<Space> getAllChildSpace(String spaceId) {
                return null;
            }

            @Override
            public void findSpaceChild(Map<String, List<Space>> map) {

            }

            @Override
            public List<Map<String, Object>> findDeviceByRoom(String roomId) {
                return null;
            }

            @Override
            public List<Map<String, Object>> findDeviceByRoom(String roomId, List<String> deviceCategoryTypes) {
                return null;
            }

            @Override
            public void setSpaceRelation(String spaceId, String childIds) {

            }

            @Override
            public List<Space> getChildSpace(String parentId) {
                return null;
            }

            @Override
            public List<Map<String, Object>> getDirectDeviceBySpace(String spaceId) {
                return null;
            }

            @Override
            public List<Map<String, Object>> findSpaceNameByDevice(String deviceId) {
                return null;
            }

            @Override
            public Space findSpaceNameById(String id) {
                return null;
            }

            @Override
            public int findSpaceMount(SpaceDeviceVO spaceDeviceVO) {
                return 0;
            }

            @Override
            public List<Space> findSpaceUnMount(Map<String, Object> params) {
                return null;
            }

            @Override
            public Integer countSpaceDeviceMount(String spaceId) {
                return null;
            }

            @Override
            public List<Space> findSpaceByParent(Map<String, Object> params) {
                return null;
            }

            @Override
            public void insertSpaceDevice(SpaceDevice record) {

            }

            @Override
            public void deleteBySpace(String spaceId) {

            }

            @Override
            public int judgeSpaceSwitchStatus(List<String> spaceIds) {
                return 0;
            }

            @Override
            public List<Space> getSpaceByBuildAndType(Map<String, Object> map) {
                return null;
            }

            @Override
            public Integer countByGroupIds(Map<String, Object> map) {
                return null;
            }

            @Override
            public Integer countOnSwitchByGroupIds(Map<String, Object> map) {
                return null;
            }

            @Override
            public Integer countLightOnSwitchByGroupIds(Map<String, Object> map) {
                return null;
            }

            @Override
            public List<Map<String, Object>> findGroupIdByRoom(String roomId) {
                return null;
            }

            @Override
            public List<Map<String, Object>> findDeviceByGroup(Map<String, Object> params) {
                return null;
            }

            @Override
            public List<Map<String, Object>> getDeviceIdByTypeAndSpace(Map<String, Object> params) {
                return null;
            }

            @Override
            public List<String> getDeviceIdBySpaceId(String spaceId) {
                return null;
            }

            @Override
            public void updateParentIdNull(String spaceId) {

            }

            @Override
            public void setSpaceRelation(Map<String, Object> map) {

            }

            @Override
            public int updateSpaceDeviceStatus(SpaceDeviceVO spaceDeviceVO) {
                return 0;
            }

            @Override
            public List<SpaceDeviceVO> findSpaceInfo(String status) {
                return null;
            }

            @Override
            public List<Space> findSpaceByType(String type, String userId) {
                return null;
            }

            @Override
            public void deleteSpace(String spaceId, String userId) {

            }

            @Override
            public Space findByIdAuthUserId(String spaceId, String userId) {
                return null;
            }
        };
    }
}
