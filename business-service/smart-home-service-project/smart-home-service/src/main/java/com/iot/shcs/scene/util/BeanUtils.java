package com.iot.shcs.scene.util;


import com.iot.control.scene.vo.rsp.SceneDetailResp;
import com.iot.control.scene.vo.rsp.SceneResp;
import com.iot.shcs.scene.vo.req.SceneDetailReq;
import com.iot.shcs.scene.vo.req.SceneReq;

import java.util.List;

/**
 * scene转换
 */
public class BeanUtils {

    public static void toCopy(SceneReq sceneReq, com.iot.control.scene.vo.req.SceneReq result){
        result.setId(sceneReq.getId());
        result.setSceneName(sceneReq.getSceneName());
        result.setSpaceId(sceneReq.getSpaceId());
        result.setCreateBy(sceneReq.getCreateBy());
        result.setUpdateBy(sceneReq.getUpdateBy());
        result.setCreateTime(sceneReq.getCreateTime());
        result.setUpdateTime(sceneReq.getUpdateTime());
        result.setTenantId(sceneReq.getTenantId());
        result.setOrgId(sceneReq.getOrgId());
        result.setIcon(sceneReq.getIcon());
        result.setSetType(sceneReq.getSetType());
        result.setSort(sceneReq.getSort());
        result.setUploadStatus(sceneReq.getUploadStatus());
        result.setLocationId(sceneReq.getLocationId());
        result.setTemplateId(sceneReq.getTemplateId());
        result.setDevSceneId(sceneReq.getDevSceneId());
    }

    public static void toCopy(SceneResp sceneResp, com.iot.shcs.scene.vo.rsp.SceneResp result){
        result.setId(sceneResp.getId());
        result.setSceneName(sceneResp.getSceneName());
        result.setSpaceId(sceneResp.getSpaceId());
        result.setCreateBy(sceneResp.getCreateBy());
        result.setUpdateBy(sceneResp.getUpdateBy());
        result.setCreateTime(sceneResp.getCreateTime());
        result.setUpdateTime(sceneResp.getUpdateTime());
        result.setTenantId(sceneResp.getTenantId());
        result.setOrgId(sceneResp.getOrgId());
        result.setIcon(sceneResp.getIcon());
        result.setSetType(sceneResp.getSetType());
        result.setSort(sceneResp.getSort());
        result.setUploadStatus(sceneResp.getUploadStatus());
        result.setLocationId(sceneResp.getLocationId());
        result.setTemplateId(sceneResp.getTemplateId());
        result.setDevSceneId(sceneResp.getDevSceneId());
    }
    public static void toCopySceneList(List<SceneResp> sceneResp, List<com.iot.shcs.scene.vo.rsp.SceneResp> result){
        sceneResp.forEach(m->{
            com.iot.shcs.scene.vo.rsp.SceneResp sceneRespResult = new com.iot.shcs.scene.vo.rsp.SceneResp();
            sceneRespResult.setId(m.getId());
            sceneRespResult.setSceneName(m.getSceneName());
            sceneRespResult.setSpaceId(m.getSpaceId());
            sceneRespResult.setCreateBy(m.getCreateBy());
            sceneRespResult.setUpdateBy(m.getUpdateBy());
            sceneRespResult.setCreateTime(m.getCreateTime());
            sceneRespResult.setUpdateTime(m.getUpdateTime());
            sceneRespResult.setTenantId(m.getTenantId());
            sceneRespResult.setOrgId(m.getOrgId());
            sceneRespResult.setIcon(m.getIcon());
            sceneRespResult.setSetType(m.getSetType());
            sceneRespResult.setSort(m.getSort());
            sceneRespResult.setUploadStatus(m.getUploadStatus());
            sceneRespResult.setLocationId(m.getLocationId());
            sceneRespResult.setTemplateId(m.getTemplateId());
            sceneRespResult.setDevSceneId(m.getDevSceneId());
            sceneRespResult.setIsDirect(m.getIsDirect());
            sceneRespResult.setDirectId(m.getDirectId());
            result.add(sceneRespResult);
        });
    }

    public static void toCopy(SceneDetailReq sceneDetailReq, com.iot.control.scene.vo.req.SceneDetailReq result){
        result.setId(sceneDetailReq.getId());
        result.setSceneId(sceneDetailReq.getSceneId());
        result.setDeviceId(sceneDetailReq.getDeviceId());
        result.setSpaceId(sceneDetailReq.getSpaceId());
        result.setDeviceTypeId(sceneDetailReq.getDeviceTypeId());
        result.setTargetValue(sceneDetailReq.getTargetValue());
        result.setCreateBy(sceneDetailReq.getCreateBy());
        result.setUpdateBy(sceneDetailReq.getUpdateBy());
        result.setCreateTime(sceneDetailReq.getCreateTime());
        result.setUpdateTime(sceneDetailReq.getUpdateTime());
        result.setTenantId(sceneDetailReq.getTenantId());
        result.setSort(sceneDetailReq.getSort());
        result.setLocationId(sceneDetailReq.getLocationId());
        result.setMethod(sceneDetailReq.getMethod());
    }

    public static void toCopySceneDetailList(List<SceneDetailResp> sceneDetailResps, List<com.iot.shcs.scene.vo.rsp.SceneDetailResp> result) {
        sceneDetailResps.forEach(m->{
            com.iot.shcs.scene.vo.rsp.SceneDetailResp sceneDetailResp = new com.iot.shcs.scene.vo.rsp.SceneDetailResp();
            sceneDetailResp.setId(m.getId());
            sceneDetailResp.setSceneId(m.getSceneId());
            sceneDetailResp.setDeviceId(m.getDeviceId());
            sceneDetailResp.setSpaceId(m.getSpaceId());
            sceneDetailResp.setDeviceTypeId(m.getDeviceTypeId());
            sceneDetailResp.setTargetValue(m.getTargetValue());
            sceneDetailResp.setCreateBy(m.getCreateBy());
            sceneDetailResp.setUpdateBy(m.getUpdateBy());
            sceneDetailResp.setCreateTime(m.getCreateTime());
            sceneDetailResp.setUpdateTime(m.getUpdateTime());
            sceneDetailResp.setTenantId(m.getTenantId());
            sceneDetailResp.setSort(m.getSort());
            sceneDetailResp.setLocationId(m.getLocationId());
            sceneDetailResp.setMethod(m.getMethod());
            result.add(sceneDetailResp);
        });


    }


}
