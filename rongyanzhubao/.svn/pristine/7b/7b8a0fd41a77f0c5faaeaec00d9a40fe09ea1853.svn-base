
package com.cn.jm.service;

import com.cn._gen.model.Account;
import com.cn._gen.model.AccountUser;
import com.jfinal.kit.StrKit;

public class JMAccountUserService extends BasicsService<AccountUser> {

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
	
	private void setNotBlankAttr(String value, String attr, AccountUser accountUser) {
		if(StrKit.notBlank(value))
			accountUser.set(attr, value);
	}
}