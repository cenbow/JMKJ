
package com.cn.jm.controller.base.api;

import com.cn._gen.model.Account;
import com.cn._gen.model.Follow;
import com.cn.jm._dao.follow.JMFollowService;
import com.cn.jm.core.JMMessage;
import com.cn.jm.core.utils.util.ConverUtils;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.web.controller.JMBaseApiController;
import com.cn.jm.interceptor.JMApiAccountInterceptor;
import com.cn.jm.web.core.parse.annotation.API;
import com.cn.jm.web.core.parse.annotation.ParseOrder;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;

/**
 * 
 *
 * @Description: 关注模块
 *
 */
@API
@ParseOrder(44)
@JMRouterMapping(url = "/api/follow")
public class ApiFollowController extends JMBaseApiController {

	@Inject
	public JMFollowService followService;

	/**
	 * @author 
	 * @throws Exception 
	 * @date 2019年7月3日 下午4:01:37
	 * @Description: 关注
	 * @reqMethod post
	 * @paramter sessionId,string,sessionId,p:00150f03fd5d47ecb2717a413d6af732
	 * @paramter ids,stirng,关注的物品id,p:1
	 * @paramter type,int,关注的物品的类型 0直播间
	 * @pDescription
	 *
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class})
	public void followRes() {
		Account account = getAttr("account");
		Follow pojo = ConverUtils.fullBean(Follow.class, getRequest());
		pojo.setAccountId(account.getId());
		followService.insertFollow(pojo);
		JMResult.success(this, JMMessage.SUCCESS);
	}

	/**
	 * @author 
	 * @throws Exception 
	 * @date 2019年7月3日 下午4:01:37 
	 * @Description: 取消关注
	 * @reqMethod post
	 * @paramter sessionId,string,sessionId,p:00150f03fd5d47ecb2717a413d6af732
	 * @paramter ids,stirng,关注的物品id,p:1
	 * @paramter type,int,关注的物品的类型 0直播间
	 * @pDescription
	 *
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class})
	public void cancelFollowRes() {
		Account account = getAttr("account");
		Follow pojo = ConverUtils.fullBean(Follow.class, getRequest());
		pojo.setAccountId(account.getId());
		followService.cancelFollowRes(pojo).renderJson(this);
	}


}