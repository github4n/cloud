package com.iot.building.scene.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

import com.iot.building.scene.vo.req.LocationSceneDetailReq;
import com.iot.building.scene.vo.req.LocationSceneRelationReq;
import com.iot.building.scene.vo.req.LocationSceneReq;
import com.iot.building.scene.vo.req.SceneTemplateManualReq;
import com.iot.building.scene.vo.resp.LocationSceneDetailResp;
import com.iot.building.scene.vo.resp.LocationSceneRelationResp;
import com.iot.building.scene.vo.resp.LocationSceneResp;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.control.scene.vo.req.SceneDetailReq;
import com.iot.control.scene.vo.req.SceneReq;
import com.iot.control.scene.vo.rsp.SceneDetailResp;
import com.iot.control.scene.vo.rsp.SceneResp;

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
     * 保存情景和详情
     * @param
     * @return
     */
	public void saveSceneAndSceneDetail(String deviceTarValues, Long userId, Integer setType, Long locationId);
    
	/**
     * 查询情景详情信息
     * @param sceneDetailReq
     * @return
     */
	public List<SceneDetailResp> findSceneDetailInfo(SceneDetailReq sceneDetailReq);
	
	/**
     * 更新情景详情信息
     * @param
     * @return
     */
	public void updateSceneDetailInfo(Long sceneId, String deviceTarValues, Long userId, Integer setType);
	
	/**
     * 删除情景详情
     * @param sceneId
     * @param spaceId
     * @param userId
     * @return
     */
	public void deleteSceneDetail(Long tenantId, Long sceneId, Long spaceId, Long userId);
	
	/**
     * 情景执行
     * @param sceneId
     * @return
     */
	public void sceneExecute(Long tenantId,Long sceneId);
	
	/**
     * 查询情景详情列表
     * @return
     */
	public List<SceneResp> findSceneDetailList(SceneReq sceneReq);
	
	/**
     * 保存OR更新整校情景
     * @param locationSceneReq
     * @return
     */
	public void saveOrUpdateLocationScene(LocationSceneReq locationSceneReq);
	
	/**
     * 保存整校情景
     * @param locationSceneReq
     * @return
     */
	public Long saveLocationScene(LocationSceneReq locationSceneReq);
	
	/**
     * 查询整校情景列表
     * @param locationSceneReq
     * @return
     */
	public List<LocationSceneResp> findLocationSceneList(LocationSceneReq locationSceneReq);
	
	/**
     * 查询整校情景列表
     * @param locationSceneReq
     * @return
     */
	public Page<LocationSceneResp> findLocationSceneListStr(LocationSceneReq locationSceneReq);
	
	/**
     * 保存OR更新整校情景详情
     * @param locationSceneDetailReq
     * @return
     */
	public void saveOrUpdateLocationSceneDetail(LocationSceneDetailReq locationSceneDetailReq);
	
	/**
     * 查询整校情景详情列表
     * @param locationSceneDetailReq
     * @return
     */
	public List<LocationSceneDetailResp> findLocationSceneDetailList(LocationSceneDetailReq locationSceneDetailReq);
	
	/**
     * 删除整校情景
     * @param id
     * @return
     */
	public void deleteLocationScene(Long id);
	
	/**
     * 删除整校情景详情
     * @param tenantId
     * @param locationSceneId
     * @return
     */
	public void deleteLocationSceneDetail(Long tenantId, Long locationSceneId);
	
	/**
     * 删除整校情景详情
     * @param locationSceneId
     * @return
     */
	public void deleteLocationSceneDetailStr(Long locationSceneId);
	
	/**
     * 更新整校情景
     * @param locationSceneReq
     * @return
     */
	public void updateLocationScene(LocationSceneReq locationSceneReq);
	
	/**
     * 通过整校情景ID获取情景ID列表
     * @param locationSceneId
     * @return
     */
	public List<Long> findSceneIds(Long locationSceneId);
	
	/**
     * 查询树
     * @param locationId
     * @return
     */
	public List<Map<String, Object>> findTree(Long orgId, Long tenantId, Long locationId);
	
	/**
     * 保存整校情景关系
     * @param locationSceneRelationReq
     * @return
     */
	public void saveLocationSceneRelation(LocationSceneRelationReq locationSceneRelationReq);
	
	/**
     * 查询整校情景关系列表
     * @return
     */
	public List<LocationSceneRelationResp> findLocationSceneRelationList();
	
	/**
     * 查询情景列表
     * @param req
     * @return
     */
	public List<SceneResp> querySceneList(@RequestBody SceneDetailReq req);
	
	/**
     * 获取空间情景状态
     * @param spaceId
     * @return
     */
	public Long getSpaceSceneStatus(Long tenantId, Long spaceId);
	
	/**
     * 保存空间情景状态
     * @param sceneId
     * @return
     */
	public Long setSpaceSceneStatus(Long tenantId,Long sceneId);

    /**
    *
    * 描述：执行下一个情景
    * @author wagnlei
    * @created 2017年11月21日 下午5:07:42
    * @since
    * @param spaceId
    * @throws BusinessException
    */
   public void sceneExecuteNext(Long tenantId,Long spaceId) throws BusinessException;
   
   /**
   *
   * 描述：通过房间和情景名字执行情景
   * @author wagnlei
   * @created 2017年11月21日 下午5:07:42
   * @since
   * @param spaceId
   * @param name
   * @throws BusinessException
   */
   public void excuteSceneBySpaceAndSceneName(Long tenantId,Long spaceId, String name);
   
   /**
   *
   * 描述：情景模板手动初始化
   * @author wagnlei
   * @created 2017年11月21日 下午5:07:42
   * @since
   * @throws BusinessException
   */
   public void sceneTemplateManualInit(SceneTemplateManualReq req);
   
   /**
    *
    * 描述：模板手动初始化
    * @author wagnlei
    * @created 2017年11月21日 下午5:07:42
    * @since
    * @throws BusinessException
    */
   public void templateManualInit(SceneTemplateManualReq req);
   
   /**
   *
   * 描述：ifttt 判断情景保存是否同一个网关，true 同一个网关
   * @author wagnlei
   * @created 2017年11月21日 下午5:07:42
   * @since
   * @throws BusinessException
   */
   public Boolean isSingleGateway(List<String> deviceList,List<Long> sceneIds,Long tenantId);

   /**
   *
   * 描述：情景下发
   * @author linjihuang
   * @created 2018年12月03日 下午5:07:42
   * @since
   */
   public void issueScene(SceneReq req);

    List<LocationSceneResp> findLocationSceneListByName(LocationSceneReq req);
}