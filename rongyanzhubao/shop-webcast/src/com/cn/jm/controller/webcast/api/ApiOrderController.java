
package com.cn.jm.controller.webcast.api;

import com.cn._gen.model.Account;
import com.cn._gen.model.Order;
import com.cn.jm._dao.goods.JMGoodsStarDao;
import com.cn.jm._dao.order.JMOrderDao;
import com.cn.jm._dao.refund.JMRefundOrderDao;
import com.cn.jm.core.JMConsts;
import com.cn.jm.core.interceptor.JMVaildInterceptor;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.valid.annotation.JMRuleVaild;
import com.cn.jm.core.valid.annotation.JMRulesVaild;
import com.cn.jm.core.web.controller.JMBaseApiController;
import com.cn.jm.interceptor.JMApiAccountInterceptor;
import com.cn.jm.service.JMOrderService;
import com.cn.jm.util.JMResultUtil;
import com.cn.jm.web.core.parse.annotation.API;
import com.cn.jm.web.core.parse.annotation.ParseOrder;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
/**
 * 
 *
 * @date 2018年12月20日 下午7:30:07
 * @author Administrator
 * @Description: 直播间订单模块
 *
 */
@API
@ParseOrder(3)
@JMRouterMapping(url = ApiOrderController.url)
public class ApiOrderController extends JMBaseApiController {
	
	public static final String url ="/api/room/order";
	@Inject
	public JMOrderDao orderDao ;
	@Inject
	public JMGoodsStarDao goodsStarDao ;
	@Inject
	public JMRefundOrderDao refundOrderDao ;
	@Inject
	JMOrderService orderService;
	
	
	/**
	 * 
	 * @date 2019年7月15日 15:49:10
	 * @Description: 直播端我的订单
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter state,int,状态：-1全部 0待付款 2待发货 7待收货 8待评价,r:t,p:-1,d:-1
	 * @paramter startTime,String,开始时间,r:f
	 * @paramter endTime,String,结束时间,r:f
	 * @paramter minPrice,String,最低金额,r:f
	 * @paramter maxPrice,String,最大金额,r:f
	 * @paramter keyword,String,搜索名称,r:f
	 * @paramter pageSize,int,每页大小,r:f,p:10,d:10
	 * @paramter pageNumber,int,页码,r:f,p:1,d:1
	 * @pDescription code:0失败1成功2未登录,desc:描述,no:订单号,thumbnail:商品图片,goodsName:商品名称
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class})
	public void page(){
		Account account = getAttr("account");
		Integer pageNumber = getParaToInt("pageNumber", 1);
		Integer pageSize = getParaToInt("pageSize",JMConsts.pageSize);
		int state = getParaToInt("state",-1);
		String keyword = getPara("keyword");
		String minPrice = getPara("minPrice");
		String maxPrice = getPara("maxPrice");
		String startTime = getPara("startTime");
		String endTime = getPara("endTime");
		JMResultUtil.success(orderService.pageMyRoomOrder(account, state, keyword, minPrice, maxPrice, startTime, endTime, pageNumber, pageSize)).renderJson(this);
	}

	/**
	 * 
	 * @date 2019年7月15日 15:49:10
	 * @Description: 直播端我的订单个数
	 * @reqMethod post
	 * @paramter startTime,String,开始时间,r:f
	 * @paramter endTime,String,结束时间,r:f
	 * @paramter minPrice,String,最低金额,r:f
	 * @paramter maxPrice,String,最大金额,r:f
	 * @paramter keyword,String,搜索名称,r:f
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @pDescription code:0失败1成功2未登录,参数标识:0未支付1正在支付中2已支付3支付失败4正在退款中5已退款6取消订单7已发货8确认收货待评价9已评价完成10售后关闭
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class})
	public void myOrderNum() {
		Account account = getAttr("account");
		String keyword = getPara("keyword");
		String minPrice = getPara("minPrice");
		String maxPrice = getPara("maxPrice");
		String startTime = getPara("startTime");
		String endTime = getPara("endTime");
		Integer state = getParaToInt("state", -1);
		JMResultUtil.success(orderService.myWebCastOrderNum(account, keyword, minPrice, maxPrice, startTime, endTime, state)).renderJson(this);
	}

	/**
	 * 
	 * @date 2019年7月23日 09:59:25
	 * @Description: 直播端售后列表
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter state,int,状态：-1全部0申请中1已通过等待退款2不通过申请3关闭申请4退货|退款完成5已通过等待上传物流单号退货6已上传物流单号7上传物流单号后不通过8上传物流单号后通过等待退款,r:f,p:-1,d:-1
	 * @paramter type,int,类型：-1全部0仅退款（发货前）1退货退款 2仅退款（发货后未收到货）,r:f,p:-1,d:-1
	 * @paramter pageSize,int,每页大小,r:f,p:10,d:10
	 * @paramter pageNumber,int,页码,r:f,p:1,d:1
	 * @pDescription	
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class})
	public void webcastRefundPage(){
		Account account = getAttr("account");
		int pageSize = getParaToInt("pageSize",10);
		int pageNumber = getParaToInt("pageNumber",1);
		int state = getParaToInt("state",-1);
		int type = getParaToInt("type",-1);
		JMResult.success(this,refundOrderDao.webcastRefundPage(account, pageNumber, pageSize, state, type),"获取成功");
	}
	/**
	 * 
	 * @date 2019年2月18日 上午9:21:32
	 * @author JaysonLee
	 * @Description: 订单详情
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter orderId,int,订单id,r:t,p:51,d:51
	 * @pDescription	
	 *
	 */
	@Before(value={JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"orderId"})
	})
	public void detail(){
		int orderId = getParaToInt("orderId");
		Order order = orderDao.detail(orderId);
		JMResult.success(this,order,"获取成功");
	}
}