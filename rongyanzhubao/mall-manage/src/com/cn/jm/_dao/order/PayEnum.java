package com.cn.jm._dao.order;

public enum PayEnum {

	ALI_APP_PAY(0), WEIXIN_APP(1), BALANCE(2), WEIXIN_JSAPI(3),BANK(4);

	private int payType;
	public int getPayType() {
		return this.payType;
	}

	PayEnum(int payType) {
		this.payType = payType;
	}
	public boolean equals(int payType) {
		return this.payType == payType;
	}
}
