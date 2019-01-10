package com.iot.device.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.common.util.CSVUtils;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.StringUtil;
import com.iot.common.util.ToolUtil;
import com.iot.common.util.ZipUtil;
import com.iot.device.core.utils.BeanCopyUtils;
import com.iot.device.core.utils.certencrypt.EncryptionFiles;
import com.iot.device.dto.UUIDInfoDto;
import com.iot.device.dto.UUIDOrderDto;
import com.iot.device.exception.DeviceExceptionEnum;
import com.iot.device.exception.UUIDManegerExceptionEnum;
import com.iot.device.mapper.LicenseUsageMapper;
import com.iot.device.mapper.UUIDManegerMapper;
import com.iot.device.model.CustUuidManage;
import com.iot.device.model.DeviceExtend;
import com.iot.device.model.LicenseUsage;
import com.iot.device.model.UUidApplyRecord;
import com.iot.device.service.IUUIDManegerService;
import com.iot.device.vo.LicenseUsageVo;
import com.iot.device.vo.req.LicenseUsageReq;
import com.iot.device.vo.req.uuid.GenerateUUID;
import com.iot.device.vo.req.uuid.GetUUIDOrderReq;
import com.iot.device.vo.req.uuid.GetUUIDReq;
import com.iot.device.vo.req.uuid.UUIDRefundOperateReq;
import com.iot.device.vo.req.uuid.UUIDRefundReq;
import com.iot.device.vo.req.uuid.UUidApplyRecordInfo;
import com.iot.file.api.FileApi;
import com.iot.file.api.FileUploadApi;
import com.iot.file.dto.FileDto;
import com.iot.file.util.FileUtil;
import com.iot.mqttploy.api.MqttPloyApi;
import com.iot.mqttploy.entity.PloyInfo;
import com.iot.redis.RedisCacheUtil;
import com.iot.tenant.api.TenantApi;
import com.iot.tenant.vo.resp.TenantInfoResp;

@Service("UUIDManegerService")
@Transactional
public class UUIDManegerServiceImpl implements IUUIDManegerService {

	public static final String splitStr = ":";
	public static final String pwdStr = "pwd";
	public static final String compensateFailKey = "UUID:compensateFail";
	/**日志*/
	private static final Logger LOGER = LoggerFactory.getLogger(UUIDManegerServiceImpl.class);
	/**
	 * 通用策略名称
	 */
	private static final String IPC_UUIDTYPE = "IPC";
	private final int DeleteNum = 900;
	private final int uuidBigNum = 10000;
	private final int uuidMediumNum = 600;
	private final int uuidBigThreadNum = 16;
	private final int uuidMediumThreadNum = 4;
	@Autowired
	UUIDManegerMapper uuidManegerMapper;
	@Autowired
	FileApi fileApi;
	@Autowired
	TenantApi tenantApi;
	@Autowired
	MqttPloyApi mqttPloyApi;
	@Autowired
	LicenseUsageMapper licenseUsageMapper;
	@Value("${uuid.host}")
	private String host;
	@Value("${uuid.shPath}")
	private String shPath;
	@Value("${uuid.certPath}")
	private String certPath;
	@Value("${uuid.certMD5}")
	private String certMD5;

	@Autowired
	private FileUploadApi fileUploadApi;

	/**
	 * 描述：删除文件或者文件夹
	 *
	 * @param path
	 * @author 李帅
	 * @created 2017年9月23日 下午4:08:48
	 * @since
	 */
	public static void deleteAllFilesOfDir(File path) {
		if (!path.exists())
			return;
		if (path.isFile()) {
			path.delete();
			return;
		}
		File[] files = path.listFiles();
		for (int i = 0; i < files.length; i++) {
			deleteAllFilesOfDir(files[i]);
		}
		path.delete();
	}

	/**
	 *
	 * 描述：通过批次号获取证书下载URL
	 * @author 李帅
	 * @created 2018年6月8日 下午1:59:38
	 * @since
	 * @param tenantId
	 * @param userId
	 * @param batchNum
	 * @return
	 * @throws BusinessException
	 */
	public String getDownloadUUID(Long tenantId, String userId, Long batchNum) throws BusinessException {
		try {
			// 入参质量校验
			if (tenantId == null) {
				throw new BusinessException(DeviceExceptionEnum.TENANTID_IS_NULL);
			}
			if (StringUtil.isEmpty(userId)) {
				throw new BusinessException(DeviceExceptionEnum.USERID_IS_NULL);
			}
			if (batchNum == null) {
				throw new BusinessException(DeviceExceptionEnum.BATCHNUM_IS_ERROR);
			}
			CustUuidManage custUuidManage = uuidManegerMapper.getDownloadUUID(batchNum, tenantId);
			FileDto fileDto = null;
			if (custUuidManage != null) {
				if (custUuidManage.getStatus() == 1) {
					throw new BusinessException(DeviceExceptionEnum.CERT_BEING);
				} else if (custUuidManage.getStatus() == 2) {
					if (StringUtil.isNotBlank(custUuidManage.getFileId())) {
						fileDto = fileApi.getGetUrl(custUuidManage.getFileId());
					} else {
						throw new BusinessException(DeviceExceptionEnum.FILEID_NOT_EXIST);
					}
				} else {
					throw new BusinessException(DeviceExceptionEnum.GENERATION_FAILED);
				}

			} else {
				throw new BusinessException(DeviceExceptionEnum.CERT_NOT_EXIST);
			}
			//更新下载次数
			uuidManegerMapper.updateUuidApplyRecord(batchNum);

			return fileDto.getPresignedUrl();
		} catch (BusinessException e) {
			LOGER.error("", e);
			throw e;
		} catch (Exception e) {
			LOGER.error("", e);
			throw new BusinessException(DeviceExceptionEnum.GETDOWNLOADUUID_FAILED, e);
		}
	}

//	// 判断文件是否存在
//	public boolean judeFileExists(File file) {
//		if (file.exists()) {
//			return true;
//		} else {
//			return false;
//		}
//
//	}

	
	
	/**
	 *
	 * 描述：UUID生成
	 * @author 李帅
	 * @created 2018年4月12日 下午2:49:25
	 * @since
	 * @param generateUUID
	 * @throws BusinessException
	 */
	public Long generateUUID(Long batchNum) throws BusinessException {
		try {
			//入参质量校验
			if(batchNum == null){
				throw new BusinessException(DeviceExceptionEnum.BATCHNUM_IS_ERROR);
			}
			//获取批次信息
			GenerateUUID generateUUID = uuidManegerMapper.getGenerateUUIDInfo(batchNum);
			if(generateUUID == null){
				throw new BusinessException(DeviceExceptionEnum.CERT_NOT_EXIST);
			}
			if(generateUUID.getPayStatus() != 2){
				throw new BusinessException(DeviceExceptionEnum.ORDER_NOT_PAID);
			}
			// 解密操作
			try {
				EncryptionFiles.dencryptFile(shPath + System.getProperty("file.separator") + "CA_target.crt",
						shPath + System.getProperty("file.separator") + "CA_" + batchNum + ".crt");
				EncryptionFiles.dencryptFile(shPath + System.getProperty("file.separator") + "CA_target.key",
						shPath + System.getProperty("file.separator") + "CA_" + batchNum + ".key");
			} catch (Exception e) {
				throw new BusinessException(DeviceExceptionEnum.FAILED_DEC_CA_FILE);
			}

			String CAMD5 = DigestUtils.md5Hex(new FileInputStream(shPath + System.getProperty("file.separator") + "CA_" + batchNum + ".crt"));
			LOGER.info(shPath + System.getProperty("file.separator") + "CA_" + batchNum + ".crt::::" + CAMD5);
			String CAKEYMD5 = DigestUtils.md5Hex(new FileInputStream(shPath + System.getProperty("file.separator") + "CA_" + batchNum + ".key"));
			LOGER.info(shPath + System.getProperty("file.separator") + "CA_" + batchNum + ".key::::" + CAMD5);
			if(!"a32944a50e0df5c7c41d4de3ca17a5bd".equals(CAMD5) || !"57e7c21a9b4b27ed3723901a43db15e4".equals(CAKEYMD5)) {
				throw new BusinessException(DeviceExceptionEnum.CA_MD5_ABNORMAL);
			}
			//获取租户信息
			TenantInfoResp tenantInfo = new TenantInfoResp();
			try {
				tenantInfo = tenantApi.getTenantById(generateUUID.getTenantId());
			} catch (Exception e) {
				LOGER.info("getTenantById fail.");
				tenantInfo.setName("");
				tenantInfo.setCode("");
			}
			//生成UUID
			generateCredentials(generateUUID.getProductId(), tenantInfo.getCode(), batchNum, generateUUID.getCreateNum(),
					generateUUID.getUuidValidityDays(), generateUUID.getDeviceType(), tenantInfo.getName(),
					generateUUID.getIsDirectDevice(), generateUUID.getDeviceTypeId(), generateUUID.getTenantId());
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("status", "1");
			resultParam.put("batchNum", batchNum);
			uuidManegerMapper.updateCustUUIDManage(resultParam);
			return batchNum;
		} catch (BusinessException e) {
			LOGER.error("",e);
			throw e;
		} catch (Exception e) {
			LOGER.error("",e);
			throw new BusinessException(DeviceExceptionEnum.GENERATEUUID_FAILED, e);
		}
	}

	/**
	 *
	 * 描述：异步生成UUID
	 * @author 李帅
	 * @created 2018年5月9日 下午2:46:42
	 * @since
	 * @param productId
	 * @param custCode
	 * @param batch
	 * @param num
	 * @param uuidValidityDays
	 * @param uuidType
	 * @param custName
	 * @param isDirectDevice
	 */
	public void generateCredentials(final Long productId, final String custCode, final Long batch,
									final int num, final BigDecimal uuidValidityDays, final String uuidType, final String custName,
									final Long isDirectDevice, final Long deviceTypeId, Long tenantId) {
		new Thread() {
			public void run() {
				try {
					long startTime1=System.currentTimeMillis(); //获取结束时间

					shellGenerateCredentials(productId, custCode, batch, num, uuidValidityDays, uuidType, custName, isDirectDevice, deviceTypeId, tenantId);

					long endTime1=System.currentTimeMillis(); //获取结束时间
					LOGER.info("证书生成程序运行时间： "+(endTime1-startTime1)+"ms");

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 *
	 * 描述：批量生成证书并打包上传至FS
	 * @author 李帅
	 * @created 2018年5月9日 下午2:46:31
	 * @since
	 * @param productId
	 * @param custCode
	 * @param batch
	 * @param num
	 * @param uuidValidityDays
	 * @param uuidType
	 * @param custName
	 * @param isDirectDevice
	 * @throws Exception
	 */
	public void shellGenerateCredentials(Long productId, String custCode, Long batch, int num, BigDecimal uuidValidityDays,
										 String uuidType, String custName, Long isDirectDevice, Long deviceTypeId, Long tenantId) throws Exception{
		List<String> p2pIdList = null;
		List<String> idsList = null;
		try {
			/**创建目录*/
			String tempId = this.createTempDir();
			/**批量获取UUID*/
			idsList = new ArrayList<String>();
			if (num > 100) {
				int subCount = num % 100 == 0 ? num / 100 : num / 100 + 1;
				int startIndext = 0;
				int stopIndext = 0;
				int endIndext = num % 100 == 0 ? 100 : num % 100;
				for (int i = 0; i < subCount; i++) {
					stopIndext = (i == subCount - 1) ? stopIndext + endIndext : stopIndext + 100;
					idsList.addAll(ToolUtil.getUUIDS( stopIndext - startIndext));
					startIndext = stopIndext;
				}
			} else {
				idsList.addAll(ToolUtil.getUUIDS( num));
			}
			/**存放CSV文件*/
			List<String> csvList = new ArrayList<String>();
			csvList.add("Batch,Device-UUID,P2P-ID,PassWord,SN");

			/**获取证书并附加策略*/
			//获取最大的SN
			long sn = getMaxSN(num);
			if(IPC_UUIDTYPE.equals(uuidType)) {

				//获取未使用的P2PID
				p2pIdList = getP2PId(custCode, num);
				if(p2pIdList.size() < num) {
					throw new BusinessException(DeviceExceptionEnum.NOT_ENOUGH_P2PID);
				}
			}
			//生成证书，插入数据
			csvList = creatCertAndInsertData(idsList, sn, csvList, p2pIdList, tempId, productId, custCode, batch, num,
					uuidValidityDays, uuidType, custName, isDirectDevice, deviceTypeId, tenantId);
			/**创建CSV文件*/

			long startTime1=System.currentTimeMillis(); //获取结束时间

			File csv = new File(certPath.concat(System.getProperty("file.separator")).concat(tempId).concat(System.getProperty("file.separator")).concat("Certificate.csv"));
			/**写CSV文件*/
			CSVUtils.exportCsv(csv, csvList);
			/**写ZIP文件*/
			//源文件夹
			String srcFile = certPath.concat(System.getProperty("file.separator")).concat(tempId);
			//目标压缩夹
			String destPath = certPath.concat(System.getProperty("file.separator")).concat(tempId)+".zip";
			ZipUtil.zip(srcFile, destPath, "123456789");
			/**存入S3文件*/
			MultipartFile multipartFile = FileUtil.toMultipartFile(new File(destPath));
			String s3key = null;
			try {
				s3key = fileUploadApi.uploadFile(multipartFile, tenantId);
			} catch (Exception e) {
				throw new Exception(e);
			}

			long endTime1=System.currentTimeMillis(); //获取结束时间
			LOGER.info("上传证书程序运行时间： "+(endTime1-startTime1)+"ms");

			/**修改到UUID管理表，状态为1：已完成，fileId填入s3key*/
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("fileId", s3key);
			resultParam.put("status", "2");
			resultParam.put("batchNum", batch);
			uuidManegerMapper.updateCustUUIDManage(resultParam);
			/**删除ZIP文件*/
			File zip = new File(destPath);
			if(zip.exists()){
				zip.delete();
			}
			//删除压缩前的文件
			File tempDir = new File(srcFile);
			deleteAllFilesOfDir(tempDir);
			//删除解密的CA文件
			File decFile = new File(shPath + System.getProperty("file.separator") + "CA_" + batch + ".crt");
			decFile.delete();
			File decKeyFile = new File(shPath + System.getProperty("file.separator") + "CA_" + batch + ".key");
			decKeyFile.delete();
			idsList.clear();
			csvList.clear();
			idsList = null;
			csvList = null;
		} catch (BusinessException e) {
			//删除解密的CA文件
			File decFile = new File(shPath + System.getProperty("file.separator") + "CA_" + batch + ".crt");
			decFile.delete();
			File decKeyFile = new File(shPath + System.getProperty("file.separator") + "CA_" + batch + ".key");
			decKeyFile.delete();
			//回滚P2PID使用状态
			rollBackP2PIdMark(custCode, p2pIdList);
			//标记批次生成信息为P2PID不够用
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("status", "4");
			resultParam.put("batchNum", batch);
			uuidManegerMapper.updateCustUUIDManage(resultParam);
			throw e;
		} catch (Exception e) {
			//删除解密的CA文件
			File decFile = new File(shPath + System.getProperty("file.separator") + "CA_" + batch + ".crt");
			decFile.delete();
			File decKeyFile = new File(shPath + System.getProperty("file.separator") + "CA_" + batch + ".key");
			decKeyFile.delete();
			//回滚设备数据和设备扩展数据
			Map<String, List<String>> deleteDeviceParam = new HashMap<String, List<String>>();
			List<List<String>> idsListList = null;
			if(idsList.size() > DeleteNum) {
				idsListList = dealBySubList(idsList, DeleteNum);
				for(List<String> ids : idsListList) {
					deleteDeviceParam.put("idsList", ids);
					uuidManegerMapper.deleteDeviceWithUUID(deleteDeviceParam);
					uuidManegerMapper.deleteDeviceExtendWithUUID(deleteDeviceParam);
				}
			}else {
				deleteDeviceParam.put("idsList", idsList);
				uuidManegerMapper.deleteDeviceWithUUID(deleteDeviceParam);
				uuidManegerMapper.deleteDeviceExtendWithUUID(deleteDeviceParam);
			}
			//回滚证书
			for(String uuid : idsList) {
				RedisCacheUtil.delete(new StringBuilder().append(uuid).append(splitStr).append(pwdStr).toString());
			}
			//回滚P2PID使用状态
			rollBackP2PIdMark(custCode, p2pIdList);
			//标记批次生成失败
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("status", "3");
			resultParam.put("batchNum", batch);
			uuidManegerMapper.updateCustUUIDManage(resultParam);
			throw e;
		}
	}

	/**
	 *
	 * 描述：生成证书，插入数据
	 * @author 李帅
	 * @created 2018年5月30日 下午4:21:36
	 * @since
	 * @param idsList
	 * @param sn
	 * @param csvList
	 * @param p2pIdList
	 * @param tempId
	 * @param productId
	 * @param custCode
	 * @param batch
	 * @param num
	 * @param uuidValidityDays
	 * @param uuidType
	 * @param custName
	 * @param isDirectDevice
	 * @param deviceTypeId
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<String> creatCertAndInsertData(List<String> idsList, long sn, List<String> csvList, List<String> p2pIdList, String tempId,
											   Long productId, String custCode, Long batch, int num, BigDecimal uuidValidityDays,String uuidType,
											   String custName, Long isDirectDevice, Long deviceTypeId, Long tenantId) throws Exception {
		List<DeviceExtend> deviceExtendList = new ArrayList<DeviceExtend>();
		DeviceExtend devExtend = null;
		int p2pNum = 0;
		List<PloyInfo> ployInfos = new ArrayList<PloyInfo>();
		PloyInfo ployInfo = null;
		for(String deuuid : idsList){
			String deviceCipher = StringUtil.getRandomStringWithNoZore(8);
			sn ++;
			String snString = String.format("%0" + 11 + "d", sn);
			if(IPC_UUIDTYPE.equals(uuidType)) {
				csvList.add(batch + "," + deuuid + "," + p2pIdList.get(p2pNum) + "," + deviceCipher + "," + snString);
			}else {
				csvList.add(batch + "," + deuuid + "," + "," + deviceCipher + "," + snString);
			}
			LOGER.info(deuuid);
			ployInfo = new PloyInfo();
			ployInfo.setClientId(deuuid);
			ployInfo.setUuid(deuuid);
			ployInfo.setPassword(DigestUtils.sha256Hex(deviceCipher));
			ployInfos.add(ployInfo);
			//添加到设备拓展表
			devExtend = new DeviceExtend();
			devExtend.setTenantId(tenantId);
			devExtend.setBatchNum(batch);
			devExtend.setDeviceId(deuuid);
			if(IPC_UUIDTYPE.equals(uuidType)) {
				devExtend.setP2pId(p2pIdList.get(p2pNum));
			}else {
				devExtend.setP2pId("");
			}
			devExtend.setUuidValidityDays(uuidValidityDays);
			devExtend.setDeviceCipher(deviceCipher);
			deviceExtendList.add(devExtend);
			p2pNum ++;
		}

		long startTime2=System.currentTimeMillis(); //获取结束时间
		mqttPloyApi.batchSaveAcls(ployInfos);
		long endTime2=System.currentTimeMillis(); //获取结束时间
		LOGER.info("策略生成序运行时间： "+(endTime2-startTime2)+"ms");

		long startTime=System.currentTimeMillis();   //获取开始时间
		int bacthUUID = 0;
		List<List<DeviceExtend>> certListList = null;
		if (idsList.size() > uuidBigNum) {
			bacthUUID = idsList.size() / uuidBigThreadNum + 1;
			certListList = dealBySubDeviceExtend(deviceExtendList, bacthUUID);
			// 创建一个线程池
			ExecutorService pool = Executors.newFixedThreadPool(uuidBigThreadNum);
			// 创建多个有返回值的任务
			List<Future> list = new ArrayList<Future>();
			for (int i = 0; i < uuidBigThreadNum; i++) {
				if(certListList.get(i) != null) {
					//异步生成证书
					Callable callable = new UUIDCallable(host, shPath, certPath, certListList.get(i), tempId, batch);
					// 执行任务并获取Future对象
					Future future = pool.submit(callable);
					list.add(future);
				}
			}
			for (Future future : list) {
				while(true) {
					if(!future.isDone()) {
						Thread.sleep(30);
					}else{
						break;
					}
				}
				if(!(boolean) future.get()) {
					throw new Exception("creatCertAndInsertData fail.");
				}
			}
			// 关闭线程池
			pool.shutdown();
		} else if (idsList.size() > uuidMediumNum) {
			bacthUUID = idsList.size() / uuidMediumThreadNum + 1;
			certListList = dealBySubDeviceExtend(deviceExtendList, bacthUUID);
			ExecutorService pool = Executors.newFixedThreadPool(uuidMediumThreadNum);
			List<Future> list = new ArrayList<Future>();
			for (int i = 0; i < uuidMediumThreadNum; i++) {
				if(certListList.get(i) != null) {
					//异步生成证书
					Callable callable = new UUIDCallable(host, shPath, certPath, certListList.get(i), tempId, batch);
					Future future = pool.submit(callable);
					list.add(future);
				}
			}
			// 获取所有并发任务的运行结果
			for (Future future : list) {
				while(true) {
					if(!future.isDone()) {
						Thread.sleep(30);
					}else{
						break;
					}
				}
				if(!(boolean) future.get()) {
					throw new Exception("creatCertAndInsertData fail.");
				}
			}
			pool.shutdown();
		} else {
			ExecutorService pool = Executors.newFixedThreadPool(1);
			//异步生成证书
			Callable callable = new UUIDCallable(host, shPath, certPath, deviceExtendList, tempId, batch);
			Future future = pool.submit(callable);
			// 获取所有并发任务的运行结果
			while(true) {
				if(!future.isDone()) {
					Thread.sleep(30);
				}else{
					break;
				}
			}
			if(!(boolean) future.get()) {
				throw new Exception("creatCertAndInsertData fail.");
			}
			pool.shutdown();
		}
		long endTime=System.currentTimeMillis(); //获取结束时间
		LOGER.info("生成证书程序运行时间： "+(endTime-startTime)+"ms");
		long startTime1=System.currentTimeMillis(); //获取结束时间
		Map<String, Object> deviceExtendParam = new HashMap<String, Object>();
		List<List<DeviceExtend>> deviceExtendListList = null;
		if(deviceExtendList.size() > DeleteNum) {
			deviceExtendListList = dealBySubDeviceExtend(deviceExtendList, DeleteNum);
			for(List<DeviceExtend> deviceExtend : deviceExtendListList) {
				deviceExtendParam.put("deviceExtendList", deviceExtend);
				uuidManegerMapper.saveDeviceExtend(deviceExtendParam);
			}
		}else {
			deviceExtendParam.put("deviceExtendList", deviceExtendList);
			uuidManegerMapper.saveDeviceExtend(deviceExtendParam);
		}

		long endTime1=System.currentTimeMillis(); //获取结束时间
		LOGER.info("保存设备扩展数据程序运行时间： "+(endTime1-startTime1)+"ms");
		long startTime3=System.currentTimeMillis(); //获取结束时间

		//通过设备扩展表关联批量插入，若添加事物处理后要重新修改这段逻辑
		Map<String, Object> saveDeviceParam = new HashMap<String, Object>();
		saveDeviceParam.put("venderCode", custCode);
		saveDeviceParam.put("manufacturer", custName);
		saveDeviceParam.put("deviceTypeId", deviceTypeId);
		saveDeviceParam.put("productId", productId);
		saveDeviceParam.put("tenantId", tenantId);
		saveDeviceParam.put("isDirectDevice", isDirectDevice);

		List<List<String>> idsListList = null;
		if(idsList.size() > DeleteNum) {
			idsListList = dealBySubList(idsList, DeleteNum);
			for(List<String> ids : idsListList) {
				saveDeviceParam.put("idsList", ids);
				uuidManegerMapper.saveDeviceWithUUID(saveDeviceParam);
			}
		}else {
			saveDeviceParam.put("idsList", idsList);
			uuidManegerMapper.saveDeviceWithUUID(saveDeviceParam);
		}
		long endTime3=System.currentTimeMillis(); //获取结束时间
		LOGER.info("保存设备数据程序运行时间： "+(endTime3-startTime3)+"ms");
		return csvList;
	}

	/**
	 *
	 * 描述：通过list的     subList(int fromIndex, int toIndex)方法实现
	 * @author 李帅
	 * @created 2017年11月2日 上午10:44:35
	 * @since
	 * @param sourList
	 * @param batchCount
	 */
	public List<List<String>> dealBySubList(List<String> sourList, int batchCount) {
		int sourListSize = sourList.size();
		int subCount = sourListSize % batchCount == 0 ? sourListSize / batchCount : sourListSize / batchCount + 1;
		int startIndext = 0;
		int stopIndext = 0;
		int endIndext = sourListSize % batchCount == 0 ? batchCount : sourListSize % batchCount;
		List<List<String>> tempListList = new ArrayList<List<String>>();
		List<String> tempList = null;
		for (int i = 0; i < subCount; i++) {
			stopIndext = (i == subCount - 1) ? stopIndext + endIndext : stopIndext + batchCount;
			tempList = new ArrayList<String>(sourList.subList(startIndext, stopIndext));
			tempListList.add(tempList);
			startIndext = stopIndext;
		}
		return tempListList;
	}

	/**
	 *
	 * 描述：通过list的     subList(int fromIndex, int toIndex)方法实现
	 * @author 李帅
	 * @created 2017年11月2日 上午10:44:35
	 * @since
	 * @param sourList
	 * @param batchCount
	 */
	public List<List<DeviceExtend>> dealBySubDeviceExtend(List<DeviceExtend> sourList, int batchCount) {
		int sourListSize = sourList.size();
		int subCount = sourListSize % batchCount == 0 ? sourListSize / batchCount : sourListSize / batchCount + 1;
		int startIndext = 0;
		int stopIndext = 0;
		int endIndext = sourListSize % batchCount == 0 ? batchCount : sourListSize % batchCount;
		List<List<DeviceExtend>> tempListList = new ArrayList<List<DeviceExtend>>();
		List<DeviceExtend> tempList = null;
		for (int i = 0; i < subCount; i++) {
			stopIndext = (i == subCount - 1) ? stopIndext + endIndext : stopIndext + batchCount;
			tempList = new ArrayList<DeviceExtend>(sourList.subList(startIndext, stopIndext));
			tempListList.add(tempList);
			startIndext = stopIndext;
		}
		return tempListList;
	}

	/**
	 *
	 * 描述：回滚P2PID使用状态
	 * @author 李帅
	 * @created 2018年1月8日 下午2:30:04
	 * @since
	 * @param custCode
	 * @param p2pIdList
	 * @throws Exception
	 */
	public void rollBackP2PIdMark(String custCode,List<String> p2pIdList) throws Exception {
		if(p2pIdList == null || p2pIdList.size() < 1) {
			return;
		}
		Map<String, Object> map = null;
		if(p2pIdList.size() > DeleteNum) {
			List<List<String>> p2pIdListList = dealBySubList(p2pIdList, DeleteNum);
			for(List<String> p2pIdLists : p2pIdListList) {
				map = new HashMap<String, Object>();
				map.put("custCode", custCode);
				map.put("p2pId", p2pIdLists);
				uuidManegerMapper.rollBackP2PIdMark(map);
			}
		}else {
			map = new HashMap<String, Object>();
			map.put("custCode", custCode);
			map.put("p2pId", p2pIdList);
			uuidManegerMapper.rollBackP2PIdMark(map);
		}
	}

	/**
	 *
	 * 描述：获取SN最大值,并回写SN最大值
	 * @author 李帅
	 * @created 2017年9月13日 下午2:48:19
	 * @since
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Long getMaxSN(int num) throws Exception {

//		ZKLockUtil locks = null;
//		try {
//			locks = new ZKLockUtil(BusinessType.SN);
//		} catch (BusinessException e1) {
//			e1.printStackTrace();
//		}
		Long maxSN = null;
		try {
//			if (locks.getLock().acquire(20, TimeUnit.SECONDS)) {
			maxSN = uuidManegerMapper.getMaxSN();
			if (maxSN == null) {
				maxSN = 0L;
			}
			uuidManegerMapper.setMaxSN(maxSN + num);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			locks.close();
		}
		return maxSN;
	}

	/**
	 *
	 * 描述：获取P2PID
	 * @author 李帅
	 * @created 2018年1月5日 下午4:57:32
	 * @since
	 * @param custCode
	 * @param num
	 * @return
	 * @throws Exception
	 */
	public List<String> getP2PId(String custCode, int num) throws Exception {

//		ZKLockUtil locks = null;
//		try {
//			locks = new ZKLockUtil(BusinessType.P2PID);
//		} catch (BusinessException e1) {
//			e1.printStackTrace();
//		}
		List<String> p2pIdList = null;
		try {
//			if (locks.getLock().acquire(20, TimeUnit.SECONDS)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("custCode", custCode);
			map.put("num", num);
			p2pIdList = uuidManegerMapper.getP2PId(map);

			//更新P2PID状态
			if(p2pIdList.size() > DeleteNum) {
				List<List<String>> p2pIdListList = dealBySubList(p2pIdList, DeleteNum);
				for(List<String> p2pIdLists : p2pIdListList) {
					map = new HashMap<String, Object>();
					map.put("custCode", custCode);
					map.put("p2pId", p2pIdLists);
					uuidManegerMapper.updateP2PIdMark(map);
				}
			}else {
				map = new HashMap<String, Object>();
				map.put("custCode", custCode);
				map.put("p2pId", p2pIdList);
				uuidManegerMapper.updateP2PIdMark(map);
			}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			locks.close();
		}
		return p2pIdList;
	}

	/**
	 *
	 * 描述：创建文件存入目录
	 * @author mao2080@sina.com
	 * @created 2017年8月26日 下午2:45:46
	 * @since
	 * @return
	 */
	private String createTempDir(){
		String tempId = UUID.randomUUID().toString();
		File file = new File(certPath.concat(System.getProperty("file.separator")).concat(tempId));
		if(!file.exists()){
			file.mkdir();
		}
		return tempId;
	}

	@Override
	public Page<UUIDOrderDto> getUUIDOrder(GetUUIDOrderReq uuidOrderReq) throws BusinessException {
		/*PageHelper.startPage(CommonUtil.getPageNum(uuidOrderReq), CommonUtil.getPageSize(uuidOrderReq),true);
		try {
			return new PageInfo<>(uuidManegerMapper.getUUIDOrder(uuidOrderReq));
		}catch (Exception e){
			throw new BusinessException(DeviceExceptionEnum.DEVICE_NOT_EXIST, "unknown error");
		}*/

		com.baomidou.mybatisplus.plugins.Page<UUIDOrderDto> page =new com.baomidou.mybatisplus.plugins.Page<UUIDOrderDto>(CommonUtil.getPageNum(uuidOrderReq), CommonUtil.getPageSize(uuidOrderReq));
		List<UUIDOrderDto> orders = null;
		try {
			orders = uuidManegerMapper.getUUIDOrder(page,uuidOrderReq);
		} catch (Exception e) {
			LOGER.error("getUUIDOrder error", e);
			throw new BusinessException(DeviceExceptionEnum.GETUUIDOrder_FAILED, e);
		}
		page.setRecords(orders);

		com.iot.common.helper.Page<UUIDOrderDto> myPage=new com.iot.common.helper.Page<UUIDOrderDto>();
		BeanCopyUtils.copyMybatisPlusPageToPage(page,myPage);
		return myPage;

		/*com.baomidou.mybatisplus.plugins.Page page2 = new com.baomidou.mybatisplus.plugins.Page(CommonUtil.getPageNum(uuidOrderReq), CommonUtil.getPageSize(uuidOrderReq));
		EntityWrapper wrapper = new EntityWrapper<>();
		if (uuidOrderReq.getBatchNumId()!=null)
			wrapper.eq("uar.id", uuidOrderReq.getBatchNumId());
		if (uuidOrderReq.getProductId()!=null)
			wrapper.eq("uar.product_id", uuidOrderReq.getProductId());
		if (uuidOrderReq.getApplyStatus()!=null)
			wrapper.eq("uar.uuid_apply_status", uuidOrderReq.getApplyStatus());
		if (uuidOrderReq.getStart()!=null)
			wrapper.ge("uar.create_time", uuidOrderReq.getStart());
		if (uuidOrderReq.getEnd()!=null)
			wrapper.lt("uar.create_time", uuidOrderReq.getEnd());

		List<UUIDOrderDto> orders=uuidManegerMapper.getUUIDOrder1(page2, wrapper);
		page2.setRecords(orders);

		com.iot.common.helper.Page<UUIDOrderDto> myPage=new com.iot.common.helper.Page<UUIDOrderDto>();
		BeanCopyUtils.copyMybatisPlusPageToPage(page2,myPage);
		return myPage;*/
	}

	@Override
	public Page<UUIDInfoDto> getUUIDInfo(GetUUIDReq uuidReq) throws BusinessException {
		com.baomidou.mybatisplus.plugins.Page<UUIDInfoDto> page =new com.baomidou.mybatisplus.plugins.Page<UUIDInfoDto>(CommonUtil.getPageNum(uuidReq), CommonUtil.getPageSize(uuidReq));
		List<UUIDInfoDto> orders = null;
		try {
			orders = uuidManegerMapper.getUUIDInfo(page,uuidReq);
		} catch (Exception e) {
			LOGER.error("getUUIDInfo error", e);
			throw new BusinessException(DeviceExceptionEnum.GETUUIDINFO_FAILED, e);
		}
		page.setRecords(orders);

		com.iot.common.helper.Page<UUIDInfoDto> myPage=new com.iot.common.helper.Page<UUIDInfoDto>();
		BeanCopyUtils.copyMybatisPlusPageToPage(page,myPage);
		return myPage;
	}

	/**
	 * 描述：上报总装测试数据
	 *
	 * @param licenseUsageReq
	 * @return
	 * @throws BusinessException
	 * @author chq
	 * @created 2018年6月27日 下午2:59:27
	 * @since
	 */
	public void licenseUsage(LicenseUsageReq licenseUsageReq) throws BusinessException {
		if (licenseUsageReq == null || licenseUsageReq.getLicenseUsageList() == null) {
			throw new BusinessException(UUIDManegerExceptionEnum.LICENSE_USAGE_IS_NULL);
		}
		List<LicenseUsageVo> licenseUsageVos = licenseUsageReq.getLicenseUsageList();

		try {
			for (LicenseUsageVo licenseUsageVo : licenseUsageVos) {
				LicenseUsage licenseUsage = new LicenseUsage();
				PropertyUtils.copyProperties(licenseUsage, licenseUsageVo);
				licenseUsage.setCreateTime(new Date());
				licenseUsageMapper.insert(licenseUsage);
			}
		} catch (Exception e) {
			throw new BusinessException(UUIDManegerExceptionEnum.LICENSE_USAGE_SAVE_ERROR, e);
		}
	}

	/**
     * 
     * 描述：证书生成补偿
     * @author 李帅
     * @created 2018年8月24日 上午9:53:15
     * @since 
     * @throws BusinessException
     */
	public void generateCompensate() throws BusinessException {
		List<Long> batchNumIds = uuidManegerMapper.getNeedCompensateBatch();
		List<Long> compensateFail = RedisCacheUtil.listGetAll(compensateFailKey, Long.class);
		batchNumIds.removeAll(compensateFail);
		for (Long batchNumId : batchNumIds) {
			try {
				generateUUID(batchNumId);
			} catch (Exception e) {
				if(compensateFail == null) {
					compensateFail = new ArrayList<Long>();
					compensateFail.add(batchNumId);
				}else {
					compensateFail.add(batchNumId);
				}
			}
		}
		RedisCacheUtil.listSet(compensateFailKey, compensateFail);
	}

	public GenerateUUID getGenerateUUIDInfo(Long batchNum){
		if(batchNum == null){
			throw new BusinessException(DeviceExceptionEnum.BATCHNUM_IS_ERROR);
		}
		try {
			return uuidManegerMapper.getUuidInfo(batchNum);
		}catch (Exception e){
			throw new BusinessException(DeviceExceptionEnum.BATCHNUM_IS_ERROR);
		}
	}

	/**
	 * 
	 * 描述：UUID订单退款前操作
	 * @author 李帅
	 * @created 2018年11月14日 下午4:50:48
	 * @since 
	 * @param uuidRefundReq
	 */
	public UUidApplyRecordInfo beforeUUIDRefund(UUIDRefundReq uuidRefundReq){
		if(uuidRefundReq == null){
			throw new BusinessException(DeviceExceptionEnum.UUIDREFUNDREQ_IS_NULL);
		}
		if(uuidRefundReq.getTenantId() == null) {
			throw new BusinessException(DeviceExceptionEnum.TENANTID_IS_NULL);
		}
		if(StringUtil.isEmpty(uuidRefundReq.getOrderId())) {
			throw new BusinessException(DeviceExceptionEnum.ORDERID_IS_NULL);
		}
		UUidApplyRecord applyRecord = uuidManegerMapper.getUUidApplyRecord(uuidRefundReq);
		if(applyRecord == null) {
			throw new BusinessException(DeviceExceptionEnum.ORDER_NOT_EXIST);
		}
		if(!(applyRecord.getPayStatus() == 2 || applyRecord.getPayStatus() == 6)) {
			throw new BusinessException(DeviceExceptionEnum.ORDER_CANNOT_REFUND);
		}
		try {
			uuidManegerMapper.beforeUUIDRefund(uuidRefundReq);
			UUidApplyRecordInfo applyRecordInfo = new UUidApplyRecordInfo();
			BeanUtils.copyProperties(applyRecord, applyRecordInfo);
			return applyRecordInfo;
		}catch (BusinessException e){
			throw new BusinessException(DeviceExceptionEnum.BEFOREUUIDREFUND_IS_ERROR);
		}
	}
	
	/**
	 * 
	 * 描述：UUID订单退款成功操作
	 * @author 李帅
	 * @created 2018年11月14日 下午4:50:48
	 * @since 
	 * @param uuidRefundReq
	 */
	public void refundSuccess(UUIDRefundOperateReq refundOperateReq){
		if(refundOperateReq == null){
			throw new BusinessException(DeviceExceptionEnum.UUIDREFUNDREQ_IS_NULL);
		}
		if(refundOperateReq.getTenantId() == null) {
			throw new BusinessException(DeviceExceptionEnum.TENANTID_IS_NULL);
		}
		if(refundOperateReq.getRefundObjectId() == null) {
			throw new BusinessException(DeviceExceptionEnum.BATCHNUM_IS_NULL);
		}
		if(StringUtil.isEmpty(refundOperateReq.getOrderId())) {
			throw new BusinessException(DeviceExceptionEnum.ORDERID_IS_NULL);
		}
		try {
			UUIDRefundReq uuidRefundReq = new UUIDRefundReq();
			BeanUtils.copyProperties(refundOperateReq, uuidRefundReq);
			UUidApplyRecord applyRecord = uuidManegerMapper.getUUidApplyRecord(uuidRefundReq);
			if(applyRecord == null) {
				throw new BusinessException(DeviceExceptionEnum.ORDER_NOT_EXIST);
			}
			if(applyRecord.getPayStatus() != 4) {
				throw new BusinessException(DeviceExceptionEnum.ORDER_NOTIN_REFUNDLIST);
			}
			//获取批次信息
			GenerateUUID generateUUID = uuidManegerMapper.getUUIDOrderInfo(refundOperateReq.getRefundObjectId());
			if(generateUUID == null){
				throw new BusinessException(DeviceExceptionEnum.CERT_NOT_EXIST);
			}
		
			//删除P2PID使用状态
			if(IPC_UUIDTYPE.equals(generateUUID.getDeviceType())) {
				List<String> p2pIdList = uuidManegerMapper.getP2PIdByBatchNum(refundOperateReq.getRefundObjectId());
				if(p2pIdList != null) {
					rollBackP2PIdMark("", p2pIdList);
				}
				
			}
			
			List<String> idsList = uuidManegerMapper.getDeviceIdByBatchNum(refundOperateReq.getRefundObjectId());
			//删除设备数据和设备扩展数据
			Map<String, List<String>> deleteDeviceParam = new HashMap<String, List<String>>();
			List<List<String>> idsListList = null;
			if(idsList.size() > DeleteNum) {
				idsListList = dealBySubList(idsList, DeleteNum);
				for(List<String> ids : idsListList) {
					deleteDeviceParam.put("idsList", ids);
					uuidManegerMapper.deleteDeviceWithUUID(deleteDeviceParam);
					uuidManegerMapper.deleteDeviceExtendWithUUID(deleteDeviceParam);
				}
			}else {
				deleteDeviceParam.put("idsList", idsList);
				uuidManegerMapper.deleteDeviceWithUUID(deleteDeviceParam);
				uuidManegerMapper.deleteDeviceExtendWithUUID(deleteDeviceParam);
			}
			//删除策略
			for(String uuid : idsList) {
				RedisCacheUtil.delete(new StringBuilder().append(uuid).append(splitStr).append(pwdStr).toString());
			}
			//删除文件
			fileApi.deleteObject(applyRecord.getFileId());
			
			uuidManegerMapper.refundSuccess(uuidRefundReq);
		}catch (Exception e){
			throw new BusinessException(DeviceExceptionEnum.REFUNDSUCCESS_IS_ERROR);
		}
	}
	
	/**
	 * 
	 * 描述：UUID订单退款失败操作
	 * @author 李帅
	 * @created 2018年11月15日 下午3:59:30
	 * @since 
	 * @param refundOperateReq
	 */
	public void refundFail(UUIDRefundOperateReq refundOperateReq){
		if(refundOperateReq == null){
			throw new BusinessException(DeviceExceptionEnum.UUIDREFUNDREQ_IS_NULL);
		}
		if(refundOperateReq.getTenantId() == null) {
			throw new BusinessException(DeviceExceptionEnum.TENANTID_IS_NULL);
		}
		if(StringUtil.isEmpty(refundOperateReq.getOrderId())) {
			throw new BusinessException(DeviceExceptionEnum.ORDERID_IS_NULL);
		}
		UUIDRefundReq uuidRefundReq = new UUIDRefundReq();
		BeanUtils.copyProperties(refundOperateReq, uuidRefundReq);
		UUidApplyRecord applyRecord = uuidManegerMapper.getUUidApplyRecord(uuidRefundReq);
		if(applyRecord == null) {
			throw new BusinessException(DeviceExceptionEnum.ORDER_NOT_EXIST);
		}
		if(applyRecord.getPayStatus() != 4) {
			throw new BusinessException(DeviceExceptionEnum.ORDER_NOTIN_REFUNDLIST);
		}
		try {
			uuidManegerMapper.refundFail(uuidRefundReq);
		}catch (BusinessException e){
			throw new BusinessException(DeviceExceptionEnum.REFUNDFAIL_IS_ERROR);
		}
	}
}
