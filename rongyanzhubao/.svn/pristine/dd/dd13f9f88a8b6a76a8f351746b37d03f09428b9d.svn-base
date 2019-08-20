package com.cn.jm._dao.account;


import java.util.HashMap;
import java.util.List;

import com.cn._gen.dao.AccountSessionDao;
import com.cn._gen.model.AccountSession;
import com.cn.jm.core.db.JMToolSql;
import com.cn.jm.core.web.dao.JMBaseDao;

/**
 * 
 * @author 李劲
 *
 * 2017年9月17日 下午5:35:22
 */
public class JMAccountSessionDao extends AccountSessionDao{

	
	/**
	 * 获取登录会话
	 * @param sessionId
	 * @return
	 */
	public AccountSession getBySessionId(String sessionId) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("sessionId", sessionId.toString().trim());
		return get(param,true);
	}
	
	/**
	 * 获取登陆会话
	 * @param accountId
	 * @return
	 */
	public AccountSession getByAccountId(long accountId) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("accountId", accountId);
		return get(param,false);
	}
	
	public List<AccountSession> listByAccountId(int accountId) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("accountId", accountId);
		return list(true);
	}

	public List<AccountSession> list(int userId) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userId);
		return list(param,"id",JMBaseDao.DESC);
	}

	public List<AccountSession> list(HashMap<String, Object> param,String orderField,String orderDirection) {
		return dao.find(String.format("select * from tb_user_session where 1=1 %s order by %s %s", JMToolSql.whereAND(param), orderField, orderDirection));
	}
}



