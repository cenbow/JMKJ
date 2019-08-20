package com.cn.jm.controller.base.api;

import java.text.ParseException;

import com.cn._gen.model.Account;
import com.cn._gen.model.BalanceRecord;
import com.cn.jm.core.JMConsts;
import com.cn.jm.core.interceptor.JMVaildInterceptor;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.valid.annotation.JMRuleVaild;
import com.cn.jm.core.valid.annotation.JMRulesVaild;
import com.cn.jm.core.web.controller.JMBaseApiController;
import com.cn.jm.interceptor.JMApiAccountInterceptor;
import com.cn.jm.service.JMAccountUserService;
import com.cn.jm.service.JMBalanceRecordService;
import com.cn.jm.service.JMExtractLogService;
import com.cn.jm.util.JMResultUtil;
import com.cn.jm.util.UploadUtil;
import com.cn.jm.web.core.parse.annotation.API;
import com.cn.jm.web.core.parse.annotation.ParseOrder;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;

/**
 * @date 2019年1月14日 下午2:51:10
 * @author Administrator
 * @Description: 基础模块
 */
@ParseOrder(1)
@API
@JMRouterMapping(url = "/api/account/user")
public class ApiAccountUserController extends JMBaseApiController {

	@Inject 
	JMAccountUserService accountUserService;
	
	@Inject
	JMExtractLogService jmExtractLogService;
	
	@Inject
	JMBalanceRecordService jmBalanceRecordService;
	/**
	 * @date 2019年5月6日 15:16:04
	 * @Description: 获取用户昵称和头像(获取个人信息)
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @pDescription code:0出错1成功2请登录,mobile:店铺手机号
	 */
	@Before(value = { JMApiAccountInterceptor.class })
	public void myInformation() {
		Account account = getAttr("account");
		JMResultUtil.success(accountUserService.myInformation(account.getId())).renderJson(this);
	}

	/**
	 * @date 2019年5月6日 15:16:04
	 * @Description: 更新店铺手机号
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter mobile,String,更换手机号,r:t
	 * @pDescription code:0出错1成功2请登录,mobile:店铺手机号
	 */
	@Before(value = { JMApiAccountInterceptor.class })
	public void updateShopMobile() {
		String mobile = getPara("mobile");
		Account account = getAttr("account");
		accountUserService.updateShopMobile(account.getId(), mobile).renderJson(this);
	}
	
	/**
	 * @date 2019年5月6日 15:16:04
	 * @Description: 根据accountId获取用户昵称和头像
	 * @reqMethod post
	 * @paramter accountId,String,用户id,r:t
	 * @pDescription code:0出错1成功2请登录
	 */
	@Before(value = { JMVaildInterceptor.class })
	@JMRulesVaild({ @JMRuleVaild(fields = { "accountId" }) })
	public void informationByAccountId() {
		Integer accountId = getParaToInt("accountId");
		JMResultUtil.success(accountUserService.selectByAccountId(accountId)).renderJson(this);
	}
	
	/**
	 * @date 2019年7月3日 17:35:14
	 * @Description: 更新用户昵称和头像
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter nick,String,昵称,r:f
	 * @paramter head,File,头像,r:f
	 * @paramter mobile,String,联系电话,r:f
	 * @pDescription code:0出错1成功2请登录
	 */
	@Before(value = { JMApiAccountInterceptor.class })
	public void updateInformation() {
		String head = UploadUtil.uploadImg(this, "head", "/account");
		String nick = getPara("nick");
		String mobile = getPara("mobile");
		Account account = getAttr("account");
		if(accountUserService.updateHeadOrNick(account, head, nick, mobile)) {
			JMResultUtil.success(head).renderJson(this);
		}else {
			JMResultUtil.fail().renderJson(this);
		}
	}

	/**
	 * @Description: 提现
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter amount,string,提现金额,r:t
	 * @paramter bankName,String,银行名称,r:t
	 * @paramter bankBranch,String,开户行,r:t
	 * @paramter bankUser,String,持卡人,r:t
	 * @paramter bankNumber,String,银行卡号,r:t
	 * @pDescription code:0出错1成功2请登录
	 */
	@Before(value = { JMApiAccountInterceptor.class, JMVaildInterceptor.class})
	@JMRulesVaild({ @JMRuleVaild(fields = { "bankName", "bankBranch", "bankUser", "bankNumber"}) })
	public void balanceExtract() {
		String amount = getPara("amount");
		String bankName = getPara("bankName");
		String bankBranch = getPara("bankBranch");
		String bankUser = getPara("bankUser");
		String bankNumber = getPara("bankNumber");
		Account account = getAttr("account");
		jmExtractLogService.create(account, amount, bankName, bankBranch, bankUser, bankNumber).renderJson(this);
	}
	
	/**
	 * @throws ParseException 
	 * @Description: 账单明细
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter pageNumber,Integer,页码,r:f,d:1
	 * @paramter pageSize,Integer,页面大小,r:f,d:20
	 * @paramter type,Integer,-1全部0支出1收入,r:t,d:-1
	 * @paramter startTime,String,开始时间,r:f
	 * @paramter endTime,String,结束时间,r:f
	 * @pDescription code:0出错1成功2请登录,message:说明,createTime:时间,amount:金额,type:0支出1收入,msgType:1邀请返利2提现3提现失败,balance:变更后余额,state:如果是提现的提现状态(0未审核1通过2不通过)
	 */
	@Before(value = { JMApiAccountInterceptor.class })
	public void pageExtractLog() throws ParseException {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", JMConsts.pageSize);
		Integer type = getParaToInt("type", -1);
		String startTime = getPara("startTime");
		String endTime = getPara("endTime");
		Account account = getAttr("account");
		Page<BalanceRecord> page = jmBalanceRecordService.page(pageNumber, pageSize, account.getId(),
				type, null, startTime, endTime, false);
		JMResult.success(this, page, "获取成功");
	}

	/**
	 * @Description: 提现金额页面
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @pDescription code:0出错1成功2请登录,withdrawable:可提现,unbooked:待入账
	 */
	@Before(value = { JMApiAccountInterceptor.class })
	public void cashPrice() {
		Account account = getAttr("account");
		JMResultUtil.success(accountUserService.cashPrice(account.getId())).renderJson(this);
	}
	
}
