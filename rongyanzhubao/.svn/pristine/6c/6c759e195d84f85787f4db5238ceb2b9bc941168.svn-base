
package com.cn.jm.controller.base.api;

import com.cn._gen.model.Content;
import com.cn.jm._dao.content.JMContentDao;
import com.cn.jm.core.interceptor.JMVaildInterceptor;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.valid.annotation.JMRuleVaild;
import com.cn.jm.core.valid.annotation.JMRulesVaild;
import com.cn.jm.core.web.controller.JMBaseApiController;
import com.cn.jm.service.JMContentService;
import com.cn.jm.web.core.parse.annotation.API;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
/**
 * 
 *
 * @date 2019年1月18日 下午7:01:14
 * @author Administrator
 * @Description: 系统信息模块
 *
 */

@JMRouterMapping(url = ApiContentController.url)
public class ApiContentController extends JMBaseApiController {
	
	public static final String url ="/api/content";
	@Inject
	public JMContentDao contentDao;
	@Inject
	JMContentService contentService;
//	
//	public void list() {
//		List<Content> contentList = contentDao.list(true);
//		for (Content content : contentList) {
//			content.remove("content");
//		}
//		JMResult.success(this, contentList, "获取成功");
//	}
	
	/**
	 * 
	 * @date 2019年1月18日 下午7:01:31
	 * @author JaysonLee
	 * @Description: 获取系统文本，h5文本，图片，各种协议等信息
	 * @reqMethod post
	 * @paramter id,int,1：服务协议，2：关于我们，3用户协议，4购买帮助，5购买攻略，6交易保障，7帮助中心，11客服电话,r:t,p:2,d:2
	 * @pDescription type:0H5文本 1纯文本
	 *
	 */
	@API(isScran= true)
	@Before(value={JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"id"})
	})
	public void get() {
		Integer id = getParaToInt("id");
		Content content = contentDao.getById(id);
		JMResult.success(this, content, "获取成功");
	}

	/**
	 * 
	 * @Description: 获取支付后的银行卡信息显示
	 * @reqMethod post
	 * @pDescription 
	 *
	 */
	@API(isScran= true)
	public void bankContent() {
		JMResult.success(this, contentService.bankContent(), "获取成功");
	}
	
}