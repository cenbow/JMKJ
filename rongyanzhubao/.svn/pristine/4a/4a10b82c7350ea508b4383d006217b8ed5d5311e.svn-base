package com.cn.jm.interceptor;

import com.cn._gen.model.Account;
import com.cn.jm.core.JMConsts;
import com.cn.jm.information.BasicsInformation;
import com.cn.jm.service.JMAccountService;
import com.cn.jm.service.JMRoleService;
import com.cn.jm.util.RedisUtil;
import com.jfinal.aop.Inject;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public class SystemLoginInterceptor implements Interceptor{

	@Inject
	JMAccountService accountService;
	@Inject
	JMRoleService roleService;
	
	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		String contentType = controller.getRequest().getContentType();

		if (contentType != null && contentType.toLowerCase().startsWith("multipart/")) {
			controller.getFiles();
		}
		Integer accountId = RedisUtil.getUserByToKen(controller);
		if(accountId == null) {
			controller.setAttr("error", "非法请求/用户权限不存在，请重新登陆");
			controller.render(JMConsts.base_view_url + "/system/login.html");
			return;
		}
		Account account = accountService.selectById(accountId);
		if(account == null) {
			controller.setAttr("error", "非法请求/用户权限不存在，请重新登陆");
			controller.render(JMConsts.base_view_url + "/system/login.html");
			return;
		}
		if(account.getState() == BasicsInformation.STATUS_FROZEN) {
			controller.setAttr("error", "用户已经被冻结");
			controller.render(JMConsts.base_view_url + "/system/login.html");
			return;
		}
		
//		//如果不是商家则进入校验,商家的菜单已经在搜索时进行筛选,无需再添加角色
//		if(!AccountEnum.TYPE_SHOP.equals(account.getType())) {
//			Role role = roleService.getRoleByAccountId(accountId);
//			if (role == null) {
//				controller.setAttr("error", "非法请求/用户权限不存在，请重新登陆");
//				controller.render(JMConsts.base_view_url + "/system/login.html");
//				return;
//			}
//			controller.setAttr("roleId",role.getId());
//		}

		controller.setAttr("account",account);
		controller.setAttr("accountId",accountId);
		inv.invoke();
	}

}
