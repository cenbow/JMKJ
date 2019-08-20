
package com.cn.jm.controller.shop.api;

import java.util.ArrayList;
import java.util.List;

import com.cn._gen.model.Account;
import com.cn._gen.model.Order;
import com.cn.jm._dao.goods.JMGoodsStarDao;
import com.cn.jm._dao.order.JMOrderDao;
import com.cn.jm._dao.order.OrderState;
import com.cn.jm._dao.refund.JMRefundOrderDao;
import com.cn.jm.core.JMConsts;
import com.cn.jm.core.interceptor.JMVaildInterceptor;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.valid.annotation.JMRuleVaild;
import com.cn.jm.core.valid.annotation.JMRulesVaild;
import com.cn.jm.core.web.controller.JMBaseApiController;
import com.cn.jm.information.PromptInformationEnum;
import com.cn.jm.interceptor.JMApiAccountInterceptor;
import com.cn.jm.service.JMOrderService;
import com.cn.jm.util.JMResultUtil;
import com.cn.jm.util.UploadUtil;
import com.cn.jm.web.core.parse.annotation.API;
import com.cn.jm.web.core.parse.annotation.ParseOrder;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
/**
 * 
 *
 * @date 2018年12月20日 下午7:30:07
 * @author Administrator
 * @Description: 订单模块
 *
 */
@API
@ParseOrder(3)
@JMRouterMapping(url = ApiOrderController.url)
public class ApiOrderController extends JMBaseApiController {
	
	public static final String url ="/api/order";
	@Inject
	public JMOrderDao orderDao = Aop.get(JMOrderDao.class);
	@Inject
	public JMGoodsStarDao goodsStarDao ;
	@Inject
	public JMRefundOrderDao refundOrderDao ;
	@Inject
	JMOrderService orderService;
	
	/**
	 * @date 2019年7月3日 14:26:36
	 * @Description: 创建立即购买订单
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter goodsId,int,商品id,r:t,p:10,d:10
	 * @paramter addressId,int,地址id,r:t,p:4,d:4
	 * @paramter remark,String,订单留言,r:f,p:这是留言,d:这是留言
	 * @pDescription code:0失败1成功2未登录,desc:描述,no:订单号
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"goodsId","addressId"})
	})
	public void create(){
		Account account = getAttr("account");
		int goodsId = getParaToInt("goodsId");
		int addressId = getParaToInt("addressId");
		String remark = getPara("remark","");
		if (!orderService.canPay(goodsId, null)) {
			renderJson(JMResultUtil.fail(PromptInformationEnum.GOODS_FORM_TIES));
			return;
		}
		try {
			renderJson(orderDao.create(goodsId, addressId, account, remark, null, OrderState.SHOP_ORDER_TYPE));
		} catch (Exception e) {
			e.printStackTrace();
			JMResult.fail(this,"创建订单失败");
		}
	}

	/**
	 * @date 2019年7月3日 14:26:36
	 * @Description: 创建转售商品立即购买订单
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter goodsResaleId,int,转售id,r:t,p:10,d:10
	 * @paramter addressId,int,地址id,r:t,p:4,d:4
	 * @paramter remark,String,订单留言,r:f,p:这是留言,d:这是留言
	 * @pDescription code:0失败1成功2未登录,desc:描述,no:订单号
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"goodsResaleId","addressId"})
	})
	public void createResaleOrder(){
		Account account = getAttr("account");
		int goodsResaleId = getParaToInt("goodsResaleId");
		int addressId = getParaToInt("addressId");
		String remark = getPara("remark","");
		try {
			renderJson(orderDao.createResaleOrder(goodsResaleId, addressId, account, remark));
		} catch (Exception e) {
			e.printStackTrace();
			JMResult.fail(this,"创建订单失败");
		}
	}
	
	/**
	 * @date 2019年7月3日 14:26:30
	 * @Description: 为订单设置收货地址
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter orderNo,String,订单编号,r:t,p:10,d:10
	 * @paramter addressId,int,地址id,r:t,p:4,d:4
	 * @pDescription code:0失败1成功2未登录,desc:描述,no:订单号
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"orderNo","addressId"})
	})
	public void setOrderAddress(){
		int addressId = getParaToInt("addressId");
		String orderNo = getPara("orderNo");
		try {
			renderJson(orderDao.updateOrderAddress(addressId, orderNo));
		} catch (Exception e) {
			e.printStackTrace();
			JMResult.fail(this,"创建订单失败");
		}
	}
	
	
//	/**
//	 * 
//	 * @date 2018年12月20日 下午7:29:49
//	 * @author JaysonLee
//	 * @Description: 创建购物车订单
//	 * @reqMethod post
//	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
//	 * @paramter addressId,int,地址id,r:t,p:4,d:4
//	 * @paramter cartIds,String,购物车id用英文逗号分割,r:t,p:[187,188],d:[187,188]
//	 * @paramter remark,String,订单留言,r:f,p:这是留言,d:这是留言
//	 * @pDescription code:0失败1成功2未登录,desc:描述,no:订单号
//	 *
//	 */
//	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
//	@JMRulesVaild({
//		@JMRuleVaild(fields={"cartIds","addressId"})
//	})
//	public void createCart(){
//		Account account = getAttr("account");
//		int addressId = getParaToInt("addressId");
//		String cartIds = getPara("cartIds");
//		String remark = getPara("remark","");
//		try {
//			renderJson(orderDao.createCartOrder(cartIds, addressId, account.getId(), remark));
//		} catch (Exception e) {
//			e.printStackTrace();
//			JMResult.fail(this,"创建订单失败");
//		}
//	}


	/**
	 * 
	 * @date 2019年7月15日 15:49:10
	 * @Description: 用户端我的订单个数
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
		int state = getParaToInt("state",-1);
		JMResultUtil.success(orderService.myOrderNum(account, keyword, minPrice, maxPrice, startTime, endTime, state)).renderJson(this);
	}
	/**
	 * 
	 * @date 2018年12月20日 下午7:41:35
	 * @author JaysonLee
	 * @Description: 用户端我的订单
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter state,int,状态：-1全部 0待付款 2待发货 7待收货 8待评价,r:t,p:-1,d:-1
	 * @paramter pageSize,int,每页大小,r:f,p:10,d:10
	 * @paramter pageNumber,int,页码,r:f,p:1,d:1
	 * @pDescription	code:0失败1成功2未登录,desc:描述,no:订单号
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class})
	public void page(){
		Account account = getAttr("account");
		Integer pageNumber = getParaToInt("pageNumber", 1);
		Integer pageSize = getParaToInt("pageSize",JMConsts.pageSize);
		int state = getParaToInt("state",-1);
		JMResult.success(this,orderDao.pageMy(pageNumber, pageSize, state, account.getId()),"获取成功");
	}
	
	/**
	 * 
	 * @date 2018年12月21日 上午9:54:29
	 * @author JaysonLee
	 * @Description: 取消订单
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter orderId,int,订单id,r:t,p:10,d:10
	 * @pDescription	code:0失败1成功2未登录,desc:描述
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"orderId"})
	})
	public void cancel(){
		Account account = getAttr("account");
		int orderId = getParaToInt("orderId");
		renderJson(orderDao.cancel(account.getId(), orderId));
	}
	
	/**
	 * 
	 * @date 2019年1月5日 下午2:16:00
	 * @author JaysonLee
	 * @Description: 申请仅退款
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter orderId,int,订单id,r:t,p:26,d:26
	 * @paramter reason,String,退款原因,r:f,p:这是退款原因,d:退款原因
	 * @pDescription code:0失败1成功2未登录,desc:描述
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"orderId"})
	})
	public void refundOnlyMoney(){
		Account account = getAttr("account");
		int orderId = getParaToInt("orderId");
		String reason = getPara("reason","");
		renderJson(orderDao.toRefundAll(orderId, account.getId(), reason));
	}
	
	/**
	 * 
	 * @date 2019年1月5日 下午2:58:23
	 * @author JaysonLee
	 * @Description: 申请退货退款
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter orderId,int,订单id,r:t,p:51,d:51
	 * @paramter img1,file,退货图片1,r:f,p:D://testimg1.jpg,d:D://testimg1.jpg
	 * @paramter img2,file,退货图片2,r:f,p:D://testimg2.jpg,d:D://testimg2.jpg
	 * @paramter img3,file,退货图片3,r:f,p:D://testimg3.jpg,d:D://testimg3.jpg
	 * @paramter img4,file,退货图片4,r:f,p:D://testimg4.jpg,d:D://testimg4.jpg
	 * @paramter img5,file,退货图片5,r:f,p:D://testimg5.jpg,d:D://testimg5.jpg
	 * @paramter img6,file,退货图片6,r:f,p:D://testimg6.jpg,d:D://testimg6.jpg
	 * @paramter reason,String,退款原因,r:t,p:这是退款原因,d:退款原因
	 * @paramter type,int,退货类型：1退货退款2仅退款（未收到货）,r:t,p:1,d:1
	 * @paramter ids,String,退货退款订单商品id_数量 组合，用英文逗号分割,r:t,p:[45_1,46_1],d:[45_1,46_1]
	 * @pDescription code:0失败1成功2未登录,desc:描述
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"reason","ids","orderId","type"})
	})
	public void refundMoneyGoods(){
		List<String> imgList = UploadUtil.batchUploadImg(6, this, "img", "/goods/refund");
		String reason = getPara("reason");
		String ids = getPara("ids");
		int type = getParaToInt("type");
		int orderId = getParaToInt("orderId");
	    Account account = getAttr("account");
	    renderJson(orderDao.toRefunds(account.getId(), ids, orderId, reason,imgList,type));
	}
	
	
	/**
	 * 
	 * @date 2019年1月5日 下午2:58:23
	 * @author JaysonLee
	 * @Description: 申请退货退款(小程序用)
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter orderId,int,订单id,r:t,p:51,d:51
	 * @paramter img1,String,退货图片1(传相对路径),r:f,p:/upload/file/2/20190221/1550743665090_988772.jpg,d:/upload/file/2/20190221/1550743665090_988772.jpg
	 * @paramter img2,String,退货图片2,r:f,p:D://testimg2.jpg,d:D://testimg2.jpg
	 * @paramter img3,String,退货图片3,r:f,p:D://testimg3.jpg,d:D://testimg3.jpg
	 * @paramter img4,String,退货图片4,r:f,p:D://testimg4.jpg,d:D://testimg4.jpg
	 * @paramter img5,String,退货图片5,r:f,p:D://testimg5.jpg,d:D://testimg5.jpg
	 * @paramter img6,String,退货图片6,r:f,p:D://testimg6.jpg,d:D://testimg6.jpg
	 * @paramter reason,String,退款原因,r:t,p:这是退款原因,d:退款原因
	 * @paramter type,int,退货类型：1退货退款2仅退款（未收到货）,r:t,p:1,d:1
	 * @paramter ids,String,退货退款订单商品id_数量 组合，用英文逗号分割,r:t,p:[45_1,46_1],d:[45_1,46_1]
	 * @pDescription code:0失败1成功2未登录,desc:描述
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"reason","ids","orderId","type"})
	})
	public void refundMoneyGoodsForApplet(){
		List<String> imgList = new ArrayList<String>();
		for(int i = 1;i<=6;i++){
			String img = getPara("img"+i);
			imgList.add(img);
		}
		String reason = getPara("reason");
		String ids = getPara("ids");
		int type = getParaToInt("type");
		int orderId = getParaToInt("orderId");
		Account account = getAttr("account");
		renderJson(orderDao.toRefunds(account.getId(), ids, orderId, reason,imgList,type));
	}
	
	/**
	 * 
	 * @date 2019年1月7日 下午7:20:10
	 * @author JaysonLee
	 * @Description: 提交退货物流单号
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter refundOrderId,int,退货订单id,r:t,p:1,d:1
	 * @paramter logisticsNo,String,退货物流单号,r:t,p:1234567890,d:1234567890
	 * @pDescription	
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"logisticsNo","refundOrderId"})
	})
	public void refundLogisticsNo(){
		String logisticsNo = getPara("logisticsNo");
		int refundOrderId = getParaToInt("refundOrderId");
	    Account account = getAttr("account");
	    renderJson(orderDao.refundLogisticsNo(refundOrderId, logisticsNo, account.getId()));
	}
	
	/**
	 * 
	 * @date 2019年1月8日 上午10:21:55
	 * @author JaysonLee
	 * @Description: 售后列表
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter state,int,状态：-1全部0申请中1已通过等待退款2不通过申请3关闭申请4退货|退款完成5已通过等待上传物流单号退货6已上传物流单号7上传物流单号后不通过8上传物流单号后通过等待退款,r:f,p:-1,d:-1
	 * @paramter type,int,类型：-1全部0仅退款（发货前）1退货退款 2仅退款（发货后未收到货）,r:f,p:-1,d:-1
	 * @paramter pageSize,int,每页大小,r:f,p:10,d:10
	 * @paramter pageNumber,int,页码,r:f,p:1,d:1
	 * @pDescription	
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={})
	})
	public void refundList(){
		Account account = getAttr("account");
		int pageSize = getParaToInt("pageSize",10);
		int pageNumber = getParaToInt("pageNumber",1);
		int state = getParaToInt("state",-1);
		int type = getParaToInt("type",-1);
		JMResult.success(this,refundOrderDao.pageMy(account.getId(), pageNumber, pageSize, state, type),"获取成功");
	}
	
	/**
	 * 
	 * @date 2019年2月21日 下午5:11:57
	 * @author JaysonLee
	 * @Description: 退款详情
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter id,int,退款订单id,r:t,p:1,d:1
	 * @pDescription	
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"id"})
	})
	public void refundDetail(){
		int id = getParaToInt("id");
		JMResult.success(this,refundOrderDao.detail(id), "获取成功");
	}
	
	
	/**
	 * 
	 * @date 2019年1月8日 下午3:59:51
	 * @author JaysonLee
	 * @Description: 关闭申请
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter refundOrderId,int,退款订单id,r:t,p:1,d:1
	 * @pDescription	
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"refundOrderId"})
	})
	public void cancelRefund(){
		Account account = getAttr("account");
		int refundOrderId = getParaToInt("refundOrderId");
		renderJson(refundOrderDao.cancel(account.getId(), refundOrderId));
	}
	
	/**
	 * 
	 * @date 2019年1月8日 下午4:41:20
	 * @author JaysonLee
	 * @Description: 确认收货
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter orderId,int,订单id,r:t,p:51,d:51
	 * @pDescription	
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"orderId"})
	})
	public void confirmReceipt(){
		Account account = getAttr("account");
		int orderId = getParaToInt("orderId");
		renderJson(orderDao.confirmReceipt(account.getId(), orderId));
	}
	
	/**
	 * 
	 * @date 2019年2月18日 上午9:21:32
	 * @author JaysonLee
	 * @Description: 订单详情
	 * @reqMethod post
	 * @paramter orderId,int,订单id,r:t,p:51,d:51
	 * @pDescription	
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"orderId"})
	})
	public void detail(){
		Account account = getAttr("account");
		int orderId = getParaToInt("orderId");
		Order order = orderDao.detail(account.getId(), orderId);
		JMResult.success(this,order,"获取成功");
	}
	
	/**
	 * 
	 * @date 2019年2月18日 上午10:05:53
	 * @author JaysonLee
	 * @Description: 删除订单
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter orderId,int,订单id,r:t,p:51,d:51
	 * @pDescription	
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"orderId"})
	})
	public void delOrder(){
		Account account = getAttr("account");
		int orderId = getParaToInt("orderId");
		if(orderDao.delOrder(account.getId(), orderId)){
			JMResult.success(this,"操作成功");
			return ;
		}
		JMResult.fail(this,"操作失败");
	}
	

	/**
	 * 
	 * @date 2019年2月18日 上午10:05:53
	 * @author JaysonLee
	 * @Description: 查看订单物流状态
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter orderNo,String,订单编号,r:t,p:51,d:51
	 * @pDescription	
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"orderNo"})
	})
	public void selectOrderLogistics() {
		Account account = getAttr("account");
		String orderNo = getPara("orderNo");
		try {
			orderDao.selectOrderLogistics(orderNo,account).renderJson(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @date 2019年7月15日 15:49:10
	 * @Description: 商家端我的订单
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter state,int,状态：-1全部 0待付款 2待发货 7待收货 8已完成,r:t,p:-1,d:-1
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
	public void myShopOrder(){
		Account account = getAttr("account");
		Integer pageNumber = getParaToInt("pageNumber", 1);
		Integer pageSize = getParaToInt("pageSize",JMConsts.pageSize);
		int state = getParaToInt("state",-1);
		String keyword = getPara("keyword");
		String minPrice = getPara("minPrice");
		String maxPrice = getPara("maxPrice");
		String startTime = getPara("startTime");
		String endTime = getPara("endTime");
		JMResultUtil.success(orderService.pageMyShopOrder(account, state, keyword, minPrice, maxPrice, startTime, endTime, pageNumber, pageSize)).renderJson(this);
	}

	/**
	 * 
	 * @date 2019年7月15日 15:49:10
	 * @Description: 商家端我的订单个数
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
	public void myShopOrderNum() {
		Account account = getAttr("account");
		String keyword = getPara("keyword");
		String minPrice = getPara("minPrice");
		String maxPrice = getPara("maxPrice");
		String startTime = getPara("startTime");
		String endTime = getPara("endTime");
		int state = getParaToInt("state",-1);
		JMResultUtil.success(orderService.myShopOrderNum(account, keyword, minPrice, maxPrice, startTime, endTime, state)).renderJson(this);
	}

	/**
	 * 
	 * @date 2019年7月23日 09:59:25
	 * @Description: 商家端售后列表
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
	public void shopRefundPage(){
		Account account = getAttr("account");
		int pageSize = getParaToInt("pageSize",10);
		int pageNumber = getParaToInt("pageNumber",1);
		int state = getParaToInt("state",-1);
		int type = getParaToInt("type",-1);
		JMResult.success(this,refundOrderDao.shopRefundPage(account.getId(), pageNumber, pageSize, state, type),"获取成功");
	}


	/**
	 * 
	 * @date 2019年7月23日 09:59:25
	 * @Description: 商品是否能购买
	 * @reqMethod post
	 * @paramter goodsId,int,商品id,r:t,p:1,d:1
	 * @paramter orderId,int,订单id,r:f,p:1,d:1
	 * @pDescription true:可以购买,false:不能购买
	 *
	 */
	@Before(value={JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"goodsId"})
	})
	public void goodsCanPay() {
		Integer goodsId = getParaToInt("goodsId");
		Integer orderId = getParaToInt("orderId", null);
		renderJson(JMResultUtil.success(orderService.canPay(goodsId, orderId)));
	}
	
}