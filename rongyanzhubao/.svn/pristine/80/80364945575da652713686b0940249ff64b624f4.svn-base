package com.cn.jm.util.tio;


import java.util.HashMap;

import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.server.ServerGroupContext;
import org.tio.utils.lock.SetWithLock;
import org.tio.websocket.common.WsResponse;

import com.cn.jm.core.utils.cache.JMCacheKit;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.util.JMResultUtil;
import com.cn.jm.util.tio.config.JMServerConfig;
import com.cn.jm.util.tio.config.JMWebsocketStarter;

public class JMTio {
	
	public static final String cacheName = "tio_account";
	
	public static final String groupCacheName = "tio_group";
	
	
	
	public static void setAccount(Integer accountId,ChannelContext channelContext) {
		JMCacheKit.put(cacheName, accountId, channelContext);
		Object object = JMCacheKit.get(cacheName, accountId);
		System.err.println(channelContext);
		System.err.println(object);
	}
	
	public static void setAccountGroup(Integer roomId,ChannelContext channelContext) {
		JMCacheKit.put(groupCacheName, roomId, channelContext);
	}
	
	
	
	public static void removeAccount(Integer accountId) {
		JMCacheKit.remove(cacheName, accountId);
	}
	
	public static ChannelContext getAccount(Integer accountId) {
		return JMCacheKit.get(cacheName, accountId);
	} 
	
	
//	public static void send(Integer toAccountId,HashMap<String, Object> data) {
//		ServerGroupContext groupContext = JMWebsocketStarter.getServerGroupContext();
//		if (groupContext != null) {
//			WsResponse wsResponse = WsResponse.fromText(JMResultUtil.success(data).toString(), JMServerConfig.CHARSET);
//			Tio.sendToUser(groupContext, toAccountId.toString(), wsResponse);
//		}
//	}
	
	public static void send(Integer toAccountId,JMResult jmResult) {
		ServerGroupContext groupContext = JMWebsocketStarter.getServerGroupContext();
		if (groupContext != null) {
			WsResponse wsResponse = WsResponse.fromText(jmResult.toString(), JMServerConfig.CHARSET);
			Tio.sendToUser(groupContext, toAccountId.toString(), wsResponse);
		}
//		ChannelContext channelContext=getAccount(toAccountId);
//		System.err.println(channelContext);
//		if(channelContext!=null) {
//			System.err.println(jmResult.toString());
//			WsResponse wsResponse = WsResponse.fromText(jmResult.toString(), JMServerConfig.CHARSET);
//			Tio.send(channelContext, wsResponse);
//		}
	}
	
	public static void sendAllUser(JMResult jmResult) {
		ServerGroupContext groupContext = JMWebsocketStarter.getServerGroupContext();
		if (groupContext != null) {
			WsResponse wsResponse = WsResponse.fromText(jmResult.toString(), JMServerConfig.CHARSET);
			Tio.sendToAll(groupContext, wsResponse);
		}
	}
	
	public static boolean isInGroup(String group,Integer accountId) {
		ServerGroupContext groupContext = JMWebsocketStarter.getServerGroupContext();
		if (groupContext != null) {
			SetWithLock<ChannelContext> channelContextSet = Tio.getChannelContextsByUserid(groupContext, accountId.toString());
			if(channelContextSet==null) {
				return false;
			}
			for(ChannelContext channelContext:channelContextSet.getObj()) {
				boolean inGroup = Tio.isInGroup(group, channelContext);
				if(inGroup) {
					return true;
				}
			}
			return false;
		}
		return false;
	}
	
	
	
	
	
}
