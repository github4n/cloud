package com.iot.file.storage.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.JsonUtil;
import com.iot.common.util.ToolUtil;
import com.iot.file.PropertyConfigureUtil;
import com.iot.file.contants.ModuleConstants;
import com.iot.file.entity.FileBean;
import com.iot.file.storage.IStorage;
import com.iot.file.util.FileUtil;
import com.iot.redis.RedisCacheUtil;
import com.iot.util.GetBigFileMD5;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.*;


/**
 * 
 * 项目名称：IOT云平台
 * 模块名称：文件服务
 * 功能描述：S3存储实现类
 * 创建人： zhouzongwei
 * 创建时间：2018年3月12日 下午3:19:43
 * 修改人： zhouzongwei
 * 修改时间：2018年3月12日 下午3:19:43
 */
public class S3Storage implements IStorage {
	
	/**日志*/
	private static final Log LOGER = LogFactory.getLog(S3Storage.class);
	
	/**S3-服务*/
	private static AmazonS3 S3;
	
	/**S3-桶名*/
	private static String  bucketName;
	
	/**
	 * 
	 * 描述：连接测试
	 * @author mao2080@sina.com
	 * @created 2017年4月11日 下午3:46:38
	 * @since 
	 * @param
	 * @throws BusinessException
	 */
    public S3Storage(){
    	connection();
    }
    
    private void connection() {
    	try {
			Regions regions = Regions.fromName(PropertyConfigureUtil.mapProps.get("aws.s3.region").toString());
			Region region=Region.getRegion(regions);
    		S3Storage.S3 = AmazonS3ClientBuilder.standard().withRegion(region.getName()).withCredentials(getProvider()).build();
    		//创建桶
    		bucketName = PropertyConfigureUtil.mapProps.get("bucketName").toString();
    	    createBucket(bucketName);
			LOGER.info(CommonUtil.getSystemLog("Amazon s3服务启动成功"));
		} catch (Exception e) {
			LOGER.error(CommonUtil.getSystemLog("Amazon s3服务启动失败"), e);
		}
		
	}

	/**
	 * 
	 * 描述：生成AWSStaticCredentialsProvider
	 * @author mao2080@sina.com
	 * @created 2017年4月11日 下午8:27:25
	 * @since 
	 * @return AWSStaticCredentialsProvider
	 * @throws BusinessException
	 */
	private AWSStaticCredentialsProvider getProvider() throws Exception{
		String keyid = PropertyConfigureUtil.mapProps.get("aws.accessKeyId").toString();
		String accesskey = PropertyConfigureUtil.mapProps.get("aws.secretKey").toString();
		AWSCredentials credentials = new BasicAWSCredentials(keyid, accesskey);
		return new AWSStaticCredentialsProvider(credentials);
	}
	
	/**
	 * 
	 * 描述：获取存储URL
	 * @author zhouzongwei
	 * @created 2018年3月13日 下午3:11:26
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
		URL url = S3Storage.S3.generatePresignedUrl(urlRequest);
		FileBean fileBean=new FileBean();
		fileBean.setPresignedUrl(url.toString());
		fileBean.setFileId(fileId);
		fileBean.setId(id);
		fileBean.setFilePath(filePath);
		return fileBean;
	}

	/**
	 * 
	 * 描述：获取批量存储URL
	 * @author zhouzongwei
	 * @created 2018年3月13日 下午3:13:39
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
			URL url = S3Storage.S3.generatePresignedUrl(urlRequest);
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
	 * 描述：获取存储URL
	 * @author zhouzongwei
	 * @created 2018年3月13日 下午3:47:39
	 * @since 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	@Override
	public FileBean getGetUrl(String filePath) throws Exception{
		GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName,filePath, HttpMethod.GET);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.HOUR_OF_DAY, ModuleConstants.expiration);
		urlRequest.setExpiration(calendar.getTime());
		URL url = S3Storage.S3.generatePresignedUrl(urlRequest);
		FileBean fileBean=new FileBean();
		fileBean.setPresignedUrl(url.toString());
		return fileBean;
	}
  
	/**
	 * 
	 * 描述：获取存储URL
	 * @author zhouzongwei
	 * @created 2018年3月13日 下午3:48:45
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
			GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName,filePaths.get(i).getFilePath(), HttpMethod.GET);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.HOUR_OF_DAY, ModuleConstants.expiration);
			urlRequest.setExpiration(calendar.getTime());
			URL url = S3Storage.S3.generatePresignedUrl(urlRequest);
			FileBean fileBean=new FileBean();
			fileBean.setPresignedUrl(url.toString());
			fileBean.setFileId(filePaths.get(i).getFileId());
			fileBeanList.add(fileBean);
		}
		return fileBeanList;
	}

	/**
	 * 
	 * 描述：删除对象
	 * @author zhouzongwei
	 * @created 2018年3月13日 下午3:58:49
	 * @since 
	 * @param filePath
	 * @throws Exception
	 */
	@Override
	public void deleteObject(String filePath) throws Exception{
		S3Storage.S3.deleteObject(bucketName,filePath);
		
	}

	/**
	 * 
	 * 描述：批量删除对象
	 * @author zhouzongwei
	 * @created 2018年3月13日 下午3:59:00
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
	 * @author zhouzongwei
	 * @created 2018年3月13日 下午3:23:12
	 * @since 
	 * @param bucketName
	 * @throws Exception
	 */
	private static void createBucket(String bucketName) throws Exception {
		
		if (!S3Storage.S3.doesBucketExist(bucketName)) {
			S3Storage.S3.createBucket(bucketName);
		}
	}

	/**
	 * 
	 * 描述：单个上传文件到s3
	 * @author 李帅
	 * @created 2018年3月29日 下午3:16:42
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
			PutObjectResult result = S3Storage.S3.putObject(bucketName, key, file);
			if (expiration > 0) {
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(new Date());
				calendar.add(Calendar.MINUTE, expiration);
				result.setExpirationTime(calendar.getTime());
			}
		}
		return key;
	}

	/**
	 * 描述：单个上传文件到s3
	 * @author 490485964@qq.com
	 * @date 2018/4/11 11:35
	 * @param
	 * @return
	 */
	@Override
	public String putObject(String filePath, File file,String fileType) throws Exception {
		if (CommonUtil.isEmpty(file)) {
			return null;
		}
		String bucketName = PropertyConfigureUtil.mapProps.get("bucketName").toString();
		String key = filePath + ModuleConstants.SPRIT + ToolUtil.getUUID()+ModuleConstants.POINT+fileType;
		S3Storage.S3.putObject(bucketName, key, file);
		return key;
	}


	/**
	 * 描述：单个上传文件到s3
	 * @author 490485964@qq.com
	 * @date 2018/4/11 11:35
	 * @param
	 * @return
	 */
	@Override
	public String putObject(String filePath, File file)  throws Exception{
		if (CommonUtil.isEmpty(file)) {
			return null;
		}
		String bucketName = PropertyConfigureUtil.mapProps.get("bucketName").toString();
		S3Storage.S3.putObject(bucketName, filePath, file);
		return filePath;
	}

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
		URL url = S3Storage.S3.generatePresignedUrl(urlRequest);
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
		return this.putObject(absolutePath, inputStream);
	}

	@Override
	public String putObject(String absolutePath, InputStream inputStream) throws Exception {
		S3.putObject(bucketName, absolutePath, inputStream, null);
		return absolutePath;
	}

	@Override
	public String getObjectContentMD5(String path) throws Exception {
		GetObjectMetadataRequest getObjectMetadataRequest = new GetObjectMetadataRequest(bucketName,path);
		String md5 = S3Storage.S3.getObjectMetadata(getObjectMetadataRequest).getETag();
		return md5;
	}
}
