package myAdmin.core.authority;

import javax.servlet.http.HttpServletRequest;

import myAdmin.common.ResultCode;
import myAdmin.config.AdminConfigure;
import myAdmin.core.exception.BusinessException;
import myAdmin.core.interceptor.MyInterceptorContextHolder;
import myAdmin.module.admin.bean.bo.UserCache;
import myAdmin.module.admin.service.AdminLoginService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Set;

/**
 * 切面方法校验权限
 */
@Aspect
@Component
public class AuthenticationAspect {

	@Autowired
	private AdminConfigure adminConfig;
	
	//切入点
	@Pointcut("@annotation(myAdmin.core.authority.Authentication)")
    public void test() {}
	
	@Before("test() && @annotation(param)")
	public void doBefore(JoinPoint joinPoint, Authentication param){
		
        Long user_id = MyInterceptorContextHolder.getUserId();
        if (user_id == null) {
        	throw new BusinessException(ResultCode.FORBIDDEN.getMessage(), ResultCode.FORBIDDEN);
		}
        Long userId = Long.valueOf(String.valueOf(user_id));
		
		String perm = param.value();
		if (!isContainAuthority(perm, userId)) {
			throw new BusinessException(ResultCode.FORBIDDEN.getMessage(), ResultCode.FORBIDDEN);
		}
	}
	
	//判断是否有当前用户是否有权限
	private boolean isContainAuthority(String perm, Long userId) {
		if (StringUtils.isBlank(perm)) {
			return true;
		}
		UserCache userCache = AdminLoginService.UserLoginCache.get(userId);
		if (ObjectUtils.isEmpty(userCache) || !userCache.getPermSet().contains(perm)) {
			return false;
		}
		return true;
	}
}
