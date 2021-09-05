package myAdmin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "admin")
@PropertySource(value = "application.properties")
@Data
public class AdminConfigure {

    //token有效时间，单位秒
    private Integer expires;
    private String secretKey;
    //加盐值
    private String salt;
    private String aesKey;
    private String authorityKey;
    private String userId;
    private String initPassword;

}
