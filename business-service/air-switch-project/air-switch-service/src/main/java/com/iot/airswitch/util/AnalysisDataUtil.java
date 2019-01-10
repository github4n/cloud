package com.iot.airswitch.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.iot.airswitch.constant.Constants;
import com.iot.airswitch.vo.CommToServerVo;
import com.iot.airswitch.vo.data.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Xieby
 * @Date: 2018/10/18
 * @Description: *
 */
public class AnalysisDataUtil {

    /**
     * 1. 协议版本  1  0xf1
     * 2. 消息类型  1
     * 3. 附加码    1
     * 4. 消息返回  1
     * 5. 数据长度  2
     * 6. 消息系列号 2
     * 7. MAC地址  6
     * 8. 时区     1
     * 9. 保留     1
     * 10. DATA
     * 11. CRC校验 4
     */
    public static List<String> comminuteData(String result) {
        List<String> list = Lists.newArrayList();
        if (Strings.isNullOrEmpty(result)) {
            return list;
        }
        int resultLength = result.length();
        if (resultLength < 40) {
            return list;
        }
        int dataLength = Integer.parseInt(result.substring(8, 12), 16);
        List<Integer> lengthList = Lists.newArrayList(1, 1, 1, 1, 2, 2, 6, 1, 1, dataLength, 4);
        int totalIndex = 0;
        for (Integer i : lengthList) {
            list.add(result.substring(totalIndex*2, (totalIndex+i)*2));
            totalIndex += i;
        }
        return list;
    }

    /**
     * 命令的长度计算
     */
    public static Integer getCmdLength(String data) {
        if (Strings.isNullOrEmpty(data) || data.length() < 20) {
            return 0;
        }
        int dataLength = Integer.parseInt(data.substring(8, 12), 16);
        return (20+dataLength)*2;
     }

    public static CommToServerVo handleData(String info) {
        List<String> dataList = comminuteData(info);
        CommToServerVo vo = new CommToServerVo();
        if (dataList.size() < 2) {
            return null;
        }
        String messageType = dataList.get(1);
        switch (messageType) {
            case Constants.CMD_STATUS_REPORT : assembleStatusReport(vo, dataList) ; break;
            case Constants.CMD_ACM_COUNT : assembleAcmCount(vo, dataList); break;
            case Constants.CMD_PUSH_EVENT : assemblePushEvent(vo, dataList); break;
            case Constants.CMD_HEART_BEAT : assembleHeartBeat(vo, dataList); break;
            case Constants.CMD_REGISTER : assembleRegister(vo, dataList); break;
            case Constants.CMD_PUSH_NET_CONFIG : assemblePushNetConfig(vo, dataList); break;
            case Constants.CMD_GET_NET_CONFIG : assembleGetNetConfig(vo, dataList); break;
            case Constants.CMD_PUSH_GW_CONFIG : assemblePushGwConfig(vo, dataList); break;
            case Constants.CMD_GET_GW_CONFIG : assemblePushGwConfig(vo, dataList); break;
            default: break;
        }

        return vo;
    }

    private static void assembleGetNetConfig(CommToServerVo vo, List<String> dataList) {
        assembleHeader(vo, dataList);

        String data = dataList.get(9);
        NetConfigData configData = new NetConfigData();

        configData.setNNOStr(data.substring(0, 2));
        configData.setTYPE(data.substring(2, 4));
        configData.setMODEL(data.substring(4, 8));
        configData.setVER(data.substring(8, 12));
        configData.setMAC(data.substring(12, 24));
        configData.setCTRL(data.substring(24, 28));
        configData.setIP(data.substring(28, 36));
        configData.setMASK(data.substring(36, 44));
        configData.setGW(data.substring(44, 52));
        configData.setSIP(data.substring(52, 60));
        configData.setSPORTStr(data.substring(60, 64));
        configData.setDNSS(data.substring(64, 72));
        configData.setRTVIStr(data.substring(72, 76));
        configData.setLPORT(data.substring(76, 80));
        configData.setZONE(data.substring(80, 82));
        configData.setLIP(data.substring(82, 90));
        configData.setDEVPWD(data.substring(90, 106));
        configData.setREV1(data.substring(106, 112));
        configData.setNAME(data.substring(112, 160));
        configData.setREV2(data.substring(160, 168));
        configData.setSSID(data.substring(168, 208));
        configData.setWPWD(data.substring(208, 248));

        vo.setDATA(JSON.toJSONString(configData));
    }

    /**
     * 推送电量统计
     * Data : 循环 （total : 80)
     *    1. NNO   1
     *    2. TYPE  1
     *    3. MODEL 2
     *    4. VER   2
     *    5. SER   4
     *    6. REV   2
     *    7. CTRL  2
     *    8. VOLH  2
     *    9. VOLL  2
     *    10.VOLS  2
     *    11.LKIH  2
     *    12.LKIS  2
     *    13.PWRH  2
     *    14.PWRL  2
     *    15.PWRS  2
     *    16.TMPH  2
     *    17.TMPS  2
     *    18.CURH  2
     *    19.CURS  2
     *    20.REV1  4
     *    21.LKDT  4
     *    22.LKDI  4
     *    23.REV   6
     *    24.NAME 24
     */
    private static void assemblePushGwConfig(CommToServerVo vo, List<String> dataList) {

        assembleHeader(vo, dataList);

        String data = dataList.get(9);
        List<GwConfigData> gwList = Lists.newArrayList();

        for (int i=0; i<data.length()/160; i++) {
            GwConfigData gw = new GwConfigData();
            String tmpData = data.substring(i*160, (i+1)*160);

            gw.setNNO(tmpData.substring(0, 2));
            gw.setTYPE(tmpData.substring(2, 4));
            gw.setMODEL(tmpData.substring(4, 8));
            gw.setVER(tmpData.substring(8, 12));
            gw.setSER(tmpData.substring(12, 20));
            gw.setREV(tmpData.substring(20, 24));
            gw.setCTRL(tmpData.substring(24, 28));
            gw.setVOLH(Integer.parseInt(tmpData.substring(28, 32), 16));
            gw.setVOLL(Integer.parseInt(tmpData.substring(32, 36), 16));
            gw.setVOLS(Integer.parseInt(tmpData.substring(36, 40), 16));
            gw.setLKIH(Integer.parseInt(tmpData.substring(40, 44), 16));
            gw.setLKIS(Integer.parseInt(tmpData.substring(44, 48), 16));
            gw.setPWRH(Integer.parseInt(tmpData.substring(48, 52), 16));
            gw.setPWRL(Integer.parseInt(tmpData.substring(52, 56), 16));
            gw.setPWRS(Integer.parseInt(tmpData.substring(56, 60), 16));
            gw.setTMPH(Integer.parseInt(tmpData.substring(60, 64), 16));
            gw.setTMPS(Integer.parseInt(tmpData.substring(64, 68), 16));
            gw.setCURH(Integer.parseInt(tmpData.substring(68, 72), 16));
            gw.setCURS(Integer.parseInt(tmpData.substring(72, 76), 16));
            gw.setREV1(tmpData.substring(76, 84));
            gw.setLKDT(tmpData.substring(84, 92));
            gw.setLKDI(tmpData.substring(92, 100));
            gw.setREV2(tmpData.substring(100, 112));
            gw.setNAME(tmpData.substring(112, 160));

            gwList.add(gw);
        }

        vo.setDATA(JSON.toJSONString(gwList));
    }

    /**
     * 推送网络配置
     * Data : (total 124)
     *   1. NNO    1
     *   2. TYPE   1
     *   3. MODEL  2
     *   4. VER    2
     *   5. MAC    6
     *   6. CTRL   2
     *   7. IP     4
     *   8. MASK   4
     *   9. GW     4
     *   10. SIP   4
     *   11. SPORT 2
     *   12. DNSS  4
     *   13. RTVI  2
     *   14. LPORT 2
     *   15. ZONE  1
     *   16. LIP   4
     *   17. DEVPWD 8
     *   18. REV1  3
     *   19. NAME  24
     *   20. REV2  4
     *   21. SSID  20
     *   22. WPWD  20
     */
    private static void assemblePushNetConfig(CommToServerVo vo, List<String> dataList) {

        assembleHeader(vo, dataList);

        String data = dataList.get(9);
        NetConfigData configData = new NetConfigData();

        configData.setNNO(Integer.parseInt(data.substring(0, 2), 16));
        configData.setTYPE(data.substring(2, 4));
        configData.setMODEL(data.substring(4, 8));
        configData.setVER(data.substring(8, 12));
        configData.setMAC(data.substring(12, 24));
        configData.setCTRL(HexUtil.hexToBinary(data.substring(24, 28)));
        configData.setIP(HexUtil.hexToIp(data.substring(28, 36)));
        configData.setMASK(HexUtil.hexToIp(data.substring(36, 44)));
        configData.setGW(HexUtil.hexToIp(data.substring(44, 52)));
        configData.setSIP(HexUtil.hexToIp(data.substring(52, 60)));
        configData.setSPORT(Long.parseLong(data.substring(60, 64), 16));
        configData.setDNSS(HexUtil.hexToIp(data.substring(64, 72)));
        configData.setRTVI(Integer.parseInt(data.substring(72, 76), 16));
        configData.setLPORT(data.substring(76, 80));
        configData.setZONE(data.substring(80, 82));
        configData.setLIP(data.substring(82, 90));
        configData.setDEVPWD(data.substring(90, 106));
        configData.setREV1(data.substring(106, 112));
        configData.setNAME(data.substring(112, 160));
        configData.setREV2(data.substring(160, 168));
        configData.setSSID(data.substring(168, 208));
        configData.setWPWD(data.substring(208, 248));

        vo.setDATA(JSON.toJSONString(configData));
    }

    /**
     * 组装注册消息
     * Data :
     * 1. NNO    1
     * 2. TYPE   1
     * 3. VER    2
     * 4. Model  2
     */
    private static void assembleRegister(CommToServerVo vo, List<String> dataList) {

        assembleHeader(vo, dataList);

        RegisterData registerData = new RegisterData();
        String data = dataList.get(9);
        registerData.setNNO(Integer.parseInt(data.substring(0, 2), 16));
        registerData.setTYPE(data.substring(2, 4));
        registerData.setVER(data.substring(4, 8));
        registerData.setModel(data.substring(8, 12));

        vo.setDATA(JSON.toJSONString(registerData));
    }

    /**
     * CMD - 0xAA
     * 组装心跳上报
     */
    private static void assembleHeartBeat(CommToServerVo vo, List<String> dataList) {
        assembleHeader(vo, dataList);
    }

    /**
     * Data : 循环 (total 28)
     *      1. No      2
     *      2. CODE    1
     *      3. EVT_ID  1
     *      4. EVT_LV  1
     *      5. EVT_RE  1
     *      6. EVT_EX  1
     *      7. EVT_SP  4
     *      8. TIME    4
     *      9. APX     13
     */
    private static void assemblePushEvent(CommToServerVo vo, List<String> dataList) {

        assembleHeader(vo, dataList);

        List<PushEventData> pushList = Lists.newArrayList();
        String data = dataList.get(9);
        for (int i=0; i<data.length()/56; i++) {
            PushEventData push = new PushEventData();
            String tmpData = data.substring(i*56, (i+1)*56);

            push.setNo(tmpData.substring(0, 4));
            push.setCODE(tmpData.substring(4, 6));
            push.setEVT_ID(tmpData.substring(6, 8));
            push.setEVT_LV(tmpData.substring(8, 10));
            push.setEVT_RE(tmpData.substring(10, 12));
            push.setEVT_EX(tmpData.substring(12, 14));
            push.setEVT_SP(tmpData.substring(14, 22));
            push.setTIME(tmpData.substring(22, 30));
            push.setAPX(tmpData.substring(30, 56));

            pushList.add(push);
        }

        vo.setDATA(JSON.toJSONString(pushList));
    }


    /**
     * Data :
     * 1. ASN  2
     * 2. NUM  1
     * 3. TYPE 1
     * 4. TIMW 4
     * 5. 循环 （total 15）
     *    NNO  1
     *    TYPE 1
     *    DN   1
     *    VOL  2
     *    LKI  2
     *    TMP  2
     *    CUR  2
     *    PS   4
     */
    private static void assembleAcmCount(CommToServerVo vo, List<String> dataList) {

        assembleHeader(vo, dataList);

        ACMData acmData = new ACMData();
        String data = dataList.get(9);

        acmData.setASN(data.substring(0, 4));
        acmData.setNUM(Integer.parseInt(data.substring(4,6), 16));
        acmData.setTYPE(data.substring(6, 8));
        acmData.setTIME(data.substring(8, 16));
        String rData = data.substring(16, data.length());
        List<ACMData.ACMNodeData> nodeList = Lists.newArrayList();
        for (int i=0; i<rData.length()/30; i++) {
            ACMData.ACMNodeData node = new ACMData.ACMNodeData();
            String tmpData = rData.substring(i*30, (i+1)*30);

            node.setNNO(tmpData.substring(0, 2));
            node.setTYPE(tmpData.substring(2, 4));
            node.setDN(Integer.parseInt(tmpData.substring(4, 6), 16));
            node.setVOL(Integer.parseInt(tmpData.substring(6, 10), 16));
            node.setLKI(Integer.parseInt(tmpData.substring(10, 14), 16));
            node.setTMP(Integer.parseInt(tmpData.substring(14, 18), 16));
            node.setCUR(Integer.parseInt(tmpData.substring(18, 22), 16));
            node.setPS(Integer.parseInt(tmpData.substring(22, 30), 16));

            nodeList.add(node);
        }
        acmData.setData(nodeList);

        vo.setDATA(JSON.toJSONString(acmData));
    }

    /**
     * Data
     * 1. TIME 4
     * 2. 循环 （total 22）
     *    NNO 节点号 1
     *    TYPE 类型  1
     *    VOL  电压值(V) 2
     *    LKI  漏电流值(0.1mA) 2
     *    PWR  功率值(W) 2
     *    TMP  温度值(0.1℃) 2
     *    CUR  电流值(10mA) 2
     *    CUR2 N相电流值（无意义） 2
     *    ALM  报警位（按位定义） 4
     *    PS   累计电量（无意义） 4
     */
    private static void assembleStatusReport(CommToServerVo vo, List<String> dataList) {

        assembleHeader(vo, dataList);

        String data = dataList.get(9);
        StatusReportData reportData = new StatusReportData();

        reportData.setTIME(String.valueOf(Integer.parseInt(data.substring(0, 8), 16)));
        String rData = data.substring(8, data.length());
        List<StatusReportData.StatusReport> reportList = Lists.newArrayList();
        for (int i=0; i< rData.length()/44; i++) {
            StatusReportData.StatusReport report = new StatusReportData.StatusReport();
            String tmpData = rData.substring(i*44, (i+1)*44);
            report.setNNO(tmpData.substring(0,2));
            report.setTYPE(tmpData.substring(2,4));
            report.setVOL(Integer.parseInt(tmpData.substring(4, 8), 16));
            report.setLKI(Integer.parseInt(tmpData.substring(8, 12), 16));
            report.setPWR(Integer.parseInt(tmpData.substring(12, 16), 16));
            report.setTMP(Integer.parseInt(tmpData.substring(16, 20), 16));
            report.setCUR(Integer.parseInt(tmpData.substring(20, 24), 16));
            report.setCUR2(tmpData.substring(24, 28));
            report.setALM(tmpData.substring(28, 36));
            report.setPS(Integer.parseInt(tmpData.substring(36, 44), 16));

            reportList.add(report);
        }

        reportData.setList(reportList);
        vo.setDATA(JSON.toJSONString(reportData));
    }

    /**
     * 组装数据的Header
     */
    private static void assembleHeader(CommToServerVo vo, List<String> dataList) {
        vo.setPVER(dataList.get(0));
        vo.setCMD(dataList.get(1));
        vo.setCMD_VER(dataList.get(2));
        vo.setCMD_VER2(dataList.get(3));
        vo.setLen(dataList.get(4));
        vo.setCMD_No(dataList.get(5));
        vo.setMAC(dataList.get(6));
        vo.setTIMEZONE(dataList.get(7));
        vo.setRSEV(dataList.get(8));
    }

    public static void main(String[] args) {
//        String content = "F1B30100007C000198CC4D1000B91080008A5433100598CC4D1000B90007C0A889F0FFFFFF00C0A88901784DE2521E4EDF0606060078171E10FFFFFFFF7A57A5A743894AE400FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF63657368693200FFFFFFFFFFFFFFFFFFFFFFFFFF6D616E64756E3939393900FFFFFFFFFFFFFFFFFFE9F57B0500000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
////        String content = "F1B10100007C000198CC4D1000B91080008A5433100598CC4D1000B90007C0A889F0FFFFFF00C0A88901784DE2521E4EDF0606060078171E10FFFFFFFF7A57A5A743894AE400FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF63657368693200FFFFFFFFFFFFFFFFFFFFFFFFFF6D616E64756E3939393900FFFFFFFFFFFFFFFFFFE2351F2F00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
//        List<String> list = comminuteData(content);
//        System.out.println(list);
//        for (String st : list) {
//            System.out.println(st);
//        }

        // 0xA0 info
        String a0Data = "F1A003100046583D98CC4D10036410005BCD43B0018000DE00000000020000010000C000000000000000028000DE0000000001C600000000C000000000000000038000DE0000000001CB00000000C00000000000000005DC846F";
        // 0xA2 info
        String a2Info = "F1A2F1060062088A98CC4D1000B910000054F1065BCD2F21008A0000000000000000000000000003802B0000000001A400000000000001802B0000000001CC00000000000002802B00000000019C00000000000004802B0000000001A400000000000005802B0000000001A2000000000000158EED34";
        // 0xA4 info
        String a4Info = "F1A4E10200388A1B98CC4D1000B9100801714F39000000FFFFFF00DFD7395B5B39D7DF5B39D7DF000000000001724F39000000FFFFFF009A0CCA5B5BCA0C9A5BCA0C9A0000000000060D6CD8";
        // 0xAA info
        String aaInfo = "F1AA000000008CBA98CC4D1000B910060CA632E6";
        // 0xB5 info
        String b5Info = "F1B405000190000198CC4D1000B910800380200800000000000000000001010400AF0000012C00001B80000000000384000012C000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000180400700BB0000000000000001010400640000012C000037000000000003840000258000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000280200800000000000000000001010400AF0000012C00001B80000000000384000012C000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000480140800000000000000000001010400AF0000012C0000113000000000038400000BB800000000000000000000000000000000000000000000000000000000000000000000000000000000000000000580140800000000000000000001010400AF0000012C0000113000000000038400000BB80000000000000000000000000000000000000000000000000000000000000000000000000000000000000000463B3FD6";

        CommToServerVo vo = handleData(a0Data);
        StatusReportData data = JSONObject.parseObject(vo.getDATA(), StatusReportData.class);
//        List<StatusReportData> list = JSONObject.parseArray(vo.getDATA(), StatusReportData.class);
//        System.out.println(data);

        double va = 22;
        System.out.println(va/1000);
        BigDecimal bigDecimal = new BigDecimal(va/1000);
        System.out.println(bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP));
    }

}
