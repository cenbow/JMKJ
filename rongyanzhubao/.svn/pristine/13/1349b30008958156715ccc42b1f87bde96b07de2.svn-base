
package com.cn.jm.controller.shop.api;

import java.util.List;

import com.cn._gen.model.Account;
import com.cn._gen.model.Bank;
import com.cn._gen.model.BankAccount;
import com.cn.jm._dao.bank.JMBankAccountDao;
import com.cn.jm._dao.bank.JMBankDao;
import com.cn.jm.core.JMConsts;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.web.controller.JMBaseApiController;
import com.cn.jm.interceptor.JMApiAccountInterceptor;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;

@JMRouterMapping(url = ApiBankController.url)
public class ApiBankController extends JMBaseApiController {
	
	public static final String url ="/api/shop/bank";
	@Inject
	public JMBankDao bankDao;
	@Inject
	public JMBankAccountDao bankAccountDao;
	
	public void list(){
		List<Bank> list = bankDao.list(true);
		
		JMResult.success(this, list,"获取成功");
	}
	
	/**
	 * 用户的银行列表
	 */
	@Before(value={JMApiAccountInterceptor.class})
	public void pageAccount() {
		Account account = getAttr("account");
		
		Integer pageNumber = getParaToInt(0, 1);
		Integer pageSize = getParaToInt("pageSize",JMConsts.pageSize);
		
		Page<BankAccount> page = bankAccountDao.page(pageNumber, pageSize, account.getId());
		JMResult.success(this, page,"获取成功");
	}
	
	/**
	 * 用户新增银行卡
	 */
	@Before(value={JMApiAccountInterceptor.class})
	public void save() {
		Account account = getAttr("account");
		
		String bankNick = getPara("bankNick");
		String bankName = getPara("bankName");
		String bankAddress = getPara("bankAddress");
		String bankAccount = getPara("bankAccount");
		
		JMResult result = bankAccountDao.save(account.getId(), bankNick, bankName, bankAddress, bankAccount);
		result.renderJson(this);
	}
	
	/**
	 * 删除银行信息
	 */
	@Before(value={JMApiAccountInterceptor.class})
	public void delete() {
		Account account = getAttr("account");
		
		Integer bankAccountId = getParaToInt("bankAccountId");
		
		BankAccount bankAccount = bankAccountDao.getById(bankAccountId);
		if (null == bankAccount || bankAccount.getAccountId() != account.getId()) {
			JMResult.fail(this, "非法请求,该信息不存在或该信息不属于您");
			return;
		}
		if (bankAccountDao.deleteById(bankAccountId)) {
			JMResult.success(this, "删除成功");
		}else {
			JMResult.fail(this, "删除失败");
		}
	}
	
	/**
	 * 更改默认银行卡信息
	 */
	@Before(value={JMApiAccountInterceptor.class})
	public void changeChoice() {
		Account account = getAttr("account");
		
		Integer bankAccountId = getParaToInt("bankAccountId");
		
		JMResult result = bankAccountDao.choice(account.getId(), bankAccountId);
		result.renderJson(this);
	}
	
	/**
	 * 获取默认银行卡信息
	 */
	@Before(value={JMApiAccountInterceptor.class})
	public void getChoice() {
		Account account = getAttr("account");
		
		BankAccount bankAccount = bankAccountDao.getChoice(account.getId());
		
		JMResult.success(this, bankAccount,"获取成功");
	}
	
}