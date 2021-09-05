package myAdmin.core.request;

/**
 * 自定义请求线程的ThreadLocal
 */
public class MyRequestContextHolder {

    private static final ThreadLocal<String> Request_Context_Holder = new ThreadLocal<>();

    public static void setIp(String ip){
        Request_Context_Holder.set(ip);
    }

    public static String getIp(){
        return Request_Context_Holder.get();
    }

    public static void clear(){
        Request_Context_Holder.remove();
    }

}
