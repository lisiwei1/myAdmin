package myAdmin.core.log;

import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import myAdmin.core.async.AsyncComp;
import myAdmin.core.request.MyRequestContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

@Aspect
@Component
public class LogAspect {

    @Autowired
    private AsyncComp asyncComp;

    /** @GetMapping 注解为切点 */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void logRequestMapping() {
        logInit();
    }

    /** @GetMapping 注解为切点 */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void logGetMapping() {
        logInit();
    }

    /** @PostMapping 注解为切点 */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void logPostMapping() {
        logInit();
    }

    /** @DeleteMapping 注解为切点 */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void logDeleteMapping() {
        logInit();

    }

    /** @PutMapping 注解为切点 */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void logPutMapping() {
        logInit();

    }

    /** @PatchMapping 注解为切点 */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public void logPatchMapping() {
        logInit();
    }

    /** @Scheduled 注解为切点 */
    @Pointcut("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void logScheduled() {
        logInit();
    }


    /** @LogOperation 注解为切点 */
    @Pointcut("@annotation(myAdmin.core.log.LogOperation)")
    public void logLogOperation() {
        logInit();
    }

    private void logInit() {}

    private static final String ASPECT_POINT = "logRequestMapping()||logGetMapping()||logPostMapping()||logDeleteMapping()||logPutMapping()||logPatchMapping()||logScheduled()||logLogOperation()";

    private static final String EXCLUDE_CLASS_NAMES = "^springfox\\.(.+)|^org.springframework\\.(.+)";

    /**
     * 在切点之前织入
     *
     * @param joinPoint
     * @throws Throwable
     */
    @Before(ASPECT_POINT)
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 开始打印请求日志

        String className = joinPoint.getSignature().getDeclaringTypeName();
        if (className.matches(EXCLUDE_CLASS_NAMES)) {
            return;
        }
        LogPackageHolder.init();
        LogPackage logPackage = LogPackageHolder.getLogPackage();
        parseAnnotation(logPackage, joinPoint);
        LogOperation logOperation = logPackage.getLogOperation();
        // 如果是跳过日志记录，则释放当前日志
        if (logOperation != null && logOperation.skipLog()) {
            LogPackageHolder.release();
            return;
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            logPackage.putVariable(LogVariableKey.URL, request.getRequestURL().toString());
            logPackage.putVariable(LogVariableKey.HTTP_METHOD, request.getMethod());
            logPackage.putVariable(LogVariableKey.IP, request.getRemoteAddr());
            //记录当前线程的请求IP
            MyRequestContextHolder.setIp(request.getRemoteAddr());
        }
        logPackage.putVariable(LogVariableKey.CLASS_NAME, className);
        logPackage.putVariable(LogVariableKey.CLASS_METHOD, joinPoint.getSignature().getName());
        logPackage.putVariable(LogVariableKey.REQUEST_PARAMS, getReqParams(joinPoint));
    }

    private Map<String, Object> getReqParams(JoinPoint joinPoint) {
        Map<String, Object> reqParam = Maps.newHashMap();
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < parameterNames.length; i++) {
            String parameterName = parameterNames[i];
            Object arg = args[i];
            if (arg instanceof HttpServletRequest) {
                reqParam.put(parameterName, ((HttpServletRequest) arg).getParameterMap());
            } else {
                reqParam.put(parameterName, arg);
            }
        }
        return reqParam;
    }

    /**
     * 在切点之后织入
     *
     * @throws Throwable
     */
    @After(ASPECT_POINT)
    public void doAfter() throws Throwable {}

    /**
     * 环绕
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around(ASPECT_POINT)
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            Object result = proceedingJoinPoint.proceed();
            LogPackage logPackage = LogPackageHolder.getLogPackage();
            if (logPackage != null) {
                logPackage.putVariable(LogVariableKey.RESPONSE_PARAMS, result);
            }
            return result;
        } catch (Throwable e) {
            LogPackage logPackage = LogPackageHolder.getLogPackage();
            if (logPackage != null) {
                logPackage.setThrowable(e);
            }
            throw e;
        } finally {
            LogPackage logPackage = LogPackageHolder.getLogPackage();
            if (logPackage != null) {
                asyncComp.execute(new Runnable() {

                    @Override
                    public void run() {
                        logPackage.print();

                    }
                });
            }
            LogPackageHolder.release();
        }
    }

    /**
     * 获取切面注解
     *
     * @param joinPoint
     *            切点
     * @return 描述信息
     * @throws Exception
     */
    public void parseAnnotation(LogPackage logPackage, JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    // 读取swagger注释
                    ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
                    if (apiOperation != null) {
                        logPackage.putVariable(LogVariableKey.DESC, apiOperation.value());
                    }
                    // 读取log操作注解
                    LogOperation logOperation = method.getAnnotation(LogOperation.class);
                    if (logOperation != null) {
                        logPackage.setLogOperation(logOperation);
                        if (StringUtils.isNotEmpty(logOperation.value())) {
                            logPackage.putVariable(LogVariableKey.DESC, logOperation.value());
                        }
                    }
                    break;
                }
            }
        }
    }
}
