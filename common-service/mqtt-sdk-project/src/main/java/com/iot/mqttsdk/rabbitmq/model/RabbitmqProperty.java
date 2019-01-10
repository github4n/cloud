package com.iot.mqttsdk.rabbitmq.model;

/**
 * 项目名称：IOT云平台
 * 模块名称：mqsdk
 * 功能描述：mq配置信息
 * 创建人： 490485964@qq.com
 * 创建时间：2018年04月17日 19:42
 * 修改人： 490485964@qq.com
 * 修改时间：2018年04月17日 19:42
 */
public class RabbitmqProperty {
    // 设置连接的用户名
    private String userName;
    //设置连接的密码
    private String passWord;
    // 主机名 ip
    private String host;
    //端口
    private int port;
    //路由key
//    private String routingKey;
    //交换机
//    private String exchanges;


    public RabbitmqProperty() {
    }

    public RabbitmqProperty(String userName, String passWord, String host, int port) {
        this.userName = userName;
        this.passWord = passWord;
        this.host = host;
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
