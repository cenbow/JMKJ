/**
 * 融云 Server API java 客户端
 * create by kitName
 * create datetime : 2017-03-13 
 * 
 * v2.0.1
 */
package com.cn.jm.util.rongyun;

import java.util.concurrent.ConcurrentHashMap;

import com.cn.jm.util.rongyun.methods.Chatroom;
import com.cn.jm.util.rongyun.methods.Group;
import com.cn.jm.util.rongyun.methods.Message;
import com.cn.jm.util.rongyun.methods.Push;
import com.cn.jm.util.rongyun.methods.SMS;
import com.cn.jm.util.rongyun.methods.User;
import com.cn.jm.util.rongyun.methods.Wordfilter;
import com.jfinal.kit.PropKit;

public class RongCloud {
	
	//public final static String domain = "http://121.14.7.232:8080/qukuailian";
	public final static String domain = "http://10.0.0.250:8080/shop-pd";
	
	private final static String appKey = PropKit.get("ryKey");//"n19jmcy5nsz89";
	private final static String appSecret = PropKit.get("rySec");//"k0r8c2W9Yy";

	private static ConcurrentHashMap<String, RongCloud> rongCloud = new ConcurrentHashMap<String,RongCloud>();
	
	public User user;
	public Message message;
	public Wordfilter wordfilter;
	public Group group;
	public Chatroom chatroom;
	public Push push;
	public SMS sms;

	private RongCloud(String appKey, String appSecret) {
		user = new User(appKey, appSecret);
		message = new Message(appKey, appSecret);
		wordfilter = new Wordfilter(appKey, appSecret);
		group = new Group(appKey, appSecret);
		chatroom = new Chatroom(appKey, appSecret);
		push = new Push(appKey, appSecret);
		sms = new SMS(appKey, appSecret);

	}

	public static RongCloud getInstance(String appKey, String appSecret) {
		if (null == rongCloud.get(appKey)) {
			rongCloud.putIfAbsent(appKey, new RongCloud(appKey, appSecret));
		}
		return rongCloud.get(appKey);
	}
	
	public static RongCloud getInstance() {
		if (null == rongCloud.get(appKey)) {
			rongCloud.putIfAbsent(appKey, new RongCloud(appKey, appSecret));
		}
		return rongCloud.get(appKey);
	}
	 
}