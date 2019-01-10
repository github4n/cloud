package com.iot.shcs.scene.service;

import com.iot.shcs.scene.vo.req.SceneDetailReq;
import com.iot.shcs.scene.vo.rsp.SceneDetailResp;

import java.util.List;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2018/5/7 13:55
 * 修改人:
 * 修改时间：
 */
public interface SceneDetailService {

    /**
     * 保存或者更新
     * @param sceneDetailReq
     */
    void insertOrUpdateSceneDetail(SceneDetailReq sceneDetailReq);


    /**
     * 根据sceneId删除情景微调
     * @param sceneId
     * @return
     */
    public void delSceneDetailBySceneId(Long sceneId,Long tenantId,Long userId);

    /**
     * 删除
     * @param sceneId
     * @param deviceId
     * @return
     */
    int delSceneDetailBySceneIdAndDeviceId(Long sceneId, String deviceId,Long tenantId);


    /**
     *
     * @param sceneId
     * @param tenantId
     * @return
     */
    List<SceneDetailResp> findSceneDetailsBySceneId(Long sceneId,Long tenantId);

    /**
     * 删除
     * @param deviceId
     * @param tenantId
     * @return
     */
    public int deleteSceneDetailByDeviceId(String deviceId,Long tenantId);


    void deleteSceneDetailByBatchDeviceIds(Long tenantId, List<String> deviceIds);


}
