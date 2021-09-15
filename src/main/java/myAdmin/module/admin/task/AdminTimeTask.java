package cn.gd.winning.hpsp.module.admin.task;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.gd.winning.hpsp.module.admin.service.AdminConfigService;

@Component
public class AdminTimeTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminTimeTask.class);
	
	@Autowired
	private AdminConfigService configService;

	@PostConstruct //项目启动时自动执行一次
	public void actionConfigDataToCache() {
		LOGGER.info("开始查数据库的配置表数据缓存到内存");
		configService.actionConfigToCache();
		LOGGER.info("配置表数据缓存完毕");
	}
	
}
