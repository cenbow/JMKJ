package com.cn.jm._dao.img;

/**
 * 
 * @author Administrator
 * type :0默认 1商品详情图 2广告 3商品主图
 */
public enum ImgEnum {
	/** 商品详情图 */
	GOODS_DETAILS_TYPE(1),
	/** 商品主图*/
	GOODS_OWNER_TYPE(3),
	/** 转售商品详情图*/
	RESALE_GOODS__DETAILS_TYPE(5);
	int code;
	ImgEnum(int code){
		this.code = code;
	}
	int getCode() {
		return code;
	}
	@Override
	public String toString() {
		return String.valueOf(code);
	}
}
