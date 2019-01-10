package com.iot.favorite.service.impl;

import com.alibaba.fastjson.JSON;
import com.iot.common.beans.CommonResponse;
import com.iot.control.favorite.api.FavoriteApi;
import com.iot.control.favorite.vo.FavoriteReq;
import com.iot.control.favorite.vo.FavoriteResp;
import com.iot.control.scene.api.SceneApi;
import com.iot.control.scene.vo.req.GetSceneReq;
import com.iot.control.scene.vo.rsp.SceneResp;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.vo.req.device.ListDeviceInfoReq;
import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import com.iot.favorite.service.FavoriteService;
import com.iot.util.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FavoriteServiceImpl implements FavoriteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FavoriteServiceImpl.class);
    @Autowired
    private FavoriteApi favoriteApi;

    @Autowired
    private DeviceCoreApi deviceCoreApi;

    @Autowired
    private SceneApi sceneApi;

    @Override
    public Map<String, Object> getFavoriteList(Long tenantId, Long userId,Long homeId) {

        AssertUtils.notNull(tenantId, "tenantId.notnull");
        AssertUtils.notNull(userId, "userId.notnull");

        Map<String, Object> payload=new HashMap<>();
        //获取favorite表中的内容
        List<FavoriteResp> respList=favoriteApi.getFavoriteList(tenantId,userId,homeId);

        log.info("*****getFavoriteList respList={}", JSON.toJSONString(respList));
        if(CollectionUtils.isEmpty(respList)){
            return payload;
        }
        //存储获取的设备和scene列表
        List<ListDeviceInfoRespVo> devList=new ArrayList<>();
        List<SceneResp> sceneList=new ArrayList<>();

        List<String> deviceIds=new ArrayList<>();
        List<Long> sceneIds=new ArrayList<>();

        for (FavoriteResp resp:respList){
            Integer typeId=resp.getTypeId();
            switch (typeId){
                case 1:  //1表示存储的为deviceUuid
                    deviceIds.add(resp.getDevScene());
                    break;
                case 2:  //2表示存储的为sceneId
                    try {
                        Long id=Long.parseLong(resp.getDevScene());
                        sceneIds.add(id);
                    }catch (Exception e){
                        e.printStackTrace();
                        LOGGER.error("getFavoriteList erro");
                    }
                    break;
            }
        }
        //通过devIds批量查找设备信息
        if(!CollectionUtils.isEmpty(deviceIds)){
            ListDeviceInfoReq listDeviceInfoReq=new ListDeviceInfoReq();
            listDeviceInfoReq.setDeviceIds(deviceIds);
            devList=deviceCoreApi.listDevices(listDeviceInfoReq);
        }
        //通过seceneIds批量查找scene信息
        if(!CollectionUtils.isEmpty(sceneIds)){
            GetSceneReq getSceneReq=new GetSceneReq();
            getSceneReq.setSceneIds(sceneIds);
            getSceneReq.setTenantId(tenantId);
            sceneList=sceneApi.getSceneByIds(getSceneReq);
        }

        payload.put("dev",devList);
        payload.put("scenes",sceneList);
        return payload;
    }

    @Override
    public void addFavorite(FavoriteReq req) {

        AssertUtils.notNull(req.getSpaceId(), "spaceId.notnull");
        AssertUtils.notNull(req.getDevScene(), "devScene.notnull");
        AssertUtils.notNull(req.getUserId(), "userId.notnull");
        AssertUtils.notNull(req.getTypeId(), "typeId.notnull");
        AssertUtils.notNull(req.getTenantId(), "tenantId.notnull");
        AssertUtils.notNull(req.getCreateBy(), "createBy.notnull");
        AssertUtils.notNull(req.getUpdateBy(), "updateBy.notnull");

        favoriteApi.insertFavorite(req);
    }

    @Override
    public Boolean getFavoriteStatus(FavoriteReq req) {
        AssertUtils.notNull(req.getSpaceId(), "spaceId.notnull");
        AssertUtils.notNull(req.getDevScene(), "devScene.notnull");
        AssertUtils.notNull(req.getUserId(), "userId.notnull");
        AssertUtils.notNull(req.getTypeId(), "typeId.notnull");
        AssertUtils.notNull(req.getTenantId(), "tenantId.notnull");
        FavoriteResp favoriteResp=favoriteApi.getFavoriteStatus(req);
        Boolean status=false;
        if(favoriteResp!=null){
            return true;
        }
        return status;
    }

    @Override
    public void cancelFavorite(FavoriteReq req) {
        AssertUtils.notNull(req.getDevScene(), "devScene.notnull");
        AssertUtils.notNull(req.getUserId(), "userId.notnull");
        AssertUtils.notNull(req.getTypeId(), "typeId.notnull");
        AssertUtils.notNull(req.getTenantId(), "tenantId.notnull");
        FavoriteReq favoriteReq=new FavoriteReq();
        favoriteApi.delSingleFavorite(favoriteReq);
    }
}
