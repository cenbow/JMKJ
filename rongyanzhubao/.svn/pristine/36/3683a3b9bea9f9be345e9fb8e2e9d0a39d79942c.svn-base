package com.cn.jm.controller.webcast.api;


import com.aliyuncs.exceptions.ClientException;
import com.cn._gen.model.Account;
import com.cn._gen.model.BroadcastRequest;
import com.cn._gen.model.Room;
import com.cn.jm.core.JMMessage;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.core.web.controller.JMBaseApiController;
import com.cn.jm.information.ZbMessage;
import com.cn.jm.interceptor.JMApiAccountInterceptor;
import com.cn.jm.service.JMRoomMerchantService;
import com.cn.jm.service.JMRoomService;
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
 * @Description: 直播商家模块
 *
 */
@ParseOrder(41)
@API
@JMRouterMapping(url = "/api/room/merchant")
public class ApiRoomMerchantController extends JMBaseApiController{
	
	@Inject
	public JMRoomService roomService;
	
	@Inject
	public JMRoomMerchantService roomMerchantService;
	
	
	/**
	 * 
	 * @author cyl
	 * @throws ClientException 
	 * @date 2019年6月29日 下午6:00:38
	 * @Description:审核开播请求
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter id,int,开播请求Id
	 * @paramter state,int,状态1同意2不同意  
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class})
	public void editRequest() throws ClientException {
		Integer id = getParaToInt("id");
		Integer state = getParaToInt("state");
		boolean flag = roomMerchantService.editRequest(id, state);
		if(flag) {
			JMResult.success(this,JMMessage.SUCCESS);
		}
	}
	
	
	/**
	 * 
	 * @author cyl
	 * @date 2019年6月29日 下午6:47:59
	 * @Description:设置房管
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter selectAccountId,int,选择的房管用户Id
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class})
	public void setUpRoomManagement() {
		//商家
		Account account = getAttr("account");
		Integer selectAccountId = getParaToInt("selectAccountId");
		roomService.setUpRoomManagement(account.getId(), selectAccountId).renderJson(this);
	}
	
	
	/**
	 * 
	 * @author cyl
	 * @date 2019年6月29日 下午6:48:04
	 * @Description:发送请求关联给主播
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 * @paramter anchorAccountId,int,选择的主播用户Id
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class})
	public void sendAnchor() {
		//商家
		Account account = getAttr("account");
		Integer anchorAccountId = getParaToInt("anchorAccountId");
		Room room = roomService.selectRoomByAccountId(anchorAccountId);
		if(room==null) {
			JMResult.fail(this,ZbMessage.ROOM_NOT_EXITS);
			return;
		}
		boolean flag = roomMerchantService.insertAssociateRequest(account, anchorAccountId);
		if(flag) {
			JMResult.success(this, JMMessage.SUCCESS);
		}else {
			JMResult.fail(this, JMMessage.FAIL);
		}
	}
	
	/**
	 * 
	 * @author cyl
	 * @date 2019年6月29日 下午6:48:04
	 * @Description:查看下线之前是否有主播发送开播请求
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class})
	public void selectCurrentRequest() {
		Account account = getAttr("account");
		BroadcastRequest request = roomMerchantService.selectCurrentRequest(account);
		JMResult.success(this, request, JMMessage.SUCCESS);
	}
	
	
	/**
	 * 
	 * @author cyl
	 * @date 2019年7月4日 上午11:21:19
	 * @Description:查看我的房管
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class})
	public void selectMyManage() {
		Account account = getAttr("account");
		roomMerchantService.selectMyManage(account).renderJson(this);
	}
	
	
	/**
	 * 
	 * @author cyl
	 * @date 2019年7月4日 上午11:21:19
	 * @Description:查看我的主播
	 * @reqMethod post
	 * @paramter sessionId,String,个人信息唯一标识ID,r:t,p:2e40f29dbed24774a3326146af3212cb,d:2e40f29dbed24774a3326146af3212cb
	 */
	@API(isScran = true)
	@Before(value={JMApiAccountInterceptor.class})
	public void selectMyAnchor() {
		Account account = getAttr("account");
		roomMerchantService.selectMyAnchor(account).renderJson(this);
	}
	
	
	

}
