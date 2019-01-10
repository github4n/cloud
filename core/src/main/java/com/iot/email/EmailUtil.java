package com.iot.email;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public final class EmailUtil {

    private final String host;

    private final String username;

    private final String password;

    public static EmailUtil getEmailUtil(String host, String username, String password) {
        return new EmailUtil(host, username, password);
    }

    private EmailUtil(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }

    public boolean sendMail(String from, String to, String subject, String body) throws Exception {
        Properties props = new Properties();
        props.setProperty("mail.debug", "true");
        props.setProperty("mail.smtp.auth", "true");
        // props.setProperty("mail.host", "smtp.126.com");
        props.setProperty("mail.host", host);
        props.setProperty("mail.transport.protocol", "smtp");
        Session session = Session.getInstance(props);
        // 创建邮件对象
        Message msg = new MimeMessage(session);
        msg.setSubject(subject);
        // 设置邮件内容
        msg.setContent(body, "text/html; charset=utf-8");

        msg.setSentDate(new Date());
        // msg.setFrom(new InternetAddress("lishuai1990ls@126.com"));
        msg.setFrom(new InternetAddress(from));
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        if (to.contains("@outlook")) {
            msg.addRecipient(Message.RecipientType.CC, new InternetAddress(from));
        }
        msg.saveChanges();
        Transport transport = session.getTransport("smtp");
        // 连接邮件服务器
        transport.connect(username, password);
        // 发送邮件
        transport.sendMessage(msg, msg.getAllRecipients());
        // 关闭连接
        transport.close();
        return true;
    }

}
