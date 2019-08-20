package com.cn.jm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.cn._gen.model.Account;
import com.cn._gen.model.Menu;
import com.cn.jm._dao.menu.JMMenuDao;
import com.cn.jm._dao.power.JMPowerDao;
import com.jfinal.aop.Aop;

public class JMMenuService extends BasicsService<Menu>{
	
	JMMenuDao menuDao = Aop.get(JMMenuDao.class);
	JMPowerDao powerDao = Aop.get(JMPowerDao.class);
	
	public List<Menu> submenuList(int MenuId, int series, Integer roleId) {
		return menuDao.submenuList(MenuId, series, roleId);
	}

	public List<Menu> getMenu(Account account) {
		// 获取权限下的所有目录
		List<Menu> menus = menuDao.selectPrivileMenu(account);
		List<Menu> catalogs = this.findTree(menus, true);
		return catalogs;
	}

	public List<Menu> getMenuList(Account account) {
		// 获取权限下的所有目录
		List<Menu> menus = menuDao.selectPrivileMenu(account);
		//将按钮设置进菜单中
		for (Menu menu : menus) {
			if(menu.getUrl() != null) {
				menu.put("button",powerDao.selectByMenuId(menu.getId()));
			}
		}
		return menus;
	}

	public List<Menu> findTree(List<Menu> menus, boolean removeEmpty) {
		List<Menu> rootMenus = new ArrayList<>();
		List<Menu> childrens = new ArrayList<>();
		for (Menu menu : menus) {
			if (menu.getParent().equals(-1))
				rootMenus.add(menu);
			else
				childrens.add(menu);
		}
		for (Menu rootMenu : rootMenus) {
			rootMenu.put("children",
					this.getChildren(rootMenu.getId(), childrens));
		}
//		if (removeEmpty)
//			removeEmpty(rootMenus);
		return rootMenus;
	}

//	private void removeEmpty(List<Menu> rootMenus) {
//		Iterator<Menu> it = rootMenus.iterator();
//		for (; it.hasNext();) {
//			Menu menu = it.next();
//			if (CollectionsUtils.isEmpty(menu.get("children"))) {
//				it.remove();
//			}
//		}
//	}

	private List<Menu> getChildren(Integer parentId, List<Menu> catalogs) {
		if (parentId == null) {
			parentId = -1;
		}
		List<Menu> list = new LinkedList<>();
		for (;;) {
			Iterator<Menu> it = catalogs.iterator();
			Menu node = null;
			while (it.hasNext()) {
				Menu menu = it.next();
				if (parentId.equals(menu.getParent())) {
					it.remove();
					node = menu;
					break;
				}
			}
			if (node == null) {
				break;
			}
			List<Menu> child = this.getChildren(node.getId(), catalogs);
			node.put("children", child);
			list.add(node);
		}
		return list;
	}

	public void editMenu(Menu menu) {
		update(menu);
	}

	public void delMenu(Menu menu) {
		delete(menu);
	}

	public void saveMenu(Menu menu) {
		menu.setCreateTime(new Date());
		menu.save();
	}

	public List<Menu> listRoleMenuAndPower(Account account, Integer roleId) {
		List<Menu> menuList = menuDao.listRoleMenu(roleId);
		for (Menu menu : menuList) {
			if(menu.getUrl() != null) {
				menu.put("button",powerDao.listRolePowerByMenuId(menu.getId(),roleId));
			}
		}
		return menuList;
	}
}
