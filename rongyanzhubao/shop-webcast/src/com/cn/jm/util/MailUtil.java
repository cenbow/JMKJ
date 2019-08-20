package com.cn.jm.util;

import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
//import java.util.Properties;
//import java.util.Scanner;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.util.MailSSLSocketFactory;

public class MailUtil {
	static Session session;
	static {
		Properties props = new Properties();
		// 开启debug调试  
		props.setProperty("mail.debug", "false");  
		// 发送服务器需要身份验证  
		props.setProperty("mail.smtp.auth", "true");  
		// 设置邮件服务器主机名  
		props.setProperty("mail.host", "smtp.qq.com");  
		// 发送邮件协议名称  
		props.setProperty("mail.transport.protocol", "smtp");  
		MailSSLSocketFactory sf;
		try {
			sf = new MailSSLSocketFactory();
			sf.setTrustAllHosts(true);  
			props.put("mail.smtp.ssl.enable", "true");  
			props.put("mail.smtp.ssl.socketFactory", sf); 
			session = Session.getInstance(props);  
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}  
	}
	public static void sendMessage(String title,String message) throws GeneralSecurityException, AddressException, MessagingException {
		//邮件内容部分  
		Message msg = new MimeMessage(session);  
		msg.setSubject(title);
		msg.setText(message);  
		//邮件发送者  
		msg.setFrom(new InternetAddress("824715645@qq.com"));  
		//发送邮件  
		Transport transport = session.getTransport();  
		transport.connect("smtp.qq.com", "824715645@qq.com", "eauxgztzhafzbccj");  
		transport.sendMessage(msg, new Address[] { new InternetAddress("824715645@qq.com") });  
		transport.close();  
	}
	
	//如果需要发送邮箱功能需要导入相关jar包
	public static void send(String phone,String code) throws Exception{
		StringBuilder builder = new StringBuilder();  
		builder.append("\n用户"+phone+"的验证码为 : "+code);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss:SSS");  
        String sendTime =formatter.format(new Date());  
		builder.append("\n发送时间为 : " + sendTime);  
		sendMessage("验证码", builder.toString());
	}
	
	public static final Pattern VALIDATION_PATTERN = Pattern.compile("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$");
	
	public static boolean check(String mail) {
		return VALIDATION_PATTERN.matcher(mail).matches();
	}
	
}
