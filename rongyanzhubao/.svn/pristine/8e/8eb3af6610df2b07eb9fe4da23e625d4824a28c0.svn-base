package com.cn.jm.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PropKit;

import net.sf.json.JSONObject;


/**
 * 
 * 类说明：腾讯聊天群组相关管理
 */

public class TencentIMUtils {
	private static final String create_group_url = "https://console.tim.qq.com/v4/group_open_http_svc/create_group";
	private static final String forbid_send_msg = "https://console.tim.qq.com/v4/group_open_http_svc/forbid_send_msg";
	private static final String black_list_add = "https://console.tim.qq.com/v4/sns/black_list_add";
	private static final String black_list_delete = "https://console.tim.qq.com/v4/sns/black_list_delete";
	private static final String black_list_get = "https://console.tim.qq.com/v4/sns/black_list_get";
	private static final String change_group_owner = "https://console.tim.qq.com/v4/group_open_http_svc/change_group_owner";
	private static final String account_import = "https://console.tim.qq.com/v4/im_open_login_svc/account_import";
	private static final String openim_dirty_words_add="https://console.tim.qq.com/v4/openim_dirty_words/add";
	private static final String openim_dirty_words_delete="https://console.tim.qq.com/v4/openim_dirty_words/delete";
	//云通信自定义消息
	private static final String sendMsg="https://console.tim.qq.com/v4/openim/sendmsg";
	//云直播创建录制任务与结束录制任务接口
	private static final String recordUrl ="http://fcgi.video.qcloud.com/common_access";

	private static Map<String, String> getQueryParas() {
		String userSig = ZbUserSig.genUserSig("admin", 15552000);
		Map<String, String> queryParas = new HashMap<String, String>();
		queryParas.put("usersig", userSig);
		queryParas.put("identifier", "admin");
		queryParas.put("sdkappid",PropKit.get("TENCENT_COULD_SDKAPPID"));
		queryParas.put("random", getCode(8));
		queryParas.put("contenttype", "json");
		return queryParas;
	}

	private static Map<String, String> getHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		return headers;
	}
	  /**
	   * 
	   * @Description:生成指定长度的随机数
	   * @param length
	   * @return
	   */
	  public static String getCode(int length) {
	    String code = "";
	    for (int i = 0; i < length; i++) {
	      Random random = new Random();
	      int str = random.nextInt(9) + 1;
	      code += str;
	    }
	    return code;
	  }
	

	public static final TencentIMUtils ZB_SERVICE = new TencentIMUtils();

	public static void main(String[] args) {
		// String[] menber = {"peter","leckie"};
//		String[] menber = { "1234aa" };
//		String GroupId = "@TGS#2TKS2UJFJ";
//		System.out.println(forbidSendMsg(GroupId, menber, 60));
//		System.err.println(addBlack("test1", menber));
//		XPResult addOrDeleteBlack = addOrDeleteBlack("test1", menber, 0);
//		boolean addOrDeleteBlack = addOrDeleteBlack("test1", menber, 0);
//		System.err.println(addOrDeleteBlack("test1", menber, 1));
//		System.err.println(listBlack("a123", 0, 10, 0));
		System.err.println(accountImport(62+"", "nick", "http://www.baidu.com"));
		//创建群组
//		accountImport("5", "nick", "http://www.qq.com");
//		String createGroup = createGroup("7", "group:7-nickb","nickb", "http://www.qq.com");
//		System.out.println(createGroup);
	}
	
	/**
	 * 
	 * @Description: 获取调用腾讯api创建群组的ID， 
	 * @author: LTS
	 * @data: 2018年7月7日上午11:18:27
	 * @param str
	 * @return    群ID
	 *{"ActionStatus":"OK","ErrorCode":0,"GroupId":"@TGS#aFABE6JFD"}
	 */
	private static String getCreatGroupResult(String str){
		int start = str.indexOf("\"GroupId\":");
		int end = str.indexOf("\"}");
		str = str.substring(start+11, end);
		return str;
	}
	

	/**
	 * 
	 * @Description: 获取调用腾讯api文档结果码， 
	 * @author: LTS
	 * @data: 2018年7月7日上午11:18:27
	 * @param str
	 * @return    0正常，其他请参加腾讯云错误码
	 *
	 */
	private static int getCode(String str){
//		JSONObject jsonObject = JSONObject.fromObject(str);
//		System.err.println(str);
//		System.err.println(jsonObject.getString("ResultItem"));
		int result = -1;
		if(str.contains("OK")){
			result = 0;
		}
		return result;
//		int start = str.indexOf("\"ResultCode\":");
//		int end = str.indexOf(",\"ResultInfo\":");
//		str = str.substring(start+13, end);
//		System.err.println("ResultCode"+str);
//		return Integer.valueOf(str);
	}
	
//	JSONObject jsonObject = JSONObject.fromObject(str);
//	System.err.println(str);
//	System.err.println(jsonObject.getString("ResultItem"));
	/**
	 * 
	 * @Description: 创建聊天群
	 * @author: LTS
	 * @data: 2018年7月6日下午4:33:01
	 * @param userId
	 *            指定群组管理者的userId，必填
	 * @param groupName
	 *            创建的群名称，必填
	 * @param nick
	 *            群主昵称，选填
	 * @param FaceUrl
	 *            群主头像，选填
	 *
	 */
	public static String createGroup(String userId, String groupName, String nick, String FaceUrl,String GroupId) {
		
		//先导入群主ID
		
		if(accountImport(userId, nick, FaceUrl) == false)
			return "导入群主到腾讯云出错";
		
		String data = "{" + "\"Owner_Account\": \"" + userId + "\"," + // 群主的UserId（选填）
					"\"Type\": \"AVChatRoom\"," + // 群组类型：（必填）私有群（Private）公开群（Public）聊天室（ChatRoom）互动直播聊天室（AVChatRoom）在线成员广播大群（BChatRoom）
					"\"GroupId\": \""+GroupId+"\","+
					"\"Name\": \"" + groupName + "\"" // 群名称（必填）
					+ "}";
			
		// 发送请求
		String GruoupResult = HttpKit.post(create_group_url, getQueryParas(), data, getHeaders());
	    System.err.println("创建腾讯云群组："+GruoupResult);
	    
		return getCreatGroupResult(GruoupResult);
		

	}


	/*
	 * { "GroupId": "@TGS#2C5SZEAEF", "Members_Account": [ // 最多支持500个 "peter",
	 * "leckie" ], "ShutUpTime": 60 // 禁言时间，单位为秒 }
	 */
	/**
	 * 
	 * @Description: 禁言
	 * @author: LTS
	 * @data: 2018年7月6日下午6:53:24
	 * @param GroupId
	 *            群组id
	 * @param Members_Account
	 *            要禁言的成员，指在腾讯云的userId，最多支持500个
	 * @param ShutUpTime
	 *            禁言时间，单位为秒
	 * @return
	 *
	 */
	public static boolean forbidSendMsg(String GroupId, String[] Members_Account, int ShutUpTime) {
		StringBuilder data = new StringBuilder();
		StringBuilder munber = new StringBuilder();
		data.append("{");
		data.append("\"GroupId\": \"" + GroupId + "\",");
		data.append("\"Members_Account\": [");
		for (String string : Members_Account) {
			munber.append("\"" + string + "\",");
		}
		String munbers = munber.substring(0, munber.length() - 1);
		data.append(munbers);
		data.append("],");
		data.append("\"ShutUpTime\": " + ShutUpTime);
		data.append("}");
		String GruoupResult = HttpKit.post(forbid_send_msg, getQueryParas(), data.toString(), getHeaders());
		System.err.println("禁言："+GruoupResult);
		if(getCode(GruoupResult) ==0){
			return true;
		} else{
			return false;
		}
	}
	
	/**
	 * 
	 * @Description: 添加或删除黑名单 
	 * @author: LTS
	 * @data: 2018年7月6日下午7:01:14
	 * @param From_Account  请求为该 Identifier 添加黑名单。
	 * @param To_Account 待添加为黑名单的用户 Identifier 列表，单次请求的 To_Account 数不得超过 1000。
	 * @param type  0 添加  1删除
	 * @return    ResultCode 0 正常
	 *
	 */
	public static boolean addOrDeleteBlack(String From_Account, String[] To_Account, int type){
		StringBuilder data = new StringBuilder();
		StringBuilder munber = new StringBuilder();
		data.append("{");
		data.append("\"From_Account\": \"" + From_Account + "\",");
		data.append("\"To_Account\": [");
		for (String string : To_Account) {
			munber.append("\"" + string + "\",");
		}
		String munbers = munber.substring(0, munber.length() - 1);
		data.append(munbers);
		data.append("]");
		data.append("}");
		String result = "";
		if(type ==0){
			result = HttpKit.post(black_list_add, getQueryParas(), data.toString(), getHeaders());
		} else if(type ==1){
			result = HttpKit.post(black_list_delete, getQueryParas(), data.toString(), getHeaders());
			
		}
		if(getCode(result) == 0){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * @Description: 拉取黑名单 
	 * @author: LTS
	 * @data: 2018年7月6日下午7:13:36
	 * @param From_Account 需要拉取该 Identifier 的黑名单。
	 * @param StartIndex  拉取的起始位置。
	 * @param MaxLimited  每页最多拉取的黑名单数。
	 * @param LastSequence 上一次拉黑名单时后台返回给客户端的 Seq，初次拉取时为 0。
	 * @return    
	 *
	 */
	public static boolean listBlack(String From_Account, int StartIndex ,int MaxLimited, int LastSequence){
		StringBuilder data = new StringBuilder();
		data.append("{");
		data.append("\"From_Account\": \"" + From_Account + "\",");
		data.append("\"StartIndex\": " + StartIndex+",");
		data.append("\"MaxLimited\": " + MaxLimited+",");
		data.append("\"LastSequence\": " + LastSequence);
		data.append("}");
		System.err.println(data.toString());
		String result = HttpKit.post(black_list_get, getQueryParas(), data.toString(), getHeaders());
		if(getCode(result) ==0){
			return true;
		} else{
			return false;
		}
	}
	
	/**
	 * 
	 * @Description: 移交群管理员 
	 * @author: LTS
	 * @data: 2018年7月7日下午2:08:53
	 * @param GroupId   要被转移的群ID（必填）
	 * @param NewOwner_Account   新群主ID（必填）
	 * @return    
	 *
	 */
	public static boolean changeGroupOwner(String GroupId, String NewOwner_Account){
		StringBuilder data = new StringBuilder();
		data.append("{");
		data.append("\"GroupId\": \"" + GroupId + "\",");
		data.append("\"NewOwner_Account\": \"" + GroupId + "\"");
		data.append("}");
		String result = HttpKit.post(change_group_owner, getQueryParas(), data.toString(), getHeaders());
		if(getCode(result) ==0){
			return true;
		} else{
			return false;
		}
	}
	
	/**
	 * 
	 * @Description: 用户导入腾讯云通信 
	 * @author: LTS
	 * @data: 2018年7月12日上午11:03:41
	 * @param identifier   用户名，长度不超过 32 字节， 必填
	 * @param nick    用户昵称，选填
	 * @param FaceUrl  	用户头像URL，选填
	 * @return    
	 *
	 */
	private static boolean accountImport(String identifier, String nick, String FaceUrl){
		StringBuilder data = new StringBuilder();
		data.append("{");
		data.append("\"Identifier\": \"" +identifier + "\",");
		data.append("\"Nick\": \"" + nick + "\",");
		data.append("\"FaceUrl\": \"" + nick + "\"");
		data.append("}");
		String result = HttpKit.post(account_import, getQueryParas(), data.toString(), getHeaders());
		JSONObject jsonObject = JSONObject.fromObject(result);
		String ActionStatus = jsonObject.getString("ActionStatus"); 
		int ErrorCode = jsonObject.getInt("ErrorCode"); 
		if(ActionStatus.equals("OK") && ErrorCode==0){
			return true;
		}
		return false;
	}
	
	/**
	 * 添加App自定义脏字
	 */
	public static boolean addDirtyWord(String word){
		StringBuilder data = new StringBuilder();
		data.append("{");
		data.append("\"DirtyWordsList\":[\"" +word + "\"]");
		data.append("}");
		String result = HttpKit.post(openim_dirty_words_add, getQueryParas(), data.toString(), getHeaders());
		JSONObject jsonObject = JSONObject.fromObject(result);
		System.err.println(jsonObject);
		String ActionStatus = jsonObject.getString("ActionStatus"); 
		int ErrorCode = jsonObject.getInt("ErrorCode"); 
		if(ActionStatus.equals("OK") && ErrorCode==0){
			return true;
		}
		return false;
	}
	/**
	 * 删除App自定义脏字
	 */
	public static boolean deleteDirtyWord(String word){
		StringBuilder data = new StringBuilder();
		data.append("{");
		data.append("\"DirtyWordsList\":[\"" +word + "\"]");
		data.append("}");
		String result = HttpKit.post(openim_dirty_words_delete, getQueryParas(), data.toString(), getHeaders());
		JSONObject jsonObject = JSONObject.fromObject(result);
		System.err.println(jsonObject);
		String ActionStatus = jsonObject.getString("ActionStatus"); 
		int ErrorCode = jsonObject.getInt("ErrorCode"); 
		if(ActionStatus.equals("OK") && ErrorCode==0){
			return true;
		}
		return false;
	}
	
	
}