package myAdmin.core.swagger;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @author ljr
 *
 *         2021年3月2日
 */
@ConfigurationProperties(prefix = "swagger")
@Component
@Data
public class SwaggerConfigProperties {
	/**
	 * 是否开启Swagger
	 */
	private boolean enable = false;
	/**
	 * 要扫描的包
	 */
	private String packageScan;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 版本信息
	 */
	private String version;
	
}
