package com.iot.tenant.enums;

import com.iot.common.util.StringUtil;

/**
 * 项目名称：cloud
 * 功能描述：语言类型枚举
 * 创建人： yeshiyuan
 * 创建时间：2018/9/29 15:47
 * 修改人： yeshiyuan
 * 修改时间：2018/9/29 15:47
 * 修改描述：
 */
public enum LangTypeEnum {
    ZH_CN("zh_CN"),
    EN_US("en_US"),
    ZH_TW("zh_TW"),
    EUC_Kr("euc_Kr"),
    JA_JP("ja_JP"),
    SP_SA("sp_SA"),
    GR_GE("gr_GE"),
    FR_FR("fr_FR");

    private String langType;

    LangTypeEnum(String langType) {
        this.langType = langType;
    }

    public String getLangType() {
        return langType;
    }

    public void setLangType(String langType) {
        this.langType = langType;
    }

    /**
      * @despriction：校验是否是默认语言类型（中英文）
      * @author  yeshiyuan
      * @created 2018/9/29 15:52
      * @return
      */
    public static boolean checkDefaultLang(String langType) {
        if (StringUtil.isBlank(langType)) {
            return false;
        }
        if (langType.equals(LangTypeEnum.ZH_CN.getLangType())
                || langType.equals(LangTypeEnum.EN_US.getLangType())) {
            return true;
        }
        return false;
    }

    /**
      * @despriction：校验语言类型
      * @author  yeshiyuan
      * @created 2018/9/29 15:55
      * @return
      */
    public static boolean checkLangType(String langType) {
        if (StringUtil.isBlank(langType)) {
            return false;
        }
        for (LangTypeEnum lang : LangTypeEnum.values()) {
            if (langType.equals(lang.getLangType())) {
                return true;
            }
        }
        return false;
    }
}
