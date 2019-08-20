package com.cn.jm.controller.webcast.api;

import java.util.Date;

import com.cn._gen.model.Account;
import com.cn._gen.model.Report;
import com.cn.jm.core.JMMessage;
import com.cn.jm.core.utils.util.ConverUtils;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.web.controller.JMBaseApiController;
import com.cn.jm.interceptor.JMApiAccountInterceptor;
import com.cn.jm.service.JMAccountService;
import com.cn.jm.service.JMReportService;
import com.cn.jm.web.core.parse.annotation.API;
import com.cn.jm.web.core.parse.annotation.ParseOrder;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;

/**
 * 
 *
 * @date 
 * @author 
 * @Description: 举报模块
 *
 */
@ParseOrder(42)
@API
@JMRouterMapping(url = "/api/report")
public class ApiReportController extends JMBaseApiController{
	
	@Inject
	public JMReportService reportService;
	
	@Inject
	public JMAccountService accountSerivce;
	
	
	/**
	 * 
	 * @author cyl
	 * @date 2019年7月4日 下午2:56:24
	 * @Description:举报投诉用户
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter type,int,投诉类型0投诉主播1投诉客服2物流状况3y优惠活动4功能异常5表扬6建议7其他
	 * @paramter description,String,描述
	 * @paramter targetId,int,投诉的用户Id
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class})
	public void report() {
		Account account = getAttr("account");
		Report report = ConverUtils.fullBean(Report.class,getRequest());
		Account targetAccount = accountSerivce.selectById(report.getTargetId());
		report.setCreateTime(new Date());
		report.setTargetAccount(targetAccount.getMobile());
		report.setAccountId(account.getId());
		report.setAccount(account.getMobile());
		boolean flag = reportService.insertReport(report);
		if(flag) {
			JMResult.success(this, JMMessage.SUCCESS);
		}else {
			JMResult.fail(this, JMMessage.FAIL);
		}
	}

}
