package com.iot.building.space.api.fallback;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.iot.building.space.vo.*;
import com.iot.common.beans.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.iot.building.space.api.SpaceApi;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceReq;
import com.iot.control.space.vo.SpaceResp;
import com.iot.control.space.vo.SpaceVo;
import com.iot.device.vo.req.DeviceBusinessTypeIDSwitchReq;

import feign.hystrix.FallbackFactory;
import org.springframework.web.multipart.MultipartFile;

@Component("buildSpaceApiFallbackFactory")
public class SpaceApiFallbackFactory implements FallbackFactory<SpaceApi> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpaceApiFallbackFactory.class);

    @Override
    public SpaceApi create(Throwable cause) {
        return new SpaceApi() {

            @Override
            public List<Long> getAllSpace(SpaceReq spaceReq) {
                return null;
            }

			@Override
            public Integer countOnLightDevice(DeviceBusinessTypeIDSwitchReq req) {
                return null;
            }

            @Override
            public List<SpaceResp> getChildSpace(SpaceReq spaceReq) throws BusinessException {
                return null;
            }

            @Override
            public List<SpaceResp> getSpaceByCondition(SpaceReq spaceReq) throws BusinessException {
                return null;
            }

            @Override
            public List<String> getMountDeviceBySpaceId(SpaceDeviceReq spaceDeviceReq) {
                return null;
            }

            @Override
            public void updateSpaceDevicePosition(SpaceDeviceReq spaceDeviceReq) {

            }

            @Override
            public Integer countBySpaceAndDevice(QueryParamReq queryParamReq) {
                return null;
            }

            @Override
            public int findSpaceCountByParentId(SpaceReq space) {
                return 0;
            }

            @Override
            public boolean checkSpaceName(SpaceReq spaceReq) {
                return false;
            }

            @Override
            public List<SpaceResp> findSpaceByType(SpaceReq spaceReq) {
                LOGGER.error("spaceApi error.", cause);
                return Lists.newArrayList();
            }

            @Override
            public List<SpaceResp> findSpaceUnMount(SpaceReq spaceReq) {
                return null;
            }


            @Override
            public List<LocationResp> findLocationByCondition(LocationReq locationReq) {
                return null;
            }

            @Override
            public void saveLocation(LocationReq locationReq) {

            }

            @Override
            public void updateLocation(LocationReq locationReq) {

            }

            @Override
            public String saveSpaceBackgroundImgImport(SpaceBackgroundImgReq req) {
                return null;
            }

            @Override
            public List<SpaceBackgroundImgResp> getSpaceBackgroundImg(SpaceBackgroundImgReq req) {
                return null;
            }

            @Override
            public Integer updateSpaceBackgroundImg(SpaceBackgroundImgReq req) {
                return null;
            }

			@Override
			public List<Map<String, Object>> findDeviceByRoomAndDeviceBusinessType(QueryParamReq req)
					throws BusinessException {
				return null;
			}

			@Override
			public List<Map<String, Object>> findDeviceByRoomAndDeviceType(QueryParamReq req) throws BusinessException {
				return null;
			}

			@Override
			public Integer countOnDevice(DeviceBusinessTypeIDSwitchReq req) {
				return null;
			}

			@Override
			public int updateSpaceDeviceStatus(SpaceDeviceReq spaceDeviceReq) {
				return 0;
			}

			@Override
			public void mount(SpaceDeviceReq spaceDeviceReq) throws Exception {
			}

			@Override
			public void removeMount(SpaceDeviceReq spaceDeviceReq) throws Exception {
			}

			@Override
			public Boolean groupControl(Map<String, Object> propertyMap) throws BusinessException {
				return null;
			}

			@Override
			public Boolean control(Map<String, Object> propertyMap) throws BusinessException {
				return null;
			}

			@Override
			public void syncSpaceStatus(String deviceId) {
			}

			@Override
			public CommonResponse spaceDataImport(SpaceExcelReq spaceExcelReq) {
				return null;
			}

			@Override
			public List<DeploymentResp> getDeploymentList(DeploymentReq req) {
				return null;
			}

			@Override
			public List<SpaceResp> findBySpaceIds(QueryParamReq req) {
				return null;
			}

			@Override
			public void addOrUpdateCalendar(CalendarReq calendarReq) {

			}

			@Override
			public Page<CalendarResp> findCalendarList(String pageNum, String pageSize, String name) throws BusinessException {
				return null;
			}

			@Override
			public List<CalendarResp> findCalendarListNoPage(String name) throws BusinessException {
				return null;
			}

			@Override
			public void saveOrUpdateDeploy(DeploymentReq req) {

			}

			@Override
			public void deleteBatchDeploy(String deployIds) {

			}

			@Override
			public Page<DeploymentResp> getDeploymentPage(DeploymentReq req) {
				return null;
			}

			@Override
			public List<Long> getLocationTenant() {
				return null;
			}

			@Override
			public void replaceDevice(SpaceDeviceReq spaceDeviceReq) {
			}

			@Override
			public List<Map<String, Object>> getDeviceListBySpace(SpaceDeviceReq spaceDeviceReq) {
				return null;
			}

			@Override
			public Page<SpaceResp> findSpacePageByLocationId(Long locationId, Long orgId, Long tenantId, String name,
					int pageNumber, int pageSize) {
				return null;
			}

			@Override
			public List<SpaceResp> findSpaceByLocationId(Long locationId, Long orgId, Long tenantId, String name) {
				return null;
			}

			@Override
			public List<Map<String, Object>> findTreeSpaceByLocationId(Long tenantId, Long orgId, Long locationId) {
				return null;
			}

			@Override
			public void deleteMountByDeviceIds(Long tenantId, Long orgId, String deviceIds) throws Exception {
				
			}

			@Override
			public List<Map<String, Object>> getFloorAndDeviceCount(Long tenantId, Long orgId, Long buildId,
					String types) throws BusinessException {
				return null;
			}

			@Override
			public List<Map<String, Object>> findDeviceByRoom(Long spaceId, Long orgId, Long tenantId)
					throws BusinessException {
				return null;
			}

			@Override
			public void setSpaceRelation(Long tenantId, Long orgId, Long spaceId, String childIds)
					throws BusinessException {
				
			}

			@Override
			public List<SpaceVo> getChildSpaceStatus(Long tenantId, Long orgId, Long buildId, String type)
					throws BusinessException {
				return null;
			}

			@Override
			public List<Map<String, Object>> getDirectDeviceBySpace(Long tenantId, Long orgId, Long spaceId) {
				return null;
			}

			@Override
			public List<SpaceResp> findSpaceByTenantId(Long tenantId, Long orgId) {
				return null;
			}

			@Override
			public Set<String> countGatewayDeviceOnRoom(Long tenantId, Long orgId, Long spaceId) {
				return null;
			}

			@Override
			public List<Map<String, Object>> getMeetingSpaceTree(Long tenatnId, Long orgId, Long locationId) {
				return null;
			}

			@Override
			public void delLocation(Long tenantId, Long orgId, Long id) {
				
			}

			@Override
			public SpaceBackgroundImgResp getSpaceBackgroundImgById(Long tenantId, Long orgId, Long id) {
				return null;
			}

			@Override
			public Integer deleteSpaceBackgroundImg(Long tenantId, Long orgId, Long id) {
				return null;
			}

			@Override
			public DeploymentResp findDeploymentById(Long tenantId, Long orgId, Long id) {
				return null;
			}

			@Override
			public List<SpaceResp> findSpaceListByDeployId(Long deployId, Long tenantId, Long orgId, Long locationId) {
				return null;
			}

			@Override
			public int getSpaceStatus(Long tenantId, Long orgId, Long spaceId) {
				return 0;
			}

			@Override
			public int setSpaceStatus(Long tenantId, Long orgId, Long spaceId) {
				return 0;
			}

			@Override
			public void deleteCalendar(Long tenantId, Long orgId, Long id) {
				
			}

			@Override
			public CalendarResp getCalendarIofoById(Long tenantId, Long orgId, Long id) {
				return null;
			}

			@Override
			public DeploymentResp findByName(Long orgId,String name) {
				return null;
			}

        };
    }
}
