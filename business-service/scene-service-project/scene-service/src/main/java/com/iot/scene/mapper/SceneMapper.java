package com.iot.scene.mapper;

import com.iot.scene.domain.DeviceTarValue;
import com.iot.scene.domain.RoomScene;
import com.iot.scene.domain.RoomTemplate;
import com.iot.scene.domain.SceneDetail;
import com.iot.scene.domain.SceneExec;
import com.iot.scene.domain.SceneSvg;
import com.iot.scene.domain.Template;
import com.iot.scene.domain.TemplateDetail;
import com.iot.scene.vo.DeviceTarValueTO;
import com.iot.scene.vo.DeviceTarValueVO;
import com.iot.scene.vo.RoomSceneTO;
import com.iot.scene.vo.RoomTemplateVO;
import com.iot.scene.vo.SceneDetailVO;
import com.iot.scene.vo.SceneTemplateTO;
import com.iot.scene.vo.SceneTemplateVO;
import com.iot.scene.vo.TemplateVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：情景dao
 * 创建人： wujianlong
 * 创建时间：2017年11月10日 下午3:07:57
 * 修改人： wujianlong
 * 修改时间：2017年11月10日 下午3:07:57
 */
public interface SceneMapper {

	/**
	 * 
	 * 描述：保存模板
	 * @author wujianlong
	 * @created 2017年11月14日 下午4:21:25
	 * @since 
	 * @param template
	 * @throws Exception
	 */
	void saveTemplate(@Param("template") Template template);

	/**
	 * 
	 * 描述：保存模板详情
	 * @author wujianlong
	 * @created 2017年11月14日 下午4:23:02
	 * @since 
	 * @param templateDetail
	 * @throws Exception
	 */
	void saveTemplateDetail(@Param("templateDetail") TemplateDetail templateDetail);

	/**
	 * 
	 * 描述：查询情景模板
	 * @author wujianlong
	 * @created 2017年11月14日 下午5:44:37
	 * @since 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	List<SceneTemplateVO> getSceneTemplateList(@Param("name") String name);

	/**
	 * 
	 * 描述：查询产品类型目标值
	 * @author wujianlong
	 * @created 2017年11月15日 上午10:06:23
	 * @since 
	 * @param templateId
	 * @return
	 * @throws Exception
	 */
	List<DeviceTarValue> getDeviceTarValueList(@Param("templateId") String templateId);

	/**
	 * 
	 * 描述：查询模板
	 * @author wujianlong
	 * @created 2017年11月15日 上午11:43:50
	 * @since 
	 * @param templateId
	 * @return
	 * @throws Exception
	 */
	SceneTemplateTO getTemplate(@Param("templateId") String templateId);

	/**
	 * 
	 * 描述：查询模板详情
	 * @author wujianlong
	 * @created 2017年11月15日 上午11:45:47
	 * @since 
	 * @param templateId
	 * @return
	 * @throws Exception
	 */
	List<DeviceTarValueTO> getTemplateDetailList(@Param("templateId") String templateId);

	/**
	 * 
	 * 描述：更新模板
	 * @author wujianlong
	 * @created 2017年11月15日 下午2:47:32
	 * @since 
	 * @param template
	 * @throws Exception
	 */
	void updateTemplate(@Param("template") Template template);

	/**
	 * 
	 * 描述：更新模板详情
	 * @author wujianlong
	 * @created 2017年11月15日 下午2:48:26
	 * @since 
	 * @param templateDetail
	 * @throws Exception
	 */
	void updateTemplateDetail(@Param("templateDetail") TemplateDetail templateDetail);

	/**
	 * 
	 * 描述：查询房间中是否绑定模板
	 * @author wujianlong
	 * @created 2017年11月15日 下午3:19:59
	 * @since 
	 * @param templateId
	 * @return
	 * @throws Exception
	 */
	List<String> getRoomByTemplateId(@Param("templateId") String templateId);

	/**
	 * 
	 * 描述：删除模板
	 * @author wujianlong
	 * @created 2017年11月15日 下午3:43:04
	 * @since 
	 * @param templateId
	 * @throws Exception
	 */
	void delTemplate(@Param("templateId") String templateId);

	/**
	 * 
	 * 描述：删除模板详情
	 * @author wujianlong
	 * @created 2017年11月15日 下午3:43:36
	 * @since 
	 * @param templateId
	 * @throws Exception
	 */
	void delTemplateDetail(@Param("templateId") String templateId);

	/**
	 * 
	 * 描述：查询模板
	 * @author wujianlong
	 * @created 2017年11月16日 上午11:34:35
	 * @since 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	List<TemplateVO> getTemplateList(@Param("name") String name);

	/**
	 * 
	 * 描述：删除房间情景
	 * @author wujianlong
	 * @created 2017年11月16日 下午2:32:13
	 * @since 
	 * @param spaceId
	 * @throws Exception
	 */
	void delRoomTemplate(@Param("spaceId") String spaceId);

	/**
	 * 
	 * 描述：保存房间情景
	 * @author wujianlong
	 * @created 2017年11月16日 下午2:33:21
	 * @since 
	 * @param roomTemplate
	 * @throws Exception
	 */
	void saveRoomTemplate(@Param("roomTemplate") RoomTemplate roomTemplate);

	/**
	 * 
	 * 描述：查询房间下的情景
	 * @author wujianlong
	 * @created 2017年11月16日 下午3:38:47
	 * @since 
	 * @param spaceId
	 * @return
	 * @throws Exception
	 */
	List<TemplateVO> getTemplateBySpaceId(@Param("spaceId") String spaceId);

	/**
	 * 
	 * 描述：查询设备名-目标值
	 * @author wujianlong
	 * @created 2017年11月16日 下午4:05:45
	 * @since 
	 * @param spaceId
	 * @param templateId
	 * @return
	 * @throws Exception
	 */
	List<DeviceTarValueVO> getDeviceTarValueVOList(@Param("spaceId") String spaceId, @Param("templateId") String templateId);

	/**
	 * 
	 * 描述：保存情景微调
	 * @author wujianlong
	 * @created 2017年11月16日 下午4:31:02
	 * @since 
	 * @param sceneDetail
	 * @throws Exception
	 */
	void saveSceneDetail(@Param("sceneDetail") SceneDetail sceneDetail);

	/**
	 * 
	 * 描述：查询模板名个数
	 * @author wujianlong
	 * @created 2017年11月17日 上午10:43:44
	 * @since 
	 * @param name
	 * @return
	 * @throws Exception
	 */
    int getTemplateNameCount(@Param("name") String name);

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
	/*public void updateDeviceName(@Param("deviceId") String deviceId,@Param("deviceName") String deviceName);*/

	/**
	 * 
	 * 描述：查询不同设备情景执行
	 * @author wujianlong
	 * @created 2017年11月21日 下午5:30:02
	 * @since 
	 * @param spaceId
	 * @param templateId
	 * @return
	 * @throws Exception
	 */
	List<SceneExec> getDiffDeviceCategorySceneExecList(@Param("spaceId") String spaceId, @Param("templateId") String templateId);

	/**
	 * 
	 * 描述：查询微调设备情景执行
	 * @author wujianlong
	 * @created 2017年11月21日 下午5:42:17
	 * @since 
	 * @param spaceId
	 * @param templateId
	 * @return
	 * @throws Exception
	 */
	List<SceneExec> getSpecDeviceSceneExecList(@Param("spaceId") String spaceId, @Param("templateId") String templateId);

	/**
	 * 
	 * 描述：查询变化的房间情景
	 * @author wujianlong
	 * @created 2017年11月22日 下午4:19:52
	 * @since 
	 * @param realityId
	 * @return
	 */
    List<RoomSceneTO> getRoomScene(@Param("realityId") String realityId);

	/**
	 * 
	 * 描述：查询房间id
	 * @author wujianlong
	 * @created 2017年11月22日 下午5:36:56
	 * @since 
	 * @param templateId
	 * @return
	 */
    List<RoomScene> getRoomSceneBySceneId(@Param("templateId") String templateId);

	/**
	 * 
	 * 描述：查询模板情景
	 * @author wujianlong
	 * @created 2017年12月12日 下午4:36:59
	 * @since 
	 * @param spaceId
	 * @param templateId
	 * @return
	 * @throws Exception
	 */
	List<SceneDetailVO> getTemplateSceneList(@Param("spaceId") String spaceId, @Param("templateId") String templateId);

	/**
	 * 
	 * 描述：查询情景微调
	 * @author wujianlong
	 * @created 2017年12月12日 下午4:46:13
	 * @since 
	 * @param spaceId
	 * @return
	 * @throws Exception
	 */
	List<SceneDetailVO> getSceneDetailList(@Param("spaceId") String spaceId);

	SceneSvg getSceneSvg(@Param("spaceId") String spaceId);

	void deleteSceneSvg(@Param("spaceId") String spaceId);
    
    void deleteSceneDetail(@Param("spaceId") String spaceId,@Param("deviceIds") List<String> deviceIds);

	List<RoomTemplateVO> getDeviceValueByTempID(String spaceId, String templateId);
    
    /**
     * 
     * 描述：查询情景关联ID 
     * @author linjihuang
     * @created 2018年01月18日 下午10:46:13
     * @since 
     * @param spaceId
     * @return
     * @throws Exception
     */
	List<String> getSceneTemplateId(String spaceId);
}
