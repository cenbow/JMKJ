package com.cn.jm._dao.zan;

public enum ZanEnum {
	
	/** 商品评论*/
	GOODS_STAR_TYPE(4),
	
	/**直播间点赞*/
	ROOM_TYPE(5);
	
	private final int value;

	private ZanEnum(int value) {
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
