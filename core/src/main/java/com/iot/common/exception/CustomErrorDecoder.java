package com.iot.common.exception;


import com.alibaba.fastjson.JSON;
import com.iot.common.util.JsonUtil;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>描述：错误转码业务异常</p>
 *
 * @author xiangyitao
 * @date 2017/12/03
 */
public class CustomErrorDecoder implements ErrorDecoder {

    private final Logger logger = LoggerFactory.getLogger(CustomErrorDecoder.class);

    @Override
    public Exception decode(String methodKey, Response response) {
//        logErrorResponse(response);
        logger.info("decode:{}, {}", methodKey, JSON.toJSONString(response));
        /**
         * 业务异常
         */
        int status = response.status();

        if (status < 400 || status >= 500) {
            return new ErrorDecoder.Default().decode(methodKey, response);
        }

        if (response.body() == null) {
            return new ErrorDecoder.Default().decode(methodKey, response);
        }

        //400-500之间错误码处理
        try {
            String body = Util.toString(response.body().asReader());
            ExceptionResult<String> exceptionResult = JsonUtil.fromJson(body, ExceptionResult.class);
            if (exceptionResult != null) {
                if (exceptionResult.getSubException() != null) {
                    return new RemoteCallBusinessException(new IBusinessException() {
                        @Override
                        public int getCode() {
                            return exceptionResult.getCode();
                        }

                        @Override
                        public String getMessageKey() {
                            return exceptionResult.getDesc();
                        }
                    }, exceptionResult.getSubException());
//                    return new MicroServiceException(exceptionResult.getDesc(), exceptionResult.getSubException());
                } else {
                    return new RemoteCallBusinessException(new IBusinessException() {
                        @Override
                        public int getCode() {
                            return exceptionResult.getCode();
                        }

                        @Override
                        public String getMessageKey() {
                            return exceptionResult.getDesc();
                        }
                    });
//                    return new MicroServiceException(exceptionResult.getDesc(), exceptionResult.getSubException());
                }
            }
        } catch (Exception ignored) {
            logger.error("exception:" + ignored);
        }

        return new ErrorDecoder.Default().decode(methodKey, response);
    }

    /*private void logErrorResponse(Response response) {
        int status = response.status();
        if (HttpStatus.SC_OK != status) {
            logger.debug(response.toString());
            try {
                logger.debug(Util.toString(response.body().asReader()));
            } catch (Exception e) {
                logger.error("exception:" + e);
            }
        }
    }*/
}
