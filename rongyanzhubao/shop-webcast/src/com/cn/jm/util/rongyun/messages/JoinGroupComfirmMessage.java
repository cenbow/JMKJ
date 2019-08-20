package com.cn.jm.util.rongyun.messages;

import com.cn.jm.util.rongyun.util.GsonUtil;

/**
 * 自定义群聊邀请确认消息(被邀请人确认)
 */
public class JoinGroupComfirmMessage extends BaseMessage {
	
	private Integer groupId;//群id
	private Integer userId;//邀请人
	private Integer number;//群聊人数
	private Integer state = 0;//0未确认1已确认
	private String no = "";//进群密码
	private String  groupName = "";//群聊名称
	private String head = "";//群头像
	private String nick = "";//邀请人昵称
	private String inviteHead = "";//邀请人头像
	private Integer inUserId ;//被邀请人id
	private Integer joinType  = 0;//0普通邀请1热门邀请
	private String fansRemark = "";//已在群里的好友
	private transient static final String TYPE = "XP:JoinGroupComfirmMsg";//消息类型

	public JoinGroupComfirmMessage(Integer groupId, String groupName,Integer userId,
			Integer number,String nick ,String head,String fansRemark,Integer state,
			String no,Integer inUserId,Integer joinType,String inviteHead ) {
		this.groupId = groupId;
		this.groupName = groupName;
		this.userId = userId;
		this.number = number;
		this.head = head;
		this.nick = nick;
		this.fansRemark = fansRemark;
		this.state = state;
		this.no = no;
		this.inUserId = inUserId;
		this.joinType = joinType;
		this.inviteHead = inviteHead;
	}

	

	public Integer getJoinType() {
		return joinType;
	}



	public void setJoinType(Integer joinType) {
		this.joinType = joinType;
	}



	



	public String getInviteHead() {
		return inviteHead;
	}



	public void setInviteHead(String inviteHead) {
		this.inviteHead = inviteHead;
	}



	public Integer getInUserId() {
		return inUserId;
	}



	public void setInUserId(Integer inUserId) {
		this.inUserId = inUserId;
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



	public String getGroupName() {
		return groupName;
	}



	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	

	public String getFansRemark() {
		return fansRemark;
	}



	public void setFansRemark(String fansRemark) {
		this.fansRemark = fansRemark;
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
		return GsonUtil.toJson(this, JoinGroupComfirmMessage.class);
	}
	

}
