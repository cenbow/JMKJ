package com.cn.jm.util.rongyun.messages;

import com.cn.jm.util.rongyun.util.GsonUtil;

/**
 * 自定义红包提醒信息
 */
public class RedTipMessage extends BaseMessage {
	
	private Integer redId;
	private Integer types;//0 red 1air
	private Integer receiveUserId;
	private String receiveUserName = "";
	private Integer sendUserId;
	private String sendUserName = "";
	private Integer redCount;
	private Boolean isLast;
	private transient static final String TYPE = "XP:RedTipMsg";

	public RedTipMessage(Integer redId, Integer receiveUserId,
			String receiveUserName, Integer sendUserId, String sendUserName,
			Integer redCount, Boolean isLast,Integer types) {
		this.redId = redId;
		this.receiveUserId = receiveUserId;
		this.receiveUserName = receiveUserName;
		this.sendUserId = sendUserId;
		this.sendUserName = sendUserName;
		this.redCount = redCount;
		this.isLast = isLast;
		this.types = types ;
	}

	public Integer getRedId() {
		return redId;
	}

	public void setRedId(Integer redId) {
		this.redId = redId;
	}

	public Integer getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(Integer receiveUserId) {
		this.receiveUserId = receiveUserId;
	}

	public String getReceiveUserName() {
		return receiveUserName;
	}

	public void setReceiveUserName(String receiveUserName) {
		this.receiveUserName = receiveUserName;
	}

	public Integer getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(Integer sendUserId) {
		this.sendUserId = sendUserId;
	}

	public String getSendUserName() {
		return sendUserName;
	}

	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}

	public Integer getRedCount() {
		return redCount;
	}

	public void setRedCount(Integer redCount) {
		this.redCount = redCount;
	}

	public Boolean getIsLast() {
		return isLast;
	}

	public void setIsLast(Boolean isLast) {
		this.isLast = isLast;
	}

	@Override
	public String getType() {
		return TYPE;
	}

	
	public void setTypes(Integer types) {
		this.types = types;
	}

	public Integer getTypes() {
		return types;
	}

	@Override
	public String toString() {
		return GsonUtil.toJson(this, RedTipMessage.class);
	}

}
