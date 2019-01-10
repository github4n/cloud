package com.iot.report.utils;

public class RedisKeyUtils {

    public static final String DEV_ACTIVATED = "dev-activated:%s:%d";

    public static final String DEV_DISTRIBUTION = "dev-distribution:%s";

    public static final String DEV_IP = "dev-ip:%s";

    public static final String DEV_DISTRIBUTION_ACTIVE_ACTIVATE = "dev-distribution-all";

    public static final String DEV_DISTRIBUTION_ACTIVE = "dev-distribution-active";

    public static final String DEV_DISTRIBUTION_ACTIVATE = "dev-distribution-activate";

    public static String getDevActivatedKey(String date, Long tenantId) {
        return String.format(DEV_ACTIVATED, date, tenantId);
    }

    public static String getDistributionKey(String deviceId) {
        return String.format(DEV_DISTRIBUTION, deviceId);
    }

    public static String getDevIpKey(String deviceId) {
        return String.format(DEV_IP, deviceId);
    }

    public static String getDevDistributionActiveActivateKey() {
        return DEV_DISTRIBUTION_ACTIVE_ACTIVATE;
    }
    public static String getDevDistributionActiveKey() {
        return DEV_DISTRIBUTION_ACTIVE;
    }
    public static String getDevDistributionActivateKey() {
        return DEV_DISTRIBUTION_ACTIVATE;
    }
}
