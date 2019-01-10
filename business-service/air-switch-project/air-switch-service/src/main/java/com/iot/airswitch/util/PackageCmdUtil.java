package com.iot.airswitch.util;

import com.google.common.base.Strings;

import com.google.common.collect.Lists;
import com.iot.airswitch.constant.CMDEnum;
import com.iot.airswitch.constant.CommandEnum;
import com.iot.airswitch.constant.Constants;
import com.iot.airswitch.vo.data.NetConfigData;
import io.swagger.models.auth.In;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: Xieby
 * @Date: 2018/10/23
 * @Description: *
 */
public class PackageCmdUtil {

    /**
     * 拼装回复命令
     * PVER + CMD + CMD_VER + CMD_ACK + Len + CMD_No + TIME + UID + DATA(0) + CRC
     */
    public static String packageResponse(List<String> list) {

        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        StringBuffer sb = new StringBuffer();

        sb.append(list.get(0)) // PVER
          .append(list.get(1)) // CMD
          .append(list.get(2)) // CMD_VER
          .append(Constants.CMD_ACK) // CMD_ACK
          .append(HexUtil.getLen(null, 2))   // DATA Len
          .append(list.get(5)) // CMD No
          .append(HexUtil.getCurrentDateToHex()) // TIME
          .append(Constants.UID_NMM); // UID

        // 计算CRC32
        String crc = CRCUtil.crc32Cal(HexUtil.cmdToByte(sb.toString()));

        sb.append(crc);
        return sb.toString();
    }

    /**
     * 拼装一般请求命令
     */
    private static String packageRequestCmd(String cmd, String cmdVer, String data) {
        StringBuffer sb = new StringBuffer();

        String len = HexUtil.getLen(data, 2);

        sb.append(Constants.CMD_PVER)
          .append(cmd)
          .append(cmdVer)
          .append(Constants.CMD_ACK)
          .append(len)
          .append(HexUtil.getCmdNo())
          .append(HexUtil.getCurrentDateToHex())
          .append(Constants.UID_NMM);

        if (!Strings.isNullOrEmpty(data)) {
            sb.append(data);
        }

        // 计算CRC32
        String crc = CRCUtil.crc32Cal(HexUtil.cmdToByte(sb.toString()));

        sb.append(crc);

        // 增加换行符
//        sb.append("0A");

        return sb.toString();
    }

    /**
     * 获取网络配置
     */
    public static String packageGetNetConfig() {
        return packageRequestCmd(CMDEnum.GET_NET_CONFIG.code, Constants.CMD_VER, null);
    }

    /**
     * 获取电量信息
     */
    public static String packageGetGWConfig() {
        return packageRequestCmd(CMDEnum.GET_GW_CONFIG.code, Constants.CMD_VER,null);
    }

    /**
     * 修改服务器指向
     */
    public static String packageSetServerAddress(String ip, String port) {
        String data = ip + port;
        return packageRequestCmd(CMDEnum.COMMAND.code, CommandEnum.SERVER_SET.code, data);
    }

    /**
     * 合闸
     */
    public static String packageOpenNode(List<String> nodeList) {
        String data = getCommandData(nodeList);
        return packageRequestCmd(CMDEnum.COMMAND.code, CommandEnum.OPEN.code, data);
    }

    /**
     * 分闸
     */
    public static String packageCloseNode(List<String> nodeList) {
        String data = getCommandData(nodeList);
        return packageRequestCmd(CMDEnum.COMMAND.code, CommandEnum.CLOSE.code, data);
    }

    /**
     * 漏电测试
     */
    public static String packageLeakageTest(List<String> nodeList) {
        String data = getCommandData(nodeList);
        return packageRequestCmd(CMDEnum.COMMAND.code, CommandEnum.LEAKAGE_TEST.code, data);
    }

    /**
     * 修改上传时间间隔
     */
    public static String packageRTVI(Integer interval) {
        String rtvi = HexUtil.intToHexAuto(interval);
        return packageRequestCmd(CMDEnum.COMMAND.code, CommandEnum.RTVI.code, rtvi);
    }

    /**
     * 修改设备名称
     */
    public static String packageModifyName(NetConfigData config, String name) {
        String cmdHex = HexUtil.nameToHex(name);
//        String cmdHex = getHexName(name);
        StringBuffer sb = new StringBuffer();
        sb.append(config.getNNOStr())
          .append(config.getTYPE())
          .append(config.getMODEL())
          .append(config.getVER())
          .append(config.getMAC())
          .append(config.getCTRL())
          .append(config.getIP())
          .append(config.getMASK())
          .append(config.getGW())
          .append(config.getSIP())
          .append(config.getSPORTStr())
          .append(config.getDNSS())
          .append(config.getRTVIStr())
          .append(config.getLPORT())
          .append(config.getZONE())
          .append(config.getLIP())
          .append(config.getDEVPWD())
          .append(config.getREV1())
          .append(cmdHex)
          .append(config.getREV2())
          .append(config.getSSID())
          .append(config.getWPWD());

        return packageRequestCmd(CMDEnum.SET_NET_CONFIG.code, Constants.CMD_VER, sb.toString());
    }

    private static String getHexName(String name) {
        StringBuffer sb = new StringBuffer();
        String nameHex = HexUtil.bytesToHex(name.getBytes());
        if (nameHex.length() >= 48) {
            sb.append(nameHex.substring(0, 48));
        } else {
            sb.append(nameHex);
            for (int i=0; i < (48-nameHex.length()); i++) {
                sb.append("F");
            }
        }
        return sb.toString();
    }

    private static String getCommandData(List<String> nodeList) {
        StringBuffer sb = new StringBuffer();
        sb.append("00000000"); // PMT1
        sb.append("00000000"); // PMT2
        for (String node : nodeList) {
            sb.append(node);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("open cmd = " + packageOpenNode(Lists.newArrayList("01")).toUpperCase());
        System.out.println("close cmd = " +packageCloseNode(Lists.newArrayList("01")).toUpperCase());

        System.out.println(packageGetNetConfig().toUpperCase());
        System.out.println(packageGetGWConfig().toUpperCase());

    }

}
