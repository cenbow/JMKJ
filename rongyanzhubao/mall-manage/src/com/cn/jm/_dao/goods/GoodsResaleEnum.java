package com.cn.jm._dao.goods;

public enum GoodsResaleEnum {
	/** 原图片*/
	ORIGINAL_IMAGE(0),
	/** 新上传的图片*/
	NEWLY_ADDED(1);
	
	int value;
	GoodsResaleEnum(int value){
		this.value = value;
	}
	public int getValue() {
		return value;
	}
	public boolean identical(int value) {
		return this.value == value;
	}
	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
