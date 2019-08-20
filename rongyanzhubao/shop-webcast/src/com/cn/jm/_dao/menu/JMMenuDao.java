package com.cn.jm._dao.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cn._gen.dao.MenuDao;
import com.cn._gen.model.Account;
import com.cn._gen.model.Menu;
import com.cn.jm._dao.account.AccountEnum;
import com.cn.jm._dao.account.JMAccountDao;
import com.cn.jm.core.web.dao.JMBaseDao;
import com.jfinal.aop.Inject;

/**
 * 系统菜单
 */
public class JMMenuDao extends MenuDao {
	
	public static final JMMenuDao jmd = new JMMenuDao();
	public static List<Menu> leftMenuList = new ArrayList<Menu>();
	
	public List<Menu> list(HashMap<String, Object> param) {
		return jmd.list("", param, "sort", JMBaseDao.DESC,true);
	}
	
	@Inject
	JMAccountDao accountDao;
	
	/**
	 * 获取第一层菜单
	 * @return
	 */
	public List<Menu> one(){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("series", 0);
		List<Menu> MenuList = list(param);
		return MenuList;
	} 
	
	/**
	 * 获取某栏目第二层栏目
	 * @return
	 */
	public List<Menu> second(int MenuId){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("parent", MenuId);
		param.put("series", 1);
		List<Menu> MenuList = list(param);
		return MenuList;
	} 
	
	/**
	 * 获取下一层的菜单
	 * @param parent
	 * @return
	 */
	public List<Menu> submenuList(long MenuId,int series,Integer roleId){
		HashMap<String, Object> param = new HashMap<String, Object>();
		String appendSql = "";
		if(MenuId != -1){
			appendSql = " and parent = "+MenuId;
			param.put("parent", MenuId);
		}
		param.put("series", series+1);
		List<Menu> list = null ;
		if(roleId != null){
			list = Menu.dao.find("select * from system_menu where id in (select menuId from system_role_menu where roleId = ? and series = ?) "+appendSql,roleId,series+1);
		}else {
			list = list(param);
		}
		return list;
	}
	
	
	private void nextByDel(List<Menu> list,long parent){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("parent", parent);
		List<Menu> mList = list(param);
		if(mList != null && mList.size() > 0){
			for (int i = 0; i < mList.size(); i++) {
				Menu menu = mList.get(i);
				list.add(menu);
				nextByDel(list, menu.getId());
			}
			
		}
	}
	
	@Override
	public boolean idelete(int id, boolean result) {
		if (result) {
			List<Menu> list = new ArrayList<Menu>();
			nextByDel(list, id);
			if (!list.isEmpty()) {
				result = delete(list);
			}
		}
		result = super.idelete(id, result);
		return result;
	}

	public List<Menu> selectPrivileMenu(Account account) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT m.* FROM system_menu m ");
		//如果用户是超级管理员则加入全部
		if(AccountEnum.TYPE_SYSTEM.equals(account.getType())) {
			sql.append(" INNER JOIN system_role_menu rm ON rm.menuId = m.id")
			   .append(" INNER JOIN system_role r ON r.id = rm.roleId")
			   .append(" INNER JOIN system_role_account ra ON ra.roleId = r.id ")
			   .append(" WHERE ra.accountId = ").append(account.getId());
		}
		sql.append(" ORDER BY m.sort,m.id");
		return list(sql.toString(), true);
	}

	/**
	 * 搜索出全部菜单,如果菜单有对应的权限则加入标识 choice
	 * @param roleId
	 * @return 
	 */
	public List<Menu> listRoleMenu(Integer roleId) {
		return Menu.dao.find("SELECT m.id,m.name,m.parent,m.url,rm.id choice FROM system_menu m LEFT JOIN system_role_menu rm ON rm.menuId = m.id AND rm.roleId = " + roleId + " GROUP BY m.id ORDER BY m.`sort`,m.`id`");
	}
	
}
