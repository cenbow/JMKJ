package com.cn.jm.controller.base.api;

import com.cn._gen.model.Account;
import com.cn.jm._dao.account.AccountEnum;
import com.cn.jm.core.JMMessage;
import com.cn.jm.core.interceptor.JMVaildInterceptor;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.valid.annotation.JMRuleVaild;
import com.cn.jm.core.valid.annotation.JMRulesVaild;
import com.cn.jm.core.web.controller.JMBaseApiController;
import com.cn.jm.information.PromptInformationEnum;
import com.cn.jm.interceptor.JMApiAccountInterceptor;
import com.cn.jm.service.JMAccountService;
import com.cn.jm.service.JMAccountUserService;
import com.cn.jm.util.JMResultUtil;
import com.cn.jm.web.core.parse.annotation.API;
import com.cn.jm.web.core.parse.annotation.ParseOrder;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.weixin.sdk.kit.IpKit;

/**
 * 
 *
 * @date 2019年1月14日 下午2:51:10
 * @author Administrator
 * @Description: 基础模块
 *
 */
@ParseOrder(1)
@API
@JMRouterMapping(url = "/api/account")
public class ApiAccountController extends JMBaseApiController {

	@Inject
	JMAccountService accountService;
	@Inject 
	JMAccountUserService service;
	
	/**
	 * 
	 * @date 2019年5月6日 15:16:04
	 * @Description: 登录
	 * @reqMethod post
	 * @paramter loginAccount,String,登录账号(loginType为Mail和Mobile时必传),r:f,p:15767676767,d:d:null
	 * @paramter password,String,密码(loginType为Mail和Mobile时必传),r:f,p:123456,d:d:null
	 * @paramter loginType,String,登录类型:Mail：邮箱密码登录，WeChat：微信登录，FaceBook：faceBook登录 ，QQ：qq登录 ，Mobile：手机号和密码登录 ，MobileCode：手机号验证码登录 ，Sina：新浪微博登录,r:t,p:Phone,d:d:null
	 * @paramter ids,String,第三方信息的识别id(loginType不为Mail和Mobile时必传),r:f,p:123456,d:d:null
	 * @paramter code,String,验证码码登录时必传,r:f,p:1234,d:d:null
	 * @paramter registerType,int,注册类型0用户端1直播端2商家端,r:t
	 * @pDescription code:0出错1成功2请登录3需绑定手机号
	 */
	@API(isScran = true)
	@Before(value = { JMVaildInterceptor.class })
	@JMRulesVaild({ @JMRuleVaild(fields = { "loginType", "registerType" }) })
	@ParseOrder(2)
	public void login() {
		String loginAccount = getPara("loginAccount");
		String password = getPara("password");
		String loginType = getPara("loginType");
		String ids = getPara("ids");
		String code = getPara("code");
		String loginIp = IpKit.getRealIp(getRequest());
		Integer registerType = getParaToInt("registerType", 0);
		String userAgent = getRequest().getHeader("User-Agent");
		accountService.login(loginAccount, password, loginIp, userAgent, ids, code, loginType, registerType).renderJson(this);
	}

//	/**
//	 * 
//	 * @date 2019年6月28日 10:20:33
//	 * @Description: 根据邮箱注册
//	 * @reqMethod post
//	 * @paramter mail,String,邮箱,r:t,p:824715645@qq.com,d:d:null
//	 * @paramter password,String,密码,r:t,p:123456,d:d:null
//	 * @paramter code,String,短信验证码,r:t,p:1234,d:d:null
//	 * @pDescription code:0出错1成功2请登录
//	 *
//	 */
//	@API(isScran = true)
//	@Before(value = { JMVaildInterceptor.class })
//	@JMRulesVaild({ @JMRuleVaild(fields = { "mail", "password", "code" }) })
//	@ParseOrder(1)
//	public void registerByMail() {
//		String mail = getPara("mail");
//		String password = getPara("password");
//		String code = getPara("code");
//
//		if (accountService.checkMailRegisterCode(mail, code)) {// 校验传入验证码是否正确
//			accountService.saveAccountByMail(mail, password, AccountEnum.TYPE_USER).renderJson(this);
//		} else {
//			JMResultUtil.fail(PromptInformationEnum.CODE_FAIL).renderJson(this);
//		}
//	}

	/**
	 * 
	 * @date 2019年6月28日 10:20:33
	 * @Description: 根据手机号码注册
	 * @reqMethod post
	 * @paramter mobile,String,手机号,r:t,p:15767676767,d:d:null
	 * @paramter password,String,密码,r:t,p:123456,d:d:null
	 * @paramter code,String,短信验证码,r:t,p:1234,d:d:null
	 * @paramter registerType,int,注册类型0用户端1直播端2商家端,r:t
	 * @paramter invitationCode,String,邀请码,r:f
	 * @pDescription code:0出错1成功2请登录
	 *
	 */
	@API(isScran = true)
	@Before(value = { JMVaildInterceptor.class })
	@JMRulesVaild({ @JMRuleVaild(fields = { "mobile", "password", "code", "registerType" }) })
	@ParseOrder(1)
	public void registerByMobile() {
		String mobile = getPara("mobile");
		String password = getPara("password");
		String code = getPara("code");
		String invitationCode = getPara("invitationCode");
		Integer registerType = getParaToInt("registerType");
		if (accountService.checkMobileRegisterCode(mobile, code, registerType)) {// 校验传入验证码是否正确
			accountService.saveAccountByMobile(mobile, password, AccountEnum.TYPE_USER, invitationCode, registerType).renderJson(this);
		} else {
			JMResultUtil.fail(PromptInformationEnum.CODE_FAIL).renderJson(this);
		}
	}

//	/**
//	 * 
//	 * @date 2019年5月6日 15:35:39
//	 * @Description: 邮箱获取验证码
//	 * @reqMethod post
//	 * @paramter mail,String,邮箱,r:t,p:824715645@qq.com,d:d:null
//	 * @paramter type,String,发送验证码类型:Register：注册，ForgetPwd：忘记密码，Binding：绑定手机号,r:t,p:ForgetPwd,d:d:null
//	 * @pDescription code:0出错1成功2请登录3需绑定手机号
//	 *
//	 */
//	@API
//	@Before(value = { JMVaildInterceptor.class })
//	@JMRulesVaild({ @JMRuleVaild(fields = { "mail", "type" }), })
//	@ParseOrder(3)
//	public void codeByMail() {
//		String mail = getPara("mail");
//		String type = getPara("type");
//		accountService.sendMailCode(mail, type).renderJson(this);
//	}
	/**
	* 
	* @date 2019年5月6日 15:35:39
	* @Description: 手机获取验证码
	* @reqMethod post
	* @paramter mobile,String,手机号,r:t,p:15767676767,d:d:null
	* @paramter registerType,int,注册类型0用户端1直播端2商家端,r:t
	* @paramter type,String,发送验证码类型:Register：注册，ForgetPwd：忘记密码，Binding：绑定手机号，Login：登录，Replace：更换绑定手机号和更新新手机号,r:t,p:ForgetPwd,d:d:null
	* @pDescription code:0出错1成功2请登录3需绑定手机号
	*
	*/
	@API
	@Before(value = { JMVaildInterceptor.class })
	@JMRulesVaild({ @JMRuleVaild(fields = { "mobile", "type", "registerType" }) })
	@ParseOrder(3)
	public void codeByMobile() {
		String mail = getPara("mobile");
		String type = getPara("type");
		Integer registerType = getParaToInt("registerType", 0);
		accountService.sendMobildeCode(mail, type, registerType).renderJson(this);
	}

	/**
	 * @Description: 绑定手机号
	 * @reqMethod post
	 * @paramter mobile,String,手机号,r:t,p:824715645@qq.com,d:d:null
	 * @paramter ids,String,授权登录时返回的ids,r:t,p:0_oGZfg0jtaIxT4h5It8RAHD8DTxdA,d:d:null
	 * @paramter code,int,验证码,r:t,p:1234,d:1234
	 * @paramter registerType,int,注册类型0用户端1直播端2商家端,r:t
	 * @paramter type,String,绑定第三方类型:faceBookId：faceBook第三方登录，weChatId：微信登录，qqId：微信登录，sinaId：新浪微博登录,r:t,p:login,d:null
	 * @pDescription code:0出错1成功2请登录3需绑定手机号
	 */
	@ParseOrder(5)
	@API(isScran = true)
	@Before(value = { JMVaildInterceptor.class })
	@JMRulesVaild({ @JMRuleVaild(fields = { "mobile", "ids", "code", "type", "registerType" }) })
	public void bdMobileLogin() {
		String ids = getPara("ids");
		String mobile = getPara("mobile");
		String code = getPara("code");
		String bdType = getPara("type");
		String loginIp = IpKit.getRealIp(getRequest());
		String userAgent = getRequest().getHeader("User-Agent");
		Integer registerType = getParaToInt("registerType");
		if (accountService.checkMobileBindingCode(mobile, code, registerType)) {
			accountService.bdMobile(mobile, code, loginIp, userAgent, ids, bdType, registerType).renderJson(this);
		} else {
			JMResultUtil.fail(PromptInformationEnum.CODE_FAIL).renderJson(this);
		}
	}

	/**
	 * 
	 * @date 2019年1月14日 下午2:54:33
	 * @author JaysonLee
	 * @Description: 退出登录
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:d:null
	 * @paramter registerType,int,注册类型0用户端1直播端2商家端,r:t
	 * @pDescription code:0出错1成功2请登录3需绑定手机号
	 *
	 */
	@API(isScran = true)
	@Before(value = { JMVaildInterceptor.class })
	@JMRulesVaild({ @JMRuleVaild(fields = { "sessionId" }) })
	public void logout() {
		String sessionId = getPara("sessionId", null);
		String loginIp = IpKit.getRealIp(getRequest());
		String userAgent = getRequest().getHeader("User-Agent");
		JMResult result = accountService.logout(sessionId, loginIp, userAgent);
		result.renderJson(this);
	}

	/**
	 * 
	 * @date 2019年6月28日 10:13:56
	 * @Description: 根据手机号找回密码
	 * @reqMethod post
	 * @paramter mobile,String,手机号,r:t,p:15767676767,d:d:null
	 * @paramter password,String,新密码,r:t,p:123456,d:d:null
	 * @paramter code,String,短信验证码,r:t,p:5625,d:null
	 * @paramter registerType,int,注册类型0用户端1直播端2商家端,r:t
	 * @pDescription code:0出错1成功2请登录3需绑定手机号
	 *
	 */
	@API(isScran = true)
	@Before(value = { JMVaildInterceptor.class })
	@JMRulesVaild({ @JMRuleVaild(fields = { "mobile", "password", "code", "registerType" }) })
	public void findPasswordByMobile() {
		String mobile = getPara("mobile");
		String password = getPara("password");
		Integer code = getParaToInt("code");
		Integer registerType = getParaToInt("registerType");
		if (accountService.checkMobileForgetCode(mobile, code.toString(), registerType)) {
			accountService.updatePwdByMobile(mobile, password, registerType).renderJson(this);
		} else {
			JMResultUtil.fail(PromptInformationEnum.CODE_FAIL).renderJson(this);
		}

	}

	/**
	 * 
	 * @date 2019年7月2日 09:19:58
	 * @Description: 更换手机号码
	 * @reqMethod post
	 * @paramter mobile,String,手机号,r:t,p:15767676767,d:d:null
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:d:null
	 * @paramter code,String,短信验证码,r:t,p:5625,d:null
	 * @paramter registerType,int,注册类型0用户端1直播端2商家端,r:t
	 * @pDescription code:0出错1成功2请登录
	 *
	 */
	@API(isScran = true)
	@Before(value = {JMApiAccountInterceptor.class, JMVaildInterceptor.class})
	@JMRulesVaild({ @JMRuleVaild(fields = {"mobile", "code", "registerType"}) })
	public void replaceMobile() {
		Account account = getAttr("account");
		String mobile = getPara("mobile");
		String code = getPara("code");
		Integer registerType = getParaToInt("registerType");
		accountService.replaceMobile(account, mobile, code, registerType).renderJson(this);
	}
	
	/**
	 * 
	 * @date 2019年7月2日 09:20:27
	 * @Description: 校验旧手机号和更新新手机号验证码
	 * @reqMethod post
	 * @paramter mobile,String,手机号,r:t,p:15767676767,d:d:null
	 * @paramter registerType,int,注册类型0用户端1直播端2商家端,r:t
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:d:null
	 * @paramter code,String,短信验证码,r:t,p:5625,d:null
	 * @pDescription code:0出错1成功2请登录
	 *
	 */
	@API(isScran = true)
	@Before(value = { JMApiAccountInterceptor.class, JMVaildInterceptor.class })
	@JMRulesVaild({ @JMRuleVaild(fields = { "mobile", "code", "registerType" }) })
	public void checkCode(){
		String mobile = getPara("mobile");
		String code = getPara("code");
		Account account = getAttr("account");
		Integer registerType = getParaToInt("registerType");
		if(accountService.checkMobileReplaceCode(mobile, code, account.getId(), registerType)) {
			JMResultUtil.success().renderJson(this);
		} else {
			JMResultUtil.fail(PromptInformationEnum.CODE_FAIL).renderJson(this);
		}
	}
	
	/**
	 * 
	 * @author cyl
	 * @date 2019年7月3日 下午8:01:21
	 * @Description:更新云通信的userSig
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:d:null
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class})
	public void genUserSig(){
		Account account = getAttr("account");
		int expire = getParaToInt("expire",180 * 24 *60 *60); //过期时间
		accountService.editAccountUserSig(account, expire).renderJson(this);
	}
	
	/**
	 * 
	 * @author cyl
	 * @date 2019年7月3日 下午8:53:14
	 * @Description:根据手机号和类型搜索用户
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:d:null
	 * @paramter mobile,String,手机号
	 * @paramter registerType,int,注册类型0用户端1直播端2商家端,r:t
	 * @paramter type,int,用户类型0系统管理1普通客户2商家3主播4房管
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class, JMVaildInterceptor.class})
	@JMRulesVaild({ @JMRuleVaild(fields = { "mobile", "type", "registerType" }) })
	public void selectAccount(){
		String mobile = getPara("mobile");
		Integer type = getParaToInt("type");
		Integer registerType = getParaToInt("registerType");
		Account account = accountService.selectAccount(mobile, type, registerType);
		JMResult.success(this, account, JMMessage.SUCCESS);
	}
}
