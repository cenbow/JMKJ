package com.cn.jm.controller.base.api;

import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.web.controller.JMBaseController;
import com.cn.jm.service.JMConfigService;
import com.cn.jm.web.core.parse.annotation.API;
import com.cn.jm.web.core.parse.annotation.ParseOrder;
import com.cn.jm.web.core.router.JMRouterMapping;
/**
 * 系统配置控制器
 */
import com.jfinal.aop.Inject;


import com.cn._gen.model.Area;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.cn.jm._dao.area.JMAreaDao;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.web.controller.JMBaseApiController;
import com.cn.jm.web.core.parse.annotation.API;
import com.jfinal.aop.Inject;

/**
 * 
 *
 * @date 2019年6月18日 下午5:44:32
 * @author Administrator
 * @Description: 获取系统配置
 *
 */
@ParseOrder(1)
@API
@JMRouterMapping(url = "/api/configure")
public class ApiConfigureController extends JMBaseController {

	@Inject
	JMConfigService jmConfigService;
	
	/**
	 * @date 2019年7月3日 17:35:14
	 * @Description: 根据名称获取配置
	 * @reqMethod post
	 * @paramter name,String,配置信息唯一标识(ExtractFee：提现手续费比例，),r:t,p:name
	 * @pDescription code:0出错1成功2请登录
	 */
	public void getByName() {
		String name = getPara("name", "");
		JMResult.success(this, jmConfigService.getByName(name).getValue(), "获取成功");
	}
}
