package com.iot.scene.controller;

import com.iot.BusinessExceptionEnum;
import com.iot.cloud.helper.Constants;
import com.iot.cloud.vo.Space;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.AuthorizeShareRequired;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.enums.PermissionEnum;
import com.iot.common.exception.BusinessException;
import com.iot.control.favorite.api.FavoriteApi;
import com.iot.control.favorite.vo.FavoriteReq;
import com.iot.control.scene.vo.req.SceneAddReq;
import com.iot.control.space.api.SpaceApi;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.scene.api.SceneApi;
import com.iot.shcs.scene.vo.req.SceneDetailReqVo;
import com.iot.shcs.scene.vo.req.SceneReq;
import com.iot.shcs.scene.vo.rsp.SceneResp;
import com.iot.user.api.UserApi;
import com.iot.util.AssertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "Scene接口")
@RestController
@RequestMapping("/scene")
public class SceneController {

    @Autowired
    private SceneApi sceneApi;

    @Autowired
    private UserApi userApi;

    @Autowired
    private FavoriteApi favoriteApi;

    @Autowired
    private SpaceApi spaceApi;
    @LoginRequired(value = Action.Normal)
    @AuthorizeShareRequired(value =PermissionEnum.SCENE_LIST)
    @ResponseBody
    @ApiOperation(value = "获取当前用户的scene列表", response = SceneResp.class)
    @RequestMapping(value = "/getScenes", method = {RequestMethod.POST, RequestMethod.GET})
    public CommonResponse<List> getScenes(@RequestParam(value = "homeId",required = false) Long homeId) {
        try {
            List<SceneResp> sceneBasicRespList = new ArrayList<>();
            //不传homeId
            if(homeId==null){
                sceneBasicRespList=sceneApi.findSceneRespListByUserId(SaaSContextHolder.getCurrentUserId(),SaaSContextHolder.currentTenantId());
            }else {
                sceneBasicRespList=sceneApi.findSceneRespListBySpaceId(homeId,SaaSContextHolder.currentTenantId());
            }
            sceneBasicRespList.forEach(m->{
                m.setSceneId(m.getId());
                m.setName(m.getSceneName());
            });
            return CommonResponse.success(sceneBasicRespList);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
        }
    }


    @LoginRequired(value = Action.Normal)
    @AuthorizeShareRequired(value =PermissionEnum.SCENE_ADD)
    @ResponseBody
    @ApiOperation(value = "添加scene", response = Map.class)
    @RequestMapping(value = "/addScene", method = RequestMethod.POST)
    public CommonResponse<Map> createScene(@RequestBody SceneReq sceneReq) {
        try {
            sceneReq.setSceneName(sceneReq.getName());
            sceneReq.setIcon(sceneReq.getIcon());
            sceneReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
            sceneReq.setUpdateBy(SaaSContextHolder.getCurrentUserId());
            sceneReq.setSpaceId(sceneReq.getHomeId());
            sceneReq.setTenantId(SaaSContextHolder.currentTenantId());
            SceneResp scene = sceneApi.saveScene(sceneReq);
            return CommonResponse.success(commonBackParam(scene));
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
        }
    }

    @LoginRequired(value = Action.Normal)
    @AuthorizeShareRequired(value =PermissionEnum.SCENE_UPDATE)
    @ResponseBody
    @ApiOperation(value = "编辑scene", response = Map.class)
    @RequestMapping(value = "/editScene", method = RequestMethod.POST)
    public CommonResponse<Map> updateScene(@RequestBody SceneReq sceneReq) {
        try {
            sceneReq.setId(sceneReq.getSceneId());
            sceneReq.setSceneName(sceneReq.getName());
            sceneReq.setIcon(sceneReq.getIcon());
            sceneReq.setUpdateBy(SaaSContextHolder.getCurrentUserId());
            sceneReq.setSpaceId(sceneReq.getHomeId());
            sceneReq.setTenantId(SaaSContextHolder.currentTenantId());
            SceneResp scene = sceneApi.updateScene(sceneReq);
            return CommonResponse.success(commonBackParam(scene));
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
        }
    }

    private Map<String, Object> commonBackParam(SceneResp scene) {
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.SCENE_ID, scene.getId());
        return map;
    }

    @LoginRequired(value = Action.Normal)
    @AuthorizeShareRequired(value =PermissionEnum.SCENE_DELETE)
    @ResponseBody
    @ApiOperation(value = "删除scene(Ble)")
    @RequestMapping(value = "/delScene", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse delScene(@RequestBody SceneReq sceneReq) {
        try {
            sceneReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
            sceneReq.setTenantId(SaaSContextHolder.currentTenantId());
            sceneReq.setId(sceneReq.getSceneId());
            sceneApi.delBleScene(sceneReq);
            return CommonResponse.success();
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
        }
    }

    @LoginRequired(value = Action.Normal)
    @AuthorizeShareRequired(value =PermissionEnum.SCENE_LIST)
    @ResponseBody
    @ApiOperation(value = "查询场景项列表", response = Map.class)
    @RequestMapping(value = "/getThen", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getThen(@RequestBody SceneReq sceneReq) {
        try {
            sceneReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
            sceneReq.setTenantId(SaaSContextHolder.currentTenantId());
            sceneReq.setId(sceneReq.getSceneId());

            return CommonResponse.success(sceneApi.getThen(sceneReq));
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
        }
    }

    @LoginRequired(value = Action.Normal)
    @AuthorizeShareRequired(value =PermissionEnum.SCENE_ADD)
    @ResponseBody
    @ApiOperation(value = "添加场景项")
    @RequestMapping(value = "/addAction", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse addAction(@RequestBody SceneDetailReqVo actionReq) {
        AssertUtils.notNull(actionReq, "actionReq.notnull");
        AssertUtils.notNull(actionReq.getAction(), "action.notnull");
        AssertUtils.notNull(actionReq.getSceneId(), "sceneId.notnull");
        try {
            actionReq.setUserId(SaaSContextHolder.getCurrentUserId());
            actionReq.setTenantId(SaaSContextHolder.currentTenantId());
            actionReq.setUserUuid(SaaSContextHolder.getCurrentUserUuid());
            sceneApi.addAction(actionReq);
            return CommonResponse.success();
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
        }
    }

    @LoginRequired(value = Action.Normal)
    @AuthorizeShareRequired(value =PermissionEnum.SCENE_DELETE)
    @ResponseBody
    @ApiOperation(value = "删除场景项")
    @RequestMapping(value = "/delAction", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse delAction(@RequestBody SceneDetailReqVo actionReq) {
        AssertUtils.notNull(actionReq, "actionReq.notnull");
        AssertUtils.notNull(actionReq.getAction(), "action.notnull");
        AssertUtils.notNull(actionReq.getSceneId(), "sceneId.notnull");
        try {
            actionReq.setUserId(SaaSContextHolder.getCurrentUserId());
            actionReq.setTenantId(SaaSContextHolder.currentTenantId());
            actionReq.setUserUuid(SaaSContextHolder.getCurrentUserUuid());
            sceneApi.delAction(actionReq);
            //删除favorite中的场景
            Long sceneId=actionReq.getSceneId();
            FavoriteReq favoriteReq=new FavoriteReq();

            favoriteReq.setTenantId(SaaSContextHolder.currentTenantId());
            favoriteReq.setDevScene(String.valueOf(sceneId));
            favoriteReq.setUserId(SaaSContextHolder.getCurrentUserId());
            favoriteReq.setTypeId(2);

            favoriteApi.delSingleFavorite(favoriteReq);
            return CommonResponse.success();
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
        }
    }

    @LoginRequired(value = Action.Normal)
    @AuthorizeShareRequired(value =PermissionEnum.SCENE_UPDATE)
    @ResponseBody
    @ApiOperation(value = "修改场景项")
    @RequestMapping(value = "/editAction", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse editAction(@RequestBody SceneDetailReqVo actionReq) {
        AssertUtils.notNull(actionReq, "actionReq.notnull");
        AssertUtils.notNull(actionReq.getAction(), "action.notnull");
        AssertUtils.notNull(actionReq.getSceneId(), "sceneId.notnull");
        try {
            actionReq.setUserId(SaaSContextHolder.getCurrentUserId());
            actionReq.setTenantId(SaaSContextHolder.currentTenantId());
            actionReq.setUserUuid(SaaSContextHolder.getCurrentUserUuid());
            sceneApi.editAction(actionReq);
            return CommonResponse.success();
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
        }
    }

}
