package com.cn.jm.util.tio.config;


import java.io.IOException;

import org.tio.server.ServerGroupContext;
import org.tio.websocket.server.WsServerStarter;

import com.cn.jm.util.tio.JMIpStateListener;
import com.cn.jm.util.tio.JMServerAioListener;
import com.cn.jm.util.tio.JMWsMsgHandler;

public class JMWebsocketStarter {

	public WsServerStarter wsServerStarter;
	private static ServerGroupContext serverGroupContext;

	/**
	 *
	 * @author tanyaowu
	 */
	public JMWebsocketStarter(int port, JMWsMsgHandler wsMsgHandler) throws Exception {
		wsServerStarter = new WsServerStarter(port, wsMsgHandler);

		serverGroupContext = wsServerStarter.getServerGroupContext();
		serverGroupContext.setName(JMServerConfig.PROTOCOL_NAME);
		serverGroupContext.setServerAioListener(JMServerAioListener.me);

		//设置ip监控
		serverGroupContext.setIpStatListener(JMIpStateListener.me);
		//设置ip统计时间段
		serverGroupContext.ipStats.addDurations(JMServerConfig.IpStatDuration.IPSTAT_DURATIONS);
		
		//设置心跳超时时间
		serverGroupContext.setHeartbeatTimeout(JMServerConfig.HEARTBEAT_TIMEOUT);
		
//		if (P.getInt("ws.use.ssl", 1) == 1) {
//			//如果你希望通过wss来访问，就加上下面的代码吧，不过首先你得有SSL证书（证书必须和域名相匹配，否则可能访问不了ssl）
//			String keyStoreFile = "classpath:config/ssl/keystore.jks";
//			String trustStoreFile = "classpath:config/ssl/keystore.jks";
//			String keyStorePwd = "214323428310224";
//			serverGroupContext.useSsl(keyStoreFile, trustStoreFile, keyStorePwd);
//		}
	}

	/**
	 * @param args
	 * @author tanyaowu
	 * @throws IOException
	 */
	public static void start() throws Exception {
		JMWebsocketStarter appStarter = new JMWebsocketStarter(JMServerConfig.SERVER_PORT, JMWsMsgHandler.me);
		appStarter.wsServerStarter.start();
	}

	/**
	 * @return the serverGroupContext
	 */
	public static ServerGroupContext getServerGroupContext() {
		return serverGroupContext;
	}

	public WsServerStarter getWsServerStarter() {
		return wsServerStarter;
	}
	
}
