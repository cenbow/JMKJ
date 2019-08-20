package com.cn.jm.service;

import java.util.HashMap;

import com.cn._gen.model.AccountSession;
import com.cn.jm._dao.account.JMAccountSessionDao;
import com.jfinal.aop.Aop;

public class JMAccountSessionService extends BasicsService<AccountSession>{
	
	JMAccountSessionDao accountSessionDao = Aop.get(JMAccountSessionDao.class);
	
	/**
	 * 检查session是否存在,存在刷新登陆时间,不存在创建
	 * @param accountId
	 * @param sessionId
	 * @return
	 */
	public boolean check(int accountId,String sessionId,String sessionSalt) {
		long liveSeconds = 120 * 60;// 用户保持登录为 120 分钟，单位为秒
		long overtime = System.currentTimeMillis() + (liveSeconds * 1000);// expireAt 用于设置 session 的过期时间点，需要转换成毫秒
		
		AccountSession session = accountSessionDao.getByAccountId(accountId);
		if (session == null) {
			session = new AccountSession();
			session.setAccountId(accountId);
			session.setSessionId(sessionId);
			session.setSessionSalt(sessionSalt);
			session.setOvertime(overtime);
			return save(session);
		} else {
			session.setOvertime(overtime);
			session.setSessionId(sessionId);
			return update(session);
		}
	}

	public boolean deleteBySessionId(String sessionId){
		return accountSessionDao.delete(accountSessionDao.getBySessionId(sessionId));
	}

	/**
	 * 获取登录会话
	 * @param sessionId
	 * @return
	 */
	public AccountSession getBySessionId(String sessionId) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("sessionId", sessionId.toString().trim());
		return accountSessionDao.get(param,true);
	}

	/**
	 * 登录会话是否已过期
	 */
	public boolean isExpired(String sessionId) {
		AccountSession session = getBySessionId(sessionId);
		if(session ==  null)
			return false;
		return isExpired(session);
	}

	/**
	 * 登录会话是否未过期
	 */
	public boolean notExpired(String sessionId) {
		return ! isExpired(sessionId);
	}
	
	public boolean isExpired(AccountSession session) {
		boolean isExpired = session.getOvertime() < System.currentTimeMillis();
		if(isExpired)
			session.delete();// 被动式删除过期数据，此外还需要定时线程来主动清除过期数据
		return isExpired;
	}

	
}
