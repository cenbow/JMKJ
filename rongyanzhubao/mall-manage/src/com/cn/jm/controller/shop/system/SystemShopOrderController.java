
package com.cn.jm.controller.shop.system;

import java.util.ArrayList;
import java.util.List;

import com.cn._gen.model.Order;
import com.cn._gen.model.OrderGoods;
import com.cn._gen.model.RefundOrder;
import com.cn._gen.model.Spec;
import com.cn._gen.model.SpecAttribute;
import com.cn.jm._dao.goods.JMGoodsModelsDao;
import com.cn.jm._dao.order.JMOrderDao;
import com.cn.jm._dao.order.JMOrderGoodsDao;
import com.cn.jm._dao.order.OrderState;
import com.cn.jm._dao.refund.JMRefundGoodsDao;
import com.cn.jm._dao.refund.JMRefundOrderDao;
import com.cn.jm._dao.spec.JMSpecAttributeDao;
import com.cn.jm._dao.spec.JMSpecDao;
import com.cn.jm.core.JMConsts;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.web.controller.JMBaseSystemController;
import com.cn.jm.interceptor.SystemLoginInterceptor;
import com.cn.jm.service.JMOrderService;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;

/**
 * Generated by 广州小跑robot.
 */
@JMRouterMapping(url = SystemShopOrderController.url)
@Before(value={SystemLoginInterceptor.class})
public class SystemShopOrderController extends JMBaseSystemController {
	
	public static final String path = JMConsts.base_view_url+"/system/shop/order";
	public static final String url ="/system/shop/order";
	
	@Inject
	public JMSpecDao specDao;
	@Inject
	public JMSpecAttributeDao specAttributeDao ;
	@Inject
	public JMGoodsModelsDao goodsModelsDao ;
	@Inject
	public JMOrderDao orderDao ;
	@Inject
	public JMOrderGoodsDao orderGoodsDao ;
//	@Inject
//	public JMAccountExpandDao accountExpandDao;
	@Inject
	public JMRefundOrderDao refundOrderDao ;
	@Inject
	public JMRefundGoodsDao refundGoodsDao ;
	
	@Inject
	JMOrderService orderService;
	
	public void index(){
		page();
	}
	
	public void page(){
		render(path+"/list.html");
	}
	
	public void pageData(){
		String keyword = getPara("orderNo","");
		String addressName = getPara("addressName","");
		String address = getPara("address","");
		String goodsName = getPara("goodsName","");
//		Integer id = getParaToInt("id",null);
		
		String startTime = getPara("startTime","");
		String endTime = getPara("endTime","");
		
		int state = getParaToInt("states",-1);
		
		
		Integer pageNumber = getParaToInt("pageNumber",1);
		Integer pageSize = getParaToInt("pageSize",10);
		Page<OrderGoods> page = orderDao.pageSys(keyword, addressName, startTime, endTime, state, pageNumber, pageSize,address,goodsName);
		JMResult.success(this,page,"");
	}
	
	
	public void toDetail(){
		setAttr("orderId",getParaToInt("orderId"));
		render(path+"/detail.html");
	}
	
	
//	public void detailData(){
//		int orderId = getParaToInt("orderId");
//		Order order = orderDao.getById(orderId,false);
//		order.put("orderGoodsList",orderGoodsDao.listByOrderId(orderId));
//		order.put("buyName",accountExpandDao.getInfoByUserId(order.getAccountId()));
//		JMResult.success(this,order,"获取成功");
//	}
	
	
	public void add(){
		setToken();
		
		setAttr("action", JMConsts.ACTION_ADD);
		setAttr("spec", new Spec());
		List<SpecAttribute> list = new ArrayList<>();
		list.add(new SpecAttribute());
		//setAttr("specAttrs",list);
		setAttr("mdList",goodsModelsDao.list(false));
		render(path+"/add.html");
	}
	
	public void save(){
		String []attrs = getParaValues("attrs");
		Spec spec = getModel(Spec.class);
		if(specDao.add(spec.getValue(),spec.getShopMdId(), attrs)){
			JMResult.success(this,"添加成功");
			return ;
		}else{
			JMResult.fail(this,"添加失败");
		}
	}
	
	
	public void edit(){
		setToken();
		int id = getParaToInt("id");
		setAttr("spec", specDao.getById(id,false));
		setAttr("mdList",goodsModelsDao.list(false));
		setAttr("specAttrs",specAttributeDao.listAttrsBySpecId(id));
		render(path+"/edit.html");
	}
	
	public void update(){
		Spec spec = getModel(Spec.class);
		String []attrs = getParaValues("attrs");
		Integer []attrsId = getParaValuesToInt("attrsId");
		boolean result = specDao.updateSpec(spec, attrsId, attrs);
		if(result){
			JMResult.success(this, "编辑成功");
		}else{
			JMResult.fail(this, "编辑失败");
		}
	}
	
	
	public void delete(){
		int id = getParaToInt("id");
		
		if (specDao.deleteById(id)) {
			JMResult.success(this, "删除成功");
		}else {
			JMResult.fail(this, "删除失败");
		}
	}
	
	
	public void dels(){
		Integer[] ids = getParaValuesToInt("ids");
		if(ids != null){
			boolean result = specDao.deleteByIds(ids);
			if (result) {
				JMResult.success(this, "删除成功");
			}else {
				JMResult.fail(this, "删除失败");
			}
		}else {
			JMResult.fail(this, "请选择需要删除的数据");
		}
	}
	
	/**
	 * 
	 * @date 2019年2月26日 下午3:42:07
	 * @author JaysonLee
	 * @Description: 进入发货框
	 * @reqMethod post
	 * @paramter
	 * @pDescription	
	 *
	 */
	
	public void toDelivery(){
		int orderId = getParaToInt("orderId");
		setAttr("orderId",orderId);
		createToken();
		render(path+"/delivery.html");
	}
	
	/**
	 * 
	 * @date 2019年2月26日 下午3:42:40
	 * @author JaysonLee
	 * @Description: 发货
	 * @reqMethod post
	 * @paramter
	 * @pDescription	
	 *
	 */
	public void deliveryOrder(){
		int orderId = getParaToInt("orderId");
		String logisticsNo = getPara("logisticsNo",null);
		JMResult jmResult = orderDao.deliveryOrder(orderId, logisticsNo);
		renderJson(jmResult);
	}
	
	
	public void toRefundDetail(){
		int orderId = getParaToInt("orderId");
		setAttr("orderId",orderId);
		render(path+"/refundDetail.html");
	}
	
	
	public void toHandleRefund(){
		int orderId = getParaToInt("id");
		setAttr("id",orderId);
		render(path+"/handleRefund.html");
	}
	
	
	public void selectRefundDetail(){
		int orderId = getParaToInt("orderId");
		setAttr("orderId",orderId);
		List<RefundOrder> refundOrders = refundOrderDao.list("select * from shop_refund_order where orderId = "+orderId, false);
		for(RefundOrder refundOrder : refundOrders){
			if(refundOrder != null){
				refundOrder.put("refundGoodsList",refundGoodsDao.listByRefundOrderId(refundOrder.getId()));
//				refundOrder.put("buyUser",accountExpandDao.getInfoByUserId(refundOrder.getUserId()));
				refundOrder.put("order",orderDao.getById(orderId,false));
			}
		}
		JMResult.success(this,refundOrders,"获取成功");
	}
	
	/**
	 * 
	 * @date 2019年2月27日 上午11:44:52
	 * @author JaysonLee
	 * @Description: 处理退款
	 * @reqMethod post
	 * @paramter
	 * @pDescription	
	 *
	 */
	
//	public void handleRefund(){
//		int id = getParaToInt("id");
//		String reason = getPara("reason","");
//		int state = getParaToInt("state",2);//1通过2不通过
//		RefundOrder refundOrder = refundOrderDao.getById(id,false);
//		if(refundOrder.getType() == 0 ){//仅退款(发货前)
//			//state 1已通过等待退款（针对仅退款）2不通过申请5已通过等待上传物流单号退货
//			renderJson(orderDao.dealAllRefund(id, state,reason));
//			return ;
//		}else if(refundOrder.getType() == 1){//退货退款
//			int dealState = state == 2 ? 7 : 8 ;//7上传物流单号后不通过8上传物流单号后通过等待退款
//			renderJson(orderDao.dealGoodsRefund(id, dealState,reason));
//			return ;
//		}else if(refundOrder.getType() == 2){
//			int dealState = state == 1 ? 5 : 2 ;
//			renderJson(orderDao.dealAllRefund(id, dealState,reason));
//			return ;
//		}else{
//			JMResult.success(this,"非法操作");
//		}
//	}

	
	public void cancel() {
		Integer orderId = getParaToInt("orderId");
		Order order = orderDao.getById(orderId,true);
		order.setState(6);
		orderDao.update(order);
		JMResult.success(this,"操作成功");
	}
	
	public void paySuccess() {
		Integer orderId = getParaToInt("orderId");
		Order order = orderDao.getById(orderId);
		if(!OrderState.WAIT_SERVE.equals(order.getState())) {
			JMResult.fail(this, "失败,订单状态错误");
			return ;
		}
		orderService.paySuccesss(order);
		JMResult.success(this,"操作成功");
	}
}