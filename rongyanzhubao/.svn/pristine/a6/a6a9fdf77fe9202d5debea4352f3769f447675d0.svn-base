
package com.cn.jm.service;

import java.util.HashMap;

import com.cn._gen.model.Account;
import com.cn._gen.model.AccountUser;
import com.cn._gen.model.RoomMerchant;
import com.cn.jm._dao.account.AccountEnum;
import com.cn.jm._dao.account.JMAccountDao;
import com.cn.jm._dao.room.JMRoomMerchantDao;
import com.jfinal.aop.Aop;
import com.cn.jm._dao.account.JMAccountUserDao;
import com.cn.jm._dao.order.JMOrderDao;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.util.JMResultUtil;
import com.jfinal.aop.Inject;
import com.jfinal.kit.StrKit;

public class JMAccountUserService extends BasicsService<AccountUser> {
	
	JMAccountDao accountDao = Aop.get(JMAccountDao.class);
	JMRoomMerchantDao merchantDao = Aop.get(JMRoomMerchantDao.class);

	@Inject
	JMOrderDao orderDao;
	@Inject
	JMAccountUserDao accountUserDao;
	
	public boolean updateHeadOrNick(Account account, String head, String nick, String mobile) {
		AccountUser accountUser = selectOneByKeyAndValue("accountId = ", account.getId());
		setNotBlankAttr(mobile, "mobile", accountUser);
		setNotBlankAttr(nick, "nick", accountUser);
		setNotBlankAttr(head, "head", accountUser);
		return update(accountUser);
	}

	/**
	 * 根据用户id,获取头像和昵称和性别和钱包
	 * @param accountId
	 * @return 
	 */
	public AccountUser selectByAccountId(Integer accountId) {
		return selectOneByKeyAndValue("nick,head,sex,amount,mobile contactNumber", "accountId =", accountId);
	}
	
	public AccountUser myInformation(Integer accountId) {
		AccountUser accountUser = selectByAccountId(accountId);
		Account account = accountDao.getById(accountId);
		//如果是主播,判断有没有绑定商家
		if(account.getType()==AccountEnum.TYPE_ANCHOR.getValue()) {
			RoomMerchant merchant = merchantDao.selectRoomMerchant(account.getId());
			if(merchant!=null) {
				accountUser.put("isBindMerchant",1);
				AccountUser merchantAccount = selectByAccountId(merchant.getMerchantAccountId());
				accountUser.put("merchantNick",merchantAccount.getNick());
			}else {
				accountUser.put("isBindMerchant",0);
			}
		}
		if(StrKit.isBlank(accountUser.getMobile())) {
			accountUser.setMobile(account.getMobile());
		}
		return accountUser;
	}
	
	private void setNotBlankAttr(String value, String attr, AccountUser accountUser) {
		if(StrKit.notBlank(value))
			accountUser.set(attr, value);
	}

	/**
	 * 获取用户待收入金额和可提现金额 withdrawable可提现 unbooked待入账
	 * @param id
	 * @return 
	 */
	public HashMap<String, Object> cashPrice(Integer accountId) {
		HashMap<String,Object> resultMap = new HashMap<>();
		resultMap.put("withdrawable", selectOneByKeyAndValue("amount", "accountId=", accountId).getAmount());
		resultMap.put("unbooked", orderDao.cashPrice(accountId));
		return resultMap;
	}

	public JMResult updateShopMobile(Integer accountId, String mobile) {
		return accountUserDao.updateMobileByAccountId(mobile, accountId) ? JMResultUtil.success() : JMResultUtil.fail();
	}
}