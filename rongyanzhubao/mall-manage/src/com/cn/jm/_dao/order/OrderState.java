package com.cn.jm._dao.order;

public enum OrderState {
	
	/**
	 * 待支付
	 */
	WAIT_PAY(0),
	/**
	 * 等待服务
	 */
	WAIT_SERVE(1),
	/**
	 * 取消
	 */
	CANCEL(2),
	
	/**
	 * 店员开始服务 等待买家确认
	 */
	WAIT_CUSTOMER_SURE(3),
	
	/**
	 * 买家确认，服务中 
	 */
	START_SERVE(4),
	/**
	 * 待评价
	 */
	WAIT_COMMENT(5),
	/**
	 * 已完成
	 */
	FINSH(6),
	/**
	 * 申请退款
	 */
	APPLY_REFUND(7),
	/**
	 * 已关闭
	 */
	CLOSE(8);
	
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
