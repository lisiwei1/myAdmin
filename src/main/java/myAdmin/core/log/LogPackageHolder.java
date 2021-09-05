package myAdmin.core.log;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public class LogPackageHolder {

    private static final ThreadLocal<LogPackage> logPackageThreadLocal = new ThreadLocal<>();

    public static void init() {
        String traceToken = null;
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes != null && ((ServletRequestAttributes) attributes).getRequest() != null) {
            traceToken = ((ServletRequestAttributes) attributes).getRequest().getHeader(LogVariableKey.TRACE_TOKEN);
        }
        if (StringUtils.isBlank(traceToken)) {
            traceToken = UUID.randomUUID().toString();
        }
        LogPackage logPackage = new LogPackage(traceToken);
        logPackageThreadLocal.set(logPackage);
    }

    protected static LogPackage getLogPackage() {
        return logPackageThreadLocal.get();
    }

    public static void putVariable(String key, Object value) {
        LogPackage logPackage = getLogPackage();
        if (logPackage != null) {
            logPackage.putVariable(key, value);
        }
    }

    public static void appendVariable(String key, Object value) {
        LogPackage logPackage = getLogPackage();
        if (logPackage != null) {
            logPackage.appendVariable(key, value);
        }
    }

    public static void addSQL(String sql) {
        LogPackage logPackage = getLogPackage();
        if (logPackage != null) {
            LogOperation logOperation=logPackage.getLogOperation();
            if(logOperation!=null&&logOperation.skipSql()) {
                return;
            }
            logPackage.addSQL(sql);
        }
    }

    public static long getCurrentConsumeTime() {
        LogPackage logPackage = getLogPackage();
        if (logPackage != null) {
            return logPackage.getCurrentConsumeTime();
        }
        return -1;
    }

    public static final String getCurrentTraceToken() {
        LogPackage logPackage = getLogPackage();
        if (logPackage != null) {
            return logPackage.getTraceToken();
        }
        return null;
    }

    public static final void setCurrentTraceToken(String traceToken) {
        LogPackage logPackage = getLogPackage();
        if (logPackage != null) {
            logPackage.setTraceToken(traceToken);
        }
    }

    public static void release() {
        logPackageThreadLocal.set(null);
    }

    public static void compensateLogException(Throwable throwable) {
        try {
            LogPackage logPackage = getLogPackage();
            if (logPackage == null) {
                LogPackageHolder.init();
                logPackage = LogPackageHolder.getLogPackage();
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    HttpServletRequest request = attributes.getRequest();
                    logPackage.putVariable(LogVariableKey.URL, request.getRequestURL().toString());
                    logPackage.putVariable(LogVariableKey.HTTP_METHOD, request.getMethod());
                    logPackage.putVariable(LogVariableKey.IP, request.getRemoteAddr());
                }
                logPackage.setThrowable(throwable);
                logPackage.print();
            }
        } finally {
            release();
        }
    }
}
