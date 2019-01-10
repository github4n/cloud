package com.iot.file.storage.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.JsonUtil;
import com.iot.common.util.ToolUtil;
import com.iot.file.PropertyConfigureUtil;
import com.iot.file.contants.ModuleConstants;
import com.iot.file.entity.FileBean;
import com.iot.file.storage.IStorage;
import com.iot.redis.RedisCacheUtil;


/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：文件服务
 * 功能描述：阿里云存储实现类
 * 创建人： 李帅
 * 创建时间：2018年11月28日 下午2:26:08
 * 修改人：李帅
 * 修改时间：2018年11月28日 下午2:26:08
 */
public class OSSStorage implements IStorage {
	
	/**日志*/
	private static final Log LOGER = LogFactory.getLog(OSSStorage.class);
	
	/**阿里云-服务*/
	private static OSSClient ossClient;
	
	/**阿里云-Key*/
	private static String  accessKeyId;
	
	/**阿里云-Secret*/
	private static String  accessKeySecret;
	
	/**阿里云-所在数据中心地点*/
	private static String  endpoint;
	
	/**阿里云存储-桶名*/
	private static String  bucketName;
	
	/**
	 * 
	 * 构建函数连接测试
	 * @author 李帅
	 * @created 2018年11月28日 下午2:39:57
	 * @since
	 */
    public OSSStorage(){
    	connection();
    }
    
    private void connection() {
    	try {
    		accessKeyId=PropertyConfigureUtil.mapProps.get("accessKeyId").toString();
    		accessKeySecret=PropertyConfigureUtil.mapProps.get("accessKeySecret").toString();
    		endpoint=PropertyConfigureUtil.mapProps.get("endpoint").toString();
    		OSSStorage.ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    		//创建桶
    		bucketName = PropertyConfigureUtil.mapProps.get("bucketName").toString();
    	    createBucket(bucketName);
			LOGER.info(CommonUtil.getSystemLog("阿里云服务启动成功"));
		} catch (Exception e) {
			LOGER.error(CommonUtil.getSystemLog("阿里云服务启动失败"), e);
		}
		
	}

	/**
	 * 
	 * 描述：获取阿里云存储URL
	 * @author 李帅
	 * @created 2018年11月28日 下午2:40:38
	 * @since 
	 * @param tenantId
	 * @param fileType
	 * @return
	 * @throws Exception
	 */
	@Override
	public FileBean getPutUrl(Long tenantId, String fileType) throws Exception{
		//文件路径格式: 桶名/租户id/文件类型/文件名
		//文件名格式：32位uuid.文件类型
		Long id=RedisCacheUtil.incr(ModuleConstants.DB_TABLE_FILE_INFO,0L);
		String fileId=UUID.randomUUID().toString().replace("-","");
		String filePath=tenantId+ ModuleConstants.SPRIT+fileType+ModuleConstants.SPRIT+fileId + ModuleConstants.POINT+fileType;
		GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName,filePath, HttpMethod.PUT);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.HOUR_OF_DAY, ModuleConstants.expiration);
		urlRequest.setExpiration(calendar.getTime());
		urlRequest.setContentType(getContentType(fileType));
		URL url = OSSStorage.ossClient.generatePresignedUrl(urlRequest);
		FileBean fileBean=new FileBean();
		fileBean.setPresignedUrl(url.toString());
		fileBean.setFileId(fileId);
		fileBean.setId(id);
		fileBean.setFilePath(filePath);
		return fileBean;
	}

	/**
	 * 
	 * 描述：获取批量阿里云存储URL
	 * @author 李帅
	 * @created 2018年11月28日 下午2:40:56
	 * @since 
	 * @param tenantId
	 * @param fileType
	 * @param urlNum
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<FileBean> getPutUrl(Long tenantId, String fileType, Integer urlNum) throws Exception{
		List<FileBean> fileBeanList=new ArrayList<FileBean>();
		String prefix=tenantId+ModuleConstants.SPRIT+fileType+ModuleConstants.SPRIT;
		for(int i=0;i<urlNum;i++){
			Long id=RedisCacheUtil.incr(ModuleConstants.DB_TABLE_FILE_INFO,0L);
			String fileId=UUID.randomUUID().toString().replace("-","");
			String filePath=prefix + fileId + ModuleConstants.POINT+fileType;
			GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName,filePath, HttpMethod.PUT);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.HOUR_OF_DAY, ModuleConstants.expiration);
			urlRequest.setExpiration(calendar.getTime());
			urlRequest.setContentType(getContentType(fileType));
			URL url = OSSStorage.ossClient.generatePresignedUrl(urlRequest);
			FileBean fileBean=new FileBean();
			fileBean.setPresignedUrl(url.toString());
			fileBean.setId(id);
			fileBean.setFileId(fileId);
			fileBean.setFilePath(filePath);
			fileBeanList.add(fileBean);
		}
		return fileBeanList;
	}

	/**
	 * 
	 * 描述：Content-Type常用对照
	 * @author 李帅
	 * @created 2018年11月28日 下午6:22:56
	 * @since 
	 * @param fileType
	 * @return
	 */
	public static String getContentType(String fileType) {
		if (fileType.equalsIgnoreCase("ts")) {
			return "application/x-linguist";
		}
		if (fileType.equalsIgnoreCase("jpeg") || fileType.equalsIgnoreCase("jpg")
				|| fileType.equalsIgnoreCase("png")) {
			return "image/jpeg";
		}
		if (fileType.equalsIgnoreCase("bin")) {
			return "application/octet-stream";
		}
		if (fileType.equalsIgnoreCase("html")) {
			return "text/html";
		}
		if (fileType.equalsIgnoreCase("txt")) {
			return "text/plain";
		}
		if (fileType.equalsIgnoreCase("xml")) {
			return "text/xml";
		}
		if (fileType.equalsIgnoreCase("zip")) {
			return "application/zip";
		}
		if (fileType.equalsIgnoreCase("gif")) {
			return "image/gif";
		}
		return "image/jpeg";
	}
	/**
	 * 
	 * 描述：获取阿里云存储URL
	 * @author 李帅
	 * @created 2018年11月28日 下午2:41:25
	 * @since 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	@Override
	public FileBean getGetUrl(String filePath) throws Exception{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.HOUR_OF_DAY, ModuleConstants.expiration);
		URL url = ossClient.generatePresignedUrl(bucketName, filePath, calendar.getTime());
		FileBean fileBean=new FileBean();
		fileBean.setPresignedUrl(url.toString());
		return fileBean;
	}
  
	/**
	 * 
	 * 描述：获取阿里云存储URL
	 * @author 李帅
	 * @created 2018年11月28日 下午2:41:41
	 * @since 
	 * @param filePaths
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<FileBean> getGetUrl(List<FileBean> filePaths) throws Exception{
		List<FileBean> fileBeanList=new ArrayList<FileBean>();
		for(int i=0;i<filePaths.size();i++){
			LOGER.info(JsonUtil.toJson(filePaths));
//			GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName,filePaths.get(i).getFilePath(), HttpMethod.GET);
//			Calendar calendar = Calendar.getInstance();
//			calendar.setTime(new Date());
//			calendar.add(Calendar.HOUR_OF_DAY, ModuleConstants.expiration);
//			urlRequest.setExpiration(calendar.getTime());
//			URL url = OSSStorage.ossClient.generatePresignedUrl(urlRequest);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.HOUR_OF_DAY, ModuleConstants.expiration);
			URL url = ossClient.generatePresignedUrl(bucketName, filePaths.get(i).getFilePath(), calendar.getTime());
			FileBean fileBean=new FileBean();
			fileBean.setPresignedUrl(url.toString());
			fileBean.setFileId(filePaths.get(i).getFileId());
			fileBeanList.add(fileBean);
		}
		return fileBeanList;
	}

	/**
	 * 
	 * 描述：删除阿里云对象
	 * @author 李帅
	 * @created 2018年11月28日 下午2:41:52
	 * @since 
	 * @param filePath
	 * @throws Exception
	 */
	@Override
	public void deleteObject(String filePath) throws Exception{
		OSSStorage.ossClient.deleteObject(bucketName,filePath);
		
	}

	/**
	 * 
	 * 描述：批量删除阿里云对象
	 * @author 李帅
	 * @created 2018年11月28日 下午2:42:05
	 * @since 
	 * @param filePaths
	 * @throws Exception
	 */
	@Override
	public void deleteObject(List<String> filePaths) throws Exception{
		for(String filePath : filePaths){
			this.deleteObject(filePath);
		}
	}
	
	/**
	 * 
	 * 描述：创建桶
	 * @author 李帅
	 * @created 2018年11月28日 下午2:27:15
	 * @since 
	 * @param bucketName
	 * @throws Exception
	 */
	private static void createBucket(String bucketName) throws Exception {
		
		if (!ossClient.doesBucketExist(bucketName)) {
			OSSStorage.ossClient.createBucket(bucketName);
		}
	}

	/**
	 * 
	 * 描述：单个上传文件到阿里云
	 * @author 李帅
	 * @created 2018年11月28日 下午2:42:22
	 * @since 
	 * @param filePath
	 * @param file
	 * @param expiration
	 * @return
	 * @throws Exception
	 */
	@Override
	public String putObject(String filePath, File file, int expiration) throws Exception {
		String bucketName = PropertyConfigureUtil.mapProps.get("bucketName").toString();
		String key = null;
//		createBucket(bucketName);
		if (CommonUtil.isEmpty(file)) {
//			throw new Exception(S3ExceptionEnum.S3_FILE_IS_EMPTY);
		} else {
			key = filePath + '/' + ToolUtil.getUUID();
//			PutObjectResult result = OSSStorage.ossClient.putObject(bucketName, key, file);
			OSSStorage.ossClient.putObject(bucketName, key, file);
			if (expiration > 0) {
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(new Date());
				calendar.add(Calendar.MINUTE, expiration);
//				result.setExpirationTime(calendar.getTime());
			}
		}
		return key;
	}

	/**
	 * 
	 * 描述：单个上传文件到阿里云
	 * @author 李帅
	 * @created 2018年11月28日 下午2:42:36
	 * @since 
	 * @param filePath
	 * @param file
	 * @param fileType
	 * @return
	 * @throws Exception
	 */
	@Override
	public String putObject(String filePath, File file,String fileType) throws Exception {
		if (CommonUtil.isEmpty(file)) {
			return null;
		}
		String bucketName = PropertyConfigureUtil.mapProps.get("bucketName").toString();
		String key = filePath + ModuleConstants.SPRIT + ToolUtil.getUUID()+ModuleConstants.POINT+fileType;
		OSSStorage.ossClient.putObject(bucketName, key, file);
		return key;
	}


	/**
	 * 
	 * 描述：单个上传文件到阿里云
	 * @author 李帅
	 * @created 2018年11月28日 下午2:42:47
	 * @since 
	 * @param filePath
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@Override
	public String putObject(String filePath, File file)  throws Exception{
		if (CommonUtil.isEmpty(file)) {
			return null;
		}
		String bucketName = PropertyConfigureUtil.mapProps.get("bucketName").toString();
		OSSStorage.ossClient.putObject(bucketName, filePath, file);
		return filePath;
	}

	/**
	 * 
	 * 描述：
	 * @author 李帅
	 * @created 2018年11月28日 下午2:43:05
	 * @since 
	 * @param preFilePath
	 * @param fileType
	 * @return
	 * @throws Exception
	 */
	@Override
	public FileBean getUploadUrl(String preFilePath, String fileType) throws Exception {
		String fileId = ToolUtil.getUUID();
		String fileName = fileId + "." + fileType;
		String filePath = preFilePath + ModuleConstants.SPRIT + fileName;
		//生成put类型的url
		GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName,filePath, HttpMethod.PUT);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.HOUR_OF_DAY, ModuleConstants.expiration);
		urlRequest.setExpiration(calendar.getTime());
		urlRequest.setContentType(getContentType(fileType));
		URL url = OSSStorage.ossClient.generatePresignedUrl(urlRequest);
		FileBean fileBean=new FileBean();
		fileBean.setPresignedUrl(url.toString());
		fileBean.setFileId(fileId);
		fileBean.setFilePath(filePath);
		return fileBean;
	}

	@Override
	public String putObject(String preFilePath, String fileType, InputStream inputStream) throws Exception {
		String fileId=UUID.randomUUID().toString().replace("-","");
		String fileName =fileId + "." + fileType;
		String absolutePath = preFilePath + ModuleConstants.SPRIT + fileName;
		Date expiration1 = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
		PutObjectResult putObjectResult = ossClient.putObject(bucketName, absolutePath, inputStream);
		return absolutePath;
	}

	@Override
	public String putObject(String absolutePath, InputStream inputStream) throws Exception {
		try {
			Date expiration1 = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
			PutObjectResult putObjectResult = ossClient.putObject(bucketName, absolutePath, inputStream);
			return absolutePath;
		}  finally {
			this.closeInputStream(inputStream);
		}
	}

	/**
	  * @despriction：关闭输入流
	  * @author  yeshiyuan
	  * @created 2018/8/16 11:19
	  * @param null
	  * @return
	  */
	private void closeInputStream(InputStream inputStream){
		if (inputStream != null){
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public String getObjectContentMD5(String path) throws Exception {
		try {
			ObjectMetadata ossObject = ossClient.getObjectMetadata(bucketName, path);
			if(ossObject != null) {
				return ossObject.getETag().toLowerCase();
			}else {
				return null;
			}
			
		} catch (Exception e) {
			return null;
		}
	}
}
