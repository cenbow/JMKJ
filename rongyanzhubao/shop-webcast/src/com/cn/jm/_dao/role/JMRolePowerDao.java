package com.cn.jm._dao.role;

import java.util.Arrays;
import java.util.List;

import com.cn._gen.dao.RolePowerDao;
import com.cn._gen.model.Menu;
import com.cn._gen.model.Power;
import com.cn.jm.core.db.JMToolSql;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

/**
 * 
 * @author 李劲
 *
 * 2017年9月25日 上午12:17:15
 */
public class JMRolePowerDao extends RolePowerDao {
	
	public static final JMRolePowerDao jmd = new JMRolePowerDao();
	
	public List<Power> powerList(long roleId){
		String sql = "SELECT a.*,b.menuPowerId,b.menuId  FROM system_power AS a LEFT JOIN system_role_power AS b ON a.id = b.powerId WHERE b.roleId = %s ORDER BY a.id DESC";
		sql = JMToolSql.format(sql, roleId);
		return JMToolSql.list(cacheName, dao, sql);
	}
	
	public List<Power> powerList(long roleId,long menuId){
		String sql = "SELECT a.*,b.menuPowerId,b.menuId FROM system_power AS a LEFT JOIN system_role_power AS b ON a.id = b.powerId WHERE b.roleId = %s AND b.menuId = %s ORDER BY a.id DESC";
		sql = JMToolSql.format(sql, roleId,menuId);
		return JMToolSql.list(cacheName, dao, sql);
	}
	
	public List<Menu> menuList(Long roleId){
		String sql = "SELECT a.*,b.menuPowerId,b.menuId FROM system_menu AS a LEFT JOIN system_role_power AS b ON a.id = b.menuId WHERE b.roleId = %s ORDER BY a.id DESC";
		sql = JMToolSql.format(sql, roleId);
		return JMToolSql.list(cacheName, dao, sql);
	}
	
	@Override
	public boolean isave(Model<?> model, boolean result) {
		result = super.isave(model, result);
		if (result) {
		}
		return result;
	}

	public void deleteByRoleId(Integer roleId) {
		Db.update("DELETE FROM system_role_power WHERE roleId = " + roleId);
	}

	public void insertPowerIds(Integer [] powerIds, Integer roleId) {
		StringBuilder insertSql = new StringBuilder("INSERT INTO system_role_power(roleId,powerId) VALUES ");
		Arrays.asList(powerIds).forEach(powerId -> insertSql.append("('").append(roleId).append("','").append(powerId).append("'),"));
		Db.update(insertSql.deleteCharAt(insertSql.length()-1).toString());
	}
	
}
