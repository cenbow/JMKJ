package com.cn.jm.service;

import com.cn._gen.model.RoleAccount;
import com.cn.jm._dao.role.JMRoleAccountDao;
import com.jfinal.aop.Aop;

public class JMRoleAccountService extends BasicsService<RoleAccount>{

	JMRoleAccountDao dao = Aop.get(JMRoleAccountDao.class);

	public RoleAccount getByAccountId(Integer id) {
		return dao.getByAccountId(id);
	}


}
