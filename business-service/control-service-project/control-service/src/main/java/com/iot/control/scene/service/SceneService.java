package com.iot.control.scene.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.control.scene.domain.Scene;
import com.iot.control.scene.vo.req.SceneReq;
import com.iot.control.scene.vo.rsp.SceneResp;

import java.lang.management.LockInfo;
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
public interface SceneService extends IService<Scene> {

    /**
     * 	根据ID获取情景
     *
     * @param sceneReq
     * @return
     */
    SceneResp sceneById(SceneReq sceneReq);

    /**
     * scene通用查询
     * @param sceneReq
     * @return
     */
    List<SceneResp> sceneByParam(SceneReq sceneReq);


    /**
     * scene通用查询(无缓存)
     * @param sceneReq
     * @return
     */
    List<SceneResp> sceneByParamDoing(SceneReq sceneReq);

    /**
     * 更新
     * @param sceneReq
     * @return
     */
    int updateScene(SceneReq sceneReq);

    /**
     * 增加
     * @param sceneReq
     */
    SceneResp insertScene(SceneReq sceneReq);

    /**
     * 删除
     * @param sceneReq
     * @return
     */
    int deleteScene(SceneReq sceneReq);


    /**
     * 查询最大情景
     * @param spaceId
     * @return
     */
    SceneResp maxSortSceneBySpaceId(Long spaceId);


    int countBySceneName(String sceneName, Long userId, Long sceneId);

    List<SceneResp> getSceneByIds(List<Long> ids, Long tenantId, Long orgId);

    String moveSceneSpaceId(String passWord);

}