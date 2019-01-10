package com.iot.center.utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.env.Environment;

import com.iot.common.helper.ApplicationContextHelper;
import com.iot.redis.RedisCacheUtil;

public class MailUtil {
	
	private Environment dnvironment = ApplicationContextHelper.getBean(Environment.class);

	// 设置服务器
	private static String KEY_SMTP;
	private static String VALUE_SMTP;
	// 服务器验证
	private static String KEY_PROPS;
	private static String VALUE_PROPS;
	// 发件人用户名、密码
	private String SEND_USER;
	private String SEND_UNAME;
	private String SEND_PWD;
	// 建立会话
	private MimeMessage message;
	private Session s;

	/*
	 * 初始化方法
	 */
	public MailUtil() {
		KEY_SMTP = dnvironment.getProperty("smtp.keysmtp");
		VALUE_SMTP = dnvironment.getProperty("smtp.valuesmtp");
		KEY_PROPS = dnvironment.getProperty("smtp.keyprops");
		VALUE_PROPS = dnvironment.getProperty("smtp.valueprops");
		SEND_USER = dnvironment.getProperty("smtp.senduser");
		SEND_UNAME = dnvironment.getProperty("smtp.sendname");
		SEND_PWD = dnvironment.getProperty("smtp.sendpassword");
		Properties props = System.getProperties();
		props.setProperty(KEY_SMTP, VALUE_SMTP);
		props.put(KEY_PROPS, VALUE_PROPS);
		s = Session.getDefaultInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(SEND_UNAME, SEND_PWD);
			}
		});
		s.setDebug(true);
		message = new MimeMessage(s);
	}

	/**
	 * 发送邮件
	 * 
	 * @param headName
	 *            邮件头文件名
	 * @param sendHtml
	 *            邮件内容
	 * @param receiveUser
	 *            收件人地址
	 */
	public boolean doSendHtmlEmail(String headName, String sendHtml, String receiveUser) {
		try {
			// 发件人
			InternetAddress from = new InternetAddress(SEND_USER);
			message.setFrom(from);
			// 收件人
			String[] receiveUserArr = receiveUser.split(";");
			for (String userAddress : receiveUserArr) {
				InternetAddress to = new InternetAddress(userAddress);
				message.addRecipient(Message.RecipientType.TO, to);
			}
			// 邮件标题
			message.setSubject(headName);
			String content = sendHtml.toString();
			// 邮件内容,也可以使纯文本"text/plain"
			message.setContent(content, "text/html;charset=UTF-8");
			message.saveChanges();
			Transport transport = s.getTransport("smtp");
			// smtp验证，就是你用来发邮件的邮箱用户名密码
			transport.connect(VALUE_SMTP, SEND_UNAME, SEND_PWD);
			// 发送
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			System.out.println("send success!");
			// 防止邮箱无限制发送 设置间隔一分钟
			String key = "email-interval:" + receiveUser;
			RedisCacheUtil.valueSet(key, receiveUser);;
			RedisCacheUtil.expireKey(key, 60);//失效时间1分钟
			return true;
		} catch (MessagingException e) {
			return false;
		}
	}

	public static void main(String[] args) {
		MailUtil mailUtil = new MailUtil();
		mailUtil.doSendHtmlEmail("验证码", "this is a test email", "linjihuang@leedarson.com");
	}

}
