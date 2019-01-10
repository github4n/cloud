package com.iot.payment.enums.order;

/**
 * 项目名称：cloud
 * 功能描述：订单记录-订单类型（对应sys_dict_item的type_id=2）
 * 创建人： yeshiyuan
 * 创建时间：2018/7/3 11:59
 * 修改人： yeshiyuan
 * 修改时间：2018/7/3 11:59
 * 修改描述：
 */
public enum OrderTypeEnum {

	VIDEO_PLAN(1,"录影计划"),
	UUID_PROGRAMME(2,"UUID方案"),
	APP_PACK(3,"APP打包"),
	VIDEO_SERVICE(4, "语音接入")
	;


	/** 编码*/
	private int code;

	/** 描述*/
	private String desc;


	private OrderTypeEnum(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
	
	public static boolean checkOrderType(Integer orderType) {
		boolean result = false;
		for (OrderTypeEnum orderTypeEnum:OrderTypeEnum.values()) {
			if (orderTypeEnum.getCode() == orderType){
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	  * @despriction：校验虚拟服务类型
	  * @author  yeshiyuan
	  * @created 2018/9/14 10:23
	  * @param null
	  * @return
	  */
	public static boolean checkServiceType(Integer orderType) {
		if (orderType == null) {
			return false;
		}
		if (orderType.equals(VIDEO_SERVICE.getCode())) {
			return true;
		}else if (orderType.equals(APP_PACK.getCode())) {
			return true;
		}
		return false;
	}
}
