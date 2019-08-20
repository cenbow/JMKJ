package com.cn.jm._dao.order;

public enum OrderState {
	
	/** 未支付 */
	WAIT_PAY_STATE(0),
	/** 正在支付中  */
	WAIT_SERVE_STATE(1),
	/** 待发货(已支付) */
	TO_BE_RECEIVED_STATE(2),
	/** 支付失败 */
	PAY_FAIL_STATE(3),
	/** 正在退款中(state字段)  */
	IN_REFUND_SERVE_STATE(4),
	/** 取消订单 */
	CANCELLATION_OF_ORDER_STATE(6),
	/** 已发货 */
	SHIPPED_STATE(7),
	/** 待评价 (已收货)*/
	TO_BE_EVALUATED_STATE(8),
	/** 已评价完成 */
	COMPLETED_STATE(9),
	/** 售后关闭*/
	AFTER_SUCCESS_STATE(10),
	/** 正常退款中(refundState字段)*/
	NORMAL_REFUND_STATE(0),
	/** 退款中*/
	IN_AFTER_REFUND_STATE(1),
	/** 退款过*/
	AFTER_SUCCESS_REFUND_STATE(2),
//	/** 退款过*/
//	SECOND_REFUND_STATE(3),
	/** 商城订单状态*/
	SHOP_ORDER_TYPE(0),
	/** 直播订单状态*/
	WEBCAST_ORDER_TYPE(1),
	/** 正常订单*/
	NORMAL_TYPE(0),
	/** 异常订单*/
	ABNORMAL_TYPE(1);
	
	private int code;
	
	private OrderState(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	public boolean equals(int code) {
		return this.code == code;
	}
	public boolean equals(Integer code) {
		return code.intValue() == this.code;
	}
}
