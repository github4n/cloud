package com.iot.scene.service;

import com.iot.scene.domain.DeviceTarValue;
import com.iot.scene.domain.RoomScene;
import com.iot.scene.domain.RoomTemplate;
import com.iot.scene.domain.SceneDetail;
import com.iot.scene.domain.SceneExec;
import com.iot.scene.domain.Template;
import com.iot.scene.domain.TemplateDetail;
import com.iot.scene.mapper.SceneMapper;
import com.iot.scene.vo.DeviceTarValueTO;
import com.iot.scene.vo.DeviceTarValueVO;
import com.iot.scene.vo.RoomSceneTO;
import com.iot.scene.vo.RoomTemplateVO;
import com.iot.scene.vo.SceneDetailVO;
import com.iot.scene.vo.SceneTemplateTO;
import com.iot.scene.vo.SceneTemplateVO;
import com.iot.scene.vo.TemplateVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SceneServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(SceneServiceImpl.class);

    private static final String REDIS_SCENE_STR = "SC_";//情景前缀


    @Autowired
    private SceneMapper sceneMapper;

    /**
     * 描述：保存模板
     *
     * @param template
     * @throws Exception
     * @author wujianlong
     * @created 2017年11月14日 下午4:21:25
     * @since
     */
    public void saveTemplate(Template template) {
        sceneMapper.saveTemplate(template);
    }

    /**
     * 描述：保存模板详情
     *
     * @param templateDetail
     * @throws Exception
     * @author wujianlong
     * @created 2017年11月14日 下午4:23:02
     * @since
     */
    public void saveTemplateDetail(TemplateDetail templateDetail) {
        sceneMapper.saveTemplateDetail(templateDetail);
    }

    /**
     * 描述：查询情景模板
     *
     * @param name
     * @return
     * @throws Exception
     * @author wujianlong
     * @created 2017年11月14日 下午5:44:37
     * @since
     */
    public List<SceneTemplateVO> getSceneTemplateList(String name) {
        return sceneMapper.getSceneTemplateList(name);
    }


    /**
     * 描述：查询产品类型目标值
     *
     * @param templateId
     * @return
     * @throws Exception
     * @author wujianlong
     * @created 2017年11月15日 上午10:06:23
     * @since
     */
    public List<DeviceTarValue> getDeviceTarValueList(String templateId) {
        return sceneMapper.getDeviceTarValueList(templateId);
    }

    /**
     * 描述：查询模板
     *
     * @param templateId
     * @return
     * @throws Exception
     * @author wujianlong
     * @created 2017年11月15日 上午11:43:50
     * @since
     */
    public SceneTemplateTO getTemplate(String templateId) {
        return sceneMapper.getTemplate(templateId);
    }

    /**
     * 描述：查询模板详情
     *
     * @param templateId
     * @return
     * @throws Exception
     * @author wujianlong
     * @created 2017年11月15日 上午11:45:47
     * @since
     */
    public List<DeviceTarValueTO> getTemplateDetailList(String templateId) {
        return sceneMapper.getTemplateDetailList(templateId);
    }

    /**
     * 描述：更新模板
     *
     * @param template
     * @throws Exception
     * @author wujianlong
     * @created 2017年11月15日 下午2:47:32
     * @since
     */
    public void updateTemplate(Template template) {
        sceneMapper.updateTemplate(template);
    }

    /**
     * 描述：更新模板详情
     *
     * @param templateDetail
     * @throws Exception
     * @author wujianlong
     * @created 2017年11月15日 下午2:48:26
     * @since
     */
    public void updateTemplateDetail(TemplateDetail templateDetail) {
        sceneMapper.updateTemplateDetail(templateDetail);
    }

    /**
     * 描述：查询房间中是否绑定模板
     *
     * @param templateId
     * @return
     * @throws Exception
     * @author wujianlong
     * @created 2017年11月15日 下午3:19:59
     * @since
     */
    public List<String> getRoomByTemplateId(String templateId) {
        return sceneMapper.getRoomByTemplateId(templateId);
    }

    /**
     * 描述：删除模板
     *
     * @param templateId
     * @throws Exception
     * @author wujianlong
     * @created 2017年11月15日 下午3:43:04
     * @since
     */
    public void delTemplate(String templateId) {
        sceneMapper.delTemplate(templateId);
    }

    /**
     * 描述：删除模板详情
     *
     * @param templateId
     * @throws Exception
     * @author wujianlong
     * @created 2017年11月15日 下午3:43:36
     * @since
     */
    public void delTemplateDetail(String templateId) {
        sceneMapper.delTemplateDetail(templateId);
    }

    /**
     * 描述：查询模板
     *
     * @param name
     * @return
     * @throws Exception
     * @author wujianlong
     * @created 2017年11月16日 上午11:34:35
     * @since
     */
    public List<TemplateVO> getTemplateList(String name) {
        return sceneMapper.getTemplateList(name);
    }

    /**
     * 描述：删除房间情景
     *
     * @param spaceId
     * @throws Exception
     * @author wujianlong
     * @created 2017年11月16日 下午2:32:13
     * @since
     */
    public void delRoomTemplate(String spaceId) {
        sceneMapper.delRoomTemplate(spaceId);
    }

    /**
     * 描述：保存房间情景
     *
     * @param roomTemplate
     * @throws Exception
     * @author wujianlong
     * @created 2017年11月16日 下午2:33:21
     * @since
     */
    public void saveRoomTemplate(RoomTemplate roomTemplate) {
        sceneMapper.saveRoomTemplate(roomTemplate);
    }

    /**
     * 描述：查询房间下的情景
     *
     * @param spaceId
     * @return
     * @throws Exception
     * @author wujianlong
     * @created 2017年11月16日 下午3:38:47
     * @since
     */
    public List<TemplateVO> getTemplateBySpaceId(String spaceId) {
        return sceneMapper.getTemplateBySpaceId(spaceId);
    }

    /**
     * 描述：查询设备名-目标值
     *
     * @param spaceId
     * @param templateId
     * @return
     * @throws Exception
     * @author wujianlong
     * @created 2017年11月16日 下午4:05:45
     * @since
     */
    public List<DeviceTarValueVO> getDeviceTarValueVOList(String spaceId, String templateId) {
        return sceneMapper.getDeviceTarValueVOList(spaceId, templateId);
    }

    /**
     * 描述：保存情景微调
     *
     * @param sceneDetail
     * @throws Exception
     * @author wujianlong
     * @created 2017年11月16日 下午4:31:02
     * @since
     */
    public void saveSceneDetail(SceneDetail sceneDetail) {
        sceneMapper.saveSceneDetail(sceneDetail);
    }

    /**
     * 描述：查询模板名个数
     *
     * @param name
     * @return
     * @throws Exception
     * @author wujianlong
     * @created 2017年11月17日 上午10:43:44
     * @since
     */
    public int getTemplateNameCount(String name) {
        return sceneMapper.getTemplateNameCount(name);
    }

    /**
     *
     * 描述：修改设备名
     * @author wujianlong
     * @created 2017年11月20日 下午2:42:28
     * @since
     * @param deviceId
     * @param deviceName
     * @throws Exception
     */
    /*public void updateDeviceName(@Param("deviceId") String deviceId,@Param("deviceName") String deviceName) throws Exception;*/

    /**
     * 描述：查询不同设备情景执行
     *
     * @param spaceId
     * @param templateId
     * @return
     * @throws Exception
     * @author wujianlong
     * @created 2017年11月21日 下午5:30:02
     * @since
     */
    public List<SceneExec> getDiffDeviceCategorySceneExecList(String spaceId,
                                                              String templateId) {
        return sceneMapper.getDiffDeviceCategorySceneExecList(spaceId, templateId);
    }

    /**
     * 描述：查询微调设备情景执行
     *
     * @param spaceId
     * @param templateId
     * @return
     * @throws Exception
     * @author wujianlong
     * @created 2017年11月21日 下午5:42:17
     * @since
     */
    public List<SceneExec> getSpecDeviceSceneExecList(String spaceId,
                                                      String templateId) {
        return sceneMapper.getSpecDeviceSceneExecList(spaceId, templateId);
    }

    /**
     * 描述：查询变化的房间情景
     *
     * @param realityId
     * @return
     * @author wujianlong
     * @created 2017年11月22日 下午4:19:52
     * @since
     */
    public List<RoomSceneTO> getRoomScene(String realityId) {
        return sceneMapper.getRoomScene(realityId);
    }

    /**
     * 描述：查询房间id
     *
     * @param templateId
     * @return
     * @author wujianlong
     * @created 2017年11月22日 下午5:36:56
     * @since
     */
    public List<RoomScene> getRoomSceneBySceneId(String templateId) {
        return sceneMapper.getRoomSceneBySceneId(templateId);
    }

    /**
     * 描述：查询模板情景
     *
     * @param spaceId
     * @param templateId
     * @return
     * @throws Exception
     * @author wujianlong
     * @created 2017年12月12日 下午4:36:59
     * @since
     */
    public List<SceneDetailVO> getTemplateSceneList(String spaceId, String templateId) {
        return sceneMapper.getTemplateSceneList(spaceId, templateId);
    }

    /**
     * 描述：查询情景微调
     *
     * @param spaceId
     * @return
     * @throws Exception
     * @author wujianlong
     * @created 2017年12月12日 下午4:46:13
     * @since
     */
    public List<SceneDetailVO> getSceneDetailList(String spaceId) {
        return sceneMapper.getSceneDetailList(spaceId);
    }

    /**
     * 描述：查询设备类型和参数
     *
     * @param templateId
     * @return
     * @throws Exception
     * @author linjihuang
     * @created 2018年01月17日 下午14:46:13
     * @since
     */
    public List<RoomTemplateVO> getDeviceValueByTempID(String spaceId, String templateId) {
        return sceneMapper.getDeviceValueByTempID(spaceId, templateId);
    }

    /**
     * 描述：查询情景关联ID
     *
     * @param spaceId
     * @return
     * @throws Exception
     * @author linjihuang
     * @created 2018年01月18日 下午10:46:13
     * @since
     */
    public List<String> getSceneTemplateId(String spaceId) {
        return sceneMapper.getSceneTemplateId(spaceId);
    }


}
