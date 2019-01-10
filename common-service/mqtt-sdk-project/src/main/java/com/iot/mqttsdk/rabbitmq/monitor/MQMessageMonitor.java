package com.iot.mqttsdk.rabbitmq.monitor;


import com.iot.mqttsdk.rabbitmq.model.RegisterModel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.SpringRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 项目名称：IOT云平台
 * 模块名称：mqsdk
 * 功能描述：消息监听
 * 创建人： 490485964@qq.com
 * 创建时间：2018年04月18日 10:52
 * 修改人： 490485964@qq.com
 * 修改时间：2018年04月18日 10:52
 */
@Component
public class MQMessageMonitor extends SpringRouteBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQMessageMonitor.class);
    @Override
    public void configure() throws Exception {

    }


    /**
     * 停止所有的动态路由 [这样才能真正MQ断开消费者]
     *
     * @return
     * @author lucky
     * @date 2018/8/27 14:30
     */
    public void stopRoutes() {
        try {
            super.getContext().stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 启用所有的动态路由
     *
     * @return
     * @author lucky
     * @date 2018/8/27 14:30
     */
    public void startRoutes() {
        try {
            super.getContext().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	/**
	 * 动态添加路由规则
	 * @param queueName 队列名称
	 * @param model 其他参数
	 */
	public void addRoutes(String queueName, RegisterModel model) throws Exception {
		RouteBuilder route = new RouteBuilder() {
			public void configure() throws Exception {
                try {
                    super.from(model.getQueueUrl()).routeId(queueName).process(model.getMqMessageProcessor()).log(queueName+" process end !");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }
            }
		};
		super.getContext().addRoutes(route);
	}
}
