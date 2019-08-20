package com.cn.jm.util.rongyun.messages;

import com.cn.jm.util.rongyun.util.GsonUtil;

/**
 * 自定义签到消息
 */
public class SignMessage extends BaseMessage {
	
	private Integer groupId;
	private Integer userId;
	private Integer days;
	private Integer number;
	private String name = "";
	private String head = "";
	private String nick = "";
	private transient static final String TYPE = "XP:SignMsg";

	public SignMessage(Integer groupId, String name,Integer userId,Integer days,Integer number,String nick ,String head) {
		this.groupId = groupId;
		this.name = name;
		this.userId = userId;
		this.days = days;
		this.number = number;
		this.head = head;
		this.nick = nick;
	}

	

	public String getHead() {
		return head;
	}



	public void setHead(String head) {
		this.head = head;
	}



	public String getNick() {
		return nick;
	}



	public void setNick(String nick) {
		this.nick = nick;
	}



	public Integer getGroupId() {
		return groupId;
	}



	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}



	public Integer getUserId() {
		return userId;
	}



	public void setUserId(Integer userId) {
		this.userId = userId;
	}



	public Integer getDays() {
		return days;
	}



	public void setDays(Integer days) {
		this.days = days;
	}



	public Integer getNumber() {
		return number;
	}



	public void setNumber(Integer number) {
		this.number = number;
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

	

	@Override
	public String toString() {
		return GsonUtil.toJson(this, SignMessage.class);
	}
	

}
