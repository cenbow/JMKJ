package com.cn.jm.util.rongyun.messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cn.jm.util.rongyun.util.GsonUtil;

/**
 * 自定义群聊邀请确认消息
 */
public class JoinGroupTipMessage extends BaseMessage {
	
	private Integer groupId;//群id
	private Integer userId;//邀请人
	private Integer number;//邀请人数
	private Integer state = 0;//0未确认1已确认
	private String no ;
	private String reason = "";//邀请原因
	private String head = "";//邀请人头像
	private String nick = "";//邀请人昵称
	private String adminIds = "";//管理员和群主id
	private Integer joinType  = 0;//0普通邀请1热门邀请
	private List<HashMap<String,Object>> users = new ArrayList<>();//被邀请人数组
	private transient static final String TYPE = "XP:JoinGroupTipMsg";//消息类型

	public JoinGroupTipMessage(Integer groupId, String reason,Integer userId,
			Integer number,String nick ,String head,List<HashMap<String,Object>> users,Integer state,
			String no,String adminIds,Integer joinType ) {
		this.groupId = groupId;
		this.reason = reason;
		this.userId = userId;
		this.number = number;
		this.head = head;
		this.nick = nick;
		this.users = users;
		this.state = state;
		this.no = no;
		this.adminIds = adminIds;
		this.joinType = joinType;
	}

	

	public Integer getJoinType() {
		return joinType;
	}



	public void setJoinType(Integer joinType) {
		this.joinType = joinType;
	}



	public String getAdminIds() {
		return adminIds;
	}



	public void setAdminIds(String adminIds) {
		this.adminIds = adminIds;
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



	public Integer getNumber() {
		return number;
	}



	public String getReason() {
		return reason;
	}



	public void setReason(String reason) {
		this.reason = reason;
	}



	public List<HashMap<String, Object>> getUsers() {
		return users;
	}



	public void setUsers(List<HashMap<String, Object>> users) {
		this.users = users;
	}



	public void setNumber(Integer number) {
		this.number = number;
	}




	@Override
	public String getType() {
		return TYPE;
	}

	

	public Integer getState() {
		return state;
	}



	public void setState(Integer state) {
		this.state = state;
	}



	public String getNo() {
		return no;
	}



	public void setNo(String no) {
		this.no = no;
	}



	@Override
	public String toString() {
		return GsonUtil.toJson(this, JoinGroupTipMessage.class);
	}
	

}
