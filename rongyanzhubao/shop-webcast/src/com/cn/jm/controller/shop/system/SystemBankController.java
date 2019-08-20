
package com.cn.jm.controller.shop.system;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

import com.cn._gen.model.Bank;
import com.cn.jm._dao.bank.JMBankDao;
import com.cn.jm.core.JMConsts;
import com.cn.jm.core.tool.JMToolString;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.utils.util.JMUploadKit;
import com.cn.jm.core.web.controller.JMBaseSystemController;
import com.cn.jm.core.web.dao.JMBaseDao;
import com.cn.jm.interceptor.SystemLoginInterceptor;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;

/**
 * Generated by 广州小跑robot.
 */
@JMRouterMapping(url = SystemBankController.url)
@Before(value={SystemLoginInterceptor.class})
public class SystemBankController extends JMBaseSystemController {
	
	public static final String path = JMConsts.base_view_url+"/system/shop/bank";
	public static final String url ="/system/bank";
	
	@Inject
	public JMBankDao bankDao;

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
		Page<Bank> page = null;
		
		if (JMToolString.isNotEmpty(startTime) && JMToolString.isNotEmpty(endTime)) {
			page = bankDao.page(pageNumber, pageSize, "", andpm, orpm, likepm, startTime,endTime,"id", JMBaseDao.DESC, true);
		}else {
			page = bankDao.page(pageNumber, pageSize, "", andpm, orpm, likepm, "id", JMBaseDao.DESC, true);
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
		
		setAttr("action", JMConsts.ACTION_ADD);
		setAttr("bank", new Bank());
		render(path+"/add.html");
	}

	public void save(){
		if(isRepeatSubmit()){
			JMResult.fail(this, "不允许重复提交");
			return;
		}
		
		Bank bank = getModel(Bank.class);
		bank.setCreateTime(new Date());
		if (bankDao.save(bank)) {
			JMResult.success(this, "创建成功");
		}else {
			JMResult.fail(this, "创建失败");
		}
	}
	
	public void edit(){
		setToken();
		int id = getParaToInt("id");
		
		setAttr("bank", bankDao.getById(id));
		render(path+"/edit.html");
	}

	public void update(){
		if(isRepeatSubmit()){
			JMResult.fail(this, "不允许重复提交");
			return;
		}
		
		Bank bank = getModel(Bank.class);
		if (bankDao.update(bank)) {
			JMResult.success(this, "修改成功");
		}else {
			JMResult.fail(this, "修改失败");
		}
	}
	
	public void upload() {
		File file = JMUploadKit.uploadAvatar(this, "file");
		HashMap<String, Object> data = new HashMap<>();
		data.put("name", file.getName());
		data.put("file", JMUploadKit.uploadPath+"file/"+file.getName());
		data.put("localFile", file.getAbsolutePath());
		JMResult.success(this, data,"上传成功");
	}

	public void delete(){
		int id = getParaToInt("id");
		
		if (bankDao.deleteById(id)) {
			JMResult.success(this, "删除成功");
		}else {
			JMResult.fail(this, "删除失败");
		}
	}
	
	public void dels(){
		Integer[] ids = getParaValuesToInt("ids");
		if(ids != null){
			boolean result = bankDao.deleteByIds(ids);
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