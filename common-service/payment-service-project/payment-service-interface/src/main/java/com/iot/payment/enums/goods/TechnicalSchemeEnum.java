package com.iot.payment.enums.goods;

/**
  * @despriction：描述
  * @author  yeshiyuan
  * @created 2018/12/6 15:58
  */
public enum  TechnicalSchemeEnum {

    wifi(1L,"wifi直连方案"),
    bluetooth(2L,"蓝牙方案"),
    gateway(3L,"网关方案"),
    ipc(4L,"ipc方案");

    /** 编码*/
    private Long code;

    /** 描述*/
    private String desc;

    TechnicalSchemeEnum(Long code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Long getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
      * @despriction：校验是否是直连
      * @author  yeshiyuan
      * @created 2018/12/11 10:42
      */
    public static Integer checkIsDirect(Long technicalId) {
        if (technicalId == null) {
            return 0;
        }
        if (wifi.getCode().equals(technicalId) || ipc.getCode().equals(technicalId)) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
      * @despriction：校验拷贝配网是否通过技术方案
      * @author  yeshiyuan
      * @created 2018/12/11 17:03
      */
    public static boolean copyNetworkByTechnical(Long technicalId) {
        if (bluetooth.getCode().equals(technicalId) || ipc.getCode().equals(technicalId) || wifi.getCode().equals(technicalId)) {
            return true;
        } else {
            return false;
        }
    }
}
