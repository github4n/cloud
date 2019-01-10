package com.iot.portal.packagemanager.service;

import com.iot.control.packagemanager.vo.req.scene.SaveSceneInfoReq;
import com.iot.control.packagemanager.vo.req.scene.SceneDetailInfoReq;
import com.iot.control.packagemanager.vo.resp.SceneInfoDetailResp;
import com.iot.control.packagemanager.vo.resp.SceneInfoResp;
import com.iot.portal.packagemanager.vo.resp.SceneListResp;

import java.util.List;

/**
 * @program: cloud
 * @description: 套包场景service
 * @author: yeshiyuan
 * @create: 2018-12-10 14:44
 **/
public interface IPackageSceneService {

    /**
      * @despriction：保存场景配置
      * @author  yeshiyuan
      * @created 2018/12/10 14:49
      */
    void saveSceneConfig(SaveSceneInfoReq saveReq);

    /**
      * @despriction：删除场景
      * @author  yeshiyuan
      * @created 2018/12/10 18:57
      */
    void deleteScene(Long sceneId);

    /**
     *@description 根据套包id，租户id获取场景列表信息
     *@author wucheng
     *@params [packageId, tenantId]
     *@create 2018/12/12 9:38
     *@return java.util.List<com.iot.control.packagemanager.vo.resp.SceneInfoResp>
     */
    List<SceneListResp> getSceneListByPackageIdAndTenantId(Long packageId);

    /**
     *@description 根据场景id获取当前场景的详细信息
     *@author wucheng
     *@params [id]
     *@create 2018/12/12 15:25
     *@return com.iot.control.packagemanager.vo.resp.SceneInfoDetailResp
     */
    SceneInfoDetailResp getSceneInfoDetailById(Long id);
}
