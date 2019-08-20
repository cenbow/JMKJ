package com.cn.jm.controller.base.system;

import java.util.List;

import com.cn._gen.model.Account;
import com.cn._gen.model.Menu;
import com.cn._gen.model.Power;
import com.cn.jm.core.JMConsts;
import com.cn.jm.core.JMMessage;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.cn.jm.core.utils.util.ConverUtils;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.web.controller.JMBaseSystemController;
import com.cn.jm.interceptor.SystemLoginInterceptor;
import com.cn.jm.service.JMMenuService;
import com.cn.jm.service.JMPowerService;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;

/**
 * 
 * @author 李劲
 *
 * 2017年9月24日 下午9:30:42
 */
@JMRouterMapping(url = SystemMenuController.url)
@Before(SystemLoginInterceptor.class)
public class SystemMenuController extends JMBaseSystemController {
	
	public static final String path = JMConsts.base_view_url+"/system/menu";
	public static final String url ="/system/menu";
	
	@Inject
	JMPowerService powerService;
	@Inject
	JMMenuService menuService;
	
	public void index(){
		render(path + "/list.html");
	}
	public void toAdd() {
		Integer parentId = getParaToInt("parentId");
		Menu menu = new Menu();
		menu.setParent(parentId);
		setAttr("menu", menu);
		createToken();
		render(path + "/menuform.html");
	}
	public void selectMenu() {
		Account account = getAttr("account");
		List<Menu> catalogs = menuService.getMenu(account);
		JMResult.success(this, catalogs, JMMessage.SUCCESS);
	}
	public void getMenuList() {
		Account account = getAttr("account");
		List<Menu> catalogs = menuService.getMenuList(account);
		JMResult.success(this, catalogs, JMMessage.SUCCESS);
	}
	public void editMenu() {
		Menu menu = ConverUtils.fullBean(Menu.class, getRequest());
		menuService.editMenu(menu);
		JMResult.success(this, JMMessage.SUCCESS);
	}
	public void delMenu() {
		Menu menu = ConverUtils.fullBean(Menu.class, getRequest());
		menuService.delMenu(menu);
		JMResult.success(this, JMMessage.SUCCESS);
	}
	public void saveMenu() {
		Menu menu = getBean(Menu.class);
		menuService.saveMenu(menu);
		JMResult.success(this, JMMessage.SUCCESS);
	}
	public void toButtonAdd() {
		Integer menuId = getParaToInt("menuId");
		Power power = new Power();
		power.setMenuId(menuId);
		setAttr("power", power);
		createToken();
		render(path + "/powerform.html");
	}
	public void editPower() {
		Power menu = ConverUtils.fullBean(Power.class, getRequest());
		powerService.editPower(menu);
		JMResult.success(this, JMMessage.SUCCESS);
	}
	public void delPower() {
		Power menu = ConverUtils.fullBean(Power.class, getRequest());
		powerService.delPower(menu);
		JMResult.success(this, JMMessage.SUCCESS);
	}
	public void savePower() {
		Power menu = getBean(Power.class);
		powerService.savePower(menu);
		JMResult.success(this, JMMessage.SUCCESS);
	}

}
