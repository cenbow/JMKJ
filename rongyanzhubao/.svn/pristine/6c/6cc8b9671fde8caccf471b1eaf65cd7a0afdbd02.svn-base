
package com.cn.jm.controller.base.api;

import java.util.Date;

import com.cn._gen.model.Account;
import com.cn._gen.model.Suggest;
import com.cn.jm.core.interceptor.JMVaildInterceptor;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.web.controller.JMBaseApiController;
import com.cn.jm.interceptor.JMApiAccountInterceptor;
import com.cn.jm.service.JMSuggestService;
import com.cn.jm.web.core.parse.annotation.API;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;

/**
 * 意见建议/反馈
 * @author 李劲
 *
 */
@JMRouterMapping(url = ApiSuggestController.url)
public class ApiSuggestController extends JMBaseApiController {
	
	public static final String url ="/api/suggest";
	@Inject
	public JMSuggestService suggestService;
	
	/**
	 * @date 2019年3月26日 16:21:42
	 * @Description: 反馈
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter content,String,反馈内容,r:t,p:有点,d:一点点
	 * @paramter contact,String,联系方式,r:t,p:110,d:120
	 *
	 */
	@API(isScran= true)
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	public void save(){
		Account account = getAttr("account");
		String content = getPara("content");
		String contact = getPara("contact");
		if(content.length()>=250) {
			JMResult.fail(this, "提交失败,反馈内容过长");
		}
		if (suggestService.createSuggest(account, content, contact)) {
			JMResult.success(this, "提交成功");
		}else {
			JMResult.fail(this, "提交失败");
		}
	}
	
}