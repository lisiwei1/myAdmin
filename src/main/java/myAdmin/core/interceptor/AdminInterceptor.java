package myAdmin.core.interceptor;

import myAdmin.common.ResultCode;
import myAdmin.config.AdminConfigure;
import myAdmin.module.admin.bean.bo.UserCache;
import myAdmin.module.admin.service.AdminLoginService;
import myAdmin.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Autowired
    private AdminConfigure adminConfigure;
    @Autowired
    private AdminLoginService adminLoginService;

    /**
     * 在请求到达Controller控制器之前 通过拦截器执行一段代码
     * 如果方法返回true,继续执行后续操作
     * 如果返回false，执行中断请求处理，请求不会发送到Controller
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取token
        String token = request.getHeader("token");
        if (token == null) {
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().print("{\"code\": " + ResultCode.FAILED.getCode() + ", \"message\": \"token不能为空\"}");
            return false;
        }
        //AES解密获取用户信息
        String token_original = AESUtil.AESDncode(adminConfigure.getAesKey(), token);
        if (token_original == null) {
            //暂未登录或token已经过期
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().print("{\"code\": " + ResultCode.UNAUTHORIZED.getCode() + ", \"message\": \"" + ResultCode.UNAUTHORIZED.getMessage() + "\"}");
            return false;
        }
        int index = token_original.lastIndexOf(".");
        Long userId = Long.valueOf(token_original.substring(index + 1));
        UserCache userCache =  AdminLoginService.UserLoginCache.get(userId);
        if (userId == null || userCache == null || !token.equals(userCache.getToken())) {
            //暂未登录或token已经过期
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().print("{\"code\": " + ResultCode.UNAUTHORIZED.getCode() + ", \"message\": \"" + ResultCode.UNAUTHORIZED.getMessage() + "\"}");
            return false;
        }
        //刷新缓存有效时间
        adminLoginService.refreshLoginState(userId, userCache);
        MyInterceptorContextHolder.setUserId(userId);
        return true;
    }

    /**
     * 控制器之后，跳转前
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    /**
     * 跳转之后执行
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清除ThreadLocal
        MyInterceptorContextHolder.clear();
    }

}
