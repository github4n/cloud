package com.iot.payment.enums.order;

/**
 * 项目名称：cloud
 * 功能描述：订单记录-订单状态（对应sys_dict_item的type_id=3）
 * 创建人： yeshiyuan
 * 创建时间：2018/7/3 11:59
 * 修改人： yeshiyuan
 * 修改时间：2018/7/3 11:59
 * 修改描述：
 */
public enum OrderRecordStatusEnum {

	CLOSE(0,"已关闭"),
	WAIT_PAY(1,"待付款"),
	PAY_SUCCESS(2,"付款成功"),
	PAY_FAIL(3,"付款失败"),
	REFUNDING(4,"退款中"),
	REFUND_SUCCESS(5,"退款成功"),
	REFUND_FAIL(6,"退款成功")
	;


	/** 编码*/
	private Integer code;

	/** 描述*/
	private String desc;


	private OrderRecordStatusEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public static boolean checkOrderStatus(Integer orderStatus) {
		if (orderStatus == null){
			return false;
		}
		boolean result = false;
		for (OrderRecordStatusEnum orderRecordStatusEnum : OrderRecordStatusEnum.values()) {
			if (orderRecordStatusEnum.getCode() == orderStatus){
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	  * @despriction：校验退款前提
	  * @author  yeshiyuan
	  * @created 2018/11/13 17:52
	  * @return
	  */
	public static boolean checkRefundPremise(Integer orderStatus) {
		if (orderStatus == null){
			return false;
		}
		boolean result = false;
		if (PAY_SUCCESS.getCode().equals(orderStatus) || REFUND_FAIL.getCode().equals(orderStatus)) {
			result = true;
		}
		return result;
	}
}
