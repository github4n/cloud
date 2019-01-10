package com.iot.common.util;


import com.iot.common.config.PropertyConfigurerUtil;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtil {

    // 设置服务器
    private static String keysmtp = PropertyConfigurerUtil.PROPERTY_MAP.get("smtp.keysmtp");
    private static String valuesmtp = PropertyConfigurerUtil.PROPERTY_MAP.get("smtp.valuesmtp");
    // 服务器验证
    private static String keyprops = PropertyConfigurerUtil.PROPERTY_MAP.get("smtp.keyprops");
    private static String valueprops = PropertyConfigurerUtil.PROPERTY_MAP.get("smtp.valueprops");
    // 发件人用户名、密码
    private static final String USER_STR = "smtp.senduser";
    private static final String NAME_STR = "smtp.sendname";
    private static final String PWD_STR = "smtp.sendpassword";
    // 建立会话
    private final MimeMessage message;
    private final Session s;

    /*
     * 初始化方法
     */
    public MailUtil() {
        Properties props = System.getProperties();
        props.setProperty(keysmtp, valuesmtp);
        props.put(keyprops, valueprops);
        s = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(PropertyConfigurerUtil.PROPERTY_MAP.get(NAME_STR), PropertyConfigurerUtil.PROPERTY_MAP.get(PWD_STR));
            }
        });
        s.setDebug(true);
        message = new MimeMessage(s);
    }

    /**
     * 发送邮件
     *
     * @param headName    邮件头文件名
     * @param sendHtml    邮件内容
     * @param receiveUser 收件人地址
     */
    public boolean doSendHtmlEmail(String headName, String sendHtml, String receiveUser) {
        try {
            // 发件人
            InternetAddress from = new InternetAddress(PropertyConfigurerUtil.PROPERTY_MAP.get(USER_STR));
            message.setFrom(from);
            // 收件人
            String[] receiveUserArr = receiveUser.split(";");
            for (String userAddress : receiveUserArr) {
                InternetAddress to = new InternetAddress(userAddress);
                message.addRecipient(Message.RecipientType.TO, to);
            }
            // 邮件标题
            message.setSubject(headName);
            // 邮件内容,也可以使纯文本"text/plain"
            message.setContent(sendHtml, "text/html;charset=UTF-8");
            message.saveChanges();
            Transport transport = s.getTransport("smtp");
            // smtp验证，就是你用来发邮件的邮箱用户名密码
            transport.connect(valuesmtp, PropertyConfigurerUtil.PROPERTY_MAP.get(NAME_STR), PropertyConfigurerUtil.PROPERTY_MAP.get(PWD_STR));
            // 发送
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            System.out.println("send success!");
            return true;
        } catch (MessagingException e) {
            return false;
        }
    }
}
