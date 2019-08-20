package com.cn.jm._dao.area;

public enum AreaEnum {
	/** 国*/
	COUNTRY_LEVELTYPE(0),
	/** 省*/
	PROVINCE_LEVELTYPE(1),
	/** 市*/
	CITY_LEVELTYPE(2),
	/** 区*/
	AREA_LEVELTYPE(3);
	
	int value;
	AreaEnum(int value){
		this.value = value;
	}
	public int getValue() {
		return value;
	}
	public boolean equals(int value) {
		return this.value == value;
	}
	public boolean equals(Integer value) {
		return this.value == value;
	}
	public String toString() {
		return String.valueOf(value);
	}
}
