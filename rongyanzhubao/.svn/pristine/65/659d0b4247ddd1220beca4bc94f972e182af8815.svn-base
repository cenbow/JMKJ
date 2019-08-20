
package com.cn.jm.controller.base.api;

import com.cn.jm.core.interceptor.JMVaildInterceptor;
import com.cn.jm.core.valid.annotation.JMRuleVaild;
import com.cn.jm.core.valid.annotation.JMRulesVaild;
import com.cn.jm.core.web.controller.JMBaseApiController;
import com.cn.jm.service.JMLabelService;
import com.cn.jm.util.JMResultUtil;
import com.cn.jm.web.core.parse.annotation.API;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;

/**
 * 
 *
 * @date 2019年7月2日 09:40:13
 * @author Administrator
 * @Description: 栏目模块
 *
 */
@JMRouterMapping(url = ApiLabelController.url)
public class ApiLabelController extends JMBaseApiController {
	
	public static final String url ="/api/label";
	
	@Inject
	JMLabelService labelService;
	
	/**
	 * @date 2019年7月1日 16:04:16
	 * @Description: 获取栏目列表
	 * @reqMethod post
	 * @paramter type,int,栏目类型:4电商栏目5直播栏目,r:t,p:5,d:5
	 * @pDescription image:图标,name:栏目名称,id:栏目id(需要columnId则为此id),type:4电商栏目5直播栏目,url:无视,desc:明细
	 */
	@API
	@Before(value={JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"type"}),
	})
	public void listColumnByType() {
		JMResultUtil.success(this,labelService.selectByType(getParaToInt("type")));
	}
}