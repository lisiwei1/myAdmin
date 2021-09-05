package myAdmin.core.interceptor;

/**
 * 自定义线程的ThreadLocal，记录userid
 */
public class MyInterceptorContextHolder {

    private static final ThreadLocal<Long> Request_User_Context_Holder = new ThreadLocal<>();

    public static void setUserId(Long userId){
        Request_User_Context_Holder.set(userId);
    }

    public static Long getUserId(){
        return Request_User_Context_Holder.get();
    }

    public static void clear(){
        Request_User_Context_Holder.remove();
    }

}
