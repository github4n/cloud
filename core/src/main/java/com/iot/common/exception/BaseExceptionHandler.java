package com.iot.common.exception;

import com.iot.common.constant.SystemConstants;
import com.iot.locale.LocaleMessageSourceService;
import com.netflix.client.ClientException;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author xiangyitao
 * @date 2017/11/03
 */
@RestControllerAdvice
public class BaseExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(BaseExceptionHandler.class);

    @Autowired
    private LocaleMessageSourceService localeMessageSourceService;

    /**
     * 添加全局异常处理流程，根据需要设置需要处理的异常，@Valid异常
     *
     * @param exception
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResult<String> methodArgumentNotValidHandler(MethodArgumentNotValidException exception) {
        logger.error("运行时异常", exception);
        exception.printStackTrace();
        //按需重新封装需要返回的错误信息
        StringBuffer errorString = new StringBuffer();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {

            String errorMessage = getMessage(new BusinessException(new IBusinessException() {
                @Override
                public int getCode() {
                    return 400;
                }

                @Override
                public String getMessageKey() {
                    return error.getDefaultMessage();
                }
            }));
            errorString.append("[" + error.getField() + ":" + errorMessage + "]");
        }
        return new ExceptionResult<>(errorString.toString());
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResult<String> handleIllegalArgumentException(IllegalArgumentException exception) {
        logger.info("运行时异常", exception);
        BusinessException businessException = new BusinessException(new IBusinessException() {
            @Override
            public int getCode() {
                return 400;
            }

            @Override
            public String getMessageKey() {
                return exception.getMessage();
            }
        });
        return new ExceptionResult<>(getMessage(businessException));
    }

    /**
     * 微服务业务逻辑异常捕获转换
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = MicroServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResult<String> handleMicroSeviceException(MicroServiceException exception) {
        logger.debug("MicroService business exception:", exception);
        return new ExceptionResult<>(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ExceptionHandler(value = RemoteCallBusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResult<String> handleRemoteCallBusinessException(RemoteCallBusinessException exception) {
        logger.debug("remote call business exception:", exception);
        return new ExceptionResult<>(exception.getCode(), exception.getMessage());
    }

    /**
     * 微服务调用异常捕获转换
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = HystrixBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResult<String> handleHystrixBadRequestException(HystrixBadRequestException exception) {
        logger.debug("hystrix bad request exception:", exception);
        return new ExceptionResult<>(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    /**
     * 业务逻辑捕获转换
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResult<String> handleBusinessException(BusinessException exception) {
        logger.debug("business exception:", exception);
        if (exception.getCause() != null) {
            return new ExceptionResult<>(exception.getCode(), getMessage(exception), null, exception.getCause());
        } else {
            return new ExceptionResult<>(exception.getCode(), getMessage(exception));
        }
    }

    @ExceptionHandler(value = HystrixRuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResult<String> handleHystrixRuntimeException(HystrixRuntimeException exception) {
        logger.error("HystrixRuntime异常:", exception);
        exception.printStackTrace();
        return new ExceptionResult<>(HttpStatus.BAD_REQUEST.value(), exception.getFallbackException().getCause().getCause().getMessage());
    }

    @ExceptionHandler(value = FeignException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResult<String> handleFeignException(FeignException exception) {
        logger.error("Feign异常:", exception);
        exception.printStackTrace();
        return new ExceptionResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Server.is.deserted");
    }

    @ExceptionHandler(value = ClientException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResult<String> handleFeignClientException(ClientException exception) {
        logger.error("handleFeignClientException:", exception);//Server is deserted
        exception.printStackTrace();
        return new ExceptionResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
    }

    /**
     * 异常捕获转换
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResult<String> handleException(Exception exception) {
        logger.error("异常:", exception);
        exception.printStackTrace();

        if(exception instanceof BadSqlGrammarException){
            exception = new Exception(SystemConstants.SYSTEM_ERROR_TIPS);
        }
        if (exception instanceof ClientException) {
            return new ExceptionResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Server.is.deserted");
        }
        if (exception.getCause() != null && exception.getCause() instanceof ClientException) {
            return new ExceptionResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Server.is.deserted");
        }
        return new ExceptionResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
    }

    public String getMessage(BusinessException exception) {
        String message = null;
        try {
            if (exception.getArgs() != null && exception.getArgs().length > 0) {
                message = localeMessageSourceService.getMessage(exception.getMessage(), exception.getArgs(), null);
            } else {
                message = localeMessageSourceService.getMessage(exception.getMessage(), null, null);
            }
        } catch (Exception e) {
            //TODO(xiangyitao) 本地找不到 文件对应的code /key
            if (exception.getArgs() != null && exception.getArgs().length > 0) {
                try {
                    //TODO(xiangyitao) 直接format 格式回去给用户
                    message = String.format(exception.getMessage(), exception.getArgs());
                } catch (Exception ex) {
                    //TODO(xiangyitao) FORMAT 格式出错
                    message = exception.getMessage();
                }
            } else {
                message = exception.getMessage();
            }
        }
        if (StringUtils.isEmpty(message)) {
            message = exception.getMessage();
        }
        return message;
    }
}
