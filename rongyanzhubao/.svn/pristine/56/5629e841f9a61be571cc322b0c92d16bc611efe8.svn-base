package com.cn;

import com.alibaba.druid.util.StringUtils;
import com.cn.jm.web.core.router.JMRouterMapping;
import com.cn.jm.web.core.parse.DefaultMdParse;
import com.cn.jm.web.core.parse.MdTemplate;
import com.cn.jm.web.core.parse.ParentAnnotationConverUrl;

public class DomGen {
	
	/**
	 * 存放的文件路径
	 */
	static String path = System.getProperty("user.dir") + "\\webapp";
	/**
	 * 存放的文件名称
	 */
	static String fileName = "temp.md";

	public static void main(String[] args) {
		DefaultMdParse m = new DefaultMdParse();
		// Demo m = new Demo();
		MdTemplate template = new MdTemplate();
		template.setIp("10.0.0.250");
		template.setPort("8181");
		template.setExportPath(path + "\\" + fileName);
//		template.setExportPath("D:\\workspace\\temp.md");
		
		template.setTemplatePath(System.getProperty("user.dir") + "\\resources\\md2.txt");
		template.setParentAnnotationConverUrl(new ParentAnnotationConverUrl() {

			@Override
			public String conver(Class<?> clazz) {
				if (!clazz.isAnnotationPresent(JMRouterMapping.class))
					return null;

				String url = clazz.getAnnotation(JMRouterMapping.class).url();
				return StringUtils.isEmpty(url) ? null : url;
			}

		});
		template.setPagePath(new String[] {"com.cn.jm.controller.base.api", "com.cn.jm.controller.shop.api", "com.cn.jm.controller.webcast.api"});
		m.preParse(template);
		template.getExportPath();
		try {
			/**
			 * 强制生成html文档
			 */
//			creaet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public static void creaet() throws Exception {
		Process p = Runtime.getRuntime().exec("cmd /c start pushd " + path);
		p.waitFor();
		p.destroy();
		System.out.println("复制下面链接,输入之后生成文档");
		System.out.println("i5ting_toc -f #fileName -o ".replace("#fileName",fileName));
	}
}
