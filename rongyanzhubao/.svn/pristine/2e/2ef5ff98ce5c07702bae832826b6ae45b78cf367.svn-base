package com.cn.jm.util;

import com.cn.jm.core.JMMessage;
import com.cn.jm.core.utils.util.JMResult;
import com.cn.jm.information.PromptInformationEnum;
import com.jfinal.core.Controller;

public class JMResultUtil{

	/** 需要绑定的code*/
	private static final int NEED_BINDING_CODE = 3;
	/** 可以创建直播间的code*/
	private static final int CAN_SHOW = 4;
	/** 主播离开直播间的code*/
	public static final int ROOM_CLOSE = 5;
	/** 房管或主播发送订单给用户*/
	public static final int SEND_ORDER = 6;
	/** 主播向商家请求开播*/
	public static final int BROADCAST_REQUEST = 7;
	/** 商家请求关联主播*/
	public static final int ASSOCIATE_REQUEST = 8;
	/** 更新直播间人数*/
	public static final int ROOM_NUMBER_UPDATE = 9;
	/** 直播间冻结 */
	public static final int ROOM_FZ = 10;
	/** 系统通知 */
	public static final int SYSTEM_NOTICE  = 11;
	
	
	public static JMResult getJMResult(int code,Object data,PromptInformationEnum promptInformation) {
		return JMResult.create().code(code).data(data).desc(promptInformation.getDesc());
	}

	public static JMResult success(Object data,PromptInformationEnum promptInformation) {
		return getJMResult(JMResult.SUCCESS, data,promptInformation);
	}

	public static JMResult success(PromptInformationEnum promptInformation) {
		return getJMResult(JMResult.SUCCESS,null,promptInformation);
	}
	
	public static JMResult success(Object data) {
		return getJMResult(JMResult.SUCCESS,data,PromptInformationEnum.SUCCESS);
	}
	
	public static JMResult fail(Object data,PromptInformationEnum promptInformation) {
		return getJMResult(JMResult.FAIL, data,promptInformation);
	}
	
	public static JMResult fail(PromptInformationEnum promptInformation) {
		return getJMResult(JMResult.FAIL,null,promptInformation);
	}

	public static JMResult fail(Object data) {
		return getJMResult(JMResult.FAIL,data,PromptInformationEnum.FAIL);
	}

	public static JMResult needBinding(Object data) {
		return getJMResult(NEED_BINDING_CODE,data,PromptInformationEnum.NEED_BINDING_MAIL);
	}

	public static void success(Controller controller, Object data) {
		success(data).renderJson(controller);
	}

	public static void fail(Controller controller, Object data) {
		fail(data).renderJson(controller);
	}
	
	public static JMResult canShow() {
		return getJMResult(CAN_SHOW,null,PromptInformationEnum.CAN_SHOW);
	}
	
	public static JMResult failDesc(String desc) {
		return JMResult.create().code(JMResult.FAIL).desc(desc);
	}
	
	public static JMResult loginError() {
		return JMResult.create().code(JMResult.LOGIN_ERROR)
				.desc(JMMessage.SESSION_ID_NULL);
	}
	
	
	public static JMResult success() {
		return success(PromptInformationEnum.SUCCESS);
	}
	
	public static JMResult successDesc(String desc) {
		return JMResult.create().code(JMResult.SUCCESS).desc(desc);
	}
	
	public static JMResult success(String desc,Integer code) {
		return JMResult.create().code(code).desc(desc);
	}

	public static JMResult fail() {
		return fail(PromptInformationEnum.FAIL);
	}
	
	public static JMResult success(String desc,Object data,int code) {
		return JMResult.create().code(code).desc(desc).data(data);
	}
	
	
}
