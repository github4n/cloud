package com.iot.tenant.entity;

import com.iot.common.exception.BusinessException;
import com.iot.tenant.exception.TenantExceptionEnum;
import com.iot.tenant.vo.req.lang.LangInfoReq;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：配网步骤常量key
 * 创建人： yeshiyuan
 * 创建时间：2018/10/15 10:06
 * 修改人： yeshiyuan
 * 修改时间：2018/10/15 10:06
 * 修改描述：
 */
public class NetworkStepKey {

    public static String next = "next";

    public static String desc = "description";

    public static String icon = "icon";

    public static String help = "help";

    public static String preIcon = "default_";

    /**
     * 获取步骤对应的key
     * @param deviceTypeId
     * @param networkTypeId
     * @param step
     * @return
     */
    public static String getBaseKeyPre(Long deviceTypeId, Long networkTypeId, Integer step) {
        return deviceTypeId + "_" + networkTypeId + "_" + step + ":";
    }

    /**
     * 获取步骤对应帮助文案的key
     * @param deviceTypeId
     * @param networkTypeId
     * @param step
     * @return
     */
    public static String getBaseHelpKeyPre(Long deviceTypeId, Long networkTypeId, Integer step) {
        return deviceTypeId + "_" + networkTypeId + "_" + step + "_" + help + ":";
    }

    public static String getTenantKeyPre(Long appId, Long productId, Long networkTypeId, Integer step) {
        return appId + "_" + productId + "_" + networkTypeId + "_" + step + ":";
    }

    public static String getTenantHelpKeyPre(Long appId, Long productId, Long networkTypeId, Integer step) {
        return appId + "_" + productId + "_" + networkTypeId + "_" + step + "_" + help + ":";
    }

    public static void checkLangKey(List<LangInfoReq> langInfoReqs) {
        for (LangInfoReq lang : langInfoReqs) {
            String key = lang.getKey();
            if (!(key.contains(next) || key.contains(desc) || key.contains(icon) || key.contains(help))) {
                throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "key("+key +") format error");
            }
            LangInfoReq.checkDefaultVal(lang);
        }
    }

    public static void checkTenantLangKey(List<LangInfoReq> langInfoReqs) {
        for (LangInfoReq lang : langInfoReqs) {
            String key = lang.getKey();
            if (!(key.contains(next) || key.contains(desc) || key.contains(icon) || key.contains(help))) {
                throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "key("+key +") format error");
            }
            LangInfoReq.checkTenantVal(lang);
        }
    }


    /**
      * @despriction：去除前缀
      * @author  yeshiyuan
      * @created 2018/10/17 10:42
      * @return
      */
    public static LangInfoReq removePre(String preKey, LangInfoReq langInfoReq) {
        langInfoReq.setKey(langInfoReq.getKey().replace(preKey, ""));
        return langInfoReq;
    }
}
