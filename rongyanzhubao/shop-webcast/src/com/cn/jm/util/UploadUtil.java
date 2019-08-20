package com.cn.jm.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.cn.jm.core.tool.ToolDateTime;
import com.cn.jm.core.tool.ToolUtils;
import com.cn.jm.information.BasicsInformation;
import com.jfinal.core.Controller;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.upload.UploadFile;

import net.coobird.thumbnailator.Thumbnails;

/**
 * 1.0 : 创建可以应付功能的上传文件工具类 1.1 : 优化一部分上传到本地文件的代码(将视频上传和图片上传统一成一个方法,文件名称更换为 [传入的
 * path + 雪花生成的数字 + 文件类型] 防止访问量大时造成的文件出现重复问题) 减少1.0的130行代码
 * 
 * @author Administrator
 *
 */
public class UploadUtil {

	public static final String OSS_URI = "";
	/** 上传图片的路径 */
	public static final String UPLOAD_IMG_PATH = "image/";
	/** 上传视频的路径 */
	private static final String UPLOAD_VIDEO_PATH = "video/";
	/** 根据url上传文件的路径 */
	public static final String UPLOAD_URL_FILE_URI = "file/";
	/** 视频截图的路径 */
	public static final String FFMPEG_IMG_PATH = "ffmpeg/";
	public static final String UPLOAD_TEMP = "/temp/";
	public static final String ENDPOINT;
	public static final String BUCKET;
	private static final Random RANDOM_NUMBER_GENERATOR = new Random();
	public static final String ACCESS_KEY_ID;
	public static final String ACCESS_KEY_SECRET;
	private static final DefaultCredentialProvider DEFAULT_CREDENTIAL_PROVIDER;
	public static ClientConfiguration config = new ClientConfiguration();

	public static final String BASEPATH = PathKit.getWebRootPath() + "/upload/";
	static {
		ENDPOINT = PropKit.get("oss_endpoint");
		BUCKET = PropKit.get("oss_bucket");
		ACCESS_KEY_ID = PropKit.get("oss_accessKeyId");
		ACCESS_KEY_SECRET = PropKit.get("oss_accessKeySecret");
		DEFAULT_CREDENTIAL_PROVIDER = new DefaultCredentialProvider(
				ACCESS_KEY_ID, ACCESS_KEY_SECRET);
	}
	/**
	 * 生成新的文件,之后把传入的文件内容赋予新的文件中,之后把传入的文件删除
	 * 
	 * @param upload
	 *            需要转换传入的文件
	 * @param path
	 *            路径需求前缀加上 / 例如:/image
	 * @return 返回用 ip + 文件路径 的字符串,开启服务器可以直接访问
	 */
	private static String uploadFile(File uploadFile, String path) {
		if (uploadFile == null) {
			return null;
		}
		final String nowDate = TimeUtil.formatStr(new Date());
		File folder = new File(BasicsInformation.UPLOAD_FILE_PATH + path + "/" + nowDate);
		if (!folder.exists()) {// 判断当天的文件夹是否生成,如果未生成则生成
			folder.mkdirs();
		}
		// 获取图片名称和图片类型
		final String uploadFileName = uploadFile.getName();
		final String fileType = uploadFileName.substring(uploadFileName.lastIndexOf('.'));
		// 文件名称组合 : 传入的 path + 雪花生成的数字 + 文件类型
		File file = new File(folder.toString() + "/" + SnowflakeUtil.UTIL.nextId() + fileType);
		// 创建OSSClient实例。
		OSSClient ossClient = null;
		String resultFilePath = file.getPath().replace("\\", "/").replace(BasicsInformation.UPLOAD_FILE_PATH, "");
		try {
			ossClient = new OSSClient("https://" + ENDPOINT, DEFAULT_CREDENTIAL_PROVIDER, config);
			// 复制图片内容
//			uploadFile.renameTo(file);
			ossClient.putObject(BUCKET, resultFilePath, uploadFile);
			// 删除传入的图片
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			uploadFile.delete();
			if (ossClient != null)
				ossClient.shutdown();
		}

		// 由于文件的路径会存在 D:\\ 的情况,需要转换成 D://
//		return file.getPath().replace("\\", "/").replace(BasicsInformation.UPLOAD_FILE_PATH, BasicsInformation.FILE_URI);
		return "https://" + BUCKET + "." + ENDPOINT + "/" + resultFilePath;
	}

	/**
	 * 生成新的文件,之后把传入的文件内容赋予新的文件中,之后把传入的文件删除
	 * 
	 * @param upload
	 *            需要转换传入的文件
	 * @param path
	 *            路径需求前缀加上 / 例如:/image
	 * @return 返回用 ip + 文件路径 的字符串,开启服务器可以直接访问
	 */
	private static ArrayList<String> uploadFileList(List<File> fileList, String path) {
		if (fileList == null || fileList.size() == 0) {
			return null;
		}
		ArrayList<String> resultList = new ArrayList<>();
		final String nowDate = TimeUtil.formatStr(new Date());
		File folder = new File(BasicsInformation.UPLOAD_FILE_PATH + path + "/" + nowDate);
		if (!folder.exists()) {// 判断当天的文件夹是否生成,如果未生成则生成
			folder.mkdirs();
		}
		OSSClient ossClient = null;
		// 创建OSSClient实例。
		try {
			ossClient = new OSSClient("https://" + ENDPOINT, DEFAULT_CREDENTIAL_PROVIDER, config);
			// 获取图片名称和图片类型
			for (File uploadFile : fileList) {
				final String uploadFileName = uploadFile.getName();
				final String fileType = uploadFileName.substring(uploadFileName.lastIndexOf('.'));
				// 文件名称组合 : 传入的 path + 雪花生成的数字 + 文件类型
				File file = new File(folder.toString() + "/" + SnowflakeUtil.UTIL.nextId() + fileType);
				String resultFilePath = file.getPath().replace("\\", "/").replace(BasicsInformation.UPLOAD_FILE_PATH, "");
				ossClient.putObject(BUCKET, resultFilePath, uploadFile);
				uploadFile.delete();
				resultList.add("https://" + BUCKET + "." + ENDPOINT + "/" + resultFilePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ossClient != null)
				ossClient.shutdown();
		}
		// 由于文件的路径会存在 D:\\ 的情况,需要转换成 D://
//		return file.getPath().replace("\\", "/").replace(BasicsInformation.UPLOAD_FILE_PATH, BasicsInformation.FILE_URI);
		return resultList;
	}

	/**
	 * 上传图片
	 * 
	 * @param controller
	 * @param parameterName
	 *            图片的参数名称
	 * @param path
	 *            图片指定路径 ,传入值例如 /image 注意加入斜杆
	 * @return
	 */
	public static String uploadImg(Controller controller, String parameterName, String path) {
		UploadFile upload = controller.getFile(parameterName, UPLOAD_IMG_PATH);
		if (upload == null) {
			return null;
		}
		return uploadFile(upload.getFile(), path);
	}

	// /**
	// * 上传视频并且截取第一张图片
	// *
	// * @param uploadFile
	// * @param path
	// * 上传文件的储存位置
	// * @return List<String> 第一个数据为视频地址,第二个数据为视频截图地址
	// */
	// public static List<String> uploadVideo(File uploadFile, String path) {
	// if (uploadFile == null) {
	// return null;
	// }
	// List<String> list = new ArrayList<>();
	//
	// String nowDate = TimeUtil.formatStr(new Date());
	// File file = new File(UPLOAD_FILE_PATH + UPLOAD_VIDEO_PATH + nowDate + path);
	// if (!file.exists()) {
	// file.mkdirs();
	// }
	// String uploadFileName = uploadFile.getName();
	// File to = new File(file.toString() + "/video_" + System.currentTimeMillis()
	// + uploadFileName.substring(uploadFileName.indexOf(".")));
	// // 如果文件不存在则新增
	// if (!to.getParentFile().exists()) {
	// to.getParentFile().mkdirs();
	// }
	// try {
	// uploadFile.renameTo(to);
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// list.add(FILE_URI + UPLOAD_VIDEO_PATH + nowDate + path + "/" + to.getName());
	// list.add(FfmpegUtil.printScreen(UPLOAD_VIDEO_PATH + nowDate + path + "/" +
	// to.getName()));
	// uploadFile.delete();
	// return list;
	// }

	/**
	 * 上传多张图片
	 * 
	 * @param controller
	 * @param parameterName
	 *            图片的参数名称,注意名称不可以重复
	 * @param path
	 *            图片指定路径 ,传入值例如 /image/
	 * @return
	 */
	public static List<String> uploadImgs(Controller controller, String path) {
		List<UploadFile> files = controller.getFiles(UPLOAD_IMG_PATH);
		if (files.size() == 0) {
			return null;
		}
		List<String> imageList = new ArrayList<>();
		// 生成当前时间
		String nowDate = TimeUtil.formatStr(new Date());
		int i = 1;// 为每一张图片加一个识别,防止图片名称重复
		File file = new File(BasicsInformation.UPLOAD_FILE_PATH + UPLOAD_IMG_PATH + nowDate + path);
		if (!file.exists()) {// 如果文件夹不存在则创建
			file.mkdirs();
		}
		for (UploadFile upload : files) {
			File to = new File(file.toString() + "/image_" + i + System.currentTimeMillis() + ".jpg");
			i++;
			if (upload != null) {
				File f = upload.getFile();
				if (f.exists()) {
					try {
						Thumbnails.of(f.getAbsolutePath()).scale(1f).outputQuality(0.2f).outputFormat("jpg").toFile(to);
					} catch (Exception e) {
						continue;
					}
					f.delete();
					imageList.add(BasicsInformation.FILE_URI + UPLOAD_IMG_PATH + nowDate + path + "/" + to.getName());
				}
			}
		}

		return imageList;
	}

	/**
	 * 生成新的文件,之后把传入的文件内容赋予新的文件中,之后把传入的文件删除
	 * 
	 * @param upload
	 *            需要转换传入的文件
	 * @param path
	 *            路径需求前缀加上 / 例如:/image
	 * @param names
	 *            从控制器获取的图片name值
	 * @return 返回用 ip + 文件路径 的字符串,开启服务器可以直接访问
	 */
	public static ArrayList<String> uploadImgsByNames(Controller controller, String path, String[] names) {
		ArrayList<File> fileList = new ArrayList<>();
		if (names != null && names.length != 0) {
			for (String name : names) {
				UploadFile uploadFile = controller.getFile(name);
				if (uploadFile != null) {
					fileList.add(uploadFile.getFile());
				}
			}
		}
		return uploadFileList(fileList, path);
	}

	/**
	 * 上传视频
	 * 
	 * @param controller
	 * @param parameterName
	 * @return
	 */
	public static String uploadVideo(Controller controller, String parameterName) {
		UploadFile upload = controller.getFile(parameterName, UPLOAD_VIDEO_PATH);
		if (upload == null) {
			return null;
		}
		return uploadFile(upload.getFile(), "/video");
	}

	/**
	 * 上传视频
	 * 
	 * @param controller
	 * @param parameterName
	 *            传入图片的参数字段
	 * @param path
	 *            视频指定路径 ,传入值例如 /video
	 * @return
	 */
	public static String uploadVideo(Controller controller, String parameterName, String path) {
		UploadFile upload = controller.getFile(parameterName, UPLOAD_VIDEO_PATH);
		if (upload == null) {
			return null;
		}
		return uploadFile(upload.getFile(), path);
	}

	/**
	 * 上传文件类型
	 * 
	 * @param f
	 * @param path
	 * @return
	 */
	public static String uploadImageFile(File f, String path) {
		if (f == null) {
			return null;
		}
		return uploadFile(f, path);
	}

	/**
	 * 文件所属的url
	 * 
	 * @param url
	 * @param path
	 *            储存文件分区名称
	 * @return
	 */
	public static String uploadUrl(String url, String path) {
		if (url == null || url.equals("")) {
			return null;
		}

		String fileType = url.substring(url.lastIndexOf("."));
		String nowDate = TimeUtil.formatStr(new Date());
		File to = new File(BasicsInformation.UPLOAD_FILE_PATH + UPLOAD_URL_FILE_URI + nowDate + "/" + path + "/file_"
				+ System.currentTimeMillis() + fileType);
		// 如果文件不存在则新增
		if (!to.getParentFile().exists()) {
			to.getParentFile().mkdirs();
		}
		try {
			URL httpurl = new URL(url);
			FileUtils.copyURLToFile(httpurl, to);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return BasicsInformation.FILE_URI + UPLOAD_URL_FILE_URI + nowDate + "/" + path + "/" + to.getName();
	}

	/**
	 * 文件所属的url生成固定尾缀的jpg图片
	 * 
	 * @param url
	 * @return
	 */
	public static String uploadUrlGenerateJpg(String url, String path) {
		if (url == null || url.equals("")) {
			return null;
		}
		String nowDate = TimeUtil.formatStr(new Date());
		File to = new File(BasicsInformation.UPLOAD_FILE_PATH + UPLOAD_URL_FILE_URI + nowDate + "/" + path + "/file_"
				+ System.currentTimeMillis() + ".jpg");
		// 如果文件不存在则新增
		if (!to.getParentFile().exists()) {
			to.getParentFile().mkdirs();
		}
		try {
			URL httpurl = new URL(url);
			FileUtils.copyURLToFile(httpurl, to);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return BasicsInformation.FILE_URI + UPLOAD_URL_FILE_URI + nowDate + "/" + path + "/" + to.getName();
	}

	/**
	 * 批量上传多个文件
	 *
	 * @return
	 */
	public List<File> ossUploads(Controller controller) {
		List<UploadFile> listUploadFiles = controller.getFiles(PathKit.getWebRootPath() + "/upload");
		List<File> listFiles = new ArrayList<File>();
		for (UploadFile file : listUploadFiles) {
			File changeFile = new File(PathKit.getWebRootPath() + "/upload/"
					+ ToolDateTime.format(new Date(), "yyyyMMddHHmmss") + "_" + ToolUtils.getUuidByJdk(true) + ".jpg");
			if (file != null) {
				File source = file.getFile();
				source.renameTo(changeFile);
				listFiles.add(source);
			}
		}
		return listFiles;
	}

	public static void ossDownload(String urlString, String filename, String savePath) throws Exception {
		// 构造URL
		URL url = new URL(urlString);
		// 打开连接
		URLConnection con = url.openConnection();
		// 设置请求超时为5s
		con.setConnectTimeout(5 * 1000);
		// 输入流
		InputStream is = con.getInputStream();

		// 1K的数据缓冲
		byte[] bs = new byte[1024];
		// 读取到的数据长度
		int len;
		// 输出的文件流
		File sf = new File(savePath);
		if (!sf.exists()) {
			sf.mkdirs();
		}
		OutputStream os = new FileOutputStream(sf.getPath() + "\\" + filename);
		// 开始读取
		while ((len = is.read(bs)) != -1) {
			os.write(bs, 0, len);
		}
		// 完毕，关闭所有链接
		os.close();
		is.close();
	}

	public static String ossFileUpload(Controller controller, String param, String path) {
		UploadFile f = controller.getFile(param);
		if (f == null) {
			return null;
		}
		String webRoot = PathKit.getWebRootPath();
		String saveDir = JFinal.me().getConstants().getBaseUploadPath();
		String dateFormat = TimeUtil.formatStr(new Date());
		StringBuilder newFileName = new StringBuilder("/upload");
		if (StrKit.notBlank(saveDir)) {
			newFileName.append("/").append(path);
		}
		newFileName.append("/").append(dateFormat).append("/");
		StringBuilder fileName = new StringBuilder().append(System.currentTimeMillis()).append("_")
				.append(RANDOM_NUMBER_GENERATOR.nextInt(999999)).append(getFileExt(f.getFileName()));
		newFileName.append(fileName);
		File dest = new File(webRoot + newFileName.toString());
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		File source = f.getFile();
		source.renameTo(dest);

		// 创建OSSClient实例。
		try {
			uploadFileToOss(dest);
			return newFileName.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			f.getFile().delete();
			source.delete();
		}

	}

	/**
	 * 进行图片上传,但是不进行名字更换
	 * 
	 * @param f
	 * @param name
	 * @param path
	 * @return
	 */
	public static String ossImageUpload(File f, String name, String path) {
		String webRoot = PathKit.getWebRootPath();
		File dest = new File(webRoot + name);
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		f.renameTo(dest);
		// 创建OSSClient实例。
		try {
			uploadFileToOss(dest);
			return OSS_URI + name;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			f.delete();
		}

	}

	/**
	 * 进行图片上传,进行图片名称生成,只生成jpg类型
	 * 
	 * @param f
	 * @param path
	 *            前后都需要加上斜杆 /path/
	 * @return
	 */
	public static String ossImageUpload(File f, String path) {
		String webRoot = PathKit.getWebRootPath();
		String nowDate = TimeUtil.formatStr(new Date());
		String name = "/upload" + path + nowDate + "/" + System.currentTimeMillis() + ".jpg";
		File dest = new File(webRoot + name);
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		f.renameTo(dest);
		// 创建OSSClient实例。
		try {
			uploadFileToOss(dest);
			return OSS_URI + name;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			f.delete();
		}
		return null;
	}

	/**
	 * 上传文件到oss服务器中
	 */
	private static void uploadFilesToOss(List<File> fileList) {
		// 创建OSSClient实例。
		OSSClient ossClient = null;
		try {
			ossClient = new OSSClient(ENDPOINT, DEFAULT_CREDENTIAL_PROVIDER, null);
			for (File file : fileList) {
				String name = file.getName();
				// 上传文件。<yourLocalFile>由本地文件路径加文件名包括后缀组成，例如/users/local/myfile.txt。
				ossClient.putObject(BUCKET, name.substring(1, name.toString().length()), file);
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		} finally {
			// 关闭OSSClient。
			if (ossClient != null)
				ossClient.shutdown();
		}
	}

	private static void uploadFileToOss(File file) {
		uploadFilesToOss(Arrays.asList(file));
	}

	public static String getFileExt(String fileName) {
		return fileName.substring(fileName.lastIndexOf('.'), fileName.length());
	}

	public static List<String> ossFileUploads(Controller controller, String path) {
		List<UploadFile> files = controller.getFiles();
		if (files == null) {
			return null;
		}
		String webRoot = PathKit.getWebRootPath();
		String saveDir = JFinal.me().getConstants().getBaseUploadPath();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Random r = new Random();
		List<String> fileNameList = new ArrayList<>();
		// 创建OSSClient实例。
		OSSClient ossClient = null;
		try {
			ossClient = new OSSClient(ENDPOINT, DEFAULT_CREDENTIAL_PROVIDER, null);
			for (UploadFile f : files) {
				StringBuilder newFileName = new StringBuilder("/upload");
				if (StrKit.notBlank(saveDir)) {
					newFileName.append("/").append(path);
				}

				int x = r.nextInt(999999 - 100000 + 1) + 100000;
				newFileName.append("/").append(dateFormat.format(new Date())).append("/");
				StringBuilder fileName = new StringBuilder().append(System.currentTimeMillis() + "_" + x)
						.append(getFileExt(f.getFileName()));
				newFileName.append(fileName);
				File dest = new File(webRoot + newFileName.toString());
				if (!dest.getParentFile().exists()) {
					dest.getParentFile().mkdirs();
				}
				File source = f.getFile();
				source.renameTo(dest);

				// 上传文件。<yourLocalFile>由本地文件路径加文件名包括后缀组成，例如/users/local/myfile.txt。
				// System.out.println(source.getPath());
				ossClient.putObject("zhaoxinim", newFileName.toString().substring(1, newFileName.toString().length()),
						dest);
				fileNameList.add(newFileName.toString());
				f.getFile().delete();
				source.delete();
				dest.delete();
			}
			return fileNameList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			// 关闭OSSClient。
			if (ossClient != null)
				ossClient.shutdown();
		}
	}

	public static String ossGetFileContent(File f) {
		// 将文件的内容读取出来
		StringBuilder result = new StringBuilder();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(f));
			// 构造一个BufferedReader类来读取文件
			String s = null;
			while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
				result.append(System.lineSeparator() + s);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 将文件删除
		f.delete();
		return result.toString();
	}

	public static List<String> batchUploadImg(int length, Controller controller, String param, String path) {
		List<String> list = new ArrayList<>(length);
		for (int i = 1; i <= length; ++i) {
			String image = uploadImg(controller, param + i, path);
			if (image != null) {
				list.add(image);
			}
		}
		return list;
	}

	public static String uploadOSSImg(Controller controller, String param, String path) {

		File file = new File(BASEPATH + UPLOAD_IMG_PATH);

		if (!file.exists()) {
			file.mkdirs();
		}

		File to = new File(file.toString() + "/image_" + System.currentTimeMillis() + ".jpg");

		UploadFile upload = controller.getFile(param, UPLOAD_TEMP);

		// 创建OSSClient实例。
		OSSClient ossClient = null;

		if (upload != null && upload.getFile() != null) {

			try {

				Thumbnails.of(upload.getFile().getAbsolutePath()).scale(1f).outputQuality(1f).outputFormat("jpg")
						.toFile(to);

				CredentialsProvider credentialsProvider = new DefaultCredentialProvider(ACCESS_KEY_ID,
						ACCESS_KEY_SECRET);

				ossClient = new OSSClient("https://" + ENDPOINT, credentialsProvider, config);

				ossClient.putObject(BUCKET, path + "/" + to.getName(), upload.getFile());

				return "https://" + BUCKET + "." + ENDPOINT + "/" + path + "/" + to.getName();

			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} finally {
				if (ossClient != null)
					ossClient.shutdown();
				upload.getFile().delete();
				to.delete();
			}

		} else
			return null;
	}

	/**
	 * 将带逗号的字符串转化数组返回,如果没有值则返回空数组
	 * @param strImages
	 * @return 
	 */
	public static String[] changeImages(String strImages) {
		if(StrKit.isBlank(strImages)) {
			return new String[0];
		}
		return strImages.split(",");
	}

}
