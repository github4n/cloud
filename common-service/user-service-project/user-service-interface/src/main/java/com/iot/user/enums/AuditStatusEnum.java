package com.iot.user.enums;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人： 李帅
 * 创建时间：2018年10月22日 下午6:07:16
 * 修改人：李帅
 * 修改时间：2018年10月22日 下午6:07:16
 */
public enum AuditStatusEnum {
//	0:未审核 1:审核未通过 2:审核通过
    UNREVIEWED(0, "未审核"),

    AUDITFAILED(1, "审核未通过"),

    NORMAL(2, "审核通过")

	;

    AuditStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

	/** 类型编码 */
    private Integer code;

	/** 描述 */
    private String desc;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
