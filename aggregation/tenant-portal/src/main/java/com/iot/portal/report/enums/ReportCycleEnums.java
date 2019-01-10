package com.iot.portal.report.enums;

import com.iot.common.exception.BusinessException;
import com.iot.portal.exception.BusinessExceptionEnum;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：聚合层
 * 功能描述：报表查询周期枚举
 * 创建人： maochengyuan
 * 创建时间：2019/1/9 14:57
 * 修改人： maochengyuan
 * 修改时间：2019/1/9 14:57
 * 修改描述：
 */
public enum ReportCycleEnums {

	CYCLE_1("1", "1"),

	CYCLE_7("7", "7"),

	CYCLE_14("14", "14"),

	CYCLE_30("30", "30"),

	;

	/** 编码*/
	private String code;

	/** 描述*/
	private String desc;

	public static ReportCycleEnums getCycleEnums(String code){
		for(ReportCycleEnums enums : ReportCycleEnums.values()){
			if(enums.getCode().equals(code)){
				return enums;
			}
		}
		throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR);
	}

	ReportCycleEnums(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

}
