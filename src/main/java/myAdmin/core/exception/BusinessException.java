package myAdmin.core.exception;

import myAdmin.common.ResultCode;

public class BusinessException extends RuntimeException{

    private static final long serialVersionUID = -3425326065308681632L;

    private ResultCode code = ResultCode.FAILED;

    private boolean isShow = true;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, ResultCode code) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message, ResultCode code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public ResultCode getCode() {
        return code;
    }

    public boolean isShow() {
        return isShow;
    }

    public BusinessException setShow(boolean isShow) {
        this.isShow = isShow;
        return this;
    }

    public BusinessException setCode(ResultCode code) {
        this.code = code;
        return this;
    }
}
