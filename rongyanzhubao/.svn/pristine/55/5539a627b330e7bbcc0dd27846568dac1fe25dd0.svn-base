package com.cn.jm.util.tio;

import java.util.HashMap;

import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.Tio;
import org.tio.server.ServerGroupContext;
import org.tio.websocket.common.WsResponse;

import com.cn.jm.core.JMMessage;
import com.cn.jm.information.ZbMessage;
import com.cn.jm.util.JMResultUtil;
import com.cn.jm.util.tio.config.JMServerConfig;
import com.cn.jm.util.tio.config.JMWebsocketStarter;

public class TioMethod {
	
	public static void login(HashMap<String, Object> data, ChannelContext channelContext) throws InterruptedException {
		Integer accountId = Integer.valueOf(data.get("accountId").toString());
		Tio.bindUser(channelContext, accountId + "");
		HashMap<String,Object> param = new HashMap<>();
		param.put("test", "测试");
		String msg = JMResultUtil.success(param).toString();
		WsResponse wsResponse = WsResponse.fromText(msg, JMServerConfig.CHARSET);
		Tio.send(channelContext, wsResponse);
	}

	public static void loginOut(HashMap<String, Object> data, ChannelContext channelContext) {
		Integer accountId = Integer.valueOf(data.get("accountId").toString());
		Tio.unbindUser(channelContext.groupContext, accountId + "");
		String msg = JMResultUtil.successDesc(JMMessage.SUCCESS).toString();
		System.out.println("退出登陆成功");
		WsResponse wsResponse = WsResponse.fromText(msg, JMServerConfig.CHARSET);
		Tio.send(channelContext, wsResponse);
		Tio.close(channelContext, msg);
	}
	
	//绑定组
	public static void bindGroup(ChannelContext channelContext,String roomId) {
		Tio.bindGroup(channelContext, roomId);
		String msg = JMResultUtil.successDesc(ZbMessage.BIND_GROUP_SUCCESS).toString();
		WsResponse wsResponse = WsResponse.fromText(msg, JMServerConfig.CHARSET);
		Tio.send(channelContext, wsResponse);
	}
	
	//离开组
	public static void ubindGroup(ChannelContext channelContext,String roomId) {
		Tio.bindGroup(channelContext, roomId);
		String msg = JMResultUtil.successDesc(ZbMessage.UBIND_GROUP_SUCCESS).toString();
		WsResponse wsResponse = WsResponse.fromText(msg, JMServerConfig.CHARSET);
		Tio.send(channelContext, wsResponse);
	}
	
	
	

}
