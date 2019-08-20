package com.cn.jm.interceptor;

import com.cn.jm.core.valid.JMBaseValidator;
import com.jfinal.core.Controller;

public class SystemLoginValidator extends JMBaseValidator{
	
	@Override
	protected void validate(Controller controller) {
		setShortCircuit(true);
		
		validateRequiredString("account", "account", "账号不能为空");
		validateRequiredString("password", "password", "密码不能为空");
		validateRequiredString("randomCode", "randomCode", "验证码不能为空");
	}
	
	@Override
	protected void handleError(Controller controller) {
	}

}
