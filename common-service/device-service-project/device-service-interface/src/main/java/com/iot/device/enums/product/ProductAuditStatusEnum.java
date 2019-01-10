package com.iot.device.enums.product;

/**
 * 项目名称：cloud
 * 功能描述：产品审核状态
 * 创建人： yeshiyuan
 * 创建时间：2018/10/24 15:43
 * 修改人： yeshiyuan
 * 修改时间：2018/10/24 15:43
 * 修改描述：
 */
public enum  ProductAuditStatusEnum {
    WAIT_AUDIT(0,"待处理"),
    AUDIT_FAIL(1,"处理失败"), // 审核未通过
    AUDIT_SUCCESS(2,"处理成功"); // 审核成功

    private int value;
    private String desc;

    ProductAuditStatusEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDesc(Integer value) {
        if (value == null) {
            return "";
        }
        String desc = "";
        for (ProductAuditStatusEnum auditStatus:ProductAuditStatusEnum.values()) {
            if (value.equals(auditStatus.getValue())) {
                desc = auditStatus.getDesc();
                break;
            }
        }
        return desc;
    }

    /**
     * @despriction：校验语言类型
     * @author  maochengyuan
     * @created 2018/10/24 15:55
     * @return
     */
    public static boolean checkAuditResult(Integer auditStatus) {
        if (auditStatus == null) {
            return false;
        }
        if (auditStatus.equals(AUDIT_FAIL.getValue()) || auditStatus.equals(AUDIT_SUCCESS.getValue())) {
            return true;
        }
        return false;
    }
}
