package com.iot.file.service.impl;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.iot.common.exception.BusinessException;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.SecurityUtil;
import com.iot.common.util.StringUtil;
import com.iot.exception.BusinessExceptionEnum;
import com.iot.file.cache.FileRedisUtil;
import com.iot.file.contants.ModuleConstants;
import com.iot.file.dao.FileMapper;
import com.iot.file.dto.FileDto;
import com.iot.file.entity.FileBean;
import com.iot.file.queue.FileInfoExcutor;
import com.iot.file.queue.FileInfoProducer;
import com.iot.file.service.FileService;
import com.iot.file.storage.IStorage;
import com.iot.file.storage.StorageFactory;
import com.iot.file.util.FileUtil;
import com.iot.file.vo.FileInfoRedisVo;
import com.iot.file.vo.FileInfoResp;
import com.iot.file.vo.NeedReturnFileInfoReq;
import com.iot.redis.RedisCacheUtil;
import com.iot.util.GetBigFileMD5;

@Service("fileService")
@DependsOn({"file_init"})
public class FileServiceImpl implements FileService {


	/**日志*/
	private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

	private IStorage storage;

	@Autowired
	private FileMapper fileMapper;

	private static ExecutorService executorService = Executors.newCachedThreadPool();

	@Value("${fileStorage.mapProps.expiration}")
	private Integer expiraHour;

	/**
	 * 描述：默认构造
	 * @author mao2080@sina.com
	 * @created 2018/3/26 14:43
	 * @param
	 * @return
	 */
	@PostConstruct
	public void init(){
		try {
			logger.info("get IStorage");
			this.storage = StorageFactory.createStorage();
			logger.info("storage:{}",storage);
		} catch (Exception e) {
			logger.error("FileServiceImpl init error:",e);
		}
	}



	/**
	 *
	 * 描述：获取单个预签名Put 类型的Url
	 * @author zhouzongwei
	 * @created 2018年3月9日 下午3:07:27
	 * @since
	 * @param tenantId:租户id
	 * @param fileType：文件类型
	 * @return
	 */
	@Override
	public FileDto getPutUrl(Long tenantId, String fileType) throws BusinessException{
		if(tenantId == null){
			throw new BusinessException(BusinessExceptionEnum.TENANTID_ISNULL);
		}
		if(StringUtil.isBlank(fileType)){
			throw new BusinessException(BusinessExceptionEnum.FILETYPE_ISNULL);
		}
		try {
			FileBean fileBean = this.storage.getPutUrl(tenantId, fileType);
			//异步存入到数据库
			List<FileBean> fileBeanList = Arrays.asList(fileBean);
			FileInfoExcutor.addToQueue(fileBeanList,tenantId,fileType);
			return new FileDto(fileBean.getPresignedUrl(),fileBean.getFileId(), fileBean.getFileId()+"."+fileType);
		} catch (Exception e) {
			logger.error("FileServiceImpl getPutUrl error",e);
			throw new BusinessException(BusinessExceptionEnum.GET_PUT_URL_ERROR);
		}
	}

	/**
	 *
	 * 描述：获取多个预签名Put 类型的Url
	 * @author zhouzongwei
	 * @created 2018年3月13日 下午4:26:58
	 * @since
	 * @param tenantId
	 * @param fileType
	 * @param urlNum
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<FileDto> getPutUrl(Long tenantId, String fileType, Integer urlNum) throws BusinessException{
		if(tenantId == null){
			throw new BusinessException(BusinessExceptionEnum.TENANTID_ISNULL);
		}
		if(StringUtil.isBlank(fileType)){
			throw new BusinessException(BusinessExceptionEnum.FILETYPE_ISNULL);
		}
		if(urlNum < 1){
			throw new BusinessException(BusinessExceptionEnum.FILE_URL_NUM_INCORRECT);
		}
		try {
			List<FileBean> fileBeanList = this.storage.getPutUrl(tenantId, fileType, urlNum);
			//异步存入到数据库
			FileInfoExcutor.addToQueue(fileBeanList,tenantId,fileType);
			List<FileDto> fileDtoList = new ArrayList<>();
			for(FileBean fileBean:fileBeanList){
				FileDto fileDto = new FileDto(fileBean.getPresignedUrl(),fileBean.getFileId(), "");
				fileDtoList.add(fileDto);
			}
			return fileDtoList;
		} catch (Exception e) {
			logger.error("FileServiceImpl getPutUrl error",e);
			throw new BusinessException(BusinessExceptionEnum.BATCH_GET_PUT_URL_ERROR);
		}
	}

	/**
	 *
	 * 描述：获取单个预签名get 类型的Url
	 * @author zhouzongwei
	 * @created 2018年3月13日 下午4:29:22
	 * @since
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	@Override
	public FileDto getGetUrl(String fileId) throws BusinessException{
		if(StringUtil.isBlank(fileId)){
			throw new BusinessException(BusinessExceptionEnum.FILEID_ISNULL);
		}
		try {
			String url = FileRedisUtil.getUrlFromRedis(fileId);
			if (StringUtil.isBlank(url)){
				String filePath = this.fileMapper.getFilePath(fileId);
				if(StringUtil.isBlank(filePath)){
					//throw new BusinessException(BusinessExceptionEnum.FILE_RELATIONSHIP_NOT_FOUND);
					return null;
				}
				url = this.storage.getGetUrl(filePath).getPresignedUrl();
				//把url放入缓存
				FileRedisUtil.setUrlToRedis(fileId,url, (expiraHour - 1) * 3600L);
			}
			return new FileDto(url, fileId, "");
		}  catch (Exception e) {
			logger.error("FileServiceImpl getGetUrl error",e);
			throw new BusinessException(BusinessExceptionEnum.GET_GET_URL_ERROR);
		}
	}

	/**
	 *
	 * 描述：获取多个预签名get 类型的Url
	 * @author zhouzongwei
	 * @created 2018年3月13日 下午4:29:22
	 * @since
	 * @param fileIds
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, String> getGetUrl(List<String> fileIds) throws BusinessException{
		if(fileIds == null || fileIds.isEmpty()){
			return Collections.emptyMap();
		}
		try {
			Map<String, String> fileMap = new HashMap<>();
			//未在缓存里待获取url的文件ids
			List<String> waitGetUrlFileIds = new ArrayList<>();
			//在reidis查找文件对应的url，为空的再筛入待获取队列，进行查询
			List<String> urls = FileRedisUtil.batchGetUrlFromRedis(fileIds);
			if (urls!=null && !urls.isEmpty()){
				for (int i = 0; i < urls.size(); i++) {
					if (StringUtil.isBlank(urls.get(i))){
						waitGetUrlFileIds.add(fileIds.get(i));
					}else{
						fileMap.put(fileIds.get(i),urls.get(i));
					}
				}
			}
			if (waitGetUrlFileIds.size()>0){
				Map<String,String> newUrlMap = new HashMap<>(); //新获取的url
				List<FileBean> filePaths = this.fileMapper.getFileInfoList(waitGetUrlFileIds);
				List<FileBean>  fileBeanList = this.storage.getGetUrl(filePaths);
				fileBeanList.forEach(bean ->{
					fileMap.put(bean.getFileId(), bean.getPresignedUrl());
					newUrlMap.put(bean.getFileId(), bean.getPresignedUrl());
				});
				//异步存进redis里
				executorService.execute(new Runnable() {
					@Override
					public void run() {
						//存进redis里
						FileRedisUtil.batchSetUrlToRedis(newUrlMap,(expiraHour - 1) * 3600L);
					}
				});
			}
			return fileMap;
		} catch (Exception e) {
			logger.error("FileServiceImpl getGetUrl error",e);
			throw new BusinessException(BusinessExceptionEnum.BATCH_GET_GET_URL_ERROR);
		}
	}

	/**
	 *
	 * 描述：删除单个对象
	 * @author zhouzongwei
	 * @created 2018年3月13日 下午4:31:03
	 * @since
	 * @param fileId
	 * @throws Exception
	 */
	@Override
	public int deleteObject(String fileId) throws BusinessException{
		if(StringUtil.isBlank(fileId)){
			return ModuleConstants.SUCCESS;
		}
		CompletableFuture.runAsync(()->{
			String filePath = this.fileMapper.getFilePath(fileId);
			if(StringUtil.isBlank(filePath)){
				throw new BusinessException(BusinessExceptionEnum.FILE_RELATIONSHIP_NOT_FOUND);
			}
			try {
				this.storage.deleteObject(filePath);
			} catch (Exception e) {
				logger.error("FileServiceImpl deleteObject error",e);
			}
			this.fileMapper.delete(fileId);
		});
		return ModuleConstants.SUCCESS;

	}
	/**
	 * 描述：获取md5值
	 * @author nongchongwei
	 * @date 2018/11/5 17:55
	 * @param
	 * @return
	 */
	@Override
	public String getObjectContentMD5(String fileId) throws BusinessException {
		if(StringUtil.isBlank(fileId)){
			return null;
		}
		String filePath = this.fileMapper.getFilePath(fileId);
		if(StringUtil.isBlank(filePath)){
			return null;
		}
		String md5 = null;
		try {
			md5 = this.storage.getObjectContentMD5(filePath);
		} catch (Exception e) {
			logger.error("FileServiceImpl getObjectContentMD5 error",e);
			e.printStackTrace();
			throw new BusinessException(BusinessExceptionEnum.GET_MD5_ERROR);
		}
		return md5;
	}


	/**
	 *
	 * 描述：删除多个对象
	 * @author zhouzongwei
	 * @created 2018年3月13日 下午4:31:32
	 * @since
	 * @param fileIds
	 * @throws Exception
	 */
	@Override
	public int deleteObject(List<String> fileIds) throws BusinessException{
		if(fileIds == null || fileIds.isEmpty()){
			return ModuleConstants.SUCCESS;
		}
		List<List<String>> lapseEventIdListList = null;
		try {
			if (fileIds.size() > 500) {
				lapseEventIdListList = CommonUtil.dealBySubList(fileIds, 500);
				for(List<String> lapseEventIdList : lapseEventIdListList){
					List<FileBean> filePaths = this.fileMapper.getFileInfoList(lapseEventIdList);
					for (FileBean bean : filePaths){
						this.storage.deleteObject(bean.getFilePath());
					}
					this.fileMapper.batchDelete(lapseEventIdList);
				}
			}else{
				List<FileBean> filePaths = this.fileMapper.getFileInfoList(fileIds);
				for (FileBean bean : filePaths){
					this.storage.deleteObject(bean.getFilePath());
				}
				this.fileMapper.batchDelete(fileIds);
			}

			return ModuleConstants.SUCCESS;
		} catch (Exception e) {
			logger.error("FileServiceImpl deleteObject error",e);
			throw new BusinessException(BusinessExceptionEnum.BATCH_DELETE_FILE_ERROR);
		}
	}

	@Override
	public int deleteObjectByPath(String path) throws BusinessException {
		if(StringUtil.isBlank(path)){
			return ModuleConstants.SUCCESS;
		}
		try {
			this.storage.deleteObject(path);
			return ModuleConstants.SUCCESS;
		} catch (Exception e) {
			logger.error("FileServiceImpl deleteObjectByPath error",e);
			throw new BusinessException(BusinessExceptionEnum.DELETE_FILE_ERROR);
		}
	}

	@Override
	public int deleteObjectByPath(List<String> pathList) throws BusinessException {
		if(pathList == null || pathList.isEmpty()){
			return ModuleConstants.SUCCESS;
		}
		try {
			for (String path : pathList){
				this.storage.deleteObject(path);
			}
			return ModuleConstants.SUCCESS;
		} catch (Exception e) {
			logger.error("FileServiceImpl deleteObjectByPath error",e);
			throw new BusinessException(BusinessExceptionEnum.BATCH_DELETE_FILE_ERROR);
		}
	}

	/**
	 * @despriction：描述 上传文件，保存至S3服务器
	 * @author  yeshiyuan
	 * @created 2018/4/20 9:26
	 * @param multipartFile 文件
	 * @param tenantId 租户id
	 * @return 返回fileId
	 */
	public String upLoadFile(MultipartFile multipartFile, Long tenantId) throws BusinessException {
		NeedReturnFileInfoReq req = new NeedReturnFileInfoReq();
		FileInfoResp fileInfoResp = this.uploadFile(multipartFile,tenantId, req);
		return fileInfoResp.getFileId();
	}

	/**
	 * @despriction：修改文件
	 * @author  yeshiyuan
	 * @created 2018/4/24 10:37
	 * @param file 文件
	 * @param fileId 文件uuid
	 * @param filePath 文件路径（可不传）
	 * @return
	 */
	@Override
	public void updateFile(MultipartFile multipartFile, String fileId,String filePath) throws BusinessException {
		if (CommonUtil.isEmpty(multipartFile) || StringUtil.isEmpty(fileId)){
			throw new BusinessException(BusinessExceptionEnum.VIDEO_UPDATEFILE_ERROR);
		}
		try {
			if (StringUtil.isBlank(filePath)){
				filePath = fileMapper.getFilePath(fileId);
			}
			this.storage.putObject(filePath, multipartFile.getInputStream());
			String fileName = multipartFile.getOriginalFilename();
			fileMapper.updateFileUpdateTime(fileId, new Date(), fileName);
		} catch (Exception e) {
			logger.error("修改文件出错:",e);
			throw new  BusinessException(BusinessExceptionEnum.VIDEO_UPDATEFILE_ERROR);
		}
	}

	/**
	 * @despriction：通过文件uuid获取文件信息
	 * @author  yeshiyuan
	 * @created 2018/4/24 10:54
	 * @param fileId 文件uuid
	 * @return
	 */
	@Override
	public FileBean getFileInfoByFileId(String fileId) {
		if (StringUtil.isEmpty(fileId)){
			throw new BusinessException(BusinessExceptionEnum.FILEID_ISNULL);
		}
		return fileMapper.getFileInfoByFileId(fileId);
	}

	/**
	 * @despriction：从redis中获取fileId进而删除s3和数据库的数据
	 * @author  yeshiyuan
	 * @created 2018/6/5 14:40
	 * @param redisTaskId
	 * @return
	 */
	@Override
	public void deleteFileByRedisTaskId(String redisTaskId) {
		Set<String> fileIds = RedisCacheUtil.setGetAll(redisTaskId,String.class,false);
		if (fileIds != null && !fileIds.isEmpty()){
			List<List<String>> lists = new ArrayList<>();
			if (fileIds.size() > ModuleConstants.batchDelNum){
				lists = CommonUtil.dealBySubList(new ArrayList<>(fileIds), ModuleConstants.batchDelNum);
			}else{
				lists.add(new ArrayList<>(fileIds));
			}
			List<FutureTask> futureTasks = new ArrayList<>();
			for (List<String> fileIdList : lists) {
				FutureTask<Integer> futureTask = new FutureTask<>(new Callable<Integer>() {
					@Override
					public Integer call() throws Exception {
						try{
							List<FileBean> filePaths = fileMapper.getFileInfoList(fileIdList);
							if (filePaths!=null && filePaths.size()>0){
								List<Long> ids = new ArrayList<>();
								for (FileBean bean : filePaths){
									storage.deleteObject(bean.getFilePath());
									ids.add(bean.getId());
								}
								//删除文件id对应的url缓存
								FileRedisUtil.batchDelUrlFromRedis(fileIdList);
								return fileMapper.batchDeleteByIds(ids);
							}else {
								return 0;
							}
						}catch (Exception e){
							logger.error("File service deleteFileByRedisTaskId -> FutureTask error : ",e);
							return -1;
						}
					}
				});
				futureTasks.add(futureTask);
				executorService.submit(futureTask);
			}
			//异步校验结果是否全部执行成功
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					boolean successFlag = true;
					for (FutureTask<Integer> futureTask: futureTasks) {
						try {
							boolean noCompleteFlag = true;
							while (noCompleteFlag){
								if (futureTask.isDone()){
									int code = futureTask.get();
									noCompleteFlag = false;
									if (code == -1){
										successFlag = false;
										break;
									}
								}
							}
						} catch (InterruptedException e) {
							logger.error("校验线程删除文件结果异常",e);
						} catch (ExecutionException e) {
							logger.error("校验线程删除文件结果异常",e);
						}
					}
					if (successFlag){
						RedisCacheUtil.delete(redisTaskId);
						logger.info("--------------- deleteFileByRedisTaskId ： {} run success",redisTaskId);
					}else{
						logger.info("--------------- deleteFileByRedisTaskId ： {} run fail",redisTaskId);
					}
				}
			});
		}
	}

	/**
	 * @despriction：文件上传并获取到文件的MD5
	 * @author  yeshiyuan
	 * @created 2018/8/3 20:03
	 * @param null
	 * @return
	 */
	@Override
	public FileInfoResp upLoadFileAndGetMd5(MultipartFile multipartFile, Long tenantId) {
		return uploadFile(multipartFile, tenantId, new NeedReturnFileInfoReq(true,false));
	}

	/**
	 * @despriction：上传文件，并根据参数返回相关字段
	 * @author  yeshiyuan
	 * @created 2018/8/3 20:15
	 * @param null
	 * @return
	 */
	public FileInfoResp uploadFile(MultipartFile multipartFile, Long tenantId, NeedReturnFileInfoReq needReq){
		return this.uploadFile(multipartFile, tenantId, needReq, true);
	}

	/**
	 * @despriction：上传文件，并根据参数返回相关字段(文件信息是否需要保存至数据库)
	 * @author  yeshiyuan
	 * @created 2018/8/3 20:15
	 * @param saveInfoToDb false的话保存至redis，有效期过后文件未上报的话会被删除；true的话直接保存至db
	 * @return
	 */
	@Override
	public FileInfoResp uploadFile(MultipartFile multipartFile, Long tenantId, NeedReturnFileInfoReq needReq, boolean saveInfoToDb) {
		//文件路径格式: 桶名/租户id/文件类型/文件名
		if(CommonUtil.isEmpty(multipartFile)){
			throw new BusinessException(BusinessExceptionEnum.VIDEO_UPLOADFILE_ERROR);
		}
		String s3key = null;
		String fileId, md5 = null, url = null;
		String fileName = multipartFile.getOriginalFilename();
		String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
		String tenantIdPath = SecurityUtil.EncryptByAES(tenantId.toString(), ModuleConstants.AES_KEY);
		String preFilePath = tenantIdPath + ModuleConstants.SPRIT + fileType;
		try {
			if (needReq.isNeedMd5()){
				md5 = GetBigFileMD5.getMD5(multipartFile.getInputStream());
			}
			//返回key格式：1000/ts/a1b9a1479d724fb6a275baa994fdacb8.ts    ->   租户id/文件类型/fileId.文件后缀
			s3key = this.storage.putObject(preFilePath, fileType, multipartFile.getInputStream());
			if (needReq.isNeedUrl()){
				url = this.storage.getGetUrl(s3key).getPresignedUrl();
			}
			FileBean fileBean = FileUtil.createFileBean(s3key,fileType,tenantId);
			fileBean.setFileName(fileName);
			fileId = fileBean.getFileId();
			if (saveInfoToDb) {
				//文件信息保存至数据库（推送至队列，由线程去读取存储）
				FileInfoProducer.addToQueue(fileBean);
			} else {
				//设置过期时间比url的有效时间多一个小时，主要是为了给定时任务扫描微软云的，校验此文件是否有上传并上报信息
				FileRedisUtil.saveFileInfoToRedis(s3key, fileId, fileType, tenantId, (expiraHour + 1) * 3600L);
			}
		} catch (Exception e) {
			logger.error("FileServiceImpl uploadFile error:", e);
			throw new  BusinessException(BusinessExceptionEnum.VIDEO_UPLOADFILE_ERROR);
		}
		return new FileInfoResp(fileId, url, md5);
	}

	/**
	 * @despriction：查找文件路径
	 * @author  yeshiyuan
	 * @created 2018/8/21 14:47
	 * @param null
	 * @return
	 */
	@Override
	public Map<String, String> batchGetFilePath(List<String> fileIds) {
		List<FileBean> fileBeans = fileMapper.getFileInfoList(fileIds);
		Map<String, String> map = new HashMap<>();
		fileBeans.forEach(o -> {
			map.put(o.getFileId(), o.getFilePath());
		});
		return map;
	}

	/**
	 * @despriction：把redis中的文件信息保存至数据库
	 * @author  yeshiyuan
	 * @created 2018/10/9 19:10
	 * @return
	 */
	@Override
	public void saveFileInfoToDb(String fileId) {
		FileInfoRedisVo fileInfoRedisVo = FileRedisUtil.getFileInfo(fileId);
		if (fileInfoRedisVo != null) {
			FileBean fileBean = new FileBean(fileId, fileInfoRedisVo.getFileType(), fileInfoRedisVo.getTenantId(), fileInfoRedisVo.getCreateTime(), fileInfoRedisVo.getFilePath());
			fileMapper.insertFileInfo(fileBean);
			//把redis中的缓存文件信息删除
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:HH");
			FileRedisUtil.deleteFileInfo(fileId, dateFormat.format(fileInfoRedisVo.getCreateTime()));
		}
	}

	/**
	 * @despriction：批量把redis中的文件信息保存至数据库
	 * @author  yeshiyuan
	 * @created 2018/10/9 19:10
	 * @return
	 */
	@Override
	public void saveFileInfosToDb(List<String> fileIds) {
		List<FileBean> fileBeans = new ArrayList<>();
		fileIds.forEach(fileId -> {
			FileInfoRedisVo fileInfoRedisVo = FileRedisUtil.getFileInfo(fileId);
			if (fileInfoRedisVo != null) {
				FileBean fileBean = new FileBean(fileId, fileInfoRedisVo.getFileType(), fileInfoRedisVo.getTenantId(), fileInfoRedisVo.getCreateTime(), fileInfoRedisVo.getFilePath());
				fileBeans.add(fileBean);
				//把redis中的缓存文件信息删除
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:HH");
				FileRedisUtil.deleteFileInfo(fileId, dateFormat.format(fileInfoRedisVo.getCreateTime()));
			}
		});
		if (!fileBeans.isEmpty()) {
			fileMapper.insertFileInfos(fileBeans);
		}
	}

	/**
	 * @despriction：获取下载验证码（有效期单位秒）
	 * @author  yeshiyuan
	 * @created 2018/11/28 18:01
	 */
	@Override
	public String getDownloadCode(Integer second) {
		return FileRedisUtil.getDownloadCode(second);
	}

	/**
	 * @despriction：检验下载验证码是否过期
	 * @author  yeshiyuan
	 * @created 2018/11/28 19:11
	 */
	@Override
	public boolean checkDownloadCode(String uuid) {
		return FileRedisUtil.checkDownloadCode(uuid);
	}
}