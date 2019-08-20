package com.cn.jm._dao.role;

import java.util.Arrays;
import java.util.List;

import com.cn._gen.dao.RoleMenuDao;
import com.cn._gen.model.Menu;
import com.cn.jm._dao.menu.JMMenuDao;
import com.cn.jm.core.db.JMToolSql;
import com.jfinal.plugin.activerecord.Db;

/**
 * 
 * @author 李劲
 *
 * 2017年9月25日 上午12:17:04
 */
public class JMRoleMenuDao extends RoleMenuDao {
	public static final JMRoleMenuDao jmd = new JMRoleMenuDao();
	
	public List<Menu> all(int roleId){
		List<Menu> menuList = oneList(roleId);
		if(menuList != null){
			next(roleId,menuList);
		}
		return menuList;
	}
	
//	public boolean isLegal(long roleId,String action){
//		String sql = "SELECT a.* FROM system_menu AS a LEFT JOIN system_role_menu AS b ON a.id = b.menuId WHERE a.url = %s AND b.roleId = %s AND a.series = 0 ORDER BY a.sort DESC";
//		sql = JMToolSql.format(sql,action, roleId);
//		return JMToolSql.get(cacheName, JMMenuDao.dao, sql) != null;
//	}
	public boolean isLegal(long roleId,String action){
		String sql = "SELECT a.* FROM system_menu AS a LEFT JOIN system_role_menu AS b ON a.id = b.menuId WHERE b.roleId = %s AND a.series = 0 ORDER BY a.sort DESC";
		sql = JMToolSql.format(sql, roleId);
		return JMToolSql.get(cacheName, JMMenuDao.dao, sql) != null;
	}
	
	public List<Menu> oneList(int roleId){
		String sql = "SELECT a.* FROM system_menu AS a LEFT JOIN system_role_menu AS b ON a.id = b.menuId WHERE b.roleId = %s AND a.series = 0 ORDER BY a.sort DESC";
		sql = JMToolSql.format(sql, roleId);
		return JMToolSql.list(cacheName, JMMenuDao.dao, sql);
	}
	
	public List<Menu> list(int roleId){
		String sql = "SELECT a.* FROM system_menu AS a LEFT JOIN system_role_menu AS b ON a.id = b.menuId WHERE b.roleId = %s ORDER BY a.sort DESC";
		sql = JMToolSql.format(sql, roleId);
		return JMToolSql.list(cacheName, JMMenuDao.dao, sql);
	}
	
	public void next(int roleId,List<Menu> menuList){
		for(Menu menu : menuList){
			List<Menu> nextList = list(roleId);
			menu.put("nextList", nextList);
			if(nextList != null && !nextList.isEmpty()){
				next(roleId,nextList);
			}
		}
	}
	

	public List<Menu> next(Long roleId,Long menuId){
		String sql = "SELECT a.*  FROM system_menu AS a LEFT JOIN system_role_menu AS b ON a.id = b.menuId WHERE b.roleId = %s AND a.parent = %s ORDER BY a.sort DESC";
		sql = JMToolSql.format(sql, roleId,menuId);
		return JMToolSql.list(cacheName, JMMenuDao.dao, sql);
	}

	public void deleteByRoleId(Integer roleId) {
		Db.update("DELETE FROM system_role_menu WHERE roleId = "+roleId);
	}

	public void insertMenuIds(Integer [] menuIds, Integer roleId) {
		StringBuilder insertSql = new StringBuilder("INSERT INTO system_role_menu(roleId,menuId) VALUES ");
		Arrays.asList(menuIds).forEach(menuId -> insertSql.append("('").append(roleId).append("','").append(menuId).append("'),"));
		Db.update(insertSql.deleteCharAt(insertSql.length()-1).toString());
	}
	
}
