
package com.cn.jm.controller.shop.api;

import com.cn._gen.model.Account;
import com.cn.jm._dao.user.JMUserCollectDao;
import com.cn.jm.core.JMConsts;
import com.cn.jm.core.interceptor.JMVaildInterceptor;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.valid.annotation.JMRuleVaild;
import com.cn.jm.core.valid.annotation.JMRulesVaild;
import com.cn.jm.core.web.controller.JMBaseController;
import com.cn.jm.interceptor.JMApiAccountInterceptor;
import com.cn.jm.util.JMResultUtil;
import com.cn.jm.web.core.parse.annotation.API;
import com.cn.jm.web.core.parse.annotation.ParseOrder;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
/**
 * 
 *
 * @date 2018年12月19日 下午7:38:47
 * @author Administrator
 * @Description: 收藏模块
 *
 */
@API
@ParseOrder(4)
@JMRouterMapping(url = ApiUserCollectController.url)
public class ApiUserCollectController extends JMBaseController {
	
	public static final String url ="/api/user/collect";
	@Inject
	public JMUserCollectDao usercollectDao;

	/**
	 * 
	 * @date 2018年12月19日 下午7:43:40
	 * @author JaysonLee
	 * @Description: 添加、删除商品到收藏
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter goodsId,int,商品id,r:t,p:2,d:1
	 * @pDescription code:0失败1成功2未登录,desc:描述	
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"goodsId"})
	})
	public void addOrDel(){
		Account account = getAttr("account");
		int goodsId = getParaToInt("goodsId");
		renderJson(usercollectDao.addOrDel(account.getId(), goodsId));
	}

	/**
	 * 
	 * @date 2019年7月25日 18:05:28
	 * @Description: 删除收藏的全部失效商品
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @pDescription code:0失败1成功2未登录,desc:描述	
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class})
	public void deleteInvalidGoods(){
		Account account = getAttr("account");
		usercollectDao.deleteInvalidGoods(account.getId());
		renderJson(JMResultUtil.success());
	}
	/**
	 * 
	 * @date 2018年12月19日 下午7:47:18
	 * @author JaysonLee
	 * @Description: 从购物车移到收藏夹
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter goodsIds,String,商品id字符串，用英文逗号分割,r:t,p:2,d:1
	 * @pDescription code:0失败1成功2未登录,desc:描述
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"goodsIds"})
	})
	public void addsFromCart(){
		Account account = getAttr("account");
		String goodsIds = getPara("goodsIds");
		renderJson(usercollectDao.adds(account.getId(), goodsIds));
	}
	
	
	/**
	 * 
	 * @date 2018年12月19日 下午7:38:17
	 * @author JaysonLee
	 * @Description: 商品收藏列表
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter keyword,String,商品名称关键词,r:f,p:1,d:1
	 * @paramter pageSize,int,每页大小,r:f,p:10,d:10
	 * @paramter pageNumber,int,页码,r:f,p:1,d:1
	 * @paramter sellState,int,是否失效商品0失效1未失效,r:f
	 * @pDescription code:0失败1成功2未登录,desc:描述
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class})
	public void page(){
		Account account = getAttr("account");
		String keyword = getPara("keyword","");
		Integer pageNumber = getParaToInt(0, 1);
		Integer sellState = getParaToInt("sellState");
		Integer pageSize = getParaToInt("pageSize",JMConsts.pageSize);
		JMResult.success(this,usercollectDao.pageForApi(keyword, account.getId(), sellState, pageSize, pageNumber),"获取成功");
	}
}