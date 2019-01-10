package com.iot.airswitch;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.iot.airswitch.job.CmdJob;
import com.iot.airswitch.util.ThreadPoolUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class AirSwitchServiceApplicationTests {

	@Test
	public void contextLoads() {
		CmdJob cmdJob = new CmdJob("172.16.28.230", 5918, "F1D3A100000902405c06339bFFFFFF00000000000000000001c79b4bf2");
		ThreadPoolUtil.instance().submit(cmdJob);
	}

	@Test
	public void TestList() {
		List<Integer> list = Lists.newArrayList(1, 21, 4, 2);

		list.sort((k1, k2) -> {
			return k1.compareTo(k2);
		});

		System.out.println(JSON.toJSONString(list));
	}

}
