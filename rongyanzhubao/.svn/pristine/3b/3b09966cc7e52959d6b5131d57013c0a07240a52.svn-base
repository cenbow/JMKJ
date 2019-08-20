
package com.cn.jm.controller.shop.api;

import com.cn._gen.model.Account;
import com.cn._gen.model.Address;
import com.cn.jm._dao.account.JMAccountDao;
import com.cn.jm._dao.address.JMAddressDao;
import com.cn.jm._dao.goods.JMGoodsCartDao;
import com.cn.jm.core.interceptor.JMVaildInterceptor;
import com.cn.jm.core.utils.util.ConverUtils;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.valid.annotation.JMRuleVaild;
import com.cn.jm.core.valid.annotation.JMRulesVaild;
import com.cn.jm.core.valid.rule.JMVaildLength;
import com.cn.jm.interceptor.JMApiAccountInterceptor;
import com.cn.jm.web.core.parse.annotation.API;
import com.cn.jm.web.core.parse.annotation.ParseOrder;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;

/**
 * 
 *
 * @date 2018年12月20日 上午11:18:32
 * @author Administrator
 * @Description: 用户地址模块
 *
 */
@API
@ParseOrder( 1)
@JMRouterMapping(url = ApiAddressController.url)
public class ApiAddressController extends Controller {
	
	public static final String url ="/api/address";
	@Inject
	public JMAddressDao addressDao;
	
	@Inject
	public JMGoodsCartDao cartdao;
	
	@Inject
	public JMAccountDao accountDao;
	
//	@Inject
//	JMSystemFreightService freightService ;
	
	/**
	 * 
	 * @date 2018年12月20日 上午11:40:03
	 * @author JaysonLee
	 * @Description: 新增用户地址
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter name,String,收货人姓名,r:t,p:JaysonLee,d:JaysonLee
	 * @paramter mobile,String,收货人电话,r:t,p:13423423123,d:13423423123
	 * @paramter code,String,收货邮编,r:f,p:134231,d:134233
	 * @paramter sheng,String,收货地址省名,r:t,p:广东省
	 * @paramter shi,String,收货地址市名,r:t,p:广州市
	 * @paramter qu,String,收货地址区名,r:t,p:海珠区
	 * @paramter address,String,详细收货地址（不含省市区）,r:t,p:大家庭花园南座101
	 * @paramter isChoice,int,是否默认地址(0否1是),r:t,p:0,d:0
	 * @paramter areaId,int,区id（加多的参数）,r:t,p:1,d:1
	 * @pDescription code:0失败1成功2未登录,desc:描述
	 *
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"name","sheng","shi","qu","address","isChoice","areaId"}),
		@JMRuleVaild(fields={"mobile"},ruleClass = JMVaildLength.class,maxlength = {"11"},minlength = {"11"})
	})
	public void add(){
		Account account = getAttr("account");
		Address address = ConverUtils.fullBean(Address.class, getRequest());
		renderJson(addressDao.add(address, account.getId()));
	}
	
	
	
	/**
	 * 
	 * @date 2018年12月20日 下午3:07:56
	 * @author JaysonLee
	 * @Description: 修改地址
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter id,int,收货地址id,r:t,p:1,d:1
	 * @paramter name,String,收货人姓名,r:f,p:Jayson,d:Jayson
	 * @paramter mobile,String,收货人电话,r:f,p:13423423123,d:13423423123
	 * @paramter code,String,收货邮编,r:f,p:000000,d:000000
	 * @paramter sheng,String,收货地址省名,r:f,p:广东省
	 * @paramter shi,String,收货地址市名,r:f,p:深圳市
	 * @paramter qu,String,收货地址区名,r:f,p:宝安区
	 * @paramter address,String,详细收货地址（不含省市区）,r:f,p:大家庭花园南座401
	 * @paramter isChoice,int,是否默认地址(0否1是),r:t,p:1,d:0
	 * @paramter areaId,int,区id（加多的参数）,r:t,p:1,d:1
	 * @pDescription code:0失败1成功2未登录,desc:描述	
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	public void edit(){
		Account account = getAttr("account");
		Address address = ConverUtils.fullBean(Address.class, getRequest());
		renderJson(addressDao.edit(address, account.getId()));
	}
	
	/**
	 * 
	 * @date 2018年12月20日 上午11:57:23
	 * @author JaysonLee
	 * @Description: 用户地址列表
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @pDescription code:0失败1成功2未登录,desc:描述	
	 *
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class})
	public void list(){
		Account account = getAttr("account");
		JMResult.success(this,addressDao.listByUser(account.getId()),"获取成功");
	}
	
	/**
	 * 
	 * @date 2018年12月20日 上午11:57:23
	 * @author JaysonLee
	 * @Description: 获取用户默认地址
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @pDescription code:0失败1成功2未登录,desc:描述	
	 *
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class})
	public void defaultAddress(){
		Account account = getAttr("account");
		JMResult.success(this,addressDao.getDefault(account.getId()),"获取成功");
	}
	
	/**
	 * 
	 * @date 2018年12月20日 下午3:32:56
	 * @author JaysonLee
	 * @Description: 删除收货地址
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter id,int,收货地址id,r:t,p:2,d:2
	 * @pDescription code:0失败1成功2未登录,desc:描述
	 *
	 */
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"id"})
	})
	public void del(){
		Account account = getAttr("account");
		int id = getParaToInt("id");
		renderJson(addressDao.del(account.getId(), id));
	}
	
	
//	/**
//	 * 
//	 * @date 2018年12月18日 下午3:30:22
//	 * @author lgk
//	 * @Description:计算邮费
//	 * @reqMethod json
//	 * @paramter addressId,string,用户收货地址,r:t,p:5,d:5
//	 * @paramter skuList.num,int,数量
//	 * @paramter skuList.skuId,string,规格商品ID
//	 * @pDescription 
//	 * @json {"sessionId":"2e40f29dbed24774a3326146af3212cb","addressId":"4","skuList":[{"num":1,"skuId":5},{"num":2,"skuId":9}]}
//	 *
//	 */
//	@ParseOrder(2)
//	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
//	public void calculationFreight() {
//		JSONObject obj = getAttr("obj");
//		if(obj == null) {
//			JMResult.fail(this,"没有json数据");
//			return ;
//		}
//		Integer addressId = obj.getInteger("addressId");
//		Account account = getAttr("account");
//		JSONArray array = obj.getJSONArray("skuList");
//		List<GoodsCart> goodsCards = array.toJavaList(GoodsCart.class);
//		if (CollectionsUtils.isEmpty(goodsCards)) {
//			JMResult.fail(this,"请选择商品");
//			return ;
//		}
//		freightService.calculationFreight(addressId, goodsCards, account.getId())
//				.renderJson(this);
//	}

	
}