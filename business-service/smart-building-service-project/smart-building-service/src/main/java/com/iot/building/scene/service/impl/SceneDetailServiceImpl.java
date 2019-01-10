package com.iot.building.scene.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iot.building.scene.service.SceneDetailService;
import com.iot.control.scene.api.SceneApi;
import com.iot.control.scene.vo.req.SceneDetailReq;
import com.iot.control.scene.vo.req.SceneReq;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2018/5/7 13:55
 * 修改人:
 * 修改时间：
 */

@Service
@Transactional
public class SceneDetailServiceImpl implements SceneDetailService {

	@Autowired
	private SceneApi sceneApi;
//	@Override
//    public void saveSceneDetailModel(Long userId, SceneReq scene, SceneDetailReq sceneDetail) {
//        Date currentTime = new Date();
//        sceneDetail.setCreateBy(userId);
//        sceneDetail.setUpdateBy(userId);
//        sceneDetail.setCreateTime(currentTime);
//        sceneDetail.setUpdateTime(currentTime);
//        sceneDetail.setSceneId(scene.getId());
//        sceneDetail.setTenantId(scene.getTenantId());
//        sceneDetail.setLocationId(scene.getLocationId());
//        sceneApi.insertSceneDetail(sceneDetail);
//    }

//	@Override
//    public List<SceneDetailResp> findSceneDetailVOListBySpaceId(Long spaceId){
//        return sceneApi.findSceneDetailVOListBySpaceId(spaceId);
//    }
//
//	@Override
//	public List<SceneDetailResp> findSceneDetailVOList(SceneDetailReq sceneDetailReq){
//        return sceneDetailMapper.findSceneDetailVOList(sceneDetailReq);
//    }

//	@Override
//	public int updateSceneDetailBySceneIdAndDeviceTypeId(SceneDetail sceneDetail){
//        return sceneDetailMapper.updateSceneDetailBySceneIdAndDeviceTypeId(sceneDetail);
//    }

//	@Override
//	public void delSceneDetailBySceneId(Long sceneId) {
//		SceneDetailReq sceneDetailReq = new SceneDetailReq();
//		sceneDetailReq.setSceneId(sceneId);
//        sceneApi.deleteSceneDetail(sceneDetailReq);
//        // 移除缓存
//        RedisCacheUtil.delete(RedisKeyUtil.getSceneDetailListIdKey(sceneId));
//    }

//    @Override
//    public List<SceneDetailResp> findSceneDetailsBySceneId(Long sceneId) {
//        String cacheKey = RedisKeyUtil.getSceneDetailListIdKey(sceneId);
//
//        // 优先从缓存取
//        List<SceneDetailResp> sceneDetailList = RedisCacheUtil.listGetAll(cacheKey, SceneDetailResp.class);
//        if (CollectionUtils.isEmpty(sceneDetailList)) {
//            SceneDetailReq sceneDetailReq = new SceneDetailReq();
//            sceneDetailReq.setSceneId(sceneId);
//            sceneDetailList = sceneApi.sceneDetailByParam(sceneDetailReq);
//            if (CollectionUtils.isNotEmpty(sceneDetailList)) {
//                // 加入缓存
//                RedisCacheUtil.listSet(cacheKey, sceneDetailList);
//                RedisCacheUtil.expireKey(cacheKey, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT);
//            }
//        }
//
//        return sceneDetailList;
//    }

//	@Override
//	public int deleteSceneDetailByDeviceId(String deviceId) {
//        if (deviceId == null) {
//            throw new BusinessException(BusinessExceptionEnum.PARAMETER_ILLEGALITY);
//        }
//
//        try {
//            return sceneDetailMapper.delSceneDetailByDeviceId(deviceId);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }

//	@Override
//	public List<SceneDetailResp> findSceneDetailsBySceneId(Long sceneId) {
//        String cacheKey = RedisKeyUtil.getSceneDetailListIdKey(sceneId);
//
//        // 优先从缓存取
//        List<SceneDetailResp> sceneDetailList = RedisCacheUtil.listGetAll(cacheKey, SceneDetailResp.class);
//        if (CollectionUtils.isEmpty(sceneDetailList)) {
//            sceneDetailList = sceneDetailMapper.findSceneDetailsBySceneId(sceneId);
//            if (CollectionUtils.isNotEmpty(sceneDetailList)) {
//                // 加入缓存
//                RedisCacheUtil.listSet(cacheKey, sceneDetailList);
//                RedisCacheUtil.expireKey(cacheKey, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT);
//            }
//        }
//
//        return sceneDetailList;
//    }

}
