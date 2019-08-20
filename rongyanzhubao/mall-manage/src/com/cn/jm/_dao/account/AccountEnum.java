package com.cn.jm._dao.account;

public enum AccountEnum {
	/** 冻结*/
	STATE_FZ(1),
	
	/** 超级管理员*/
	TYPE_SUPER(-1),
	/** 系统管理员*/
	TYPE_SYSTEM(0),
	/**普通用户*/
	TYPE_USER(1),
	/**商家*/
	TYPE_SHOP(2),
	/**主播*/
	TYPE_ANCHOR(3),
	/**房管*/
	TYPE_MANAGER(4);
	
	private final int value;

	private AccountEnum(int value) {
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
