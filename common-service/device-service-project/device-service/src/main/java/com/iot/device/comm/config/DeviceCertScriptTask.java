package com.iot.device.comm.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：启动后检查证书生成脚本及CA证书是否存在
 * 功能描述：启动后检查证书生成脚本及CA证书是否存在
 * 创建人： 李帅
 * 创建时间：2018年11月13日 上午10:07:01
 * 修改人：李帅
 * 修改时间：2018年11月13日 上午10:07:01
 */
@Component
@Order(value=1)
public class DeviceCertScriptTask implements CommandLineRunner{

	@Value("${uuid.shPath}")
	private String shPath;
	@Value("${uuid.certPath}")
	private String certPath;

    private static Logger logger = LoggerFactory.getLogger(DeviceCertScriptTask.class);

    /**
     * 
     * 描述：启动后检查证书生成脚本及CA证书是否存在
     * @author 李帅
     * @created 2018年11月13日 上午10:15:11
     * @since 
     * @param args
     */
    @Override
    public void run(String... args) {
        try {
        	Properties props=System.getProperties();
        	String osName = props.getProperty("os.name");
        	if(!osName.startsWith("Windows")) {
        		//创建脚本路径
    			File dir = new File(shPath);
    			if (!dir.exists()) {
    				dir.mkdir();
    				//检查证书脚本
    				judeDirExists();
    				//sh文件授权
    				int runningStatus = 0;
    				StringBuffer stringBuffer = new StringBuffer();
    				try {
    					String[] cmdArray = { "/bin/bash", "-c", "cd " + shPath + "; chmod +x *.sh"};
    					ProcessBuilder builder = new ProcessBuilder(cmdArray);
    					Process process = builder.start();
    					try {
    						runningStatus = process.waitFor();
    					} catch (InterruptedException e) {
    						runningStatus = 1;
    						logger.error("等待shell脚本执行状态时，报错...", e);
    						stringBuffer.append(e.getMessage());
    					}
    					// 获取执行返回值
    					if (runningStatus != 0) {
    						// 进行错误处理
    						throw new Exception("进行错误处理");
    					}
    					process.destroy();
    					process = null;
    					
    				} catch (Exception e) {
    					logger.error("执行shell脚本出错...", e);
    					stringBuffer.append(e.getMessage());
    					runningStatus = 1;
    				}
    			}
    			//创建证书存放路径
    			File tempDir = new File(certPath);
    			if (!tempDir.exists()) {
    				tempDir.mkdir();
    			}
        	}else {
        		logger.error("Window系统不支持证书生成");
        	}
        } catch (Exception e) {
            logger.error("FileTaskNotify run error:", e);
        }
    }
    /**
	 * 
	 * 描述：判断证书脚本文件是否存在,若不存在就创建脚本
	 * @author 李帅
	 * @created 2018年11月9日 下午2:13:16
	 * @since
	 */
	public void judeDirExists() {
		try {
			// 获取文件路径
			String jarPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
			jarPath = jarPath.substring(0, jarPath.length() - 2);
			URL url = null;
			InputStream in = null;
			InputStream input = null;
			File targetFile = null;
			OutputStream os = null;
			int bytesRead = 0;
			byte[] bufferOut = null;
			// 读取文件内容,生成gen_crt脚本文件
			String jarPathGenCrt = "jar:" + jarPath + "/cert/gen_crt.sh";
			url = new URL(jarPathGenCrt);
			in = url.openStream();
			input = url.openStream();
			targetFile = new File(shPath + "/gen_crt.sh");
			os = new FileOutputStream(targetFile);
			bytesRead = 0;
			bufferOut = new byte[8192];
			while ((bytesRead = input.read(bufferOut, 0, 8192)) != -1) {
				os.write(bufferOut, 0, bytesRead);
			}
			// 读取文件内容,生成CA_target.crt脚本文件
			String jarPathCATargetCrt = "jar:" + jarPath + "/cert/CA_target.crt";
			url = new URL(jarPathCATargetCrt);
			input = url.openStream();
			targetFile = new File(shPath + "/CA_target.crt");
			os = new FileOutputStream(targetFile);
			bytesRead = 0;
			bufferOut = new byte[8192];
			while ((bytesRead = input.read(bufferOut, 0, 8192)) != -1) {
				os.write(bufferOut, 0, bytesRead);
			}
			// 读取文件内容,生成CA_target.key脚本文件
			String jarPathCATargetKey = "jar:" + jarPath + "/cert/CA_target.key";
			url = new URL(jarPathCATargetKey);
			input = url.openStream();
			targetFile = new File(shPath + "/CA_target.key");
			os = new FileOutputStream(targetFile);
			bytesRead = 0;
			bufferOut = new byte[8192];
			while ((bytesRead = input.read(bufferOut, 0, 8192)) != -1) {
				os.write(bufferOut, 0, bytesRead);
			}
			if(os != null) {
				os.close();
			}
			if(input != null) {
				input.close();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
