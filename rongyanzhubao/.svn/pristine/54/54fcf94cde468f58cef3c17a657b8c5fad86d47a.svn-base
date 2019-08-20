package com.cn.jm.service;

import java.util.List;

import com.cn._gen.model.Menu;
import com.cn._gen.model.RoleMenu;
import com.cn.jm._dao.role.JMRoleMenuDao;
import com.jfinal.aop.Aop;

public class JMRoleMenuService extends BasicsService<RoleMenu> {
	
	JMRoleMenuDao roleMenuDao = Aop.get(JMRoleMenuDao.class);

	public boolean isLegal(Integer roleId, String action) {
		return roleMenuDao.isLegal(roleId, action);
	}

	public List<Menu> all(Integer roleId) {
		return roleMenuDao.all(roleId);
	}

	public List<Menu> list(int roleId) {
		return roleMenuDao.list(roleId);
	}

	public void deleteByRoleId(Integer roleId) {
		roleMenuDao.deleteByRoleId(roleId);
	}

	public void insertMenuIds(Integer [] menuIds, Integer roleId) {
		if(menuIds != null ) {
			roleMenuDao.insertMenuIds(menuIds,roleId);
		}
	}
}
