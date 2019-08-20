package com.cn.jm._dao.goods;

public enum GoodsEnum {
	/** 上架*/
	UPPER_SHELF_STATE(0),
	/** 下架*/
	LOWER_SHELF_STATE(1),
	/** 删除*/
	DELETE_STATE(2),
	/** 已出售*/
	NOT_SELL_STATE(0),
	/** 未出售*/
	CAN_SELL_STATE(1),
	/** 商城端商品*/
	SHOP_GOODS_TYPE(0),
	/** 直播端商品*/
	WEBCAST_GOODS_TYPE(1);
	
	int code;
	GoodsEnum(int code){
		this.code = code;
	}
	public int getCode() {
		return code;
	}
	public boolean identical(int code) {
		return this.code == code;
	}
	@Override
	public String toString() {
		return String.valueOf(code);
	}
}
