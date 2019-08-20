package com.cn.jm.util.rongyun.messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cn.jm.util.rongyun.util.GsonUtil;

/**
 * 自定义群聊直播消息
 */
public class JoinGroupLiveMessage extends BaseMessage {
	
	private String head = "";//邀请人头像
	private String nick = "";//邀请人昵称
	private String title = "";//直播标题
	private String playUrl = "";//直播播放地址
	private String shotImg = "";//直播截图
	private Integer state ;//直播状态 0未开播1开播中2已关闭  4禁播
	private Integer liveType ;//直播类型 0导入地址1录制直播
	private Integer examineState ;//0正常 1待审核全网直播 2审核通过全网直播 3 审核不通过全网直播
	private Integer liveId ;//直播Id
	private List<HashMap<String,Object>> groups = new ArrayList<>();//关联群聊数组，首个为主群
	private transient static final String TYPE = "XP:JoinGroupLiveMsg";//消息类型

	public JoinGroupLiveMessage(String nick ,String head,List<HashMap<String,Object>> groups,Integer state,
			String title,String playUrl,String shotImg,Integer liveType,Integer examineState,Integer liveId ) {
		this.head = head;
		this.nick = nick;
		this.groups = groups;
		this.state = state ;
		this.liveType = liveType;
		this.examineState = examineState;
		this.shotImg = shotImg;
		this.playUrl = playUrl;
		this.title = title ;
		this.liveId = liveId ;
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

	

	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getPlayUrl() {
		return playUrl;
	}



	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}



	public String getShotImg() {
		return shotImg;
	}



	public void setShotImg(String shotImg) {
		this.shotImg = shotImg;
	}



	public Integer getLiveType() {
		return liveType;
	}



	public void setLiveType(Integer liveType) {
		this.liveType = liveType;
	}



	public Integer getExamineState() {
		return examineState;
	}



	public void setExamineState(Integer examineState) {
		this.examineState = examineState;
	}



	public List<HashMap<String, Object>> getGroups() {
		return groups;
	}



	public void setGroups(List<HashMap<String, Object>> groups) {
		this.groups = groups;
	}



	public Integer getLiveId() {
		return liveId;
	}



	public void setLiveId(Integer liveId) {
		this.liveId = liveId;
	}



	@Override
	public String toString() {
		return GsonUtil.toJson(this, JoinGroupLiveMessage.class);
	}
	

}
