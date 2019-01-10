package com.iot.ifttt;

import com.iot.ifttt.common.IftttServiceEnum;
import com.iot.ifttt.common.IftttTypeEnum;
import com.iot.ifttt.service.IIftttService;
import com.iot.ifttt.vo.*;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IftttServiceApplicationTests {

    @Autowired
    private IIftttService iftttService;

    @Test
    public void contextLoads() {
        CheckAppletReq checkAppletReq = new CheckAppletReq();
        checkAppletReq.setType(IftttTypeEnum.DEV_STATUS.getType());
        String deviceId = "a4eab429f5f037c4ac0439cd04a713c5";
        String field ="Door";
        String msg = "{\"devId\":\"" + deviceId + "\",\"field\":\"" + field + "\"}";
        checkAppletReq.setMsg(msg);
        iftttService.check(checkAppletReq);
    }

    @Test
    public void saveIfttt() {
        //定时任务
        //AppletVo appletVo = getTimerAuto();
        AppletVo appletVo = getDevAuto();
        //AppletVo appletVo = getSunsetAuto();
        iftttService.save(appletVo);
    }


    /**
     * 天文定时
     *
     * @return
     */
    private AppletVo getSunsetAuto() {
		/*{
			"msg": "{\"itemId\":2}",
				"type": "TIMER"
		}*/

        AppletVo appletVo = new AppletVo();
        //applet
        appletVo.setName("天文定时测试");
        appletVo.setCreateBy(0l);
        appletVo.setStatus("on");

        //this
        List<AppletThisVo> thisList = Lists.newArrayList();
        AppletThisVo appletThisVo = new AppletThisVo();
        appletThisVo.setServiceCode(IftttServiceEnum.ASTRONOMICAL.getCode()); //设备服务
        appletThisVo.setLogic("or");
        //this_item
        List<AppletItemVo> thisItems = Lists.newArrayList();
        AppletItemVo appletItemVo = new AppletItemVo();
        String json = "{\"intervalType\":2,\"repeat\":[0,1,2,3,4,5,6],\"latitude\":\"24.524449898792874\",\"timeZone\":\"GMT+8\",\"trigType\":\"sunrise\",\"idx\":0,\"longitude\":\"118.1577499221764\",\"intervalTime\":\"17400\"}";
        appletItemVo.setJson(json);
        thisItems.add(appletItemVo);
        appletThisVo.setItems(thisItems);
        thisList.add(appletThisVo);
        appletVo.setThisList(thisList);
        //that
        List<AppletThatVo> thatList = Lists.newArrayList();
        AppletThatVo appletThatVo = new AppletThatVo();
        appletThatVo.setServiceCode(IftttServiceEnum.MQ.getCode()); //MQ消息服务
        appletThatVo.setCode("timer_test1"); //事件码
        //that_item
        List<AppletItemVo> thatItems = Lists.newArrayList();
        AppletItemVo item = new AppletItemVo();
        json = "{\"tenantId\":7,\"msg\":{\"thenType\" : \"dev\",\"id\" : \"1090210050\",\"idx\" : 2,\"attr\" : {\"warning_duration\" : 60,\"warning_mode\" : \"fire\",\"siren_level\" : \"high\",\"strobe\" : \"on\",\"strobe_level\" : \"high\"}}}";
        item.setJson(json);
        thatItems.add(item);
        appletThatVo.setItems(thatItems);
        thatList.add(appletThatVo);
        appletVo.setThatList(thatList);
        return appletVo;
    }

    /**
     * 设备状态触发联动
     *
     * @return
     */
    private AppletVo getDevAuto() {
		/*{
			"msg": "{\"devId\":\"b6f7c7f3e7a14a74be155d6a0f7f38e1\",\"field\":\"door\"}",
				"type": "DEV_STATUS"
		}*/

        AppletVo appletVo = new AppletVo();
        //applet
        appletVo.setName("设备状态触发测试");
        appletVo.setCreateBy(0l);
        appletVo.setStatus("on");

        //dev-this
        List<AppletThisVo> thisList = Lists.newArrayList();
        AppletThisVo appletThisVo = new AppletThisVo();
        appletThisVo.setServiceCode(IftttServiceEnum.DEV_STATUS.getCode()); //设备服务
        appletThisVo.setLogic("or");
        //this_item
        List<AppletItemVo> thisItems = Lists.newArrayList();
        AppletItemVo appletItemVo = new AppletItemVo();
        String json = "{\"devId\":\"b6f7c7f3e7a14a74be155d6a0f7f38e1\",\"field\": \"door\",\"sign\": \"=\",\"value\":1,\"type\":0}";  //json格式
        appletItemVo.setJson(json);
        thisItems.add(appletItemVo);
        appletThisVo.setItems(thisItems);
        thisList.add(appletThisVo);
        appletVo.setThisList(thisList);


        //time-this
        AppletThisVo appletThisVo1 = new AppletThisVo();
        appletThisVo1.setServiceCode(IftttServiceEnum.TIME_RANGE.getCode()); //时间范围服务
        appletThisVo1.setLogic("or");
        //this_item
        List<AppletItemVo> thisItems1 = Lists.newArrayList();
        AppletItemVo appletItemVo1 = new AppletItemVo();
        json = "{\"begin\": \"00:00\",\"end\": \"23:59\",\"repeat\": [ 0, 1, 2, 3, 4, 5, 6 ]}";  //json格式
        appletItemVo1.setJson(json);
        thisItems1.add(appletItemVo1);
        appletThisVo1.setItems(thisItems1);
        thisList.add(appletThisVo1);
        appletVo.setThisList(thisList);

        //that
        List<AppletThatVo> thatList = Lists.newArrayList();
        AppletThatVo appletThatVo = new AppletThatVo();
        appletThatVo.setServiceCode(IftttServiceEnum.MQ.getCode()); //MQ消息服务
        appletThatVo.setCode("timer_test8"); //事件码
        //that_item
        List<AppletItemVo> thatItems = Lists.newArrayList();
        AppletItemVo item = new AppletItemVo();
        json = "{\"tenantId\":6,\"msg\":{\"thenType\" : \"dev\",\"id\" : \"1090210050\",\"idx\" : 2,\"attr\" : {\"warning_duration\" : 60,\"warning_mode\" : \"fire\",\"siren_level\" : \"high\",\"strobe\" : \"on\",\"strobe_level\" : \"high\"}}}";
        item.setJson(json);
        thatItems.add(item);
        appletThatVo.setItems(thatItems);
        thatList.add(appletThatVo);
        appletVo.setThatList(thatList);
        return appletVo;
    }


    /**
     * 定时任务
     *
     * @return
     */
    private AppletVo getTimerAuto() {
		/*{
			"msg": "{\"itemId\":2}",
				"type": "TIMER"
		}*/

        AppletVo appletVo = new AppletVo();
        //applet
        appletVo.setName("定时测试");
        appletVo.setCreateBy(0l);
        appletVo.setStatus("off");

        //this
        List<AppletThisVo> thisList = Lists.newArrayList();
        AppletThisVo appletThisVo = new AppletThisVo();
        appletThisVo.setServiceCode(IftttServiceEnum.TIMER.getCode()); //定时服务
        appletThisVo.setLogic("or");
        //this_item
        List<AppletItemVo> thisItems = Lists.newArrayList();
        AppletItemVo appletItemVo = new AppletItemVo();
        String json = "{\"at\":\"13:30\",\"repeat\":[1,2,3,4,5,6,7]}";  //json格式
        appletItemVo.setJson(json);
        thisItems.add(appletItemVo);
        appletThisVo.setItems(thisItems);
        thisList.add(appletThisVo);
        appletVo.setThisList(thisList);
        //that
        List<AppletThatVo> thatList = Lists.newArrayList();
        AppletThatVo appletThatVo = new AppletThatVo();
        appletThatVo.setServiceCode(IftttServiceEnum.MQ.getCode()); //MQ消息服务
        appletThatVo.setCode("timer_test"); //事件码
        //that_item
        List<AppletItemVo> thatItems = Lists.newArrayList();
        AppletItemVo item = new AppletItemVo();
        json = "{\"tenantId\":6,\"msg\":{\"test\":668}}";
        item.setJson(json);
        thatItems.add(item);
        appletThatVo.setItems(thatItems);
        thatList.add(appletThatVo);
        appletVo.setThatList(thatList);
        return appletVo;
    }

}
