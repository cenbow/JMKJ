package com.cn.jm.controller.base.system;

import java.util.List;

import com.cn._gen.model.Account;
import com.cn._gen.model.Menu;
import com.cn.jm.core.JMConsts;
import com.cn.jm.core.JMMessage;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.web.controller.JMBaseSystemController;
import com.cn.jm.service.JMMenuService;
import com.cn.jm.service.JMRoleMenuService;
import com.cn.jm.service.JMRolePowerService;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Db;

/**
 * 2019年5月23日 09:28:59
 */
@JMRouterMapping(url = SystemRolePowerController.url)
public class SystemRolePowerController extends JMBaseSystemController {
	public static final String path = JMConsts.base_view_url+"/system/power";
	public static final String url ="/system/role/power";

	@Inject
	JMMenuService menuService;
	@Inject
	JMRoleMenuService roleMenuService;
	@Inject
	JMRolePowerService rolePowerService;
	
	public void index(){
		setAttr("roleId",getPara("roleId"));
		render(path + "/list.html");
	}
	public void getMenuList() {
		Account account = getAttr("account");
		Integer roleId = getParaToInt("roleId");
		List<Menu> catalogs = menuService.listRoleMenuAndPower(account,roleId);
		JMResult.success(this, catalogs, JMMessage.SUCCESS);
	}
	/**
	 * menuIds 和 powerIds传入的格式为1,1,
	 */
	public void update() {
		Integer [] menuIds = getParaValuesToInt("menuIds");
		Integer [] powerIds = getParaValuesToInt("powerIds");
		Integer roleId = getParaToInt("roleId");
		if(Db.tx(()->{
			try {
				//删除对应角色的所有权限
				roleMenuService.deleteByRoleId(roleId);
				rolePowerService.deleteByRoleId(roleId);
				//新增刚刚配置的权限
				roleMenuService.insertMenuIds(menuIds,roleId);
				rolePowerService.insertPowerIds(powerIds,roleId);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		})) {
			JMResult.success(this,JMMessage.SUCCESS);
		}else {
			JMResult.fail(this,JMMessage.FAIL);
		}
	}
}
