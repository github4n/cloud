//package com.iot.device.service.impl;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.security.KeyFactory;
//import java.security.KeyManagementException;
//import java.security.KeyStore;
//import java.security.KeyStoreException;
//import java.security.NoSuchAlgorithmException;
//import java.security.PrivateKey;
//import java.security.SecureRandom;
//import java.security.UnrecoverableKeyException;
//import java.security.cert.CertificateException;
//import java.security.cert.CertificateFactory;
//import java.security.cert.X509Certificate;
//import java.security.spec.PKCS8EncodedKeySpec;
//
//import javax.net.ssl.KeyManagerFactory;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSocketFactory;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.TrustManagerFactory;
//
//import org.apache.commons.codec.binary.Base64;
//import org.eclipse.paho.client.mqttv3.MqttClient;
//import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
//import org.eclipse.paho.client.mqttv3.MqttException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class MqttSslServiceImpl{
//	
//	private static final Logger LOGER = LoggerFactory.getLogger(MqttSslServiceImpl.class);
//	
//    private String host;
//	
//    private String shPath;
//    
//    private String certPath;
//	
//	/**客服端证书-crt基础路径-读取配置*/
//    private String clientCrtPath;
//	
//	/**客服端证书-pem基础路径-读取配置*/
//	private String clientPemPath;
//	
//	public MqttClient client;
//	
//    private String devUUID;
//	
//    private String password;
//    
//	MqttSslServiceImpl(String host, String shPath, String certPath, String devUUID, String password,
//			String tempFileName) {
//		this.host = host;
//		this.shPath = shPath;
//		this.certPath = certPath;
//		this.clientCrtPath = certPath.concat(System.getProperty("file.separator"))
//				.concat(tempFileName + System.getProperty("file.separator") + devUUID + ".cert");
//		this.clientPemPath = certPath.concat(System.getProperty("file.separator"))
//				.concat(tempFileName + System.getProperty("file.separator") + devUUID + ".pem");
//		this.devUUID = devUUID;
//		this.password = password;
//		LOGER.info(this.host);
//		LOGER.info(this.shPath);
//		LOGER.info(this.certPath);
//		LOGER.info(this.clientCrtPath);
//		LOGER.info(this.clientPemPath);
//		LOGER.info(this.devUUID);
//		LOGER.info(this.password);
//	}
//	
//	public MqttClient getClient() throws MqttException {
//		try {
//			if (client == null) {
//				client = new MqttClient(host, devUUID);
//				MqttConnectOptions conOptions = new MqttConnectOptions();
//				conOptions.setUserName(devUUID);
//				conOptions.setPassword(password.toCharArray());
//				conOptions.setCleanSession(true);
//				SSLSocketFactory factory = getSSLSocktetX509("", null, null, "ldx2017");
//				conOptions.setSocketFactory(factory);
//				client.connect(conOptions);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return client;
//	}
//
//	/**
//	 * 
//	 * 描述：单向认证caKeyStroe:CA.jks
//	 * @author 李帅
//	 * @created 2018年6月4日 上午11:02:05
//	 * @since 
//	 * @param caKeyStroe
//	 * @param password
//	 * @return
//	 * @throws KeyManagementException
//	 * @throws NoSuchAlgorithmException
//	 * @throws UnrecoverableKeyException
//	 * @throws IOException
//	 * @throws CertificateException
//	 * @throws KeyStoreException
//	 */
//	public SSLSocketFactory configureSSLSocketFactory(String caKeyStroe, String password) throws KeyManagementException,
//			NoSuchAlgorithmException, UnrecoverableKeyException, IOException, CertificateException, KeyStoreException {
//		KeyStore ks = KeyStore.getInstance("JKS");
//		InputStream jksInputStream = getClass().getClassLoader().getResourceAsStream(caKeyStroe);
//		ks.load(jksInputStream, password.toCharArray());
//		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//		kmf.init(ks, password.toCharArray());
//		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//		tmf.init(ks);
//		SSLContext sc = SSLContext.getInstance("TLS");
//		TrustManager[] trustManagers = tmf.getTrustManagers();
//		sc.init(kmf.getKeyManagers(), trustManagers, null);
//		SSLSocketFactory ssf = sc.getSocketFactory();
//		return ssf;
//	}
//
//	public SSLSocketFactory getSSLSocktetX509(String caPath, String crtPath, String keyPath, String password) throws Exception {
//		java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//		CertificateFactory cAf = CertificateFactory.getInstance("X.509");
//		FileInputStream caIn = new FileInputStream(new File(shPath + System.getProperty("file.separator") + "CA_test.crt"));
//		X509Certificate ca = (X509Certificate) cAf.generateCertificate(caIn);
//		KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
//		caKs.load(null, null);
//		caKs.setCertificateEntry("ca-certificate", ca);
//		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//		tmf.init(caKs);
//		caIn.close();
//		CertificateFactory cf = CertificateFactory.getInstance("X.509");
//		FileInputStream crtIn = new FileInputStream(new File(clientCrtPath));
//		X509Certificate caCert = (X509Certificate) cf.generateCertificate(crtIn);
//		crtIn.close();
//		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
//		ks.load(null, null);
//		ks.setCertificateEntry("certificate", caCert);
//		ks.setKeyEntry("private-key", getPrivateKey(keyPath), password.toCharArray(), new java.security.cert.Certificate[] { caCert });
//		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//		kmf.init(ks, password.toCharArray());
//		SSLContext context = SSLContext.getInstance("TLSv1");
//		context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
//		return context.getSocketFactory();
//	}
//
//	public PrivateKey getPrivateKey(String path) throws Exception {
//		Base64 base64 = new Base64();
//		byte[] buffer = base64.decode(getPem(path));
//		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
//		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//		return (PrivateKey) keyFactory.generatePrivate(keySpec);
//	}
//
//	private String getPem(String path) throws Exception {
//		FileInputStream fin = new FileInputStream(new File(clientPemPath));
//		BufferedReader br = new BufferedReader(new InputStreamReader(fin));
//		String readLine = null;
//		StringBuilder sb = new StringBuilder();
//		while ((readLine = br.readLine()) != null) {
//			if (readLine.charAt(0) == '-') {
//				continue;
//			} else {
//				sb.append(readLine);
//				sb.append('\r');
//			}
//		}
//		fin.close();
//		return sb.toString();
//	}
//
//}