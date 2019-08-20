package com.cn.jm.controller.base.system;

import java.util.HashMap;

import com.cn.jm.core.JMConsts;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.cn.jm.core.utils.cache.JMCacheKit;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.utils.util.RSAUtils;
import com.cn.jm.core.web.controller.JMBaseSystemController;
import com.cn.jm.interceptor.SystemLoginInterceptor;
import com.cn.jm.interceptor.SystemLoginValidator;
import com.cn.jm.service.JMAccountService;
import com.cn.jm.util.RedisUtil;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.captcha.CaptchaRender;

@JMRouterMapping(url = SystemController.url)
@Before(value={SystemLoginInterceptor.class})
public class SystemController extends JMBaseSystemController{

	public static final String url = "/system";
	public static final String path = JMConsts.base_view_url+"/system";
	
	@Inject
	JMAccountService accountService;
	
	public void index(){
		render(path+"/index.html");
	}
	
	public void console() {
		render(path+"/home/console.html");
	}
	
	/**
	 * 验证码
	 */
	@Clear(SystemLoginInterceptor.class)
	public void randomCode() {
		CaptchaRender img = new CaptchaRender();
		render(img);
	}
	
	/**
	 * 平台管理登录
	 */
	@Clear(SystemLoginInterceptor.class)
	@Before(SystemLoginValidator.class)
	public void doLogin() {
		String account = getPara("account").trim();
		String password = getPara("password").trim();
		String randomCode = getPara("randomCode").trim();
		
		boolean loginSuccess = CaptchaRender.validate(this, randomCode);
        if (!loginSuccess) {
        	JMResult.fail(this, "验证码不正确");
			return ;
        }
        JMResult result = accountService.sysLogin(account, password,this);
        
        result.renderJson(this);
	}

	/**
	 * 登出
	 */
	@Before(SystemLoginInterceptor.class)
	public void loginOut() {
		Object accountId = getAttr("accountId");
		RedisUtil.loginOut(accountId);
		render(path+"/login.html");
	}

	/**
	 * 跳转登录界面
	 */
	@Clear(SystemLoginInterceptor.class)
	public void toLogin() {
		renderCaptcha();
		String error = getPara("error");
		
		setAttr("error", error);
		setAttr("account", getCookie("account"));
		setAttr("password", getCookie("password"));
		setAttr("keepLogin", getCookie("keepLogin"));
		
		try {
			HashMap<String,Object> resultKeys = RSAUtils.genKeys();
			setAttr("publicKey",resultKeys.get("pubKey"));//公钥
			setAttr("publicModulus",resultKeys.get("modulus"));//模
			setSessionAttr("privateKey",resultKeys.get("priKey"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		render(path+"/login.html");
	}
	
	@Clear(SystemLoginInterceptor.class)
	public void view() {
		render("/WEB-INF/views/index.html");
	}
	
	@Clear(SystemLoginInterceptor.class)
	public void clearCahce() {
		JMCacheKit.removeAll();
		JMResult.success(this, "清理成功");
	}
	
	/*@Clear(SystemLoginInterceptor.class)
	public void add(){
		Account account = new Account();
		account.setAccount("jm");
		JMResult result = accountDao.save(account, "123456", -1, getIp());
		result.renderJson(this);
	}*/
}
