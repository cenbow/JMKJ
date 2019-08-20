package com.cn.jm.util.tio.config;

import com.cn.jm.util.tio.JMWsMsgHandler;
import com.jfinal.plugin.IPlugin;

public class TioPlugin implements IPlugin{
	
	private JMWebsocketStarter appStarter = null;
	
	@Override
	public boolean start() {
		try {
			appStarter = new JMWebsocketStarter(JMServerConfig.SERVER_PORT, JMWsMsgHandler.me);
			appStarter.wsServerStarter.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean stop() {
		return false;
	}

}
