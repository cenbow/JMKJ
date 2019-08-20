package com.cn.jm.controller.base.system;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.cn._gen.model.Account;
import com.cn._gen.model.Role;
import com.cn.jm._dao.account.AccountEnum;
import com.cn.jm.core.JMConsts;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.cn.jm.core.tool.JMToolString;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.utils.util.JMUploadKit;
import com.cn.jm.core.web.controller.JMBaseSystemController;
import com.cn.jm.core.web.dao.JMBaseDao;
import com.cn.jm.interceptor.SystemLoginInterceptor;
import com.cn.jm.service.JMAccountService;
import com.cn.jm.service.JMRoleService;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;

/**
 * 
 * @author 李劲
 *
 * 2017年9月25日 上午12:26:59
 */
@JMRouterMapping(url = SystemAdminController.url)
@Before(value={SystemLoginInterceptor.class})
public class SystemAdminController extends JMBaseSystemController {
	public static final String path = JMConsts.base_view_url+"/system/setting/admin";
	public static final String url ="/system/account/admin";
	
	@Inject 
	JMAccountService accountService;
	@Inject
	JMRoleService roleService;
	
	public void index(){
		page();
	}

	
	public void page(){
		String keyword = getPara("keyword","");
		Integer id = getParaToInt("id");
		
		String startTime = getPara("startTime","");
		String endTime = getPara("endTime","");
		
		Integer pageNumber = getParaToInt(0, 1);
		Integer pageSize = JMConsts.pageSize;
		
		HashMap<String, Object> andpm = new HashMap<String, Object>();
		HashMap<String, Object> likepm = new HashMap<String, Object>();
		HashMap<String, Object> orpm = new HashMap<String, Object>();
		
		if (JMToolString.isNotEmpty(keyword)) {
			likepm.put("account", keyword);
		}
		
		if (id != null) {
			andpm.put("id", id);
		}
		andpm.put("type", AccountEnum.TYPE_SYSTEM.getValue());
		Page<Account> page = null;
		
		if (JMToolString.isNotEmpty(startTime) && JMToolString.isNotEmpty(endTime)) {
			page = accountService.page(pageNumber, pageSize, "", andpm, orpm, likepm, startTime,endTime,"id", JMBaseDao.DESC, true);
		}else {
			page = accountService.page(pageNumber, pageSize, "", andpm, orpm, likepm, "id", JMBaseDao.DESC, true);
		}
		
		setAttr("id", id);
		setAttr("keyword", keyword);
		setAttr("startTime", startTime);
		setAttr("endTime", endTime);
		setAttr("page", page);
		
		setAttr("page", page);
		jump();
		render(path+"/page.html");
	}

	
	public void add(){
		setToken();
		
		List<Role> roleList = roleService.all();
		
		setAttr("role",new Role());
		setAttr("roleList", roleList);
		setAttr("account", new Account());
		setAttr("action", JMConsts.ACTION_ADD);
		render(path+"/add.html");
	}

	public void save(){
		int roleId = getParaToInt("roleId");
		Account account = getModel(Account.class);
		String ac = account.getAccount();
		String password = account.getPassword().trim();
		if (JMToolString.isEmpty(ac)) {
			JMResult.fail(this, "账号不能为空");
			return;
		}
		if (JMToolString.isEmpty(password)) {
			JMResult.fail(this, "密码不能为空");
			return;
		}

		if (accountService.getByAccount(ac) != null) {
			JMResult.fail(this, "账号已经存在");
			return;
		}
		
		account.setCreateTime(new Date());
		account.setType(AccountEnum.TYPE_SYSTEM.getValue());
		
		boolean result = accountService.saveAdmin(account, password, roleId);
		
		if (result) {
			JMResult.success(this, "创建成功");
		}else {
			JMResult.fail(this, "创建失败");
		}
	}

	
	public void edit(){
		int id = getParaToInt("id");
		setToken();
		
		Account account = accountService.selectById(id);
		
		List<Role> roleList = roleService.all();
		setAttr("roleList", roleList);
		Role sr = roleService.getRoleByAccountId(account.getId());
		if (sr == null) {
			setAttr("role",new Role());
		}else {
			setAttr("role",sr);
		}
		setAttr("account", account);
		setAttr("action", JMConsts.ACTION_EDIT);
		render(path+"/edit.html");
	}

	public void update(){
		int roleId = getParaToInt("roleId");
		Account account = getModel(Account.class);
		
		if (accountService.updateSystemAccount(account,roleId)) {
			JMResult.success(this, "修改成功");
		}else {
			JMResult.fail(this, "修改失败");
		}
	}
	
	public void upload() {
		File avatar = JMUploadKit.uploadAvatar(this, "file");
		HashMap<String, Object> data = new HashMap<>();
		data.put("name", avatar.getName());
		data.put("file", JMUploadKit.uploadPath+"avatar/"+avatar.getName());
		data.put("localFile", avatar.getAbsolutePath());
		JMResult.success(this, data,"上传成功");
	}

	public void delete(){
		int id = getParaToInt("id");
		
		if (accountService.deleteById(id)) {
			JMResult.success(this, "删除成功");
		}else {
			JMResult.fail(this, "删除失败");
		}
	}

	public void dels(){
		Integer[] ids = getParaValuesToInt("ids");
		if(ids != null){
			boolean result = accountService.deleteByIds(ids);
			if (result) {
				JMResult.success(this, "删除成功");
			}else {
				JMResult.fail(this, "删除失败");
			}
		}else {
			JMResult.fail(this, "请选择需要删除的数据");
		}
	}

}
