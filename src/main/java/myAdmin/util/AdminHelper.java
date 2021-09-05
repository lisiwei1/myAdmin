package myAdmin.util;

import myAdmin.core.exception.BusinessException;
import myAdmin.core.interceptor.MyInterceptorContextHolder;

public class AdminHelper {

    /**
     * 获取请求人的userId
     * @return
     */
    public static Long getUserId() {
        Long userId = MyInterceptorContextHolder.getUserId();
        if (userId == null) {
            throw new BusinessException("未获取到用户信息");
        }
        return userId;
    }

}
