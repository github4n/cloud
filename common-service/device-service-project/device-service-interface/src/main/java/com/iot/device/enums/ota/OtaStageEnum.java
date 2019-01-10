package com.iot.device.enums.ota;

/** 
 * 
 * 项目名称：IOT云平台
 * 模块名称：设备
 * 功能描述：分位枚举类
 * 创建人： nongchongwei
 * 创建时间：2018/7/25 10:04
 * 修改人： nongchongwei
 * 修改时间：2018/7/25 10:04
 * 修改描述：
 */
public enum OtaStageEnum {

    IDLE(0, "空闲"),
    DOWNLOADING (1, "正在下载"),
    DOWNLOADED(2, "下载完成"),
    UPGRADE(3, "等待升级"),
    INSTALLING(4, "正在安装"),
    SUCCESS(5, "完成安装 (重启)"),
    FAILED(6, "升级失败"),
    BUSY(7, "设备在忙")
    ;

    private Integer value;

    private String desc;

    OtaStageEnum(Integer value, String desc) {
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
     * 描述：判断是否属于此枚举
     * @author maochengyuan
     * @created 2018/7/25 10:00
     * @param value
     * @return boolean
     */
    public static boolean checkIsValid(int value){
        for (OtaStageEnum item: OtaStageEnum.values()) {
            if (value == item.getValue()){
                return true;
            }
        }
        return false;
    }
}
