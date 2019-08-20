package com.cn.jm.controller.webcast.system;

import com.cn._gen.model.Report;
import com.cn.jm._dao.report.JMReportDao;
import com.cn.jm.core.JMConsts;
import com.cn.jm.core.JMMessage;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.web.controller.JMBaseSystemController;
import com.cn.jm.interceptor.SystemLoginInterceptor;
import com.cn.jm.service.JMReportService;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
@JMRouterMapping(url = SystemReportController.url)
@Before(value={SystemLoginInterceptor.class})
public class SystemReportController extends JMBaseSystemController{
	public static final String path = JMConsts.base_view_url+"/system/report";
	public static final String url ="/system/report";
	
	@Inject
	public JMReportService reportService;
	
	@Clear(SystemLoginInterceptor.class)
	public void index() {
		render(path + "/list.html");
	}
	
	public void page() {
		Integer pageNumber = getParaToInt("pageNumber",1);
		Integer pageSize = getParaToInt("pageSize",10);
		String keyword = getPara("keyword");
		Integer type = getParaToInt("type");
		Page<Report> page = reportService.selectPage(pageNumber, pageSize, keyword, type);
		JMResult.success(this, page, JMMessage.SUCCESS);
	}
	
	public void toEdit() {
		Integer id = getParaToInt("id");
		Report report = reportService.selectById(id);
		report.put("reportType",JMReportDao.typeMap.get(report.getType()));
		setAttr("report",report);
		render(path + "/form.html");
	}
	
	public void editState() {
		Integer id = getParaToInt("id");
		Report report = reportService.selectById(id);
		boolean flag = reportService.editState(report);
		if(flag) {
			JMResult.success(this,JMMessage.SUCCESS);
		}
	}

}
