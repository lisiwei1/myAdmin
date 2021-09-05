package myAdmin.core.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AdminInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private AdminInterceptor adminInterceptor;

    /**
     * 注册拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //addInterceptor 注册拦截器
        //addPathPatterns 配置拦截规则
        List<String> patterns = new ArrayList<>();
        //TODO 读配置文件
        patterns.add("/admin/login/loginUser");

        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(patterns);
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
