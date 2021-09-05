package myAdmin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密算法 用于用户密码的加密
 */
public class Md5Util {

    private static final String DEFAULT_ENCODING = "utf-8";

    private static final Logger LOG = LoggerFactory.getLogger(Md5Util.class);

    // 加密函数
    public final static String MD5(String s) {
        return MD5(s, "");
    }

    private static String MD5(String strSrc, String key) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] b = strSrc.getBytes(DEFAULT_ENCODING);

            md5.update(b);

            String result = "";
            byte[] temp = md5.digest(key.getBytes(DEFAULT_ENCODING));
            for (byte value : temp) {
                result += Integer.toHexString((0x000000ff & value) | 0xffffff00).substring(6);
            }
            return result;

        } catch (Exception e) {
            LOG.error("md5加密异常", e);
        }
        return "";
    }
}
