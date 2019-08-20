package com.cn.jm._dao.account;

import java.math.BigDecimal;
import java.util.HashMap;

import com.cn._gen.dao.AccountUserDao;
import com.cn._gen.model.AccountUser;
import com.cn.jm.core.db.JMToolSql;
import com.cn.jm.util.SqlUtil;
import com.jfinal.plugin.activerecord.Db;

/**
 * 
 * 用户信息
 * 
 * @author 李劲
 * @创建时间：2017年7月6日 上午10:04:22
 */
public class JMAccountUserDao extends AccountUserDao {

	public AccountUser getByMobile(String mobile) {
		return AccountUser.dao.findFirst("select a.* from tb_account_user a inner join tb_account b on a.accountId = b.id where a.mobile = ? ",mobile);
	}

	/**
	 * 获取用户详细信息
	 * 
	 * @param userId
	 * @return
	 */
	public AccountUser getByAccountId(int userId) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("accountId", userId);
		return get(param,false);
	}

	/**
	 * 收入
	 */
	public boolean incomeCommission(int userId, BigDecimal balance) {
		return Db.update("update tb_account_user set commission = commission + ?,amount = amount + ? where accountId = ?", balance,balance, userId) ==1;
	}

	public AccountUser getInfoByUserId(Integer accountId) {
		return AccountUser.dao.findFirst("select a.accountId,a.nick,a.head,b.mobile from tb_account_user a inner join tb_account b on a.accountId = b.id where b.id = ? limit 1",accountId);
	}

	public boolean addAmount(Integer examineAccountId, BigDecimal amount) {
		return Db.update("update tb_account_user set amount = amount + ? where accountId = ?", amount,examineAccountId) ==1;
	}

	public Object getHeadNickByAccountId(Integer accountId) {
		return JMToolSql.get(cacheName,new AccountUser(),"select accountId,nick,head from tb_account_expand where accountId=?",accountId);
	}

	public AccountUser getByValue(String key, String value) {
		StringBuilder sql = new StringBuilder("SELECT * FROM tb_account_user WHERE ");
		SqlUtil.addWhere(sql,key,"=",value);
		sql.append(" LIMIT 1");
		return AccountUser.dao.findFirst(sql.toString());
	}
	/**
	 * 如果只更新一条数据返回true,否则返回false
	 * 但是数据会更新
	 * @param mobile
	 * @param id
	 * @return
	 */
	public boolean updateMobileByAccountId(String mobile, Integer id) {
		return Db.update("UPDATE tb_account_user SET mobile = ? WHERE accountId = ?", mobile, id) == 1;
	}
	
}
