package com.iot.device.enums;

/**
 * 项目名称：cloud
 * 功能描述：模组ifttt类型枚举
 * 创建人： yeshiyuan
 * 创建时间：2018/10/23 14:10
 * 修改人： yeshiyuan
 * 修改时间：2018/10/23 14:10
 * 修改描述：
 */
public enum ModuleIftttTypeEnum {
    //0:不支持 1：支持if 2:支持then 3:支持if支持then
    NO(0,"不支持"),
    IF(1,"if"),
    THEN(2,"then"),
    IF_TEHN(3,"if_then");

    private Integer value;

    private String desc;

    ModuleIftttTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
      * @despriction：校验是否已选择if
      * @author  yeshiyuan
      * @created 2018/10/23 14:21
      * @return
      */
    public static Integer checkChooseIf(Integer portalIftttType) {
        if (IF.getValue().equals(portalIftttType) || IF_TEHN.getValue().equals(portalIftttType)) {
            return 1;
        }else {
            return 0;
        }
    }
    /**
     * @despriction：校验是否已选择then
     * @author  yeshiyuan
     * @created 2018/10/23 14:21
     * @return
     */
    public static Integer checkChooseThen(Integer portalIftttType) {
        if (THEN.getValue().equals(portalIftttType) || IF_TEHN.getValue().equals(portalIftttType)) {
            return 1;
        }else {
            return 0;
        }
    }

    /**
      * @despriction：描述
      * @author  yeshiyuan
      * @created 2018/12/10 11:58
      */
    public static boolean checkModuleDetail(String iftttType) {
        if (THEN.getDesc().equals(iftttType) || IF.getDesc().equals(iftttType)) {
            return true;
        }else {
            return false;
        }
    }
}
