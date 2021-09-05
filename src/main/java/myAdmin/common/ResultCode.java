package myAdmin.common;

/**
 * 返回结果状态码
 */
public enum ResultCode {

    SUCCESS(200, "请求成功"),
    FAILED(500, "请求失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    EMPTY(600, "请求成功，没有找到数据");

    private long code;// 响应返回码
    private String message;// 返回码描述

    private ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
