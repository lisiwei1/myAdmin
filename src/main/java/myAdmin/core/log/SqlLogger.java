package myAdmin.core.log;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.FormattedLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlLogger extends FormattedLogger {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void logException(Exception e) {
        logger.error(e.getMessage(), e);
    }

    @Override
    public void logText(String text) {
        LogPackageHolder.addSQL(text);
    }

    @Override
    public boolean isCategoryEnabled(Category category) {
        return true;
    }
}
