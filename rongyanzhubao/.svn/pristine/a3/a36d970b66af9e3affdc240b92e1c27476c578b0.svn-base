package com.cn.jm.util;


import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.zip.Deflater;

import com.jfinal.kit.PropKit;


/**
 * 获取腾讯云IM签名userSig
 * @author admin
 *
 */
public class ZbUserSig {
    private static int mSdkAppid = 0;
    private static PrivateKey mPrivateKey = null;
    private static PublicKey mPublicKey = null;
    private static String tencent_private_key = PropKit.use("thirdparty.properties").get("TENCENT_PRIVATE_KEY");
    private static String tencent_public_key = PropKit.use("thirdparty.properties").get("TENCENT_PUBLIC_KEY");;
    private static String tencent_could_sdkAppId = PropKit.use("thirdparty.properties").get("TENCENT_COULD_SDKAPPID");;
    /**
     * 设置sdkappid
     * @param sdkappid
     */
    private static void setSdkAppid(int sdkappid) {
        mSdkAppid = sdkappid;
    }
    
    /**
     * 设置私钥 如果要生成userSig和privMapEncrypt则需要私钥
     * @param privateKey 私钥文件内容
     */
    private static void setPrivateKey(String privateKey) {
//        String privateKeyPEM = privateKey.replace("-----BEGIN PRIVATE KEY-----", "")
//        .replace("-----END PRIVATE KEY-----", "")
//        .replace("\n", "");
        byte[] encodedKey = Base64.getDecoder().decode(privateKey.getBytes());
        
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            mPrivateKey = keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 设置公钥 如果要验证userSig和privMapEncrypt则需要公钥
     * @param publicKey 公钥文件内容
     */
    private static void  setPublicKey(String publicKey) {
//        String publicKeyPEM = publicKey.replace("-----BEGIN PUBLIC KEY-----", "")
//        .replace("-----END PUBLIC KEY-----", "")
//        .replace("\n", "");
        byte[] encodedKey = Base64.getDecoder().decode(publicKey.getBytes());
        
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            mPublicKey = keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * ECDSA-SHA256签名
     * @param data 需要签名的数据
     * @return 签名
     */
    private static byte[] sign(byte[] data) {
        try {
            Signature signer = Signature.getInstance("SHA256withECDSA");
            signer.initSign(mPrivateKey);
            signer.update(data);
            return signer.sign();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 验证ECDSA-SHA256签名
     * @param data 需要验证的数据原文
     * @param sig 需要验证的签名
     * @return true:验证成功 false:验证失败
     */
    public boolean verify(byte[] data, byte[] sig) {
        try {
            Signature signer = Signature.getInstance("SHA256withECDSA");
            signer.initVerify(mPublicKey);
            signer.update(data);
            return signer.verify(sig);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 用于url的base64encode
     * '+' => '*', '/' => '-', '=' => '_'
     * @param data 需要编码的数据
     * @return 编码后的base64数据
     */
    private static byte[] base64UrlEncode(byte[] data) {
        byte[] encode = Base64.getEncoder().encode(data);
        for (int i = 0; i < encode.length; i++) {
            if (encode[i] == '+') {
                encode[i] = '*';
            } else if (encode[i] == '/') {
                encode[i] = '-';
            } else if (encode[i] == '=') {
                encode[i] = '_';
            }
        }
        return encode;
    }
//    
//    /**
//     * 用于url的base64decode
//     * '*' => '+', '-' => '/', '_' => '='
//     * @param data 需要解码的数据
//     * @return 解码后的数据
//     */
//    private byte[] base64UrlDecode(byte[] data) {
//        byte[] encode = Arrays.copyOf(data, data.length);
//        for (int i = 0; i < encode.length; i++) {
//            if (encode[i] == '*') {
//                encode[i] = '+';
//            } else if (encode[i] == '-') {
//                encode[i] = '/';
//            } else if (encode[i] == '_') {
//                encode[i] = '=';
//            }
//        }
//        return encode;
//    }
//    
    /**
     * 生成userSig
     * @param userid 用户名
     * @param expire userSig有效期，建议为300秒
     * @return 生成的userSig
     */
    public static String genUserSig(String userid, int expire) {
    	
		setSdkAppid(Integer.valueOf(tencent_could_sdkAppId));
        setPrivateKey(tencent_private_key);
        setPublicKey(tencent_public_key);
    	
        String time = String.valueOf(System.currentTimeMillis()/1000);
        String serialString =
        "TLS.appid_at_3rd:" + 0 + "\n" +
        "TLS.account_type:" + 0 + "\n" +
        "TLS.identifier:" + userid + "\n" +
        "TLS.sdk_appid:" + mSdkAppid + "\n" +
        "TLS.time:" + time + "\n" +
        "TLS.expire_after:" + expire +"\n";
        
        byte[] signBytes = sign(serialString.getBytes(Charset.forName("UTF-8")));
        String sig = Base64.getEncoder().encodeToString(signBytes);
        
        String jsonString = "{"
        + "\"TLS.account_type\":\"" + 0 +"\","
        +"\"TLS.identifier\":\"" + userid +"\","
        +"\"TLS.appid_at_3rd\":\"" + 0 +"\","
        +"\"TLS.sdk_appid\":\"" + mSdkAppid +"\","
        +"\"TLS.expire_after\":\"" + expire +"\","
        +"\"TLS.sig\":\"" + sig +"\","
        +"\"TLS.time\":\"" + time +"\","
        +"\"TLS.version\": \"201512300000\""
        +"}";
        
        //compression
        Deflater compresser = new Deflater();
        compresser.setInput(jsonString.getBytes(Charset.forName("UTF-8")));
        
        compresser.finish();
        byte [] compressBytes = new byte [512];
        int compressBytesLength = compresser.deflate(compressBytes);
        compresser.end();
        String userSig = new String(base64UrlEncode(Arrays.copyOfRange(compressBytes, 0, compressBytesLength)));
        
        return userSig;
    }
    
    /**
     * 生成privMapEncrypt
     * @param userid 用户名
     * @param roomid 房间号
     * @param expire privMapEncrypt有效期，建议为300秒
     * @return 生成的privMapEncrypt
     */
    public static String genPrivMapEncrypt(String userid, int roomid, int expire) {
        String time = String.valueOf(System.currentTimeMillis()/1000);
        
        //视频校验位需要用到的字段
        /*
         cVer    unsigned char/1 版本号，填0
         wAccountLen unsigned short /2   第三方自己的帐号长度
         buffAccount wAccountLen 第三方自己的帐号字符
         dwSdkAppid  unsigned int/4  sdkappid
         dwRoomId    unsigned int/4  群组号码
         dwExpTime   unsigned int/4  过期时间 （当前时间 + 有效期（单位：秒，建议300秒））
         dwPrivilegeMap  unsigned int/4  权限位
         dwAccountType   unsigned int/4  第三方帐号类型
         */
        int accountLength = userid.length();
        int offset = 0;
        byte[] bytes = new byte[1+2+accountLength+4+4+4+4+4];
        bytes[offset++] = 0;
        bytes[offset++] = (byte)((accountLength & 0xFF00) >> 8);
        bytes[offset++] = (byte)(accountLength & 0x00FF);
        
        for (; offset < 3 + accountLength; ++offset) {
            bytes[offset] = (byte)userid.charAt(offset - 3);
        }
        bytes[offset++] = (byte)((mSdkAppid & 0xFF000000) >> 24);
        bytes[offset++] = (byte)((mSdkAppid & 0x00FF0000) >> 16);
        bytes[offset++] = (byte)((mSdkAppid & 0x0000FF00) >> 8);
        bytes[offset++] = (byte)(mSdkAppid & 0x000000FF);
        
        long nRoomId = Long.valueOf(roomid);
        bytes[offset++] = (byte)((nRoomId & 0xFF000000) >> 24);
        bytes[offset++] = (byte)((nRoomId & 0x00FF0000) >> 16);
        bytes[offset++] = (byte)((nRoomId & 0x0000FF00) >> 8);
        bytes[offset++] = (byte)(nRoomId & 0x000000FF);
        
        long expiredTime = Long.valueOf(time) + expire;
        bytes[offset++] = (byte)((expiredTime & 0xFF000000) >> 24);
        bytes[offset++] = (byte)((expiredTime & 0x00FF0000) >> 16);
        bytes[offset++] = (byte)((expiredTime & 0x0000FF00) >> 8);
        bytes[offset++] = (byte)(expiredTime & 0x000000FF);
        
        bytes[offset++] = (byte)((255 & 0xFF000000) >> 24);
        bytes[offset++] = (byte)((255 & 0x00FF0000) >> 16);
        bytes[offset++] = (byte)((255 & 0x0000FF00) >> 8);
        bytes[offset++] = (byte)(255 & 0x000000FF);
        
        bytes[offset++] = (byte)((0 & 0xFF000000) >> 24);
        bytes[offset++] = (byte)((0 & 0x00FF0000) >> 16);
        bytes[offset++] = (byte)((0 & 0x0000FF00) >> 8);
        bytes[offset++] = (byte)(0 & 0x000000FF);
        
        String userbuf = Base64.getEncoder().encodeToString(bytes);
        
        String serialString =
        "TLS.appid_at_3rd:" + 0 + "\n" +
        "TLS.account_type:" + 0 + "\n" +
        "TLS.identifier:" + userid + "\n" +
        "TLS.sdk_appid:" + mSdkAppid + "\n" +
        "TLS.time:" + time + "\n" +
        "TLS.expire_after:" + expire +"\n" +
        "TLS.userbuf:" + userbuf + "\n";
        
        byte[] signBytes = sign(serialString.getBytes(Charset.forName("UTF-8")));
        String sig = Base64.getEncoder().encodeToString(signBytes);
        
        String jsonString = "{"
        +"\"TLS.appid_at_3rd\":\"" + 0 +"\","
        +"\"TLS.account_type\":\"" + 0 +"\","
        +"\"TLS.identifier\":\"" + userid +"\","
        +"\"TLS.sdk_appid\":\"" + mSdkAppid +"\","
        +"\"TLS.expire_after\":\"" + expire +"\","
        +"\"TLS.sig\":\"" + sig +"\","
        +"\"TLS.time\":\"" + time +"\","
        +"\"TLS.userbuf\":\"" + userbuf +"\","
        +"\"TLS.version\": \"201512300000\""
        +"}";
        
        //compression
        Deflater compresser = new Deflater();
        compresser.setInput(jsonString.getBytes(Charset.forName("UTF-8")));
        
        compresser.finish();
        byte [] compressBytes = new byte [512];
        int compressBytesLength = compresser.deflate(compressBytes);
        compresser.end();
        String privMapEncrypt = new String(base64UrlEncode(Arrays.copyOfRange(compressBytes, 0, compressBytesLength)));
        
        return privMapEncrypt;
    }
    
    
    public static void main(String[] args) {
//        int sdkappid = Integer.valueOf(ConstantThirdParty.tencent_appid);   //腾讯云云通信sdkappid
//        int roomid = 1234;           //音视频房间号roomid
//        String userid = "webrtc98";  //用户名userid
        
//        File privateKeyFile = new File("E:\\com\\svnProject\\yizhi\\src\\private_key");
//        byte[] privateKey = new byte[(int)privateKeyFile.length()];
//        
//        File publicKeyFile = new File("E:\\com\\svnProject\\yizhi\\src\\public_key");
//        byte[] publicKey = new byte[(int)publicKeyFile.length()];
//        
//        try {
//            //读取私钥的内容
//            //PS:不要把私钥文件暴露到外网直接下载了哦
//            FileInputStream in1 = new FileInputStream(privateKeyFile);
//            in1.read(privateKey);
//            in1.close();
//            
//            //读取公钥的内容
//            FileInputStream in2 = new FileInputStream(publicKeyFile);
//            in2.read(publicKey);
//            in2.close();
//            
//        } catch (Exception e ) {
//            e.printStackTrace();
//        }
//        
//        setSdkAppid(sdkappid);
//        setPrivateKey(ConstantThirdParty.tencent_private_key);
//        setPublicKey(ConstantThirdParty.tencent_public_key);
        
      int roomid = 27;           //音视频房间号roomid
      String userid = "19";  //用户名userid
//      String userid = "小米";  //用户名userid
    	
        //生成userSig
        String userSig = genUserSig(userid, 15552000);//180*24*60*60
        
        //生成privMapEncrypt
        String privMapEncrypt = genPrivMapEncrypt(userid, roomid, 300);
        
        System.out.println("userSig:\n" + userSig);
        System.out.println("privMapEncrypt:\n" + privMapEncrypt);
    }
    
}

