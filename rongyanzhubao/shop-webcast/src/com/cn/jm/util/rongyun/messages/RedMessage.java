package com.cn.jm.util.rongyun.messages;

import com.cn.jm.util.rongyun.util.GsonUtil;

/**
 * 自定义红包消息
 */
public class RedMessage extends BaseMessage {
	
	private Integer redId;
	private Integer coinId;
	private String name = "";
	private transient static final String TYPE = "XP:RedMsg";

	public RedMessage(Integer redId, String name,Integer coinId) {
		this.redId = redId;
		this.name = name;
		this.coinId = coinId;
	}

	public Integer getRedId() {
		return redId;
	}

	public void setRedId(Integer redId) {
		this.redId = redId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getType() {
		return TYPE;
	}

	public Integer getCoinId() {
		return coinId;
	}

	public void setCoinId(Integer coinId) {
		this.coinId = coinId;
	}

	@Override
	public String toString() {
		return GsonUtil.toJson(this, RedMessage.class);
	}
	

}
