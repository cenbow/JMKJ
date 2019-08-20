package com.cn.jm.method.code;

import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.information.PromptInformationEnum;
import com.cn.jm.information.RedisInformation;
import com.cn.jm.service.JMAccountService;
import com.cn.jm.util.JMResultUtil;
import com.jfinal.aop.Aop;

public class BindingMailCodeMethod implements CodeMethod {

	JMAccountService accountService = Aop.get(JMAccountService.class);

	@Override
	public JMResult check(String mail, int registerType) {
		return accountService.isMailExistence(mail, registerType)
				? JMResultUtil.fail(PromptInformationEnum.USER_EXISTS)
				: JMResultUtil.success(RedisInformation.MAIL_BINDING_CODE, PromptInformationEnum.SUCCESS);
	}

}
