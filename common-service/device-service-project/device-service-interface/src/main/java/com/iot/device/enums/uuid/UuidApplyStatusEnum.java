package com.iot.device.enums.uuid;

/**
 * 项目名称：cloud
 * 功能描述：uuid订单状态
 * 创建人： yeshiyuan
 * 创建时间：2018/6/29 14:57
 * 修改人： yeshiyuan
 * 修改时间：2018/6/29 14:57
 * 修改描述：
 */
public enum UuidApplyStatusEnum {

    //1:处理中;2:已完成;3:生成失败;4: P2PID不足
    WAIT_DEAL(0,"待处理"),
    DEALING(1,"处理中"),
    FINISH(2,"已完成"),
    GENERATE_FAIL(3,"生成失败"),
    P2PID_INSUFFICIENT(4,"P2PID不足");

    private Integer value;

    private String desc;

    UuidApplyStatusEnum(Integer value, String desc) {
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
      * @despriction：判断是否属于此枚举
      * @author  yeshiyuan
      * @created 2018/6/29 14:59
      * @param null
      * @return
      */
    public static boolean checkIsValid(int value){
        for (UuidApplyStatusEnum item: UuidApplyStatusEnum.values()) {
            if (value == item.getValue()){
                return true;
            }
        }
        return false;
    }
}
