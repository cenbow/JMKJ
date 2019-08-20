package com.cn.jm.controller.webcast.api;

import com.aliyuncs.exceptions.ClientException;
import com.cn._gen.model.Account;
import com.cn._gen.model.AssociateRequest;
import com.cn._gen.model.Room;
import com.cn._gen.model.RoomRecord;
import com.cn.jm._dao.account.AccountEnum;
import com.cn.jm.core.JMMessage;
import com.cn.jm.core.exception.BusinessException;
import com.cn.jm.core.interceptor.JMVaildInterceptor;
import com.cn.jm.core.utils.util.ConverUtils;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.valid.annotation.JMRuleVaild;
import com.cn.jm.core.valid.annotation.JMRulesVaild;
import com.cn.jm.core.web.controller.JMBaseApiController;
import com.cn.jm.information.ZbMessage;
import com.cn.jm.interceptor.JMApiAccountInterceptor;
import com.cn.jm.service.JMRoomMerchantService;
import com.cn.jm.service.JMRoomService;
import com.cn.jm.service.JMZanService;
import com.cn.jm.util.JMResultUtil;
import com.cn.jm.util.UploadUtil;
import com.cn.jm.web.core.parse.annotation.API;
import com.cn.jm.web.core.parse.annotation.ParseOrder;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Page;
/**
 * 
 *
 * @date 
 * @author 
 * @Description: 直播间主播模块
 *
 */
@ParseOrder(40)
@API
@JMRouterMapping(url = "/api/room")
public class ApiRoomController extends JMBaseApiController{
	
	@Inject
	public JMRoomService roomService;
	
	@Inject
	public JMRoomMerchantService merchantService;
	
	@Inject
	JMZanService zanService;
	
	
	/**
	 * 
	 * @author cyl
	 * @throws ClientException 
	 * @date 2019年6月29日 上午10:57:10
	 * @Description:我要开播
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @pDescription code:为4时可以创建直播间
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class})
	public void startBroadcasting() {
		Account account = getAttr("account");
		if(account.getType()!=AccountEnum.TYPE_ANCHOR.getValue()) {
			JMResult.fail(this, ZbMessage.NOT_ANCHOR);
			return;
		}
		boolean checkIsBindMerchant = roomService.checkIsBindMerchant(account.getId());
		if(!checkIsBindMerchant) {
			JMResult.fail(this, ZbMessage.NOT_BIND_MERCHANT);
			return;
		}
//		BroadcastRequest broadcastRequest = roomService.selectBroadcastRequest(account.getId());
//		if(broadcastRequest==null||broadcastRequest.getState()==RequerstEnum.DISAGREE.getValue()) {
//			try {
//				roomService.insertBroadcastRequest(account).renderJson(this);
//			} catch (ClientException e) {
//				e.printStackTrace();
//			}
//			return;
//		}
//		if(broadcastRequest.getState()==RequerstEnum.REQUEST.getValue()) {
//			JMResult.fail(this, ZbMessage.WAITING_MERCHANT_PERMIT);
//			return;
//		}
		JMResultUtil.canShow().renderJson(this);
		
	}
	
	
	/**
	 * 
	 * @date 
	 * @author 
	 * @Description: 创建或更新直播间并开播
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter image,file,直播间封面
	 * @paramter name,string,直播间名称
	 * @paramter desc,string,直播间简介
	 * @paramter address,int,直播间地址
	 * @paramter roomIntroduction,string,直播间介绍
	 * @paramter anchorIntroduction,string,主播介绍
	 * @paramter announcement,string,直播间公告
	 * @paramter ids,string,直播间栏目Id多个用逗号拼接
	 * @pDescription playUrl:播放流地址,pushUrl:推流地址
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class})
	public void createRoom() {
		String image = UploadUtil.uploadOSSImg(this, "image", "room");
		Account account = getAttr("account");
		if(account.getType()!=AccountEnum.TYPE_ANCHOR.getValue()) {
			JMResult.fail(this, ZbMessage.NOT_ANCHOR);
			return;
		}
		Room room = ConverUtils.fullBean(Room.class, getRequest());
		String ids = getPara("ids");
		if(image!=null) {
			room.setImage(image);
		}
		room.setAccountId(account.getId());
		roomService.createRoom(room,account.getId(),ids).renderJson(this);
	}
	
	
	/**
	 * 
	 * @date 
	 * @author 
	 * @Description:查看我的直播间详情
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @pDescription image:直播间封面,name:直播间名称,desc:简介,address:直播间地址,roomIntroduction:直播间介绍,
	 * anchorIntroduction:主播介绍,announcement:公告,roomNumber:直播间编号,state:0直播中1冻结2离线,nick:主播昵称,head:主播头像 
	 */
	@API(isScran = true)
	@Before({JMApiAccountInterceptor.class})
	public void selectMyRoomDetail() {
		Account account = getAttr("account");
		Room room = roomService.selectRoom(account);
		JMResult.success(this, room ,JMMessage.SUCCESS);
	}
	
	
	
	/**
	 * 
	 * @date 
	 * @author 
	 * @throws BusinessException 
	 * @Description:获取直播间详情(进入直播间)
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter roomId,int,直播间Id
	 * @pDescription image:直播间封面,name:直播间名称,desc:简介,address:直播间地址,roomIntroduction:直播间介绍,
	 * anchorIntroduction:主播介绍,announcement:公告,roomNumber:直播间编号,state:0直播中1冻结2离线,watchNumber:观看人数,
	 * playUrl:播放流地址,pushUrl:推流地址
	 */
	@API(isScran = true)
	@Before({JMVaildInterceptor.class,JMApiAccountInterceptor.class})
	@JMRuleVaild(fields = { "roomId"})
	public void selectRoomDetail() {
		Integer roomId = getParaToInt("roomId");
		Account account = getAttr("account");
		try {
			roomService.selectRoomDetail(roomId,account).renderJson(this);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @date 
	 * @author 
	 * @throws BusinessException 
	 * @Description:离开直播间
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter roomId,int,直播间Id
	 *
	 */
	@API(isScran = true)
	@Before({JMVaildInterceptor.class,JMApiAccountInterceptor.class})
	@JMRuleVaild(fields = { "roomId"})
	public void leaveRoom() {
		Integer roomId = getParaToInt("roomId");
		Account account = getAttr("account");
		try {
			roomService.leaveRoom(roomId,account).renderJson(this);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @date 
	 * @author 
	 * @Description:首页直播间列表
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter labelId,int,栏目Id
	 * @paramter isRecommend,int,0不精选1精选
	 * @pDescription image:直播间封面,name:直播间名称,desc:简介,address:直播间地址,roomIntroduction:直播间介绍,
	 * anchorIntroduction:主播介绍,announcement:公告,roomNumber:直播间编号,state:0直播中1冻结2离线,watchNumber:观看人数
	 */
	@API(isScran = true)
	@Before({JMVaildInterceptor.class,JMApiAccountInterceptor.class})
	public void selectRoomPage() {
		Integer pageNumber = getParaToInt("pageNumber",1);
		Integer pageSize = getParaToInt("pageSize",10);
		Integer labelId = getParaToInt("labelId");
		Integer isRecommend = getParaToInt("isRecommend",0);
		String keyword = getPara("keyword");
		Page<Room> page = roomService.selectRoomPage(pageNumber, pageSize, labelId, isRecommend, keyword);
		JMResult.success(this, page, JMMessage.SUCCESS);
	}
	
	
	/**
	 * 
	 * @author cyl
	 * @date 2019年6月29日 下午6:48:04
	 * @Description:查看下线之前是否有商家发送关联请求
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class})
	public void selectCurrentAssociateRequest() {
		Account account = getAttr("account");
		AssociateRequest associateRequest = merchantService.selectCurrentAssociateRequest(account);
		JMResult.success(this, associateRequest, JMMessage.SUCCESS);
	}
	
	/**
	 * 
	 * @author cyl
	 * @date 2019年6月29日 下午6:48:04
	 * @Description:主播同意或拒绝商家发送关联请求
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter id,int,关联请求的id 
	 * @paramter state,int,状态1同意2不同意 
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class})
	public void editCurrentAssociateRequest() {
		Account account = getAttr("account");
		Integer id = getParaToInt("id");
		Integer state =getParaToInt("state");
		roomService.editAssociateRequest(id, state, account).renderJson(this);
	}
	
	
	/**
	 * 
	 * @author cyl
	 * @date 2019年7月4日 下午2:46:50
	 * @Description:查看我的房管
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @pDescription nick:昵称,head:头像
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class})
	public void selectMyManage() {
		Account account = getAttr("account");
		roomService.selectMyManage(account).renderJson(this);
	}
	
	
	/**
	 * 
	 * @author cyl
	 * @date 2019年7月4日 下午2:46:50
	 * @Description:查看我的商家
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @pDescription nick:昵称,head:头像
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class})
	public void selectMyMerchant() {
		Account account = getAttr("account");
		roomService.selectMyMerchant(account).renderJson(this);
	}
	
	/**
	 * 
	 * @author cyl
	 * @date 2019年7月4日 下午2:46:50
	 * @Description:查询我关注的直播间
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @pDescription image:直播间封面,name:直播间名称,desc:简介,address:直播间地址,roomIntroduction:直播间介绍,
	 * anchorIntroduction:主播介绍,announcement:公告,roomNumber:直播间编号,state:0直播中1冻结2离线,watchNumber:观看人数
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class})
	public void selectMyFollowRoom() {
		Account account = getAttr("account");
		Integer pageNumber = getParaToInt("pageNumber",1);
		Integer pageSize = getParaToInt("pageSize",10);
		Page<Room> page = roomService.selectMyFollowRoom(account, pageNumber, pageSize);
		JMResult.success(this, page, JMMessage.SUCCESS);
	}
	
	/**
	 * 
	 * @author cyl
	 * @date 2019年7月4日 下午2:46:50
	 * @Description:查询直播间状态
	 * @reqMethod post
	 * @paramter roomId,int,直播间Id
	 * @pDescription state:0直播中1冻结2离线
	 */
	@API(isScran = true)
	public void selectRoomState() {
		Integer roomId = getParaToInt("roomId");
		Room room = roomService.selectById(roomId);
		room.keep("state");
		JMResult.success(this, room, JMMessage.SUCCESS);
	}
	
//	/**
//	 * 
//	 * @date 2019年7月12日 10:10:01
//	 * @Description: 点赞直播间或取消点赞直播间
//	 * @reqMethod post
//	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
//	 * @paramter roomId,int,直播间id,r:t,p:1,d:1
//	 * @pDescription	
//	 *
//	 */
//	@API(isScran = true)
//	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
//	@JMRulesVaild({
//		@JMRuleVaild(fields={"roomId"})
//	})
//	public void zan() {
//		Account account = getAttr("account");
//		Integer roomId = getParaToInt("roomId");
//		zanService.zan(roomId, account.getId(), ZanEnum.ROOM_TYPE);
//		JMResultUtil.success().renderJson(this);
//	}
//	
	/**
	 * 
	 * @date 2019年7月12日 10:10:01
	 * @Description: 点赞直播间
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter roomId,int,直播间id,r:t,p:1,d:1
	 * @pDescription	
	 *
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class,JMVaildInterceptor.class})
	@JMRulesVaild({
		@JMRuleVaild(fields={"roomId"})
	})
	public void zan() {
		Integer roomId = getParaToInt("roomId");
		roomService.addZan(roomId);
		JMResult.success(this, JMMessage.SUCCESS);
	}
	
	/**
	 * 
	 * @author cyl
	 * @date 2019年7月22日 下午5:49:56
	 * @Description:获取播放连接
	 * @reqMethod post
	 * @paramter roomId,int,直播间id,r:t,p:1,d:1
	 */
	public void selectPlayUrl() {
		Integer roomId = getParaToInt("roomId");
		RoomRecord roomRecord = roomService.selectLiveRoomRecord(roomId);
		JMResult.success(this, roomRecord, JMMessage.SUCCESS);
	}
	
	
	
//	
//	public void sendTest() {
//		HashMap<String,Object> params = new HashMap<>();
//		params.put("roomNumber", "111");
//		JMTio.send(7, JMResult.success(params));
//		HashMap<String,Object> result = new HashMap<>();
//		result.put("orderNo","G20190711164148968403273");
// 		JMTio.send(7,JMResultUtil.getJMResult(JMResultUtil.SEND_ORDER, 
// 				result, PromptInformationEnum.SEND_ORDER));
//		JMResult.success(this, JMMessage.SUCCESS);
//	}

}
