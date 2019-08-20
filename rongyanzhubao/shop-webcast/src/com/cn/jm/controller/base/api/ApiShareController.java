
package com.cn.jm.controller.base.api;

import com.cn._gen.model.Account;
import com.cn.jm.core.JMMessage;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.web.controller.JMBaseApiController;
import com.cn.jm.information.BasicsInformation;
import com.cn.jm.interceptor.JMApiAccountInterceptor;
import com.cn.jm.service.JMGoodsResaleService;
import com.cn.jm.web.core.parse.annotation.API;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;

/**
 * 
 *
 * @date 2019年6月15日 上午9:41:12
 * @author Administrator
 * @Description: 分享链接
 *
 */
@API
@JMRouterMapping(url = ApiShareController.url)
public class ApiShareController extends JMBaseApiController {
	
	public static final String url ="/api/share";

	@Inject
	JMGoodsResaleService goodsResaleService;

	/**
	 * @date 2019年6月15日 上午9:26:42
	 * @Description: 获取店铺分享链接
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @reqMethod post
	 * @pDescription	
	 *
	 */
	@Before(value = { JMApiAccountInterceptor.class })
	public void getShopShareUrl() {
		Account account = getAttr("account");
		JMResult.success(this, String.format(BasicsInformation.HTML_SHOP_URL, account.getId()), "获取成功");
	}
	
	/**
	 * 
	 * @date 2019年6月15日 上午9:26:42
	 * @Description: 获取邀请好友链接
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @reqMethod post
	 * @pDescription	
	 *
	 */
	@Before(value = { JMApiAccountInterceptor.class })
	public void getInviteFriendsShareUrl() {
		Account account = getAttr("account");
		JMResult.success(this, String.format(BasicsInformation.HTML_INVITE_FRIENDS, account.getInvitationCode()), "获取成功");
	}

	/**
	 * 
	 * @date 2019年6月15日 上午9:26:42
	 * @Description: 获取分享转售商品
	 * @paramter goodsResaleId,int,转售id,r:t
	 * @reqMethod post
	 * @pDescription	
	 */
	public void getGoodsResaleShareUrl() {
		Integer goodsResaleId = getParaToInt("goodsResaleId");
		goodsResaleService.getGoodsResaleUrl(goodsResaleId).renderJson(this);;
	}
	
	/**
	   * 
	   * @author cyl
	   * @date 2019年7月22日 下午5:49:56
	   * @Description:获取直播间分享链接
	   * @reqMethod post
	   * @paramter roomId,int,直播间id,r:t,p:1,d:1
	   */
	  @API
	  public void selectRoomShareUrl() {
	    Integer roomId = getParaToInt("roomId");
	    JMResult.success(this, String.format(BasicsInformation.HTML_ROOM_URL,roomId), JMMessage.SUCCESS);
	  }
	
}