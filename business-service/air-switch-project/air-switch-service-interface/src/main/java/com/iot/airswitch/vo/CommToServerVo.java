package com.iot.airswitch.vo;

/**
 * @Author: Xieby
 * @Date: 2018/10/15
 * @Description: *
 */
public class CommToServerVo {

    /**
     * 协议版本 0xF1
     */
    private String PVER;

    /**
     * 消息ID
     */
    private String CMD;

    /**
     * 附加码
     */
    private String CMD_VER;

    /**
     * 附加码2
     */
    private String CMD_VER2;

    /**
     * 消息返回
     */
    private String CMD_ACK;

    /**
     * DATA数据长度
     */
    private String Len;

    /**
     * 消息序列号
     */
    private String CMD_No;

    /**
     * MAC地址
     */
    private String MAC;

    /**
     * 时区
     */
    private String TIMEZONE;

    /**
     * 保留
     */
    private String RSEV;

    /**
     * 数据内容
     */
    private String DATA;

    /**
     * CRC校验
     */
    private String CRC;

    public String getPVER() {
        return PVER;
    }

    public void setPVER(String PVER) {
        this.PVER = PVER;
    }

    public String getCMD() {
        return CMD;
    }

    public void setCMD(String CMD) {
        this.CMD = CMD;
    }

    public String getCMD_VER() {
        return CMD_VER;
    }

    public void setCMD_VER(String CMD_VER) {
        this.CMD_VER = CMD_VER;
    }

    public String getCMD_VER2() {
        return CMD_VER2;
    }

    public void setCMD_VER2(String CMD_VER2) {
        this.CMD_VER2 = CMD_VER2;
    }

    public String getCMD_ACK() {
        return CMD_ACK;
    }

    public void setCMD_ACK(String CMD_ACK) {
        this.CMD_ACK = CMD_ACK;
    }

    public String getLen() {
        return Len;
    }

    public void setLen(String len) {
        Len = len;
    }

    public String getCMD_No() {
        return CMD_No;
    }

    public void setCMD_No(String CMD_No) {
        this.CMD_No = CMD_No;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public String getTIMEZONE() {
        return TIMEZONE;
    }

    public void setTIMEZONE(String TIMEZONE) {
        this.TIMEZONE = TIMEZONE;
    }

    public String getRSEV() {
        return RSEV;
    }

    public void setRSEV(String RSEV) {
        this.RSEV = RSEV;
    }

    public String getDATA() {
        return DATA;
    }

    public void setDATA(String DATA) {
        this.DATA = DATA;
    }

    public String getCRC() {
        return CRC;
    }

    public void setCRC(String CRC) {
        this.CRC = CRC;
    }
}
