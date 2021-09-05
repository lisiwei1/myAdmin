package myAdmin.core.exception;

import myAdmin.common.ResultCode;

/**
 * 底层框架异常
 */
public class CoreException extends RuntimeException {

    private static final long serialVersionUID = -3425326065308681632L;

    private ResultCode code = ResultCode.FAILED;

    public CoreException() {
        super();
    }

    public CoreException(String message) {
        super(message);
    }

    public CoreException(String message, ResultCode code) {
        super(message);
        this.code = code;
    }

    public CoreException(String message, ResultCode code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public CoreException(Throwable cause) {
        super(cause);
    }

    public ResultCode getCode() {
        return code;
    }
}
