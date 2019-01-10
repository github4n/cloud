package com.iot.device.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iot.device.model.DeviceExtend;

public class UUIDCallable implements Callable<Object> {
	
	private static final Logger LOGER = LoggerFactory.getLogger(UUIDCallable.class);
	
    private String host;
    private String shPath;
    private String certPath;
    private String tempId;
	private Long batch;
    private List<DeviceExtend> deviceExtendList;

	UUIDCallable(String host, String shPath, String certPath, List<DeviceExtend> deviceExtendList, String tempId, Long batch) {
		this.host = host;
		this.shPath = shPath;
		this.certPath = certPath;
		this.tempId = tempId;
		this.deviceExtendList = deviceExtendList;
		this.batch = batch;
		if(LOGER.isInfoEnabled()) {
			LOGER.info(String.format("UUIDCallable  host=%s, shPath=%s, certPath=%s, tempId=%s, batch=%s", host,shPath,certPath,tempId,batch));
		}

	}

    @Override
    public Object call() throws Exception {
    	
    	try {
			for(DeviceExtend deviceExtend : this.deviceExtendList) {
				//生成证书
				creatCert(tempId, deviceExtend.getDeviceCipher(), deviceExtend.getDeviceId(), deviceExtend.getUuidValidityDays());
//				//证书验证
//				MqttSslServiceImpl mqttSsl = new MqttSslServiceImpl(host, shPath, certPath, 
//						deviceExtend.getDeviceId(), deviceExtend.getDeviceCipher(), tempId);
//				MqttClient mqttClient = mqttSsl.getClient();
//				if(mqttClient.isConnected()) {
//					LOGER.info("connection success !!!");
//					try {
//						mqttClient.disconnect();
//						mqttClient.close();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}else {
//					LOGER.info("connection error !!!");
//				}
			}
			return true;
		} catch (Exception e) {
			LOGER.error("call Exception " + e.getMessage(),e);
			return false;
		}
		
    }
    
    /**
	 * 
	 * 描述：生成证书
	 * @author 李帅
	 * @created 2018年5月30日 下午5:30:14
	 * @since 
	 * @param tempId
	 * @param deviceCipher
	 * @param deuuid
	 */
	public void creatCert(String tempId, String deviceCipher, String deuuid, BigDecimal uuidValidityDays) {
		int runningStatus = 0;
//		String s = null;
		StringBuffer stringBuffer = new StringBuffer();
		try {
//			String[] cmdArray = { "/bin/bash", "-c", "cd " + shPath + "; ./jh.sh " + deviceCipher + " client " + deuuid
//					+ " " + deuuid + " CA_" + batch + " " + certPath + "/" + tempId };
			
			String[] cmdArray = { "/bin/bash", "-c", "cd " + shPath + "; ./gen_crt.sh " + deviceCipher + " " + deuuid + " " + deuuid
					+ " " + uuidValidityDays +" " + certPath + "/" + tempId + " CA_" + batch + " " +  "cert"+ " " + "pem"};
//			LOGER.info(cmdArray[0]);
//			LOGER.info(cmdArray[1]);
//			LOGER.info(cmdArray[2]);
			ProcessBuilder builder = new ProcessBuilder(cmdArray);
			Process process = builder.start();

//			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
//			BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//			while ((s = stdInput.readLine()) != null) {
//				LOGER.info("shell log info ...." + s);
//				stringBuffer.append(s);
//			}
//			while ((s = stdError.readLine()) != null) {
//				LOGER.error("shell log error...." + s);
//				stringBuffer.append(s);
//			}
			try {
				runningStatus = process.waitFor();
			} catch (InterruptedException e) {
				runningStatus = 1;
				LOGER.error("等待shell脚本执行状态时，报错...", e);
				stringBuffer.append(e.getMessage());
			}

//			closeStream(stdInput);
//			closeStream(stdError);
			// 获取执行返回值
			if (runningStatus != 0) {
				// 进行错误处理
				throw new Exception("进行错误处理");
			}
			process.destroy();
			process = null;
			
		} catch (Exception e) {
			LOGER.error("执行shell脚本出错...", e);
			stringBuffer.append(e.getMessage());
			runningStatus = 1;
		} finally {
			try {
				TimeUnit.MICROSECONDS.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			File filePem = new File(certPath.concat(System.getProperty("file.separator")).concat(tempId)
					.concat(System.getProperty("file.separator")) + deuuid + ".pem");
			if (!(filePem.exists() && filePem.isFile() && filePem.length() > 0)) {
				LOGER.error("shell pem error retry...." + deuuid);
				this.creatCert(tempId, deviceCipher, deuuid, uuidValidityDays);
			}
			File fileCert = new File(certPath.concat(System.getProperty("file.separator")).concat(tempId)
					.concat(System.getProperty("file.separator")) + deuuid + ".cert");
			if (!(fileCert.exists() && fileCert.isFile() && fileCert.length() > 0)) {
				LOGER.error("shell cert error retry...." + deuuid);
				this.creatCert(tempId, deviceCipher, deuuid, uuidValidityDays);
			}
		}

	}
	
//	/**
//	 * 
//	 * 描述：关闭流
//	 * @author 李帅
//	 * @created 2018年5月31日 上午10:35:10
//	 * @since 
//	 * @param reader
//	 */
//	private void closeStream(BufferedReader reader){  
//	    try {  
//	        if(reader != null){  
//	            reader.close();  
//	        }  
//	    } catch (Exception e) {  
//	        reader = null;  
//	    }  
//	}
}
