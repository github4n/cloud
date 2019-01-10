package com.iot.smarthome.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Descrpiton:
 *      http 工具类
 *
 * @Author: yuChangXing
 * @Date: 2018/12/19 14:17
 * @Modify by:
 */
public class HttpUtils {

    public final static String RESULT_FLAG_KEY = "result_flag";
    public final static String RESULT_CONTENT_KEY = "result_content";
    public final static String RESULT_FLAG_TRUE = "true";
    public final static String RESULT_FLAG_FALSE = "false";
    // -- Content Type 定义 --//
    public static final String TEXT_TYPE = "text/plain";
    public static final String JSON_TYPE = "application/json";
    public static final String XML_TYPE = "text/xml";
    public static final String HTML_TYPE = "text/html";
    public static final String JS_TYPE = "text/javascript";
    public static final String EXCEL_TYPE = "application/vnd.ms-excel";
    // 设置从connect Manager获取Connection 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的
    public static final int CONNECTION_REQUEST_TIMEOUT = 2 * 1000;
    // 默认连接超时时间(毫秒)
    public static final int CONNECT_TIMEOUT = 5 * 1000;
    // 请求获取数据的超时时间，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用
    public static final int SOCKET_TIMEOUT = 5 * 1000;
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    private static PoolingHttpClientConnectionManager connMgr = null;
    private static CloseableHttpClient httpClient = null;

    static {
        // 这个是控制并发量的，两个都要设置
        connMgr = new PoolingHttpClientConnectionManager();

        // 默认值为 20
        connMgr.setMaxTotal(200);           // 设置整个连接池最大连接数 根据自己的场景决定

        // 默认值为 2
        connMgr.setDefaultMaxPerRoute(20); //是路由的默认最大连接（该值默认为2），限制数量实际使用DefaultMaxPerRoute并非MaxTotal。
        //设置过小无法支持大并发(ConnectionPoolTimeoutException: Timeout waiting for connection from pool)，路由是对maxTotal的细分。

        httpClient = HttpClients.custom().setConnectionManager(connMgr).build();
    }

    /**
     * 处理HTTP请求
     * 基于org.apache.http.client包做了简单的二次封装
     *
     * @param method                   HTTP请求类型
     * @param url                      请求对应的URL地址
     * @param paramData                请求所带参数(有可能为 sting 、 Map<string,strong>)
     * @param headers
     * @param requestCharset           请求参数编码
     * @param responseCharset          返回结果编码
     * @param connectionRequestTimeout 设置从connect Manager获取Connection 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的
     * @param connectTimeout           设置连接超时时间，单位毫秒
     * @param socketTimeout            请求获取数据的超时时间，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用
     * @return
     */
    private static String doRequest(final RequestMethod method, final String url,
                                    final Object paramData, final Map<String, String> headers,
                                    final String requestCharset, final String responseCharset,
                                    int connectionRequestTimeout, int connectTimeout, int socketTimeout) throws Exception {
        //如果url没有传入，则直接返回
        if (null == url || url.isEmpty()) {
            logger.warn("The url is null or empty!!You must give it to me!OK?");
            return null;
        }

        //logger.info("-----------------请求地址:{}-----------------", url);

        //配置请求参数
        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout).build();

        /*CloseableHttpClient httpClient = HttpClients.custom()
                //.setConnectionManager(connMgr)
                .setDefaultRequestConfig(config).build();*/

        HttpUriRequest request = null;
        switch (method) {
            case GET:
                String getUrl = url;
                if (null != paramData && paramData instanceof String) {
                    getUrl += "?" + (String) paramData;
                } else if (null != paramData && paramData instanceof Map) {
                    getUrl = generateHttpGetUrl(url, (Map) paramData);
                }

                request = new HttpGet(getUrl);
                HttpGet httpGet = ((HttpGet) request);
                httpGet.setConfig(config);      //配置请求参数

                break;

            case POST:
                //logger.info("请求入参:");
                //logger.info(paramData.toString());
                request = new HttpPost(url);

                HttpPost httpPost = ((HttpPost) request);
                httpPost.setConfig(config);     //配置请求参数

                if (null != paramData && paramData instanceof String) {
                    StringEntity stringEntity = new StringEntity((String) paramData, requestCharset);
                    httpPost.setEntity(stringEntity);
                } else if (null != paramData && paramData instanceof Map) {
                    generateHttpPostParams(httpPost, (Map) paramData, requestCharset);
                }

                generateHttpPostHeaders(httpPost, headers);

                break;
            case PUT:
            case DELETE:
            default:
                logger.warn("-----------------请求类型:{} 暂不支持-----------------", method.toString());
                break;
        }

        CloseableHttpResponse response = null;
        try {
            long start = System.currentTimeMillis();
            //发起请求
            response = httpClient.execute(request);

            long time = System.currentTimeMillis() - start;
            //logger.info("本次请求'{}'耗时:{}ms", url.substring(url.lastIndexOf("/") + 1, url.length()), time);

            // 返回结果
            String resultContent = null;
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                resultContent = EntityUtils.toString(entity, responseCharset);  //不能重复调用此方法，IO流已关闭

                int resultCode = response.getStatusLine().getStatusCode();
                //返回码200，请求成功；其他情况都为请求出现错误
                if (HttpStatus.SC_OK == resultCode) {
                    //logger.info("-----------------请求成功-----------------");
                    //logger.info("响应结果:");
                    //logger.info(resultContent);
                } else {
                    logger.warn("-----------------请求出现错误，错误码:{}-----------------", resultCode);
                }

                // Consume response content
                EntityUtils.consume(entity);
            }

            return resultContent;
        } catch (ClientProtocolException e) {
            logger.error("ClientProtocolException:", e);
            logger.warn("-----------------请求出现异常:{}-----------------", e.toString());
            throw new Exception(e.getMessage());
        } catch (IOException e) {
            logger.error("IOException:", e);
            logger.warn("-----------------请求出现IO异常:{}-----------------", e.toString());
            throw new Exception(e.getMessage());
        } catch (Exception e) {
            logger.error("Exception:", e);
            logger.warn("-----------------请求出现其他异常:{}-----------------", e.toString());
            throw new Exception(e.getMessage());
        } finally {
            //abort the request
            if (null != request && !request.isAborted()) {
                // Do not need the rest
                request.abort();
            }

            if (request instanceof HttpGet) {
                ((HttpGet) request).releaseConnection();
            } else if (request instanceof HttpPost) {
                ((HttpPost) request).releaseConnection();
            }

            //close the connection
            //HttpClientUtils.closeQuietly(httpClient);     // 执行会 导致下一次httpClient调用报 Connection pool shut down
            //HttpClientUtils.closeQuietly(response);
        }
    }

    /**
     * 简单的 post方法请求
     *
     * @param url
     * @param content
     * @return
     */
    public static String simplePostMethod(String url, String content) {
        String resultContent = null;
        try {
            resultContent = doRequest(RequestMethod.POST, url, content,
                    null, DEFAULT_CHARSET, DEFAULT_CHARSET,
                    CONNECTION_REQUEST_TIMEOUT, CONNECT_TIMEOUT, SOCKET_TIMEOUT);
        } catch (Throwable e) {
            logger.error("simplePostMethod request请求异常，url: " + url, e);
        }

        return resultContent;
    }

    /**
     * 简单的 post方法请求
     *
     * @param url
     * @param content
     * @param headers
     * @return
     */
    public static Map<String, String> simplePostMethod(String url, String content, Map<String, String> headers) {
        return simplePostMethod(url, content, headers, DEFAULT_CHARSET, DEFAULT_CHARSET);
    }

    /**
     * 简单的 post方法请求
     *
     * @param url
     * @param content
     * @param headers
     * @param defaultRequestCharset
     * @return
     */
    public static Map<String, String> simplePostMethod(String url, String content, Map<String, String> headers,
                                                       String defaultRequestCharset) {
        return simplePostMethod(url, content, headers, defaultRequestCharset, DEFAULT_CHARSET);
    }

    /**
     * 简单的 post方法请求
     *
     * @param url
     * @param content
     * @param headers
     * @param defaultRequestCharset
     * @param defaultResponseCharset
     * @return
     */
    public static Map<String, String> simplePostMethod(String url, String content, Map<String, String> headers,
                                                       String defaultRequestCharset,
                                                       String defaultResponseCharset) {
        Map<String, String> result = new HashMap<String, String>();
        try {
            String resultContent = doRequest(RequestMethod.POST, url, content,
                    headers, defaultRequestCharset, defaultResponseCharset,
                    CONNECTION_REQUEST_TIMEOUT, CONNECT_TIMEOUT, SOCKET_TIMEOUT);

            if (resultContent != null) {
                generateHttpResult(result, true, resultContent.replaceAll("\r", ""));
            }
        } catch (Throwable e) {
            logger.error("simplePostMethod request请求异常，url: " + url, e);
            generateHttpResult(result, false, e.getMessage());
        }
        return result;
    }

    /**
     * post方法请求
     *
     * @param url
     * @param params
     * @return
     */
    public static Map<String, String> postMethod(String url, Map<String, String> params) {
        return postMethod(url, null, params);
    }

    /**
     * post方法请求
     *
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static Map<String, String> postMethod(String url, Map<String, String> headers,
                                                 Map<String, String> params) {
        return postMethod(url, headers, params, DEFAULT_CHARSET);
    }

    /**
     * post方法请求
     *
     * @param url
     * @param headers
     * @param params
     * @param defaultRequestCharset
     * @return
     */
    public static Map<String, String> postMethod(String url, Map<String, String> headers,
                                                 Map<String, String> params, String defaultRequestCharset) {
        return postMethod(url, headers, params, defaultRequestCharset, DEFAULT_CHARSET);
    }

    /**
     * post方法请求
     *
     * @param url
     * @param headers
     * @param params
     * @param defaultRequestCharset
     * @param defaultResponseCharset
     * @return
     */
    public static Map<String, String> postMethod(String url, Map<String, String> headers,
                                                 Map<String, String> params,
                                                 String defaultRequestCharset,
                                                 String defaultResponseCharset) {
        Map<String, String> result = new HashMap<String, String>();
        try {
            String resultContent = doRequest(RequestMethod.POST, url, params,
                    headers, defaultRequestCharset, defaultResponseCharset,
                    CONNECTION_REQUEST_TIMEOUT, CONNECT_TIMEOUT, SOCKET_TIMEOUT);

            if (resultContent != null) {
                generateHttpResult(result, true, resultContent.replaceAll("\r", ""));
            }
        } catch (Throwable e) {
            logger.error("postMethod request请求异常，url: " + url, e.getMessage());
            generateHttpResult(result, false, e.getMessage());
        }
        return result;
    }

    /**
     * 生成httppost头部信息
     *
     * @param httpPost
     * @param headers
     */
    private static void generateHttpPostHeaders(HttpPost httpPost, Map<String, String> headers) {
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                httpPost.setHeader(key, headers.get(key));
            }
        }
    }

    /**
     * 生成httppost数据(参数)
     *
     * @param httpPost
     * @param params
     * @param defaultResponseCharset
     * @throws UnsupportedEncodingException
     */
    private static void generateHttpPostParams(HttpPost httpPost, Map<String, String> params,
                                               String defaultResponseCharset) throws UnsupportedEncodingException {
        if (params != null && params.size() > 0) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            StringBuffer temp = new StringBuffer();
            for (String key : params.keySet()) {
                nvps.add(new BasicNameValuePair(key, params.get(key)));
                temp = temp.append(key).append("=").append(params.get(key)).append("&");
            }
//            logger.info("请求参数："+httpPost.getURI()+"?"+temp.toString());
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, defaultResponseCharset));
        }
    }

    /**
     * get方法请求
     *
     * @param url
     * @return
     */
    public static String getMethod(String url) {
        String result = null;
        try {
            String resultContent = doRequest(RequestMethod.GET, url, null,
                    null, DEFAULT_CHARSET, DEFAULT_CHARSET,
                    CONNECTION_REQUEST_TIMEOUT, CONNECT_TIMEOUT, SOCKET_TIMEOUT);

            if (resultContent != null) {
                result = resultContent.replaceAll("\r", "");
            }
        } catch (Throwable e) {
            logger.error("GET请求异常，url: " + url, e);
            result = null;
        }

        return result;
    }

    /**
     * 带参数的 get方法请求
     *
     * @param url
     * @param params
     * @return
     */
    public static Map<String, String> getMethod(String url, Map<String, String> params) {
        Map<String, String> result = new HashMap<String, String>();
        try {
            String resultContent = doRequest(RequestMethod.GET, url, params,
                    null, DEFAULT_CHARSET, DEFAULT_CHARSET,
                    CONNECTION_REQUEST_TIMEOUT, CONNECT_TIMEOUT, SOCKET_TIMEOUT);

            if (resultContent != null) {
                generateHttpResult(result, true, resultContent.replaceAll("\r", ""));
            }
        } catch (Throwable e) {
            logger.error("GET请求异常，url: " + url, e);
            generateHttpResult(result, false, e.getMessage());
        }
        return result;
    }


    /**
     * 把参数 拼接到 url后面
     *
     * @param url
     * @param params
     * @return
     */
    private static String generateHttpGetUrl(String url, Map<String, String> params) {
        if (params != null && params.size() > 0) {
            url += url.indexOf("?") > -1 ? "&" : "?timestamp=" + System.currentTimeMillis() + "&";
            for (String key : params.keySet()) {
                url += (key + "=" + params.get(key) + "&");
            }
            return url.substring(0, url.length() - 1);
        }
        return url;
    }

    /**
     * 填充结果数据
     *
     * @param result
     * @param flag
     * @param message
     */
    private static void generateHttpResult(Map<String, String> result, boolean flag, String message) {
//        logger.info("http response status：{}, response: {}", flag, message);
        result.put(RESULT_FLAG_KEY, flag ? RESULT_FLAG_TRUE : RESULT_FLAG_FALSE);
        result.put(RESULT_CONTENT_KEY, message);
    }

    /**
     * 判断http请求结果是否正确
     *
     * @param result
     * @return
     */
    public static boolean isSuccess(Map<String, String> result) {
        return result.get(RESULT_FLAG_KEY).equals(RESULT_FLAG_TRUE) ? true : false;
    }

    /**
     * 判断http请求结果是否失败
     *
     * @param result
     * @return
     */
    public static boolean isFail(Map<String, String> result) {
        return result.get(RESULT_FLAG_KEY).equals(RESULT_FLAG_FALSE) ? true : false;
    }

    /**
     * 获取返回内容
     *
     * @param result
     * @return
     */
    public static String responseContent(Map<String, String> result) {
        return result.get(RESULT_CONTENT_KEY);
    }


    /**
     * 获取表单中 复选框(Check控件) 参数的值
     */
    public static boolean isTrue(HttpServletRequest request, String paramName) {
        String value = request.getParameter(paramName);
        return value != null && (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("t") || value.equalsIgnoreCase("1") || value.equalsIgnoreCase("enabled") || value.equalsIgnoreCase("y") || value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("on"));
    }

    /**
     * 获取表单中 复选框(Check控件) 参数的值
     */
    public static boolean isTrue(String paramValue) {
        return paramValue != null && (paramValue.equalsIgnoreCase("true") || paramValue.equalsIgnoreCase("t") || paramValue.equalsIgnoreCase("1") || paramValue.equalsIgnoreCase("enabled") || paramValue.equalsIgnoreCase("y") || paramValue.equalsIgnoreCase("yes") || paramValue.equalsIgnoreCase("on"));
    }
}
