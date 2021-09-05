package myAdmin.starter;

import myAdmin.core.log.systemprint.SystemPrintSlf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableAsync
@ServletComponentScan
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@EntityScan(basePackages = "myAdmin")
@EnableJpaRepositories(basePackages = "myAdmin")
@ComponentScan(basePackages = {"myAdmin"})
public class MyAdminApplication {

	public static void main(String[] args) {
		SystemPrintSlf4j.init();
		SpringApplication.run(MyAdminApplication.class, args);
	}

}
