package com.iot.file.storage.impl;

import com.iot.common.exception.BusinessException;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.SecurityUtil;
import com.iot.common.util.StringUtil;
import com.iot.common.util.ToolUtil;
import com.iot.file.PropertyConfigureUtil;
import com.iot.file.contants.ModuleConstants;
import com.iot.file.entity.FileBean;
import com.iot.file.storage.IStorage;
import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.util.*;

/**
 * 
 * 项目名称：IOT云平台
 * 模块名称：文件服务
 * 功能描述：微软云存储
 * 创建人： zhouzongwei
 * 创建时间：2018年3月16日 下午1:52:24
 * 修改人： zhouzongwei
 * 修改时间：2018年3月16日 下午1:52:24
 */
public class AzureBlobStorage implements IStorage {

	/**日志*/
	private static final Log LOGER = LogFactory.getLog(AzureBlobStorage.class);
	
	/**微软云存储-桶名*/
	private static String  bucketName;
	
	/**存储连接参数字符串*/
	private static String  storageConnectionString;
	
	/**账号名*/
	private static String  ccountName;
	
	/**账号key*/
	private static String  accountKey;
	
	/**存储资源url*/
	private static String  storageResourceUrl;
	
	private static CloudStorageAccount storageAccount;
	
	private static CloudBlobContainer cloudBlobContainer;
	
	
	
	/**
	 * 
	 * 构建函数
	 * @author zhouzongwei
	 * @created 2018年3月16日 下午1:56:31
	 * @since
	 */
	public AzureBlobStorage(){
    	connection();
    }
	
	/**
	 * 
	 * 描述：连接测试,创建桶
	 * @author zhouzongwei
	 * @created 2018年3月16日 下午1:56:37
	 * @since
	 */
	private void connection() {
		try {
			ccountName=PropertyConfigureUtil.mapProps.get("accountName").toString();
	        accountKey=PropertyConfigureUtil.mapProps.get("accountKey").toString();
	        bucketName=PropertyConfigureUtil.mapProps.get("bucketName").toString();
	        storageResourceUrl=PropertyConfigureUtil.mapProps.get("storageResourceUrl").toString();
			storageConnectionString="DefaultEndpointsProtocol=https;" + "AccountName="+ccountName+";"
					+ "AccountKey="+accountKey;
			//创建容器
			createBucket(bucketName);
			LOGER.info(CommonUtil.getSystemLog("Microsoft AzureStorage服务启动成功"));
		} catch (Exception e) {
			LOGER.error(CommonUtil.getSystemLog("Microsoft AzureStorage服务启动失败"),e);
		} 
	}


	/**
	 * 
	 * 描述：获取存储put的url
	 * @author zhouzongwei
	 * @created 2018年3月16日 下午2:35:48
	 * @since 
	 * @param tenantId
	 * @param fileType
	 * @return
	 * @throws Exception
	 */
	@Override
	public FileBean getPutUrl(Long tenantId, String fileType) throws Exception {
		// 文件路径格式: 容器/租户id/文件类型/文件名
		// 文件名格式：32位uuid.文件类型
		if(null == tenantId || StringUtil.isBlank(fileType)){
			return null;
		}
		String fileId=UUID.randomUUID().toString().replace("-","");
		String fileName =fileId + "." + fileType;
		String tenantIdPath = SecurityUtil.EncryptByAES(tenantId.toString(),ModuleConstants.AES_KEY);
		String filePath = tenantIdPath + ModuleConstants.SPRIT + fileType + ModuleConstants.SPRIT + fileName;
		FileBean fileBean = new FileBean();
		//生成put类型的url
	    String url=generatePutUrl(filePath);
		fileBean.setPresignedUrl(url);
		fileBean.setFileId(fileId);
		fileBean.setFilePath(filePath);
		fileBean.setFileId(fileId);
		return fileBean;
	}

	/**
	 * 
	 * 描述：批量获取存储put的url
	 * @author zhouzongwei
	 * @created 2018年3月16日 下午3:13:14
	 * @since 
	 * @param tenantId
	 * @param fileType
	 * @param urlNum
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<FileBean> getPutUrl(Long tenantId, String fileType, Integer urlNum) throws Exception {
		if(null == tenantId || StringUtil.isBlank(fileType) || null == urlNum){
			return null;
		}
		List<FileBean> fileBeanList=new ArrayList<FileBean>();
		String tenantIdPath = SecurityUtil.EncryptByAES(tenantId.toString(),ModuleConstants.AES_KEY);
		String prefix=tenantIdPath+ModuleConstants.SPRIT+fileType+ModuleConstants.SPRIT;
		for(int i=0;i<urlNum;i++){
			String fileId=UUID.randomUUID().toString().replace("-","");
			String fileName=fileId+"."+fileType;
			String filePath=prefix+fileName;
			String url=generatePutUrl(filePath);
			FileBean fileBean=new FileBean();
			fileBean.setPresignedUrl(url.toString());
			fileBean.setFileId(fileId);
			fileBean.setCreateTime(new Date());
			fileBean.setFilePath(filePath);
			fileBeanList.add(fileBean);
		}
		return fileBeanList;
	}

	/**
	 * 
	 * 描述：获取存储get的url
	 * @author zhouzongwei
	 * @created 2018年3月16日 下午3:13:10
	 * @since 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	@Override
	public FileBean getGetUrl(String filePath) throws Exception {
		FileBean fileBean=new FileBean();
		String url=generateGetUrl(filePath);
		fileBean.setPresignedUrl(url);
		fileBean.setCreateTime(new Date());
		return fileBean;
	}

	/**
	 * 
	 * 描述：批量获取存储get的url
	 * @author zhouzongwei
	 * @created 2018年3月16日 下午3:18:15
	 * @since 
	 * @param filePaths
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<FileBean> getGetUrl(List<FileBean> filePaths) throws Exception {
		List<FileBean> fileBeanList=new ArrayList<FileBean>();
		for(int i=0;i<filePaths.size();i++){
			FileBean fileBean=new FileBean();
			String url=generateGetUrl(filePaths.get(i).getFilePath());
			if (!StringUtil.isBlank(url)){
				fileBean.setPresignedUrl(url);
				fileBean.setFileId(filePaths.get(i).getFileId());
				fileBeanList.add(fileBean);
			}
		}
		return fileBeanList;
	}

	/**
	 * 
	 * 描述：删除对象
	 * @author zhouzongwei
	 * @created 2018年3月16日 下午3:22:08
	 * @since 
	 * @param filePath
	 * @throws Exception
	 */
	@Override
	public void deleteObject(String filePath) throws Exception {
		CloudBlockBlob cloudBlockBlob=cloudBlobContainer.getBlockBlobReference(filePath);
		if(cloudBlockBlob.deleteIfExists()){
			return;
		}

	}

	/**
	 * 
	 * 描述：批量删除对象
	 * @author zhouzongwei
	 * @created 2018年3月16日 下午3:22:00
	 * @since 
	 * @param filePaths
	 * @throws Exception
	 */
	@Override
	public void deleteObject(List<String> filePaths) throws Exception {
		for(String filePath : filePaths){
			this.deleteObject(filePath);
		}

	}

	/**
	 * 
	 * 描述：创建容器
	 * @author zhouzongwei
	 * @created 2018年3月16日 下午2:34:37
	 * @since 
	 * @param bucketName
	 * @throws Exception
	 */
	private void createBucket(String bucketName) throws Exception{
		storageAccount = CloudStorageAccount.parse(storageConnectionString);
		CloudBlobClient cloudBlobClient=storageAccount.createCloudBlobClient();
		cloudBlobContainer=cloudBlobClient.getContainerReference(bucketName);
		//创建容器
		if(cloudBlobContainer.createIfNotExists()){
			 LOGER.info(CommonUtil.getSystemLog("New Container created :"+bucketName));
		}
		
	}
	
	/**
	 * 
	 * 描述：生成put签名url
	 * @author zhouzongwei
	 * @created 2018年3月16日 下午2:51:20
	 * @since 
	 * @return
	 * @throws StorageException 
	 * @throws InvalidKeyException 
	 */
	private String generatePutUrl(String filePath) throws Exception {
		SharedAccessAccountPolicy sharedAccessAccountPolicy=new SharedAccessAccountPolicy();
		//账号服务类型
		Set<SharedAccessAccountService> serviceSet= new HashSet<SharedAccessAccountService>();
		serviceSet.add(SharedAccessAccountService.BLOB);
	    EnumSet<SharedAccessAccountService> enumServiceSet = EnumSet.copyOf(serviceSet);
		sharedAccessAccountPolicy.setServices(enumServiceSet);
		//账号权限类型
		Set<SharedAccessAccountPermissions> permissionSet= new HashSet<SharedAccessAccountPermissions>();
		permissionSet.add(SharedAccessAccountPermissions.WRITE);
	    EnumSet<SharedAccessAccountPermissions> enumPermissionSet = EnumSet.copyOf(permissionSet);
		sharedAccessAccountPolicy.setPermissions(enumPermissionSet);
		sharedAccessAccountPolicy.setProtocols(SharedAccessProtocols.HTTPS_ONLY);
		//账号服务级别
		Set<SharedAccessAccountResourceType> resourceTypeSet= new HashSet<SharedAccessAccountResourceType>();
		resourceTypeSet.add(SharedAccessAccountResourceType.OBJECT);
	    EnumSet<SharedAccessAccountResourceType> enumResourceTypesSet = EnumSet.copyOf(resourceTypeSet);
		sharedAccessAccountPolicy.setResourceTypes(enumResourceTypesSet);
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
		calendar.add(Calendar.HOUR_OF_DAY, ModuleConstants.expiration);
		sharedAccessAccountPolicy.setSharedAccessExpiryTime(calendar.getTime());
		String sasUrl=storageAccount.generateSharedAccessSignature(sharedAccessAccountPolicy);
		String url=storageResourceUrl+filePath+"?"+sasUrl;
		return url;
	}
	
	/**
	 * 
	 * 描述：生成get签名url
	 * @author zhouzongwei
	 * @created 2018年3月16日 下午3:04:43
	 * @since 
	 * @return
	 * @throws Exception
	 */
	private String generateGetUrl(String filePath) throws Exception {
		SharedAccessAccountPolicy sharedAccessAccountPolicy=new SharedAccessAccountPolicy();
		//账号服务类型
		Set<SharedAccessAccountService> serviceSet= new HashSet<SharedAccessAccountService>();
		serviceSet.add(SharedAccessAccountService.BLOB);
	    EnumSet<SharedAccessAccountService> enumServiceSet = EnumSet.copyOf(serviceSet);
		sharedAccessAccountPolicy.setServices(enumServiceSet);
		//账号权限类型
		Set<SharedAccessAccountPermissions> permissionSet= new HashSet<SharedAccessAccountPermissions>();
		permissionSet.add(SharedAccessAccountPermissions.READ);
	    EnumSet<SharedAccessAccountPermissions> enumPermissionSet = EnumSet.copyOf(permissionSet);
		sharedAccessAccountPolicy.setPermissions(enumPermissionSet);
		sharedAccessAccountPolicy.setProtocols(SharedAccessProtocols.HTTPS_ONLY);
		//账号服务级别
		Set<SharedAccessAccountResourceType> resourceTypeSet= new HashSet<SharedAccessAccountResourceType>();
		resourceTypeSet.add(SharedAccessAccountResourceType.OBJECT);
	    EnumSet<SharedAccessAccountResourceType> enumResourceTypesSet = EnumSet.copyOf(resourceTypeSet);
		sharedAccessAccountPolicy.setResourceTypes(enumResourceTypesSet);
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, ModuleConstants.expiration);
		sharedAccessAccountPolicy.setSharedAccessExpiryTime(calendar.getTime());
		String sasUrl=storageAccount.generateSharedAccessSignature(sharedAccessAccountPolicy);
		String url=storageResourceUrl+filePath+"?"+sasUrl;
		return url;
	}
	
	/**
	 * 
	 * 描述：单个上传文件到s3
	 * @author mao2080@sina.com
	 * @created 2017年4月11日 下午5:13:43
	 * @since 
	 * @param preFilePath 文件存储目录前缀
	 * @param file 文件对象
	 * @param expiration 失效时间 兼容S3
	 * @return 文件存储路径
	 * @throws BusinessException
	 */
	public String putObject(String preFilePath, File file, int expiration) throws Exception{
		if (CommonUtil.isEmpty(file)) {
			return null;
		}
		String absolutePath = preFilePath + ModuleConstants.SPRIT + file.getName();
		this.putObject(absolutePath, file);
		return absolutePath;
	}

	@Override
	public String putObject(String filePath, File file, String fileType) throws Exception {
		if (CommonUtil.isEmpty(file)) {
			return null;
		}
		String fileId=UUID.randomUUID().toString().replace("-","");
		String fileName =fileId + "." + fileType;
		String absolutePath = filePath + ModuleConstants.SPRIT + fileName;
		this.putObject(absolutePath, file);
		return absolutePath;
	}

	/**
	 * 描述：单个上传文件到s3
	 * @author 490485964@qq.com
	 * @date 2018/4/11 11:35
	 * @param absolutePath 文件完整路径，包含文件名（包含文件名 eg: 0/png/ac410b63761e4a8baceb7f6c5f0e9780.png）
	 * @param file 文件
	 * @return
	 */
	@Override
	public String putObject(String absolutePath, File file) throws Exception {
		if (CommonUtil.isEmpty(file)) {
			return null;
		}
		InputStream inputStream = new FileInputStream(file);
		this.putObject(absolutePath, inputStream);
		return absolutePath;
		/*
		try {
			CloudBlockBlob cloudBlockBlob = cloudBlobContainer.getBlockBlobReference(absolutePath);
			inputStream =
			cloudBlockBlob.upload(inputStream,file.length());
			return absolutePath;
		} finally {
			this.closeInputStream(inputStream);
		}*/
	}

	/**
	 * @despriction：获取上传url
	 * @author  yeshiyuan
	 * @created 2018/8/2 11:19
	 * @param preFilePath 文件路径前缀
	 * @param fileType 文件类型
	 * @return
	 */
	@Override
	public FileBean getUploadUrl(String preFilePath, String fileType) throws Exception {
		String fileId = ToolUtil.getUUID();
		String fileName = fileId + "." + fileType;
		String filePath = preFilePath + ModuleConstants.SPRIT + fileName;
		//生成put类型的url
		String url=generatePutUrl(filePath);
		FileBean fileBean = new FileBean(url, fileId, fileType, filePath);
		return fileBean;
	}

	/**
	 * @despriction：直接用流上传文件
	 * @author  yeshiyuan
	 * @created 2018/10/19 16:29
	 * @param preFilePath 文件路径前缀
	 * @param fileType 文件类型
	 * @return
	 */
	@Override
	public String putObject(String preFilePath, String fileType, InputStream inputStream) throws Exception {
		String fileId=UUID.randomUUID().toString().replace("-","");
		String fileName =fileId + "." + fileType;
		String absolutePath = preFilePath + ModuleConstants.SPRIT + fileName;
		this.putObject(absolutePath, inputStream);
		return absolutePath;
	}

	/**
	 * @despriction：直接用流上传文件
	 * @author  yeshiyuan
	 * @created 2018/10/19 16:11
	 * @param absolutePath 文件完整路径，包含文件名（包含文件名 eg: 0/png/ac410b63761e4a8baceb7f6c5f0e9780.png）
	 * @param inputStream 文件读取流
	 * @return
	 */
	@Override
	public String putObject(String absolutePath, InputStream inputStream) throws Exception {
		try {
			CloudBlockBlob cloudBlockBlob = cloudBlobContainer.getBlockBlobReference(absolutePath);
			cloudBlockBlob.upload(inputStream, inputStream.available());
			return absolutePath;
		}  finally {
			this.closeInputStream(inputStream);
		}
	}

	/**
	 * 描述：获取对象md5
	 * @author nongchongwei
	 * @date 2018/11/5 16:54
	 * @param
	 * @return
	 */
	@Override
	public String getObjectContentMD5(String path) throws Exception {
		CloudBlockBlob cloudBlockBlob = cloudBlobContainer.getBlockBlobReference(path);
		cloudBlockBlob.downloadAttributes();
		String azureMd5 = cloudBlockBlob.getProperties().getContentMD5();
		if(StringUtil.isEmpty(azureMd5)){
			return null;
		}
		byte[] binaryContent = new byte[0];
		try {
			binaryContent = new BASE64Decoder().decodeBuffer(azureMd5);
		} catch (Exception e) {
			LOGER.error("BASE64Decoder error->",e);
			return null;
		}
		String Md5 = CommonUtil.parseByte2HexStr(binaryContent);
		return  Md5;
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
}
