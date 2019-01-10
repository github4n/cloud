package com.iot.shcs.scene.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.iot.control.scene.api.SceneApi;
import com.iot.control.scene.exception.SceneExceptionEnum;
import com.iot.shcs.scene.util.BeanUtils;
import com.iot.shcs.scene.vo.req.SceneDetailReq;
import com.iot.shcs.scene.vo.rsp.SceneDetailResp;
import com.iot.shcs.scene.service.SceneDetailService;
import com.iot.util.AssertUtils;
import io.jsonwebtoken.lang.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

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
public class SceneDetailServiceImpl implements SceneDetailService {

    private static final Logger logger = LoggerFactory.getLogger(SceneDetailServiceImpl.class);

    @Autowired
    private SceneApi sceneApi;

    @Override
    public void insertOrUpdateSceneDetail(SceneDetailReq sceneDetailReq){
        logger.debug("insertOrUpdateSceneDetail {}", JSONObject.toJSONString(sceneDetailReq));
        AssertUtils.notNull(sceneDetailReq, SceneExceptionEnum.PARAM_SCENE_DETAIL_IS_NULL.getMessageKey());
        AssertUtils.notNull(sceneDetailReq.getSceneId(), SceneExceptionEnum.PARAM_SCENE_DETAIL_SCENE_ID_IS_NULL.getMessageKey());
        AssertUtils.notNull(sceneDetailReq.getDeviceId(), SceneExceptionEnum.PARAM_SCENE_DETAIL_DEVICE_ID_IS_NULL.getMessageKey());
        Date currentTime = new Date();

        SceneDetailReq check = new SceneDetailReq();
        check.setSceneId(sceneDetailReq.getSceneId());
        check.setDeviceId(sceneDetailReq.getDeviceId());
        check.setTenantId(sceneDetailReq.getTenantId());
        com.iot.control.scene.vo.req.SceneDetailReq result = new com.iot.control.scene.vo.req.SceneDetailReq();
        BeanUtils.toCopy(check,result);

        List <com.iot.control.scene.vo.rsp.SceneDetailResp> sceneDetailResp = sceneApi.sceneDetailByParamDoing(result);
        logger.debug("insertOrUpdateSceneDetail SceneDetailResp {}", JSONObject.toJSONString(sceneDetailResp.size()));
        if (Collections.isEmpty(sceneDetailResp)) {
            // 新增
            sceneDetailReq.setCreateTime(currentTime);
            sceneDetailReq.setUpdateTime(currentTime);
            BeanUtils.toCopy(sceneDetailReq,result);
            sceneApi.insertSceneDetail(result);
        }else{
            // 编辑
            sceneDetailResp.forEach(m->{
                sceneDetailReq.setId(m.getId());
            });
            sceneDetailReq.setUpdateTime(currentTime);
            BeanUtils.toCopy(sceneDetailReq,result);
            sceneApi.updateSceneDetail(result);
        }
    }

    @Override
    public void delSceneDetailBySceneId(Long sceneId,Long tenantId,Long userId) {
        SceneDetailReq sceneDetailReq = new SceneDetailReq();
        sceneDetailReq.setSceneId(sceneId);
        sceneDetailReq.setTenantId(tenantId);
        sceneDetailReq.setCreateBy(userId);
        com.iot.control.scene.vo.req.SceneDetailReq sceneDetailReqr = new com.iot.control.scene.vo.req.SceneDetailReq();
        BeanUtils.toCopy(sceneDetailReq,sceneDetailReqr);
        sceneApi.deleteSceneDetail(sceneDetailReqr);
    }

    @Override
    public int delSceneDetailBySceneIdAndDeviceId(Long sceneId, String deviceId,Long tenantId){
        SceneDetailReq sceneDetailReq = new SceneDetailReq();
        sceneDetailReq.setSceneId(sceneId);
        sceneDetailReq.setDeviceId(deviceId);
        sceneDetailReq.setTenantId(tenantId);
        com.iot.control.scene.vo.req.SceneDetailReq sceneDetailReqr = new com.iot.control.scene.vo.req.SceneDetailReq();
        BeanUtils.toCopy(sceneDetailReq,sceneDetailReqr);
        int updateCount = sceneApi.deleteSceneDetail(sceneDetailReqr);
        return updateCount;
    }

    @Override
    public List<SceneDetailResp> findSceneDetailsBySceneId(Long sceneId, Long tenantId) {
        SceneDetailReq sceneDetailReq = new SceneDetailReq();
        sceneDetailReq.setSceneId(sceneId);
        sceneDetailReq.setTenantId(tenantId);
        com.iot.control.scene.vo.req.SceneDetailReq sceneDetailReqr = new com.iot.control.scene.vo.req.SceneDetailReq();
        BeanUtils.toCopy(sceneDetailReq,sceneDetailReqr);
        List<com.iot.control.scene.vo.rsp.SceneDetailResp> sceneDetailResps = sceneApi.sceneDetailByParam(sceneDetailReqr);
        List<SceneDetailResp> result = new ArrayList<>();
        BeanUtils.toCopySceneDetailList(sceneDetailResps,result);
        return result;
    }

    @Override
    public int deleteSceneDetailByDeviceId(String deviceId, Long tenantId) {
        SceneDetailReq sceneDetailReq = new SceneDetailReq();
        sceneDetailReq.setDeviceId(deviceId);
        sceneDetailReq.setTenantId(tenantId);
        com.iot.control.scene.vo.req.SceneDetailReq result = new com.iot.control.scene.vo.req.SceneDetailReq();
        BeanUtils.toCopy(sceneDetailReq,result);
        return sceneApi.deleteSceneDetailByDeviceId(result);
    }

    @Override
    public void deleteSceneDetailByBatchDeviceIds(Long tenantId, List<String> deviceIds) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return;
        }
        deviceIds.forEach(deviceId ->{
            this.deleteSceneDetailByDeviceId(deviceId, tenantId);
        });
    }
}
