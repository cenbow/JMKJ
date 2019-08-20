package com.cn.jm.controller.shop.api;

import java.util.ArrayList;
import java.util.List;

import com.cn._gen.model.Account;
import com.cn._gen.model.Goods;
import com.cn._gen.model.GoodsResale;
import com.cn._gen.model.Marketing;
import com.cn.jm._dao.account.JMAccountDao;
import com.cn.jm._dao.goods.JMGoodsCartDao;
import com.cn.jm._dao.goods.JMGoodsDao;
import com.cn.jm._dao.keyword.JMKeywordDao;
import com.cn.jm._dao.marketing.JMMarketingDao;
import com.cn.jm._dao.marketing.JMMarketingGoodsDao;
import com.cn.jm._dao.search.JMSearchHistoryDao;
import com.cn.jm._dao.sku.JMSkuDao;
import com.cn.jm._dao.spec.JMSpecDao;
import com.cn.jm.core.JMConsts;
import com.cn.jm.core.interceptor.JMVaildInterceptor;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.valid.annotation.JMRuleVaild;
import com.cn.jm.core.valid.annotation.JMRulesVaild;
import com.cn.jm.core.web.controller.JMBaseApiController;
import com.cn.jm.interceptor.JMApiAccountInterceptor;
import com.cn.jm.service.JMGoodsResaleService;
import com.cn.jm.service.JMGoodsService;
import com.cn.jm.util.UploadUtil;
import com.cn.jm.web.core.parse.annotation.API;
import com.cn.jm.web.core.parse.annotation.ParseOrder;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
/**
 * 
 *
 * @date 2018年12月19日 上午9:48:28
 * @author Administrator
 * @Description: 商品模块
 *
 */
@API
@ParseOrder(2)
@JMRouterMapping(url = ApiGoodsController.url)
public class ApiGoodsController extends JMBaseApiController {
	
	public static final String url = "/api/goods";
	@Inject
	public JMGoodsDao goodsDao;
	@Inject
	public JMSkuDao jmSkuDao;
	@Inject
	public JMGoodsCartDao goodsCartDao ;
	@Inject
	public JMAccountDao accountDao ;
	@Inject
	public JMSearchHistoryDao searchHistoryDao ;
	@Inject
	public JMMarketingDao marketingDao ;
	@Inject
	public JMMarketingGoodsDao marketingGoodsDao ;
	@Inject
	public JMSpecDao specDao;
	@Inject
	public JMKeywordDao keywordDao;
	
	@Inject
	JMGoodsResaleService goodsResaleService;
	@Inject
	JMGoodsService goodsService;
	
	/**
	 * 
	 * @date 2018年12月17日 下午6:01:04
	 * @author JaysonLee
	 * @Description: 商品列表
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID(这个接口可不传，有就传),r:f,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter keyword,String,关键字查询,r:f
	 * @paramter pageSize,int,每页大小,r:f,d:10
	 * @paramter pageNumber,int,页码,r:f,d:1
	 * @paramter minprice,string,最小价格,r:f,d:0
	 * @paramter maxprice,int,最大价格,r:f,d:null
	 * @paramter labelId,int,标签id精确到二级标签，-1代表没有筛选,r:f,d:-1
	 * @paramter serviceType,int,-1全部，0包邮， 1七天内退货，2包邮+七天内退货,r:f,d:-1
	 * @paramter isRoomGoods,int,-1全部0直播间商品,r:f,d:0
	 * @paramter type,int,查找类型-1：默认排序（按照时间倒序）0:推荐(并且必传labelId) 2：最新 3：销量DESC 4：销量ASC 5：价格DESC 6：价格ASC 7最旧 ,r:f,d:-1
	 * @pDescription code:0失败1成功2未登录,desc:描述,skulist:当前商品所有可用sku列表,speclist:当前商品显示的所有规格列表,serviceType:-2都没有，0包邮， 1七天内退货，2包邮+七天内退货,collectedNum:收藏数,isCollected:是否收藏（boolean）,resaleNum:转售数,resalePrice:转售金额,sellState:为0时则已结缘,isBroad:是否直播中 true为直播中,roomId如果不为空的话则搜索页面显示的是直播间信息
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class})
	@API(isScran=true)
	public void page(){
		Account account = getAttr("account");
		Integer userId = account.getId() ;
		String keyword = getPara("keyword", "");
		Integer labelId = getParaToInt("labelId", -1);//标签id精确到二级标签,-1代表没有筛选
		Integer type = getParaToInt("type", -1);
		
		String minprice = getPara("minprice", "0");//最小价格
		String maxprice = getPara("maxprice", null);
		
		int serviceType = getParaToInt("serviceType", -1);//-1全部，0包邮， 1七天内退货，2包邮+七天内退货
		
		Integer pageNumber = getParaToInt("pageNumber", 1);
		Integer pageSize = getParaToInt("pageSize",JMConsts.pageSize);
		Integer isRoomGoods = getParaToInt("isRoomGoods",-1);
		
		Page<Goods> page = goodsDao.pageForApi(keyword, labelId, type, minprice, maxprice, serviceType, pageNumber, pageSize, userId, isRoomGoods);
		
		JMResult.success(this, page,"获取成功");
	}
	
	/**
	 * 
	 * @Description: 我的商品列表
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID(这个接口可不传，有就传),r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter keyword,String,关键字查询,r:f
	 * @paramter pageSize,int,每页大小,r:f,d:10
	 * @paramter pageNumber,int,页码,r:f,d:1
	 * @paramter minprice,string,最小价格,r:f,d:0
	 * @paramter maxprice,string,最大价格,r:f,d:null
	 * @paramter startTime,string,开始时间,r:f,d:null
	 * @paramter endTime,string,结束时间,r:f,d:null
	 * @paramter timeOrder,string,ASC时间升序DESC降序,r:f,d:DESC
	 * @paramter priceOrder,string,ASC价格升序DESC降序,r:f,d:null
	 * @paramter sellState,int,0已出售1未出售,r:t,d:0
	 * @paramter labelId,int,标签id精确到二级标签，-1代表没有筛选,r:f,d:-1
	 * @pDescription code:0失败1成功2未登录,desc:描述,skulist:当前商品所有可用sku列表,speclist:当前商品显示的所有规格列表,serviceType:-2都没有，0包邮， 1七天内退货，2包邮+七天内退货,collectedNum:收藏数,isCollected:是否收藏（boolean）,resaleNum:转售数,resalePrice:转售金额,sellState:为0时则已结缘,isBroad:是否直播中 true为直播中,roomId如果不为空的话则搜索页面显示的是直播间信息
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class})
	@API(isScran=true)
	public void myGoodsPage(){
		Account account = getAttr("account");
		Integer pageNumber = getParaToInt("pageNumber", 1);
		Integer pageSize = getParaToInt("pageSize",JMConsts.pageSize);
		String keyword = getPara("keyword");
		String minprice = getPara("minprice", "0");//最小价格
		String maxprice = getPara("maxprice");
		String startTime = getPara("startTime");
		String endTime = getPara("endTime");
		Integer labelId = getParaToInt("labelId", -1);
		String timeOrder = getPara("timeOrder", "DESC");
		String priceOrder = getPara("priceOrder");
		Integer sellState = getParaToInt("sellState");
		Page<Goods> page = goodsDao.pageMyGoods(pageNumber, pageSize, keyword, minprice, maxprice, startTime, endTime, labelId, timeOrder, priceOrder, sellState, account);
		
		JMResult.success(this, page,"获取成功");
	}

	/**
	 * 
	 * @date 2019年7月4日 09:07:07
	 * @Description: 根据栏目id获取商品列表
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID(这个接口可不传，有就传),r:f,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter pageSize,int,每页大小,r:f,d:10
	 * @paramter pageNumber,int,页码,r:f,d:1
	 * @paramter columnId,int,栏目id,r:f,d:-1
	 * @pDescription code:0失败1成功2未登录,desc:描述,skulist:当前商品所有可用sku列表,speclist:当前商品显示的所有规格列表,serviceType:-2都没有，0包邮， 1七天内退货，2包邮+七天内退货,,isCollected:是否收藏（boolean）,resaleNum:转售数,resalePrice:转售金额,sellState:为0时则已结缘,isBroad:是否直播中 true为直播中
	 *
	 */
	@Before(value = {JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"columnId"})
	})
	@API(isScran=true)
	public void pageByColumnId(){
		Account account = getAttr("account");
		Integer userId = account.getId();
		Integer columnId = getParaToInt("columnId");
		Integer pageNumber = getParaToInt("pageNumber", 1);
		Integer pageSize = getParaToInt("pageSize",JMConsts.pageSize);
		
		Page<Goods> page = goodsDao.pageForApi(columnId, pageNumber, pageSize, userId);
		
		JMResult.success(this, page,"获取成功");
	}
	
	/**
	 * 
	 * @date 2019年7月3日 20:03:10
	 * @Description: h5商品页面
	 * @reqMethod post
	 * @paramter accountId,int,用户id,r:t
	 * @paramter pageSize,int,每页大小,r:f,d:10
	 * @paramter pageNumber,int,页码,r:f,d:1
	 * @paramter keyword,String,关键字查询,r:f
	 * @paramter labelId,int,标签id精确到二级标签，-1代表没有筛选,r:f,d:-1
	 * @paramter type,int,(1在售宝贝，2已售欣赏),r:f
	 * @paramter sellState,int,0已出售1未出售,r:f
	 * @pDescription code:0失败1成功2未登录,desc:描述,skulist:当前商品所有可用sku列表,speclist:当前商品显示的所有规格列表,serviceType:-2都没有，0包邮， 1七天内退货，2包邮+七天内退货,,isCollected:是否收藏（boolean）,resaleNum:转售数,resalePrice:转售金额,sellState:为0时则已结缘
	 */
	@Before(value = {JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"accountId"})
	})
	@API(isScran=true)
	public void htmlGoods(){
		Integer accountId = getParaToInt("accountId");
		Integer pageNumber = getParaToInt("pageNumber", 1);
		String keyword = getPara("keyword");
		Integer type = getParaToInt("type");
		Integer sellState = getParaToInt("sellState");
		Integer labelId = getParaToInt("labelId", -1);//标签id精确到二级标签,-1代表没有筛选
		String minprice = getPara("minprice", "0");//最小价格
		String maxprice = getPara("maxprice", null);
		Integer pageSize = getParaToInt("pageSize",JMConsts.pageSize);
		Page<GoodsResale> page = goodsResaleService.pageHtmlGoods(pageNumber, pageSize, accountId, keyword, type, minprice, maxprice,
				labelId, sellState);
		JMResult.success(this, page, "获取成功");
	}
	
	/**
	 * 
	 * @date 2019年7月3日 20:03:10
	 * @Description: h5新品商品页面
	 * @reqMethod post
	 * @paramter accountId,int,用户id,r:t
	 * @pDescription code:0失败1成功2未登录,desc:描述,data:如果该用户没有前五天商品则为空，有时就根据返回的day字段从0开始获取信息,skulist:当前商品所有可用sku列表,isCollected:是否收藏（boolean）,resaleNum:转售数,resalePrice:转售金额,sellState:为0时则已结缘
	 */
	@Before(value = {JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"accountId"})
	})
	@API(isScran=true)
	public void htmlNewGoods(){
		Integer accountId = getParaToInt("accountId");
		JMResult.success(this, goodsResaleService.htmlNewGoods(accountId), "获取成功");
	}

	/**
	 * 
	 * @date 2019年7月8日 16:48:32
	 * @Description: h5商品详情
	 * @reqMethod post
	 * @paramter goodsId,int,商品id,r:t,p:1,d:1
	 * @paramter goodsResaleId,int,转售id,r:f
	 * @pDescription code:0失败1成功2未登录,desc:描述,skulist:当前商品所有可用sku列表,speclist:当前商品显示的所有规格列表,ownerList:商品主图集合,detailsList:商品详情图主图,recommendList:推荐商品集合
	 *
	 */
	@Before(value = {JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"goodsId"})
	})
	public void htmlGoodsDetail(){
		int goodsId = getParaToInt("goodsId");
		Integer goodsResaleId = getParaToInt("goodsResaleId", null);
		Goods goods = goodsDao.htmlDetail(goodsId, goodsResaleId);
		if(goods == null){
			JMResult.fail(this,"该商品不存在");
			return ;
		}
		JMResult.success(this,goods,"获取成功");
	}
	/**
	 * 
	 * @date 2019年7月3日 20:03:10
	 * @Description: 用户的转售商品页面
	 * @reqMethod post
	 * @paramter accountId,int,用户id,r:t
	 * @paramter pageSize,int,每页大小,r:f,d:10
	 * @paramter pageNumber,int,页码,r:f,d:1
	 * @paramter keyword,String,关键字查询,r:f
	 * @paramter type,int,(1在售宝贝，2已售欣赏),r:f
	 * @pDescription code:0失败1成功2未登录,desc:描述,skulist:当前商品所有可用sku列表,speclist:当前商品显示的所有规格列表,serviceType:-2都没有，0包邮， 1七天内退货，2包邮+七天内退货,isCollected:是否收藏（boolean）,resaleNum:转售数,resalePrice:转售金额,sellState:为0时则已结缘,goodsResaleId:转售id
	 */
	@Before(value = {JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"accountId"})
	})
	@API(isScran=true)
	public void pageResaleGoods(){
		Integer accountId = getParaToInt("accountId");
		Integer pageNumber = getParaToInt("pageNumber", 1);
		String keyword = getPara("keyword");
		Integer type = getParaToInt("type");
		Integer pageSize = getParaToInt("pageSize",JMConsts.pageSize);
		Page<GoodsResale> page = goodsResaleService.pageMyResaleGoods(pageNumber, pageSize, accountId, keyword, type);
		JMResult.success(this, page, "获取成功");
	}
	
	/**
	 * 
	 * @date 2019年7月3日 20:03:10
	 * @Description: 我的转售商品页面
	 * @reqMethod post
	 * @paramter accountId,int,用户id,r:t
	 * @paramter pageSize,int,每页大小,r:f,d:10
	 * @paramter pageNumber,int,页码,r:f,d:1
	 * @paramter keyword,String,关键字查询,r:f
	 * @paramter type,int,(1在售宝贝，2已售欣赏),r:f
	 * @pDescription code:0失败1成功2未登录,desc:描述,skulist:当前商品所有可用sku列表,speclist:当前商品显示的所有规格列表,serviceType:-2都没有，0包邮， 1七天内退货，2包邮+七天内退货,isCollected:是否收藏（boolean）,resaleNum:转售数,resalePrice:转售金额,sellState:为0时则已结缘,goodsResaleId:转售id
	 */
	@Before(value={JMApiAccountInterceptor.class})
	@API(isScran=true)
	public void pageMyResaleGoods(){
		Account account = getAttr("account");
		Integer pageNumber = getParaToInt("pageNumber", 1);
		String keyword = getPara("keyword");
		Integer type = getParaToInt("type");
		Integer pageSize = getParaToInt("pageSize",JMConsts.pageSize);
		Page<GoodsResale> page = goodsResaleService.pageMyResaleGoods(pageNumber, pageSize, account.getId(), keyword, type);
		JMResult.success(this, page, "获取成功");
	}
	
//	/**
//	 * 
//	 * @date 2018年12月18日 下午6:01:56
//	 * @author JaysonLee
//	 * @Description: 添加商品到购物车
//	 * @reqMethod post
//	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
//	 * @paramter goodsId,int,商品id,r:t,p:1,d:1
//	 * @paramter skuId,int,规格id 可以从skulist获取,r:t,p:4,d:3
//	 * @paramter num,int,商品数量,r:t,p:1,d:1
//	 * @pDescription code:0失败1成功2未登录,desc:描述	
//	 *
//	 */
//	@API(isScran = true)
//	@Before(value={JMApiAccountInterceptor.class})
//	public void addCart(){
//		try {
//			int goodsId = getParaToInt("goodsId");
//			int skuId = getParaToInt("skuId");
//			int num = getParaToInt("num",1);
//			Account account = getAttr("account");
//			renderJson(goodsCartDao.add(account.getId(), goodsId, num, skuId));
//		} catch (Exception e) {
//			e.printStackTrace();
//			JMResult.fail(this,"参数错误或者发生错误");
//		}
//		
//	}
	
	
	/**
	 * 
	 * @date 2019年1月24日 上午11:59:01
	 * @author JaysonLee
	 * @Description: 商品详情
	 * @reqMethod post
	 * @paramter goodsId,int,商品id,r:t,p:1,d:1
	 * @paramter sessionId,String,个人信息唯一标识ID(这个接口可不传，有就传),r:f,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @pDescription code:0失败1成功2未登录,desc:描述,skulist:当前商品所有可用sku列表,speclist:当前商品显示的所有规格列表 ,isCollected:是否收藏（boolean）,ownerList:商品主图集合,detailsList:商品详情图主图,recommendList:推荐商品集合
	 *
	 */
	@Before(value = {JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"goodsId"})
	})
	public void detail(){
		Account account = getAttr("account");
		Integer userId = account.getId();
		int goodsId = getParaToInt("goodsId");
		Goods goods = goodsDao.detail(goodsId,userId);
		if(goods == null){
			JMResult.fail(this,"该商品不存在");
			return ;
		}
		JMResult.success(this,goods,"获取成功");
	}
	
	/**
	 * 
	 * @date 2019年1月24日 上午11:59:01
	 * @Description: 转售商品详情
	 * @reqMethod post
	 * @paramter goodsResaleId,int,转售id,r:t,p:1,d:1
	 * @paramter sessionId,String,个人信息唯一标识ID(这个接口可不传，有就传),r:f,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @pDescription code:0失败1成功2未登录,desc:描述,skulist:当前商品所有可用sku列表,speclist:当前商品显示的所有规格列表 ,isCollected:是否收藏（boolean）,ownerList:商品主图集合,detailsList:商品详情图主图,recommendList:推荐商品集合,imageState:为0时则编辑转售信息时不需要将详情图带入编辑页,否则带入
	 *
	 */
	@Before(value = {JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"goodsResaleId"})
	})
	public void resaleGoodsDetail(){
		String sessionId = getPara("sessionId",null);
		Integer userId = null ;
		if(sessionId != null){
			Account account = accountDao.getBySessionId(sessionId);
			if(account != null){
				userId = account.getId();
			}
		}
		int goodsResaleId = getParaToInt("goodsResaleId");
		goodsDao.resaleGoodsDetail(goodsResaleId,userId).renderJson(this);
	}
	
	
	/**
	 * 
	 * @date 2019年1月24日 下午4:41:55
	 * @author JaysonLee
	 * @Description: 商品分类列表
	 * @reqMethod post
	 * @paramter parentId,int,父id，不传为全部,r:f,p:1,d:1
	 * @pDescription nextList:二级分类列表
	 *
	 */
	public void goodsLabel(){
		Integer parentId = getParaToInt("parentId",null);
		JMResult.success(this,goodsDao.goodsLabel(parentId),"获取成功");
	}

	/**
	 * 
	 * @date 2019年7月15日 16:37:00
	 * @Description: 根据二级分类id获取一级名称加二级名称
	 * @reqMethod post
	 * @paramter labelId,int,二级分类id,r:t,p:1,d:1
	 * @pDescription nextList:二级分类列表
	 *
	 */
	@Before(value = {JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"labelId"})
	})
	public void labelNameById(){
		Integer labelId = getParaToInt("labelId");
		JMResult.success(this,goodsDao.labelNameById(labelId),"获取成功");
	}
	
	/**
	 * 
	 * @date 2019年1月25日 下午2:13:44
	 * @author JaysonLee
	 * @Description: 热门搜索关键词列表
	 * @reqMethod get
	 * @paramter 
	 * @pDescription	
	 *
	 */
	public void hotSearch(){
		JMResult.success(this, keywordDao.list(true), "获取成功");
//		JMResult.success(this,searchHistoryDao.hotList(),"获取成功");
	}
	
	
	/**
	 * 
	 * @date 2019年3月18日 上午11:17:19
	 * @author JaysonLee
	 * @Description: 营销模块列表
	 * @reqMethod get
	 * @paramter
	 * @pDescription	
	 *
	 */
	public void listMarketing() {
		List<Marketing> list = marketingDao.listAll();
		JMResult.success(this,list,"获取成功");
	}
	
	/**
	 * 
	 * @date 2019年3月18日 上午11:59:17
	 * @author JaysonLee
	 * @Description: 根据营销id获取商品分页
	 * @reqMethod post
	 * @paramter marketId,int,营销模块id,r:t,p:1,d:1
	 * @paramter keyword,String,搜索关键词,r:f,p:1,d:1
	 * @paramter pageSize,int,每页大小,r:f,d:10
	 * @paramter pageNumber,int,页码,r:f,d:1
	 * @pDescription	
	 *
	 */
	@Before(value = {JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"marketId"})
	})
	public void pageGoodsByMarketing() {
		int marketId = getParaToInt("marketId");
		String keyword = getPara("keyword","");
		int pageSize = getParaToInt("pageSize",10);
		int pageNumber = getParaToInt("pageNumber",1);
		Page<Goods> page = marketingGoodsDao.pageGoodsByMarketId(marketId, keyword, pageSize, pageNumber);
		JMResult.success(this,page,"获取成功");
	}
	
	
	/**
	 * 
	 * @date 2019年7月2日 16:03:54
	 * @Description: 转售商品
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter goodsId,int,商品id,r:f,p:1,d:1
	 * @paramter describe,String,商品描述,r:f,p:1,d:1
	 * @pDescription	
	 *
	 */
	@Before(value = {JMApiAccountInterceptor.class, JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"goodsId"})
	})
	public void resaleGoods() {
		Account account = getAttr("account");
		Integer goodsId = getParaToInt("goodsId");
		String describe = getPara("describe");
		goodsResaleService.resaleGoods(account.getId(), goodsId, describe).renderJson(this);
	}


	/**
	 * 
	 * @date 2019年7月3日 20:03:10
	 * @Description: 编辑我的转售的商品
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter goodsId,int,商品id,r:f,p:1,d:1
	 * @paramter describe,String,商品描述,r:f,p:1,d:1
	 * @paramter detailImageNames,String,接收详情图图片的参数名称(有多少个文件就用多个逗号分隔),r:f,p:1,d:1
	 * @paramter ?,File,图片名称(接收的所有文件参数名称由传入的数组定义),r:f,p:1,d:1
	 * @paramter detailImageIds,String,未被删除的商品详情图id(多个用逗号隔开),r:f
	 * @pDescription code:0失败1成功2未登录
	 */
	@Before(value = {JMApiAccountInterceptor.class, JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"goodsId"})
	})
	@API(isScran=true)
	public void updateMyResaleGoods(){
		Account account = getAttr("account");
		String[] detailImageNames = UploadUtil.changeImages(getPara("detailImageNames"));
		Integer goodsId = getParaToInt("goodsId");
		String[] detailImageIds = UploadUtil.changeImages(getPara("detailImageIds"));
		String describe = getPara("describe");
		ArrayList<String> detailImageList = UploadUtil.uploadImgsByNames(this, "/goods",detailImageNames);
		goodsResaleService.updateMyResaleGoods(account.getId(), detailImageList, goodsId, describe, detailImageIds).renderJson(this);
	}
	
	/**
	 * 
	 * @date 2019年7月2日 16:03:54
	 * @Description: 删除转售商品
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter goodsId,int,商品id,r:f,p:1,d:1
	 * @pDescription 
	 *
	 */
	@Before(value = {JMApiAccountInterceptor.class, JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"goodsId"})
	})
	public void deleteResaleGoods() {
		Account account = getAttr("account");
		Integer goodsId = getParaToInt("goodsId");
		goodsResaleService.deleteResaleGoods(account.getId(), goodsId).renderJson(this);
	}

	/**
	 * @date 2019年7月4日 15:05:07
	 * @Description: 获取新增商品要的参数列表
	 * @reqMethod post
	 * @pDescription 
	 */
	public void listSpec(){
//		Integer shopModId = getParaToInt("shopModId");
		JMResult.success(this,specDao.newList(),"获取成功");
	}
	
	/**
	 * @date 2019年7月4日 15:05:07
	 * @Description: 获取商品对应的规格列表
	 * @paramter goodsId,int,商品id,r:t
	 * @reqMethod post
	 * @pDescription 
	 */
	@Before(value = {JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"goodsId"})
	})
	public void listSkuByGoodsId(){
		Integer goodsId = getParaToInt("goodsId");
		JMResult.success(this, goodsService.listSkuDetail(goodsId), "获取成功");
	}
	
	/**
	 * @date 2019年7月4日 15:05:07
	 * @Description: 新增商品
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter detailImageNames,String,接收详情图图片的参数名称(有多少个文件就多个逗号分隔),r:t,p:1,d:1
	 * @paramter ?,File,图片名称(接收的所有文件参数名称由传入的数组定义),r:t,p:1,d:1
	 * @paramter video,File,视频,r:f,p:1,d:1
	 * @paramter goods.labelId,int,二级类别id,r:t,p:1,d:1
	 * @paramter goods.name,String,商品标题,r:f,p:1,d:1
	 * @paramter goods.artNo,String,商品货号,r:t,p:1,d:1
	 * @paramter goods.price,String,商品价格,r:f,p:1,d:1
	 * @paramter skuNum,int,返回的参数列表个数,r:f
	 * @paramter attrids?,int,?为递增个数(如果返回的参数列表有三个，则有attrids0，attrids1，attrids2)，attrids为返回的参数列表的atts中的第一个id,r:f
	 * @paramter name?,String,?为递增个数(如果返回的参数列表有三个，则有name0，name1，name2)，name为填写的参数对应的信息,r:f
	 * @pDescription 
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"goods.artNo"})
	})
	public void saveGoods() {
		Account account = getAttr("account");
		String video = UploadUtil.uploadVideo(this, "video", "/video");
		String[] detailImageNames = UploadUtil.changeImages(getPara("detailImageNames"));
		if(detailImageNames == null || detailImageNames.length == 0) {
			JMResult.fail(this, "图片至少上传一张");
			return;
		}
		ArrayList<String> detailImageList = UploadUtil.uploadImgsByNames(this, "/goods",detailImageNames);
		int skuNum = getParaToInt("skuNum",0);
		Goods goods = getBean(Goods.class);
		
		goodsService.apiSaveGoods(this, video, skuNum, goods, account, detailImageList).renderJson(this);
	}

	/**
	 * 
	 * @date 2019年7月4日 15:05:07
	 * @Description: 更新商品
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter detailImageNames,String,接收详情图图片的参数名称(有多少个文件就用逗号拼接),r:f,p:1,d:1
	 * @paramter ?,File,图片名称(接收的所有文件参数名称由传入的数组定义),r:f,p:1,d:1
	 * @paramter video,File,视频,r:f,p:1,d:1
	 * @paramter goods.id,int,商品id,r:t
	 * @paramter goods.labelId,int,二级类别id,r:f,p:1,d:1
	 * @paramter goods.name,String,商品标题,r:f,p:1,d:1
	 * @paramter goods.artNo,String,商品货号,r:t,p:1,d:1
	 * @paramter goods.price,String,商品价格,r:f,p:1,d:1
	 * @paramter detailImageIds,String,未被删除的商品详情图id(存在多张时用逗号拼接传入),r:f
	 * @paramter nowSkuNum,int,新商品列表的个数,r:f
	 * @paramter oldSkuNum,int,旧参数列表的个数,r:f
	 * @paramter attrids?,int,?为递增个数(如果返回的参数列表有三个，则有attrids0，attrids1，attrids2)，attrids为返回的参数列表的atts中的第一个id,r:f
	 * @paramter nowName?,String,?为递增个数(如果返回的参数列表有三个，则有nowName0，nowName1，nowName2)，nowName为新的的参数列表对应的信息,r:f
	 * @paramter name?,String,?为递增个数(如果返回的参数列表有三个，则有name0，name1，name2)，name为旧规格填框填写的信息,r:f
	 * @paramter skuId?,String,?为递增个数(如果返回的参数列表有三个，则有skuId0，skuId1，skuId2)，skuId为商品信息的id,r:f
	 * @pDescription 
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"goods.id", "goods.artNo"})
	})
	public void updateGoods() {
		Account account = getAttr("account");
		String[] detailImageNames = UploadUtil.changeImages(getPara("detailImageNames"));
		String[] detailImageIds = UploadUtil.changeImages(getPara("detailImageIds"));
		Integer nowSkuNum = getParaToInt("nowSkuNum", 0);
		Integer oldSkuNum = getParaToInt("oldSkuNum",0);
		String video = UploadUtil.uploadVideo(this, "video", "/video");
		ArrayList<String> detailImageList = UploadUtil.uploadImgsByNames(this, "/goods",detailImageNames);
		Goods goods = getBean(Goods.class);
		goodsService.apiUpdateGoods(this, video, nowSkuNum, oldSkuNum, goods, account, detailImageIds, detailImageList).renderJson(this);
	}

	/**
	 * @date 2019年7月4日 15:05:07
	 * @Description: 删除商品
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter goodsId,int,商品id,r:t,p:1,d:1
	 * @pDescription 
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"goodsId"})
	})
	public void delteGoods() {
		Account account = getAttr("account");
		Integer goodsId = getParaToInt("goodsId");
		goodsService.delteGoods(goodsId, account).renderJson(this);
	}

	/**
	 * @date 2019年7月4日 15:05:07
	 * @Description: 将商品设置为出售或者未出售
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter goodsId,int,商品id,r:t,p:1,d:1
	 * @pDescription 
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"goodsId"})
	})
	public void setGoodsState() {
		Account account = getAttr("account");
		Integer goodsId = getParaToInt("goodsId");
		goodsService.setGoodsState(goodsId, account).renderJson(this);
	}
	
}