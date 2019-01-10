package com.iot.control.packagemanager.service;

import com.iot.control.packagemanager.vo.req.SceneInfoSaveReq;
import com.iot.control.packagemanager.vo.resp.SceneInfoIdAndNameResp;
import com.iot.control.packagemanager.vo.resp.SceneInfoResp;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 *@description 场景服务类
 *@author nongchongwei
 *@create 2018/11/21 16:59
 */
public interface ISceneInfoService {
    /**
     *@description 根据套包id，租户id获取场景
     *@author wucheng
     *@params [packageId, tenantId]
     *@create 2018/12/12 9:38
     *@return java.util.List<com.iot.control.packagemanager.vo.resp.SceneInfoResp>
     */
    List<SceneInfoResp> getSceneInfoPage(Long packageId, Long tenantId);
    /**
     *@description 插入
     *@author wucheng
     *@params [record]
     *@create 2018/12/7 14:40
     *@return int
     */
    int insertSceneInfo(SceneInfoSaveReq record);
    /**
     *@description 根据套包id和租户id 获取当前套包绑定的场景数量
     *@author wucheng
     *@params [packageId, tenantId]
     *@create 2018/12/7 14:40
     *@return int
     */
    int countSceneNumber(Long packageId, Long tenantId);
    
    /**
     *@description 修改场景
     *@author wucheng
     *@params [record]
     *@create 2018/12/7 14:56
     *@return int
     */
    int updateByPrimaryKey(SceneInfoSaveReq sceneInfoVo);

    /**
     *@description 根据场景id和租户id查询场景id和名称
     *@author wucheng
     *@params [sceneIds, tenantId]
     *@create 2018/12/13 10:51
     *@return java.util.List<com.iot.control.packagemanager.vo.resp.SceneInfoIdAndNameResp>
     */
    List<SceneInfoIdAndNameResp> getSceneInfoByIds(List<Long> sceneIds, Long tenantId);

    /**
     * @despriction：获取场景详情
     * @author  yeshiyuan
     * @created 2018/12/10 17:33
     */
    SceneInfoResp getSceneInfo(Long sceneId);

    /**
     * @despriction：删除场景
     * @author  yeshiyuan
     * @created 2018/12/10 18:51
     */
    int deleteByIdAndTenantId(Long sceneId, Long tenantId);

    /**
     * @despriction：校验场景是否存在
     * @author  yeshiyuan
     * @created 2018/12/10 20:35
     */
    boolean checkExist(List<Long> senceIds, Long tenantId);
}
