package com.iot.shcs.scene.service;

import com.iot.common.beans.CommonResponse;
import com.iot.shcs.scene.vo.req.SceneDetailReq;
import com.iot.shcs.scene.vo.req.SceneDetailReqVo;
import com.iot.shcs.scene.vo.req.SceneReq;
import com.iot.shcs.scene.vo.rsp.SceneDetailResp;
import com.iot.shcs.scene.vo.rsp.SceneDetailVo;
import com.iot.shcs.scene.vo.rsp.SceneResp;

import java.util.List;

/**
 * 项目名称：IOT云平台
 * 模块名称：
 * 功能描述：情景service
 * 创建人： wujianlong
 * 创建时间：2017年11月9日 下午2:56:10
 * 修改人： zhangyue
 * 修改时间：2017年11月9日 下午2:56:10
 */
public interface SceneService {

    /**
     * 保存情景主表
     *
     * @param sceneReq
     * @return
     * @author wanglei
     */
    SceneResp saveScene(SceneReq sceneReq);

    /**
     * 修改情景主表
     *
     * @return
     * @author wanglei
     */
    SceneResp updateScene(SceneReq sceneReq);

    /**
     * 根据 userId 获取 SceneBasicResp 列表
     *
     * @param userId
     * @return
     */
    List<SceneResp> findSceneRespListByUserId(Long userId,Long tenantId);

    List<SceneResp> findSceneRespListBySpaceId(Long spaceId, Long tenantId);

    CommonResponse excScene(Long sceneId, String seq,Long tenantId);



    List<String> findSceneDirectDeviceUuidListBySceneId(Long sceneId,Long tenantId);

    /**
     * 获取设备的clientTopicId
     *
     * @param deviceId
     */
    String getClientTopicId(String deviceId);

    /**
     * 根据sceneId删除情景
     *
     * @param sceneId
     */
    void delSceneBySceneId(Long sceneId,Long tenantId,Long userId);



    SceneResp getSceneById(Long sceneId,Long tenantId);


    List<SceneDetailResp> sceneDetailAndSpaceDeviceList(SceneDetailReq sceneDetailReq);


    void delBleScene(SceneReq sceneReq);

    SceneDetailVo getSceneThen(SceneReq sceneReq);

    void addOrEditAction(SceneDetailReqVo actionReq);

    void delAction(SceneDetailReqVo actionReq);

}