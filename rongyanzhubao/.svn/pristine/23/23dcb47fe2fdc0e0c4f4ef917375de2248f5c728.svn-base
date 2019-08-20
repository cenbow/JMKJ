package com.cn.jm.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.jm.core.utils.util.DateUtils;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PropKit;

/**
 * 创建人： 李智胜 创建时间：2017年11月16日下午4:58:51 类说明：腾讯直播服务端
 */

public class ZbUtil {
	public static ZbUtil util = new ZbUtil();

	private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

	private static final String SRC_FILE_LIST_FILE_ID = "srcFileList.${n}.fileId";
	private static final String N = "${n}";
	private static final String SUCCESS = "Success";
	
	/** 腾讯云信息*/
	String TENCENT_URL = PropKit.use("thirdparty.properties").get("TENCENT_URL");
	String TENCENT_ZHIBO_API_KEY = PropKit.use("thirdparty.properties").get("TENCENT_ZHIBO_API_KEY");
	//防盗链appkey
	String TENCENT_ZHIBO_FANGDAO_KEY = PropKit.use("thirdparty.properties").get("TENCENT_ZHIBO_FANGDAO_KEY");
	String TENCENT_BIZID = PropKit.use("thirdparty.properties").get("TENCENT_BIZID");
	String TENCENT_APPID = PropKit.use("thirdparty.properties").get("TENCENT_APPID");
	//应用的sdkAppid
	String TENCENT_COULD_SDKAPPID = PropKit.use("thirdparty.properties").get("TENCENT_COULD_SDKAPPID");
	//应用私钥
	String TENCENT_PRIVATE_KEY = PropKit.use("thirdparty.properties").get("TENCENT_PRIVATE_KEY");
	//应用公钥
	String TENCENT_PUBLIC_KEY = PropKit.use("thirdparty.properties").get("TENCENT_PUBLIC_KEY");
	
	String TENCENT_SECRET_KEY = PropKit.use("thirdparty.properties").get("TENCENT_SECRET_KEY");
	String TENCENT_SECRET_ID = PropKit.use("thirdparty.properties").get("TENCENT_SECRET_ID");

	/**
	 * 
	 * @param key
	 *            防盗链key
	 * @param streamId
	 *            腾讯的Bizid 加上房间id
	 * @param txTime
	 *            流过期时间
	 * @return
	 */
	private String getSafeUrl(String key, String streamId, long txTime) {
		String input = new StringBuilder().append(key).append(streamId).append(Long.toHexString(txTime).toUpperCase())
				.toString();

		String txSecret = null;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			txSecret = byteArrayToHexString(messageDigest.digest(input.getBytes("UTF-8")));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return txSecret == null ? ""
				: new StringBuilder().append("txSecret=").append(txSecret).append("&").append("txTime=")
						.append(Long.toHexString(txTime).toUpperCase()).toString();
	}

	private String byteArrayToHexString(byte[] data) {
		char[] out = new char[data.length << 1];

		for (int i = 0, j = 0; i < data.length; i++) {
			out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS_LOWER[0x0F & data[i]];
		}
		return new String(out);
	}

	/**
	 * 
	 * 方法说明：生成推流地址 创建时间：2017年11月16日下午6:09:45 By 李智胜
	 * 
	 * @param zbRoomId
	 * @return
	 **
	 */
	public String getPushUrl(String zbRoomId, boolean record) {
		String streamId = TENCENT_BIZID + "_" + zbRoomId;
		String bizid = TENCENT_BIZID;

		String pushUrl = "rtmp://" + bizid + ".livepush.myqcloud.com/live/" + streamId + "?bizid=" + bizid + "&"
				+ getSafeUrl(TENCENT_ZHIBO_FANGDAO_KEY, streamId,
						DateUtils.offsetDate(new Date(), 1, Calendar.DATE).getTime() / 1000);
		if (record) {
			pushUrl = pushUrl + "&record=mp4&record_interval=5400";
		}
		return pushUrl;
	}

	/**
	 * 
	 * @Description:生成推流地址
	 * @param id
	 * @param record
	 * @return
	 */
	public String getPushUrl(Long id, boolean record) {
		String streamId = TENCENT_BIZID + "_" + id;
		String bizid = TENCENT_BIZID;
		String pushUrl = "rtmp://" + bizid + ".livepush.myqcloud.com/live/" + streamId + "?bizid=" + bizid + "&"
				+ getSafeUrl(TENCENT_ZHIBO_FANGDAO_KEY, streamId,
						DateUtils.offsetDate(new Date(), 1, Calendar.DATE).getTime() / 1000);
		if (record) {
			pushUrl = pushUrl + "&record=mp4&record_interval=5400";
		}
		return pushUrl;
	}

	/**
	 * 
	 * 方法说明：生成播放地址 创建时间：2017年11月17日上午9:27:51 By 李智胜
	 * 
	 * @param zbRoomId
	 * @return
	 **
	 */
	public String getPlayUrl(String zbRoomId) {

		String streamId = TENCENT_BIZID + "_" + zbRoomId;

		String playUrl = "http://" + TENCENT_BIZID + ".liveplay.myqcloud.com/live/" + streamId;
		return playUrl;
	}

	public String getStreamId(Long id) {
		return TENCENT_BIZID + "_" + id;
	}

//	/**
//	 * 
//	 * @Description:一对一没有房间
//	 * @param id
//	 * @return
//	 */
//	public String getPlayUrl(Long id) {
//		String streamId = TENCENT_BIZID + "_" + id + ".m3u8";// hls协议播放流
//		// String streamId = TENCENT_BIZID + "_" + id;//rtmp协议
//
//		String playUrl = "http://" + TENCENT_BIZID + ".liveplay.myqcloud.com/live/" + streamId;
//		return playUrl;
//	}
	/**
	 * 临时播流地址
	 * @param id
	 * @return
	 */
	@Deprecated
	public String getPlayUrl(Long id) {
		String streamId = TENCENT_BIZID + "_" + id + ".m3u8";// hls协议播放流
		// String streamId = TENCENT_BIZID + "_" + id;//rtmp协议

		String playUrl = "http://liveplay.szrivers.cn/live/" + streamId;
		return playUrl;
	}
	/**
	 * 
	 * 方法说明：结束直播 创建时间：2017年11月17日下午2:54:12 By 李智胜
	 * 
	 * @param channelId
	 * @return
	 **
	 */
	public boolean stopZb(String channelId) {
		try {
			long t = new Date().getTime();
			String sign = JMToolMD5.MD5_32bit(TENCENT_ZHIBO_API_KEY + t);
			String Paras = "?appid=" + TENCENT_APPID + "&interface=Live_Channel_SetStatus"
					+ "&Param.s.channel_id=" + channelId + "&Param.n.status=0" + "&t=" + t + "&sign=" + sign;
			String result = HttpKit.get(TENCENT_URL + Paras);
			JSONObject obj = JSONObject.parseObject(result);
			if (obj.getInteger("ret") == 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 该截取出来的鬼东西也可以称为StreamName
	 * 
	 * @param pushUrl
	 * @return
	 */
	public String getChannelIdByPushUrl(String pushUrl) {
		int indexOf = pushUrl.indexOf("/live/");
		return pushUrl.substring(indexOf + 6, pushUrl.indexOf("?bizid="));
	}

	/**
	 * 
	 * method statement：API鉴权回调sign校验 createTime：2018年1月29日下午12:03:05 By JaysonLee
	 * 
	 * @param t
	 * @param sign
	 * @return
	 **
	 */
	public boolean IsSign(String t, String sign) {
		String input = new StringBuffer().append(TENCENT_ZHIBO_API_KEY).append(t).toString();
		String mySign = null;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			mySign = byteArrayToHexString(messageDigest.digest(input.getBytes("UTF-8")));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (sign.equals(mySign)) {
			return true;
		}
		return false;
	}

	/**
	 * 通过房间id去录制视频
	 * 
	 * @param channelId
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public Object start(String channelId) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date nowTime = new Date();
		Date endTimeTime = new Date(nowTime.getTime() + 1000 * 60 * 60 * 23);
		// long t = new Date().getTime() + 60000;
		long t = new Date().getTime();
		String sign = JMToolMD5.MD5_32bit(TENCENT_ZHIBO_API_KEY + t);
		String url = TENCENT_URL + "?appid=" + TENCENT_APPID
				+ "&interface=Live_Tape_Start" + "&Param.s.file_format=mp4" + "&Param.s.record_type=mp4"
				+ "&Param.s.channel_id=" + channelId + // 频道id
				"&Param.s.start_time=" + URLEncoder.encode(formatter.format(nowTime), "UTF-8") + // 开始时间 需要urlencode
				"&Param.s.end_time=" + URLEncoder.encode(formatter.format(endTimeTime), "UTF-8") + // 结束时间
				"&Param.n.task_sub_type=0" + // 是否开启实时录制 1开启 0关闭
				"&t=" + t + "&sign=" + sign;
		String request = HttpKit.get(url);
		return JSONObject.parseObject(request).getJSONObject("output").get("task_id");
	}

	@Deprecated
	public JSONObject stopRecording(String channel_id, String taskId) throws Exception {
		long t = new Date().getTime();
		String sign = JMToolMD5.MD5_32bit(TENCENT_ZHIBO_API_KEY + t);
		String url = TENCENT_URL + "?appid=" + TENCENT_APPID
				+ "&interface=Live_Tape_Stop" + "&Param.s.channel_id=" + channel_id + // 频道id
				"&Param.n.task_id=" + taskId + "&t=" + t + "&sign=" + sign;
		JSONObject jsonObject = JSONObject.parseObject(HttpKit.get(url));
		return jsonObject;
	}

	@Deprecated
	public JSONArray selectRecordVideo(String channelId) throws Exception {
		long t = new Date().getTime() + 60000;
		String sign = JMToolMD5.MD5_32bit(TENCENT_ZHIBO_API_KEY + t);
		String url = TENCENT_URL + "?appid=" + TENCENT_APPID
				+ "&interface=Live_Tape_GetFilelist" + "&Param.n.page_no=1" + "&Param.n.page_size=10"
				+ "&Param.s.channel_id=" + channelId + "&t=" + t + "&sign=" + sign;
		JSONObject request = JSONObject.parseObject(HttpKit.get(url));
		JSONArray jsonArray = request.getJSONObject("output").getJSONArray("file_list");
		if (jsonArray.size() != 0) {
			return jsonArray;
//			return jsonArray.getJSONObject(0).get("record_file_url");
		}
		return null;
	}

	/**
	 * 根据拼接任务id,获取拼接后的视频 暂时用于获取拼接视频完的方法,以后需获取其他可自行改动
	 * 
	 * @param channelId
	 * @return
	 * @throws Exception
	 */
	public String getSplicingVideoByVodTaskId(String vodTaskId) throws Exception {
		// TreeMap可以自动排序
		TreeMap<String, Object> params = getPublicTree("GetTaskInfo");
		params.put("vodTaskId", vodTaskId);
		params.put("Signature", sign(getStringToSign(params), TENCENT_SECRET_KEY, "HmacSHA1")); // 公共参数
		JSONObject requestObject = JSONObject.parseObject(HttpKit.get(getUrl(params)));
		if (SUCCESS.equals(requestObject.get("codeDesc"))) {
			// 正确时的获取实例
			// JSONObject fromObject =
			// JSONObject.parseObject("{\"code\":0,\"message\":\"\",\"codeDesc\":\"Success\",\"createTime\":1552286424,\"type\":\"concatFile\",\"beginProcessTime\":1552286424,\"finishTime\":1552286427,\"status\":\"FINISH\",\"data\":{\"concact_id\":\"\",\"fileInfo\":[{\"fileId\":\"5285890786678776558\",\"fileType\":\"mp4\",\"fileUrl\":\"http:\\/\\/1258705927.vod2.myqcloud.com\\/24942d10vodcq1258705927\\/fcbf4cea5285890786678776558\\/playlist.f9.mp4\",\"message\":\"OK\",\"status\":0}],\"userContext\":\"\",\"vodTaskId\":\"1258705927-concatFile-05d11f11444927a73cec29fa1b7b787ct0\"}}");
			return requestObject.getJSONObject("data").getJSONArray("fileInfo").getJSONObject(0).getString("fileUrl");
		} else {
			return null;
		}
	}

	/**
	 * 根据获取腾讯云录制返回的视频列表进行拼接
	 * @param jsonArray
	 * @return
	 * @throws Exception
	 */
	public Object getSplicingVideoVodTaskId(JSONArray jsonArray) throws Exception {
		// TreeMap可以自动排序
		TreeMap<String, Object> params = getPublicTree("ConcatVideo");
		for (int i = 0; i < jsonArray.size(); i++) {
			params.put(SRC_FILE_LIST_FILE_ID.replace(N, String.valueOf(i)), jsonArray.getJSONObject(i).get("file_id")); // 业务参数
		}
		params.put("name", "splicingVideo"); // 业务参数
		params.put("dstType.0", "mp4"); // 业务参数
		params.put("Signature", sign(getStringToSign(params), TENCENT_SECRET_KEY, "HmacSHA1")); // 公共参数
		JSONObject responseObject = JSONObject.parseObject(HttpKit.get(getUrl(params)));
		if (SUCCESS.equals(responseObject.get("codeDesc"))) {
			return responseObject.get("vodTaskId");
		} else {
			return null;
		}
	}

	public static void main(String[] args) throws Exception {
//		String str = "{\"errmsg\":\"return successfully!\",\"message\":\"returnsucessfully!\",\"output\":{\"all_count\":3,\"file_list\":[{\"appid\":\"1258705927\",\"create_time\":\"2019-03-111435:15\",\"end_time\":\"2019-03-111435:14\",\"err_code\":\"0\",\"expire_time\":\"2038-01-191114:07\",\"file_format\":\"3\",\"file_id\":\"5285890786670115322\",\"file_size\":\"2140533\",\"id\":\"326278046\",\"record_file_id\":\"5285890786670115322\",\"record_file_url\":\"http://1258705927.vod2.myqcloud.com/24942d10vodcq1258705927/86814aca5285890786670115322/f0.mp4\",\"record_type\":null,\"report_message\":null,\"start_time\":\"2019-03-111435:10\",\"stream_id\":\"42934_554673555988545536\",\"task_id\":null,\"task_sub_type\":\"0\",\"vid\":\"1258705927_4d26c7e2e82f4b69a7f29a2193288136\",\"vod2Flag\":\"1\"},{\"appid\":\"1258705927\",\"create_time\":\"2019-03-111435:29\",\"end_time\":\"2019-03-111435:28\",\"err_code\":\"0\",\"expire_time\":\"2038-01-191114:07\",\"file_format\":\"3\",\"file_id\":\"5285890786670098248\",\"file_size\":\"445009\",\"id\":\"326278116\",\"record_file_id\":\"5285890786670098248\",\"record_file_url\":\"http://1258705927.vod2.myqcloud.com/24942d10vodcq1258705927/84be875b5285890786670098248/f0.mp4\",\"record_type\":null,\"report_message\":null,\"start_time\":\"2019-03-111435:28\",\"stream_id\":\"42934_554673555988545536\",\"task_id\":null,\"task_sub_type\":\"0\",\"vid\":\"1258705927_acf7cec1b5254734a5c7441ba9ebc2be\",\"vod2Flag\":\"1\"},{\"appid\":\"1258705927\",\"create_time\":\"2019-03-111435:44\",\"end_time\":\"2019-03-111435:44\",\"err_code\":\"0\",\"expire_time\":\"2038-01-191114:07\",\"file_format\":\"3\",\"file_id\":\"5285890786670116737\",\"file_size\":\"623718\",\"id\":\"326278194\",\"record_file_id\":\"5285890786670116737\",\"record_file_url\":\"http://1258705927.vod2.myqcloud.com/24942d10vodcq1258705927/8681e8555285890786670116737/f0.mp4\",\"record_type\":null,\"report_message\":null,\"start_time\":\"2019-03-111435:43\",\"stream_id\":\"42934_554673555988545536\",\"task_id\":null,\"task_sub_type\":\"0\",\"vid\":\"1258705927_30076ce71bde4809ab0e405be77bb1a1\",\"vod2Flag\":\"1\"}]},\"ret\":0,\"retcode\":0}";
//		JSONObject request = JSONObject.parseObject(str);
//		JSONArray jsonArray = request.getJSONObject("output").getJSONArray("file_list");
		ZbUtil z = new ZbUtil();
//		Object vodTaskId = z.getSplicingVideoVodTaskId(jsonArray);
//		System.out.println(vodTaskId);
		String vodTaskId = "1258705927-concatFile-fa747c00ddfc40cb5340affd378e9a4ct0";
		if(vodTaskId != null) {
			String url = z.getSplicingVideoByVodTaskId(vodTaskId.toString());
			System.out.println(url);
		}
	}

	private final static String CHARSET = "UTF-8";
	
	private TreeMap<String, Object> getPublicTree(String action) {
		// TreeMap可以自动排序
		TreeMap<String, Object> params = new TreeMap<String, Object>();
		// 实际调用时应当使用随机数，例如：params.put("Nonce", new
		// Random().nextInt(java.lang.Integer.MAX_VALUE));
		params.put("Nonce", new Random().nextInt(java.lang.Integer.MAX_VALUE)); // 公共参数
		// 实际调用时应当使用系统当前时间，例如： params.put("Timestamp", System.currentTimeMillis() /
		// 1000);
		params.put("Timestamp", System.currentTimeMillis() / 1000); // 公共参数
		params.put("SecretId", TENCENT_SECRET_ID); // 公共参数
		params.put("Action", action); // 公共参数
		params.put("Version", "2018-07-17"); // 公共参数
		params.put("Region", "ap-guangzhou"); // 公共参数
		return params;
	}
	
	public static String sign(String s, String key, String method) throws Exception {
		Mac mac = Mac.getInstance(method);
		SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(CHARSET), mac.getAlgorithm());
		mac.init(secretKeySpec);
		byte[] hash = mac.doFinal(s.getBytes(CHARSET));
		return DatatypeConverter.printBase64Binary(hash);
	}

	public static String getStringToSign(TreeMap<String, Object> params) {
		StringBuilder s2s = new StringBuilder("GETvod.api.qcloud.com/v2/index.php?");
		// 签名时要求对参数进行字典排序，此处用TreeMap保证顺序
		for (String k : params.keySet()) {
			s2s.append(k).append("=").append(params.get(k).toString()).append("&");
		}
		return s2s.toString().substring(0, s2s.length() - 1);
	}

	public static String getUrl(TreeMap<String, Object> params) throws UnsupportedEncodingException {
		StringBuilder url = new StringBuilder("https://vod.api.qcloud.com/v2/index.php?");
		// 实际请求的url中对参数顺序没有要求
		for (String k : params.keySet()) {
			// 需要对请求串进行urlencode，由于key都是英文字母，故此处仅对其value进行urlencode
			url.append(k).append("=").append(URLEncoder.encode(params.get(k).toString(), CHARSET)).append("&");
		}
		return url.toString().substring(0, url.length() - 1);
	}

	// /**
	// * 创建互动直播聊天室
	// * @param room
	// * @return
	// */
	// public String addAVChatRoom(Room room,Long accountId) {
	// JSONObject json = new JSONObject();
	// json.put("Type","AVChatRoom");
	// json.put("GroupId",room.getId().toString());
	// json.put("Name","直播弹幕");
	// String url =
	// "https://console.tim.qq.com/v4/group_open_http_svc/create_group?"
	// + "usersig=\""+ZbUserSig.genUserSig(accountId,1000*60*60*2,1)+"\""
	// + "&identifier="+room.getId()
	// + "&sdkappid=1400129920"
	// + "&random="+JMBaseDao.getSnowflake().nextId()
	// + "&contenttype=\"json\"";
	// String request = HttpKit.post(url, json.toString());
	// return request;
	// }
	//
	// /**
	// * 解散互动直播聊天室
	// * @param args
	// */
	// public String destroyGroup(Room room,Long accountId) {
	// JSONObject json = new JSONObject();
	// json.put("GroupId",room.getId().toString());
	// String url =
	// "https://console.tim.qq.com/v4/group_open_http_svc/destroy_group?"
	// + "usersig=\""+ZbUserSig.genUserSig(accountId,1000*60*60*2,1)+"\""
	// + "&identifier="+room.getId()
	// + "&sdkappid=1400129920"
	// + "&random="+JMBaseDao.getSnowflake().nextId()
	// + "&contenttype=\"json\"";
	// String request = HttpKit.post(url, json.toString());
	// System.out.println(request);
	// return request;
	// }

}
