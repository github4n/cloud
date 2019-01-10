package com.iot.common.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.iot.common.util.CommonUtil;
import com.iot.common.util.StringUtil;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：常用工具
 * 功能描述：配置文件读取工具类
 * 创建人： mao2080@sina.com
 * 创建时间：2017年3月22日 上午9:16:10
 * 修改人： mao2080@sina.com
 * 修改时间：2017年3月22日 上午9:16:10
 */
public class PropertyConfigurerUtil extends PropertyPlaceholderConfigurer {

    /**
     * 日志
     */
    private final Log log = LogFactory.getLog(PropertyConfigurerUtil.class);

    /**
     * 存放Property信息
     */
    public static  Map<String, String> PROPERTY_MAP = new TreeMap<String, String>();

    /**
     * 日志名称
     */
    protected final Map<String, String> logerProperty;

    /**
     * Redis模块Key
     */
    protected final String redisKey;

    /**
     * 环境名称
     */
    protected String envirName;

    /**
     * 配置文件
     */
    protected Properties props;

    private Properties log4j;


    /**
     * 描述：构造函数
     *
     * @param logerProperty <日志名称,日志流>
     * @param redisKey      Redis模块Key
     * @author mao2080@sina.com
     * @created 2017年7月22日 下午4:15:37
     * @since
     */
    public PropertyConfigurerUtil(Map<String, String> logerProperty, String redisKey) {
        super();
        this.logerProperty = logerProperty;
        this.redisKey = redisKey;
    }

    /**
     * 描述：读取配置文件
     *
     * @param beanFactory BeanFactory
     * @param props       配置文件
     * @throws BeansException
     * @author mao2080@sina.com
     * @created 2017年7月22日 下午4:19:22
     * @since
     */
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
        try {
            this.props = props;
            this.envirName = props.getProperty("diamond.config.environment");
            this.props.setProperty("redis.key", StringUtil.toString(this.redisKey));

            String host = props.getProperty("diamond.config.ip");
            String port = props.getProperty("diamond.config.port");

            System.setProperty("diamond.config.ip", host);
            System.setProperty("diamond.config.port", port);

            String loggerType = this.getProperty("log4j.logger.logType");

            this.printLog(CommonUtil.getSystemLog("Diamond正在连接配置中心"));
            this.printLog(CommonUtil.getSystemLog("Diamond配置中心配置信息"));
            this.printLog(CommonUtil.getProperty("diamond.config.ip", host));
            this.printLog(CommonUtil.getProperty("diamond.config.port", port));
            this.printLog(CommonUtil.getProperty("diamond.config.environment", this.envirName));

            this.loadProperties(host, port);

            this.initLogger(loggerType);

            super.processProperties(beanFactory, this.props);

            System.setProperty("aws.accessKeyId", this.props.getProperty("aws.accessKeyId"));
            System.setProperty("aws.secretKey", this.props.getProperty("aws.secretKey"));
            PROPERTY_MAP.put("aws.accessKeyId", this.props.getProperty("aws.accessKeyId"));
            PROPERTY_MAP.put("aws.secretKey", this.props.getProperty("aws.secretKey"));
            PROPERTY_MAP.put("redis.key", StringUtil.toString(this.redisKey));
            PROPERTY_MAP.put("dubbo.protocol.port", this.props.getProperty("dubbo.protocol.port"));

            PropertyConfigurator.configure(log4j);

            this.printLog(CommonUtil.getSystemLog("Diamond配置中心连接成功"));
        } catch (Exception e) {
            log.error(CommonUtil.getSystemLog("Diamond配置中心连接失败"), e);
        }
    }

    /**
     * 描述：远程读取配置
     *
     * @param host ip
     * @param port 端口
     * @throws IOException
     * @author mao2080@sina.com
     * @created 2017年7月22日 下午4:40:41
     * @since
     */
    private void loadProperties(String host, String port) throws IOException {
        URL url = new URL("http://" + host + ":" + port + "/config/application-" + this.envirName + ".properties");
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
        String s = reader.readLine();
        String key;
        String val;
        while (StringUtils.isNotBlank(s)) {
            if (!s.startsWith("#")) {
                int pos = s.indexOf('=');
                key = null;
                val = null;
                if (pos > -1) {
                    key = s.substring(0, pos);
                    val = s.substring(pos + 1, s.length());
                }

                if (StringUtil.isNotEmpty(key)) {
                    PROPERTY_MAP.put(key, val);
                }

                if (!s.startsWith("log4j") && StringUtil.isNotEmpty(key) && StringUtil.isNotEmpty(val)) {
                    this.props.setProperty(key, val);
                } else if (!s.startsWith("log4j") && StringUtil.isNotEmpty(key) && StringUtil.isEmpty(val)) {
                    log.warn(key.concat("未配置..."));
                }
            }
        }
        reader.close();
    }

    /**
     * 描述：初始化日志服务
     *
     * @param loggerType 日志类别（Loger4j,Kafka,Kinesis）
     * @author mao2080@sina.com
     * @created 2017年7月22日 下午4:09:32
     * @since
     */
    private void initLogger(String loggerType) {
        String rootLogger = this.getProperty("log4j.rootLogger");
        String console = this.getProperty("log4j.appender.console");
        String target = this.getProperty("log4j.appender.console.target");
        String log4jLayout = this.getProperty("log4j.appender.console.layout");
        String log4jConversionPattern = this.getProperty("log4j.appender.console.layout.ConversionPattern");
        this.printLogProperty(rootLogger, console, target, log4jLayout, log4jConversionPattern);
        if (StringUtil.isEmpty(loggerType) || this.logerProperty == null || this.logerProperty.isEmpty()) {
            return;
        }
        if ("Kafka".equalsIgnoreCase(loggerType)) {
            setKafkaConfig();
            return;
        }
        if ("Kinesis".equalsIgnoreCase(loggerType)) {
            setKinesisConfig();
            return;
        }
    }

    /**
     * 设置kafka配置
     */
    private void setKafkaConfig() {
        for (Map.Entry<String, String> entry : this.logerProperty.entrySet()) {
            String logger = entry.getKey().concat("Logger");
            String kafka = entry.getKey().concat("Kafka");
            String log4jLogger = this.getProperty("log4j.logger.kafka").concat(",").concat(kafka);
            String additivity = this.getProperty("log4j.additivity.kafka");
            String brokerList = this.getProperty("log4j.appender.kafka.brokerList");
            String compressionType = this.getProperty("log4j.appender.kafka.compressionType");
            String syncSend = this.getProperty("log4j.appender.kafka.syncSend");
            String layout = this.getProperty("log4j.appender.kafka.layout");
            this.setProperty("log4j.logger." + logger, log4jLogger);
            this.setProperty("log4j.additivity." + logger, additivity);
            this.setProperty("log4j.appender." + kafka, "org.apache.kafka.log4jappender.KafkaLog4jAppender");
            this.setProperty("log4j.appender." + kafka + ".topic", entry.getValue());
            this.setProperty("log4j.appender." + kafka + ".brokerList", brokerList);
            this.setProperty("log4j.appender." + kafka + ".compressionType", compressionType);
            this.setProperty("log4j.appender." + kafka + ".syncSend", syncSend);
            this.setProperty("log4j.appender." + kafka + ".layout", layout);
        }
    }

    /**
     * 设置kinesis配置
     */
    private void setKinesisConfig() {
        for (Map.Entry<String, String> entry : this.logerProperty.entrySet()) {
            String logger = entry.getKey().concat("Logger");
            String kinesis = entry.getKey().concat("Kinesis");
            String log4jLogger = this.getProperty("log4j.logger.kinesis").concat(",").concat(kinesis);
            String additivity = this.getProperty("log4j.additivity.kinesis");
            String layout = this.getProperty("log4j.appender.kinesis.layout");
            String conversionPattern = this.getProperty("log4j.appender.kinesis.layout.ConversionPattern");
            String encoding = this.getProperty("log4j.appender.kinesis.encoding");
            String maxRetries = this.getProperty("log4j.appender.kinesis.maxRetries");
            String bufferSize = this.getProperty("log4j.appender.kinesis.bufferSize");
            String threadCount = this.getProperty("log4j.appender.kinesis.threadCount");
            String shutdownTimeout = this.getProperty("log4j.appender.kinesis.shutdownTimeout");
            String endpoint = this.getProperty("log4j.appender.kinesis.endpoint");
            String region = this.getProperty("log4j.appender.kinesis.region");
            this.setProperty("log4j.logger." + logger, log4jLogger);
            this.setProperty("log4j.additivity." + logger + "", additivity);
            this.setProperty("log4j.appender." + kinesis + "", "com.amazonaws.services.kinesis.log4j.KinesisAppender");
            this.setProperty("log4j.appender." + kinesis + ".layout", layout);
            this.setProperty("log4j.appender." + kinesis + ".layout.ConversionPattern", conversionPattern);
            this.setProperty("log4j.appender." + kinesis + ".streamName", entry.getValue());
            this.setProperty("log4j.appender." + kinesis + ".encoding", encoding);
            this.setProperty("log4j.appender." + kinesis + ".maxRetries", maxRetries);
            this.setProperty("log4j.appender." + kinesis + ".bufferSize", bufferSize);
            this.setProperty("log4j.appender." + kinesis + ".threadCount", threadCount);
            this.setProperty("log4j.appender." + kinesis + ".shutdownTimeout", shutdownTimeout);
            this.setProperty("log4j.appender." + kinesis + ".endpoint", endpoint);
            this.setProperty("log4j.appender." + kinesis + ".region", region);
        }
    }

    /**
     * 描述：打印Log4j基础配置
     *
     * @param rootLogger             rootLogger
     * @param console                console
     * @param target                 target
     * @param log4jLayout            layout
     * @param log4jConversionPattern conversionPattern
     * @author mao2080@sina.com
     * @created 2017年7月24日 上午9:23:43
     * @since
     */
    private void printLogProperty(String rootLogger, String console, String target, String log4jLayout, String log4jConversionPattern) {
        this.setProperty("log4j.logger.logType", this.getProperty("log4j.logger.logType"));
        this.setProperty("log4j.rootLogger", rootLogger);
        this.setProperty("log4j.appender.console", console);
        this.setProperty("log4j.appender.console.target", target);
        this.setProperty("log4j.appender.console.layout", log4jLayout);
        this.setProperty("log4j.appender.console.layout.ConversionPattern", log4jConversionPattern);
    }

    /**
     * 描述：远程获取配置文件value
     *
     * @param key 配置文件key
     * @return
     * @author mao2080@sina.com
     * @created 2017年7月22日 下午3:58:33
     * @since
     */
    private String getProperty(String key) {
        return PROPERTY_MAP.get(key);
    }

    /**
     * 描述：打印配置，并设置配置信息
     *
     * @param key   键
     * @param value 值
     * @author mao2080@sina.com
     * @created 2017年7月22日 下午4:08:57
     * @since
     */
    private void setProperty(String key, String value) {
        this.printLog(CommonUtil.getProperty(key, value));
        log4j = new Properties();
        log4j.put(key, value);
        this.props.setProperty(key, value);
    }

    /**
     * 描述：打印日志
     *
     * @param msg 消息内容
     * @author mao2080@sina.com
     * @created 2017年7月27日 下午4:41:19
     * @since
     */
    private void printLog(String msg) {
        log.info(msg);
    }

}
