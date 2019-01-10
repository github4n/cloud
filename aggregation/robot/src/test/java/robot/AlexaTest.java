package robot;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Servlet;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.iot.robot.transform.AlexaTransfor;
import com.iot.robot.vo.alexa.AlexaDiscoveryResponse;

public class AlexaTest {

	@Test
	public void deviceHandle() {
//		AlexaDiscoveryResponse res = new AlexaDiscoveryResponse();
//		AlexaTransfor tran = AlexaTransfor.getInsatnce();
//		Device d = new Device();
//		d.setDeviceId("11111");
//		d.setDeviceName("sdfsdf");
//		List<Object> lis = new ArrayList<>();
//		lis.add(d);
//		List[] ll = {lis};
//		System.out.println(JSONObject.toJSON(tran.getResponse(ll)));
	}
	@Test
	public void valueHandle() {
		JSONObject json1 = JSONObject.parseObject("{\"directive\": { \"header\": { \"namespace\": \"Alexa.BrightnessController\", \"name\": \"AdjustBrightness\", \"payloadVersion\": \"3\", \"messageId\": \"1bd5d003-31b9-476f-ad03-71d471922820\", \"correlationToken\": \"dFMb0z+PgpgdDmluhJ1LddFvSqZ/jCc8ptlAKulUj90jSqg==\" }, \"endpoint\": { \"scope\": { \"type\": \"BearerToken\", \"token\": \"access-token-from-Amazon\" }, \"endpointId\": \"appliance-001\", \"cookie\": {} }, \"payload\": { \"brightnessDelta\": 3 } } }");
		JSONObject header = json1.getJSONObject("directive").getJSONObject("header");
		JSONObject endpoint = json1.getJSONObject("directive").getJSONObject("endpoint");
		JSONObject payload = json1.getJSONObject("directive").getJSONObject("payload");
		String commondStr = header.getString("name");
		System.out.println(Servlet.class.getClassLoader());
	}

}
