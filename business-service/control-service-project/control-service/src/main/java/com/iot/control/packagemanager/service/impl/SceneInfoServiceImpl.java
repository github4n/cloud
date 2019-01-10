package com.iot.control.packagemanager.service.impl;

import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.control.packagemanager.entity.SceneInfo;
import com.iot.control.packagemanager.exception.SceneInfoExceptionEnum;
import com.iot.control.packagemanager.mapper.SceneInfoMapper;
import com.iot.control.packagemanager.service.ISceneInfoService;
import com.iot.control.packagemanager.vo.req.SceneInfoSaveReq;
import com.iot.control.packagemanager.vo.resp.SceneInfoIdAndNameResp;
import com.iot.control.packagemanager.vo.resp.SceneInfoResp;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: nongchongwei
 * @Descrpiton: 场景
 * @Date: 9:21 2018/10/23
 * @Modify by:
 */
@Slf4j
@Service
public class SceneInfoServiceImpl implements ISceneInfoService {

    public static final Logger LOGGER = LoggerFactory.getLogger(SceneInfoServiceImpl.class);

    @Autowired
    private SceneInfoMapper sceneInfoMapper;


    @Override
    public List<SceneInfoResp> getSceneInfoPage(Long packageId,Long tenantId) {
        List<SceneInfoResp> sceneInfoRespList = null;
        try {
            sceneInfoRespList = sceneInfoMapper.selectByPackageId(packageId,tenantId);
        }catch (Exception e){
            LOGGER.error("getSceneInfoPage error", e);
            throw new BusinessException(SceneInfoExceptionEnum.QUERY_ERROR, e);
        }
        return sceneInfoRespList;
    }

    @Override
    public int insertSceneInfo(SceneInfoSaveReq saveSceneInfoReq) {
        return sceneInfoMapper.insert(saveSceneInfoReq);
    }

    @Override
    public int countSceneNumber(Long packageId, Long tenantId) {
        return sceneInfoMapper.countSceneNumber(packageId, tenantId);
    }

    @Override
    public int updateByPrimaryKey(SceneInfoSaveReq sceneInfoSaveReq) {
        return sceneInfoMapper.updateByPrimaryKey(sceneInfoSaveReq);
    }

    /**
     * @despriction：获取场景详情
     * @author  yeshiyuan
     * @created 2018/12/10 17:33
     */
    @Override
    public SceneInfoResp getSceneInfo(Long sceneId) {
        SceneInfoResp sceneInfoResp = new SceneInfoResp();
        SceneInfo sceneInfo = sceneInfoMapper.selectByPrimaryKey(sceneId);
        if (sceneInfoResp!=null) {
            BeanUtil.copyProperties(sceneInfo, sceneInfoResp);
        }
        return sceneInfoResp;
    }

    /**
     * @despriction：删除场景
     * @author  yeshiyuan
     * @created 2018/12/10 18:51
     */
    @Override
    public int deleteByIdAndTenantId(Long sceneId, Long tenantId) {
        return sceneInfoMapper.deleteByIdAndTenantId(sceneId, tenantId);
    }

    /**
     * @despriction：校验场景是否存在
     * @author  yeshiyuan
     * @created 2018/12/10 20:35
     */
    @Override
    public boolean checkExist(List<Long> senceIds, Long tenantId) {
        if (senceIds == null || senceIds.isEmpty()) {
            return false;
        }
        int count = sceneInfoMapper.countByIdAndTenantId(senceIds, tenantId);
        if (senceIds.size() == count) {
            return true;
        } else {
            return false;
        }
    }
    /**
     *@description 根据场景id和租户id查询场景id和名称
     *@author wucheng
     *@params [sceneIds, tenantId]
     *@create 2018/12/13 11:06
     *@return java.util.List<com.iot.control.packagemanager.vo.resp.SceneInfoIdAndNameResp>
     */
    @Override
    public List<SceneInfoIdAndNameResp> getSceneInfoByIds(List<Long> sceneIds, Long tenantId) {
        return sceneInfoMapper.getSceneInfoByIds(sceneIds, tenantId);
    }
}
