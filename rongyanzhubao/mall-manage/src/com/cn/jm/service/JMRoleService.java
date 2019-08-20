package com.cn.jm.service;

import java.util.HashMap;
import java.util.List;

import com.cn._gen.model.Role;
import com.cn.jm._dao.role.JMRoleDao;
import com.jfinal.aop.Aop;
import com.jfinal.plugin.activerecord.Page;

public class JMRoleService extends BasicsService<Role>{
	
	JMRoleDao roleDao = Aop.get(JMRoleDao.class);
	
	public List<Role> all() {
		return roleDao.all();
	}

	public Role getRoleByAccountId(Integer accountId) {
		return roleDao.selectByAccountId(accountId);
	}

	public Page<Role> page(int pageNumber, int pageSize, String columns, HashMap<String, Object> andpm, HashMap<String, Object> orpm, HashMap<String, Object> likepm, String orderField, String orderDirection, boolean useCache) {
		return roleDao.page(pageNumber, pageSize, columns, andpm, orpm, likepm, orderField, orderDirection, useCache);
	}

	public Page<Role> page(int pageNumber, int pageSize, String columns, HashMap<String, Object> andpm, HashMap<String, Object> orpm, HashMap<String, Object> likepm, String startTime, String endTime, String orderField, String orderDirection, boolean useCache) {
		return roleDao.page(pageNumber, pageSize, columns, andpm, orpm, likepm, startTime,endTime, orderField, orderDirection, useCache);
	}

}
