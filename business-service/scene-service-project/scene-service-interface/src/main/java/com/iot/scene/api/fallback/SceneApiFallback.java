package com.iot.scene.api.fallback;

import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.scene.api.SceneApiService;
import com.iot.scene.domain.DeviceTarValue;
import com.iot.scene.domain.RoomScene;
import com.iot.scene.domain.SceneExec;
import com.iot.scene.domain.SceneTemplate;
import com.iot.scene.vo.DeviceTarValueVO;
import com.iot.scene.vo.RoomSceneTO;
import com.iot.scene.vo.RoomTemplateTO;
import com.iot.scene.vo.SceneDetailVO;
import com.iot.scene.vo.SceneTemplateTO;
import com.iot.scene.vo.SceneTemplateVO;
import com.iot.scene.vo.TemplateDetailTO;
import com.iot.scene.vo.TemplateVO;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SceneApiFallback implements FallbackFactory<SceneApiService> {
    @Override
    public SceneApiService create(Throwable throwable) {
        return new SceneApiService() {
            @Override
            public void saveSceneTemplate(SceneTemplate sceneTemplate) throws BusinessException {

            }

            @Override
            public Page<SceneTemplateVO> getSceneTemplateList(String pageNum, String pageSize, String name) throws BusinessException {
                return null;
            }

            @Override
            public List<DeviceTarValue> getDeviceTarValueList(String templateId) throws BusinessException {
                return null;
            }

            @Override
            public SceneTemplateTO getSceneTemplate(String templateId) throws BusinessException {
                return null;
            }

            @Override
            public void updateSceneTemplate(TemplateDetailTO templateDetail) throws BusinessException {

            }

            @Override
            public void deleteSceneTemplate(String templateId) throws BusinessException {

            }

            @Override
            public List<TemplateVO> getTemplateList(String name) throws BusinessException {
                return null;
            }

            @Override
            public void saveRoomTemplate(RoomTemplateTO roomTemplate) throws BusinessException {

            }

            @Override
            public List<DeviceTarValueVO> getDeviceTarValueVOList(String spaceId, String templateId) throws BusinessException {
                return null;
            }

            @Override
            public void saveSceneDetail(String deviceTarValues, String username) throws BusinessException {

            }

            @Override
            public List<List<TemplateVO>> getTemplateListGroupByFour(String spaceId) throws BusinessException {
                return null;
            }

            @Override
            public List<SceneExec> getDiffDeviceCategorySceneExecList(String spaceId, String templateId) throws BusinessException {
                return null;
            }

            @Override
            public List<TemplateVO> getTemplateBySpaceId(String spaceId) throws BusinessException {
                return null;
            }

            @Override
            public List<SceneDetailVO> getSceneDetailList(String spaceId, String templateId) throws BusinessException {
                return null;
            }

            @Override
            public List<RoomSceneTO> getRoomScene(String realityId) {
                return null;
            }

            @Override
            public List<RoomScene> getRoomSceneBySceneId(String sceneId) {
                return null;
            }

            @Override
            public void sceneExecute(String spaceId, String templateId) {

            }

            @Override
            public void saveSceneSvg(String spaceId, String svg) throws BusinessException {

            }

            @Override
            public String getSceneSvg(String spaceId) throws BusinessException {
                return null;
            }
        };
    }
}
