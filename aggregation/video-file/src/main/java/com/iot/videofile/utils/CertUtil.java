package com.iot.videofile.utils;

import com.iot.common.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 *@description 解析ssl Cert证书中的信息工具类
 *@author wucheng
 *@create 2018/12/19 9:54
 */
public class CertUtil {

    /**
     *@description 解析cert证书
     *@author wucheng
     *@params [sslCert]
     *@create 2018/12/19 10:27
     *@return java.util.Map<java.lang.String,java.lang.String>
     */
    public static Map<String, String> analysisCert(String sslCert) {
        String[] results = null;
        if (StringUtil.isNotBlank(sslCert)) {
            String cert = sslCert.replace("/", ",");
            results = cert.split(",");
        }
        return toMap(results);
    }

    /**
     *@description 获取CN设备id
     *@author wucheng
     *@params [sslCert]
     *@create 2018/12/19 10:21
     *@return java.lang.String
     */
    public static String getDeviceId(String sslCert) {
        if (StringUtil.isNotBlank(sslCert)) {
            String cert = sslCert.replace("/", ",");
            String[] results = cert.split(",");
            Map<String, String> map = toMap(results);
            if (map != null && map.containsKey("CN")) {
                return map.get("CN");
            }
        }
        return null;
    }

    /**
     *@description 将数组装换成map
     *@author wucheng
     *@params [args]
     *@create 2018/12/19 10:27
     *@return java.util.Map<java.lang.String,java.lang.String>
     */
    private static Map<String, String> toMap(String[] args) {
        Map<String, String> map = new HashMap<>();
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].indexOf("=") > 0) {
                    String[] result = args[i].split("=");
                    if (result != null && result.length > 0) {
                        map.put(result[0], result[1]);
                    }
                }
            }
        }
        return map;
    }
    // 测试
    public static void main(String [] args) {
        String str = "C=CN,ST=ShenZhen,O=Leedarson Lighting Co. Ltd.,CN=e6e76f0e7f534efdadcb2e7e94592e33";
        System.out.println("返回证书包含信息： " + analysisCert(str));
        System.out.println("返回证书设备id： " +getDeviceId(str));
    }
}
