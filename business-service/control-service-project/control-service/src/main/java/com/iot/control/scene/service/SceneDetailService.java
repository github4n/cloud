package com.iot.control.scene.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.control.scene.domain.SceneDetail;
import com.iot.control.scene.vo.req.SceneDetailReq;
import com.iot.control.scene.vo.rsp.SceneDetailResp;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2018/5/7 13:55
 * 修改人:
 * 修改时间：
 */
public interface SceneDetailService extends IService<SceneDetail> {

    /**
     * sceneDetail通用查询
     * @param sceneDetailReq
     * @return
     */
    List<SceneDetailResp> sceneDetailByParam(SceneDetailReq sceneDetailReq);

    /**
     * sceneDetail通用查询
     * @param sceneDetailReq
     * @return
     */
    List<SceneDetailResp> sceneDetailByParamDoing(SceneDetailReq sceneDetailReq);


    /**
     * 更新
     * @param sceneDetailReq
     */
    int updateSceneDetail(SceneDetailReq sceneDetailReq);

    /**
     * 保存
     * @param sceneDetail
     */
    void insert(SceneDetailReq sceneDetail);


    /**
     * 删除
     * @param sceneDetailReq
     */
    int deleteSceneDetail(SceneDetailReq sceneDetailReq);


    /**
     * 根据deviceid删除
     * @param sceneDetailReq
     * @return
     */
    int deleteSceneDetailByDeviceId(SceneDetailReq sceneDetailReq);

    /**
     * 详情列表
     * @param sceneDetailReq
     * @return
     */
    List<SceneDetailResp> sceneDetailList(SceneDetailReq sceneDetailReq);

    /**
     * 统计数量
     * @param sceneId
     * @return
     */
     int countChildBySceneId(Long sceneId);

    /**
     *  统计 每个sceneId 下的sceneDetail 数量
     * @param sceneIdList
     * @return
     */
    Map<Long, Integer> countChildBySceneIds(List<Long> sceneIdList);
}
