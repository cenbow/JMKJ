package com.cn.jm.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;

public class OrderUtil {
	
	public static String createOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSS", Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);
		Random r = new Random();
		int x=r.nextInt(999999-100000+1)+100000;
		key = key + x;
		return key;
	}
	

	 /**
		 * 随机指定范围内N个不重复的数
		 * 利用HashSet的特征，只能存放不同的值
		 * @param min 指定范围最小值
		 * @param max 指定范围最大值
		 * @param n 随机数个数
		 * @param HashSet<Integer> set 随机数结果集
		 */
	    public static void randomSet(int min, int max, int n, HashSet<Integer> set) {
	        if (n > (max - min + 1) || max < min) {
	            return;
	        }
	        for (int i = 0; i < n; i++) {
	            // 调用Math.random()方法
	            int num = (int) (Math.random() * (max - min)) + min;
	            set.add(num);// 将不同的数存入HashSet中
	        }
	        int setSize = set.size();
	        // 如果存入的数小于指定生成的个数，则调用递归再生成剩余个数的随机数，如此循环，直到达到指定大小
	        if (setSize < n) {
	        	randomSet(min, max, n - setSize, set);// 递归
	        }
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
}
