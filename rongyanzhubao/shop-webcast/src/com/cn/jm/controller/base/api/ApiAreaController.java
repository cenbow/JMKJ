
package com.cn.jm.controller.base.api;

import java.util.List;

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
 * @Description: 地区数据模块
 *
 */
@JMRouterMapping(url = ApiAreaController.url)
@API
public class ApiAreaController extends JMBaseApiController {
	
	public static final String url ="/api/area";
	@Inject
	public JMAreaDao areaDao;
	
	
	/**
	 * 
	 * @date 2019年1月28日 上午10:18:40
	 * @author JaysonLee
	 * @Description: 获取所有区域
	 * @reqMethod post
	 * @paramter
	 * @pDescription	
	 *
	 */
	public void getAll() {
		List<Area> areaList = areaDao.getAllList();
		JMResult.success(this, areaList, "获取成功");
	}
	
	
	/**
	 * 
	 * @date 2019年1月28日 上午10:19:14
	 * @author JaysonLee
	 * @Description: 省区域列表
	 * @reqMethod post
	 * @paramter
	 * @pDescription	
	 *
	 */
	public void getByShengList() {
		List<Area> areaList = areaDao.getByShengList();
		JMResult.success(this, areaList, "获取成功");
	}
	
	
	
	/**
	 * 
	 * @date 2019年1月28日 上午10:19:29
	 * @author JaysonLee
	 * @Description: 市区域列表
	 * @reqMethod post
	 * @paramter parentId,int,省id,r:t,p:110000,d:110000
	 * @pDescription	
	 *
	 */
	public void getByShiList() {
		Integer parentId = getParaToInt("parentId");
		List<Area> areaList = areaDao.getByShiList(parentId);
		JMResult.success(this, areaList, "获取成功");
	}
	
	
	/**
	 * 
	 * @date 2019年1月28日 上午10:19:39
	 * @author JaysonLee
	 * @Description: 区区域列表
	 * @reqMethod post
	 * @paramter parentId,int,市id,r:t,p:110100,d:110100
	 * @pDescription	
	 *
	 */
	public void getByQuList() {
		Integer parentId = getParaToInt("parentId");
		List<Area> areaList = areaDao.getByQuList(parentId);
		JMResult.success(this, areaList, "获取成功");
	}
	
}