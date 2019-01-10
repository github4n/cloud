package com.iot.appversion;

import com.iot.tenant.util.CheckVersionUtil;

public class AppVersionTest {

    public static void main(String[] args) {
        System.out.println(0 == CheckVersionUtil.compareVersion("", ""));
        System.out.println(0 == CheckVersionUtil.compareVersion("1.0.0", ""));
        System.out.println(0 == CheckVersionUtil.compareVersion("1.0.0", "2.0"));
        System.out.println(0 == CheckVersionUtil.compareVersion("1.0.0", "1.0.0"));
        System.out.println(0 == CheckVersionUtil.compareVersion("2.0.0", "1.0.0"));
        System.out.println(0 == CheckVersionUtil.compareVersion("1.0.1", "1.0.0"));
        System.out.println(1 == CheckVersionUtil.compareVersion("1.0.0", "1.0.1"));
        System.out.println(2 == CheckVersionUtil.compareVersion("1.0.0", "1.0.2"));
        System.out.println(2 == CheckVersionUtil.compareVersion("1.0.0", "2.0.0"));
        System.out.println(2 == CheckVersionUtil.compareVersion("1.0.0", "1.0.0.1"));
        System.out.println(0 == CheckVersionUtil.compareVersion("2.5.0", "2.4.1"));
        System.out.println(0 == CheckVersionUtil.compareVersion("2.5.4", "2.4.1"));
        System.out.println(0 == CheckVersionUtil.compareVersion("2.5.4", "2.5.1"));
    }
}
