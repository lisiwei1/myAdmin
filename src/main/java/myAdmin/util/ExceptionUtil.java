package myAdmin.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {

    public static String throwableToString(Throwable throwable) {
        if (throwable != null) {
            StringWriter sw = new StringWriter();
            try (PrintWriter pw = new PrintWriter(sw);) {
                throwable.printStackTrace(pw);
            }
            return sw.toString();
        }
        return null;
    }
}
