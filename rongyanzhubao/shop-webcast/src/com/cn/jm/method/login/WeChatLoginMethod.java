package com.cn.jm.method.login;

import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.service.JMAccountService;
import com.jfinal.aop.Aop;

public class WeChatLoginMethod implements LoginMethod{
	
	JMAccountService accountService = Aop.get(JMAccountService.class);
	
	@Override
	public JMResult login(LoginParam loginParam) {
		return accountService.login(loginParam," weChatId =");
	}

}
