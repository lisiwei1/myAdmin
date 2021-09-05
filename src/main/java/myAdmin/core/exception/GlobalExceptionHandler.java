package myAdmin.core.exception;

import myAdmin.common.ControllerResult;
import myAdmin.core.log.LogPackageHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(getClass().getName());

    /**
     * 底层框架异常
     * @param exception
     * @return
     */
    @ExceptionHandler(CoreException.class)
    public ControllerResult handleServiceException(CoreException exception) {
        logger.error(exception.getMessage(), exception);
        LogPackageHolder.compensateLogException(exception);
        return ControllerResult.buildFailedResult(exception.getCode(), exception.getMessage());
    }

    /**
     * 业务异常
     * @param exception
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public ControllerResult handleServiceException(BusinessException exception){
        if (logger.isDebugEnabled()){
            if (exception.isShow()){
                logger.error(exception.getMessage(), exception);
            }
        }else {
            logger.error(exception.getMessage());
        }
        LogPackageHolder.compensateLogException(exception);
        return ControllerResult.buildFailedResult(exception.getCode(), exception.getMessage());
    }

    /**
     * 其他异常
     * @param exception
     * @return
     */
    @ExceptionHandler(Throwable.class)
    public ControllerResult handleException(Throwable exception) {
        logger.error(exception.getMessage(), exception);
        //TODO 记录日志
        return ControllerResult.failed(exception.getMessage());
    }

    /**
     * 处理 form data方式调用接口校验失败抛出的异常
     * @param exception
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST) //设置状态码为 400
    @ExceptionHandler(BindException.class)
    public ControllerResult bindExceptionHandler(BindException exception) {
        logger.error(exception.getMessage(), exception);
        BindingResult exceptionResult = exception.getBindingResult();
        // 判断异常中是否有错误信息，如果存在就使用异常中的消息，否则使用默认消息
        if (exceptionResult.hasErrors()) {
            List<ObjectError> errors = exceptionResult.getAllErrors();
            if (!errors.isEmpty()) {
                List<String> errMsgList = errors.stream().map(e->e.getDefaultMessage()).collect(Collectors.toList());
                return ControllerResult.failed(String.join(",", errMsgList));
            }
        }
        return ControllerResult.failed("请求参数错误");
    }

    /**
     * 处理 json 请求体调用接口校验失败抛出的异常
     * @param exception
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST) //设置状态码为 400
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ControllerResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
        logger.error(exception.getMessage(), exception);
        BindingResult exceptionResult = exception.getBindingResult();
        // 判断异常中是否有错误信息，如果存在就使用异常中的消息，否则使用默认消息
        if (exceptionResult.hasErrors()) {
            List<ObjectError> errors = exceptionResult.getAllErrors();
            if (!errors.isEmpty()) {
                List<String> errMsgList = errors.stream().map(e->e.getDefaultMessage()).collect(Collectors.toList());
                return ControllerResult.failed(String.join(",", errMsgList));
            }
        }
        return ControllerResult.failed("请求参数错误");
    }

    /**
     * 处理单个参数校验失败抛出的异常
     * @param exception
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST) //设置状态码为 400
    @ExceptionHandler(ConstraintViolationException.class)
    public ControllerResult constraintViolationExceptionHandler(ConstraintViolationException exception) {
        logger.error(exception.getMessage(), exception);
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        if (!constraintViolations.isEmpty()) {
            List<String> errMsgList = constraintViolations.stream().map(e->e.getMessage()).collect(Collectors.toList());
            return ControllerResult.failed(String.join(",", errMsgList));
        }
        return ControllerResult.failed("请求参数错误");
    }
}
