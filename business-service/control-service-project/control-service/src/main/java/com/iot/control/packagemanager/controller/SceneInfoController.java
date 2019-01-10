package com.iot.control.packagemanager.controller;


import com.iot.common.exception.BusinessException;
import com.iot.common.util.CommonUtil;
import com.iot.control.packagemanager.api.SceneInfoApi;
import com.iot.control.packagemanager.exception.SceneInfoExceptionEnum;
import com.iot.control.packagemanager.service.ISceneInfoService;
import com.iot.control.packagemanager.vo.req.SceneInfoSaveReq;
import com.iot.control.packagemanager.vo.resp.SceneInfoIdAndNameResp;
import com.iot.control.packagemanager.vo.resp.SceneInfoResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: nongchongwei
 * @Descrpiton: 场景
 * @Date: 9:21 2018/10/23
 * @Modify by:
 */
@RestController
public class SceneInfoController implements SceneInfoApi {

    public static final Logger LOGGER = LoggerFactory.getLogger(SceneInfoController.class);

    @Autowired
    private ISceneInfoService sceneInfoService;

    @Override
    public List<SceneInfoResp> getSceneInfoPage(@RequestParam("packageId") Long packageId, @RequestParam("tenantId") Long tenantId) {
        if(CommonUtil.isEmpty(tenantId)){
            LOGGER.info("getSceneInfoPage param is error,tenantId is empty");
            throw new BusinessException(SceneInfoExceptionEnum.PARAM_IS_ERROR, "tenantId is empty");
        }
        if(CommonUtil.isEmpty(packageId)){
            LOGGER.info("getSceneInfoPage param is error,packageById is empty");
            throw new BusinessException(SceneInfoExceptionEnum.PARAM_IS_ERROR, "packageById is empty");
        }
        return sceneInfoService.getSceneInfoPage(packageId,tenantId);
    }

    @Override
    public int insertSceneInfo(@RequestBody SceneInfoSaveReq record) {
        return sceneInfoService.insertSceneInfo(record);
    }
    
    /**
     *@description  批量插入场景信息
     *@author wucheng
     *@params [record]
     *@create 2018/12/7 14:03
     *@return int  返回插入总记录数
     */
    @Override
    public int batchInsertSceneInfo(@RequestBody List<SceneInfoSaveReq> record) {
        int result = 0;
        if (record != null && record.size() > 0) {
            for (SceneInfoSaveReq t : record )               {
               result += sceneInfoService.insertSceneInfo(t);
            }
        }
        return result;
    }
    /**
     *@description 根据套包id和租户id获取当前套包绑定的场景数量
     *@author wucheng
     *@params [packageId, tenantId]
     *@create 2018/12/7 14:55
     *@return int
     */
    @Override
    public int countSceneNumber(@RequestParam("packageId") Long packageId, @RequestParam("tenantId") Long tenantId) {
        return sceneInfoService.countSceneNumber(packageId, tenantId);
    }
    /**
     *@description 根据场景id，修改场景信息
     *@author wucheng
     *@params [record]
     *@create 2018/12/7 15:00
     *@return int
     */
    @Override
    public int updateByPrimaryKey(@RequestBody SceneInfoSaveReq record) {
        return sceneInfoService.updateByPrimaryKey(record);
    }

    /**
     * @despriction：获取场景详情
     * @author  yeshiyuan
     * @created 2018/12/10 17:33
     */
    @Override
    public SceneInfoResp getSceneInfoById(Long sceneId) {
        return sceneInfoService.getSceneInfo(sceneId);
    }

    /**
     * @despriction：删除场景
     * @author  yeshiyuan
     * @created 2018/12/10 18:51
     */
    @Override
    public int deleteByIdAndTenantId(Long sceneId, Long tenantId) {
        return sceneInfoService.deleteByIdAndTenantId(sceneId, tenantId);
    }

    /**
     * @despriction：校验场景是否存在
     * @author  yeshiyuan
     * @created 2018/12/10 20:35
     */
    @Override
    public boolean checkExist(@RequestParam("sceneIds") List<Long> sceneIds, @RequestParam("tenantId") Long tenantId) {
        return sceneInfoService.checkExist(sceneIds, tenantId);
    }
    /**
     *@description 根据场景id和租户id查询场景id和名称
     *@author wucheng
     *@params [sceneIds, tenantId]
     *@create 2018/12/13 11:06
     *@return java.util.List<com.iot.control.packagemanager.vo.resp.SceneInfoIdAndNameResp>
     */
    @Override
    public List<SceneInfoIdAndNameResp> getSceneInfoByIds(@RequestParam("sceneIds") List<Long> sceneIds, @RequestParam("tenantId") Long tenantId) {
        return sceneInfoService.getSceneInfoByIds(sceneIds, tenantId);
    }
}

