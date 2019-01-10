package com.iot.building.helper;

import com.alibaba.fastjson.JSON;
import com.iot.building.utils.RedisKeyUtil;
import com.iot.building.utils.SrcAddrUtils;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.RemoteCallBusinessException;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.control.helper.DispatcherMessage;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 消息路由
 * 云端采取客户端订阅的方式获取客户端（APP/设备）发布的消息
 * 原先为云端拦截的方式进行路由
 *
 * @author fenglijian
 * @version 1.0
 */
public class DispatcherRouteHelper {

    private static final Log logger = LogFactory.getLog(DispatcherRouteHelper.class);

    public void dispatch(DispatcherMessage dispatcherMessage, String topic) {
        logger.info("class: DispatcherRouteHelper method： dispatch description: \ntopic: " + topic + ", message: " + JSON.toJSONString(dispatcherMessage));

        try {
            // 查询spring容器中所有的bean，测试用
			/*ApplicationContext  context = ApplicationContextHelper.getApplicationContext();
			System.out.println("-------------------");
			for(String beanName:context.getBeanDefinitionNames()){
				System.out.println(beanName);
			}
			System.out.println("++++++++++++++++++++");*/

            String serviceName = dispatcherMessage.getService();
            String methodName = dispatcherMessage.getMethod();
            methodName=MethodMaping.getRealMapingMethod(methodName);
            Object service = ApplicationContextHelper.getBean(serviceName);

            Object[] methodParameters = new Object[]{dispatcherMessage, topic};

            Class[] parameterClasses = new Class[]{DispatcherMessage.class, String.class};

            Method method = service.getClass().getMethod(methodName, parameterClasses);
            method.setAccessible(true);
            method.invoke(service, methodParameters);
        } catch (InvocationTargetException ite) {
            if (ite.getTargetException() instanceof BusinessException || ite.getTargetException() instanceof RemoteCallBusinessException) {
                logger.error("class: DispatcherRouteHelper method： dispatch description: invoke method throws Exception", ite.getTargetException());
            }
        } catch (Exception e) {
            logger.error("class: DispatcherRouteHelper method： dispatch description: invoke method failure");
            e.printStackTrace();
        }
    }

    public static void dispatch(Object service, MqttMsg mqttMsg) {
        if(mqttMsg == null){
            logger.error("***** 处理消息失败,因为 mqttMsg is null.");
            return ;
        }
        String topic = mqttMsg.getTopic();
        logger.info("class: DispatcherRouteHelper method： dispatch description: \ntopic: " + topic + ", message: " + JSON.toJSONString(mqttMsg));

        String seq = mqttMsg.getSeq();
        String methodName = mqttMsg.getMethod();
        // redis锁key
        String key = RedisKeyUtil.getMqttSeqKey(methodName, seq);
        try {
            // 重复执行seq判断
            if (!RedisCacheUtil.setNx(key, String.valueOf(System.currentTimeMillis()), RedisKeyUtil.MQTT_SEQ_EXPIRE_SECOND)) {
                logger.error("dispatch(), key=" + key + " is already deal, end of execution.");
                return ;
            }

            //通过srcAddr 获取相关用户信息
            String srcAddr = mqttMsg.getSrcAddr();
            SrcAddrUtils.analyzeSrcAddr(srcAddr);

            methodName = MethodMaping.getRealMapingMethod(methodName);
            Object[] methodParameters = new Object[]{mqttMsg, topic};
            Class[] parameterClasses = new Class[]{MqttMsg.class, String.class};

            Method method = service.getClass().getMethod(methodName, parameterClasses);
            method.setAccessible(true);
            method.invoke(service, methodParameters);
        } catch (InvocationTargetException ite) {
            if (ite.getTargetException() instanceof BusinessException || ite.getTargetException() instanceof RemoteCallBusinessException) {
                logger.error("class: DispatcherRouteHelper method： dispatch description: invoke method throws Exception", ite.getTargetException());
            }
        } catch (Exception e) {
            logger.error("class: DispatcherRouteHelper method： dispatch description: invoke method failure");
            e.printStackTrace();
        } finally {
            try {
                // 删除缓存的锁 key
                RedisCacheUtil.delete(key);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //saas 移除
            SaaSContextHolder.removeCurrentContextMap();
        }
    }
    
}
