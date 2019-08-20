package com.cn.jm.method.login;

import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.service.JMAccountService;
import com.jfinal.aop.Aop;

public class FaceBookLoginMethod implements LoginMethod{
	
	JMAccountService service = Aop.get(JMAccountService.class);
	@Override
	public JMResult login(LoginParam loginParam) {
		return service.login(loginParam," faceBookId =");
	}

}
