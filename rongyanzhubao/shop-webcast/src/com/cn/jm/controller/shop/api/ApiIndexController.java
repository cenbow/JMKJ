
package com.cn.jm.controller.shop.api;

import java.util.HashMap;

import com.cn.jm._dao.content.JMContentDao;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.web.controller.JMBaseApiController;
import com.cn.jm.web.core.parse.annotation.API;
import com.cn.jm.web.core.parse.annotation.ParseOrder;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Inject;

/**
 * 
 *
 * @date 2019年1月7日 下午3:31:04
 * @author Administrator
 * @Description: 其他模块
 *
 */
@JMRouterMapping(url = ApiIndexController.url)
@ParseOrder(7)
@API
public class ApiIndexController extends JMBaseApiController {
	
	public static final String url ="/api/shop/index";
	
//	@Inject
//	public JMAdvertDao advertDao;
	@Inject
	public JMContentDao conetentDao ;
	
	
//	/**
//	 * 
//	 * @date 2019年1月8日 下午6:52:04
//	 * @author JaysonLee
//	 * @Description: 首页轮播
//	 * @reqMethod post
//	 * @paramter position,int,位置类型0启动页 1首页轮播图,r:f,p:1,d:1
//	 * @pDescription	
//	 *
//	 */
//	@Before(value = {JMVaildInterceptor.class})
//	public void ads(){
//		int position = getParaToInt("position",0);//0启动页 1首页轮播图
//		HashMap<String, Object> param = new HashMap<>();
//		param.put("state", 0);
//		param.put("position", position);
//		List<Advert> advertList = advertDao.list("", param, "sort", JMBaseDao.DESC, true);
//		JMResult.success(this, advertList, "获取成功");
//	}
	
	/**
	 * 
	 * @date 2019年3月19日 下午3:34:36
	 * @author JaysonLee
	 * @Description: 首页获取营销模块展示图
	 * @reqMethod get
	 * @paramter
	 * @pDescription	
	 *
	 */
	public void marketImg() {
		HashMap<String,Object> dataHashMap = new HashMap<String, Object>();
		dataHashMap.put("img1",conetentDao.getById(6,false));
		dataHashMap.put("img2",conetentDao.getById(7,false));
		JMResult.success(this, dataHashMap, "获取成功");
	}
	
}