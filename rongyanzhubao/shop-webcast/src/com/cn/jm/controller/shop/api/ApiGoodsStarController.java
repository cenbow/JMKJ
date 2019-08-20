
package com.cn.jm.controller.shop.api;

import java.util.ArrayList;
import java.util.List;

import com.cn._gen.model.Account;
import com.cn.jm._dao.account.JMAccountDao;
import com.cn.jm._dao.goods.JMGoodsStarDao;
import com.cn.jm._dao.order.JMOrderDao;
import com.cn.jm._dao.zan.ZanEnum;
import com.cn.jm.core.JMConsts;
import com.cn.jm.core.interceptor.JMVaildInterceptor;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.valid.annotation.JMRuleVaild;
import com.cn.jm.core.valid.annotation.JMRulesVaild;
import com.cn.jm.core.web.controller.JMBaseApiController;
import com.cn.jm.interceptor.JMApiAccountInterceptor;
import com.cn.jm.service.JMGoodsStarService;
import com.cn.jm.service.JMZanService;
import com.cn.jm.util.JMResultUtil;
import com.cn.jm.util.UploadUtil;
import com.cn.jm.web.core.parse.annotation.API;
import com.cn.jm.web.core.parse.annotation.ParseOrder;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;

/**
 * 
 *
 * @date 2019年1月7日 下午3:31:04
 * @author Administrator
 * @Description: 评论模块
 *
 */
@JMRouterMapping(url = ApiGoodsStarController.url)
@ParseOrder(6)
@API
public class ApiGoodsStarController extends JMBaseApiController {
	
	public static final String url ="/api/goods/star";
	@Inject
	public JMGoodsStarDao goodsStarDao;
	@Inject
	public JMOrderDao orderDao ;
	@Inject
	public JMAccountDao accountDao ;
	@Inject
	JMGoodsStarService goodsStarService;
	@Inject
	JMZanService zanService;
	
	/**
	 * 
	 * @date 2019年1月7日 上午10:31:30
	 * @author JaysonLee
	 * @Description: 发表评价
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter num,int,多少条评论,r:t,p:2,d:2
	 * @paramter starNum1,int,星星数，starNum后面的数字代表第几条评论，例如starNum1代表第一条评论的星星数，以此类推，五星封顶,r:t,p:3,d:3
	 * @paramter content1,String,评论内容，content后面的数字代表第几条评论，例如content1代表第一条评论的内容，以此类推,r:t,p:这是第一条评论,d:这是第一条评论
	 * @paramter orderGoodsId1,int,被评论的商品订单id，后面的数字代表第几条评论，例如orderGoodsId1代表第一条评论的商品订单id---orderGoodsId，以此类推,r:t,p:51,d:51
	 * @paramter img1_1,file,第一条评论的图片1，img后面的数字代表第几条评论，_后面的数字代表此条评论的第几张图片，例如img1_2代表第一条评论的第二张图片，以此类推,r:f,p:D://testimg1.jpg,d:D://testimg1.jpg
	 * @paramter img1_2,file,第一条评论的图片2，img后面的数字代表第几条评论，_后面的数字代表此条评论的第几张图片，例如img1_2代表第一条评论的第二张图片，以此类推,r:f,p:D://testimg2.jpg,d:D://testimg2.jpg
	 * @paramter img1_3,file,第一条评论的图片3，img后面的数字代表第几条评论，_后面的数字代表此条评论的第几张图片，例如img1_2代表第一条评论的第二张图片，以此类推,r:f,p:D://testimg3.jpg,d:D://testimg3.jpg
	 * @paramter img1_4,file,第一条评论的图片4，img后面的数字代表第几条评论，_后面的数字代表此条评论的第几张图片，例如img1_2代表第一条评论的第二张图片，以此类推,r:f,p:D://testimg4.jpg,d:D://testimg4.jpg
	 * @paramter img1_5,file,第一条评论的图片5，img后面的数字代表第几条评论，_后面的数字代表此条评论的第几张图片，例如img1_2代表第一条评论的第二张图片，以此类推,r:f,p:D://testimg5.jpg,d:D://testimg5.jpg
	 * @paramter img1_6,file,第一条评论的图片6，img后面的数字代表第几条评论，_后面的数字代表此条评论的第几张图片，例如img1_2代表第一条评论的第二张图片，以此类推,r:f,p:D://testimg6.jpg,d:D://testimg6.jpg
	 * @paramter starNum2,int,星星数，starNum后面的数字代表第几条评论，例如starNum2代表第二条评论的星星数，以此类推，五星封顶,r:t,p:5,d:5
	 * @paramter content2,String,评论内容，content后面的数字代表第几条评论，例如content1代表第一条评论的内容，以此类推,r:t,p:这是第二条评论,d:这是第二条评论
	 * @paramter orderGoodsId2,int,被评论的商品订单id，后面的数字代表第几条评论，例如orderGoodsId1代表第一条评论的商品订单id---orderGoodsId，以此类推,r:t,p:50,d:50
	 * @paramter img2_1,file,第二条评论的图片1，img后面的数字代表第几条评论，_后面的数字代表此条评论的第几张图片，例如img1_2代表第一条评论的第二张图片，以此类推,r:f,p:D://testimg1.jpg,d:D://testimg1.jpg
	 * @paramter img2_2,file,第二条评论的图片2，img后面的数字代表第几条评论，_后面的数字代表此条评论的第几张图片，例如img1_2代表第一条评论的第二张图片，以此类推,r:f,p:D://testimg2.jpg,d:D://testimg2.jpg
	 * @paramter img2_3,file,第二条评论的图片3，img后面的数字代表第几条评论，_后面的数字代表此条评论的第几张图片，例如img1_2代表第一条评论的第二张图片，以此类推,r:f,p:D://testimg3.jpg,d:D://testimg3.jpg
	 * @paramter img2_4,file,第二条评论的图片4，img后面的数字代表第几条评论，_后面的数字代表此条评论的第几张图片，例如img1_2代表第一条评论的第二张图片，以此类推,r:f,p:D://testimg4.jpg,d:D://testimg4.jpg
	 * @paramter img2_5,file,第二条评论的图片5，img后面的数字代表第几条评论，_后面的数字代表此条评论的第几张图片，例如img1_2代表第一条评论的第二张图片，以此类推,r:f,p:D://testimg5.jpg,d:D://testimg5.jpg
	 * @paramter img2_6,file,第二条评论的图片6，img后面的数字代表第几条评论，_后面的数字代表此条评论的第几张图片，例如img1_2代表第一条评论的第二张图片，以此类推,r:f,p:D://testimg6.jpg,d:D://testimg6.jpg
	 * @pDescription code:0失败1成功2未登录,desc:描述,data:评价id	
	 *
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class, JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"num"})
	})
	public void evaluate(){
		int num = getParaToInt("num",1);
		Account account = getAttr("account");
		JMResult jmResult = JMResult.create();
		for(int i = 1;i<=num;i++){
			List<String> imgList = UploadUtil.batchUploadImg(6, this, "img"+i+"_", "/goods/evaluate");
			int orderGoodsId = getParaToInt("orderGoodsId"+i);
			int starNum = getParaToInt("starNum"+i,1);
			String content = getPara("content"+i,"");
			jmResult = goodsStarDao.add(account.getId(), orderGoodsId, starNum, content, imgList);
			if(jmResult.getCode() == JMResult.FAIL){
				renderJson(jmResult);
				return ;
			}
		}
		int orderGoodsId = getParaToInt("orderGoodsId1");
		orderDao.upDateOrderByGoodsOrderId(orderGoodsId);
		renderJson(jmResult);
	}
	/**
	 * 
	 * @date 2019年1月7日 上午10:31:30
	 * @author JaysonLee
	 * @Description: 发表评价(小程序用)图片全部传地址（相对地址，不要用全链）
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter num,int,多少条评论,r:t,p:2,d:2
	 * @paramter starNum1,int,星星数，starNum后面的数字代表第几条评论，例如starNum1代表第一条评论的星星数，以此类推，五星封顶,r:t,p:3,d:3
	 * @paramter content1,String,评论内容，content后面的数字代表第几条评论，例如content1代表第一条评论的内容，以此类推,r:t,p:这是第一条评论,d:这是第一条评论
	 * @paramter orderGoodsId1,int,被评论的商品订单id，后面的数字代表第几条评论，例如orderGoodsId1代表第一条评论的商品订单id---orderGoodsId，以此类推,r:t,p:51,d:51
	 * @paramter img1_1,String,第一条评论的图片1，img后面的数字代表第几条评论，_后面的数字代表此条评论的第几张图片，例如img1_2代表第一条评论的第二张图片，以此类推,r:f,p:D://testimg1.jpg,d:D://testimg1.jpg
	 * @paramter img1_2,String,第一条评论的图片2，img后面的数字代表第几条评论，_后面的数字代表此条评论的第几张图片，例如img1_2代表第一条评论的第二张图片，以此类推,r:f,p:D://testimg2.jpg,d:D://testimg2.jpg
	 * @paramter img1_3,String,第一条评论的图片3，img后面的数字代表第几条评论，_后面的数字代表此条评论的第几张图片，例如img1_2代表第一条评论的第二张图片，以此类推,r:f,p:D://testimg3.jpg,d:D://testimg3.jpg
	 * @paramter img1_4,String,第一条评论的图片4，img后面的数字代表第几条评论，_后面的数字代表此条评论的第几张图片，例如img1_2代表第一条评论的第二张图片，以此类推,r:f,p:D://testimg4.jpg,d:D://testimg4.jpg
	 * @paramter img1_5,String,第一条评论的图片5，img后面的数字代表第几条评论，_后面的数字代表此条评论的第几张图片，例如img1_2代表第一条评论的第二张图片，以此类推,r:f,p:D://testimg5.jpg,d:D://testimg5.jpg
	 * @paramter img1_6,String,第一条评论的图片6，img后面的数字代表第几条评论，_后面的数字代表此条评论的第几张图片，例如img1_2代表第一条评论的第二张图片，以此类推,r:f,p:D://testimg6.jpg,d:D://testimg6.jpg
	 * @paramter starNum2,int,星星数，starNum后面的数字代表第几条评论，例如starNum2代表第二条评论的星星数，以此类推，五星封顶,r:t,p:5,d:5
	 * @paramter content2,String,评论内容，content后面的数字代表第几条评论，例如content1代表第一条评论的内容，以此类推,r:t,p:这是第二条评论,d:这是第二条评论
	 * @paramter orderGoodsId2,int,被评论的商品订单id，后面的数字代表第几条评论，例如orderGoodsId1代表第一条评论的商品订单id---orderGoodsId，以此类推,r:t,p:50,d:50
	 * @paramter img2_1,String,第二条评论的图片1，img后面的数字代表第几条评论，_后面的数字代表此条评论的第几张图片，例如img1_2代表第一条评论的第二张图片，以此类推,r:f,p:D://testimg1.jpg,d:D://testimg1.jpg
	 * @paramter img2_2,String,第二条评论的图片2，img后面的数字代表第几条评论，_后面的数字代表此条评论的第几张图片，例如img1_2代表第一条评论的第二张图片，以此类推,r:f,p:D://testimg2.jpg,d:D://testimg2.jpg
	 * @paramter img2_3,String,第二条评论的图片3，img后面的数字代表第几条评论，_后面的数字代表此条评论的第几张图片，例如img1_2代表第一条评论的第二张图片，以此类推,r:f,p:D://testimg3.jpg,d:D://testimg3.jpg
	 * @paramter img2_4,String,第二条评论的图片4，img后面的数字代表第几条评论，_后面的数字代表此条评论的第几张图片，例如img1_2代表第一条评论的第二张图片，以此类推,r:f,p:D://testimg4.jpg,d:D://testimg4.jpg
	 * @paramter img2_5,String,第二条评论的图片5，img后面的数字代表第几条评论，_后面的数字代表此条评论的第几张图片，例如img1_2代表第一条评论的第二张图片，以此类推,r:f,p:D://testimg5.jpg,d:D://testimg5.jpg
	 * @paramter img2_6,String,第二条评论的图片6，img后面的数字代表第几条评论，_后面的数字代表此条评论的第几张图片，例如img1_2代表第一条评论的第二张图片，以此类推,r:f,p:D://testimg6.jpg,d:D://testimg6.jpg
	 * @pDescription code:0失败1成功2未登录,desc:描述,data:评价id	
	 *
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"num"})
	})
	public void evaluateForApplet(){
		int num = getParaToInt("num",1);
		Account account = getAttr("account");
		JMResult jmResult = JMResult.create();
		for(int i = 1;i<=num;i++){
			List<String> imgList = new ArrayList<String>();
			for(int j=1;j<=6;j++){
				String img = getPara("img"+i+"_"+j);
				imgList.add(img);
			}
			int orderGoodsId = getParaToInt("orderGoodsId"+i);
			int starNum = getParaToInt("starNum"+i,1);
			String content = getPara("content"+i,"");
			jmResult = goodsStarDao.add(account.getId(), orderGoodsId, starNum, content, imgList);
			if(jmResult.getCode() == JMResult.FAIL){
				renderJson(jmResult);
				return ;
			}
		}
		int orderGoodsId = getParaToInt("orderGoodsId1");
		orderDao.upDateOrderByGoodsOrderId(orderGoodsId);
		renderJson(jmResult);
	}
	
	/**
	 * 
	 * @date 2019年1月7日 下午4:07:42
	 * @author JaysonLee
	 * @Description: 我的评论
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter pageSize,int,每页大小,r:f,p:10,d:10
	 * @paramter pageNumber,int,页码,r:f,p:1,d:1
	 * @pDescription	
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class})
	public void myComments(){
		Account account = getAttr("account");
		Integer pageNumber = getParaToInt("pageNumber", 1);
		Integer pageSize = getParaToInt("pageSize",JMConsts.pageSize);
		JMResult.success(this,goodsStarDao.pageMy(account.getId(), pageSize, pageNumber),"获取成功");
	}
	
	/**
	 * 
	 * @date 2019年1月8日 下午5:10:04
	 * @author JaysonLee
	 * @Description: 评论详情
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter id,int,评论id,r:t,p:1,d:1
	 * @pDescription	
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"id"})
	})
	public void detail(){
		Account account = getAttr("account");
		int id = getParaToInt("id");
		renderJson(goodsStarDao.detail(account.getId(), id));
	}
	
	/**
	 * 
	 * @date 2019年1月26日 上午9:57:56
	 * @author JaysonLee
	 * @Description: 商品评论列表
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter goodsId,int,商品id,r:t,p:1,d:1
	 * @paramter pageSize,int,每页大小,r:f,p:10,d:10
	 * @paramter pageNumber,int,页码,r:f,p:1,d:1
	 * @pDescription	
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class, JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"goodsId"})
	})
	public void pageByGoods(){
		Account account = getAttr("account");
		Integer userId = account.getId();
		Integer pageNumber = getParaToInt("pageNumber", 1);
		Integer pageSize = getParaToInt("pageSize",JMConsts.pageSize);
		int goodsId = getParaToInt("goodsId");
		JMResult.success(this,goodsStarDao.pageByApi(goodsId, pageSize, userId, pageNumber),"获取成功");
	}

	/**
	 * 
	 * @date 2019年1月26日 上午9:57:56
	 * @Description: 单个订单评论列表
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter orderId,int,订单id,r:t,p:1,d:1
	 * @pDescription	
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"orderId"})
	})
	public void goodsStarByOrderId(){
		Account account = getAttr("account");
		int orderId = getParaToInt("orderId");
		JMResult.success(this, goodsStarDao.goodsStarByOrderId(orderId, account.getId()), "获取成功");
	}
	
	/**
	 * 
	 * @date 2019年7月12日 10:10:01
	 * @Description: 点赞评论或者取消评论
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter goodsStarId,int,评论id,r:t,p:1,d:1
	 * @pDescription	
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"goodsStarId"})
	})
	public void zan(){
		int goodsStarId = getParaToInt("goodsStarId");
		Account account = getAttr("account");
		zanService.zan(goodsStarId, account.getId(), ZanEnum.GOODS_STAR_TYPE);
		JMResultUtil.success().renderJson(this);
	}
	
	
	
}