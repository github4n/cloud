package com.iot.shcs.space.controller;

import com.google.common.collect.Lists;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.RemoteCallBusinessException;
import com.iot.common.helper.Page;
import com.iot.file.api.FileApi;
import com.iot.file.dto.FileDto;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.common.exception.BusinessExceptionEnum;
import com.iot.shcs.security.service.SecurityService;
import com.iot.shcs.security.vo.rsp.SecurityResp;
import com.iot.shcs.space.api.SmarthomeSpaceApi;
import com.iot.shcs.space.enums.SpaceEnum;
import com.iot.shcs.space.exception.SpaceExceptionEnum;
import com.iot.shcs.space.service.ISpaceService;
import com.iot.shcs.space.vo.DeviceVo;
import com.iot.shcs.space.vo.SpaceAndSpaceDeviceVo;
import com.iot.shcs.space.vo.SpacePageResp;
import com.iot.shcs.space.vo.SpaceReq;
import com.iot.shcs.space.vo.SpaceReqVo;
import com.iot.shcs.space.vo.SpaceRespVo;
import com.iot.user.api.UserApi;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class SmarthomeSpaceController implements SmarthomeSpaceApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmarthomeSpaceController.class);

	@Autowired
	private FileApi fileApi;

	@Autowired
	private UserApi userApi;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private ISpaceService spaceService;

	@Override
	public Long save(@RequestBody @Valid SpaceReq spaceReq) {
		return spaceService.save(spaceReq);
	}

	@Override
	public void update(@RequestBody @Valid SpaceReq spaceReq) {
		spaceService.update(spaceReq);
	}

	@Override
    public boolean updateSpaceByCondition(@RequestBody @Valid SpaceReqVo reqVo){
	    return spaceService.updateSpaceByCondition(reqVo);
    }

	@Override
	public boolean deleteSpaceBySpaceId(@RequestParam("tenantId") Long tenantId, @RequestParam("spaceId") Long spaceId) {
		return spaceService.deleteSpaceBySpaceId(tenantId, spaceId);
	}

	@Override
	public boolean deleteSpaceByIds(@RequestBody @Valid SpaceAndSpaceDeviceVo req) {
		return spaceService.deleteSpaceByIds(req);
	}

	@Override
	public com.iot.shcs.space.vo.SpaceResp findSpaceInfoBySpaceId(@RequestParam("tenantId")Long tenantId, @RequestParam("spaceId") Long spaceId) {
		return spaceService.findSpaceInfoBySpaceId(tenantId, spaceId);
	}

	@Override
	public List<com.iot.shcs.space.vo.SpaceResp> findSpaceInfoBySpaceIds(@RequestBody @Valid SpaceAndSpaceDeviceVo req) {
		return spaceService.findSpaceInfoBySpaceIds(req);
	}

	@Override
	public List<com.iot.shcs.space.vo.SpaceResp> findSpaceByParentId(@RequestBody SpaceReq space) {
		return spaceService.findSpaceByParentId(space);
	}

	@Override
	public List<com.iot.shcs.space.vo.SpaceResp> findSpaceByCondition(@RequestBody SpaceReq spaceReq) {
		return spaceService.findSpaceByCondition(spaceReq);
	}

	@Override
	public int countSpaceByCondition(@RequestBody SpaceReq spaceReq) {
		return spaceService.countSpaceByCondition(spaceReq);
	}

	public SpaceRespVo addSpace(@RequestBody @Valid SpaceReq req) {
		SpaceRespVo spaceRespVo = new SpaceRespVo();

		if (SpaceEnum.HOME.getCode().equals(req.getType())) {
			//判断是否已经有默认家
			SpaceReq spaceReq = new SpaceReq();
			spaceReq.setUserId(req.getUserId());
			spaceReq.setDefaultSpace(1); //1标识默认家
			spaceReq.setTenantId(req.getTenantId());
			spaceReq.setType(SpaceEnum.HOME.getCode());
			List<com.iot.shcs.space.vo.SpaceResp> spaceList = spaceService.findSpaceByCondition(spaceReq);
			if (CollectionUtils.isNotEmpty(spaceList)) {
				throw new BusinessException(SpaceExceptionEnum.USER_HOME_IS_EXIST);
			}
		}
		//判断名称是否重复
		boolean flag = spaceService.checkSpaceName(req);

		String type = req.getType();
		if (flag) {//名称重复
			if (SpaceEnum.HOME.getCode().equals(type)) {
				throw new BusinessException(BusinessExceptionEnum.HOME_IS_EXIST);
			} else if (SpaceEnum.ROOM.getCode().equals(type)) {
				throw new BusinessException(BusinessExceptionEnum.ROOM_IS_EXIST);
			}
		}
		req.setCreateBy(req.getUserId());
		req.setCreateTime(new Date());
		Long spaceId = spaceService.save(req);
		if (SpaceEnum.HOME.getCode().equals(type)) {
			spaceRespVo.setHomeId(spaceId + "");
		} else if (SpaceEnum.ROOM.getCode().equals(type)) {
			spaceRespVo.setRoomId(spaceId + "");
		}
		return spaceRespVo;
	}

	public void editSpace(@RequestBody SpaceReq req) {
		boolean flag = spaceService.checkSpaceName(req); //判断名称是否重复
		if (flag) {
			String type = req.getType();
			if (SpaceEnum.HOME.getCode().equals(type)) {
				throw new BusinessException(BusinessExceptionEnum.HOME_IS_EXIST);
			} else if (SpaceEnum.ROOM.getCode().equals(type)) {
				throw new BusinessException(BusinessExceptionEnum.ROOM_IS_EXIST);
			}
		}
		spaceService.update(req);
	}

	public Page<SpacePageResp> getHomePage(@RequestBody SpaceReq req) {
		Page<SpacePageResp> pageResult = new Page<>();
		List<com.iot.shcs.space.vo.SpaceResp> homeList = this.findSpaceByType(req);
		Long totalCount = 0l;
		List<SpacePageResp> resultHomeList = Lists.newArrayList();
		LOGGER.info("*******getHomePage resultHomeList{} ********",resultHomeList);
		if (CollectionUtils.isNotEmpty(homeList)) {
			totalCount = Long.valueOf(homeList.size() + "");
			for (com.iot.shcs.space.vo.SpaceResp home : homeList) {
				SpacePageResp result = new SpacePageResp();
				result.setHomeId(home.getId() != null ? home.getId().toString() : "");
				result.setName(home.getName());
				//获取背景图片
				String icon = home.getIcon();
				if (!icon.contains(".png")) {
					try {
						FileDto fileDto = fileApi.getGetUrl(icon);
						result.setIcon(fileDto.getPresignedUrl());
					} catch (Exception e) {
						LOGGER.warn("get home {} icon error", home.getId(), e);
						//如果file出错，返回默认家图片
						result.setIcon("bg.png");
//						throw new BusinessException(BusinessExceptionEnum.GET_ICOM_ERROR, e);
					}
				} else {
					result.setIcon(icon);
				}
				result.setDefaultHome(home.getDefaultSpace());
				// 是否设置安防密码
				boolean isSecurityPwd = false;
				//判断安防密码是否为空值
				boolean isSecurityPwdNull=true;
				SecurityResp securityResp = securityService.getSecurityRespBySpaceId(home.getId(), req.getTenantId());
				if (securityResp != null) {
					isSecurityPwd = true;
					//判断密码不为空
					String securityPassword=securityResp.getPassword();
					String strNull="";
					MessageDigest digest = null;
					try {
						digest = MessageDigest.getInstance("md5");
						byte[] md5Byte = digest.digest(strNull.getBytes());
						String md5Str=new String(md5Byte);
						if(!md5Str.equals(securityPassword)){
							isSecurityPwdNull=false;
						}
						LOGGER.debug("***md5Str={}, getPassword={}",md5Str,securityPassword);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				result.setIsSecurityPwd(isSecurityPwd);
				result.setMeshName(home.getMeshName());
				result.setMeshPassword(home.getMeshPassword());
				result.setSeq(home.getSeq());
				result.setIsSecurityPwdNull(isSecurityPwdNull);
				resultHomeList.add(result);

			}
		}
		pageResult.setResult(resultHomeList);
		pageResult.setTotal(totalCount);
		return pageResult;
	}

	public Page<SpacePageResp> getRoomPage(@RequestBody SpaceReq req) {
		Page<SpacePageResp> pageResult = new Page<>();
		if (req == null || req.getParentId() == null) {
			return pageResult;
		}
		Long totalCount = 0l;
		List<SpacePageResp> resultRoomList = Lists.newArrayList();
		//先从缓存去取数据，若为空，则从数据库取
		List<com.iot.shcs.space.vo.SpaceResp> roomList = this.findSpaceByParentId(req);

		if (CollectionUtils.isNotEmpty(roomList)) {
			totalCount = Long.valueOf(roomList.size() + "");
			for (com.iot.shcs.space.vo.SpaceResp room : roomList) {
				SpacePageResp result = new SpacePageResp();
				result.setRoomId(room.getId() != null ? room.getId().toString() : "");
				result.setName(room.getName());
				result.setIcon(room.getIcon());
				result.setDevNum(room.getDevNum());
				resultRoomList.add(result);
			}
		}

		pageResult.setResult(resultRoomList);
		pageResult.setTotal(totalCount);
		return pageResult;
	}


	@Override
	public List<com.iot.shcs.space.vo.SpaceResp> findSpaceByType(@RequestBody SpaceReq spaceReq) {
		return spaceService.findSpaceByType(spaceReq);
	}

    @Override
    public void deleteSpaceBySpaceIdAndUserId(@RequestParam("spaceId") Long spaceId,
                                              @RequestParam("userId") Long userId, @RequestParam("tenantId")Long tenantId) {
        spaceService.deleteSpaceBySpaceIdAndUserId(spaceId, userId, tenantId);
    }

    @Override
    public com.iot.shcs.space.vo.SpaceResp findUserDefaultSpace(@RequestParam("userId") Long userId, @RequestParam("tenantId") Long tenantId) {
        return spaceService.findUserDefaultSpace(userId, tenantId);
    }

//	==================================old======================================




	@Override
	public boolean checkSpaceName(@RequestBody SpaceReq spaceReq) {
		return spaceService.checkSpaceName(spaceReq);
	}


	public List<DeviceVo> getUserUnMountDevice(Long tenantId, Long spaceId, Long userId, Integer room){
		return spaceService.getUserUnMountDevice(tenantId, spaceId, userId, room);
	}


	public Integer getDeviceSeqByUserId(Long tenantId, Long userId){
		return spaceService.getDeviceSeqByUserId(tenantId, userId);
	}

	@Override
	public List<String> getDirectDeviceUuidBySpaceId(@RequestParam("tenantId")Long tenantId, @RequestParam("spaceId")Long spaceId){
		return spaceService.getDirectDeviceUuidBySpaceId(tenantId, spaceId);
	}

}
