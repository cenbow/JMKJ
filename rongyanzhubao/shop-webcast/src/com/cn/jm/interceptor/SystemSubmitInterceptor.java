package com.cn.jm.interceptor;

import java.util.HashSet;
import java.util.Set;

import com.cn.jm.information.BasicsInformation;
import com.cn.jm.information.PromptInformationEnum;
import com.cn.jm.util.JMResultUtil;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public class SystemSubmitInterceptor implements Interceptor{

	static Set<String> set = new HashSet<>();
	
	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		String value = controller.getCookie(BasicsInformation.COOKIE_REPEAT_SUBMIT_NAME);
		if(set.add(value)) {
			inv.invoke();
			set.remove(value);
		}else
			controller.renderJson(JMResultUtil.fail(PromptInformationEnum.NOT_REPEAT_SUBMIT));
	}

}
