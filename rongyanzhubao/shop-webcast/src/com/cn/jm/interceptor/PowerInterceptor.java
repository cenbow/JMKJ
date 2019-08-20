package com.cn.jm.interceptor;

import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.service.JMPowerService;
import com.jfinal.aop.Inject;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

/**
 * 按钮权限控制拦截,需要在按钮请求的方法上配置该权限
 * 该拦截器需要在MySystemLoginInterceptor拦截器后面
 * @author Administrator
 *
 */
public class PowerInterceptor implements Interceptor{

	@Inject
	JMPowerService powerService;
	
	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		Integer roleId = controller.getAttr("roleId");
		if(roleId != null && powerService.needJurisdiction(roleId,controller.getRequest())) {
			JMResult.fail(controller, "用户缺少该权限");
			return;
		}
		inv.invoke();
	}

}
