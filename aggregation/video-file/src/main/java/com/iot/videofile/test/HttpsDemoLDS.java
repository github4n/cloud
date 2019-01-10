package com.iot.videofile.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iot.common.util.JsonUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpsDemoLDS {
//	public static String PFX_PATH = "D:/OpenSSL/SSL6/ca.p12"; // 客户端证书路径
	public static String CA_CRT_PATH = "D:/OpenSSL/CA_test.crt";
	public static String DEVICE_CRT_APTH = "D:/OpenSSL/e6e76f0e7f534efdadcb2e7e94592e33.cert";
	public static String DEVICE_KEY_APTH = "D:/OpenSSL/e6e76f0e7f534efdadcb2e7e94592e33.pem";
	private final static String PFX_PWD = "XGR5nHlS"; // 客户端证书密码
	
	public static void main(String[] args) throws Exception {
		String planId = "8f2adae75964456a8fc477fc8285505a";
		String fileType = "ts";
		System.out.println(System.getProperty("java.home"));
		System.setProperty("javax.net.ssl.trustStore", "D:\\softwarefiles\\JRE8\\lib\\jssecacerts");
		// System.setProperty("javax.net.ssl.trustStore", "D:\\softwarefiles\\JDK8\\jre\\lib\\jssecacerts");
		String fileJson = sslRequestPost(
				"https://localhost/ipc/file/getfsurlReq?planId=" + planId + "&fileType=" + fileType, null
		);
		Map<String, Object> map = JSON.parseObject(fileJson, Map.class);
		Map<String, String> fileMap = (Map<String, String>)map.get("data");
		JSONObject videoFile = new JSONObject();
		videoFile.put("planId", planId);
		Map<String, Object> file = new HashMap<>();
		file.put("fn", fileMap.get("fileName"));
		file.put("fileType", fileMap.get("fileType"));
		file.put("startTime", new Date());
		Random random = new Random();
		file.put("length", random.nextFloat()*100);
		file.put("size", random.nextInt(1000));
		videoFile.put("file", file);
		System.out.println(videoFile.toString());
		sslRequestPost("https://localhost/ipc/file/uploadFileInfo", videoFile);
	}

	public static String sslRequestPost(String url, JSONObject jsonObject) throws Exception {
		CloseableHttpClient httpclient = getHttpClient();
		try {
			HttpPost httpPost = new HttpPost(url);
			if (jsonObject!=null) {
				StringEntity stringEntity = new StringEntity(jsonObject.toString());
				stringEntity.setContentType("application/json");
				httpPost.setEntity(stringEntity);
			}
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();
				String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");// 返回结果
				System.out.println("返回结果：" + jsonStr);
				EntityUtils.consume(entity);
				return jsonStr;
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	}


	public static CloseableHttpClient getHttpClient() throws Exception{
		java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		CertificateFactory cAf = CertificateFactory.getInstance("X.509");
		FileInputStream caIn = new FileInputStream(new File(CA_CRT_PATH));
		X509Certificate ca = (X509Certificate) cAf.generateCertificate(caIn);
		KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
		caKs.load(null, null);
		caKs.setCertificateEntry("ca-certificate", ca);
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(caKs);
		caIn.close();
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		FileInputStream crtIn = new FileInputStream(new File(DEVICE_CRT_APTH));
		X509Certificate caCert = (X509Certificate) cf.generateCertificate(crtIn);
		crtIn.close();
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(null, null);
		ks.setCertificateEntry("certificate", caCert);
		ks.setKeyEntry("private-key", getPrivateKey(null), PFX_PWD.toCharArray(), new java.security.cert.Certificate[] { caCert });
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(ks, PFX_PWD.toCharArray());
		SSLContext ctx = SSLContext.getInstance("TLSv1");
		ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
		SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(ctx, new String[] {"TLSv1.2"}, null,
				new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session) {
						hostname = "172.24.24.42";
						return true;//SSLConnectionSocketFactory.getDefaultHostnameVerifier().verify(hostname, session);
					}
				});
		CloseableHttpClient httpclient = HttpClients.custom()
				.setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER).setSSLSocketFactory(ssf)
				.build();
		return httpclient;
	}
	
	public static PrivateKey getPrivateKey(String path) throws Exception {
		Base64 base64 = new Base64();
		byte[] buffer = base64.decode(getPem(path));
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return (PrivateKey) keyFactory.generatePrivate(keySpec);
	}

	private static String getPem(String path) throws Exception {
		FileInputStream fin = new FileInputStream(new File(DEVICE_KEY_APTH));
		BufferedReader br = new BufferedReader(new InputStreamReader(fin));
		String readLine = null;
		StringBuilder sb = new StringBuilder();
		while ((readLine = br.readLine()) != null) {
			if (readLine.charAt(0) == '-') {
				continue;
			} else {
				sb.append(readLine);
				sb.append('\r');
			}
		}
		fin.close();
		return sb.toString();
	}
}
