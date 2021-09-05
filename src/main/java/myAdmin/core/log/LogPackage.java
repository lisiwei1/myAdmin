package myAdmin.core.log;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import myAdmin.common.ResultCode;
import myAdmin.core.exception.BusinessException;
import myAdmin.core.exception.CoreException;
import myAdmin.util.ExceptionUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Data
public class LogPackage {

    private Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    private Gson reqRspGson = new GsonBuilder().disableHtmlEscaping().create();

    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    private String traceToken;

    private Map<String, Object> variables = Maps.newHashMap();

    private List<String> sqls = Lists.newArrayList();

    private long startTime;

    private String isPrint;

    private Throwable throwable;

    private LogOperation logOperation;

    public LogPackage(String traceToken) {
        this.traceToken = traceToken;
        this.startTime = System.currentTimeMillis();
    }

    public void putVariable(String key, Object value) {
        add(key, value, false);
    }

    public void appendVariable(String key, Object value) {
        add(key, value, true);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void add(String key, Object value, boolean isAppend) {
        if (logOperation != null) {
            if (LogVariableKey.REQUEST_PARAMS.equals(key) && logOperation.skipReq()) {
                return;
            }
            if (LogVariableKey.RESPONSE_PARAMS.equals(key) && logOperation.skipRsp()) {
                return;
            }
        }
        if (isAppend) {
            Object currentValue = variables.get(key);
            if (currentValue != null) {
                if (currentValue instanceof List) {
                    List currentValueList = (List) currentValue;
                    currentValueList.add(value);
                } else {
                    variables.put(key, Lists.newArrayList(currentValue, value));
                }
            } else {
                variables.put(key, Lists.newArrayList(value));
            }
        } else {
            variables.put(key, value);
        }
    }

    public void addSQL(String sql) {
        sqls.add(sql);
    }

    public long getCurrentConsumeTime() {
        return getCurrentConsumeTime(System.currentTimeMillis()) - startTime;
    }

    public long getCurrentConsumeTime(long currentTime) {
        return currentTime - startTime;
    }

    public void print() {
        Map<String, Object> printObject = Maps.newHashMap();
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            Object value = entry.getValue();
            if (!(value instanceof Number || value instanceof CharSequence||value instanceof List)) {
                printObject.put(entry.getKey(), reqRspGson.toJson(value));
            } else {
                printObject.put(entry.getKey(), value);
            }
        }
        boolean isShowThrowable = true;
        if (throwable instanceof BusinessException) {
            isShowThrowable = false;
            BusinessException businessException = (BusinessException) throwable;
            Map<String, Object> responseParams = Maps.newHashMap();
            responseParams.put(LogVariableKey.CODE, businessException.getCode());
            responseParams.put(LogVariableKey.MESSAGE, businessException.getMessage());
            printObject.put(LogVariableKey.RESPONSE_PARAMS, reqRspGson.toJson(responseParams));

        }
        if (throwable instanceof CoreException) {
            CoreException coreException = (CoreException) throwable;
            Map<String, Object> responseParams = Maps.newHashMap();
            responseParams.put(LogVariableKey.CODE, coreException.getCode());
            responseParams.put(LogVariableKey.MESSAGE, coreException.getMessage());
            printObject.put(LogVariableKey.RESPONSE_PARAMS, reqRspGson.toJson(responseParams));
        }
        if (throwable instanceof Throwable) {
            Map<String, Object> responseParams = Maps.newHashMap();
            responseParams.put(LogVariableKey.CODE, ResultCode.FAILED.getCode());
            responseParams.put(LogVariableKey.MESSAGE, throwable.getMessage());
            printObject.put(LogVariableKey.RESPONSE_PARAMS,reqRspGson.toJson( responseParams));
        }
        if (isShowThrowable) {
            String throwableToString = ExceptionUtil.throwableToString(throwable);
            if (throwableToString != null) {
                printObject.put(LogVariableKey.THROWABLE, throwableToString);
            }
        }
        printObject.put(LogVariableKey.SQLS, sqls);
        printObject.put(LogVariableKey.REQUEST_TIME, DateFormatUtils.format(startTime, DATE_FORMAT_PATTERN));
        long nowTime = System.currentTimeMillis();
        printObject.put(LogVariableKey.RESPONSE_TIME, DateFormatUtils.format(nowTime, DATE_FORMAT_PATTERN));
        printObject.put(LogVariableKey.CONSUME_TIME, getCurrentConsumeTime(nowTime));
        printObject.put(LogVariableKey.TRACE_TOKEN, traceToken);
        LoggerFactory.getLogger("LogPackage").info(gson.toJson(printObject));
    }
}
