package com.iot.payment.rabbitmq;

import com.iot.payment.dao.PaymentMapper;
import com.iot.payment.entity.PayTransation;
import com.iot.payment.enums.PayStatus;
import com.rabbitmq.client.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeoutException;

/**
 * 
 * 项目名称：IOT云平台 模块名称： 功能描述：用于监听退款 创建人： wujianlong 创建时间：2018年1月18日 下午3:21:30
 * 修改人： wujianlong 修改时间：2018年1月18日 下午3:21:30
 */
@Component
public class PaymentConsumerListener {

	private static final Logger logger = LoggerFactory.getLogger(PaymentConsumerListener.class);

	private static ConnectionFactory factory;

	private static Connection connection;

	private static Channel channel;

	@Value("${spring.rabbitmq.host}")
	private String host;

	@Value("${spring.rabbitmq.port}")
	private int port;

	@Value("${spring.rabbitmq.username}")
	private String username;

	@Value("${spring.rabbitmq.password}")
	private String password;

	@Value("${paypal.clientID}")
	private String clientID;

	@Value("${paypal.clientSecret}")
	private String clientSecret;

	@Value("${paypal.mode}")
	private String mode;

	@Value("${paypal.tokenUrl}")
	private String tokenUrl;

	@Value("${paypal.paymentUrl}")
	private String paymentUrl;

	@Autowired
	private PaymentMapper paymentMapper;

	@PostConstruct
	private void init() {
		logger.info(String.format("host =%s, port=%d,  username=%s, password=%s ", host, port, username, password));

		factory = new ConnectionFactory();
		factory.setUsername(username);
		factory.setPassword(password);
		factory.setHost(host);
		factory.setPort(port);
		try {
			factory.setAutomaticRecoveryEnabled(true);
			factory.setNetworkRecoveryInterval(10000);
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.exchangeDeclare("payment", "direct", true, false, null);
			channel.queueDeclare("appVerify", true, false, false, null);
			channel.queueBind("appVerify", "payment", "appPay");
		} catch (IOException e) {
			logger.error("create rabbitmq connection fail.. ", e);
		} catch (TimeoutException e) {
			logger.error("create rabbitmq connection fail.. ", e);
		}
		// 启动消费
		startConsume();
	}

	/**
	 * 
	 * 描述：启动消费
	 * 
	 * @author zhouzongwei
	 * @created 2017年12月11日 下午3:25:44
	 * @since
	 */
	private void startConsume() {
		try {
			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
					// 处理消息
					if (message != null) {
						try {
							process(message);
						} catch (Exception e) {
							logger.error("app verify fail...", e);
						}
					}
				}
			};
			channel.basicConsume("appVerify", true, consumer);
		} catch (IOException e) {
			logger.error("app verify fail...", e);
		}
	}

	/**
	 * 
	 * 描述：处理app数据校验
	 * 
	 * @author zhouzongwei
	 * @created 2017年12月11日 下午5:06:39
	 * @since
	 * @param message
	 * @throws Exception 
	 */
	private void process(String message) throws Exception {
		String[] apps = message.split(",");
		
		System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
		String str = getPayment(apps[2]);
		JSONObject detail = JSONObject.fromObject(str);
		
		String tradeId = null;
		// 对app进行数据，比如金额校验
		//校验订单是否完成
		if("approved".equals(detail.get("state"))){
			
			JSONObject transactions = detail.optJSONArray("transactions").optJSONObject(0);
			JSONObject amount = transactions.optJSONObject("amount");
			JSONArray relatedResources = transactions.optJSONArray("related_resources");
			//从数据库查询支付总金额与Paypal校验支付总金额
			if( Double.valueOf(apps[3]) == amount.optDouble("total") || apps[1].equals(amount.optString("currency"))){
				for (int i = 0,j = relatedResources.size(); i < j; i++) {
					JSONObject sale = relatedResources.optJSONObject(i).optJSONObject("sale");
					if( sale != null && "completed".equals(sale.optString("state")) ){
						tradeId = sale.optString("id");
					}
				}
			} 
		}
		
		
		if(StringUtils.isNotBlank(tradeId)) {
			PayTransation payTransation = this.paymentMapper.getPayTransationById(null, null,apps[0]);
			payTransation.setPayStatus(PayStatus.ALREADY_PAY.getCode());
			payTransation.setTradeId(tradeId);
			this.paymentMapper.updatePayTransation(payTransation);
		}
	}
	
	private String getAccessToken() throws Exception{
		URL url = new URL(tokenUrl);
		String authorization = clientID+":"+clientSecret;
		authorization = Base64.encodeBase64String(authorization.getBytes());
		
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        conn.setRequestMethod("POST");// 提交模式
        //设置请求头header
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Accept-Language", "en_US");
        conn.setRequestProperty("Authorization", "Basic "+authorization);
        // conn.setConnectTimeout(10000);//连接超时 单位毫秒
        // conn.setReadTimeout(2000);//读取超时 单位毫秒
        conn.setDoOutput(true);// 是否输入参数
        String params = "grant_type=client_credentials";
        conn.getOutputStream().write(params.getBytes());// 输入参数
        
        InputStreamReader inStream = new InputStreamReader(conn.getInputStream());
        BufferedReader reader = new BufferedReader(inStream);
        StringBuilder result = new StringBuilder();
        String lineTxt = null;
        while((lineTxt = reader.readLine()) != null){
        	result.append(lineTxt);
        }
        reader.close();
        return JSONObject.fromObject(result.toString()).optString("access_token");
	}


	private String getPayment(String payId) throws Exception{
		URL url = new URL(paymentUrl+payId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        conn.setRequestMethod("GET");// 提交模式
        //设置请求头header
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer "+getAccessToken());
        // conn.setConnectTimeout(10000);//连接超时 单位毫秒
        // conn.setReadTimeout(2000);//读取超时 单位毫秒
        InputStreamReader inStream = new InputStreamReader(conn.getInputStream());
        BufferedReader reader = new BufferedReader(inStream);
        StringBuilder result = new StringBuilder();
        String lineTxt = null;
        while((lineTxt = reader.readLine()) != null){
        	result.append(lineTxt);
        }
        reader.close();
        return result.toString();
	}

}
