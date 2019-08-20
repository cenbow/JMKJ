package com.cn.jm.service;

import com.cn._gen.model.Report;
import com.cn.jm._dao.report.JMReportDao;
import com.jfinal.aop.Aop;
import com.jfinal.plugin.activerecord.Page;

public class JMReportService extends BasicsService<Report>{
	
	JMReportDao reportDao = Aop.get(JMReportDao.class);
	
	public boolean insertReport(Report report) {
		return this.save(report);
	}
	
	public Page<Report> selectPage(Integer pageNumber,Integer pageSize,String keyword, Integer type){
		Page<Report> page = reportDao.selectSystemPage(pageNumber, pageSize, keyword, type);
		return page;
	}
	
	public boolean editState(Report report) {
		report.setState(1);//设置为已处理
		return report.update();
	}

}
