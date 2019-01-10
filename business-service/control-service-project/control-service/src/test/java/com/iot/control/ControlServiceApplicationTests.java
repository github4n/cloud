package com.iot.control;

import com.github.pagehelper.PageHelper;
import com.iot.control.ifttt.vo.res.RuleResp;
import com.iot.mqttsdk.common.MqttMsg;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class ControlServiceApplicationTests {

	/*@Autowired
	private SceneTemplateMapper sceneTemplateMapper;*/

    public void getIftttMsg(RuleResp ruleResp) {

    }

    public void getRule(MqttMsg msg) {

    }

    @Test
    public void contextLoads() {
    }


    @Test
    public void findSceneTemplateList() {
        String pageNum = "1";
        String pageSize = "10";

        PageHelper.startPage(Integer.parseInt(StringUtils.isBlank(pageNum) == true ? "1" : pageNum),
                Integer.parseInt(StringUtils.isBlank(pageSize) == true ? "10" : pageSize));

        //Page<SceneTemplateVO> page = new Page(0, 10);

        // List<SceneTemplateVO> sceneTemplateList =
        //sceneTemplateMapper.findSceneTemplateList("001", 100L);

        System.out.println();
    }
}
