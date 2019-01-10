package com.iot;

import com.iot.portal.TenantPortalApplication;
import com.iot.portal.service.LangInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TenantPortalApplication.class)
@WebAppConfiguration
public class TenantPortalApplicationTests {

	@Autowired
	private LangInfoService langInfoService;

	@Test
	public void contextLoads() {
	}

}
