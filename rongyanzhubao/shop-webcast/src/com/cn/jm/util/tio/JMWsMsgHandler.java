package com.cn.jm.util.tio;


import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsSessionContext;
import org.tio.websocket.server.handler.IWsMsgHandler;

import com.cn.jm.information.JMIMMessage;
import com.cn.jm.service.JMAccountService;
import com.jfinal.aop.Aop;
import com.jfinal.kit.JsonKit;


/**
 * @author tanyaowu 2017年6月28日 下午5:32:38
 */
public class JMWsMsgHandler implements IWsMsgHandler {
	private static Logger log = LoggerFactory.getLogger(JMWsMsgHandler.class);


	public static final JMWsMsgHandler me = new JMWsMsgHandler();
	
	
	//计算人数
	static int num = 0;

	/**
	 * 握手时走这个方法，业务可以在这里获取cookie，request参数等
	 */
	@Override
	public HttpResponse handshake(HttpRequest request, HttpResponse httpResponse, ChannelContext channelContext)
			throws Exception {
		System.out.println("connent start " + channelContext + ";当前人数:"+ (++num) + "人");
		return httpResponse;
	}

	/**
	 * 握手成功后,信息处理
	 */
	@Override
	public void onAfterHandshaked(HttpRequest request, HttpResponse httpResponse, ChannelContext channelContext)
			throws Exception {
		String clientip = request.getClientIp();
		String sessionId = request.getParam("sessionId");
		System.err.println(sessionId);
		System.err.println(clientip);
		// 如果sessionId是空的
//		if (StringUtils.isEmpty(sessionId)) {
//			String msg = JMResultUtil.loginError().toString();
//			WsResponse wsResponse = WsResponse.fromText(msg, JMServerConfig.CHARSET);
//			Tio.send(channelContext, wsResponse);
//			Tio.close(channelContext, msg);
//			return;
//		}
//		accountService.validSessionId(sessionId, loginIp, userAgent)
//		Integer accountId = accountService.;
//		if (accountId == null) {
//			String msg = JMResultUtil.loginError().toString();
//			WsResponse wsResponse = WsResponse.fromText(msg, JMServerConfig.CHARSET);
//			Tio.send(channelContext, wsResponse);
//			Tio.close(channelContext, msg);
//			return;
//		}
//		Account account = accountDao.getById(accountId);
//		if (account.getState() == JMConstants.IS_DEL) {
//			String msg = JMResultUtil.fail(JMMessage.NOT_ACCOUNT).toString();
//			WsResponse wsResponse = WsResponse.fromText(msg, JMServerConfig.CHARSET);
//			Tio.send(channelContext, wsResponse);
//			Tio.close(channelContext, msg);
//			return;
//		}
	}

	/**
	 * 字节消息（binaryType = arraybuffer）过来后会走这个方法
	 */
	@Override
	public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
		System.out.println("connent start " + channelContext + ";当前人数:"+ (--num) + "人");
		return null;
	}

	/**
	 * 当客户端发close flag时，会走这个方法
	 */
	@Override
	public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
		Tio.remove(channelContext, "receive close flag");
		return null;
	}

	/*
	 * 字符消息（binaryType = blob）过来后会走这个方法
	 */
	@Override
	public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) throws Exception {
//		System.err.println("text--------"+text);
		JMIMMessage message = JsonKit.parse(text, JMIMMessage.class);
		HashMap<String, Object> data = message.getData();
		switch (message.getType()) {
		case JMIMMessage.LOGIN:
			TioMethod.login(data, channelContext);
			break;
		case JMIMMessage.LOGIN_OUT:
			TioMethod.loginOut(data, channelContext);
			break;
//		case JMIMMessage.CREATE_ROOM:
//			TioMethod.bindGroup(channelContext,data.get("roomId").toString());
//			break;
//		case JMIMMessage.LEAVE_ROOM:
//			TioMethod.ubindGroup(channelContext, data.get("roomId").toString());
//			break;
//		case JMIMMessage.INTO_ROOM:
//			TioMethod.bindGroup(channelContext, data.get("roomId").toString());
//			break;
			
			
		}
			
		WsSessionContext wsSessionContext = (WsSessionContext) channelContext.getAttribute();
		HttpRequest httpRequest = wsSessionContext.getHandshakeRequestPacket();//获取websocket握手包
		if (log.isDebugEnabled()) {
			log.debug("握手包:{}", httpRequest);
		}
//		System.err.println("收到ws消息:{}"+text);
//		log.info("收到ws消息:{}", text);
//		String msg = "{name:'" + channelContext.userid + "',message:'" + text + "'}";
//		System.err.println(msg);
//		//返回值是要发送给客户端的内容，一般都是返回null
		return null;
	}

	
	

}
