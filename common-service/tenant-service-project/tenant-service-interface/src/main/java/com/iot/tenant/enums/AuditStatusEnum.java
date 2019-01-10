package com.iot.tenant.enums;

/**
 * 项目名称：cloud
 * 功能描述：审核状态枚举
 * 创建人： maochengyuan
 * 创建时间：2018/10/24 15:55
 * 修改人： maochengyuan
 * 修改时间：2018/10/24 15:55
 * 修改描述：
 */
public enum AuditStatusEnum{

    /**未审核*/
    Pending((byte) 0),

    /**审核未通过*/
    UnPassed((byte) 1),

    /**审核通过*/
    Passed((byte) 2),

    /**
     * 消息留言
     */
    LeaveMessage((byte)3),

    /**
     * 反馈消息
     */
    FeedbackMessage((byte)4);

    private Byte auditStatus;

    AuditStatusEnum(Byte auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Byte getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Byte auditStatus) {
        this.auditStatus = auditStatus;
    }

    /**
      * @despriction：校验语言类型
      * @author  maochengyuan
      * @created 2018/10/24 15:55
      * @return
      */
    public static boolean checkAuditStatus(Byte auditStatus) {
        if (auditStatus == null) {
            return false;
        }
        for (AuditStatusEnum lang : AuditStatusEnum.values()) {
            if (auditStatus.equals(lang.getAuditStatus())) {
                return true;
            }
        }
        return false;
    }
}
