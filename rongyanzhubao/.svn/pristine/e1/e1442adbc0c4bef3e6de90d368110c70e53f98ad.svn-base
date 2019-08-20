package com.cn.jm.util;

import java.io.File;
import java.util.Date;
import java.util.Hashtable;

import com.cn.jm.core.tool.ToolDateTime;
import com.cn.jm.core.tool.ToolUtils;
import com.cn.jm.information.BasicsInformation;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

/**
 * 二维码工具包
 * @author Administrator
 *
 */
public class QRCodeUtil {
	private static int width = 500;
	private static int height = 500;
	private static String format = "png";

	/**
	 * 生成二维码
	 * @param url
	 * @param qrCodePath 图片上传路径的相对路径
	 * @return
	 */
	public static File getQRCode(String countentUrl,String qrCodePath){
		File countentUrlFile = new File(countentUrl);
		if(!countentUrlFile.exists()) {
			countentUrlFile.mkdirs();
		}
		
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		BitMatrix bitMatrix;
		try {
			bitMatrix = new MultiFormatWriter().encode(countentUrl,BarcodeFormat.QR_CODE, width, height, hints);
			StringBuffer url = new StringBuffer();
			String name = ToolDateTime.format(new Date(), "yyyyMMddHHmmss")+"_"+ToolUtils.getUuidByJdk(true)+".jpg";
			url.append(BasicsInformation.UPLOAD_FILE_PATH).append(qrCodePath);
			File file = new File(url.toString());
			if(!file.exists()) {
				file.mkdir();
			}
			url.append(name);
			MatrixToImageWriter.writeToPath(bitMatrix, format,new File(url.toString()).toPath());

			return new File(BasicsInformation.UPLOAD_FILE_PATH + qrCodePath + name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
