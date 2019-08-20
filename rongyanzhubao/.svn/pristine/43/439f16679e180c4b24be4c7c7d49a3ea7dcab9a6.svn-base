
package com.cn.jm.controller.base.api;

import com.cn.jm.core.interceptor.JMVaildInterceptor;
import com.cn.jm.core.valid.annotation.JMRuleVaild;
import com.cn.jm.core.valid.annotation.JMRulesVaild;
import com.cn.jm.core.web.controller.JMBaseApiController;
import com.cn.jm.service.JMImgService;
import com.cn.jm.util.JMResultUtil;
import com.cn.jm.web.core.parse.annotation.API;
import com.cn.jm.web.core.parse.annotation.ParseOrder;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;

/**
 * @date 2019年7月3日 09:14:20
 * @author Administrator
 * @Description: 图片模块
 */
@ParseOrder(1)
@API
@JMRouterMapping(url = "/api/img")
public class ApiImgController extends JMBaseApiController {

	@Inject
	public JMImgService imgService;

	/**
	 * 
	 * @date 2019年6月27日 15:48:18
	 * @Description: 获取图片
	 * @reqMethod post
	 * @paramter type,int,获取类型:4轮播图，,r:t,p:0,d:0
	 * @pDescription code:0出错1成功2请登录3需绑定手机号,id:图片id,name:图片名称,image:图片链接(直接显示,不用前缀),remark:图片内容,state:0默认，1商品，2超链接,link:state为2时为链接
	 */
	@API(isScran= true)
	@Before(value={JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"type"})
	})
	@ParseOrder(1)
	public void listByType() {
		Integer type = getParaToInt("type");
		JMResultUtil.success(imgService.selectByKeyAndValue("id,ids,name,image,remark,state,link", "type =", type)).renderJson(this);;
	}
	
}