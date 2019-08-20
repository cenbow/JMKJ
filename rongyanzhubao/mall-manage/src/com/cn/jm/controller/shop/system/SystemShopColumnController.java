
package com.cn.jm.controller.shop.system;

import java.util.Date;

import com.cn._gen.model.Label;
import com.cn.jm.core.JMConsts;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.web.controller.JMBaseSystemController;
import com.cn.jm.interceptor.SystemLoginInterceptor;
import com.cn.jm.service.JMLabelService;
import com.cn.jm.util.UploadUtil;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;

/**
 * Generated by 广州小跑robot.
 */
@JMRouterMapping(url = SystemShopColumnController.url)
@Before(value={SystemLoginInterceptor.class})
public class SystemShopColumnController extends JMBaseSystemController {
	
	public static final String path = JMConsts.base_view_url+"/system/shop/column";
	public static final String url ="/system/shop/column";

	@Inject
	JMLabelService labelService;

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
		
		Page<Label> page = labelService.page(pageNumber, pageSize, id, keyword, startTime, endTime);;
		
		setAttr("id", id);
		setAttr("keyword", keyword);
		setAttr("startTime", startTime);
		setAttr("endTime", endTime);
		setAttr("page", page);
		
		jump();
		render(path + "/page.html");
	}
	
	public void add(){
		setToken();
		
		setAttr("action", JMConsts.ACTION_ADD);
		setAttr("label", new Label());
		render(path+"/add.html");
	}
	
	public void save(){
		String image = UploadUtil.uploadImg(this, "image", "/label");
		if(isRepeatSubmit()){
			JMResult.fail(this, "不允许重复提交");
			return;
		}
		
		Label label = getModel(Label.class);
		if(image != null)
			label.setImage(image);
		
		label.setCreateTime(new Date());
		if (labelService.save(label)) {
			JMResult.success(this, "创建成功");
		}else {
			JMResult.fail(this, "创建失败");
		}
	}
	
	public void edit(){
		setToken();
		int id = getParaToInt("id");
		
		setAttr("label", labelService.selectById(id));
		render(path+"/edit.html");
	}

	public void update(){
		String image = UploadUtil.uploadImg(this, "image", "/label");
		
		if(isRepeatSubmit()){
			JMResult.fail(this, "不允许重复提交");
			return;
		}
		
		Label label = getModel(Label.class);
		if(image != null)
			label.setImage(image);
		if (labelService.update(label)) {
			JMResult.success(this, "修改成功");
		}else {
			JMResult.fail(this, "修改失败");
		}
	}
	
	public void delete(){
		int id = getParaToInt("id");
		
		if (labelService.deleteById(id)) {
			JMResult.success(this, "删除成功");
		}else {
			JMResult.fail(this, "删除失败");
		}
	}
	
	public void dels(){
		Integer[] ids = getParaValuesToInt("ids");
		if(ids != null){
			boolean result = labelService.deleteByIds(ids);
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