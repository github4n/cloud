package com.iot.scene.controller;

import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.scene.api.SceneApiService;
import com.iot.scene.domain.DeviceTarValue;
import com.iot.scene.domain.RoomScene;
import com.iot.scene.domain.SceneExec;
import com.iot.scene.domain.SceneTemplate;
import com.iot.scene.service.SceneServiceImpl;
import com.iot.scene.vo.DeviceTarValueVO;
import com.iot.scene.vo.RoomSceneTO;
import com.iot.scene.vo.RoomTemplateTO;
import com.iot.scene.vo.SceneDetailVO;
import com.iot.scene.vo.SceneTemplateTO;
import com.iot.scene.vo.SceneTemplateVO;
import com.iot.scene.vo.TemplateDetailTO;
import com.iot.scene.vo.TemplateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/scene")
public class SceneServiceController implements SceneApiService {
    @Autowired
    private SceneServiceImpl sceneServiceImpl;

    @Override
    public void saveSceneTemplate(@RequestBody SceneTemplate sceneTemplate) throws BusinessException {

    }

    @Override
    public Page<SceneTemplateVO> getSceneTemplateList(@RequestParam("pageNum") String pageNum, @RequestParam("pageSize") String pageSize, @RequestParam("name") String name) throws BusinessException {
        return null;
    }

    @Override
    public List<DeviceTarValue> getDeviceTarValueList(@RequestParam("templateId") String templateId) throws BusinessException {
        return sceneServiceImpl.getDeviceTarValueList(templateId);
    }

    @Override
    public SceneTemplateTO getSceneTemplate(@RequestParam("templateId") String templateId) throws BusinessException {
        return null;
    }

    @Override
    public void updateSceneTemplate(@RequestBody TemplateDetailTO templateDetail) throws BusinessException {

    }

    @Override
    public void deleteSceneTemplate(@RequestParam("templateId") String templateId) throws BusinessException {

    }

    @Override
    public List<TemplateVO> getTemplateList(@RequestParam("name") String name) throws BusinessException {
        return sceneServiceImpl.getTemplateList(name);
    }

    @Override
    public void saveRoomTemplate(@RequestBody RoomTemplateTO roomTemplate) throws BusinessException {

    }

    @Override
    public List<DeviceTarValueVO> getDeviceTarValueVOList(@RequestParam("spaceId") String spaceId, @RequestParam("templateId") String templateId) throws BusinessException {
        return sceneServiceImpl.getDeviceTarValueVOList(spaceId, templateId);
    }

    @Override
    public void saveSceneDetail(@RequestParam("deviceTarValues") String deviceTarValues, @RequestParam("username") String username) throws BusinessException {

    }

    @Override
    public List<List<TemplateVO>> getTemplateListGroupByFour(@RequestParam("spaceId") String spaceId) throws BusinessException {
        return null;
    }

    @Override
    public List<SceneExec> getDiffDeviceCategorySceneExecList(@RequestParam("spaceId") String spaceId, @RequestParam("templateId") String templateId) throws BusinessException {
        return sceneServiceImpl.getDiffDeviceCategorySceneExecList(spaceId, templateId);
    }


    @Override
    public List<TemplateVO> getTemplateBySpaceId(@RequestParam("spaceId") String spaceId) throws BusinessException {
        return sceneServiceImpl.getTemplateBySpaceId(spaceId);
    }

    @Override
    public List<SceneDetailVO> getSceneDetailList(@RequestParam("spaceId") String spaceId, @RequestParam("templateId") String templateId) throws BusinessException {
        return null;
    }

    @Override
    public List<RoomSceneTO> getRoomScene(@RequestParam("realityId") String realityId) {
        return sceneServiceImpl.getRoomScene(realityId);
    }

    @Override
    public List<RoomScene> getRoomSceneBySceneId(@RequestParam("sceneId") String sceneId) {
        return sceneServiceImpl.getRoomSceneBySceneId(sceneId);
    }


    @Override
    public void sceneExecute(@RequestParam("spaceId") String spaceId, @RequestParam("templateId") String templateId) {

    }


    @Override
    public void saveSceneSvg(String spaceId, String svg) throws BusinessException {

    }

    @Override
    public String getSceneSvg(String spaceId) throws BusinessException {
        return null;
    }

}
