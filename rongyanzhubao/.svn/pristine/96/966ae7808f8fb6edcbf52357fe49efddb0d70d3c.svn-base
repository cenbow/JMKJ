package com.cn.jm.controller.webcast.api;

import java.math.BigDecimal;
import java.util.Date;

import com.cn._gen.model.Account;
import com.cn._gen.model.Forbidden;
import com.cn.jm.core.utils.util.ConverUtils;
import com.cn.jm.core.web.controller.JMBaseApiController;
import com.cn.jm.interceptor.JMApiAccountInterceptor;
import com.cn.jm.web.core.parse.annotation.API;
import com.cn.jm.web.core.parse.annotation.ParseOrder;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Before;
/**
 * 
 *
 * @date 
 * @author 
 * @Description: 禁言
 */
@ParseOrder(48)
@API
@JMRouterMapping(url = "/api/forbidden")
public class ApiForBiddenController extends JMBaseApiController{
	
	
	/**
	 * 
	 * @date
	 * @Description: 禁言用户
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter roomId,int,直播间id,r:t,p:-1,d:-1
	 * @paramter targetId,int,举报的用户ID,r:f
	 * @paramter duration,double,举报的时长(单位/小时),r:f,p:10,d:10
	 * @pDescription code:0失败1成功2未登录,desc:描述
	 *
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class})
	public void forBidden() {
		String duration = getPara("duration");
		Forbidden forbidden = ConverUtils.fullBean(Forbidden.class, getRequest());
		BigDecimal bigDecimal = new BigDecimal(duration);
		//单位为秒
		BigDecimal time = bigDecimal.multiply(new BigDecimal(3600));
		Account account = getAttr("account");
		forbidden.setCreateTime(new Date());
	}

}
