package com.cn.jm._dao.label;

public enum LabelEnum {
	/** 系统默认*/
	SYSYEM_TYPE(0),
	/** 文章*/
	ARTICLE_TYPE(1),
	/** 商品*/
	SHOP_TYPE(2),
	/** 用户*/
	USER_TYPE(3),
	/** 电商栏目*/
	SHOP_COLUMN_TYPE(4),
	/** 直播栏目*/
	WEBCAST_COLUMN_TYPE(5);
	
	int code;
	LabelEnum(int code){
		this.code = code;
	}
	public int getCode() {
		return code;
	}
	public String toString() {
		return String.valueOf(code);
	}
}
