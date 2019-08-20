package com.cn.jm.interceptor;

import org.apache.commons.lang3.StringUtils;

import com.cn._gen.model.Account;
import com.cn.jm.core.interceptor.JMBaseInterceptor;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.information.BasicsInformation;
import com.cn.jm.service.JMAccountService;
import com.jfinal.aop.Inject;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public class JMApiAccountInterceptor extends JMBaseInterceptor {

	@Inject
	static JMAccountService accountService;
	
	@Override
	public boolean before(Invocation inv, Controller controller) {

		String sessionId = controller.getPara("sessionId");
		// 如果sessionId是空的
		if (StringUtils.isEmpty(sessionId)) {
			JMResult.loginError(controller,"登录超时");
			return false;
		}
		
		Account account = accountService.getBySessionId(sessionId);//原来的
		if (account == null) {
			JMResult.loginError(controller,"登录超时");
			return false;
		}
		
		if (account.getState() == BasicsInformation.STATUS_FROZEN) {
			JMResult.fail(controller,"账号已冻结锁定");
			return false;
		}
		
//		String ip = JMToolWeb.getIpAddr(controller.getRequest());
//		String userAgent = controller.getRequest().getHeader("User-Agent");
//		if (!accountService.validSessionId(sessionId, ip, account,userAgent)) {
//			JMResult.fail(controller,"非本用户使用");
//			return false;
//		}
			
		controller.setAttr("account", account);
		return true;
	}

	@Override
	public void after(Invocation inv, Controller controller) {

	}
	

}
