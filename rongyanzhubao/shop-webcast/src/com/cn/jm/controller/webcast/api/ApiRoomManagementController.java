package com.cn.jm.controller.webcast.api;

import java.util.ArrayList;
import java.util.Date;

import com.cn._gen.model.Account;
import com.cn._gen.model.Goods;
import com.cn._gen.model.Room;
import com.cn.jm._dao.goods.GoodsEnum;
import com.cn.jm.core.JMMessage;
import com.cn.jm.core.utils.util.ConverUtils;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.web.controller.JMBaseApiController;
import com.cn.jm.interceptor.JMApiAccountInterceptor;
import com.cn.jm.service.JMRoomManagementService;
import com.cn.jm.util.UploadUtil;
import com.cn.jm.web.core.parse.annotation.API;
import com.cn.jm.web.core.parse.annotation.ParseOrder;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
/**
 * 
 *
 * @date 
 * @author 
 * @Description: 直播房管模块
 *
 */
@ParseOrder(43)
@API
@JMRouterMapping(url = "/api/room/management")
public class ApiRoomManagementController extends JMBaseApiController{
	@Inject
	public JMRoomManagementService managementService;
	
	/**
	 * 
	 * @author cyl
	 * @throws Exception 
	 * @date 2019年7月3日 下午4:01:37
	 * @Description:发送直播订单并创建商品
	 * @reqMethod post
	 * @paramter detailImageNames,String,接收详情图图片的参数名称(有多少个文件就用逗号拼接),r:f,p:1,d:1
	 * @paramter ?,File,图片名称(接收的所有文件参数名称由传入的数组定义),r:f,p:1,d:1
	 * @paramter goodsImageNames,String,接收详情图图片的参数名称(有多少个文件就用逗号拼接),r:f,p:1,d:1
	 * @paramter ?,File,图片名称(接收的所有文件参数名称由传入的数组定义),r:f,p:1,d:1
	 * @paramter toAccountId,Integer,发送给用户的Id,r:f,p:1,d:1
	 * @paramter name,string,商品标题,r:f,p:1,d:1
	 * @paramter artNo,string,货号,r:f,p:1,d:1
	 * @paramter price,double,商品价格,r:f,p:1,d:1
	 * @paramter roomId,Integer,当前直播间的Id,r:f,p:1,d:1 
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class})
	public void sendOrderToAccount(){
		Account account = getAttr("account");
		String[] detailImageNames = UploadUtil.changeImages(getPara("detailImageNames"));
		String[] goodsImageNames = UploadUtil.changeImages(getPara("goodsImageNames"));
		ArrayList<String> detailImageList = UploadUtil.uploadImgsByNames(this, "/goods",detailImageNames);
		ArrayList<String> goodsImageList = UploadUtil.uploadImgsByNames(this, "/goods",goodsImageNames);		
		Goods goods = ConverUtils.fullBean(Goods.class,getRequest());
		goods.setCreateTime(new Date());
		goods.setUserId(account.getId());
		goods.setType(GoodsEnum.WEBCAST_GOODS_TYPE.getCode());
		Integer toAccountId = getParaToInt("toAccountId");
		try {
			managementService.sendOrderToAccount(account,toAccountId,detailImageList,
					goodsImageList,goods).renderJson(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @author cyl
	 * @date 2019年7月8日 下午3:24:13
	 * @Description:房管查看我绑定的主播房间
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:d:null
	 * @pDescription image:直播间封面,name:直播间名称,desc:简介,address:直播间地址,roomIntroduction:直播间介绍,
	 * anchorIntroduction:主播介绍,announcement:公告,roomNumber:直播间编号,state:0直播中1冻结2离线,nick:主播昵称,head:主播头像 
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class})
	public void selectMyAnchorRoom() {
		Account account = getAttr("account");
		Room room = managementService.selectMyAnchorRoom(account);
		JMResult.success(this, room, JMMessage.SUCCESS);
		
	}
}
