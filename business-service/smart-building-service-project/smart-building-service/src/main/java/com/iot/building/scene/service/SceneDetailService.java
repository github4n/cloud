package com.iot.building.scene.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.iot.control.scene.vo.req.SceneDetailReq;
import com.iot.control.scene.vo.rsp.SceneDetailResp;

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
     * 保存情景详情
     * @param userId
     * @param scene
     * @param sceneDetail
     * @return
     */
//	void saveSceneDetailModel(Long userId, Scene scene, SceneDetailReq sceneDetail);

	/**
     * 通过SpaceId查询情景详情列表
     * @param spaceId
     * @return
     */
//	List<SceneDetailResp> findSceneDetailVOListBySpaceId(Long spaceId);
	
	/**
     * 查询情景详情列表
     * @param spaceId
     * @return
     */
//	List<SceneDetailResp> findSceneDetailVOList(SceneDetailReq sceneDetailReq);
	
	/**
     * 更新情景详情通过sceneId和设备类型
     * @param sceneDetail
     * @return
     */
//	int updateSceneDetailBySceneIdAndDeviceTypeId(SceneDetail sceneDetail);
	
	/**
     * 通过sceneId删除情景详情
     *
     * @param sceneId
     * @return
     * @author wanglei
     */
//    public void delSceneDetailBySceneId(Long sceneId);
    
    /**
     * 删除情景详情通过deviceId
     * @param deviceId
     * @return
     */
//	public int deleteSceneDetailByDeviceId(String deviceId);
	
	/**
     * 通过sceneId查询情景详情
     *
     * @param sceneId
     * @return
     */
//    List<SceneDetailResp> findSceneDetailsBySceneId(Long sceneId);
}
