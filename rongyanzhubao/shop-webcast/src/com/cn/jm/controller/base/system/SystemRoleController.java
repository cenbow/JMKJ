package com.cn.jm.controller.base.system;

import java.util.Date;
import java.util.HashMap;

import com.cn._gen.model.Role;
import com.cn.jm.core.JMConsts;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.cn.jm.core.tool.JMToolString;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.web.controller.JMBaseSystemController;
import com.cn.jm.core.web.dao.JMBaseDao;
import com.cn.jm.interceptor.SystemLoginInterceptor;
import com.cn.jm.service.JMRoleService;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;

/**
 * 2019年5月23日 10:19:40
 */
@JMRouterMapping(url = SystemRoleController.url)
@Before(value={SystemLoginInterceptor.class})
public class SystemRoleController extends JMBaseSystemController{
	public static final String path = JMConsts.base_view_url+"/system/setting/role";
	public static final String url ="/system/role";

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
		
		if (id == null) {
			if (JMToolString.isNotEmpty(keyword)) {
				likepm.put("name", keyword);
			}
		}else {
			andpm.put("id", id);
		}
		Page<Role> page = null;
		
		if (JMToolString.isNotEmpty(startTime) && JMToolString.isNotEmpty(endTime)) {
			page = roleService.page(pageNumber, pageSize, "", andpm, orpm, likepm, startTime,endTime,"id", JMBaseDao.DESC, false);
		}else {
			page = roleService.page(pageNumber, pageSize, "", andpm, orpm, likepm, "id", JMBaseDao.DESC, false);
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
		
		setAttr("role", new Role());
		setAttr("action", JMConsts.ACTION_ADD);
		render(path+"/add.html");
	}

	public void save(){
		if(isRepeatSubmit()){
			JMResult.fail(this, "不允许重复提交");
			return;
		}
		
		Role role = getModel(Role.class);
		role.setCreateTime(new Date());
		if (roleService.save(role)) {
			JMResult.success(this, "创建成功");
		}else {
			JMResult.fail(this, "创建失败");
		}
	}

	public void edit(){
		setToken();
		int id = getParaToInt("id");
		
		setAttr("role", roleService.selectById(id));
		render(path+"/edit.html");
	}

	public void update(){
		if(isRepeatSubmit()){
			JMResult.fail(this, "不允许重复提交");
			return;
		}
		Role role = getModel(Role.class);
		if (roleService.update(role)) {
			JMResult.success(this, "修改成功");
		}else {
			JMResult.fail(this, "修改失败");
		}
	}

	public void delete() {
		int id = getParaToInt("id");
		
		if (roleService.deleteById(id)) {
			JMResult.success(this, "删除成功");
		}else {
			JMResult.fail(this, "删除失败");
		}
	}
	
	public void dels(){
		Integer[] ids = getParaValuesToInt("ids");
		if(ids != null){
			boolean result = roleService.deleteByIds(ids);
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
