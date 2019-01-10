package com.iot.boss.service.packagemanager;

import com.iot.boss.vo.packagemanager.resp.SceneListResp;
import com.iot.control.packagemanager.vo.req.scene.SaveSceneInfoReq;
import com.iot.control.packagemanager.vo.req.scene.SceneDetailInfoReq;
import com.iot.control.packagemanager.vo.resp.SceneInfoDetailResp;
import com.iot.control.scene.vo.rsp.SceneDetailResp;

import java.util.List;

public interface IPackageSceneService {
    /**
     * @despriction：套包下的场景查询
     * @author  nongchongwei
     * @created 2018/11/21 11:01
     * @return
     */
    List<SceneListResp> getSceneList(Long packageId);
    
    /**
     *@description 保存场景信息
     *@author wucheng
     *@params [req]
     *@create 2018/12/11 15:00
     *@return int
     */
    int saveSceneInfo(SaveSceneInfoReq req);
   
    /**
     *@description 批量插入场景信息
     *@author wucheng
     *@params [req]
     *@create 2018/12/7 15:01
     *@return int
     */
    int  batchInsertSceneInfo(List<SaveSceneInfoReq> req);
    /**
     *@description 根据场景id，修改场景信息
     *@author wucheng
     *@params [req]
     *@create 2018/12/7 15:02
     *@return int
     */
    int updateByPrimaryKey(SaveSceneInfoReq req);

    /**
     *@description 根据场景id删除场景信息
     *@author wucheng
     *@params [id]
     *@create 2018/12/13 9:56
     *@return void
     */
    int deleteSceneInfoById(Long id);

    /**
     *@description 根据场景id获取当前场景的详细信息
     *@author wucheng
     *@params [id]
     *@create 2018/12/12 15:25
     *@return com.iot.control.packagemanager.vo.resp.SceneInfoDetailResp
     */
    SceneInfoDetailResp getSceneInfoDetailById(Long id);
}
