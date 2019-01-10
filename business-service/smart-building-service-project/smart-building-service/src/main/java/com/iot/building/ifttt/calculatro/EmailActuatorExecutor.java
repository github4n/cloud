package com.iot.building.ifttt.calculatro;

import com.iot.common.util.MailUtil;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EmailActuatorExecutor implements IActuatorExecutor {

	@Override
	public void execute(Map<String, Object> params) {
		MailUtil mailUtil = new MailUtil();
		String subject = (String) params.get("subject");
		String content = (String) params.get("content");
		String emails = (String) params.get("emails");
		
		mailUtil.doSendHtmlEmail(subject, content, emails);
	}

}
