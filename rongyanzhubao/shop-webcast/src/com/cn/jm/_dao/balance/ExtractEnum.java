package com.cn.jm._dao.balance;

public enum ExtractEnum {

	/**支出*/
	TYPE_OUT(0),
	/** 收入*/
	TYPE_IN(1),
	
	/** 邀请返利*/
	MSG_TYPE_INVITE_COMMISSION(1),
	/** 提现*/
	MSG_TYPE_EXTRACT(2),
	/** 提现失败*/
	MSG_TYPE_EXTRACTFAIL(3),
	/** 出售商品*/
	MSG_TYPE_SALE(4),
	/** 转售商品收益*/
	MSG_TYPE_RESALE(5),
	/** 分佣*/
	MSG_TYPE_SLAE_COMMISSION(6),

	/** 提现待处理*/
	EXTRACT_STATE_EXAMINING(0),
	
	/** 提现已同意*/
	EXTRACE_STATE_AGREE(1),
	
	/** 提现不通过*/
	EXTRACE_STATE_REFUSE(2);
	
	int value;
	
	ExtractEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
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
