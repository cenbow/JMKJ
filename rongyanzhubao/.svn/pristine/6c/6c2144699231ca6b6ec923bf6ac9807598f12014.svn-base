package com.cn.jm._dao.content;


public enum ContentEnum {

	/** 开户行id*/
	OPENING_BANK_ID(8),
	/** 收款账户名称*/
	PAYMENT_NAME_ID(9),
	/** 收款账号*/
	PAYMENT_ACCOUNT_ID(10);
	
	int value;
	ContentEnum(int value){
		this.value = value;
	}
	public int getValue() {
		return value;
	}
	@Override
	public String toString() {
		return String.valueOf(value);
	}
	public boolean equals(int value) {
		return value == this.value;
	}
	public boolean equals(Integer value) {
		return value != null && value.intValue() == this.value;
	}
	
}
