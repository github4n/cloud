package com.iot.pack.util;

import org.springframework.util.StringUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Date;
import java.util.Properties;

/**
 * 描述：邮件工具类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/8/13 10:39
 */
public class EmailUtil {
    private static String host = "mail.leedarson.com";
    private static String userName = "cloud@leedarson.com";
    private static String passWord = "cloud@321";


    public static void main(String[] args) throws Exception {
        String subject = "打包结果通知";
        String profile = "dev";
        String rc = "成功"; // 0 成功 1失败
        String desc = "Success.";
        String maiBody = "打包环境：" + profile;
        maiBody += "<br>应用名称：" + "SmartHome614";
        maiBody += "<br>版本号：" + "1.0.0.27";
        maiBody += "<br>打包结果：" + rc;
        maiBody += "<br>描述：" + desc;
        maiBody += "<br>安卓应用下载地址：" + "https://www.pgyer.com/M5fM";
        maiBody += "<br>苹果应用下载地址：" + "https://www.pgyer.com/M5fM";

        sendMsg("zhangyue@leedarson.com", subject, maiBody,"C:\\Users\\zhangyue\\Desktop\\portal-产品相关接口.txt");
        sendMsg("zhangyue@leedarson.com", subject, maiBody);
    }


    /**
     * 发送邮件
     *
     * @param to
     */
    public static void sendMsg(String to, String subject, String maiBody) {
        Integer retryNum = 1;

        try {
            Properties props = new Properties();
            props.setProperty("mail.debug", "true");
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.host", host);
            props.setProperty("mail.transport.protocol", "smtp");
            Session session = Session.getInstance(props);
            //System.out.println("host:" + host);
            if (!StringUtils.isEmpty(host) && host.contains("gmail.com")) {
                props.put("mail.smtp.ssl.enable", "true");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.port", "465");
                props.put("mail.smtp.socketFactory.port", "465");
                session = Session.getDefaultInstance(props,
                        new Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(userName, passWord);
                            }
                        });
            }
            // 创建邮件对象
            Message msg = new MimeMessage(session);
            msg.setSubject(subject);

            // 设置邮件内容
            msg.setContent(maiBody, "text/html; charset=utf-8");

            msg.setSentDate(new Date());
            msg.setFrom(new InternetAddress(userName));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            if (to.contains("@outlook")) {
                msg.addRecipient(Message.RecipientType.CC, new InternetAddress(userName));
            }
            msg.saveChanges();
            Transport transport = session.getTransport("smtp");
            // 连接邮件服务器
            transport.connect(userName, passWord);
            // 发送邮件
            transport.sendMessage(msg, msg.getAllRecipients());
            // 关闭连接
            transport.close();

        } catch (Exception e) {
            System.out.println("Push Email Fail StackTrace:" + e.getStackTrace().toString());
            System.out.println("Push Email Fail Suppressed:" + e.getSuppressed().toString());
            System.out.println("Push Email Fail Cause:" + e.getCause());
            System.out.println("Push Email Fail LocalizedMessage:" + e.getLocalizedMessage());
            System.out.println("Push Email Fail Message:" + e.getMessage());
            if (retryNum > 0) {
                sendMsg(to, subject, maiBody);
            }
        }
    }

    /**
     * 发送邮件
     *
     * @param to
     */
    public static void sendMsg(String to, String subject, String maiBody,String filePath) {
        Integer retryNum = 1;

        try {
            Properties props = new Properties();
            props.setProperty("mail.debug", "true");
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.host", host);
            props.setProperty("mail.transport.protocol", "smtp");
            Session session = Session.getInstance(props);
            //System.out.println("host:" + host);
            if (!StringUtils.isEmpty(host) && host.contains("gmail.com")) {
                props.put("mail.smtp.ssl.enable", "true");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.port", "465");
                props.put("mail.smtp.socketFactory.port", "465");
                session = Session.getDefaultInstance(props,
                        new Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(userName, passWord);
                            }
                        });
            }
            // 创建邮件对象
            Message msg = new MimeMessage(session);
            msg.setSubject(subject);

            // 设置邮件内容
            MimeBodyPart text = new MimeBodyPart();
            text.setContent(maiBody, "text/html; charset=utf-8");

            //设置邮件附件
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setDataHandler(new DataHandler(new FileDataSource(filePath)));
            mimeBodyPart.setFileName(new DataHandler(new FileDataSource(filePath)).getName());

            //组装内容和附件
            MimeMultipart mm = new MimeMultipart();
            mm.addBodyPart(mimeBodyPart);
            mm.addBodyPart(text);

            //设置发送的内容和附件
            msg.setContent(mm);

            msg.setSentDate(new Date());
            msg.setFrom(new InternetAddress(userName));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            if (to.contains("@outlook")) {
                msg.addRecipient(Message.RecipientType.CC, new InternetAddress(userName));
            }
            msg.saveChanges();
            Transport transport = session.getTransport("smtp");
            // 连接邮件服务器
            transport.connect(userName, passWord);
            // 发送邮件
            transport.sendMessage(msg, msg.getAllRecipients());
            // 关闭连接
            transport.close();

        } catch (Exception e) {
            System.out.println("Push Email Fail StackTrace:" + e.getStackTrace().toString());
            System.out.println("Push Email Fail Suppressed:" + e.getSuppressed().toString());
            System.out.println("Push Email Fail Cause:" + e.getCause());
            System.out.println("Push Email Fail LocalizedMessage:" + e.getLocalizedMessage());
            System.out.println("Push Email Fail Message:" + e.getMessage());
            if (retryNum > 0) {
                sendMsg(to, subject, maiBody);
            }
        }
    }
}
