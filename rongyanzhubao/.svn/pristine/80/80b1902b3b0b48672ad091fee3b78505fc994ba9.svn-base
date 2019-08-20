package com.cn.jm.service;

import java.util.List;

import com.cn._gen.model.Power;
import com.cn._gen.model.RolePower;
import com.cn.jm._dao.role.JMRolePowerDao;
import com.jfinal.aop.Aop;

public class JMRolePowerService extends BasicsService<RolePower>{

	JMRolePowerDao rolePowerDao = Aop.get(JMRolePowerDao.class);

	public List<Power> powerList(Integer roleId, Integer menuId) {
		return rolePowerDao.powerList(roleId, menuId);
	}

	public List<Power> powerList(int roleId) {
		return rolePowerDao.powerList(roleId);
	}

	public void deleteByRoleId(Integer roleId) {
		rolePowerDao.deleteByRoleId(roleId);
	}

	public void insertPowerIds(Integer [] powerIds, Integer roleId) {
		if(powerIds != null) {
			rolePowerDao.insertPowerIds(powerIds,roleId);
		}
	}
	
}
