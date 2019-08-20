package com.cn.jm.controller.base.system;

import java.util.Date;
import java.util.HashMap;

import com.cn._gen.model.Power;
import com.cn.jm.core.JMConsts;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.cn.jm.core.tool.JMToolString;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.web.controller.JMBaseSystemController;
import com.cn.jm.core.web.dao.JMBaseDao;
import com.cn.jm.interceptor.SystemLoginInterceptor;
import com.cn.jm.service.JMPowerService;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;

/**
 * 
 * @author 李劲
 *
 * 2017年9月25日 上午12:26:59
 */
@JMRouterMapping(url = SystemPowerController.url)
@Before(value={SystemLoginInterceptor.class})
public class SystemPowerController extends JMBaseSystemController {
	public static final String path = JMConsts.base_view_url+"/system/power";
	public static final String url ="/system/power";
	
	@Inject
	JMPowerService powerService;
	
	public void index(){
		page();
	}

	public void page(){
		String keyword = getPara("keyword","");
		Integer pageNumber = getParaToInt(0, 1);
		Integer pageSize = 1;
		
		HashMap<String, Object> likepm = new HashMap<String, Object>();
		if (JMToolString.isNotEmpty(keyword)) {
			likepm.put("name", keyword);
		}
		
		HashMap<String, Object> andpm = new HashMap<String, Object>();
		
		HashMap<String, Object> orpm = new HashMap<String, Object>();
		
		Page<Power> page =  powerService.pageByAndByOrByLIKE(pageNumber, pageSize, andpm, orpm, likepm, JMBaseDao.DESC);
		
		jump();
		setAttr("keyword", keyword);
		setAttr("page", page);
		render(path+"/page.jsp");
	}


	public void add(){
		setToken();
		
		setAttr("action", JMConsts.ACTION_ADD);
		render(path+"/add.jsp");
	}


	public void save(){

		Power power = getModel(Power.class);
		power.setCreateTime(new Date());
		powerService.save(power);
		
		redirect(url+"/page");
	}


	public void edit(){
		setToken();
		Integer id = getParaToInt("id");
		setAttr("power", powerService.selectById(id));
		
		setAttr("redirect", getPara("redirect"));
		setAttr("action", JMConsts.ACTION_EDIT);
		render(path+"/edit.jsp");
	}


	public void update(){
		Power obj = getModel(Power.class);
		powerService.update(obj);
		redirect(tzUrl("redirect"));
	}


	public void delete(){
		Integer powerId = getParaToInt("id");
		
		boolean result = powerService.deleteById(powerId);
		System.err.println("zzzz"+result);
		
		if (result) {
			redirect(tzUrl("redirect"));
		}else {
			JMResult.fail(this, "不允许重复提交");
		}
	}


	public void dels(){
		Integer [] ids = getParaValuesToInt("ids");
		if(ids != null){
			powerService.deleteByIds(ids);
		}
		redirect(tzUrl("redirect"));
	}
	

}
