package myAdmin.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "返回响应数据")
public class ControllerResult<T> {

    @ApiModelProperty(value = "响应状态码")
    private long code;
    @ApiModelProperty(value = "错误消息")
    private String message;
    @ApiModelProperty(value = "响应对象")
    private T data;

    protected ControllerResult() {
        super();
    }

    protected ControllerResult(long code, String message, T data) {
        super();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return code == ResultCode.SUCCESS.getCode();
    }

    public boolean isEmpty() {
        return code == ResultCode.EMPTY.getCode();
    }

    public boolean isSuccessOrEmpty() {
        return isSuccess() || isEmpty();
    }

    /**
     * 返回成功结果和默认的成功消息
     * @param <T> 返回结果数据类型
     * @param data 返回结果数据内容
     * @return  返回响应数据
     */
    public static <T> ControllerResult<T> success(T data){
        return new ControllerResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 返回成功结果和自定义的消息
     * @param message   返回自定义的消息
     * @param data      返回结果数据内容
     * @param <T>       返回结果数据类型
     * @return  返回响应数据
     */
    public static <T> ControllerResult<T> success(String message, T data){
        return new ControllerResult<T>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 返回失败结果和自定义失败消息
     * @param resultCode    返回结果状态码
     * @param message       返回失败消息
     * @param <T>           返回结果数据类型
     * @return
     */
    public  static <T> ControllerResult<T> buildFailedResult(ResultCode resultCode, String message){
        return new ControllerResult<T>(resultCode.getCode(), message, null);
    }

    /**
     * 返回失败结果和默认的失败消息
     * @param resultCode
     * @param <T>
     * @return
     */
    private static <T> ControllerResult<T> buildFailedResult(ResultCode resultCode){
        return buildFailedResult(resultCode, resultCode.getMessage());
    }

    /**
     * 返回失败结果和默认的失败消息
     * @param <T>
     * @return
     */
    public static <T> ControllerResult<T> failed(){
        return buildFailedResult(ResultCode.FAILED, ResultCode.FAILED.getMessage());
    }

    /**
     * 返回失败结果和自定义的失败消息
     * @param message
     * @param <T>
     * @return
     */
    public static <T> ControllerResult<T> failed(String message){
        return buildFailedResult(ResultCode.FAILED, message);
    }

    /**
     * 请求成功但没有数据
     * @param message
     * @param <T>
     * @return
     */
    public static <T> ControllerResult<T> empty(String message) {
        return new ControllerResult<T>(ResultCode.EMPTY.getCode(), message, null);
    }

    /**
     * 请求成功但没有数据
     * @param <T>
     * @return
     */
    public static <T> ControllerResult<T> empty() {
        return empty(ResultCode.EMPTY.getMessage());
    }

    public long getCode() {
        return code;
    }
    public void setCode(long code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
}
