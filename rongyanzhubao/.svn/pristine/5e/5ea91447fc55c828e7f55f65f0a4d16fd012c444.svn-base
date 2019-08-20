package com.cn.jm.util.rongyun.messages;

import com.cn.jm.util.rongyun.util.GsonUtil;

/**
 * 自定义空投糖果消息
 */
public class AirdropMessage extends BaseMessage {
	
	private Integer airId;
	private Integer coinId;
	private String name = "";
	private Integer userId;
	private String head ;
	private String nick;
	private String title;
	private String remark;
	private String groupIds;
	private transient static final String TYPE = "XP:airMsg";

	public AirdropMessage(Integer airId, String name,Integer coinId,Integer userId,String head,String nick,String title,String remark,String groupIds) {
		this.airId = airId;
		this.name = name;
		this.coinId = coinId;
		this.title = title;
		this.userId = userId;
		this.remark = remark ;
		this.nick = nick;
		this.head = head;
		this.groupIds = groupIds;
	}


	public Integer getAirId() {
		return airId;
	}


	public void setAirId(Integer airId) {
		this.airId = airId;
	}


	public Integer getUserId() {
		return userId;
	}


	public void setUserId(Integer userId) {
		this.userId = userId;
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


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getGroupIds() {
		return groupIds;
	}


	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}


	public void setCoinId(Integer coinId) {
		this.coinId = coinId;
	}

	@Override
	public String toString() {
		return GsonUtil.toJson(this, AirdropMessage.class);
	}
	

}
