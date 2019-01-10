package com.iot.airswitch.vo.data;

import com.google.common.base.Strings;
import com.iot.airswitch.constant.OpenCloseEnum;
import io.swagger.models.auth.In;

/**
 * @Author: Xieby
 * @Date: 2018/10/15
 * @Description: *
 */
public class NetConfigData {

    /**
     * 通讯模块节点号
     */
    private Integer NNO;

    /**
     * 0x88 485通讯模块
     * 0x89 网口通讯模块
     * 0x8a 网口+wifi通讯模块
     * 0x8b 网口+NB通讯模块
     * 0x8c 网口+2G通讯模块
     */
    private String TYPE;

    /**
     * 产品型号
     * 同 REGISTER消息 MODEL*
     */
    private String MODEL;

    /**
     * 版本号
     * 同 REGISTER消息 VER*
     */
    private String VER;

    /**
     * MAC地址 产品序列号
     */
    private String MAC;

    /**
     * 控制位
     * Bit3~15 保留
     * Bit2 心跳包启用
     * Bit1 禁用DNS解析
     * Bit0 DHCP使能
     *
     */
    private String CTRL;

    /**
     * IP地址
     */
    private String IP;

    /**
     * 子网掩码
     */
    private String MASK;

    /**
     * 网关IP地址
     */
    private String GW;

    /**
     * 服务器IP地址
     */
    private String SIP;

    /**
     * 服务器端口
     */
    private Long SPORT;

    /**
     * DNS服务器IP地址
     */
    private String DNSS;

    /**
     * STATUS_REPORT消息上传间隔
     */
    private Integer RTVI;

    /**
     * 本地监听端口
     */
    private String LPORT;

    /**
     * 时区
     */
    private String ZONE;

    /**
     * 局域网服务器IP地址
     */
    private String LIP;

    /**
     * 设备密码
     */
    private String DEVPWD;

    /**
     * 保留其他用途
     */
    private String REV1;

    /**
     * 模块/机箱名称
     */
    private String NAME;

    /**
     * 保留其他用途
     */
    private String REV2;

    /**
     * wifi SSID
     */
    private String SSID;

    /**
     * WiFi密码
     */
    private String WPWD;

    private String NNOStr;

    private String SPORTStr;

    private String RTVIStr;

    public Integer getNNO() {
        return NNO;
    }

    public void setNNO(Integer NNO) {
        this.NNO = NNO;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getMODEL() {
        return MODEL;
    }

    public void setMODEL(String MODEL) {
        this.MODEL = MODEL;
    }

    public String getVER() {
        return VER;
    }

    public void setVER(String VER) {
        this.VER = VER;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public String getCTRL() {
        return CTRL;
    }

    public void setCTRL(String CTRL) {
        this.CTRL = CTRL;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getMASK() {
        return MASK;
    }

    public void setMASK(String MASK) {
        this.MASK = MASK;
    }

    public String getGW() {
        return GW;
    }

    public void setGW(String GW) {
        this.GW = GW;
    }

    public String getSIP() {
        return SIP;
    }

    public void setSIP(String SIP) {
        this.SIP = SIP;
    }

    public Long getSPORT() {
        return SPORT;
    }

    public void setSPORT(Long SPORT) {
        this.SPORT = SPORT;
    }

    public String getDNSS() {
        return DNSS;
    }

    public void setDNSS(String DNSS) {
        this.DNSS = DNSS;
    }

    public Integer getRTVI() {
        return RTVI;
    }

    public void setRTVI(Integer RTVI) {
        this.RTVI = RTVI;
    }

    public String getLPORT() {
        return LPORT;
    }

    public void setLPORT(String LPORT) {
        this.LPORT = LPORT;
    }

    public String getZONE() {
        return ZONE;
    }

    public void setZONE(String ZONE) {
        this.ZONE = ZONE;
    }

    public String getLIP() {
        return LIP;
    }

    public void setLIP(String LIP) {
        this.LIP = LIP;
    }

    public String getDEVPWD() {
        return DEVPWD;
    }

    public void setDEVPWD(String DEVPWD) {
        this.DEVPWD = DEVPWD;
    }

    public String getREV1() {
        return REV1;
    }

    public void setREV1(String REV1) {
        this.REV1 = REV1;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getREV2() {
        return REV2;
    }

    public void setREV2(String REV2) {
        this.REV2 = REV2;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getWPWD() {
        return WPWD;
    }

    public void setWPWD(String WPWD) {
        this.WPWD = WPWD;
    }

    /**
     * DHCP 使能
     * 0 关  1开
     */
    public String getDHCP() {
        return getCTRLInfo(4);
    }

    /**
     * 禁用DNS解析
     */
    public String getDns() {
        return getCTRLInfo(3);
    }

    /**
     * 心跳包启动
     */
    public String getBeat() {
        return getCTRLInfo(2);
    }

    public String getNNOStr() {
        return NNOStr;
    }

    public void setNNOStr(String NNOStr) {
        this.NNOStr = NNOStr;
    }

    public String getSPORTStr() {
        return SPORTStr;
    }

    public void setSPORTStr(String SPORTStr) {
        this.SPORTStr = SPORTStr;
    }

    public String getRTVIStr() {
        return RTVIStr;
    }

    public void setRTVIStr(String RTVIStr) {
        this.RTVIStr = RTVIStr;
    }

    private String getCTRLInfo(int pos) {
        if (Strings.isNullOrEmpty(CTRL)) {
            return OpenCloseEnum.CLOSE.code;
        }
        // 转换为二进制
        String info = Integer.toBinaryString(Integer.parseInt(CTRL.substring(3,4)));
        int length = info.length();
        if (length == 4) {
            return info;
        }
        for (int i=0; i<(4-length); i++) {
            info = "0" + info;
        }
        return info.substring(pos-1,pos);
    }

    public static void main(String[] args) {
        NetConfigData data = new NetConfigData();
        data.setCTRL("0007");

        System.out.println(data.getDHCP());
    }
}
