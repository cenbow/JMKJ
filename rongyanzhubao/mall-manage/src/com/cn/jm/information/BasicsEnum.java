package com.cn.jm.information;

/**
 * 移步至 BasicsInformation 类设置信息
 * 由于有些信息的类型不同,所以无法设置为固定类型的枚举
 * 设置为Object类型会造成类型装换异常
 * @author Administrator
 */
@Deprecated
public enum BasicsEnum {
	/** 状态正常*/
	STATUS_NORMAL(0),
	/** 状态冻结*/
	STATUS_FROZEN(1);
	int value;
	BasicsEnum(int value){
		this.value= value;
	}
	public int getValue() {
		return value;
	}
}
