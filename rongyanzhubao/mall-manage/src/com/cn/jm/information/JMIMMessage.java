package com.cn.jm.information;

import java.util.HashMap;

public class JMIMMessage {
	
	
	public static final int LOGIN = 0;//登陆
	public static final int LOGIN_OUT = 1;//退出登陆
	public static final int CREATE_ROOM = 2;//创建直播间
	public static final int LEAVE_ROOM = 3;//离开直播间
	public static final int INTO_ROOM = 4;//进入直播间
	
	public int type;
	public HashMap<String, Object> data;
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public HashMap<String, Object> getData() {
		return data;
	}
	public void setData(HashMap<String, Object> data) {
		this.data = data;
	}

}
