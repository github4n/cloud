package com.iot.common.exception;

/**
 * <p>业务层异常</p>
 *
 * @author xiangyitao
 * @version 1.00
 * @dateTime  21:07
 */
public class RemoteCallBusinessException extends BusinessException {

    private static final long serialVersionUID = 1L;



    public RemoteCallBusinessException(IBusinessException exception) {
        super(exception);
    }

    public RemoteCallBusinessException(IBusinessException exception, Object... args) {
        super(exception, args);
    }


    public RemoteCallBusinessException(IBusinessException exception, Throwable throwable, Object... args) {
        super(exception, throwable, args);
    }

    public RemoteCallBusinessException(IBusinessException exception, Throwable throwable) {
        super(exception, throwable);
    }
}
